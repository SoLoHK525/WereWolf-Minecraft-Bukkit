package com.hkhbc.mc;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


import com.hkhbc.mc.WereWolf.State;

public class CommandManager extends WereWolfManager {
	public void onCommand(CommandSender sender, String[] args) {
		String command = args[0];
		if(command.equalsIgnoreCase("join")) {
			if(getWereWolf().state != State.WAITING) {
				getUtils().sendMessage((Player)sender, "&cThe werewolf game is currently unavailable.");
				return;
			}
			
			if(getQueue().join((Player)sender)) {
				getUtils().sendMessage((Player)sender, "You have joined the &bWereWolf &fgame.");
			} else {
				getUtils().sendMessage((Player)sender, "&cYou have already joined the &bWereWolf &fgame.");
			}
		} else if(command.equalsIgnoreCase("joinall")) {
			if(getWereWolf().state != State.WAITING) {
				getUtils().sendMessage((Player)sender, "&cThe werewolf game is currently unavailable.");
				return;
			}
			
			for(Player player : plugin.getServer().getOnlinePlayers()) {
				if(getQueue().join(player)) {
					getUtils().sendMessage(player, "You have joined the &bWereWolf &fgame.");
				}
			}
		} else if(command.contentEquals("house")){
			houseHandler(sender, args);
		} else if(command.contentEquals("location")){
			locationHandler(sender, args);
		} else if(command.equalsIgnoreCase("end")){
			getWereWolf().end();
		} else {
			invalidCommand(sender);
		}
	}
	
	public void houseHandler(CommandSender sender, String[] args) {
		if(args.length < 2) {
			getUtils().sendMessage((Player)sender, "&c&lUsage: /ww house <create/npcs/list>");
			return;
		}

		if(args[1].equalsIgnoreCase("create")) {
			Player player = (Player)sender;
			WereWolfHouses houses = plugin.getWereWolf().getHouses();
			if(houses.add(new WereWolfHouse(player.getLocation()))) {
				sender.sendMessage(plugin.prefix + "created house [" + (houses.size() - 1) + "]!");
			}
		} else if(args[1].equalsIgnoreCase("list")) {
			int index = 0;
			sender.sendMessage(ChatColor.AQUA + "======== " + ChatColor.WHITE + "WereWolf House List" + ChatColor.AQUA + " ========" );
			if(plugin.getWereWolf().getHouses().size() == 0) {
				sender.sendMessage("No Record");
			}
			for(WereWolfHouse house : plugin.getWereWolf().getHouses().houses()) {
				sender.sendMessage("  House " + ChatColor.GREEN + index++ + ChatColor.WHITE + ": X: " + String.format("%.1f", house.spawn.getX()) + " Y: " + String.format("%.1f", house.spawn.getY()) + " Z: " + String.format("%.1f", house.spawn.getZ()));
			}
			sender.sendMessage(ChatColor.AQUA + "======== " + ChatColor.WHITE + "WereWolf House List" + ChatColor.AQUA + " ========" );
		} else if(args[1].equalsIgnoreCase("reload")){
			plugin.getWereWolf().getHouses().loadConfig();
			sender.sendMessage(plugin.prefix + "reloaded houses!");
		} else if(args[1].equalsIgnoreCase("npcs")) {
			if(args.length < 4) {
				sender.sendMessage(plugin.prefix + "Usage: /werewolf house npcs add houseNo.");
			} else {
				WereWolfHouse house = plugin.getWereWolf().getHouses().houses().get(Integer.parseInt(args[3]));
				Player player = (Player)sender;
				house.npcs.add(player.getLocation());
				plugin.getWereWolf().getHouses().edit(Integer.parseInt(args[3]), house);
			}
		} else {
			invalidCommand(sender);
		}
	}
	
	//args[1]
	public void locationHandler(CommandSender sender, String[] args) {
		if(args.length < 2) {
			getUtils().sendMessage((Player)sender, "&c&lUsage: /ww location <vote/death>");
			return;
		}
		if(args[1].equalsIgnoreCase("vote")) {
			if(args.length < 3) {
				getUtils().sendMessage((Player)sender, "&c&lUsage: /ww location death add");
				return;
			}
			if(args[2].equalsIgnoreCase("add")) {
				Player player = (Player)sender;
				if(getLocation().addVoteNPCSpawn(player.getLocation())) {
					getUtils().sendMessage((Player)sender, "&aAdded new vote NPC position.");
				} else {
					error(sender);
				}
			} else {
				invalidCommand(sender);
			}
		} else if(args[1].equalsIgnoreCase("death")) {
			if(args.length < 3) {
				getUtils().sendMessage((Player)sender, "&c&lUsage: /ww location death set");
				return;
			}
			if(args[2].equalsIgnoreCase("set")) {
				Player player = (Player)sender;
				getLocation().setDeath(player.getLocation());
				getUtils().sendMessage((Player)sender, "&aSetted new death position.");
			} else {
				invalidCommand(sender);
			}
		} else if(args[1].equalsIgnoreCase("spawn")) {
			if(args.length < 3) {
				getUtils().sendMessage((Player)sender, "&c&lUsage: /ww location spawn set");
				return;
			}
			if(args[2].equalsIgnoreCase("set")) {
				Player player = (Player)sender;
				getLocation().setSpawnLocation(player.getLocation());
				getUtils().sendMessage((Player)sender, "&aSetted new spawn position.");
			} else {
				invalidCommand(sender);
			}
		} else {
			invalidCommand(sender);
		}
	}
	
	private void invalidCommand(CommandSender sender) {
		getUtils().sendMessage((Player)sender, "&c&lInvalid Command");
	}
	
	private void error(CommandSender sender) {
		getUtils().sendMessage((Player)sender, "&c&lError occurred");
	}
}
