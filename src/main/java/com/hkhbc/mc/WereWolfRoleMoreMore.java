package com.hkhbc.mc;

import org.bukkit.ChatColor;
import org.bukkit.Material;

public final class WereWolfRoleMoreMore extends WereWolfRole {
	public WereWolfRoleMoreMore() {
		this.priority = Priority.FIRST;
		this.team = Team.HUMAN;
		this.instantAbility = false;
		this.abilityOnNPCs = false;
		this.actionPerRound = 1;
		this.abilityLimit = -1;
		this.noActionPenalty = 0;
		this.color = ChatColor.LIGHT_PURPLE;
		this.name = "MoreMore";
		this.roleMessage = "You are a &d&lMoreMore &fYou can use your &d&lDildo &f to gangbang a player.";
		
		abilityItems.add(getUtils().getItem(Material.END_ROD, "&d&lDildo"));
	}
	
	@Override
	public boolean killed(WereWolfPlayer victim, WereWolfPlayer killer) {
		return true;
	}

	@Override
	public void ability(WereWolfPlayer target, WereWolfPlayer source) {
		if(target.getRole().getClass().equals(WereWolfRoleWolf.class)) {
			source.getPlayer().sendMessage(getUtils().formatColor("&fOOF! you had sex with &b&l" + target.getPlayer().getName() + " &fand he is a " + target.getRole().color + target.getRole().name));
			target.getRole().ability(source, target);
		} else {
			source.getPlayer().sendMessage(getUtils().formatColor("&fYou had sex with &b&l" + target.getPlayer().getName() + " &fwow"));
			target.getPlayer().sendMessage(getUtils().formatColor("&fYou got gangbanged by a &d&lMoreMore, last night you two had a lot of FUN :D"));
		}
	}
}
