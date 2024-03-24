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

//import com.erishiongamesllc.groundmarkers.GroundMarkerHandler;
//import com.erishiongamesllc.groundmarkers.GroundMarkerMinimapOverlay;
//import com.erishiongamesllc.groundmarkers.GroundMarkerOverlay;
import static com.erishiongamesllc.byrelease.ByReleasePlugin.PLUGIN_NAME;
import com.erishiongamesllc.byrelease.data.ByReleaseItem;
import com.erishiongamesllc.byrelease.data.ByReleaseQuest;
import com.erishiongamesllc.byrelease.overlay.ByReleaseItemOverlay;
import com.erishiongamesllc.byrelease.overlay.ByReleaseDateOverlay;
import com.erishiongamesllc.regionlocker.RegionBorderOverlay;
import com.erishiongamesllc.regionlocker.RegionLocker;
import com.erishiongamesllc.regionlocker.RegionLockerOverlay;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.inject.Provides;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.HashMap;
import javax.inject.Inject;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.QuestState;
import net.runelite.api.ScriptID;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.ScriptPostFired;
import net.runelite.api.events.VarbitChanged;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

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
	private ByReleaseDateOverlay dateOverlay;
	@Inject
	private ByReleaseItemOverlay itemOverlay;
	@Inject
	private RegionLockerOverlay regionLockerOverlay;
	@Inject
	private RegionBorderOverlay regionBorderOverlay;
//	@Inject
//	private GroundMarkerOverlay groundMarkerOverlay;
//	@Inject
//	private GroundMarkerMinimapOverlay groundMarkerMinimapOverlay;

	@Inject
	private MenuOptionClickedHandler menuOptionClickedHandler;
//	@Inject
//	private GroundMarkerHandler groundMarkerHandler;
	@Inject
	private WidgetHandler widgetHandler;
	@Inject
	private EventBus eventBus;
	@Inject
	private Gson gson;


	public static final String PLUGIN_NAME = "By Release";
	public static final String CONFIG_GROUP = "byrelease";
	private RegionLocker regionLocker;

	private boolean setUpCompleted = false;

	@Getter
	private int currentDate = 20010104;
	private int previousDate = 0;



	@Override
	protected void startUp() throws Exception
	{
		regionLocker = new RegionLocker(config);
		loadDefinitions();

		eventBus.register(menuOptionClickedHandler);
		eventBus.register(widgetHandler);
//		eventBus.register(groundMarkerHandler);
//
//		groundMarkerHandler.loadPoints();

		addOverlays();
	}

	@Override
	protected void shutDown() throws Exception
	{
		eventBus.unregister(menuOptionClickedHandler);
		eventBus.unregister(widgetHandler);
//		eventBus.unregister(groundMarkerHandler);

		removeOverlays();

		clientThread.invokeLater(widgetHandler::removeSkillWidgets);
		previousDate = 0;
		setUpCompleted = false;

		widgetHandler.shutDown();

		currentDate = 20010104;
		clientThread.invokeLater(widgetHandler::restoreDefaultPrayerWidgets);
//		clientThread.invokeLater(this::restoreDefaultSpellWidgets);

//		groundMarkerHandler.clearPoints();
	}

	private void addOverlays()
	{
		overlayManager.add(dateOverlay);
		overlayManager.add(itemOverlay);
		overlayManager.add(regionLockerOverlay);
		overlayManager.add(regionBorderOverlay);
//		overlayManager.add(groundMarkerOverlay);
//		overlayManager.add(groundMarkerMinimapOverlay);
		itemOverlay.invalidateCache();
	}

	private void removeOverlays()
	{
		overlayManager.remove(dateOverlay);
		overlayManager.remove(itemOverlay);
		overlayManager.remove(regionLockerOverlay);
		overlayManager.remove(regionBorderOverlay);
//		overlayManager.remove(groundMarkerOverlay);
//		overlayManager.remove(groundMarkerMinimapOverlay);

		itemOverlay.invalidateCache();
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
				widgetHandler.updatePrayerWidgets();
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
	public void onVarbitChanged(VarbitChanged varbitChanged)
	{
		if (varbitChanged.getVarbitId() == 4070)
		{
			widgetHandler.updateSpellWidgets();
		}
	}

	@Subscribe
	public void onConfigChanged(ConfigChanged configChanged)
	{
		if (configChanged.getGroup().equals(CONFIG_GROUP))
		{
			regionLocker.readConfig();
			updateCurrentDate();
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

		widgetHandler.setUp();

		setUpCompleted = true;
	}

	//only call on client thread
	private void update()
	{
		updateQuestList();
		updateCurrentDate();
		if (previousDate < currentDate)
		{
			//this is where every thing would be updated.

			widgetHandler.update();
			itemOverlay.invalidateCache();
		}
	}

	private void updateQuestList()
	{
		for (ByReleaseQuest byReleaseQuest: ByReleaseQuest.values())
		{
			byReleaseQuest.setQuestState
				(
					byReleaseQuest.getQuest().getState(client)
				);
		}
	}

	private void updateCurrentDate()
	{
		if (config.overrideDate())
		{
			currentDate = config.date();


			ByReleaseQuest temp = null;
			for (ByReleaseQuest quest : ByReleaseQuest.values())
			{
				if (quest.getReleaseDate() <= currentDate)
				{
					temp = quest;
				}
				else
				{
					break;
				}
			}
			if (temp != null)
			{
				System.out.println(temp.getName());
				previousDate = currentDate;
				RegionLocker.updateReleasedRegions(currentDate);
				return;
			}
			return;
		}
		ByReleaseQuest previousQuest = null;
		for (ByReleaseQuest quest : ByReleaseQuest.values())
		{
			if (quest.getQuestState() != QuestState.FINISHED)
			{
				if (previousQuest != null && previousQuest.getQuestState() == QuestState.FINISHED)
				{
					previousDate = currentDate;
					currentDate = quest.getReleaseDate();
					RegionLocker.updateReleasedRegions(currentDate);
					return;
				}
			}
			previousQuest = quest;
		}
	}

	//https://github.com/IdylRS/chrono-plugin/blob/main/src/main/java/com/chrono/ChronoPlugin.java#L171
	private <T> T loadDefinitionResource(Type type, String resource)
	{
		// Load the resource as a stream and wrap it in a reader
		InputStream resourceStream = ByReleasePlugin.class.getResourceAsStream(resource);
		assert resourceStream != null;
		InputStreamReader definitionReader = new InputStreamReader(resourceStream);

		return gson.fromJson(definitionReader, type);
	}

	private void loadDefinitions()
	{
		Type defMapType = new TypeToken<HashMap<Integer, ByReleaseItem>>() {}.getType();
		ByReleaseItem.itemDefinitions = loadDefinitionResource(defMapType, "combined_items.json");
	}
}