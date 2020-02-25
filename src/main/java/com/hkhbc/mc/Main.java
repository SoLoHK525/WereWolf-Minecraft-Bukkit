package com.hkhbc.mc;

import java.util.logging.Level;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	public final String prefix = ChatColor.GREEN + "[WereWolf] " + ChatColor.WHITE;
	public CommandManager commandManager;
	
	private PlayerEventHandler playerEventHandler;
	private WereWolf werewolf;
	
	@Override
    public void onEnable() {
		/**
		 * Instance Initialization
		 */
		commandManager = new CommandManager();
		werewolf = new WereWolf(this);
		
		playerEventHandler = new PlayerEventHandler();
		getServer().getPluginManager().registerEvents(playerEventHandler, this);
		net.citizensnpcs.api.CitizensAPI.getTraitFactory().registerTrait(net.citizensnpcs.api.trait.TraitInfo.create(WereWolfNPC.class).withName("WereWolfNPC"));
		
		getLogger().log(Level.INFO, "Werewolf loaded");
    }
	
    @Override
    public void onDisable() {
    	net.citizensnpcs.api.CitizensAPI.getTraitFactory().deregisterTrait(net.citizensnpcs.api.trait.TraitInfo.create(WereWolfNPC.class).withName("WereWolfNPC"));
    }
    
    public WereWolf getWereWolf() {
    	if(this.werewolf == null) {
    		getLogger().log(Level.INFO, "Why is this FUCKING NULL");
    	}
    	return this.werewolf;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    	if (command.getName().equalsIgnoreCase("ww") || command.getName().equalsIgnoreCase("werewolf")) {
    		if(args.length == 0) {
    			sender.sendMessage(prefix + "usage: /werewolf <command>");
    			return false;
    		}
        	commandManager.onCommand(sender, args);
            return true;
        }
        return false;  
    }
}
