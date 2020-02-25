package com.hkhbc.mc;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class WereWolfUtils extends WereWolfManager {	
	public void broadcast(List<Player> players, String message) {
		message = formatColor(message);
		for(Player player : players) {
			player.sendMessage(plugin.prefix + message);
		}
	}
	
	public void broadcastW(List<WereWolfPlayer> players, String message) {
		message = formatColor(message);
		for(WereWolfPlayer player : players) {
			player.getPlayer().sendMessage(plugin.prefix + message);
		}
	}

	public void broadcastWP(List<WereWolfPlayer> players, String message) {
		message = formatColor(message);
		for(WereWolfPlayer player : players) {
			player.getPlayer().sendMessage(message);
		}
	}
	
	public String formatColor(String str) {
		return ChatColor.translateAlternateColorCodes('&', str);
	}
	
	public void sendMessage(Player player, String message) {
		player.sendMessage(plugin.prefix + ChatColor.translateAlternateColorCodes('&', message));
	}
	
	public void sendTitle(WereWolfPlayer player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
		subtitle = subtitle == null ? "" : formatColor(subtitle);
		title = formatColor(title);
		player.getPlayer().sendTitle(title, subtitle, fadeIn, stay, fadeOut);
	}

	public void broadcastTitle(List<Player> players, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
		subtitle = subtitle == null ? "" : formatColor(subtitle);
		title = formatColor(title);
		for(Player player : players) {
			player.sendTitle(title, subtitle, fadeIn, stay, fadeOut);
		}
	}

	public void broadcastSound(List<Player> players, Sound sound, float volume, float pitch) {
		for(Player player : players) {
			player.playSound(player.getLocation(), sound, volume, pitch);
		}
	}
	
	public ItemStack getItem(Material material, String name) {
		ItemStack item = new ItemStack(material);
		ItemMeta itemMeta = item.getItemMeta();
		name = formatColor(name);
		itemMeta.setDisplayName(name);
		item.setItemMeta(itemMeta);
		return item;
	}
}
