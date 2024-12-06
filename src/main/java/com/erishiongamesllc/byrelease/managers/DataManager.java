package com.erishiongamesllc.byrelease.managers;

import com.erishiongamesllc.byrelease.data.classes.ByReleaseItem;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

//https://github.com/IdylRS/chrono-plugin/blob/main/src/main/java/com/chrono/EntityDefinition.java
public class DataManager
{
	public static HashMap<Integer, ByReleaseItem> itemDefinitions;

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
