/* BSD 2-Clause License
 * Copyright (c) 2023, Erishion Games LLC <https://github.com/Erishion-Games-LLC>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.erishiongamesllc.byrelease;

import com.erishiongamesllc.byrelease.data.ByReleaseQuest;
import com.erishiongamesllc.byrelease.data.ByReleaseSkill;
import com.google.inject.Provides;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.QuestState;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.ScriptPostFired;
import net.runelite.api.events.WidgetLoaded;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.api.widgets.WidgetType;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

@Slf4j
@PluginDescriptor
	(
	name = "ByRelease"
	)
public class ByReleasePlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private ByReleaseConfig config;

	@Inject
	private ClientThread clientThread;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private ByReleaseOverlay overlay;

	private final ArrayList<ByReleaseQuest> questList = new ArrayList<>(Arrays.asList(ByReleaseQuest.values()));
	private final HashMap<String, List<Widget>> skillWidgets = new HashMap<>();
	private boolean setUpCompleted = false;
	private int currentDate;
	private int previousDate;


	@Override
	protected void startUp() throws Exception
	{
		overlayManager.add(overlay);
	}

	@Override
	protected void shutDown() throws Exception
	{
		overlayManager.remove(overlay);
	}

	@Subscribe
	public void onGameTick(GameTick gameTick)
	{
		if (!setUpCompleted)
		{
			setUpCompleted = true;
			clientThread.invokeLater(this::setUp);
		}
	}

	@Subscribe
	public void onWidgetLoaded(WidgetLoaded widgetLoaded)
	{
		if (widgetLoaded.getGroupId() == WidgetInfo.SKILLS_CONTAINER.getGroupId())
		{
			System.out.println("WIDGET LOADED");
//			createSkillWidgets();
		}
	}

	@Subscribe
	public void onScriptPostFired(ScriptPostFired scriptPostFired)
	{
		int clientScriptQuestListDrawID = 1340;
		if(scriptPostFired.getScriptId() == clientScriptQuestListDrawID)
		{
			clientThread.invokeLater(this::update);
		}
	}

	@Provides
	ByReleaseConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(ByReleaseConfig.class);
	}

	//only call on client thread
	private void setUp()
	{
		createSkillWidgets();
		update();
	}

	private void createSkillWidgets()
	{
		for (ByReleaseSkill skill : ByReleaseSkill.values())
		{
			Widget skillWidget = client.getWidget(skill.getWidgetID());

			if (skillWidget == null)
			{
				System.out.println("SKILL WIDGET NULL: " + skill);
				return;
			}

			ArrayList<Widget> skillWidgetChildren = new ArrayList<>();

			Widget skillIconWidget = skillWidget.createChild(-1, WidgetType.GRAPHIC);
			Widget skillLevelWidget = skillWidget.createChild(-1, WidgetType.GRAPHIC);

			skillIconWidget.setSpriteId(174);
			skillIconWidget.setSize(36, 36);
			skillIconWidget.setPos(-2, -2);
			skillIconWidget.setOpacity(90);
			skillIconWidget.setHidden(true);

			skillLevelWidget.setSpriteId(176);
			skillLevelWidget.setSize(36, 36);
			skillLevelWidget.setPos(28, -2);
			skillLevelWidget.setOpacity(90);
			skillLevelWidget.setHidden(true);

			skillWidgetChildren.add(skillIconWidget);
			skillWidgetChildren.add(skillLevelWidget);
			skillWidgets.put(skill.getSkill().getName(), skillWidgetChildren);
		}
	}

	//only call on client thread
	private void update()
	{
		updateQuestList();
		updateCurrentDate();
		if (previousDate < currentDate)
		{
			//this is where every thing would be updated.
			updateSkillWidgets();
		}
	}

	private void updateQuestList()
	{
		for (ByReleaseQuest byReleaseQuest: questList)
		{
			System.out.println("Quest: " + byReleaseQuest.getQuest() + " " + byReleaseQuest.getQuest().getState(client));
			byReleaseQuest.setQuestState
				(
				byReleaseQuest.getQuest().getState(client)
				);
		}
	}

	private void updateCurrentDate()
	{
		for (int i = 0; i < questList.size(); i++)
		{
			ByReleaseQuest quest = questList.get(i);
			if (quest.getQuestState() != QuestState.FINISHED)
			{
				if (i > 0 && questList.get(i - 1).getQuestState() == QuestState.FINISHED)
				{
					previousDate = currentDate;
					currentDate = quest.getReleaseDate();
					System.out.println(questList.get(i).getQuestState());
				}
				break;
			}
		}
	}

	private void updateSkillWidgets()
	{
		for (ByReleaseSkill skill : ByReleaseSkill.values())
		{
			if (skill.getReleaseDate() > currentDate)
			{
				ArrayList<Widget> skillWidgetChildren = new ArrayList<>(skillWidgets.get(skill.getSkill().getName()));

				for (Widget skillWidgetChild : skillWidgetChildren)
				{
					skillWidgetChild.setHidden(false);
				}
			}
		}
	}

	public int getCurrentDate()
	{
		return currentDate;
	}
}