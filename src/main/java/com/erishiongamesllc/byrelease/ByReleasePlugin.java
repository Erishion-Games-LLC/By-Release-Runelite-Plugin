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

import static com.erishiongamesllc.byrelease.ByReleasePlugin.PLUGIN_NAME;
import com.erishiongamesllc.byrelease.data.ByReleaseAnvil;
import com.erishiongamesllc.byrelease.data.ByReleasePrayer;
import com.erishiongamesllc.byrelease.data.ByReleaseQuest;
import com.erishiongamesllc.byrelease.data.ByReleaseSkill;
import com.erishiongamesllc.byrelease.data.ByReleaseStandardSpell;
import com.erishiongamesllc.byrelease.data.ByReleaseTree;
import com.erishiongamesllc.byrelease.data.MenuOptions;
import com.google.inject.Provides;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.inject.Inject;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.QuestState;
import net.runelite.api.ScriptID;
import net.runelite.api.Tile;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.api.events.ScriptPostFired;
import net.runelite.api.events.VarbitChanged;
import net.runelite.api.events.WidgetLoaded;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetID;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.api.widgets.WidgetType;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.util.Text;

@Slf4j
@PluginDescriptor
	(
	name = PLUGIN_NAME
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

	public static final String PLUGIN_NAME = "By Release";
	public static final String CONFIG_GROUP = "byrelease";

	private final ArrayList<ByReleaseQuest> questList = new ArrayList<>(Arrays.asList(ByReleaseQuest.values()));
	private final HashMap<String, List<Widget>> skillWidgets = new HashMap<>();

	private final ArrayList<ByReleasePrayer> prayersFromMagicRSC = new ArrayList<>(Arrays.asList(ByReleasePrayer.THICK_SKIN, ByReleasePrayer.BURST_OF_STRENGTH, ByReleasePrayer.ROCK_SKIN));
	private final ArrayList<ByReleaseStandardSpell> spellsFromInitialRSC = new ArrayList<>(Arrays.asList(ByReleaseStandardSpell.WIND_STRIKE, ByReleaseStandardSpell.CONFUSE, ByReleaseStandardSpell.WATER_STRIKE));

	private final Set<String> nonReleasedPrayerNames = new HashSet<>();
	private final Set<String> nonReleasedSkillNames = new HashSet<>();
	private final Set<String> nonReleasedSpellNames = new HashSet<>();

	private boolean setUpCompleted = false;

	@Getter
	private int currentDate = 20010104;
	private int previousDate = 0;




	@Override
	protected void startUp() throws Exception
	{
		overlayManager.add(overlay);
	}

	@Override
	protected void shutDown() throws Exception
	{
		overlayManager.remove(overlay);
		clientThread.invokeLater(this::removeSkillWidgets);
		previousDate = 0;
		setUpCompleted = false;
		nonReleasedPrayerNames.clear();
		nonReleasedSkillNames.clear();
		nonReleasedSpellNames.clear();
		currentDate = 20010104;
		clientThread.invokeLater(this::restoreDefaultPrayerWidgets);
//		clientThread.invokeLater(this::restoreDefaultSpellWidgets);
	}

	@Subscribe
	public void onGameTick(GameTick gameTick)
	{
		if (!setUpCompleted)
		{
			clientThread.invokeLater(this::setUp);
		}
	}

	@Subscribe
	public void onWidgetLoaded(WidgetLoaded widgetLoaded)
	{
		if (widgetLoaded.getGroupId() == WidgetInfo.RESIZABLE_VIEWPORT_PRAYER_TAB.getGroupId() || widgetLoaded.getGroupId() == WidgetInfo.FIXED_VIEWPORT_PRAYER_TAB.getGroupId())
		{
			updatePrayerWidgets();
		}
		if (widgetLoaded.getGroupId() == WidgetID.PRAYER_GROUP_ID)
		{
			Widget temp = client.getWidget(WidgetID.PRAYER_GROUP_ID, 3);
			assert temp != null;
			updatePrayerWidgets();
		}
	}

	@Subscribe
	public void onScriptPostFired(ScriptPostFired scriptPostFired)
	{
		if (!setUpCompleted)
		{
			return;
		}

		final int clientScriptQuestListDrawID = 1340;

		switch (scriptPostFired.getScriptId())
		{
			//quests
			case clientScriptQuestListDrawID:
			case ScriptID.QUESTLIST_INIT:
				clientThread.invokeLater(this::update);
				break;

			//prayers
			case 461:
			case 2760:
			case ScriptID.PRAYER_UPDATEBUTTON:
			case ScriptID.PRAYER_REDRAW:
			case ScriptID.QUICKPRAYER_INIT:
				updatePrayerWidgets();
				break;

			//spells
//			case 2262:
//			case 2607:
//			case 2609:
//			case 2610:
//			case 2617:
//				updateSpellWidgets();
		}
	}

	@Subscribe
	public void onMenuOptionClicked(MenuOptionClicked menuOptionClicked)
	{
		String menuOption = menuOptionClicked.getMenuOption();
		String menuTarget = Text.removeTags(menuOptionClicked.getMenuTarget());
		switch (menuOption)
		{
			case "Activate":
			case "Toggle":
				if (nonReleasedPrayerNames.contains(menuTarget))
				{
					createUnavailableMessage(menuTarget, MenuOptions.TOGGLE);
					menuOptionClicked.consume();
				}
				break;
			case "Cast":
				if (nonReleasedSpellNames.contains(menuTarget))
				{
					createUnavailableMessage(menuTarget, MenuOptions.CAST);
					menuOptionClicked.consume();
				}
				break;
			case "Seers'":
			case "Yanille":
				if (currentDate < 20150305)
				{
					menuOptionClicked.consume();
					client.addChatMessage(ChatMessageType.GAMEMESSAGE,
						"", menuOption + " teleport is unavailable until: " + 20150305, null);
				}
				break;
			case "Grand Exchange":
				if (currentDate < 20150430)
				{
					menuOptionClicked.consume();
					client.addChatMessage(ChatMessageType.GAMEMESSAGE,
						"", "Grand Exchange teleport is unavailable until: " + 20150430, null);
				}
				break;
			case "Smelt":
				if (menuOptionClicked.getId() == 39620 && currentDate < 20200709)
				{
					createUnavailableMessage(menuTarget, MenuOptions.SMELT);
					menuOptionClicked.consume();
				}
				break;
			case "Chop down":
				handleChopDown(menuTarget, menuOptionClicked);
				break;
			case "Smith":
			{
				System.out.println("HANDLE SMITH OUT");
				handleSmith(menuTarget, menuOptionClicked);
				break;
			}
		}
	}

	@Subscribe
	public void onVarbitChanged(VarbitChanged varbitChanged)
	{
		if (varbitChanged.getVarbitId() == 4070)
		{
			updateSpellWidgets();
		}
	}

	@Subscribe
	public void onConfigChanged(ConfigChanged configChanged)
	{
		if (configChanged.getGroup().equals(CONFIG_GROUP))
		{
			System.out.println("CONFIG HAS CHANGED");
			switch (configChanged.getKey())
			{
				case "spellsFromInitialRSC":
				case "prayersFromMagicRSC":
					clientThread.invokeLater(this::setUp);
					break;
			}
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
		updateQuestList();

		updateCurrentDate();

		createSkillWidgets();

		updateSkillWidgets();

		updatePrayerWidgets();

		updateSpellWidgets();

//		updateObjectsAvailability();

		setUpCompleted = true;
	}

	private void createSkillWidgets()
	{
		for (ByReleaseSkill skill : ByReleaseSkill.values())
		{
			Widget skillWidget = client.getWidget(skill.getWidgetID());

			if (skillWidget == null)
			{
				return;
			}

			ArrayList<Widget> skillWidgetChildren = new ArrayList<>();

			Widget skillIconWidget = skillWidget.createChild(50, WidgetType.GRAPHIC);
			Widget skillLevelWidget = skillWidget.createChild(51, WidgetType.GRAPHIC);


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
			updatePrayerWidgets();
			updateSpellWidgets();
//			updateObjectsAvailability();
		}
	}

	private void updateQuestList()
	{
		for (ByReleaseQuest byReleaseQuest: questList)
		{
			byReleaseQuest.setQuestState
				(
				byReleaseQuest.getQuest().getState(client)
				);
		}
	}

	private void updateCurrentDate()
	{
		ByReleaseQuest previousQuest = null;
		for (ByReleaseQuest quest : questList)
		{
			if (quest.getQuestState() != QuestState.FINISHED)
			{
				if (previousQuest != null && previousQuest.getQuestState() == QuestState.FINISHED)
				{
					previousDate = currentDate;
					currentDate = quest.getReleaseDate();
					return;
				}
			}
			previousQuest = quest;
		}
	}

	private void updateSkillWidgets()
	{

		for (ByReleaseSkill skill : ByReleaseSkill.values())
		{
			for (Widget skillWidgetChild : skillWidgets.get(skill.getSkill().getName()))
			{
				if (skill.getReleaseDate() > currentDate)
				{
					skillWidgetChild.setHidden(false);
					nonReleasedSkillNames.add(skill.getSkill().getName());
				}
				else
				{
					skillWidgetChild.setHidden(true);
					nonReleasedSkillNames.remove(skill.getSkill().getName());
				}
			}
		}
	}

	private void updatePrayerWidgets()
	{
		for (ByReleasePrayer prayer : ByReleasePrayer.values())
		{
			Widget individualPrayerWidgetContainer = client.getWidget(prayer.getWidgetID());

			if (individualPrayerWidgetContainer == null)
			{
				continue;
			}
			//prayer is not released
			if (prayer.getReleaseDate() > currentDate)
			{
				if (config.prayersFromMagicRSC() && prayersFromMagicRSC.contains(prayer))
				{
					if (prayer == ByReleasePrayer.ROCK_SKIN && currentDate < 20010127)
					{
						System.out.println("HIDE ROCK SKIN: " + currentDate);
						//prayer is rock skin, which was released Jan 27 2001.
						individualPrayerWidgetContainer.setHidden(true);
						nonReleasedPrayerNames.add(prayer.getName());
					}
					else
					{
						individualPrayerWidgetContainer.setHidden(false);
						nonReleasedPrayerNames.remove(prayer.getName());
					}
				}
				else
				{
					individualPrayerWidgetContainer.setHidden(true);
					nonReleasedPrayerNames.add(prayer.getName());
				}
			} else
			{
				individualPrayerWidgetContainer.setHidden(false);
				nonReleasedPrayerNames.remove(prayer.getName());

			}
			individualPrayerWidgetContainer.revalidate();
		}
	}

	private void removeSkillWidgets()
	{
		for (ByReleaseSkill skill : ByReleaseSkill.values())
		{
			Widget skillWidget = client.getWidget(skill.getWidgetID());
			if (skillWidget == null)
			{
				continue;
			}
			skillWidget.deleteAllChildren();
			client.createScriptEvent(skillWidget.getOnLoadListener()).setSource(skillWidget).run();
		}
		skillWidgets.clear();
	}

	private void restoreDefaultPrayerWidgets()
	{
		Widget widget = client.getWidget(35454976);
		if (widget == null)
		{
			return;
		}
		client.createScriptEvent(widget.getOnLoadListener()).setSource(widget).run();
	}

	//works, need to fix issue where spells are not being displayed properly if dont have runes but is past released date
	private void restoreDefaultSpellWidgets()
	{
		Widget widget = client.getWidget(14286848);
		if (widget == null)
		{
			return;
		}
		client.createScriptEvent(widget.getOnLoadListener()).setSource(widget).run();
	}

	//Varbit 4070
	//0 standard
	//1 ancient
	//2 lunar
	//3 arceuus
	private void updateSpellWidgets()
	{
		System.out.println("update spell widgets");
		switch (client.getVarbitValue(4070))
		{
			case 0:
				updateStandardSpellbook();
				break;
			case 1:
				updateAncientSpellbook();
				break;
		}
	}

	private void updateStandardSpellbook()
	{
		System.out.println("updated standard spell book");
		for (ByReleaseStandardSpell spell : ByReleaseStandardSpell.values())
		{
			Widget spellWidget = client.getWidget(spell.getWidgetID());
			if (spellWidget == null)
			{
				System.out.println("spell widget is null for: " + spell.getName());
				continue;
			}
			//spell is not released
			if (spell.getReleaseDate() > currentDate)
			{
				if(config.spellsFromInitialRSC() && spellsFromInitialRSC.contains(spell))
				{
					if (spell == ByReleaseStandardSpell.WATER_STRIKE && currentDate < 20010127)
					{
//						spellWidget.setSpriteId(spell.getLockedSpriteID());
						nonReleasedSpellNames.add(spell.getName());
					}
					else
					{
//						spellWidget.setSpriteId(spell.getUnlockedSpriteID());
						nonReleasedSpellNames.remove(spell.getName());
					}
				}
				else
				{
//					spellWidget.setSpriteId(spell.getLockedSpriteID());
					nonReleasedSpellNames.add(spell.getName());
				}
			}
			else
			{
//				spellWidget.setSpriteId(spell.getUnlockedSpriteID());
				nonReleasedSpellNames.remove(spell.getName());
			}
		}
	}

	private void updateAncientSpellbook()
	{
		System.out.println("updated ancient spell book");
	}

	private void createUnavailableMessage(String target, MenuOptions type)
	{
		switch (type)
		{
			case TOGGLE:
				for (ByReleasePrayer prayer : ByReleasePrayer.values())
				{
					if (prayer.getName().equals(target))
					{
						client.addChatMessage(ChatMessageType.GAMEMESSAGE,
							"", prayer.getName() + " is unavailable until: " + prayer.getReleaseDate(), null);
						break;
					}
				}
				break;
			case CAST:
				for (ByReleaseStandardSpell spell : ByReleaseStandardSpell.values())
				{
					if (spell.getName().equals(target))
					{
						client.addChatMessage(ChatMessageType.GAMEMESSAGE,
							"", spell.getName() + " is unavailable until: " + spell.getReleaseDate(), null);
						break;
					}
				}
				break;
			case CHOP_DOWN:
				for (ByReleaseTree tree : ByReleaseTree.values())
				{
					if (tree.getName().equals(target))
					{
						client.addChatMessage(ChatMessageType.GAMEMESSAGE,
							"", tree.getName() + "is unavailable until: " + tree.getReleaseDate(), null);
						break;
					}
				}
				break;
		}
	}

	private void handleChopDown(String menuTarget, MenuOptionClicked menuOptionClicked)
	{
		for (ByReleaseTree tree : ByReleaseTree.values())
		{
			if (tree.getName().equals(menuTarget) && tree.getReleaseDate() > currentDate)
			{
				menuOptionClicked.consume();
				createUnavailableMessage(menuTarget, MenuOptions.CHOP_DOWN);
			}
		}
	}

	private void handleSmith(String menuTarget, MenuOptionClicked menuOptionClicked)
	{
		if (menuTarget.equals("Anvil") && config.enableAnvils())
		{
			final Tile tile = client.getScene().getTiles()[client.getPlane()][menuOptionClicked.getParam0()][menuOptionClicked.getParam1()];
			final WorldPoint location = tile.getWorldLocation();

			for (ByReleaseAnvil anvil : ByReleaseAnvil.values())
			{
				if (location.equals(anvil.getLocation()) && anvil.getReleaseDate() > currentDate)
				{
					menuOptionClicked.consume();
					client.addChatMessage(ChatMessageType.GAMEMESSAGE,
						"", "This anvil is unavailable until: " + anvil.getReleaseDate(), null);
				}
			}
		}
	}
}