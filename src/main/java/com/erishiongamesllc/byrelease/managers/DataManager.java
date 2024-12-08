package com.erishiongamesllc.byrelease.managers;

import com.erishiongamesllc.byrelease.ByReleasePlugin;
import com.erishiongamesllc.byrelease.data.classes.ByReleaseItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import javax.inject.Inject;

//https://github.com/IdylRS/chrono-plugin/blob/main/src/main/java/com/chrono/EntityDefinition.java
public class DataManager
{
	@Inject
	private Gson gson;

	public static HashMap<Integer, ByReleaseItem> itemDefinitions;

	public void startUp()
	{
		loadDefinitions();
	}

	public void shutDown()
	{
		itemDefinitions = null;
	}

	private void loadDefinitions()
	{
		Type defMapType = new TypeToken<HashMap<Integer, ByReleaseItem>>() {}.getType();
		DataManager.itemDefinitions = loadDefinitionResource(defMapType, "combined_items.json");
	}

	//https://github.com/IdylRS/chrono-plugin/blob/main/src/main/java/com/chrono/ChronoPlugin.java#L171
	private <T> T loadDefinitionResource(Type type, String resource)
	{
		// Load the resource as a stream and wrap it in a reader
		InputStream resourceStream = ByReleasePlugin.class.getResourceAsStream(resource);

		if (resourceStream == null)
		{
			throw new IllegalArgumentException("The following resource is missing from the ByRelease Plugin. Please leave an issue on github.: " + resource);
		}

		InputStreamReader definitionReader = new InputStreamReader(resourceStream);

		return gson.fromJson(definitionReader, type);
	}

	public static boolean isItemUnlocked(int itemId, int currentDate) throws ParseException
	{
		ByReleaseItem def = itemDefinitions.get(itemId);

		if (def == null) {
			System.out.println("Item being checked is not in the list of items: " + itemId);
			return false;
		}

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date releaseDate = dateFormat.parse(def.getReleaseDate());

		// Convert releaseDate to an integer format (yyyyMMdd)
		int releaseDateAsInt = Integer.parseInt(new SimpleDateFormat("yyyyMMdd").format(releaseDate));
		return releaseDateAsInt <= currentDate;
	}
}
