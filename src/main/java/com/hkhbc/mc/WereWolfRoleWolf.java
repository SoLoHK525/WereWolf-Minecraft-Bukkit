package com.hkhbc.mc;

import java.util.logging.Level;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public final class WereWolfRoleWolf extends WereWolfRole {
	public WereWolfRoleWolf() {
		this.priority = Priority.SECOND;
		this.team = Team.WOLF;
		this.instantAbility = false;
		this.abilityOnNPCs = false;
		this.actionPerRound = 1;
		this.abilityLimit = 0;
		this.noActionPenalty = 0;
		this.color = ChatColor.RED;
		this.name = "Wolf";
		this.roleMessage = "You are a &c&lWolf &fafter the first night, each night you can kill a person.";
		this.deathMessage = "%s were found dead, it looks like his body ate by an animal, and his role is %s";
		
		abilityItems.add(getUtils().getItem(Material.DIAMOND_SWORD, "&c&lBlood Sword"));
	}
	
	@Override
	public boolean killed(WereWolfPlayer victim, WereWolfPlayer killer) {
		return true;
	}

	@Override
	public void ability(WereWolfPlayer target, WereWolfPlayer source) {
		target.killed(source);
		
		if(target.lastAbilitiedBy != null) {
			if(target.lastAbilitiedBy.getRole().getClass().equals(WereWolfRoleMoreMore.class)) {
				target.lastAbilitiedBy.killed(source);
			}
		}
	}
}
