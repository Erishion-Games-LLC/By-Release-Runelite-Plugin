package com.erishiongamesllc.byrelease.handlers;

import com.erishiongamesllc.byrelease.ByReleaseConfig;
import com.erishiongamesllc.byrelease.ByReleasePlugin;
import com.erishiongamesllc.byrelease.data.enums.ByReleasePrayer;
import com.erishiongamesllc.byrelease.data.enums.ByReleaseQuest;
import com.erishiongamesllc.byrelease.data.enums.ByReleaseSkill;
import com.erishiongamesllc.byrelease.data.enums.ByReleaseStandardSpell;
import com.erishiongamesllc.byrelease.data.interfaces.ByReleaseInfo;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.Getter;
import lombok.Setter;
import net.runelite.api.Client;
import net.runelite.api.ScriptID;
import net.runelite.api.Varbits;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.api.events.ScriptPostFired;
import net.runelite.api.events.VarbitChanged;
import net.runelite.api.events.WidgetLoaded;
import net.runelite.api.widgets.ComponentID;
import net.runelite.api.widgets.InterfaceID;
import net.runelite.api.widgets.Widget;
import net.runelite.client.eventbus.Subscribe;

@Singleton
public class WidgetHandler
{
	private final Client client;
	private final ByReleasePlugin byReleasePlugin;
	private final ByReleaseConfig byReleaseConfig;

	private final ArrayList<ByReleasePrayer> prayersFromMagicRSC = new ArrayList<>(Arrays.asList(ByReleasePrayer.THICK_SKIN, ByReleasePrayer.BURST_OF_STRENGTH, ByReleasePrayer.ROCK_SKIN));
	private final ArrayList<String> spellsFromInitialRSC = new ArrayList<>(Arrays.asList(ByReleaseStandardSpell.WIND_STRIKE.getName(), ByReleaseStandardSpell.CONFUSE.getName(), ByReleaseStandardSpell.WATER_STRIKE.getName()));
	private final ArrayList<String> enchantmentSpells = new ArrayList<>(Arrays.asList(
		ByReleaseStandardSpell.LVL_1_ENCHANT.getName(), ByReleaseStandardSpell.LVL_2_ENCHANT.getName(),
		ByReleaseStandardSpell.LVL_3_ENCHANT.getName(), ByReleaseStandardSpell.LVL_4_ENCHANT.getName(),
		ByReleaseStandardSpell.LVL_5_ENCHANT.getName(), ByReleaseStandardSpell.LVL_6_ENCHANT.getName(), ByReleaseStandardSpell.LVL_7_ENCHANT.getName()));
	private final HashMap<ByReleaseInfo, Integer> f2pLockSpellIconsHashMap = new HashMap<>();

	@Getter
	private final Set<String> nonReleasedSkillNames = new HashSet<>();
	@Getter
	private final Set<String> nonReleasedPrayerNames = new HashSet<>();
	@Getter
	private final Set<String> nonReleasedSpellNames = new HashSet<>();

	private boolean isSetUpCompleted = false;
	private final int startingDate = ByReleaseQuest.COOKS_ASSISTANT.getReleaseDate();
	@Setter
	private int currentDate = startingDate;

	@Inject
	private WidgetHandler(Client client, ByReleasePlugin byReleasePlugin, ByReleaseConfig byReleaseConfig)
	{
		this.client = client;
		this.byReleasePlugin = byReleasePlugin;
		this.byReleaseConfig = byReleaseConfig;
	}


	@Subscribe
	public void onWidgetLoaded(WidgetLoaded widgetLoaded)
	{
		//if the client is attempting to load the prayer or quick prayer interface, make sure that the prayer widgets are correctly altered.
		if (widgetLoaded.getGroupId() == InterfaceID.PRAYER)
		{
			Widget temp = client.getWidget(InterfaceID.PRAYER, 3);
			assert temp != null;
			updatePrayerWidgets();
		}
		//quick prayers use different widgets than the normal prayers, so this needs to be revamped to fix that.
		if (widgetLoaded.getGroupId() == InterfaceID.QUICK_PRAYER)
		{
			Widget temp = client.getWidget(InterfaceID.QUICK_PRAYER, 1);
			assert temp != null;
			updatePrayerWidgets();
		}
	}

	@Subscribe
	public void onScriptPostFired(ScriptPostFired scriptPostFired)
	{
		if (!isSetUpCompleted)
		{
			return;
		}
		switch (scriptPostFired.getScriptId())
		{
			//prayer widgets could have been overwritten, reupdate just in case
			case ScriptID.PRAYER_UPDATEBUTTON:
			case ScriptID.PRAYER_REDRAW:
			case ScriptID.QUICKPRAYER_INIT:
				updatePrayerWidgets();
				break;

			//spells
			case 2262:
			case 2607:
			case 2609:
			case 2610:
			case 2617:
				System.out.println("UPDATE SPELL WIDGETS FROM: onScriptPostFired");
				updateSpellWidgets();
				break;
		}
	}

	@Subscribe
	public void onVarbitChanged(VarbitChanged varbitChanged)
	{
		if (varbitChanged.getVarbitId() == Varbits.SPELLBOOK)
		{
			updateSpellWidgets();
		}
	}

	public void setUp(int currentDate)
	{
		this.currentDate = currentDate;
		fillF2pLockSpellIconsHashMap();
		updatePrayerWidgets();
		updateSkillWidgets();
		updateSpellWidgets();
		isSetUpCompleted = true;
	}

	public void update(int currentDate)
	{
		this.currentDate = currentDate;
		updatePrayerWidgets();
		updateSkillWidgets();
		updateSpellWidgets();
	}

	public void shutDown()
	{
		restoreDefaultPrayerWidgets();
		restoreDefaultSkillWidgets();
		nonReleasedPrayerNames.clear();
		nonReleasedSkillNames.clear();
		nonReleasedSpellNames.clear();
		restoreDefaultSpellWidgets();
	}

	private void fillF2pLockSpellIconsHashMap()
	{
		f2pLockSpellIconsHashMap.put(ByReleaseStandardSpell.CROSSBOW_BOLT_ENCHANTMENTS, 0);
		f2pLockSpellIconsHashMap.put(ByReleaseStandardSpell.TELEPORT_TO_HOUSE, 1);
		f2pLockSpellIconsHashMap.put(ByReleaseStandardSpell.CAMELOT_TELEPORT, 2);
		f2pLockSpellIconsHashMap.put(ByReleaseStandardSpell.KOUREND_CASTLE_TELEPORT, 3);
		f2pLockSpellIconsHashMap.put(ByReleaseStandardSpell.ARDOUGNE_TELEPORT, 4);
		f2pLockSpellIconsHashMap.put(ByReleaseStandardSpell.CIVITAS_ILLA_FORTIS_TELEPORT, 5);
		f2pLockSpellIconsHashMap.put(ByReleaseStandardSpell.WATCHTOWER_TELEPORT, 6);
		f2pLockSpellIconsHashMap.put(ByReleaseStandardSpell.BONES_TO_PEACHES, 7);
		f2pLockSpellIconsHashMap.put(ByReleaseStandardSpell.TROLLHEIM_TELEPORT, 8);
		f2pLockSpellIconsHashMap.put(ByReleaseStandardSpell.ENTANGLE, 9);
	}

	private void updatePrayerWidgets()
	{
		for (ByReleasePrayer prayer : ByReleasePrayer.values())
		{
			//get the widget container holding the prayer icon widget and listener
			Widget individualPrayerWidgetContainer = client.getWidget(prayer.getWidgetID());

			if (individualPrayerWidgetContainer == null)
			{
				continue;
			}

			boolean isReleased = prayer.getReleaseDate() <= currentDate;
			updatePrayersVisibility(individualPrayerWidgetContainer, prayer, isReleased);
			individualPrayerWidgetContainer.revalidate();
		}
	}

	private void updatePrayersVisibility(Widget prayerWidget, ByReleasePrayer prayer, boolean isReleased)
	{
		String name = prayer.getName();
		//if it is released, we do not need to hide it
		if (isReleased)
		{
			prayerWidget.setHidden(false);
			nonReleasedPrayerNames.remove(name);
		}
		//if its not released, and we have enabled the option to use the original spells that were turned into prayers
		//check if this prayer is one of the prayers from the original magic RSC system. If it is, we should not hide the prayer
		else if (byReleaseConfig.prayersFromMagicRSC() && prayersFromMagicRSC.contains(prayer))
		{
			prayerWidget.setHidden(false);
			nonReleasedPrayerNames.remove(name);
		}
		//Here, the prayer is not released, and its not in the special cases, so set it hidden
		else
		{
			prayerWidget.setHidden(true);
			nonReleasedPrayerNames.add(name);
		}
	}

	private void restoreDefaultPrayerWidgets()
	{
		Widget widget = client.getWidget(ComponentID.PRAYER_PARENT);
		if (widget == null)
		{
			return;
		}
		client.createScriptEvent(widget.getOnLoadListener()).setSource(widget).run();
	}

	private void updateSkillWidgets()
	{
		for (ByReleaseSkill skill : ByReleaseSkill.values())
		{
			Widget individualSkillWidgetContainer = client.getWidget(skill.getWidgetID());
			if (individualSkillWidgetContainer == null)
			{
				continue;
			}

			boolean isReleased = skill.getReleaseDate() <= currentDate;

			updateSkillsVisibility(individualSkillWidgetContainer, skill, isReleased);

			individualSkillWidgetContainer.revalidate();
		}
	}

	private void updateSkillsVisibility(Widget skillWidget, ByReleaseSkill skill, boolean isReleased)
	{
		String name = skill.getName();
		if (isReleased)
		{
			skillWidget.setHidden(false);
			nonReleasedSkillNames.remove(name);		}
		else
		{
			skillWidget.setHidden(true);
			nonReleasedSkillNames.add(name);
		}
	}

	private void restoreDefaultSkillWidgets()
	{
		for (ByReleaseSkill skill : ByReleaseSkill.values())
		{
			Widget skillWidget = client.getWidget(skill.getWidgetID());
			if (skillWidget == null)
			{
				continue;
			}
			skillWidget.setHidden(false);
			skillWidget.revalidate();
		}
	}

	public void updateSpellWidgets()
	{
		System.out.println("update spell widgets");
		switch (client.getVarbitValue(Varbits.SPELLBOOK))
		{
			//0 standard
			//1 ancient
			//2 lunar
			//3 arceuus
			case 0:
				updateStandardSpellbookWidgets();
				break;
			case 1:

				break;
		}
	}

	private void updateStandardSpellbookWidgets()
	{
		for (ByReleaseStandardSpell spell : ByReleaseStandardSpell.values())
		{

			Widget spellWidget = client.getWidget(spell.getWidgetID());

			if (spellWidget == null)
			{
				continue;
			}

			boolean isReleased = spell.getReleaseDate() <= currentDate;
			updateSpellsVisibility(spellWidget, spell, isReleased);
			spellWidget.revalidate();
		}
	}

	private void updateSpellsVisibility(Widget spellWidget, ByReleaseInfo spell, boolean isReleased)
	{
		String name = spell.getName();
		//if it is released, we don't need to hide it
		if (isReleased)
		{
			spellWidget.setHidden(false);
			nonReleasedSpellNames.remove(name);
		}
		//if it is not released, check if we are using the spells from RSC before the rework in may 2001.
		//if we are and these are the spells being updated, set them to visible
		else if (byReleaseConfig.spellsFromInitialRSC() && spellsFromInitialRSC.contains(name))
		{
			spellWidget.setHidden(false);
			nonReleasedSpellNames.remove(name);
		}
		else
		{
			//lastly, it's not released, and it's not in the special cases, so set it hidden
			spellWidget.setHidden(true);
			nonReleasedSpellNames.add(name);
		}

		int spellbookSubMenu = client.getVarbitValue(Varbits.SPELLBOOK_SUBMENU);

		//if the subspell enchantment menu is not open, and the spell being updated is an enchantment spell, set it hidden.
		if (spellbookSubMenu == 0 && enchantmentSpells.contains(spell.getName()))
		{

			spellWidget.setHidden(true);
		}
		//if the subspell enchantment menu is open, and the spell being updated is not enchantment spell, set it hidden.
		else if (spellbookSubMenu == 1 && !enchantmentSpells.contains(spell.getName()))
		{
			spellWidget.setHidden(true);
		}

		if (f2pLockSpellIconsHashMap.containsKey(spell) && spellWidget.isHidden())
		{
			int spellContainerWidgetID = 14286851;
			Widget spellContainerWidget = client.getWidget(spellContainerWidgetID);
			if (spellContainerWidget == null)
			{
				return;
			}
			Widget lockSpellIcon = spellContainerWidget.getChild(f2pLockSpellIconsHashMap.get(spell));
			if (lockSpellIcon == null)
			{
				return;
			}
			lockSpellIcon.setHidden(true);
		}
	}
	
	private void restoreDefaultSpellWidgets()
	{
		for (ByReleaseStandardSpell spell: ByReleaseStandardSpell.values())
		{
			Widget spellWidget = client.getWidget(spell.getWidgetID());
			if (spellWidget == null)
			{
				continue;
			}

			if (f2pLockSpellIconsHashMap.containsKey(spell) && spellWidget.isHidden())
			{
				int spellContainerWidgetID = 14286851;
				Widget spellContainerWidget = client.getWidget(spellContainerWidgetID);
				if (spellContainerWidget == null)
				{
					return;
				}
				Widget lockSpellIcon = spellContainerWidget.getChild(f2pLockSpellIconsHashMap.get(spell));
				if (lockSpellIcon == null)
				{
					return;
				}
				lockSpellIcon.setHidden(false);
			}

			int spellbookSubMenu = client.getVarbitValue(Varbits.SPELLBOOK_SUBMENU);
			//plugin was turned off when enchantment menu was closed and the spell is an enchantment spell that should be displayed on the main page. set it hidden
			if (spellbookSubMenu == 0 && enchantmentSpells.contains(spell.getName()))
			{
				spellWidget.setHidden(true);
				spellWidget.revalidate();
			}
			//plugin was turned off when enchantment menu was closed and the spell is not an enchantment spell. it should be displayed on main page, set it visible.
			else if (spellbookSubMenu == 0 && !enchantmentSpells.contains(spell.getName()))
			{
				spellWidget.setHidden(false);
				spellWidget.revalidate();
			}
		}
	}
}