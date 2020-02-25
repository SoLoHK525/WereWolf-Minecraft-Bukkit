package com.hkhbc.mc;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

public final class WereWolfRoleWitch extends WereWolfRole {
	public WereWolfRoleWitch() {
		this.priority = Priority.FOURTH;
		this.team = Team.WOLF;
		this.instantAbility = false;
		this.abilityOnNPCs = true;
		this.actionPerRound = 1;
		this.abilityLimit = -1;
		this.noActionPenalty = 0;
		this.color = ChatColor.DARK_PURPLE;
		this.name = "Witch";
		this.roleMessage = "You are a &5&lWitch &fYou can use your &5&lThe Prediction Doll &f to peek if a player is a &c&lWolf &for a &5&lForeseer.";
		
		abilityItems.add(getUtils().getItem(Material.TOTEM_OF_UNDYING, "&5&lThe Prediction Doll"));
	}
	
	@Override
	public boolean killed(WereWolfPlayer victim, WereWolfPlayer killer) {
		return true;
	}

	@Override
	public void ability(WereWolfPlayer target, WereWolfPlayer source) {
		if(target.getRole().getClass().equals(WereWolfRoleWolf.class) || target.getRole().getClass().equals(WereWolfRoleForeseer.class)) {
			source.getPlayer().sendMessage("&fYou found &b&l" + target.getPlayer().getName() + " &fis a" + target.getRole().color + target.getRole().name);
		} else {
			source.getPlayer().sendMessage("&fYou found &b&l" + target.getPlayer().getName() + " &fis not a &c&lWolf &fnor a &5&lForeseer");
		}
	}
}
