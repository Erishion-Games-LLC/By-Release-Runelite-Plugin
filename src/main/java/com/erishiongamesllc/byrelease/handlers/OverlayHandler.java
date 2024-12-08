package com.erishiongamesllc.byrelease.handlers;

import com.erishiongamesllc.byrelease.overlay.ByReleaseDateOverlay;
import com.erishiongamesllc.byrelease.overlay.ByReleaseItemOverlay;
import com.erishiongamesllc.regionlocker.RegionLockerOverlay;
import javax.inject.Inject;
import net.runelite.client.ui.overlay.OverlayManager;

public class OverlayHandler
{
	private final OverlayManager overlayManager;
	private final ByReleaseDateOverlay byReleaseDateOverlay;
	private final ByReleaseItemOverlay byReleaseItemOverlay;
	private final RegionLockerOverlay regionLockerOverlay;

	@Inject
	private OverlayHandler(OverlayManager overlayManager, ByReleaseDateOverlay byReleaseDateOverlay, ByReleaseItemOverlay byReleaseItemOverlay, RegionLockerOverlay regionLockerOverlay)
	{
		this.overlayManager = overlayManager;
		this.byReleaseDateOverlay = byReleaseDateOverlay;
		this.byReleaseItemOverlay = byReleaseItemOverlay;
		this.regionLockerOverlay = regionLockerOverlay;
	}

	public void addOverlays()
	{
		overlayManager.add(byReleaseDateOverlay);
		overlayManager.add(byReleaseItemOverlay);
		overlayManager.add(regionLockerOverlay);
		byReleaseItemOverlay.invalidateCache();
	}

	public void removeOverlays()
	{
		overlayManager.remove(byReleaseDateOverlay);
		overlayManager.remove(byReleaseItemOverlay);
		overlayManager.remove(regionLockerOverlay);
		byReleaseItemOverlay.invalidateCache();
	}

	public void update(int currentDate)
	{
		byReleaseItemOverlay.invalidateCache();
		byReleaseDateOverlay.setCurrentDate(currentDate);
		byReleaseItemOverlay.setCurrentDate(currentDate);
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
