package com.hkhbc.mc;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public final class WereWolfRoleCupid extends WereWolfRole {
	public WereWolfRoleCupid() {
		this.priority = Priority.FIRST;
		this.team = Team.HUMAN;
		this.instantAbility = true;
		this.abilityOnNPCs = false;
		this.actionPerRound = 2;
		this.abilityLimit = 1;
		this.noActionPenalty = 0;
		this.color = ChatColor.RED;
		this.name = "Cupid";
		this.roleMessage = "You are a &c&lCupid &fyou can force 2 players to be gay and fall in love with each other.";
		
		abilityItems.add(getUtils().getItem(Material.ARROW, "&c&lLover Plug"));
		abilityItems.add(getUtils().getItem(Material.ARROW, "&c&lLover Plug"));
	}
	
	@Override
	public boolean killed(WereWolfPlayer victim, WereWolfPlayer killer) {
		return true;
	}

	@Override
	public void ability(WereWolfPlayer target, WereWolfPlayer source) {
		WereWolfPlayer target1 = source.getAbilityTargets().get(0);
		WereWolfPlayer target2 = source.getAbilityTargets().get(1);
		target1.related(target2);
		target2.related(target1);
	}
}
