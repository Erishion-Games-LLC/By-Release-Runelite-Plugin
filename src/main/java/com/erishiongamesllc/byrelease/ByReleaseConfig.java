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

import java.awt.Color;
import net.runelite.client.config.Alpha;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;
import net.runelite.client.config.Keybind;
import net.runelite.client.config.Range;

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

	@ConfigItem
	(
		keyName = "overrideDate",
		name = "Override Date",
		description = ""
	)
	default boolean overrideDate()
	{
		return true;
	}

	@ConfigItem
	(
		keyName = "date",
		name = "date",
		description = ""
	)
	default int date()
	{
		return 20010104;
	}





	//Ground Marker Settings
	@ConfigItem(
		keyName = "borderWidth",
		name = "Border Width",
		description = "Width of the marked tile border"
	)
	default double borderWidth()
	{
		return 2;
	}

	@ConfigItem(
		keyName = "drawOnMinimap",
		name = "Draw tiles on minimap",
		description = "Configures whether marked tiles should be drawn on minimap"
	)
	default boolean drawTileOnMinimmap()
	{
		return false;
	}

	@ConfigItem(
		keyName = "fillOpacity",
		name = "Fill Opacity",
		description = "Opacity of the tile fill color"
	)
	@Range(
		max = 255
	)
	default int fillOpacity()
	{
		return 50;
	}




	//Region Locker Settings
	@ConfigSection(
		name = "Environment Looks",
		description = "Settings relating to locked regions look",
		position = 1
	)
	String environmentSettings = "environmentSettings";

	@ConfigSection(
		name = "Map Settings",
		description = "Settings relating to the map overlay",
		position = 2
	)
	String mapSettings = "mapSettings";

	// Environment Looks
	@ConfigItem(
		keyName = "renderLockedRegions",
		name = "Locked chunk shader",
		description = "Adds graphical change to all chunk that are locked",
		position = 21,
		section = environmentSettings
	)
	default boolean renderLockedRegions()
	{
		return true;
	}

	@Alpha
	@ConfigItem(
		keyName = "shaderGrayColor",
		name = "Chunk shader color",
		description = "The color of the locked chunks in the shader",
		position = 22,
		section = environmentSettings
	)
	default Color shaderGrayColor()
	{
		return new Color(0, 31, 77, 204);
	}

	@Alpha
	@ConfigItem(
		keyName = "shaderGrayAmount",
		name = "Chunk shader opacity",
		description = "The amount of gray scale that is applied to a locked chunk in the shader (alpha only)",
		position = 23,
		section = environmentSettings
	)
	default Color shaderGrayAmount()
	{
		return new Color(0, 0, 0, 204);
	}

	@ConfigItem(
		keyName = "hardBorder",
		name = "Hard chunk border",
		description = "True = hard border cutoff, False = chunk border gradient",
		position = 24,
		section = environmentSettings
	)
	default boolean hardBorder()
	{
		return true;
	}

	@ConfigItem(
		keyName = "renderRegionBorders",
		name = "Draw chunk border lines",
		description = "Draw the chunk borders in the environment marked by lines",
		position = 25,
		section = environmentSettings
	)
	default boolean renderRegionBorders()
	{
		return false;
	}

	@ConfigItem(
		keyName = "regionBorderWidth",
		name = "Chunk border width",
		description = "How wide the region border will be",
		position = 26,
		section = environmentSettings
	)
	default int regionBorderWidth()
	{
		return 1;
	}

	@Alpha
	@ConfigItem(
		keyName = "regionBorderColor",
		name = "Chunk border color",
		description = "The color of the chunk borders",
		position = 27,
		section = environmentSettings
	)
	default Color regionBorderColor()
	{
		return new Color(0, 200, 83, 200);
	}

	// Map Settings

	@ConfigItem(
		keyName = "drawMapOverlay",
		name = "Draw chunks on map",
		description = "Draw a color overlay for each locked/unlocked chunk",
		position = 28,
		section = mapSettings
	)
	default boolean drawMapOverlay()
	{
		return true;
	}

	@Alpha
	@ConfigItem(
		keyName = "mapOverlayColor",
		name = "Map overlay color",
		description = "The color the map overlay will draw the chunks in",
		position = 30,
		section = mapSettings
	)
	default Color mapOverlayColor()
	{
		return new Color(200, 16, 0, 100);
	}

	@ConfigItem(
		keyName = "drawMapGrid",
		name = "Draw map grid",
		description = "Draw the grid of chunks on the map",
		position = 33,
		section = mapSettings
	)
	default boolean drawMapGrid()
	{
		return true;
	}

	@ConfigItem(
		keyName = "drawRegionId",
		name = "Draw region IDs",
		description = "Draw the chunk ID for each chunk on the map",
		position = 34,
		section = mapSettings
	)
	default boolean drawRegionId()
	{
		return true;
	}
}