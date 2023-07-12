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
import java.util.Objects;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.QuestState;
import net.runelite.api.Skill;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.WidgetLoaded;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.api.widgets.WidgetType;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

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

	private ArrayList<ByReleaseQuest> questList;
	private HashMap<String, List<Widget>> skillWidgets;
	private int currentDate;
	private int previousDate;


	@Override
	protected void startUp() throws Exception
	{
	}

	@Override
	protected void shutDown() throws Exception
	{
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged gameStateChanged)
	{
		if(gameStateChanged != null)
		{
			if (gameStateChanged.getGameState() == GameState.LOGGED_IN)
			{
				clientThread.invokeLater(this::createQuestList);
			}
		}
	}

	private void createQuestList()
	{
		questList = new ArrayList<>();
		questList.addAll(Arrays.asList(ByReleaseQuest.values()));
		for (ByReleaseQuest byReleaseQuest: questList)
		{
			byReleaseQuest.setComplete(byReleaseQuest.getQuest().getState(client) == QuestState.FINISHED);
		}
		updateCurrentDate();
		if (previousDate < currentDate)
		{
			//this is where every thing would be updated.
			System.out.println("create skill widgets");
			createSkillWidgets();
		}
	}

	private boolean canStartQuest(ByReleaseQuest quest)
	{
		return currentDate >= quest.getReleaseDate();
	}

	private void updateCurrentDate() {
		for (int i = 0; i < questList.size(); i++)
		{
			ByReleaseQuest quest = questList.get(i);
			if (!quest.isComplete())
			{
				if (i > 0 && questList.get(i - 1).isComplete())
				{
					previousDate = currentDate;
					currentDate = quest.getReleaseDate();
				}
				break;
			}
		}
	}

	@Subscribe
	public void onWidgetLoaded(WidgetLoaded widgetLoaded)
	{
		if (widgetLoaded.getGroupId() == WidgetInfo.SKILLS_CONTAINER.getGroupId())
		{
			createSkillWidgets();
		}
	}

	@Provides
	ByReleaseConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(ByReleaseConfig.class);
	}

	private void createSkillWidgets()
	{
		ArrayList<ByReleaseSkill> skillsList = new ArrayList<>(Arrays.asList(ByReleaseSkill.values()));
		skillWidgets = new HashMap<>();

		for (ByReleaseSkill skill : skillsList)
		{
			System.out.println("create skill widget: " + skill);
			addSkillWidget(skill.getSkill(), skill.getReleaseDate(), skill.getWidgetID());
		}
	}

	private void addSkillWidget(Skill skill, int releaseDate, int widgetID)
	{
		Widget skillWidget = client.getWidget(widgetID);

		if (skillWidget == null)
		{
			return;
		}

		boolean isSkillUnlocked = releaseDate <= currentDate;
		ArrayList<Widget> skillWidgetChildren = new ArrayList<>();

		Widget skillIconWidget = skillWidget.createChild(-1, WidgetType.GRAPHIC);
		Widget skillLevelWidget = skillWidget.createChild(-1, WidgetType.GRAPHIC);

		skillLevelWidget.setSpriteId(176);
		skillLevelWidget.setSize(36, 36);
		skillLevelWidget.setPos(28, -2);
		skillLevelWidget.setOpacity(90);
		skillLevelWidget.setHidden(isSkillUnlocked);

		skillIconWidget.setSpriteId(174);
		skillIconWidget.setSize(36, 36);
		skillIconWidget.setPos(-2, -2);
		skillIconWidget.setOpacity(90);
		skillIconWidget.setHidden(isSkillUnlocked);

		skillWidgetChildren.add(skillIconWidget);
		skillWidgetChildren.add(skillLevelWidget);
		skillWidgets.put(skill.getName(), skillWidgetChildren);
	}
}