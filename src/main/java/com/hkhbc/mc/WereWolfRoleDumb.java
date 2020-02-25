package com.hkhbc.mc;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

import com.hkhbc.mc.WereWolfRole.Priority;

public final class WereWolfRoleDumb extends WereWolfRole {
	public WereWolfRoleDumb() {
		this.priority = Priority.FOURTH;
		this.team = Team.HUMAN;
		this.instantAbility = false;
		this.abilityOnNPCs = true;
		this.actionPerRound = 1;
		this.abilityLimit = -1;
		this.noActionPenalty = 0;
		this.color = ChatColor.DARK_PURPLE;
		this.name = "Dumb";
		this.roleMessage = "You are a &5&lForeseer &fYou can use your &b&lInspector &f to peek a player's role.";
		
		abilityItems.add(getUtils().getItem(Material.CLOCK, "&b&lInspector"));
	}
	
	@Override
	public boolean killed(WereWolfPlayer victim, WereWolfPlayer killer) {
		return true;
	}

	@Override
	public void ability(WereWolfPlayer target, WereWolfPlayer source) {
		List<WereWolfRole> roles = JavaPlugin.getPlugin(Main.class).getWereWolf().getRoles().getAllRoles();
		int rand = (int)Math.round(Math.random() * (roles.size() - 1));
		WereWolfRole role = roles.get(rand);
		source.getPlayer().sendMessage(getUtils().formatColor("&fYou found " + target.getPlayer().getName() + " &fis " + role.color + role.name));
	}
}
