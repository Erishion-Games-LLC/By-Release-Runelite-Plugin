package com.erishiongamesllc.byrelease.data;

import static com.erishiongamesllc.byrelease.data.ByReleaseStandardSpell.CAMELOT_TELEPORT;
import static com.erishiongamesllc.byrelease.data.ByReleaseStandardSpell.VARROCK_TELEPORT;
import static com.erishiongamesllc.byrelease.data.ByReleaseStandardSpell.WATCHTOWER_TELEPORT;
import lombok.Getter;

@Getter
public enum ByReleaseAchievementDiaryTeleports implements ByReleaseInfo
{
	SEERS(CAMELOT_TELEPORT.getName(), 20150305),
	YANILLE(WATCHTOWER_TELEPORT.getName(), 20150305),
	GRAND_EXCHANGE(VARROCK_TELEPORT.getName(), 20150430)
	;

	final String name;
	final int releaseDate;

	ByReleaseAchievementDiaryTeleports(String name, int releaseDate)
	{
		this.name = name;
		this.releaseDate = releaseDate;
	}
}