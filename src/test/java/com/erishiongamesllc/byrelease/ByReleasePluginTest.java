package com.erishiongamesllc.byrelease;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;
import com.erishiongamesllc.gpu.ByReleaseGpuPlugin;

public class ByReleasePluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin
			(
				ByReleasePlugin.class,
				ByReleaseGpuPlugin.class
			);
		RuneLite.main(args);
	}
}