package com.hkhbc.mc;

import org.bukkit.ChatColor;

public final class WereWolfRoleTheCursed extends WereWolfRole {
	public WereWolfRoleTheCursed() {
		this.priority = Priority.EVERYONE;
		this.team = Team.HUMAN;
		this.instantAbility = false;
		this.abilityOnNPCs = false;
		this.actionPerRound = 0;
		this.abilityLimit = 0;
		this.noActionPenalty = 0;
		this.color = ChatColor.BLUE;
		this.name = "The Cursed";
		this.roleMessage = "You are &1&lThe Cursed&f, you look just like a &2&lVillager&f, but you kill by a &c&lWolf&f, you will become &c&lWolf";
	}
	
	@Override
	public boolean killed(WereWolfPlayer victim, WereWolfPlayer killer) {
		return true;
	}

	@Override
	public void ability(WereWolfPlayer target, WereWolfPlayer source) {
	
	}
}
