package com.hkhbc.mc;

import org.bukkit.ChatColor;

public final class WereWolfRoleFakeWolf extends WereWolfRole {
	public WereWolfRoleFakeWolf() {
		this.priority = Priority.EVERYONE;
		this.team = Team.HUMAN;
		this.instantAbility = false;
		this.abilityOnNPCs = false;
		this.actionPerRound = 0;
		this.abilityLimit = 0;
		this.noActionPenalty = 0;
		this.color = ChatColor.GRAY;
		this.name = "Fake Wolf";
		this.roleMessage = "You are &7&lFake Wolf&f, you look just like a &2&lVillager&f, but you will fake the foreseer as a &c&lWolf";
	}
	
	@Override
	public boolean killed(WereWolfPlayer victim, WereWolfPlayer killer) {
		return true;
	}

	@Override
	public void ability(WereWolfPlayer target, WereWolfPlayer source) {
		
	}
}
