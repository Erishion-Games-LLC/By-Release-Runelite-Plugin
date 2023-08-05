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
package com.erishiongamesllc.byrelease;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup(ByReleasePlugin.CONFIG_GROUP)
public interface ByReleaseConfig extends Config
{
	@ConfigItem
	(
		keyName = "spellsFromInitialRSC",
		name = "Enable spells from initial RSC Spells",
		description = "Spellbook was reworked on 24 May 2001. Mark this true to enable corrosponding magic, if not you can't use any spells until 24 May 2001"
	)
	default boolean spellsFromInitialRSC()
	{
		return true;
	}

	@ConfigItem
	(
		keyName = "prayersFromMagicRSC",
		name = "Enable prayers from initial RSC Spells",
		description = "Prayers did not exist until 24 May 2001. The original spellbook had spells that are equivalent to these prayers. "
	)
	default boolean prayersFromMagicRSC()
	{
		return true;
	}

	@ConfigItem
	(
		keyName = "enableAnvils",
		name = "Filter anvils by release date",
		description = "limit anvils available based on release date"
	)
	default boolean enableAnvils()
	{
		return true;
	}

	@ConfigItem
	(
		keyName = "allowPickup",
		name = "Enable pickup/withdraw",
		description = "Allow you to pickup and withdraw items that are not released yet for future use"
	)
	default boolean allowPickup()
	{
		return true;
	}

	@ConfigItem
	(
		keyName = "enableFurnaces",
		name = "Filter furnces by release date",
		description = "limit furnaces available based on release date"
	)
	default boolean enableFurnces()
	{
		return true;
	}

	@ConfigItem
	(
		keyName = "enableDiaryTeleports",
		name = "Enable diary teleports",
		description = "limit diary teleports based on release date"
	)
	default boolean enableDiaryTeleports()
	{
		return true;
	}
}