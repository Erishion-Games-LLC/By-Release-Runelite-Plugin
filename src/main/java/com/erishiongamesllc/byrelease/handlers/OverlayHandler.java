package com.erishiongamesllc.byrelease.handlers;

import com.erishiongamesllc.byrelease.overlay.CurrentDateOverlay;
import com.erishiongamesllc.byrelease.overlay.LockedItemOverlay;
import com.erishiongamesllc.regionlocker.RegionLockerOverlay;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.runelite.client.ui.overlay.OverlayManager;

@Singleton
public class OverlayHandler
{
	private final OverlayManager overlayManager;
	private final CurrentDateOverlay currentDateOverlay;
	private final LockedItemOverlay lockedItemOverlay;
	private final RegionLockerOverlay regionLockerOverlay;

	@Inject
	private OverlayHandler(OverlayManager overlayManager, CurrentDateOverlay currentDateOverlay, LockedItemOverlay lockedItemOverlay, RegionLockerOverlay regionLockerOverlay)
	{
		this.overlayManager = overlayManager;
		this.currentDateOverlay = currentDateOverlay;
		this.lockedItemOverlay = lockedItemOverlay;
		this.regionLockerOverlay = regionLockerOverlay;
	}

	public void addOverlays()
	{
		overlayManager.add(currentDateOverlay);
		overlayManager.add(lockedItemOverlay);
		overlayManager.add(regionLockerOverlay);
		lockedItemOverlay.invalidateCache();
	}

	public void removeOverlays()
	{
		overlayManager.remove(currentDateOverlay);
		overlayManager.remove(lockedItemOverlay);
		overlayManager.remove(regionLockerOverlay);
		lockedItemOverlay.invalidateCache();
	}

	public void update(int currentDate)
	{
		lockedItemOverlay.invalidateCache();
		currentDateOverlay.setCurrentDate(currentDate);
		lockedItemOverlay.setCurrentDate(currentDate);
	}

	public void startUp()
	{
		addOverlays();
	}

	public void shutDown()
	{
		removeOverlays();
	}
}
