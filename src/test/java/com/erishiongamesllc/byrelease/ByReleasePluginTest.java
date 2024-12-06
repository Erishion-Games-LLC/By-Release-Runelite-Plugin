package com.erishiongamesllc.byrelease;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class ByReleasePluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin
			(
				ByReleasePlugin.class
			);
		RuneLite.main(args);
	}
}