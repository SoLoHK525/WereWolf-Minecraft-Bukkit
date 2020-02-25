package com.hkhbc.mc;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.plugin.java.JavaPlugin;

import com.hkhbc.mc.WereWolfRound.Stage;

import net.citizensnpcs.api.persistence.Persist;
import net.citizensnpcs.api.trait.Trait;
import net.citizensnpcs.api.util.DataKey;

public class WereWolfNPC extends Trait {
	public WereWolfNPC() {
		super("WereWolfNPC");
		plugin = JavaPlugin.getPlugin(Main.class);
	}

	Main plugin = null;
        
    // see the 'Persistence API' section
    @Persist("mysettingname") boolean automaticallyPersistedSetting = false;

        // An example event handler. All traits will be registered automatically as Bukkit Listeners.
	@EventHandler
	public void click(net.citizensnpcs.api.event.NPCRightClickEvent event){
		WereWolfRound round = plugin.getWereWolf().getRound();
		if(event.getNPC() == this.getNPC()) {
			if(event.getClicker().getInventory().getItemInMainHand().getType() != Material.AIR) {
				if(round.getStage() == Stage.VOTE) {
					if(event.getClicker().getInventory().getItemInMainHand().getType() == round.votingPaper.getType()) {
						round.voted(event.getClicker(), this.getNPC());
					}
				} else {
					round.abilityNPC(event.getClicker(), event.getNPC());
				}
			}
		}
	}
	
	@EventHandler
	public void click(net.citizensnpcs.api.event.NPCLeftClickEvent event) {
		WereWolfRound round = plugin.getWereWolf().getRound();
		if(event.getNPC() == this.getNPC()) {
			if(event.getClicker().getInventory().getItemInMainHand().getType() != Material.AIR) {
				if(round.getStage() == Stage.VOTE) {
					if(event.getClicker().getInventory().getItemInMainHand().getType() == round.votingPaper.getType()) {
						round.voted(event.getClicker(), this.getNPC());
					}
				} else {
					round.abilityNPC(event.getClicker(), event.getNPC());
				}
			}
		}
	}
      
    // Called every tick
    @Override
    public void run() {
    }

	//Run code when your trait is attached to a NPC. 
        //This is called BEFORE onSpawn, so npc.getBukkitEntity() will return null
        //This would be a good place to load configurable defaults for new NPCs.
	@Override
	public void onAttach() {
		//plugin.getServer().getLogger().info(npc.getName() + "has been assigned MyTrait!");
	}

        // Run code when the NPC is despawned. This is called before the entity actually despawns so npc.getBukkitEntity() is still valid.
	@Override
	public void onDespawn() {
    }

	//Run code when the NPC is spawned. Note that npc.getBukkitEntity() will be null until this method is called.
        //This is called AFTER onAttach and AFTER Load when the server is started.
	@Override
	public void onSpawn() {

	}

        //run code when the NPC is removed. Use this to tear down any repeating tasks.
	@Override
	public void onRemove() {
    }
}