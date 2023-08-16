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
package com.erishiongamesllc.regionlocker;


import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;

@Getter
public enum Regions
{
	RSC_RELEASE_010104(20010104, new HashSet<>(Arrays.asList(
		12342, 12341, 12340, 12339, 12338, 12337,
		12598, 12597, 12596, 12595, 12594, 12593,
		12854, 12853, 12852, 12851, 12850, 12849,
		13110, 13109, 13108, 13107,
		12442, 12698, 12441, 12954, 13210,
		12950))),
	RSC_UPDATE_010121(20010121, new HashSet<>(Arrays.asList(13106, 13105))),
	RSC_UPDATE_010406(20010406, new HashSet<>(Arrays.asList(11574,
		11830, 11829, 11828, 11827, 11826, 11825,
		12086, 12085, 12084, 12083, 12082, 12081,
		11929, 12185, 12184))),
	RSC_UPDATE_010611(20010611, new HashSet<>(Arrays.asList(11313, 11569))),
	RSC_UPDATE_010813(20010813, new HashSet<>(Arrays.asList(
		11836, 11835, 11834, 11833, 11832, 11831,
		12092, 12091, 12090, 12089, 12088, 12087,
		12348, 12347, 12346, 12345, 12344, 12343,
		12604, 12603, 12602, 12601, 12600, 12599,
		12860, 12859, 12858, 12857, 12856, 12855,
		13116, 13115, 13114, 13113, 13112, 13111))),
	RSC_UPDATE_010923(20010923, new HashSet<>(Arrays.asList(11315, 11314, 11570))),
	RSC_UPDATE_020227(20020227, new HashSet<>(Arrays.asList(
		11062, 11061, 11060, 11058, 11057,
		11318, 11317, 11316,
		11574, 11573, 11572, 11571))),
	RSC_UPDATE_020318(20020318, new HashSet<>(Arrays.asList(12444, 12443))),
	RSC_UPDATE_020325(20020325, new HashSet<>(Arrays.asList(10806, 10805, 10804))),
	RSC_UPDATE_020409(20020409, new HashSet<>(Arrays.asList(10803))),
	RSC_UPDATE_020430(20020430, new HashSet<>(Arrays.asList(10292, 10291, 10548, 10547))),
	RSC_UPDATE_020508(20020508, new HashSet<>(Arrays.asList(11837, 12093, 12349, 12605, 12861, 13117))),
	RSC_UPDATE_020528(20020528, new HashSet<>(Arrays.asList(10550, 10549, 10290, 10546))),
	RSC_UPDATE_020617(20020617, new HashSet<>(Arrays.asList(10293, 10546))),
	RSC_UPDATE_020723(20020723, new HashSet<>(Arrays.asList(10294, 10034, 10033, 10289, 10545, 10802, 10801))),
	RSC_UPDATE_020827(20020827, new HashSet<>(Arrays.asList(10036, 10035))),
	RSC_UPDATE_020909(20020909, new HashSet<>(Arrays.asList(11059))),
	RSC_UPDATE_020924(20020924, new HashSet<>(Arrays.asList(10038, 10037))),
	RSC_UPDATE_021023(20021023, new HashSet<>(Arrays.asList(10032, 10288, 10800, 11056, 11055, 11312, 11311, 11568, 11567))),
	RSC_UPDATE_021212(20021212, new HashSet<>(Arrays.asList(9526, 9525, 9524, 9782, 9781, 9780, 10544, 11823, 11822))),
	RSC_UPDATE_030127(20030127, new HashSet<>(Arrays.asList(11054, 11310, 11566))),
	RSC_UPDATE_030303(20030303, new HashSet<>(Arrays.asList(9523, 9522, 9779))),
	RSC_UPDATE_030317(20030317, new HashSet<>(Arrays.asList(9778, 9777))),
	RSC_UPDATE_030414(20030414, new HashSet<>(Arrays.asList(12591, 12848, 12847, 13104, 13103))),
	RSC_UPDATE_030507(20030507, new HashSet<>(Arrays.asList(10031, 10030, 10287, 10286))),
	RSC_UPDATE_030609(20030609, new HashSet<>(Arrays.asList(10807))),
	RSC_UPDATE_030709(20030709, new HashSet<>(Arrays.asList(13365, 13364))),
	RSC_UPDATE_030820(20030820, new HashSet<>(Arrays.asList(11053, 11309, 11565, 11821))),
	;

	final int releaseDate;
	final Set<Integer> regions;

	Regions(int releaseDate, Set<Integer> regions)
	{
		this.releaseDate = releaseDate;
		this.regions = regions;
	}
}