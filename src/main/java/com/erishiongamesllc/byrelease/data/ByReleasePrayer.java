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
import net.runelite.api.Prayer;

@Getter
public enum ByReleasePrayer implements ByReleaseInfo
{
	THICK_SKIN(Prayer.THICK_SKIN, 20010524, 35454985, "Thick Skin"),
	BURST_OF_STRENGTH(Prayer.BURST_OF_STRENGTH, 20010524, 35454986, "Burst of Strength"),
	CLARITY_OF_THOUGHT(Prayer.CLARITY_OF_THOUGHT, 20010524, 35454987, "Clarity of Thought"),
	SHARP_EYE(Prayer.SHARP_EYE, 20060822, 35455003, "Sharp Eye"),
	MYSTIC_WILL(Prayer.MYSTIC_WILL, 20060822, 35455006, "Mystic Will"),
	ROCK_SKIN(Prayer.ROCK_SKIN, 20010524, 35454988, "Rock Skin"),
	SUPERHUMAN_STRENGTH(Prayer.SUPERHUMAN_STRENGTH, 20010524, 35454989, "Superhuman Strength"),
	IMPROVED_REFLEXES(Prayer.IMPROVED_REFLEXES, 20010524, 35454990, "Improved Reflexes"),
	RAPID_RESTORE(Prayer.RAPID_RESTORE, 20010524, 35454991, "Rapid Restore"),
	RAPID_HEAL(Prayer.RAPID_HEAL, 20010524, 35454992, "Rapid Heal"),
	PROTECT_ITEM(Prayer.PROTECT_ITEM, 20010524, 35454993, "Protect Item"),
	HAWK_EYE(Prayer.HAWK_EYE, 20060822, 35455004, "Hawk Eye"),
	MYSTIC_LORE(Prayer.MYSTIC_LORE, 20060822, 35455007, "Mystic Lore"),
	STEEL_SKIN(Prayer.STEEL_SKIN, 20010524, 35454994, "Steel Skin"),
	ULTIMATE_STRENGTH(Prayer.ULTIMATE_STRENGTH, 20010524, 35454995, "Ultimate Strength"),
	INCREDIBLE_REFLEXES(Prayer.INCREDIBLE_REFLEXES, 20010524, 35454996, "Incredible Reflexes"),
	PROTECT_FROM_MAGIC(Prayer.PROTECT_FROM_MAGIC, 20040329, 35454997, "Protect from Magic"),
	PROTECT_FROM_MISSILES(Prayer.PROTECT_FROM_MISSILES, 20010524, 35454998, "Protect from Missiles"),
	PROTECT_FROM_MELEE(Prayer.PROTECT_FROM_MELEE, 20040329, 35454999, "Protect from Melee"),
	EAGLE_EYE(Prayer.EAGLE_EYE, 20060822, 35455005, "Eagle Eye"),
	MYSTIC_MIGHT(Prayer.MYSTIC_MIGHT, 20060822, 35455008, "Mystic Might"),
	RETRIBUTION(Prayer.RETRIBUTION, 20050906, 35455000, "Retribution"),
	REDEMPTION(Prayer.REDEMPTION, 20050906, 35455001,"Redemption"),
	SMITE(Prayer.SMITE, 20050906, 35455002, "Smite"),
	PRESERVE(Prayer.PRESERVE, 20170105, 35455013, "Preserve"),
	CHIVALRY(Prayer.CHIVALRY, 20070724, 35455010, "Chivalry"),
	PIETY(Prayer.PIETY, 20070724, 35455011, "Piety"),
	RIGOUR(Prayer.RIGOUR, 20170105, 35455009, "Rigour"),
	AUGURY(Prayer.AUGURY, 20170105, 35455012, "Augury");

	private final Prayer prayer;
	private final int releaseDate;
	private final int widgetID;
	private final String name;

	ByReleasePrayer(Prayer prayer, int releaseDate, int widgetID, String name)
	{
		this.prayer = prayer;
		this.releaseDate = releaseDate;
		this.widgetID = widgetID;
		this.name = name;
	}
}
