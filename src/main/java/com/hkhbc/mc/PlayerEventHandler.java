package com.hkhbc.mc;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.java.JavaPlugin;

import com.hkhbc.mc.WereWolf.State;

public class PlayerEventHandler extends WereWolfManager implements Listener {
	
	public PlayerEventHandler () {
	}

	@EventHandler
    public final void onChestMove(InventoryClickEvent event) {
		if(plugin.getWereWolf().state == State.WAITING) return;
		
		for(WereWolfPlayer player : plugin.getWereWolf().getRound().getPlayers()) {
			if(event.getWhoClicked().getEntityId() == player.getPlayer().getEntityId()) {
		        Inventory top = event.getView().getTopInventory();
		        Inventory bottom = event.getView().getBottomInventory();
		        if (top.getType() != InventoryType.PLAYER && bottom.getType() == InventoryType.PLAYER) {
		            ItemStack item = event.getCurrentItem();
		            if (item != null) {
		                event.setResult(Result.DENY);
		                player.getPlayer().sendMessage(plugin.prefix + "you are not allowed to move any items from your inventory during gameplay.");
		            }
		        }
		        break;
			}
		}
    }
	
	@EventHandler
	public final void onPlayerDropItems(PlayerDropItemEvent event) {
		if(plugin.getWereWolf().state == State.WAITING) return;
		
		for(WereWolfPlayer player : plugin.getWereWolf().getRound().getPlayers()) {
			if(player.getPlayer().equals(event.getPlayer())) {
				event.setCancelled(true);
                player.getPlayer().sendMessage(plugin.prefix + "you are not allowed to drop any items from your inventory during gameplay.");
				break;
			}
		}
	}
	
	@EventHandler
	public final void onPlayerPickupItems(EntityPickupItemEvent event) {
		if(plugin.getWereWolf().state == State.WAITING) return;
		
		for(WereWolfPlayer player : plugin.getWereWolf().getRound().getPlayers()) {
			if(player.getPlayer().getEntityId() == event.getEntity().getEntityId()) {
				event.getItem().remove();
				event.setCancelled(true);
                player.getPlayer().sendMessage(plugin.prefix + "you are not allowed to pickup any items from your inventory during gameplay.");
				break;
			}
		}
	}
	
	@EventHandler
	public final void onPlayerInteract(PlayerInteractEvent event) {
		if(getWereWolf().state == State.WAITING) {
			return;
		}

		List<WereWolfPlayer> players = getRound().getPlayers();
		if(event.getAction().equals(Action.PHYSICAL)) {
			 if (event.getClickedBlock().getType().equals(Material.STONE_PRESSURE_PLATE)) {
				 for(WereWolfPlayer player : players) {
					 if(player.getPlayer().equals(event.getPlayer())) {
						 if(getRound().actionPlayers.contains(players.indexOf(player))){
							 return;
						 }
					 }
				 }
				 event.setCancelled(true);
	         }
		}
	}
	
	@EventHandler
	public final void onPlayerMove(PlayerMoveEvent event) {
		if(getWereWolf().state == State.WAITING) {
			return;
		}

		List<WereWolfPlayer> players = getRound().getPlayers();
		for(WereWolfPlayer player : players) {
			if(player.getPlayer().equals(event.getPlayer())) {
				if(getRound().actionPlayers.contains(players.indexOf(player)) && !getRound().actionedPlayers.contains(players.indexOf(player))){
					 return;
				 }
				
				if(player.getHouse().getSpawn().distance(event.getTo()) < 1) {
					event.getPlayer().setVelocity(player.getHouse().getSpawn().getDirection().multiply(2));
					//event.getPlayer().setVelocity(event.getFrom().getDirection().multiply(-1).setY(0));
					//event.setTo(event.getFrom());
					return;
				}
			}
		}
		/*
		if(event.getAction().equals(Action.PHYSICAL)) {
			 if (event.getClickedBlock().getType().equals(Material.STONE_PRESSURE_PLATE)) {
				 List<WereWolfPlayer> players = getRound().getPlayers();
				 for(WereWolfPlayer player : players) {
					 if(player.getPlayer().equals(event.getPlayer())) {
						 if(getRound().actionPlayers.contains(players.indexOf(player))){
							 return;
						 }
					 }
				 }
				 event.setCancelled(true);
	         }
		}*/
	}
	
	@EventHandler
	public final void onPlayerDamage(EntityDamageByEntityEvent event) {
		if(getWereWolf().state == State.WAITING) {
			return;
		}

		List<WereWolfPlayer> players = getRound().getPlayers();
		for(WereWolfPlayer player : players) {
			if(player.getPlayer().equals(event.getDamager())) {
				getRound().abilityPlayer((Player)event.getDamager(), (Player)event.getEntity());
				event.setCancelled(true);
			}
		}
	}
}
