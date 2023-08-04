package com.erishiongamesllc.byrelease.data;

import lombok.Getter;

@Getter
public enum MenuOptions
{
	ACTIVATE("Activate"),
	TOGGLE("Toggle"),
	CAST("Cast"),
	SEERS("Seers"),
	YANILLE("Yanille"),
	GRAND_EXCHANGE("Grand Exchange"),
	SMELT("Smelt"),
	CHOP_DOWN("Chop down"),
	SMITH("Smith"),
	;

	private final String menuOption;

	MenuOptions(String menuOption)
	{
		this.menuOption = menuOption;
	}
}
