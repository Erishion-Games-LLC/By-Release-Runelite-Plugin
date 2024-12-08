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
import com.erishiongamesllc.byrelease.data.classes.ByReleaseItem;
import com.erishiongamesllc.byrelease.handlers.MenuOptionClickedHandler;
import com.erishiongamesllc.byrelease.handlers.OverlayHandler;
import com.erishiongamesllc.byrelease.handlers.WidgetHandler;
import com.erishiongamesllc.byrelease.managers.DataManager;
import com.erishiongamesllc.byrelease.managers.DateManager;
import com.erishiongamesllc.byrelease.managers.QuestManager;
import com.erishiongamesllc.regionlocker.RegionLocker;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.inject.Provides;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.HashMap;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

@Slf4j
@PluginDescriptor
	(
		name = PLUGIN_NAME
	)
public class ByReleasePlugin extends Plugin
{
	@Inject
	private ClientThread clientThread;
	@Inject
	private EventBus eventBus;


	@Inject
	private OverlayHandler overlayHandler;
	@Inject
	private MenuOptionClickedHandler menuOptionClickedHandler;
	@Inject
	private WidgetHandler widgetHandler;
	@Inject
	private DataManager dataManager;
	@Inject
	private DateManager dateManager;
	@Inject
	private ByReleaseConfig byReleaseConfig;

	@Inject
	private RegionLocker regionLocker;
	@Inject
	private QuestManager questManager;

	public static final String PLUGIN_NAME = "ByRelease";
	public static final String CONFIG_GROUP = "byrelease";


	@Override
	protected void startUp() throws Exception
	{
		eventBus.register(menuOptionClickedHandler);
		eventBus.register(widgetHandler);
		eventBus.register(overlayHandler);
		eventBus.register(dateManager);

		dataManager.startUp();
		overlayHandler.startUp();
		questManager.startUp();
	}

	@Override
	protected void shutDown() throws Exception
	{
		eventBus.unregister(menuOptionClickedHandler);
		eventBus.unregister(widgetHandler);
		eventBus.unregister(overlayHandler);
		eventBus.unregister(dateManager);

		overlayHandler.shutDown();
		clientThread.invokeLater(widgetHandler::shutDown);
		dataManager.shutDown();
		dateManager.shutDown();
		questManager.shutDown();
	}

	@Subscribe
	public void onConfigChanged(ConfigChanged configChanged)
	{
		if (!configChanged.getGroup().equals(CONFIG_GROUP))
		{
			return;
		}

		switch (configChanged.getKey())
		{
			case "renderLockedRegions":
			case "grayColor":
			case "grayAmount":
			case "hardBorder":
				regionLocker.readConfig();
				break;

			case "date":
				if (byReleaseConfig.overrideDate())
				{
					clientThread.invokeLater(dateManager::update);
				}
				break;

			case "spellsFromInitialRSC":
			case "prayersFromMagicRSC":
				clientThread.invokeLater(widgetHandler::update);
				break;
		}
	}

	@Provides
	ByReleaseConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(ByReleaseConfig.class);
	}
}