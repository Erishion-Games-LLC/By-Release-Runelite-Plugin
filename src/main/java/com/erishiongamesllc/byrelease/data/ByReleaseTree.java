/*
 *  BSD 2-Clause License
 *  * Copyright (c) 2023, Erishion Games LLC <https://github.com/Erishion-Games-LLC>
 *  * All rights reserved.
 *  *
 *  * Redistribution and use in source and binary forms, with or without
 *  * modification, are permitted provided that the following conditions are met:
 *  *
 *  * 1. Redistributions of source code must retain the above copyright notice, this
 *  *    list of conditions and the following disclaimer.
 *  * 2. Redistributions in binary form must reproduce the above copyright notice,
 *  *    this list of conditions and the following disclaimer in the documentation
 *  *    and/or other materials provided with the distribution.
 *  *
 *  * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 *  * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 *  * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 *  * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 *  * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 *  * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 *  * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 *  * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 *  * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 */
package com.erishiongamesllc.byrelease.data;

import lombok.Getter;

@Getter
public enum ByReleaseTree implements ByReleaseInfo
{
	TREE("Tree", 20010104),
	//Real release date is 20070529. No reason to treat this tree special
	DYING_TREE("Dying tree", 20010104),
	//Real release date is 20020723. No reason to treat this tree special
	DEAD_TREE("Dead tree", 20010104),
	//Real release date is 20040329. No reason to treat this tree special
	EVERGREEN_TREE("Evergreen tree", 20010104),
	JUNGLE_TREE("Jungle tree", 20030820),

	OAK_TREE("Oak tree", 20020325),
	WILLOW_TREE("Willow tree", 20020325),
	MAPLE_TREE("Maple tree", 20020325),
	YEW_TREE("Yew tree", 20020325),
	MAGIC_TREE("Magic tree", 20020325),
	REDWOOD_TREE("Redwood tree", 20160602),

	TEAK_TREE("Teak tree", 20050809),
	MAHOGANY_TREE("Mahogany tree", 20050809),

	ACHEY_TREE("Achey Tree", 20040518),
	HOLLOW_TREE("Hollow tree", 20041018),
	ARCTIC_PINE_TREE("Arctic pine tree", 20070206),
	;

	final String name;
	final int releaseDate;

	ByReleaseTree(String name, int releaseDate)
	{
		this.name = name;
		this.releaseDate = releaseDate;
	}
}
//	OAK_TREE("Oak tree", ObjectID.OAK_TREE_10820, 20020325),
//
//	WILLOW_TREE_10819("Willow tree", ObjectID.WILLOW_TREE_10819, 20020325),
//	//Not actual release date, replaced old willow trees locations so set to initial release date of willow
//	WILLOW_TREE_10829("Willow tree", ObjectID.WILLOW_TREE_10829, 20020325),
//	WILLOW_TREE_10831("Willow tree", ObjectID.WILLOW_TREE_10831, 20020325),
//	WILLOW_TREE_10833("Willow tree", ObjectID.WILLOW_TREE_10833, 20020325),
//
//	TEAK_TREE("Teak tree", ObjectID.TEAK_TREE, 20050809),
//	TEAK_TREE_PRIF("Teak tree", ObjectID.TEAK_TREE_36686, 20190725),
//	TEAK_TREE_IOS("Teak tree", ObjectID.TEAK_TREE_40758, 20210203),
//
//	MAPLE_TREE("Maple tree", ObjectID.MAPLE_TREE_10832, 20020325),
//	MAPLE_TREE_GWENITH("Maple tree", ObjectID.MAPLE_TREE_36681, 20190725),
//	MAPLE_TREE_IOS("Maple tree", ObjectID.MAPLE_TREE_40754, 20210203),
//
//	MAHOGANY_TREE("Mahogany tree", ObjectID.MAHOGANY, 20050809),
//	//	MAHOGANY_TREE_PRIF("Mahogany tree", ObjectID.MAHOGANY_TREE_36688, 20190725),
//	MAHOGANY_TREE_IOS("Mahogany tree", ObjectID.MAHOGANY_TREE_40760, 20210203),
//
//	YEW_TREE("Yew tree", ObjectID.YEW_TREE, 20020325),
//	YEW_TREE_PRIF("Yew tree", ObjectID.YEW_TREE_36683, 20190725),
//	YEW_TREE_IOS("Yew tree", ObjectID.YEW_TREE_40756, 20210203),
//
//
//	ACHEY_TREE("Achey tree", ObjectID.ACHEY_TREE, 20040518),
//	ARCTIC_PINE_TREE("Arctic Pine tree", ObjectID.ARCTIC_PINE_TREE, 20070206),
//	HOLLOW_TREE_10821("Hollow tree", ObjectID.HOLLOW_TREE_10821, 20041018),
//	HOLLOW_TREE_10830("Hollow tree", ObjectID.HOLLOW_TREE_10830, 20041018),