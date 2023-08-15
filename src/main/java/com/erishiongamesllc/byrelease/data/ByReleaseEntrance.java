package com.erishiongamesllc.byrelease.data;

import lombok.Getter;
import net.runelite.api.coords.WorldPoint;

@Getter
public enum ByReleaseEntrance implements ByReleaseInfo
{
	//Only furnaces that were placed after the chunks they are in were released
	DRAYNOR_MANOR_FRONT_DOOR_LEFT(new WorldPoint(3108, 3353, 0), 20010121),
	DRAYNOR_MANOR_FRONT_DOOR_RIGHT(new WorldPoint(3109, 3353, 0), 20010121),
	COOKS_GUILD_DOOR(new WorldPoint(3143, 3443, 0), 20010317),
	MINING_GUILD_DOOR(new WorldPoint(3046, 9756, 0), 20010813),
	MINING_GUILD_LADDER_1(new WorldPoint(3020, 3339, 0), 20010813),
	MINING_GUILD_LADDER_2(new WorldPoint(3019, 3340, 0), 20010813),
	MINING_GUILD_LADDER_3(new WorldPoint(3018, 3339, 0), 20010813),
	MINING_GUILD_LADDER_4(new WorldPoint(3019, 3338, 0), 20010813),
	FALADOR_MINES_STAIRS(new WorldPoint(3060, 3377, 0), 20021114),
	MINES_STAIRS_FALADOR(new WorldPoint(3059, 9777, 0), 20021114),
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