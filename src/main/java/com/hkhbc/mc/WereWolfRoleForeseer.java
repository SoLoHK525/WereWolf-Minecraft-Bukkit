package com.hkhbc.mc;

import org.bukkit.ChatColor;
import org.bukkit.Material;

public final class WereWolfRoleForeseer extends WereWolfRole {
	public WereWolfRoleForeseer() {
		this.priority = Priority.FOURTH;
		this.team = Team.HUMAN;
		this.instantAbility = false;
		this.abilityOnNPCs = true;
		this.actionPerRound = 1;
		this.abilityLimit = -1;
		this.noActionPenalty = 0;
		this.color = ChatColor.DARK_PURPLE;
		this.name = "Foreseer";
		this.roleMessage = "You are a &5&lForeseer &fYou can use your &b&lInspector &f to peek a player's role.";
		
		abilityItems.add(getUtils().getItem(Material.CLOCK, "&b&lInspector"));
	}
	
	@Override
	public boolean killed(WereWolfPlayer victim, WereWolfPlayer killer) {
		return true;
	}

	@Override
	public void ability(WereWolfPlayer target, WereWolfPlayer source) {
		if(target.getRole().getClass().equals(WereWolfRoleFakeWolf.class)) {
			WereWolfRole wolf = new WereWolfRoleWolf();
			source.getPlayer().sendMessage(getUtils().formatColor("&fYou found &b&l" + target.getPlayer().getName() + " &fis a " + wolf.color + wolf.name));
		} else {
			source.getPlayer().sendMessage(getUtils().formatColor("&fYou found &b&l" + target.getPlayer().getName() + " &fis a " + target.getRole().color + target.getRole().name));
		}
	}
}
