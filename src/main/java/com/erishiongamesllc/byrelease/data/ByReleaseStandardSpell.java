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
	import net.runelite.api.SpriteID;

public enum ByReleaseStandardSpell {
	LUMBRIDGE_HOME_TELEPORT("Lumbridge Home Teleport", 20060927, 14286854, SpriteID.SPELL_LUMBRIDGE_HOME_TELEPORT, SpriteID.SPELL_LUMBRIDGE_HOME_TELEPORT_DISABLED),
	WIND_STRIKE("Wind Strike", 20010524, 14286855, SpriteID.SPELL_WIND_STRIKE, SpriteID.SPELL_WIND_STRIKE_DISABLED),
	CONFUSE("Confuse", 20010104, 14286856, SpriteID.SPELL_CONFUSE, SpriteID.SPELL_CONFUSE_DISABLED),
	ENCHANT_CROSSBOW_BOLT("Enchant Crossbow Bolt", 20060731, 14286857, SpriteID.SPELL_ENCHANT_CROSSBOW_BOLT, SpriteID.SPELL_ENCHANT_CROSSBOW_BOLT_DISABLED),
	WATER_STRIKE("Water Strike", 20010524, 14286858, SpriteID.SPELL_WATER_STRIKE, SpriteID.SPELL_WATER_STRIKE_DISABLED),
	LVL_1_ENCHANT("Lvl-1 Enchant", 20010524, 14286859, SpriteID.SPELL_LVL_1_ENCHANT, SpriteID.SPELL_LVL_1_ENCHANT_DISABLED),
	EARTH_STRIKE("Earth Strike", 20010524, 14286860, SpriteID.SPELL_EARTH_STRIKE, SpriteID.SPELL_EARTH_STRIKE_DISABLED),
	WEAKEN("Weaken", 20010524, 14286861, SpriteID.SPELL_WEAKEN, SpriteID.SPELL_WEAKEN_DISABLED),
	FIRE_STRIKE("Fire Strike", 20010524, 14286862, SpriteID.SPELL_FIRE_STRIKE, SpriteID.SPELL_FIRE_STRIKE_DISABLED),
	BONES_TO_BANANAS("Bones to Bananas", 20010526, 14286863, SpriteID.SPELL_BONES_TO_BANANAS, SpriteID.SPELL_BONES_TO_BANANAS_DISABLED),
	WIND_BOLT("Wind Bolt", 20010524, 14286864, SpriteID.SPELL_WIND_BOLT, SpriteID.SPELL_WIND_BOLT_DISABLED),
	CURSE("Curse", 20010524, 14286865, SpriteID.SPELL_CURSE, SpriteID.SPELL_CURSE_DISABLED),
	BIND("Bind", 20040329, 14286866, SpriteID.SPELL_BIND, SpriteID.SPELL_BIND_DISABLED),
	LOW_LEVEL_ALCHEMY("Low Level Alchemy", 20010524, 14286867, SpriteID.SPELL_LOW_LEVEL_ALCHEMY, SpriteID.SPELL_LOW_LEVEL_ALCHEMY_DISABLED),
	WATER_BOLT("Water Bolt", 20010524, 14286878, SpriteID.SPELL_WATER_BOLT, SpriteID.SPELL_WATER_BOLT_DISABLED),
	VARROCK_TELEPORT("Varrock Teleport", 20010524, 14286869, SpriteID.SPELL_VARROCK_TELEPORT, SpriteID.SPELL_VARROCK_TELEPORT_DISABLED),
	LVL_2_ENCHANT("Lvl-2 Enchant", 20010524, 14286870, SpriteID.SPELL_LVL_2_ENCHANT, SpriteID.SPELL_LVL_2_ENCHANT_DISABLED),
	EARTH_BOLT("Earth Bolt", 20010524, 14286871, SpriteID.SPELL_EARTH_BOLT, SpriteID.SPELL_EARTH_BOLT_DISABLED),
	LUMBRIDGE_TELEPORT("Lumbridge Teleport", 20010524, 14286872, SpriteID.SPELL_LUMBRIDGE_TELEPORT, SpriteID.SPELL_LUMBRIDGE_TELEPORT_DISABLED),
	TELEKINETIC_GRAB("Telekinetic Grab", 20010524, 14286873, SpriteID.SPELL_TELEKINETIC_GRAB, SpriteID.SPELL_TELEKINETIC_GRAB_DISABLED),
	FIRE_BOLT("Fire Bolt", 20010524, 14286874, SpriteID.SPELL_FIRE_BOLT, SpriteID.SPELL_FIRE_BOLT_DISABLED),
	FALADOR_TELEPORT("Falador Teleport", 20010524, 14286875, SpriteID.SPELL_FALADOR_TELEPORT, SpriteID.SPELL_FALADOR_TELEPORT_DISABLED),
	CRUMBLE_UNDEAD("Crumble Undead", 20010524, 14286876, SpriteID.SPELL_CRUMBLE_UNDEAD, SpriteID.SPELL_CRUMBLE_UNDEAD_DISABLED),
	TELEPORT_TO_HOUSE("Teleport to House", 20060531, 14286877, SpriteID.SPELL_TELEPORT_TO_HOUSE, SpriteID.SPELL_TELEPORT_TO_HOUSE_DISABLED),
	WIND_BLAST("Wind Blast", 20010524, 14286878, SpriteID.SPELL_WIND_BLAST, SpriteID.SPELL_WIND_BLAST_DISABLED),
	SUPERHEAT_ITEM("Superheat Item", 20010524, 14286879, SpriteID.SPELL_SUPERHEAT_ITEM, SpriteID.SPELL_SUPERHEAT_ITEM_DISABLED),
	CAMELOT_TELEPORT("Camelot Teleport", 20020318, 14286880, SpriteID.SPELL_CAMELOT_TELEPORT, SpriteID.SPELL_CAMELOT_TELEPORT_DISABLED),
	WATER_BLAST("Water Blast", 20010524, 14286881, SpriteID.SPELL_WATER_BLAST, SpriteID.SPELL_WATER_BLAST_DISABLED),
	LVL_3_ENCHANT("Lvl-3 Enchant", 20010524, 14286882, SpriteID.SPELL_LVL_3_ENCHANT, SpriteID.SPELL_LVL_3_ENCHANT_DISABLED),
	IBAN_BLAST("Iban Blast", 20030303, 14286883, SpriteID.SPELL_IBAN_BLAST, SpriteID.SPELL_IBAN_BLAST_DISABLED),
	SNARE("Snare", 20040329, 14286884, SpriteID.SPELL_SNARE, SpriteID.SPELL_SNARE_DISABLED),
	MAGIC_DART("Magic Dart", 20050126, 14286885, SpriteID.SPELL_MAGIC_DART, SpriteID.SPELL_MAGIC_DART_DISABLED),
	ARDOUGNE_TELEPORT("Ardougne Teleport", 20020827, 14286886, SpriteID.SPELL_ARDOUGNE_TELEPORT, SpriteID.SPELL_ARDOUGNE_TELEPORT_DISABLED),
	EARTH_BLAST("Earth Blast", 20010524, 14286887, SpriteID.SPELL_EARTH_BLAST, SpriteID.SPELL_EARTH_BLAST_DISABLED),
	HIGH_LEVEL_ALCHEMY("High Level Alchemy", 20010524, 14286888, SpriteID.SPELL_HIGH_LEVEL_ALCHEMY, SpriteID.SPELL_HIGH_LEVEL_ALCHEMY_DISABLED),
	CHARGE_WATER_ORB("Charge Water Orb", 20020318, 14286889, SpriteID.SPELL_CHARGE_WATER_ORB, SpriteID.SPELL_CHARGE_WATER_ORB_DISABLED),
	LVL_4_ENCHANT("Lvl-4 Enchant", 20010524, 14286890, SpriteID.SPELL_LVL_4_ENCHANT, SpriteID.SPELL_LVL_4_ENCHANT_DISABLED),
	WATCHTOWER_TELEPORT("Watchtower Teleport", 20030507, 14286891, SpriteID.SPELL_WATCHTOWER_TELEPORT, SpriteID.SPELL_WATCHTOWER_TELEPORT_DISABLED),
	FIRE_BLAST("Fire Blast", 20010524, 14286892, SpriteID.SPELL_FIRE_BLAST, SpriteID.SPELL_FIRE_BLAST_DISABLED),
	CHARGE_EARTH_ORB("Charge Earth Orb", 20020318, 14286893, SpriteID.SPELL_CHARGE_EARTH_ORB, SpriteID.SPELL_CHARGE_EARTH_ORB_DISABLED),
	BONES_TO_PEACHES("Bones to Peaches", 20060104, 14286894, SpriteID.SPELL_BONES_TO_PEACHES, SpriteID.SPELL_BONES_TO_PEACHES_DISABLED),
	SARADOMIN_STRIKE("Saradomin Strike", 20030922, 14286895, SpriteID.SPELL_SARADOMIN_STRIKE, SpriteID.SPELL_SARADOMIN_STRIKE_DISABLED),
	FLAMES_OF_ZAMORAK("Flames of Zamorak", 20030922, 14286896, SpriteID.SPELL_FLAMES_OF_ZAMORAK, SpriteID.SPELL_FLAMES_OF_ZAMORAK_DISABLED),
	CLAWS_OF_GUTHIX("Claws of Guthix", 20030922, 14286897, SpriteID.SPELL_CLAWS_OF_GUTHIX, SpriteID.SPELL_CLAWS_OF_GUTHIX_DISABLED),
	TROLLHEIM_TELEPORT("Trollheim Teleport", 20041005, 14286898, SpriteID.SPELL_TROLLHEIM_TELEPORT, SpriteID.SPELL_TROLLHEIM_TELEPORT_DISABLED),
	WIND_WAVE("Wind Wave", 20020318, 14286899, SpriteID.SPELL_WIND_WAVE, SpriteID.SPELL_WIND_WAVE_DISABLED),
	CHARGE_FIRE_ORB("Charge Fire Orb", 20020318, 14286900, SpriteID.SPELL_CHARGE_FIRE_ORB, SpriteID.SPELL_CHARGE_FIRE_ORB_DISABLED),
	APE_ATOLL_TELEPORT("Ape Atoll Teleport", 20060315, 14286901, SpriteID.SPELL_TELEPORT_TO_APE_ATOLL, SpriteID.SPELL_TELEPORT_TO_APE_ATOLL_DISABLED),
	WATER_WAVE("Water Wave", 20020318, 14286902, SpriteID.SPELL_WATER_WAVE, SpriteID.SPELL_WATER_WAVE_DISABLED),
	CHARGE_AIR_ORB("Charge Air Orb", 20020318, 14286903, SpriteID.SPELL_CHARGE_AIR_ORB, SpriteID.SPELL_CHARGE_AIR_ORB_DISABLED),
	VULNERABILITY("Vulnerability", 20021023, 14286904, SpriteID.SPELL_VULNERABILITY, SpriteID.SPELL_VULNERABILITY_DISABLED),
	LVL_5_ENCHANT("Lvl-5 Enchant", 20020227, 14286905, SpriteID.SPELL_LVL_5_ENCHANT, SpriteID.SPELL_LVL_5_ENCHANT_DISABLED),
	KOUREND_CASTLE_TELEPORT("Kourend Castle Teleport", 20160107, 14286906, SpriteID.SPELL_TELEPORT_TO_KOUREND, SpriteID.SPELL_TELEPORT_TO_KOUREND_DISABLED),
	EARTH_WAVE("Earth Wave", 20020318, 14286907, SpriteID.SPELL_EARTH_WAVE, SpriteID.SPELL_EARTH_WAVE_DISABLED),
	ENFEEBLE("Enfeeble", 20021023, 14286908, SpriteID.SPELL_ENFEEBLE, SpriteID.SPELL_ENFEEBLE_DISABLED),
	TELEOTHER_LUMBRIDGE("Teleother Lumbridge", 20050314, 14286909, SpriteID.SPELL_TELEOTHER_LUMBRIDGE, SpriteID.SPELL_TELEOTHER_LUMBRIDGE_DISABLED),
	FIRE_WAVE("Fire Wave", 20020318, 14286910, SpriteID.SPELL_FIRE_WAVE, SpriteID.SPELL_FIRE_WAVE_DISABLED),
	ENTANGLE("Entangle", 20040329, 14286911, SpriteID.SPELL_ENTANGLE, SpriteID.SPELL_ENTANGLE_DISABLED),
	STUN("Stun", 20021023, 14286912, SpriteID.SPELL_STUN, SpriteID.SPELL_STUN_DISABLED),
	CHARGE("Charge", 20030922, 14286913, SpriteID.SPELL_CHARGE, SpriteID.SPELL_CHARGE_DISABLED),
	WIND_SURGE("Wind Surge", 20180104, 14286914, SpriteID.SPELL_WIND_SURGE, SpriteID.SPELL_WIND_SURGE_DISABLED),
	TELEOTHER_FALADOR("Teleother Falador", 20050314, 14286915, SpriteID.SPELL_TELEOTHER_FALADOR, SpriteID.SPELL_TELEOTHER_FALADOR_DISABLED),
	WATER_SURGE("Water Surge", 20180104, 14286916, SpriteID.SPELL_WATER_SURGE, SpriteID.SPELL_WATER_SURGE_DISABLED),
	TELE_BLOCK("Tele Block", 20050314, 14286917, SpriteID.SPELL_TELE_BLOCK, SpriteID.SPELL_TELE_BLOCK_DISABLED),
	TELEPORT_TO_TARGET("Teleport to Target", 20141030, 14286918, SpriteID.SPELL_TELEPORT_TO_BOUNTY_TARGET, SpriteID.SPELL_TELEPORT_TO_BOUNTY_TARGET_DISABLED),
	LVL_6_ENCHANT("Lvl-6 Enchant", 20051004, 14286919, SpriteID.SPELL_LVL_6_ENCHANT, SpriteID.SPELL_LVL_6_ENCHANT_DISABLED),
	TELEOTHER_CAMELOT("Teleother Camelot", 20050314, 14286920, SpriteID.SPELL_TELEOTHER_CAMELOT, SpriteID.SPELL_TELEOTHER_CAMELOT_DISABLED),
	EARTH_SURGE("Earth Surge", 20180104, 14286921, SpriteID.SPELL_EARTH_SURGE, SpriteID.SPELL_EARTH_SURGE_DISABLED),
	LVL_7_ENCHANT("Lvl-7 Enchant", 20160506, 14286922, SpriteID.SPELL_LVL_7_ENCHANT, SpriteID.SPELL_LVL_7_ENCHANT_DISABLED),
	FIRE_SURGE("Fire Surge", 20180104, 14286923, SpriteID.SPELL_FIRE_SURGE, SpriteID.SPELL_FIRE_SURGE_DISABLED);

	@Getter
	private final String name;
	@Getter
	private final int releaseDate;
	@Getter
	private final int widgetID;
	@Getter
	private final int unlockedSpriteID;
	@Getter
	private final int lockedSpriteID;

	ByReleaseStandardSpell(String name, int releaseDate, int widgetID, int unlockedSpriteID, int lockedSpriteID) {
		this.name = name;
		this.releaseDate = releaseDate;
		this.widgetID = widgetID;
		this.unlockedSpriteID = unlockedSpriteID;
		this.lockedSpriteID = lockedSpriteID;
	}
}