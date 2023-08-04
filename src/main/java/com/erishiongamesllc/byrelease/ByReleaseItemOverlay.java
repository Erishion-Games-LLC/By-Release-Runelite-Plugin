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

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.text.ParseException;
import javax.inject.Inject;
import net.runelite.api.widgets.WidgetItem;
import net.runelite.client.game.ItemManager;
import net.runelite.client.ui.overlay.WidgetItemOverlay;
import net.runelite.client.util.ColorUtil;
import net.runelite.client.util.ImageUtil;

//https://github.com/IdylRS/chrono-plugin/blob/main/src/main/java/com/chrono/ChronoItemOverlay.java
public class ByReleaseItemOverlay extends WidgetItemOverlay
{
	private ByReleasePlugin byReleasePlugin;
	private final ItemManager itemManager;
	private final Cache<Long, Image> imageCache;

	@Inject
	private ByReleaseItemOverlay(ItemManager itemManager, ByReleasePlugin byReleasePlugin)
	{
		this.itemManager = itemManager;
		this.byReleasePlugin = byReleasePlugin;
		showOnEquipment();
		showOnInventory();
		showOnBank();
		showOnInterfaces();
		imageCache = CacheBuilder.newBuilder()
			.concurrencyLevel(1)
			.maximumSize(48)
			.build();
	}

	@Override
	public void renderItemOverlay(Graphics2D graphics, int itemId, WidgetItem widgetItem)
	{
		try
		{
			if (!byReleasePlugin.isItemUnlocked(itemId))
			{
				Rectangle bounds = widgetItem.getCanvasBounds();
				final Image image = createFillImage(itemId, widgetItem.getQuantity());
				graphics.drawImage(image, (int) bounds.getX(), (int) bounds.getY(), null);
			}
		}
		catch (ParseException e)
		{
			e.printStackTrace();
		}
	}

	private Image createFillImage(int itemID, int quantity)
	{
		long key = (((long) itemID) << 32) | quantity;
		Image image = imageCache.getIfPresent(key);
		if (image != null)
		{
			return image;
		}
		image = ImageUtil.fillImage(itemManager.getImage(itemID, quantity, false),
			ColorUtil.colorWithAlpha(Color.GRAY, 150));
		imageCache.put(key, image);
		return image;
	}

	void invalidateCache()
	{
		imageCache.invalidateAll();
	}
}
