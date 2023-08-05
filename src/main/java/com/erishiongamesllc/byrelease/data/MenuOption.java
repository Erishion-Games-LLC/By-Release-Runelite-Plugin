package com.erishiongamesllc.byrelease.data;

import lombok.Getter;

@Getter
public enum MenuOption
{
	ACTIVATE("Activate"),
	TOGGLE("Toggle"),
	CAST("Cast"),
	SEERS("Seers'"),
	YANILLE("Yanille"),
	GRAND_EXCHANGE("Grand Exchange"),
	SMELT("Smelt"),
	CHOP_DOWN("Chop down"),
	SMITH("Smith"),
	PICKPOCKET("Pickpocket"),
	TAKE("Take"),
	WITHDRAW("Withdraw"),
	DEPOSIT("Deposit"),
	DROP("Drop"),

	;

	private final String menuOption;

	MenuOption(String menuOption)
	{
		this.menuOption = menuOption;
	}
}
