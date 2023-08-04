/* BSD 2-Clause License
 * Copyright (c) 2023, Erishion Games LLC <https://github.com/Erishion-Games-LLC>
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
package com.erishiongamesllc.byrelease.data;

import lombok.Getter;
import net.runelite.api.Skill;

@Getter
public enum ByReleaseSkill
{
	ATTACK(Skill.ATTACK, 20010104, 20971521),
	STRENGTH(Skill.STRENGTH, 20010104, 20971522),
	DEFENCE(Skill.DEFENCE, 20010104, 20971523),
	RANGED(Skill.RANGED, 20010104, 20971524),
	PRAYER(Skill.PRAYER, 20010104, 20971525),
	MAGIC(Skill.MAGIC, 20010104, 20971526),
	RUNECRAFT(Skill.RUNECRAFT, 20040329, 20971527),
	CONSTRUCTION(Skill.CONSTRUCTION, 20060531, 20971528),
	HITPOINTS(Skill.HITPOINTS, 20010104, 20971529),
	AGILITY(Skill.AGILITY, 20021212, 20971530),
	HERBLORE(Skill.HERBLORE, 20020227, 20971531),
	THIEVING(Skill.THIEVING, 20020430, 20971532),
	CRAFTING(Skill.CRAFTING, 20010104, 20971533),
	FLETCHING(Skill.FLETCHING, 20020325, 20971534),
	SLAYER(Skill.SLAYER, 20050126, 20971535),
	HUNTER(Skill.HUNTER, 20061121, 20971536),
	MINING(Skill.MINING, 20010104, 20971537),
	SMITHING(Skill.SMITHING, 20010104, 20971538),
	FISHING(Skill.FISHING, 20010611, 20971539),
	COOKING(Skill.COOKING, 20010104, 20971540),
	FIREMAKING(Skill.FIREMAKING, 20010104, 20971541),
	WOODCUTTING(Skill.WOODCUTTING, 20010104, 20971542),
	FARMING(Skill.FARMING, 20050711, 20971543)
	;

	private final Skill skill;
	private final int releaseDate;
	private final int widgetID;

	ByReleaseSkill(Skill skill, int releaseDate, int widgetID)
	{
		this.skill = skill;
		this.releaseDate = releaseDate;
		this.widgetID = widgetID;
	}
}