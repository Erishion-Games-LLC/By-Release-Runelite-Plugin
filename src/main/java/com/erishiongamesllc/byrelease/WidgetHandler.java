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
		createSkillWidgets();
		updateSkillWidgets();
		updatePrayerWidgets();
		updateSpellWidgets();
	}

	public void update()
	{
		updateSkillWidgets();
		updatePrayerWidgets();
		updateSpellWidgets();
	}

	public void shutDown()
	{
		nonReleasedPrayerNames.clear();
		nonReleasedSkillNames.clear();
		nonReleasedSpellNames.clear();
	}

	private void createSkillWidgets()
	{
		for (ByReleaseSkill skill : ByReleaseSkill.values())
		{
			Widget skillWidget = client.getWidget(skill.getWidgetID());

			if (skillWidget == null)
			{
				return;
			}

			ArrayList<Widget> skillWidgetChildren = new ArrayList<>();

			Widget skillIconWidget = skillWidget.createChild(50, WidgetType.GRAPHIC);
			Widget skillLevelWidget = skillWidget.createChild(51, WidgetType.GRAPHIC);


			skillIconWidget.setSpriteId(174);
			skillIconWidget.setSize(36, 36);
			skillIconWidget.setPos(-2, -2);
			skillIconWidget.setOpacity(90);
			skillIconWidget.setHidden(true);

			skillLevelWidget.setSpriteId(176);
			skillLevelWidget.setSize(36, 36);
			skillLevelWidget.setPos(28, -2);
			skillLevelWidget.setOpacity(90);
			skillLevelWidget.setHidden(true);

			skillWidgetChildren.add(skillIconWidget);
			skillWidgetChildren.add(skillLevelWidget);

			skillWidgets.put(skill.getSkill().getName(), skillWidgetChildren);
		}
	}

	private void updateSkillWidgets()
	{

		for (ByReleaseSkill skill : ByReleaseSkill.values())
		{
			for (Widget skillWidgetChild : skillWidgets.get(skill.getSkill().getName()))
			{
				if (skill.getReleaseDate() > byReleasePlugin.getCurrentDate())
				{
					skillWidgetChild.setHidden(false);
					nonReleasedSkillNames.add(skill.getSkill().getName());
				}
				else
				{
					skillWidgetChild.setHidden(true);
					nonReleasedSkillNames.remove(skill.getSkill().getName());
				}
			}
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
			//prayer is not released
			if (prayer.getReleaseDate() > currentDate)
			{
				if (config.prayersFromMagicRSC() && prayersFromMagicRSC.contains(prayer))
				{
					if (prayer == ByReleasePrayer.ROCK_SKIN && currentDate < 20010127)
					{
						System.out.println("HIDE ROCK SKIN: " + currentDate);
						//prayer is rock skin, which was released Jan 27 2001.
						individualPrayerWidgetContainer.setHidden(true);
						nonReleasedPrayerNames.add(prayer.getName());
					}
					else
					{
						individualPrayerWidgetContainer.setHidden(false);
						nonReleasedPrayerNames.remove(prayer.getName());
					}
				}
				else
				{
					individualPrayerWidgetContainer.setHidden(true);
					nonReleasedPrayerNames.add(prayer.getName());
				}
			} else
			{
				individualPrayerWidgetContainer.setHidden(false);
				nonReleasedPrayerNames.remove(prayer.getName());

			}
			individualPrayerWidgetContainer.revalidate();
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
