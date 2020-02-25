package com.hkhbc.mc;

import org.bukkit.ChatColor;

public final class WereWolfRoleVillager extends WereWolfRole {
	public WereWolfRoleVillager() {
		this.priority = Priority.EVERYONE;
		this.team = Team.HUMAN;
		this.instantAbility = false;
		this.abilityOnNPCs = false;
		this.actionPerRound = 0;
		this.abilityLimit = 0;
		this.noActionPenalty = 0;
		this.color = ChatColor.DARK_GREEN;
		this.name = "Villager";
		this.roleMessage = "You are just a useless villager";
	}
	
	@Override
	public boolean killed(WereWolfPlayer victim, WereWolfPlayer killer) {
		return true;
	}

	@Override
	public void ability(WereWolfPlayer target, WereWolfPlayer source) {
		
	}
}
