/*
 *  BSD 2-Clause License
 *  * Copyright (c) 2023, Erishion Games LLC <https://github.com/Erishion-Games-LLC>
 *  * All rights reserved.
 *  *
 *  * Redistribution and use in source and binary forms, with or without
 *  * modification, are permitted provided that the following conditions are met:
 *  *
 *  * 1. Redistributions of source code must retain the above copyright notice, this
 *  *    list of conditions and the following disclaimer.
 *  * 2. Redistributions in binary form must reproduce the above copyright notice,
 *  *    this list of conditions and the following disclaimer in the documentation
 *  *    and/or other materials provided with the distribution.
 *  *
 *  * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 *  * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 *  * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 *  * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 *  * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 *  * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 *  * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 *  * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 *  * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 */

package com.erishiongamesllc.byrelease.data;

import lombok.Getter;
import net.runelite.api.coords.WorldPoint;

@Getter
public enum ByReleaseFurnace implements ByReleaseInfo
{
	//Only furnaces that were placed after the chunks they are in were released
	EDGEVILLE_FURNACE(new WorldPoint(3110, 3499, 0), 20150305),
	WILDERNESS_EAST_RUINS_FURNACE(new WorldPoint(3143, 3735, 0), 20020227),

	;

	final WorldPoint location;
	final int releaseDate;

	ByReleaseFurnace(WorldPoint location, int releaseDate)
	{
		this.location = location;
		this.releaseDate = releaseDate;
	}

	@Override
	public String getName()
	{
		return "Furnace";
	}
}