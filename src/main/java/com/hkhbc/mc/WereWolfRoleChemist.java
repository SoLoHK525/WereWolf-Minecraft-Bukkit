package com.hkhbc.mc;

import org.bukkit.ChatColor;
import org.bukkit.Material;

public final class WereWolfRoleChemist extends WereWolfRole {
	public WereWolfRoleChemist() {
		this.priority = Priority.THIRD;
		this.team = Team.HUMAN;
		this.instantAbility = false;
		this.abilityOnNPCs = true;
		this.actionPerRound = 1;
		this.abilityLimit = 1;
		this.noActionPenalty = 0;
		this.color = ChatColor.DARK_AQUA;
		this.name = "Chemist";
		this.deathMessage = "%s has been poisoned to death, and his role is: %s";
		this.roleMessage = "You are a &3&lChemist &fYou can use your &3&lUnknown Chemical &fto have a 50% of drugging a player or 50% drugging yourself";
		
		abilityItems.add(getUtils().getItem(Material.GLASS_BOTTLE, "&3&lUnknown Chemical"));
	}
	
	@Override
	public boolean killed(WereWolfPlayer victim, WereWolfPlayer killer) {
		return true;
	}

	@Override
	public void ability(WereWolfPlayer target, WereWolfPlayer source) {
		if(target.getAbilityTargets().size() == 0) {
			if(Math.random() > 0.5) {
				target.killed(source);
			} else {
				source.setLastAbilitied(source);
				source.kill(source);
			}
		} else {
			source.getPlayer().sendMessage(getUtils().formatColor("&b&l" + target.getPlayer().getName() + " &fwas not at home yesterday, so you didn't visit him."));
		}
	}
}
