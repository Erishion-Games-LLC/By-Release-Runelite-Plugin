package com.erishiongamesllc.byrelease.handlers;

import com.erishiongamesllc.byrelease.ByReleaseConfig;
import com.erishiongamesllc.byrelease.ByReleasePlugin;
import com.erishiongamesllc.byrelease.data.enums.ByReleasePrayer;
import com.erishiongamesllc.byrelease.data.enums.ByReleaseQuest;
import com.erishiongamesllc.byrelease.data.enums.ByReleaseSkill;
import com.erishiongamesllc.byrelease.data.enums.ByReleaseStandardSpell;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.Getter;
import lombok.Setter;
import net.runelite.api.Client;
import net.runelite.api.ScriptID;
import net.runelite.api.Varbits;
import net.runelite.api.events.ScriptPostFired;
import net.runelite.api.events.VarbitChanged;
import net.runelite.api.events.WidgetLoaded;
import net.runelite.api.widgets.ComponentID;
import net.runelite.api.widgets.InterfaceID;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetID;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.api.widgets.WidgetType;
import net.runelite.client.eventbus.Subscribe;

@Singleton
public class WidgetHandler
{
	private final Client client;
	private final ByReleasePlugin byReleasePlugin;
	private final ByReleaseConfig byReleaseConfig;

	private final HashMap<String, List<Widget>> skillWidgets = new HashMap<>();
	private final ArrayList<ByReleasePrayer> prayersFromMagicRSC = new ArrayList<>(Arrays.asList(ByReleasePrayer.THICK_SKIN, ByReleasePrayer.BURST_OF_STRENGTH, ByReleasePrayer.ROCK_SKIN));
	private final ArrayList<ByReleaseStandardSpell> spellsFromInitialRSC = new ArrayList<>(Arrays.asList(ByReleaseStandardSpell.WIND_STRIKE, ByReleaseStandardSpell.CONFUSE, ByReleaseStandardSpell.WATER_STRIKE));

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
			//prayers
			case ScriptID.PRAYER_UPDATEBUTTON:
			case ScriptID.PRAYER_REDRAW:
			case ScriptID.QUICKPRAYER_INIT:
				updatePrayerWidgets();
				break;

			//spells
//			case 2262:
//			case 2607:
//			case 2609:
//			case 2610:
//			case 2617:
//				updateSpellWidgets();
		}
	}

	@Subscribe
	public void onVarbitChanged(VarbitChanged varbitChanged)
	{
		if (varbitChanged.getVarbitId() == Varbits.SPELLBOOK)
		{
//			updateSpellWidgets();
		}
	}

	public void setUp(int currentDate)
	{
		updatePrayerWidgets();
//		createHiddenSkillWidgets();
//		updateSkillWidgetsVisibility();
//		updateSpellWidgets();
		isSetUpCompleted = true;
	}

	public void update()
	{
		updatePrayerWidgets();
//		updateSkillWidgetsVisibility();
//		updateSpellWidgets();
	}

	public void shutDown()
	{
		restoreDefaultPrayerWidgets();
		nonReleasedPrayerNames.clear();
		nonReleasedSkillNames.clear();
		nonReleasedSpellNames.clear();
//		removeSkillWidgets();
		//restoreDefaultSpellWidgets();
	}

	public void updatePrayerWidgets()
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

			updatePrayerVisibility(individualPrayerWidgetContainer, prayer, isReleased);

			individualPrayerWidgetContainer.revalidate();
		}
	}

	private void updatePrayerVisibility(Widget prayerWidget, ByReleasePrayer prayer, boolean isReleased)
	{
		String name = prayer.getName();
		//if it is released, we do not need to hide it
		if (isReleased)
		{
			prayerWidget.setHidden(false);
			nonReleasedPrayerNames.remove(name);
		}
		//if its not released, we check if we have enabled the option to use the original spells that were turned into prayers
		//if we haven't enabled it and its also not released, we set it to hidden
		else if (!byReleaseConfig.prayersFromMagicRSC())
		{
			prayerWidget.setHidden(true);
			nonReleasedPrayerNames.add(name);
		}
		//if its not released, and we have enabled the option to use the original spells that were turned into prayers
		//check if this prayer is one of the prayers from the original magic RSC system. If it is, we should not hide the prayer
		else if (prayersFromMagicRSC.contains(prayer))
		{
			prayerWidget.setHidden(false);
			nonReleasedPrayerNames.remove(name);
		}
		//Here, the prayer is not released, we have enabled the prayersFromMagicRSC option but the prayer is not one of them.
		//so we set it to be hidden.
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

//	private void createHiddenSkillWidgets()
//	{
//		for (ByReleaseSkill skill : ByReleaseSkill.values())
//		{
//			Widget skillWidget = client.getWidget(skill.getWidgetID());
//
//			if (skillWidget == null)
//			{
//				return;
//			}
//
//			ArrayList<Widget> skillWidgetChildren = new ArrayList<>();
//
//			Widget skillIconWidget = createWidget(skillWidget, 50, 174, -2, -2,36, 36, 90, true);
//			Widget skillLevelWidget = createWidget(skillWidget, 51, 176, 28, -2,36, 36, 90, true);
//
//			skillWidgetChildren.add(skillIconWidget);
//			skillWidgetChildren.add(skillLevelWidget);
//
//			skillWidgets.put(skill.getName(), skillWidgetChildren);
//		}
//	}

//	private Widget createWidget(Widget parent, int index, int spriteId, int posX, int posY, int size1, int size2, int opacity, boolean hidden)
//	{
//		Widget childWidget = parent.createChild(index, WidgetType.GRAPHIC);
//		childWidget.setSpriteId(spriteId);
//		childWidget.setSize(size1, size2);
//		childWidget.setPos(posX, posY);
//		childWidget.setOpacity(opacity);
//		childWidget.setHidden(hidden);
//		return childWidget;
//	}
//
//	private void updateSkillWidgetsVisibility()
//	{
//				for (ByReleaseSkill skill : ByReleaseSkill.values())
//		{
//			boolean isReleased = skill.getReleaseDate() <= currentDate;
//
//			for (Widget skillWidgetChild : skillWidgets.get(skill.getName()))
//			{
//				skillWidgetChild.setHidden(!isReleased);
//				updateNonReleasedSkillNames(skill.getName(), isReleased);
//			}
//		}
//	}
//
//	private void updateNonReleasedSkillNames(String skillName, boolean isReleased)
//	{
//		if (isReleased)
//		{
//			nonReleasedSkillNames.remove(skillName);
//		} else
//		{
//			nonReleasedSkillNames.add(skillName);
//		}
//	}
//
//	public void removeSkillWidgets()
//	{
//		for (ByReleaseSkill skill : ByReleaseSkill.values())
//		{
//			Widget skillWidget = client.getWidget(skill.getWidgetID());
//			if (skillWidget == null)
//			{
//				continue;
//			}
//			skillWidget.deleteAllChildren();
//			client.createScriptEvent(skillWidget.getOnLoadListener()).setSource(skillWidget).run();
//		}
//		skillWidgets.clear();
//	}
//
//

//

//

//
//	//Varbit 4070
//	//0 standard
//	//1 ancient
//	//2 lunar
//	//3 arceuus
//	public void updateSpellWidgets()
//	{
//		System.out.println("update spell widgets");
//		switch (client.getVarbitValue(4070))
//		{
//			case 0:
//				updateStandardSpellbook();
//				break;
//			case 1:
//
//				break;
//		}
//	}
//
//	//works, need to fix issue where spells are not being displayed properly if dont have runes but is past released date
//	private void restoreDefaultSpellWidgets()
//	{
//		Widget widget = client.getWidget(14286848);
//		if (widget == null)
//		{
//			return;
//		}
//		client.createScriptEvent(widget.getOnLoadListener()).setSource(widget).run();
//	}
//
//	private void updateStandardSpellbook()
//	{
//		System.out.println("updated standard spell book");
//		for (ByReleaseStandardSpell spell : ByReleaseStandardSpell.values())
//		{
//			Widget spellWidget = client.getWidget(spell.getWidgetID());
//			if (spellWidget == null)
//			{
//				System.out.println("spell widget is null for: " + spell.getName());
//				continue;
//			}
//			//spell is not released
//			if (spell.getReleaseDate() > currentDate)
//			{
//				if(byReleaseConfig.spellsFromInitialRSC() && spellsFromInitialRSC.contains(spell))
//				{
//					if (spell == ByReleaseStandardSpell.WATER_STRIKE && currentDate < 20010127)
//					{
////						spellWidget.setSpriteId(spell.getLockedSpriteID());
//						nonReleasedSpellNames.add(spell.getName());
//					}
//					else
//					{
////						spellWidget.setSpriteId(spell.getUnlockedSpriteID());
//						nonReleasedSpellNames.remove(spell.getName());
//					}
//				}
//				else
//				{
////					spellWidget.setSpriteId(spell.getLockedSpriteID());
//					nonReleasedSpellNames.add(spell.getName());
//				}
//			}
//			else
//			{
+////				spellWidget.setSpriteId(spell.getUnlockedSpriteID());
//				nonReleasedSpellNames.remove(spell.getName());
//			}
//		}
//	}
}