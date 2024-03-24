/*
 * Copyright (c) 2019, Slay to Stay <https://github.com/slaytostay>
 * Copyright (c) 2024, ErishionGamesLLC <https://github.com/Erishion-Games-LLC>
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
package com.erishiongamesllc.regionlocker;

import com.erishiongamesllc.byrelease.ByReleaseConfig;
import java.awt.Color;
import java.util.*;

public class RegionLocker
{
	private final ByReleaseConfig config;
	private static Set<Integer> releasedRegions = new HashSet<>(Regions.RSC_RELEASE_010104.getRegions());

	public static boolean renderLockedRegions;
	public static Color grayColor = new Color(0, 31, 77, 204);
	public static int grayAmount;
	public static boolean hardBorder;

	public RegionLocker(ByReleaseConfig config)
	{
		this.config = config;
		readConfig();
	}

	public void readConfig()
	{
		renderLockedRegions = config.renderLockedRegions();
		grayColor = config.shaderGrayColor();
		grayAmount = config.shaderGrayAmount().getAlpha();
		hardBorder = config.hardBorder();
	}

	public static void updateReleasedRegions(int currentDate)
	{
		releasedRegions.clear();
		for (Regions region : Regions.values())
		{
			if (region.getReleaseDate() <= currentDate)
			{
				releasedRegions.addAll(region.getRegions());
			}
		}
	}

	public static Set<Integer> getReleasedRegions()
	{
		if (releasedRegions == null)
		{
			return new HashSet<>();
		}
		return Collections.unmodifiableSet(releasedRegions);
	}
}