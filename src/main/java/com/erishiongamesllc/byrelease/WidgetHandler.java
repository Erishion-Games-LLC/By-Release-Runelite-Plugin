package com.erishiongamesllc.byrelease;

import com.erishiongamesllc.byrelease.data.ByReleasePrayer;
import com.erishiongamesllc.byrelease.data.ByReleaseSkill;
import com.erishiongamesllc.byrelease.data.ByReleaseStandardSpell;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.inject.Inject;
import lombok.Getter;
import net.runelite.api.Client;
import net.runelite.api.events.WidgetLoaded;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetID;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.api.widgets.WidgetType;
import net.runelite.client.eventbus.Subscribe;

public class WidgetHandler
{
	@Inject
	private Client client;
	@Inject
	private ByReleasePlugin byReleasePlugin;
	@Inject
	private ByReleaseConfig config;

	private final HashMap<String, List<Widget>> skillWidgets = new HashMap<>();
	private final ArrayList<ByReleasePrayer> prayersFromMagicRSC = new ArrayList<>(Arrays.asList(ByReleasePrayer.THICK_SKIN, ByReleasePrayer.BURST_OF_STRENGTH, ByReleasePrayer.ROCK_SKIN));
	private final ArrayList<ByReleaseStandardSpell> spellsFromInitialRSC = new ArrayList<>(Arrays.asList(ByReleaseStandardSpell.WIND_STRIKE, ByReleaseStandardSpell.CONFUSE, ByReleaseStandardSpell.WATER_STRIKE));


	@Getter
	private final Set<String> nonReleasedPrayerNames = new HashSet<>();
	@Getter
	private final Set<String> nonReleasedSkillNames = new HashSet<>();
	@Getter
	private final Set<String> nonReleasedSpellNames = new HashSet<>();

	@Subscribe
	public void onWidgetLoaded(WidgetLoaded widgetLoaded)
	{
		if (widgetLoaded.getGroupId() == WidgetInfo.RESIZABLE_VIEWPORT_PRAYER_TAB.getGroupId() || widgetLoaded.getGroupId() == WidgetInfo.FIXED_VIEWPORT_PRAYER_TAB.getGroupId())
		{
			updatePrayerWidgets();
		}
		if (widgetLoaded.getGroupId() == WidgetID.PRAYER_GROUP_ID)
		{
			Widget temp = client.getWidget(WidgetID.PRAYER_GROUP_ID, 3);
			assert temp != null;
			updatePrayerWidgets();
		}
	}

	public void setUp()
	{
		createHiddenSkillWidgets();
		updateSkillWidgetsVisibility();
		updatePrayerWidgets();
		updateSpellWidgets();
	}

	public void update()
	{
		updateSkillWidgetsVisibility();
		updatePrayerWidgets();
		updateSpellWidgets();
	}

	public void shutDown()
	{
		nonReleasedPrayerNames.clear();
		nonReleasedSkillNames.clear();
		nonReleasedSpellNames.clear();
	}

	private void createHiddenSkillWidgets()
	{
		for (ByReleaseSkill skill : ByReleaseSkill.values())
		{
			Widget skillWidget = client.getWidget(skill.getWidgetID());

			if (skillWidget == null)
			{
				return;
			}

			ArrayList<Widget> skillWidgetChildren = new ArrayList<>();

			Widget skillIconWidget = createWidget(skillWidget, 50, 174, -2, -2,36, 36, 90, true);
			Widget skillLevelWidget = createWidget(skillWidget, 51, 176, 28, -2,36, 36, 90, true);

			skillWidgetChildren.add(skillIconWidget);
			skillWidgetChildren.add(skillLevelWidget);

			skillWidgets.put(skill.getName(), skillWidgetChildren);
		}
	}

	private Widget createWidget(Widget parent, int index, int spriteId, int posX, int posY, int size1, int size2, int opacity, boolean hidden)
	{
		Widget childWidget = parent.createChild(index, WidgetType.GRAPHIC);
		childWidget.setSpriteId(spriteId);
		childWidget.setSize(size1, size2);
		childWidget.setPos(posX, posY);
		childWidget.setOpacity(opacity);
		childWidget.setHidden(hidden);
		return childWidget;
	}

	private void updateSkillWidgetsVisibility()
	{
		int currentDate = byReleasePlugin.getCurrentDate();

		for (ByReleaseSkill skill : ByReleaseSkill.values())
		{
			boolean isReleased = skill.getReleaseDate() <= currentDate;

			for (Widget skillWidgetChild : skillWidgets.get(skill.getName()))
			{
				skillWidgetChild.setHidden(!isReleased);
				updateNonReleasedSkillNames(skill.getName(), isReleased);
			}
		}
	}

	private void updateNonReleasedSkillNames(String skillName, boolean isReleased)
	{
		if (isReleased)
		{
			nonReleasedSkillNames.remove(skillName);
		} else
		{
			nonReleasedSkillNames.add(skillName);
		}
	}

	public void removeSkillWidgets()
	{
		for (ByReleaseSkill skill : ByReleaseSkill.values())
		{
			Widget skillWidget = client.getWidget(skill.getWidgetID());
			if (skillWidget == null)
			{
				continue;
			}
			skillWidget.deleteAllChildren();
			client.createScriptEvent(skillWidget.getOnLoadListener()).setSource(skillWidget).run();
		}
		skillWidgets.clear();
	}


	public void updatePrayerWidgets()
	{
		int currentDate = byReleasePlugin.getCurrentDate();

		for (ByReleasePrayer prayer : ByReleasePrayer.values())
		{
			Widget individualPrayerWidgetContainer = client.getWidget(prayer.getWidgetID());

			if (individualPrayerWidgetContainer == null)
			{
				continue;
			}

			boolean isReleased = prayer.getReleaseDate() <= currentDate;

			updatePrayerVisibility(individualPrayerWidgetContainer, prayer, isReleased);

			individualPrayerWidgetContainer.revalidate();
		}
	}

	private void updatePrayerVisibility(Widget prayerWidget, ByReleasePrayer prayer, boolean isReleased)
	{
		String name = prayer.getName();

		if (isReleased)
		{
			prayerWidget.setHidden(false);
			nonReleasedPrayerNames.remove(name);
		}
		else if (!config.prayersFromMagicRSC())
		{
			prayerWidget.setHidden(true);
			nonReleasedPrayerNames.add(name);
		}
		else if (prayersFromMagicRSC.contains(prayer) && !(prayer == ByReleasePrayer.ROCK_SKIN && byReleasePlugin.getCurrentDate() < 20010127))
		{
			prayerWidget.setHidden(false);
			nonReleasedPrayerNames.remove(name);
		}
		else
		{
			prayerWidget.setHidden(true);
			nonReleasedPrayerNames.add(name);
		}
	}

	public void restoreDefaultPrayerWidgets()
	{
		Widget widget = client.getWidget(35454976);
		if (widget == null)
		{
			return;
		}
		client.createScriptEvent(widget.getOnLoadListener()).setSource(widget).run();
	}

	//Varbit 4070
	//0 standard
	//1 ancient
	//2 lunar
	//3 arceuus
	public void updateSpellWidgets()
	{
		System.out.println("update spell widgets");
		switch (client.getVarbitValue(4070))
		{
			case 0:
				updateStandardSpellbook();
				break;
			case 1:
				updateAncientSpellbook();
				break;
		}
	}

	//works, need to fix issue where spells are not being displayed properly if dont have runes but is past released date
	private void restoreDefaultSpellWidgets()
	{
		Widget widget = client.getWidget(14286848);
		if (widget == null)
		{
			return;
		}
		client.createScriptEvent(widget.getOnLoadListener()).setSource(widget).run();
	}

	private void updateStandardSpellbook()
	{
		int currentDate = byReleasePlugin.getCurrentDate();
		System.out.println("updated standard spell book");
		for (ByReleaseStandardSpell spell : ByReleaseStandardSpell.values())
		{
			Widget spellWidget = client.getWidget(spell.getWidgetID());
			if (spellWidget == null)
			{
				System.out.println("spell widget is null for: " + spell.getName());
				continue;
			}
			//spell is not released
			if (spell.getReleaseDate() > currentDate)
			{
				if(config.spellsFromInitialRSC() && spellsFromInitialRSC.contains(spell))
				{
					if (spell == ByReleaseStandardSpell.WATER_STRIKE && currentDate < 20010127)
					{
//						spellWidget.setSpriteId(spell.getLockedSpriteID());
						nonReleasedSpellNames.add(spell.getName());
					}
					else
					{
//						spellWidget.setSpriteId(spell.getUnlockedSpriteID());
						nonReleasedSpellNames.remove(spell.getName());
					}
				}
				else
				{
//					spellWidget.setSpriteId(spell.getLockedSpriteID());
					nonReleasedSpellNames.add(spell.getName());
				}
			}
			else
			{
//				spellWidget.setSpriteId(spell.getUnlockedSpriteID());
				nonReleasedSpellNames.remove(spell.getName());
			}
		}
	}

	private void updateAncientSpellbook()
	{
		System.out.println("updated ancient spell book");
	}
}