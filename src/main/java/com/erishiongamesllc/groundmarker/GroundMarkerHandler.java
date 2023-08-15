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
package com.erishiongamesllc.groundmarker;

import com.erishiongamesllc.byrelease.ByReleasePlugin;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.awt.Color;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.GameStateChanged;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ProfileChanged;

@Singleton
public class GroundMarkerHandler
{
	public final HashMap<Integer, ArrayList<ColorTileMarker>> points = new HashMap<>();

	@Inject
	private Client client;
	@Inject
	private ByReleasePlugin plugin;

	@Subscribe
	public void onProfileChanged(ProfileChanged profileChanged)
	{
		loadPoints();
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged gameStateChanged)
	{
		if (gameStateChanged.getGameState() != GameState.LOGGED_IN)
		{
			return;
		}

		// map region has just been updated
		loadPoints();
	}

	private ArrayList<GroundMarkerPoint> loadPointsFromJsonFile()
	{
		try
		{
			InputStream inputStream = getClass().getResourceAsStream("/com/erishiongamesllc/groundmarker/tiles.json");
			if (inputStream == null)
			{
				return new ArrayList<>();
			}

			InputStreamReader reader = new InputStreamReader(inputStream);
			JsonParser jsonParser = new JsonParser();
			JsonObject jsonObject = jsonParser.parse(reader).getAsJsonObject();

			ArrayList<GroundMarkerPoint> loadedPoints = new ArrayList<>();

			for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet())
			{
				JsonElement pointsElement = entry.getValue();
				JsonArray pointsArray = pointsElement.getAsJsonArray();

				for (JsonElement pointElement : pointsArray)
				{
					GroundMarkerPoint point = new Gson().fromJson(pointElement, GroundMarkerPoint.class);
					point.setLabel(String.valueOf(point.getRemovalDate()));
					loadedPoints.add(point);
				}
			}

			return loadedPoints;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return new ArrayList<>();
		}
	}

	public void loadPoints()
	{
		points.clear();

		ArrayList<GroundMarkerPoint> loadedPoints = loadPointsFromJsonFile();

		for (GroundMarkerPoint point : loadedPoints)
		{
			ColorTileMarker tileMarker = translateToColorTileMarker(point);
			int regionID = tileMarker.getWorldPoint().getRegionID();
			points.computeIfAbsent(regionID, k -> new ArrayList<>()).add(tileMarker);
		}
	}

	private ColorTileMarker translateToColorTileMarker(GroundMarkerPoint point)
	{
		return new ColorTileMarker(WorldPoint.fromRegion(point.getRegionId(), point.getRegionX(), point.getRegionY(), point.getZ()),
			Color.RED, point.getLabel(), point.getRemovalDate());
	}

	public void clearPoints()
	{
		points.clear();
	}

	public Collection<ColorTileMarker> getPoints()
	{
		int currentDate = plugin.getCurrentDate();
		List<ColorTileMarker> filteredPoints = new ArrayList<>();
		int[] regions = client.getMapRegions();
		if (regions == null)
		{
			return filteredPoints;
		}

		for (int regionID : regions)
		{
			List<ColorTileMarker> regionPoints = points.get(regionID);
			if (regionPoints != null)
			{
				for (ColorTileMarker point : regionPoints)
				{
					if (point.getRemovalDate() > currentDate)
					{
						filteredPoints.add(point);
					}
				}
			}
		}
		return filteredPoints;
	}
}