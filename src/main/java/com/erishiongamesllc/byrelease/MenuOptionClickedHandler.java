package com.erishiongamesllc.byrelease;

import com.erishiongamesllc.byrelease.data.ByReleaseAchievementDiaryTeleports;
import com.erishiongamesllc.byrelease.data.ByReleaseAnvil;
import com.erishiongamesllc.byrelease.data.ByReleaseFurnace;
import com.erishiongamesllc.byrelease.data.ByReleaseInfo;
import com.erishiongamesllc.byrelease.data.ByReleaseItem;
import com.erishiongamesllc.byrelease.data.ByReleasePrayer;
import com.erishiongamesllc.byrelease.data.ByReleaseStandardSpell;
import com.erishiongamesllc.byrelease.data.ByReleaseTree;
import com.erishiongamesllc.byrelease.data.MenuOption;
import java.text.ParseException;
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
	private int itemID;
	private MenuOptionClicked menuOptionClicked;
	private String menuTarget;
	private MenuOption option;

	@Subscribe
	public void onMenuOptionClicked(MenuOptionClicked clicked) throws ParseException
	{
		menuOptionClicked = clicked;
		option = getMenuOption(menuOptionClicked.getMenuOption());

		if (option == null)
		{
			return;
		}

		menuTarget = Text.removeTags(menuOptionClicked.getMenuTarget());
		itemID = menuOptionClicked.getItemId();


		switch (option)
		{
			//Item is leaving or exiting inventory
			case TAKE:
			case WITHDRAW:
			case DEPOSIT:
			case DROP:
				handlePickupOrDrop();
				break;


			//Prayer and quick prayer
			case ACTIVATE:
			case TOGGLE:
				if (byReleasePlugin.getNonReleasedPrayerNames().contains(menuTarget))
				{
					createUnavailableMessage();
					menuOptionClicked.consume();
				}
				break;


			case CAST:
				if (byReleasePlugin.getNonReleasedSpellNames().contains(menuTarget))
				{
					createUnavailableMessage();
					menuOptionClicked.consume();
				}
				break;


			case SEERS:
			case YANILLE:
			case GRAND_EXCHANGE:
				handleAlternateDiaryTeleports();
				break;


			case SMELT:
				handleSmelt();
				break;

			case CHOP_DOWN:
				handleChopDown();
				break;
			case SMITH:
				handleSmith();
				break;

			case PICKPOCKET:
				if (byReleasePlugin.getNonReleasedSkillNames().contains("Thieving"))
				{
					menuOptionClicked.consume();
				}
				break;
		}
	}

	private void handlePickupOrDrop() throws ParseException
	{
		if (itemID >= 0)
		{
			if (config.allowPickup())
			{
				return;
			}
			else if (!ByReleaseItem.isItemUnlocked(itemID, byReleasePlugin.getCurrentDate()))
			{
				menuOptionClicked.consume();
				createUnavailableMessage();
			}
		}
	}

	public void createUnavailableMessage()
	{
		String unavailable = " is unavailable until: ";
		ByReleaseInfo[] values = null;

		switch (option)
		{
			case ACTIVATE:
			case TOGGLE:
				values = ByReleasePrayer.values();
				break;

			case CAST:
				//only works with standard spellbook atm
				values = ByReleaseStandardSpell.values();
				break;

			case CHOP_DOWN:
				values = ByReleaseTree.values();
				break;

			//message will say varrock/camelot instead of seers etc
			case SEERS:
			case YANILLE:
			case GRAND_EXCHANGE:
				values = ByReleaseAchievementDiaryTeleports.values();
				break;

			case TAKE:
			case DROP:
			case DEPOSIT:
			case WITHDRAW:
				ByReleaseItem item = ByReleaseItem.itemDefinitions.get(itemID);
				createMessage(item.getName() + unavailable + item.getReleaseDate());
				break;
		}

		if (values == null)
		{
			return;
		}

		for (ByReleaseInfo value : values)
		{
			System.out.println(value.getName());
			System.out.println(menuTarget);
			if (value.getName().equals(menuTarget))
			{
				createMessage(value.getName() + unavailable + value.getReleaseDate());
				break;
			}
		}
	}


	private void createUnavailableTileObjectMessage(ByReleaseInfo object)
	{
		String unavailable = " is unavailable until: ";
		createMessage(object.getName() + unavailable + object.getReleaseDate());
	}

	private void handleAlternateDiaryTeleports()
	{
		if (!config.enableDiaryTeleports())
		{
			return;
		}

		int currentDate = byReleasePlugin.getCurrentDate();
		ByReleaseAchievementDiaryTeleports diaryTeleport = ByReleaseAchievementDiaryTeleports.valueOf(option.name());


		if (currentDate < diaryTeleport.getReleaseDate())
		{
			createUnavailableMessage();
			menuOptionClicked.consume();
		}
	}

	private void handleSmelt()
	{
		if (menuTarget.equals("Furnace") && config.enableFurnces())
		{
			final Tile tile = client.getScene().getTiles()[client.getPlane()][menuOptionClicked.getParam0()][menuOptionClicked.getParam1()];
			final WorldPoint location = tile.getWorldLocation();

			for (ByReleaseFurnace furnace : ByReleaseFurnace.values())
			{
				//find the corrosponding furnace by its location, and check if its available
				if (location.equals(furnace.getLocation()) && furnace.getReleaseDate() > byReleasePlugin.getCurrentDate())
				{
					menuOptionClicked.consume();
					createUnavailableTileObjectMessage(furnace);
				}
			}
		}
	}

	//loops through the tree enum twice, once here and once in create unavailable message
	//need to fix that
	private void handleChopDown()
	{
		for (ByReleaseTree tree : ByReleaseTree.values())
		{
			if (tree.getName().equals(menuTarget) && tree.getReleaseDate() > byReleasePlugin.getCurrentDate())
			{
				menuOptionClicked.consume();
				createUnavailableMessage();
				break;
			}
		}
	}

	private void handleSmith()
	{
		if (menuTarget.equals("Anvil") && config.enableAnvils())
		{
			final Tile tile = client.getScene().getTiles()[client.getPlane()][menuOptionClicked.getParam0()][menuOptionClicked.getParam1()];
			final WorldPoint location = tile.getWorldLocation();

			for (ByReleaseAnvil anvil : ByReleaseAnvil.values())
			{
				//find the corrosponding anvil by its location, and check if its available
				if (location.equals(anvil.getLocation()) && anvil.getReleaseDate() > byReleasePlugin.getCurrentDate())
				{
					menuOptionClicked.consume();
					createUnavailableTileObjectMessage(anvil);
				}
			}
		}
	}

	private MenuOption getMenuOption(String menuOption)
	{
		for (MenuOption option : MenuOption.values())
		{
			if (option.getMenuOption().equals(menuOption))
			{
				return option;
			}
		}
		return null;
	}

	private void createMessage(String message)
	{
		client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", message, null);
	}
}