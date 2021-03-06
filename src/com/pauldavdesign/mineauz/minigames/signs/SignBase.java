package com.pauldavdesign.mineauz.minigames.signs;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import com.pauldavdesign.mineauz.minigames.Minigames;

public class SignBase implements Listener{
	
	private static Map<String, MinigameSign> minigameSigns = new HashMap<String, MinigameSign>();
	
	static{
		registerMinigameSign(new FinishSign());
		registerMinigameSign(new JoinSign());
		registerMinigameSign(new BetSign());
		registerMinigameSign(new CheckpointSign());
		registerMinigameSign(new FlagSign());
		registerMinigameSign(new QuitSign());
		registerMinigameSign(new LoadoutSign());
		registerMinigameSign(new TeleportSign());
		registerMinigameSign(new SpectateSign());
		registerMinigameSign(new RewardSign());
		registerMinigameSign(new TeamSign());
	}
	
	public SignBase(){
		Minigames.plugin.getServer().getPluginManager().registerEvents(this, Minigames.plugin);
	}
	
	public static void registerMinigameSign(MinigameSign mgSign){
		minigameSigns.put(mgSign.getName().toLowerCase(), mgSign);
	}
	
	@EventHandler
	private void signPlace(SignChangeEvent event){
		String[] signinfo = event.getLines();
		if(signinfo[0].equalsIgnoreCase("[minigame]") || signinfo[0].equalsIgnoreCase("[mgm]") || signinfo[0].equalsIgnoreCase("[mg]")){
			if(minigameSigns.containsKey(signinfo[1].toLowerCase())){
				event.setLine(0, ChatColor.DARK_BLUE + "[Minigame]");
				MinigameSign mgSign = minigameSigns.get(signinfo[1].toLowerCase());
				
				if(mgSign.getCreatePermission() != null && !event.getPlayer().hasPermission(mgSign.getCreatePermission())){
					event.setCancelled(true);
					event.getBlock().breakNaturally();
					event.getPlayer().sendMessage(ChatColor.RED + "[Minigames] " + ChatColor.WHITE + mgSign.getCreatePermissionMessage());
					return;
				}
				
				if(!mgSign.signCreate(event)){
					event.setCancelled(true);
					event.getBlock().breakNaturally();
					event.getPlayer().sendMessage(ChatColor.RED + "[Minigames] " + ChatColor.WHITE + "Invalid Minigames sign!");
				}
			}
			else{
				event.setCancelled(true);
				event.getBlock().breakNaturally();
			}
		}
	}
	
	@EventHandler
	private void signUse(PlayerInteractEvent event){
		if(event.getAction() == Action.RIGHT_CLICK_BLOCK){
			Block cblock = event.getClickedBlock();
			if(cblock.getState() instanceof Sign){
				Sign sign = (Sign) cblock.getState();
				if(sign.getLine(0).equalsIgnoreCase(ChatColor.DARK_BLUE + "[Minigame]") && 
						minigameSigns.containsKey(ChatColor.stripColor(sign.getLine(1).toLowerCase()))){
					MinigameSign mgSign = minigameSigns.get(ChatColor.stripColor(sign.getLine(1).toLowerCase()));
					
					if(mgSign.getUsePermission() != null && !event.getPlayer().hasPermission(mgSign.getUsePermission())){
						event.setCancelled(true);
						event.getPlayer().sendMessage(ChatColor.RED + "[Minigames] " + ChatColor.WHITE + mgSign.getUsePermissionMessage());
						return;
					}
					
					if(mgSign.signUse(sign, Minigames.plugin.pdata.getMinigamePlayer(event.getPlayer()))){
						event.setCancelled(true);
					}
				}
			}
		}
	}

}
