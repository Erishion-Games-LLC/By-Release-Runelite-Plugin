package com.erishiongamesllc.byrelease.data;

import lombok.Getter;
import net.runelite.api.coords.WorldPoint;

@Getter
public enum ByReleaseEntrance implements ByReleaseInfo
{
	//Only furnaces that were placed after the chunks they are in were released
	DRAYNOR_MANOR_FRONT_DOOR_LEFT(new WorldPoint(3108, 3353, 0), 20010121),
	DRAYNOR_MANOR_FRONT_DOOR_RIGHT(new WorldPoint(3109, 3353, 0), 20010121),
	;

	final WorldPoint location;
	final int releaseDate;

	ByReleaseEntrance(WorldPoint location, int releaseDate)
	{
		this.location = location;
		this.releaseDate = releaseDate;
	}

	@Override
	public String getName()
	{
		return "Entrance";
	}

	public enum ByReleaseTiles
	{
	}
}