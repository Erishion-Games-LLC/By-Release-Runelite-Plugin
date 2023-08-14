package com.erishiongamesllc.groundmarker;

import com.erishiongamesllc.byrelease.ByReleaseConfig;
import com.erishiongamesllc.byrelease.ByReleasePlugin;
import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.Getter;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.GameStateChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ProfileChanged;
import net.runelite.client.game.chatbox.ChatboxPanelManager;

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

	public ArrayList<GroundMarkerPoint> loadPointsFromJsonFile()
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
			JsonArray jsonArray = jsonParser.parse(reader).getAsJsonArray();

			Type type = new TypeToken<ArrayList<GroundMarkerPoint>>() {}.getType();
			return new Gson().fromJson(jsonArray, type);
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
			Color.RED, point.getLabel(), point.getReleaseDate(), point.getRemovalDate());
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
					if (point.getReleaseDate() <= currentDate && point.getRemovalDate() > currentDate)
					{
						filteredPoints.add(point);
					}
				}
			}
		}
		return filteredPoints;
	}
}