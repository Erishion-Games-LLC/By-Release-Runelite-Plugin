/*
 *  BSD 2-Clause License
 *  * Copyright (c) 2024, Erishion Games LLC <https://github.com/Erishion-Games-LLC>
 *  * All rights reserved.
 *  *
 *  * Redistribution and use in source and binary forms, with or without
 *  * modification, are permitted provided that the following conditions are met:
 *  *
 *  * 1. Redistributions of source code must retain the above copyright notice, this
 *  *    list of conditions and the following disclaimer.
 *  * 2. Redistributions in binary form must reproduce the above copyright notice,
 *  *    this list of conditions and the following disclaimer in the documentation
 *  *    and/or other materials provided with the distribution.
 *  *
 *  * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 *  * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 *  * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 *  * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 *  * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 *  * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 *  * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 *  * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 *  * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 */
package com.erishiongamesllc.byrelease.managers;

import com.erishiongamesllc.byrelease.ByReleaseConfig;
import com.erishiongamesllc.byrelease.data.enums.ByReleaseQuest;
import com.erishiongamesllc.byrelease.data.classes.ScriptIds;
import com.erishiongamesllc.byrelease.handlers.OverlayHandler;
import com.erishiongamesllc.byrelease.handlers.WidgetHandler;
import com.erishiongamesllc.regionlocker.RegionLocker;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.Getter;
import net.runelite.api.QuestState;
import net.runelite.api.ScriptID;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.ScriptPostFired;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.eventbus.Subscribe;

@Singleton
public class DateManager
{
	private final ByReleaseConfig byReleaseConfig;
	private final ClientThread clientThread;
	private final WidgetHandler widgetHandler;
	private final OverlayHandler overlayHandler;
	private final QuestManager questManager;
	private final int startingDate = ByReleaseQuest.COOKS_ASSISTANT.getReleaseDate();


	@Getter
	private boolean isSetUpCompleted = false;
	@Getter
	private int currentDate = startingDate;
	private int previousDate = 0;


	@Inject
	private DateManager(ByReleaseConfig byReleaseConfig, ClientThread clientThread, WidgetHandler widgetHandler, OverlayHandler overlayHandler, QuestManager questManager)
	{
		this.byReleaseConfig = byReleaseConfig;
		this.clientThread = clientThread;
		this.widgetHandler = widgetHandler;
		this.overlayHandler = overlayHandler;
		this.questManager = questManager;
	}


	@Subscribe
	public void onGameTick(GameTick gameTick)
	{
		if (!isSetUpCompleted)
		{
			clientThread.invokeLater(this::setUp);
		}
	}

	//only call on client thread
	private void setUp()
	{
		if (isSetUpCompleted)
		{
			return;
		}
		questManager.updateQuestStates();
		updateCurrentDate();
		overlayHandler.update(currentDate);
		widgetHandler.setUp(currentDate);
		isSetUpCompleted = true;
	}

	public void shutDown()
	{
		isSetUpCompleted = false;
		currentDate = 20010104;
		previousDate = 0;
	}

	@Subscribe
	public void onScriptPostFired(ScriptPostFired scriptPostFired)
	{
		if (!isSetUpCompleted)
		{
			return;
		}
		switch (scriptPostFired.getScriptId())
		{
			case ScriptIds.CLIENT_QUEST_LIST_DRAW:
			case ScriptID.QUESTLIST_INIT:
				clientThread.invokeLater(this::update);
				break;
		}
	}

	//only call on client thread
	public void update()
	{
		questManager.updateQuestStates();
		updateCurrentDate();
		//if current date does not match previous date, then we have the potential to have unlocked new things. Signal the other sections to run their checks with the new date
		//in normal play it will only ever increase, however current date could be less than previous date if using the date override setting in config
		if (currentDate != previousDate)
		{
			RegionLocker.updateReleasedRegions(currentDate);
			widgetHandler.update();
			overlayHandler.update(currentDate);
		}
	}

	public void updateCurrentDate()
	{
		if (byReleaseConfig.overrideDate())
		{
			setCurrentDateByOverride();
			return;
		}
		setCurrentDateByQuestCompletion();
	}

	private void setCurrentDateByOverride()
	{
		//if the user supplied override date is less than the startingDate of RSC, floor the currentDate to the startingDate.
		if (byReleaseConfig.date() < startingDate)
		{
			currentDate = startingDate;
			return;
		}
		currentDate = byReleaseConfig.date();
	}

	private void setCurrentDateByQuestCompletion()
	{
		//find the first quest that is not completed. Set the current date to the last quest that was completed.
		ByReleaseQuest lastCompletedQuest = null;

		for (ByReleaseQuest quest : ByReleaseQuest.values())
		{
			//if the quest is finished, save a reference to it and continue through the loop until you find the first incomplete quest
			if (questManager.getQuestStates(quest) == QuestState.FINISHED)
			{
				lastCompletedQuest = quest;
				continue;
			}

			//if latest quest is equal to null, that means that the first quest is not completed. So set the currentDate to the starting date.
			if (questManager.getQuestStates(quest) != QuestState.FINISHED && lastCompletedQuest == null)
			{
				currentDate = startingDate;
				return;
			}

			if (questManager.getQuestStates(quest) != QuestState.FINISHED)
			//if latest quest is not equal to null, then we have found the first noncompleted quest after a completed quest. So we should set the current date to be equal to the last completed quest
			{
				//asset lastCompletedQuest is not null to make the compiler happy. It cannot ever be null at this point, as it would be caught in the above code block.
				assert lastCompletedQuest != null;
				currentDate = lastCompletedQuest.getReleaseDate();
				return;
			}
		}
	}
}