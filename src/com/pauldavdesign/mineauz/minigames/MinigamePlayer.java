package com.pauldavdesign.mineauz.minigames;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.Scoreboard;

public class MinigamePlayer {
	private Player player;
	private ItemStack[] storedItems = null;
	private ItemStack[] storedArmour = null;
	private int food = 20;
	private double health = 20;
	private float saturation = 15;
	private boolean allowTP = true;
	private boolean allowGMChange = true;
	private GameMode lastGM = GameMode.SURVIVAL;
	private Scoreboard lastScoreboard = null;
	
	private Minigame minigame = null;
	private List<String> flags = new ArrayList<String>();
	private Location checkpoint = null;
	private int kills = 0;
	private int deaths = 0;
	private int score = 0;
	
	private PlayerData pdata = Minigames.plugin.pdata;
	
	public MinigamePlayer(Player player){
		this.player = player;
	}
	
	public Player getPlayer(){
		return player;
	}
	
	public String getName(){
		return player.getName();
	}
	
	public void sendMessage(String msg){
		player.sendMessage(msg);
	}
	
	public ItemStack[] getStoredItems(){
		return storedItems;
	}
	
	public ItemStack[] getStoredArmour(){
		return storedArmour;
	}
	
	public int getFood(){
		return food;
	}
	
	public double getHealth(){
		return health;
	}
	
	public float getSaturation(){
		return saturation;
	}
	
	public GameMode getLastGamemode(){
		return lastGM;
	}
	
	@SuppressWarnings("deprecation")
	public void storePlayerData(){
		storedItems = player.getInventory().getContents();
		storedArmour = player.getInventory().getArmorContents();
		food = player.getFoodLevel();
		health = player.getHealth();
		saturation = player.getSaturation();
		lastScoreboard = player.getScoreboard();
		lastGM = player.getGameMode();
		
		player.setSaturation(15);
		player.setFoodLevel(20);
		player.setHealth(player.getMaxHealth());
		player.getInventory().clear();
		player.getInventory().setArmorContents(null);
		
		savePlayerData();
		player.updateInventory();
	}
	
	public void savePlayerData(){
		if(storedItems != null){
			int num = 0;
			for(ItemStack item : storedItems){
				if(item != null){
					pdata.invsave.getConfig().set("inventories." + player.getName() + "." + num, item);
				}
				num++;
			}
		}
		else{
			pdata.invsave.getConfig().set("inventories." + player.getName(), null);
			return;
		}
		
		if(storedArmour != null){
			int num = 0;
			for(ItemStack item : storedArmour){
				if(item != null){
					pdata.invsave.getConfig().set("inventories." + player.getName() + ".armour." + num, item);
				}
				num++;
			}
		}
		
		pdata.invsave.getConfig().set("inventories." + player.getName() + ".food", food);
		pdata.invsave.getConfig().set("inventories." + player.getName() + ".saturation", saturation);
		pdata.invsave.getConfig().set("inventories." + player.getName() + ".health", health);
		pdata.invsave.getConfig().set("inventories." + player.getName() + ".lastGM", lastGM.getValue());
		
		pdata.invsave.saveConfig();
	}
	
	public void setPlayerData(ItemStack[] items, ItemStack[] armour, int food, double health, float saturation, GameMode lastGM){
		storedItems = items;
		storedArmour = armour;
		this.food = food;
		this.health = health;
		this.saturation = saturation;
		this.lastGM = lastGM;
	}
	
	@SuppressWarnings("deprecation")
	public void restorePlayerData(){
		player.getInventory().clear();
		player.getInventory().setArmorContents(null);
		
		player.getInventory().setContents(storedItems);
		player.getInventory().setArmorContents(storedArmour);
		player.setFoodLevel(food);
		player.setHealth(health);
		player.setSaturation(saturation);
		if(lastScoreboard != null){
			player.setScoreboard(lastScoreboard);
		}
		else{
			player.setScoreboard(player.getServer().getScoreboardManager().getMainScoreboard());
		}
		
		allowGMChange = true;
		allowTP = true;
		player.setGameMode(lastGM);
		
		storedItems = null;
		storedArmour = null;
		
		pdata.invsave.getConfig().set("inventories." + player.getName(), null);
		pdata.invsave.saveConfig();
		
		player.updateInventory();
	}
	
	public boolean getAllowTeleport(){
		return allowTP;
	}
	
	public void setAllowTeleport(boolean allowTP){
		this.allowTP = allowTP;
	}
	
	public boolean getAllowGamemodeChange(){
		return allowGMChange;
	}
	
	public void setAllowGamemodeChange(boolean allowGMChange){
		this.allowGMChange = allowGMChange;
	}
	
	public Minigame getMinigame(){
		return minigame;
	}
	
	public void setMinigame(Minigame minigame){
		this.minigame = minigame;
	}
	
	public void removeMinigame(){
		minigame = null;
	}
	
	public boolean isInMinigame(){
		if(minigame != null)
			return true;
		return false;
	}
	
	public List<String> getFlags(){
		return flags;
	}
	
	public boolean addFlag(String flag){
		if(!flags.contains(flag)){
			flags.add(flag);
			return true;
		}
		return false;
	}
	
	public boolean hasFlag(String flagName){
		if(flags.contains("flag")){
			return true;
		}
		return false;
	}
	
	public void setFlags(List<String> flags){
		this.flags = flags;
	}
	
	public void clearFlags(){
		flags.clear();
	}
	
	public Location getCheckpoint(){
		return checkpoint;
	}
	
	public void setCheckpoint(Location checkpoint){
		this.checkpoint = checkpoint;
	}
	
	public void removeCheckpoint(){
		checkpoint = null;
	}
	
	public int getKills(){
		return kills;
	}
	
	public void addKill(){
		kills++;
	}
	
	public void resetKills(){
		kills = 0;
	}
	
	public int getDeaths(){
		return deaths;
	}
	
	public void addDeath(){
		deaths++;
	}
	
	public void resetDeaths(){
		deaths = 0;
	}
	
	public int getScore(){
		return score;
	}
	
	public void addScore(){
		score++;
	}
	
	public void resetScore(){
		score = 0;
	}
	
	public void takeScore(){
		score--;
	}
}
