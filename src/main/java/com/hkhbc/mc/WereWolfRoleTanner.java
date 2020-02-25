package com.hkhbc.mc;

import org.bukkit.ChatColor;

public final class WereWolfRoleTanner extends WereWolfRole {
	public WereWolfRoleTanner() {
		this.priority = Priority.EVERYONE;
		this.team = Team.TANNER;
		this.instantAbility = false;
		this.abilityOnNPCs = false;
		this.actionPerRound = 0;
		this.abilityLimit = 0;
		this.noActionPenalty = 0;
		this.color = ChatColor.YELLOW;
		this.name = "Tanner";
		this.roleMessage = "You are a &e&lTanner &fif you vote to death by the villagers, you win.";
	}
	
	@Override
	public boolean killed(WereWolfPlayer victim, WereWolfPlayer killer) {
		if(killer == null) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public void ability(WereWolfPlayer target, WereWolfPlayer source) {
		
	}
}
