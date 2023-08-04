package com.erishiongamesllc.byrelease;

import com.erishiongamesllc.byrelease.data.ByReleaseAnvil;
import com.erishiongamesllc.byrelease.data.ByReleasePrayer;
import com.erishiongamesllc.byrelease.data.ByReleaseStandardSpell;
import com.erishiongamesllc.byrelease.data.ByReleaseTree;
import com.erishiongamesllc.byrelease.data.MenuOptions;
import java.text.ParseException;
import java.util.Objects;
import javax.inject.Inject;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.Tile;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.util.Text;

public class MenuOptionClickedHandler
{
	@Inject
	private ByReleasePlugin byReleasePlugin;
	@Inject
	private Client client;
	@Inject
	private ByReleaseConfig config;

	@Subscribe
	public void onMenuOptionClicked(MenuOptionClicked menuOptionClicked) throws ParseException
	{
		String menuOption = menuOptionClicked.getMenuOption();
		String menuTarget = Text.removeTags(menuOptionClicked.getMenuTarget());
		int itemID = menuOptionClicked.getItemId();


		if (itemID >= 0)
		{
			if (config.allowPickup() && (Objects.equals(menuOption, "Take") || menuOption.contains("Withdraw") || menuOption.contains("Deposit") || menuOption.contains("Drop")))
			{
				return;
			}
			else if (!byReleasePlugin.isItemUnlocked(itemID))
			{
				System.out.println("CONSUME");
				menuOptionClicked.consume();
				client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "This item was released after: " + byReleasePlugin.getCurrentDate(), null);
				return;
			}
			else
			{
				System.out.println("Is not locked");
			}
		}
		switch (menuOption)
		{
			case "Pickpocket":
				if (byReleasePlugin.getNonReleasedSkillNames().contains("Thieving"))
				{
					menuOptionClicked.consume();
				}
				break;
			case "Activate":
			case "Toggle":
				if (byReleasePlugin.getNonReleasedPrayerNames().contains(menuTarget))
				{
					createUnavailableMessage(menuTarget, MenuOptions.TOGGLE);
					menuOptionClicked.consume();
				}
				break;
			case "Cast":
				if (byReleasePlugin.getNonReleasedSpellNames().contains(menuTarget))
				{
					createUnavailableMessage(menuTarget, MenuOptions.CAST);
					menuOptionClicked.consume();
				}
				break;
			case "Seers'":
			case "Yanille":
				if (byReleasePlugin.getCurrentDate() < 20150305)
				{
					menuOptionClicked.consume();
					client.addChatMessage(ChatMessageType.GAMEMESSAGE,
						"", menuOption + " teleport is unavailable until: " + 20150305, null);
				}
				break;
			case "Grand Exchange":
				if (byReleasePlugin.getCurrentDate() < 20150430)
				{
					menuOptionClicked.consume();
					client.addChatMessage(ChatMessageType.GAMEMESSAGE,
						"", "Grand Exchange teleport is unavailable until: " + 20150430, null);
				}
				break;
			case "Smelt":
				if (menuOptionClicked.getId() == 39620 && byReleasePlugin.getCurrentDate() < 20200709)
				{
					createUnavailableMessage(menuTarget, MenuOptions.SMELT);
					menuOptionClicked.consume();
				}
				break;
			case "Chop down":
				handleChopDown(menuTarget, menuOptionClicked);
				break;
			case "Smith":
			{
				handleSmith(menuTarget, menuOptionClicked);
				break;
			}
		}
	}

	public void createUnavailableMessage(String target, MenuOptions type)
	{
		switch (type)
		{
			case TOGGLE:
				for (ByReleasePrayer prayer : ByReleasePrayer.values())
				{
					if (prayer.getName().equals(target))
					{
						client.addChatMessage(ChatMessageType.GAMEMESSAGE,
							"", prayer.getName() + " is unavailable until: " + prayer.getReleaseDate(), null);
						break;
					}
				}
				break;
			case CAST:
				for (ByReleaseStandardSpell spell : ByReleaseStandardSpell.values())
				{
					if (spell.getName().equals(target))
					{
						client.addChatMessage(ChatMessageType.GAMEMESSAGE,
							"", spell.getName() + " is unavailable until: " + spell.getReleaseDate(), null);
						break;
					}
				}
				break;
			case CHOP_DOWN:
				for (ByReleaseTree tree : ByReleaseTree.values())
				{
					if (tree.getName().equals(target))
					{
						client.addChatMessage(ChatMessageType.GAMEMESSAGE,
							"", tree.getName() + "is unavailable until: " + tree.getReleaseDate(), null);
						break;
					}
				}
				break;
		}
	}

	private void handleChopDown(String menuTarget, MenuOptionClicked menuOptionClicked)
	{
		for (ByReleaseTree tree : ByReleaseTree.values())
		{
			if (tree.getName().equals(menuTarget) && tree.getReleaseDate() > byReleasePlugin.getCurrentDate())
			{
				menuOptionClicked.consume();
				createUnavailableMessage(menuTarget, MenuOptions.CHOP_DOWN);
			}
		}
	}

	private void handleSmith(String menuTarget, MenuOptionClicked menuOptionClicked)
	{
		if (menuTarget.equals("Anvil") && config.enableAnvils())
		{
			final Tile tile = client.getScene().getTiles()[client.getPlane()][menuOptionClicked.getParam0()][menuOptionClicked.getParam1()];
			final WorldPoint location = tile.getWorldLocation();

			for (ByReleaseAnvil anvil : ByReleaseAnvil.values())
			{
				if (location.equals(anvil.getLocation()) && anvil.getReleaseDate() > byReleasePlugin.getCurrentDate())
				{
					menuOptionClicked.consume();
					client.addChatMessage(ChatMessageType.GAMEMESSAGE,
						"", "This anvil is unavailable until: " + anvil.getReleaseDate(), null);
				}
			}
		}
	}
}
