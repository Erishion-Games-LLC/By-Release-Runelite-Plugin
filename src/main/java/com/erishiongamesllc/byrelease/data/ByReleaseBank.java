package com.erishiongamesllc.byrelease.data;

import lombok.Getter;
import net.runelite.api.coords.WorldPoint;

@Getter
public enum ByReleaseBank implements ByReleaseInfo
{
	VARROCK_WEST_BOOTH_1(new WorldPoint(3186, 3444, 0), 20010123),
	VARROCK_WEST_BOOTH_2(new WorldPoint(3186, 3442, 0), 20010123),
	VARROCK_WEST_BOOTH_3(new WorldPoint(3186, 3440, 0), 20010123),
	VARROCK_WEST_BOOTH_4(new WorldPoint(3186, 3438, 0), 20010123),
	VARROCK_WEST_BOOTH_5(new WorldPoint(3186, 3436, 0), 20010123),

	VARROCK_WEST_BANKER_1(new WorldPoint(3187, 3444, 0), 20010123),
	VARROCK_WEST_BANKER_2(new WorldPoint(3187, 3442, 0), 20010123),
	VARROCK_WEST_BANKER_3(new WorldPoint(3187, 3440, 0), 20010123),
	VARROCK_WEST_BANKER_4(new WorldPoint(3187, 3438, 0), 20010123),
	VARROCK_WEST_BANKER_5(new WorldPoint(3187, 3436, 0), 20010123),


	FALADOR_WEST_BOOTH_1(new WorldPoint(2949, 3367, 0), 20010406),
	FALADOR_WEST_BOOTH_2(new WorldPoint(2948, 3367, 0), 20010406),
	FALADOR_WEST_BOOTH_3(new WorldPoint(2947, 3367, 0), 20010406),
	FALADOR_WEST_BOOTH_4(new WorldPoint(2946, 3367, 0), 20010406),
	FALADOR_WEST_BOOTH_5(new WorldPoint(2945, 3367, 0), 20010406),

	FALADOR_WEST_BANKER_1(new WorldPoint(2949, 3366, 0), 20010406),
	FALADOR_WEST_BANKER_2(new WorldPoint(2948, 3366, 0), 20010406),
	FALADOR_WEST_BANKER_3(new WorldPoint(2947, 3366, 0), 20010406),
	FALADOR_WEST_BANKER_4(new WorldPoint(2946, 3366, 0), 20010406),
	FALADOR_WEST_BANKER_5(new WorldPoint(2945, 3366, 0), 20010406),

	FALADOR_EAST_BOOTH_1(new WorldPoint(3015, 3354, 0), 20011208),
	FALADOR_EAST_BOOTH_2(new WorldPoint(3014, 3354, 0), 20011208),
	FALADOR_EAST_BOOTH_3(new WorldPoint(3013, 3354, 0), 20011208),
	FALADOR_EAST_BOOTH_4(new WorldPoint(3012, 3354, 0), 20011208),
	FALADOR_EAST_BOOTH_5(new WorldPoint(3011, 3354, 0), 20011208),
	FALADOR_EAST_BOOTH_6(new WorldPoint(3010, 3354, 0), 20011208),

	FALADOR_EAST_BANKER_1(new WorldPoint(3015, 3353, 0), 20011208),
	FALADOR_EAST_BANKER_2(new WorldPoint(3014, 3353, 0), 20011208),
	FALADOR_EAST_BANKER_3(new WorldPoint(3013, 3353, 0), 20011208),
	FALADOR_EAST_BANKER_4(new WorldPoint(3012, 3353, 0), 20011208),
	FALADOR_EAST_BANKER_5(new WorldPoint(3011, 3353, 0), 20011208),
	FALADOR_EAST_BANKER_6(new WorldPoint(3010, 3353, 0), 20011208),


	DRAYNOR_BOOTH_1(new WorldPoint(3091, 3245, 0), 20010818),
	DRAYNOR_BOOTH_2(new WorldPoint(3091, 3243, 0), 20010818),
	DRAYNOR_BOOTH_3(new WorldPoint(3091, 3242, 0), 20010818),

	DRAYNOR_BANKER_1(new WorldPoint(3090, 3245, 0), 20010818),
	DRAYNOR_BANKER_2(new WorldPoint(3090, 3243, 0), 20010818),
	DRAYNOR_BANKER_3(new WorldPoint(3090, 3242, 0), 20010818),
	DRAYNOR_BANKER_4(new WorldPoint(3088, 3242, 0), 20010818),







	GRAND_EXCHANGE_BANKER(new WorldPoint(3112, 3424, 0), 20150226),


	;

	final WorldPoint location;
	final int releaseDate;

	ByReleaseBank(WorldPoint location, int releaseDate)
	{
		this.location = location;
		this.releaseDate = releaseDate;
	}

	@Override
	public String getName()
	{
		return "Bank";
	}
}
