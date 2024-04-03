package me.CAPS123987.IIIDmultiblock;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.attributes.EnergyNetProvider;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockBreakHandler;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockPlaceHandler;
import io.github.thebusybiscuit.slimefun4.core.networks.energy.EnergyNetComponentType;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.implementation.items.SimpleSlimefunItem;
import io.github.thebusybiscuit.slimefun4.implementation.items.misc.CoolantCell;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import me.CAPS123987.BetterNuclearReactor.BetterNuclearReactor;
import me.CAPS123987.Item.Items;
import me.CAPS123987.Utils.ETInventoryBlock;
import me.CAPS123987.Utils.Methodes;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ClickAction;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockTicker;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import org.bukkit.ChatColor;
import org.bukkit.*;
import org.bukkit.Particle.DustOptions;
import org.bukkit.block.Block;
import org.bukkit.block.data.Directional;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.*;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.Vector;

import java.util.*;

public class ReactorCore extends SimpleSlimefunItem<BlockTicker> implements EnergyNetProvider, ETInventoryBlock{
	
	FileConfiguration cfg = BetterNuclearReactor.instance.getConfig();
	public int particles = cfg.getInt("Reactor_Core_Hologram_Particles");
	public boolean biggerExplosion = cfg.getBoolean("biggerExplosion");
	public final static int[] inputs = {19,28,37,25,34,43};
	public final static int[] inputs_coolant = {19,28,37};
	public final static int[] inputs_uran = {25,34,43};
	public final static int[] outputs = {22,40};
	public final static int outputuran = 40;
	public final static int outputcoolant = 22;
	public final static int[] border = {0,1,2,3,4,5,6,7,8,31};
	public final static int[] coolantBorder = {9,11,18,20,27,29,30,36,38,45,46,47};
	public final static int[] uranBorder = {15,17,24,26,32,33,35,42,44,51,52,53};
	public final static int[] uranoutputborder = {39,41,48,49,50};
	public final static int[] coolantoutputborder = {12,13,14,21,23};
	private int uniqueTick = 0;
	public final static int maxcoolant = 128;
	public final static int maxuran = 64;
	private final static Vector[] vectors= {new Vector(0,0,1),new Vector(1,0,2),new Vector(0,0,3),new Vector(-1,0,2)};
	private final static int coolant_status = 30;
	private final static int uran_status = 32;
	private final static int full_status = 31;
	public final static int burnTime = 1200;
	public final static int coolantTime = 8;
	public final static int powerPer = 1024;
	public final static long maxTemp = 7000;
	public final int maxUraniumPer = cfg.getInt("uranMax");
	public final int maxCoolantPer = maxUraniumPer;
	public final static int total = powerPer*burnTime;
	public final static int baseExplosionRadiusPer = 32;
	public final static int baseFalloutRadiusPer = 10;
	public final static int falloutTickTimePer = 9000;
	public final boolean announceExplosion = cfg.getBoolean("announceReactorExplosion");
	public final boolean announceReactorOwner = cfg.getBoolean("announceReactorOwner");
	public final boolean explosionFallout = cfg.getBoolean("explosionFallout");
	public final boolean largeExplosionFallout = cfg.getBoolean("largeExplosionFallout");
	public final static int hologramTime = 200;


	
	private final Map<Vector, SlimefunItemStack> blocks;
	public HashMap<Location,Integer> ticks = new HashMap<Location,Integer>();
	public HashMap<Location,Integer> uran500 = new HashMap<Location,Integer>();
	public HashMap<Location,Long> temp = new HashMap<Location,Long>();
	
	
	public ReactorCore(final Map<Vector, SlimefunItemStack> blocks) {
		super(Items.betterReactor,Items.REACTOR_CORE, RecipeType.ENHANCED_CRAFTING_TABLE, Items.recipe_REACTOR_CORE);
		this.blocks = blocks;
		createPreset(this, this::constructMenu);
		addItemHandler(BlockPlaceHandler(), onBreak());
	}
	
	@Override
	public BlockTicker getItemHandler() {
		// TODO Auto-generated method stub
		return new BlockTicker() {

			@Override
			public void uniqueTick() {
                if (uniqueTick == 6) {
                    uniqueTick = 0;
                    return;
                }
                uniqueTick++;

            }
			@Override
			public boolean isSynchronized() {
				// TODO Auto-generated method stub
				return true;
			}
			
			@SuppressWarnings("deprecation")
			@Override
			public void tick(Block b, SlimefunItem item, Config data) {
				Location l = b.getLocation();
				
				BlockMenu menu = BlockStorage.getInventory(b);
				if(uniqueTick==5) {
					uniqueTickk(b, menu);
				}
				final String isBuild = BlockStorage.getLocationInfo(l,"build");
				final int coolant = Integer.parseInt(BlockStorage.getLocationInfo(l, "coolant").replaceAll("[^0-9]", ""));
				final int uran = Integer.parseInt(BlockStorage.getLocationInfo(l, "uran").replaceAll("[^0-9]", ""));
				
				if(isBuild.equals("false")) {
					if(isRunning(b)) {
						for(int x = -1;x<5;x=x+4) {
							for(int z = -1;z<5;z=z+4) {
								for(int y = -1;y<8;y=y+2) {
									Location Loc = b.getLocation().clone().add(x, y, z);
									
									
									AreaEffectCloud Area = (AreaEffectCloud) Loc.getWorld().spawnEntity(Loc, EntityType.AREA_EFFECT_CLOUD);
									
									Area.addCustomEffect(new PotionEffect(PotionEffectType.HARM,5,2), true);
									Area.setDuration(36000);
									Area.setParticle(Particle.CRIT);
								}
							}
						}
						Bukkit.getPlayer(BlockStorage.getLocationInfo(l, "owner")).sendMessage(ChatColor.DARK_RED+"[REACTOR]"+ChatColor.RED+" Reactor at"+
								ChatColor.GOLD+" x: "+b.getLocation().getBlockX()+" y: "+b.getLocation().getBlockY()+" z: "+b.getLocation().getBlockZ()+ChatColor.RED+
								" LEAKED");
						ticks.replace(l, 0);
					}
					return;
				}
				
				saveCoolant(b,menu);
				saveUran(b,menu);
				coolant_status(b,menu,coolant);
				uran_status(b,menu,uran);
				int coolant_out;
				if(menu.getItemInSlot(outputcoolant)==null) {
					coolant_out = 0;
				}else {
					coolant_out = menu.getItemInSlot(outputcoolant).getAmount();
				}
				
				int uran_out;
				if(menu.getItemInSlot(outputuran)==null) {
					uran_out = 0;
				}else {
					uran_out = menu.getItemInSlot(outputuran).getAmount();
				}
				int tick;
				if(!ticks.containsKey(l)) {
					ticks.put(l, 0);
				}
				
				tick = ticks.get(l);
				updateStatus(tick, menu, coolant_out, uran_out,Bukkit.getPlayer(BlockStorage.getLocationInfo(l, "owner")),b,Integer.parseInt(BlockStorage.getLocationInfo(l, "coolantPer")),Integer.parseInt(BlockStorage.getLocationInfo(l, "uranPer")),isRunning(b));
				
				runReaction(b, menu, coolant_out,uran_out);
			}
			
		};
	}
	
	public void runReaction(Block b,BlockMenu menu, int coolant_out, int uran_out) {
		int coolant = Integer.parseInt(BlockStorage.getLocationInfo(b.getLocation(), "coolant"));
		int uran = Integer.parseInt(BlockStorage.getLocationInfo(b.getLocation(), "uran"));
		int coolantPer = Integer.parseInt(BlockStorage.getLocationInfo(b.getLocation(), "coolantPer"));
		int uranPer = Integer.parseInt(BlockStorage.getLocationInfo(b.getLocation(), "uranPer"));
		
		
		long el = uranPer*powerPer;
		
		if(isRunning(b)) {
			if(b.getChunk().isLoaded()) {
				int tick = ticks.get(b.getLocation());
				
				if(tick==1) {
					menu.pushItem(new CustomItemStack(SlimefunItems.PLUTONIUM,Math.round(uranPer/2)), outputuran);
				}
				long temperature = Math.round((
						(
								Double.valueOf(
										uran500.get(b.getLocation())
								)
						)/Double.valueOf(coolantPer)
					)
				*5500.0);

				long tempe = temp.get(b.getLocation());
				
				
				if(coolant_out==64||uran_out==64||tempe>maxTemp) {
					expolode(b,uranPer);
					ticks.remove(b.getLocation());
					
				}else {
					if(tempe<temperature) {
						temp.replace(b.getLocation(), tempe+((temperature-tempe)/20));
					}
					if(tempe>temperature) {
						temp.replace(b.getLocation(), tempe-((tempe-temperature)/10));
					}
					
					ticks.replace(b.getLocation(), tick-1);
					addCharge(b.getLocation(),(int)el);
					if(!hasCoolant(b)) {
						temp.replace(b.getLocation(), tempe+200);
					}else {
						if(tick%coolantTime==0)
							BlockStorage.addBlockInfo(b,"coolant", String.valueOf(coolant-coolantPer));
					}
					if(tick%coolantTime==0)
						menu.pushItem(new CustomItemStack(Items.HEATED_COOLANT,(int)Math.round(coolantPer/2)), outputcoolant);
					
				}
			}
			return;
		}
		if(!hasFuel(b)) {
			return;
		}
		long temperature = Math.round((Double.valueOf(uranPer)/Double.valueOf(coolantPer))*5500);
		BlockStorage.addBlockInfo(b,"uran", String.valueOf(uran-uranPer));
		if(ticks.containsKey(b.getLocation())) {
			ticks.replace(b.getLocation(), burnTime);
		}else {
			ticks.put(b.getLocation(), burnTime);
		}
		
		if(uran500.containsKey(b.getLocation())) {
			uran500.replace(b.getLocation(), uranPer);
		}else {
			uran500.put(b.getLocation(), uranPer);
		}
		
		if(temp.containsKey(b.getLocation())) {
			temp.replace(b.getLocation(), temperature);
		}else {
			temp.put(b.getLocation(), temperature);
		}
		
	}
	public void expolode(Block b, int uranPer) {
		
		if(announceExplosion) {
			if(announceReactorOwner)
				Bukkit.broadcastMessage("You hear a BOOM and a small sob from "+ BlockStorage.getLocationInfo(b.getLocation(), "owner") +" in the distance.");
			else
				Bukkit.broadcastMessage("You hear a BOOM in the distance.");
		}
		
		for(int x=-11;x!=12;x++) {
			for(int y=-11;y!=12;y++) {
				for(int z=-11;z!=12;z++) {
					Location l = b.getLocation().clone().add(x, y, z);
					if(BlockStorage.hasBlockInfo(l)) {
						BlockStorage.clearBlockInfo(l);
						l.getBlock().setType(Material.AIR);
					}
				}
			}
		}
		
		if(biggerExplosion) {
			if(announceExplosion)
				Bukkit.broadcastMessage(ChatColor.DARK_RED+"WARNING: SERVER LAG INCOMING");
			
			for(int x=-uranPer*2;x<=uranPer*2;x=x+4) {
				for(int z=-uranPer*2;z<=uranPer*2;z=z+4) {
					Location l = b.getLocation().clone().add(x, 0, z);
					
					Fireball ball = (Fireball) l.getWorld().spawnEntity(l, EntityType.FIREBALL);
					ball.setInvulnerable(true);
					ball.setYield(Math.min(baseExplosionRadiusPer*uranPer,127));
					ball.setVelocity(new Vector(0,-10,0));
				}
			}
		}else {
			for(int x=-4;x!=8;x=x+4) {
				for(int z=-4;z!=8;z=z+4) {
					Location l = b.getLocation().clone().add(x, 0, z);
					
					Creeper creeper = (Creeper) l.getWorld().spawnEntity(l, EntityType.CREEPER);
					creeper.setInvulnerable(true);
					creeper.ignite();
					creeper.setExplosionRadius(Math.min(baseExplosionRadiusPer*uranPer, 127));
					creeper.setGravity(false);
					creeper.setFuseTicks(0);		
				}
			}
		}		
		if(explosionFallout) {
			BetterNuclearReactor.instance.getServer().getScheduler().runTaskLater(BetterNuclearReactor.instance, new Runnable() {
	
				@Override
				public void run() {
					if(largeExplosionFallout) {
						for(int x = -baseFalloutRadiusPer*uranPer;x<baseFalloutRadiusPer*uranPer;x=x+12) {
							for(int z = -baseFalloutRadiusPer*uranPer;z<baseFalloutRadiusPer*uranPer;z=z+12) {
								Location Loc = b.getLocation().clone().add(x, 0, z);							
								Location AreaLoc = Loc.getWorld().getHighestBlockAt(Loc).getLocation();
								
								AreaEffectCloud Area = (AreaEffectCloud) AreaLoc.getWorld().spawnEntity(AreaLoc, EntityType.AREA_EFFECT_CLOUD);
								Area.setRadius(12);							
								Area.addCustomEffect(new PotionEffect(PotionEffectType.HARM,5,2), true);
								Area.setDuration(falloutTickTimePer*uranPer);
								Area.setParticle(Particle.REDSTONE,new DustOptions(Color.GREEN,1));
							}
						}
					}else {
						Location Loc = b.getLocation();
						
						AreaEffectCloud Area = (AreaEffectCloud) Loc.getWorld().spawnEntity(Loc, EntityType.AREA_EFFECT_CLOUD);
						Area.setRadius(12);							
						Area.addCustomEffect(new PotionEffect(PotionEffectType.HARM,5,2), true);
						Area.setDuration(falloutTickTimePer*uranPer);
						Area.setParticle(Particle.REDSTONE,new DustOptions(Color.GREEN,1));
					}
				}
				
			}, 80L);
		}
		
		

	}
	public void updateStatus(int time,BlockMenu menu, int coolant_out, int uran_out, Player p,Block b,int coolantPer,int uranPer, boolean isRunning) {
		CustomItemStack item = new CustomItemStack(Material.FLINT_AND_STEEL,ChatColor.RESET+"Remaining Time: "+String.valueOf(time)+"t");
		ItemMeta meta = item.getItemMeta();
		List<String> lore = new ArrayList<String>();
		lore.add(ChatColor.GOLD+"Is Running: "+isRunning);
		
		if(coolant_out>32) {
			lore.add(ChatColor.RED+"Heated Coolant in output");
			p.sendMessage(ChatColor.DARK_RED+"[REACTOR]"+ChatColor.RED+" Reactor at"+
			ChatColor.GOLD+" x: "+b.getLocation().getBlockX()+" y: "+b.getLocation().getBlockY()+" z: "+b.getLocation().getBlockZ()+ChatColor.RED
			+" has "+ChatColor.YELLOW+coolant_out+ChatColor.RED+" coolant in output");
		}
		if(uran_out>32) {
			lore.add(ChatColor.RED+"Uran waste in output");
			p.sendMessage(ChatColor.DARK_RED+"[REACTOR]"+ChatColor.RED+" Reactor at"+
			ChatColor.GOLD+" x: "+b.getLocation().getBlockX()+" y: "+b.getLocation().getBlockY()+" z: "+b.getLocation().getBlockZ()+ChatColor.RED
			+" has "+ChatColor.YELLOW+uran_out+ChatColor.RED+" uran waste in output");
		}
		if(isRunning&&temp.get(b.getLocation())>6000) {
			lore.add(ChatColor.DARK_RED+"High heat");
			p.sendMessage(ChatColor.DARK_RED+"[REACTOR]"+ChatColor.RED+" Reactor at"+
					ChatColor.GOLD+" x: "+b.getLocation().getBlockX()+" y: "+b.getLocation().getBlockY()+" z: "+b.getLocation().getBlockZ()+ChatColor.RED
					+" has High heat!");
		}
		if(menu.hasViewer()) {
			lore.add(ChatColor.GRAY+"Coolant Per "+coolantTime+"t: "+coolantPer);
			lore.add(ChatColor.GRAY+"Uran Per " + burnTime + "t: "+uranPer);
			
			if(isRunning) {
				lore.add(ChatColor.YELLOW+"->Current Uran Per " + burnTime + "t: "+uran500.get(b.getLocation()));
				lore.add(temp(temp.get(b.getLocation()))+"->Current temperature: "+temp.get(b.getLocation())+" °C");
				long el = uran500.get(b.getLocation())*powerPer;
				lore.add(ChatColor.YELLOW+"->Current power: "+ChatColor.YELLOW+el+" J/t");
				
			}
			
			long temperature = Math.round((Double.valueOf(uranPer)/Double.valueOf(coolantPer))*5500);
			lore.add(temp(temperature)+"Estimated temperature: "+temperature+" °C");
			long el = uranPer*powerPer;
			lore.add(ChatColor.GRAY+"Estimated power: "+ChatColor.YELLOW+el+" J/t");
			
			meta.setLore(lore);
			item.setItemMeta(meta);
			menu.replaceExistingItem(full_status,item);
		}
	}
	public ChatColor temp(long temp) {
		if(temp <= 4125) {
			return ChatColor.AQUA;
		}if(temp >= 6000) {
			return ChatColor.RED;
		}
		return ChatColor.GREEN;
		
	}
	
	@SuppressWarnings("deprecation")
	public void coolant_status(Block b,BlockMenu menu,int coolant) {
		if(menu.hasViewer()) {
			double percent = (Double.valueOf(coolant)/Double.valueOf(maxcoolant))*100;
			percent= Math.round(percent);
			int coolantPer = Integer.parseInt(BlockStorage.getLocationInfo(b.getLocation(),"coolantPer"));
			
			menu.addMenuClickHandler(coolant_status, (pl, slot, item, action)-> {
				if(!action.isRightClicked()) {
					if(coolantPer!=maxCoolantPer) {
						BlockStorage.addBlockInfo(b, "coolantPer", String.valueOf(coolantPer+1));
					}
				}else {
					if(coolantPer!=1) {
						BlockStorage.addBlockInfo(b, "coolantPer", String.valueOf(coolantPer-1));
					}
				}
				
				return false;
			});
			menu.replaceExistingItem(coolant_status, new CustomItemStack(SlimefunItems.REACTOR_COOLANT_CELL,"&bCoolant Status: &9"+String.valueOf(percent)+"%","&r&fCurrent coolant per "+coolantTime+"t: &7"+String.valueOf(coolantPer),"&r&fLeft Click: &7+1", "&r&fRight Click: &7-1"));
			
			}
		}

	@SuppressWarnings("deprecation")
	public void uran_status(Block b,BlockMenu menu,int uran) {
		if(menu.hasViewer()) {
			double percent = (Double.valueOf(uran)/Double.valueOf(maxuran))*100;
			percent= Math.round(percent);
			
			int uranPer = Integer.parseInt(BlockStorage.getLocationInfo(b.getLocation(),"uranPer"));
			menu.addMenuClickHandler(uran_status, (pl, slot, item, action)-> {
				if(!action.isRightClicked()) {
					if(uranPer!= maxUraniumPer) {
						BlockStorage.addBlockInfo(b, "uranPer", String.valueOf(uranPer+1));
					}
				}else {
					if(uranPer!=1) {
						BlockStorage.addBlockInfo(b, "uranPer", String.valueOf(uranPer-1));
					}
				}
				
				return false;
			});
			
			menu.replaceExistingItem(uran_status, new CustomItemStack(SlimefunItems.URANIUM,"&cFuel Status: &4"+String.valueOf(percent)+"%","&r&fCurrent uran per "+burnTime +"t: &7"+String.valueOf(uranPer),"&r&fLeft Click: &7+1", "&r&fRight Click: &7+1"));
		}
	}
	
	public boolean hasFuel(Block b) {
		final int uranPer = Integer.parseInt(BlockStorage.getLocationInfo(b.getLocation(), "uranPer"));
		final int uran = Integer.parseInt(BlockStorage.getLocationInfo(b.getLocation(), "uran"));
		if(uran>=uranPer) {
			return true;
		}else {
			return false;
		}
	}
	public boolean hasCoolant(Block b) {
		final int coolant = Integer.parseInt(BlockStorage.getLocationInfo(b.getLocation(), "coolant"));
		if(coolant>0) {
			return true;
		}else {
			return false;
		}
	}
	public boolean isRunning(Block b) {
		if(ticks.containsKey(b.getLocation())) {
			if(ticks.get(b.getLocation())==0) {
				return false;
			}else {
				return true;
			}
		}else {
			ticks.put(b.getLocation(), 0);
			return false;
		}
	}
	public void waterLevel(Block b, int coolant) {
		Directional dir = (Directional) b.getBlockData();
		double waterLevel = (coolant/Double.valueOf(maxcoolant))*4.0;
		int rot = Methodes.fac(dir.getFacing());
		for(Vector v : vectors) {
			final Vector relative = Methodes.rotVector(v, rot);
			
			relative.setY(0);
			Block relativeBlock = b.getRelative(relative.getBlockX(), relative.getBlockY(), relative.getBlockZ());
			if(waterLevel>0.0) {
				relativeBlock.setType(Material.WATER);
			}else {
				relativeBlock.setType(Material.AIR);
			}
			relative.setY(1);
			relativeBlock = b.getRelative(relative.getBlockX(), relative.getBlockY(), relative.getBlockZ());
			if(waterLevel>1.0) {
				relativeBlock.setType(Material.WATER);
			}else {
				relativeBlock.setType(Material.AIR);
			}
			relative.setY(2);
			relativeBlock = b.getRelative(relative.getBlockX(), relative.getBlockY(), relative.getBlockZ());
			if(waterLevel>2.0) {
				relativeBlock.setType(Material.WATER);
			}else {
				relativeBlock.setType(Material.AIR);
			}
			relative.setY(3);
			relativeBlock = b.getRelative(relative.getBlockX(), relative.getBlockY(), relative.getBlockZ());
			if(waterLevel>3.0) {
				relativeBlock.setType(Material.WATER);
			}else {
				relativeBlock.setType(Material.AIR);
			}
			
		}
		
		
	}
	public void saveCoolant(Block b, BlockMenu menu) {
		for(int i : inputs_coolant) {
			final int coolant = Integer.parseInt(BlockStorage.getLocationInfo(b.getLocation(), "coolant").replaceAll("[^0-9]", ""));
			ItemStack items = menu.getItemInSlot(i);
			if(items!=null) {
				SlimefunItem sfitems = SlimefunItem.getByItem(items);
				if(sfitems instanceof CoolantCell) {
					if(coolant<=maxcoolant) {
						int amount = items.getAmount();
						int minus = maxcoolant-coolant;
						
						if(amount>minus) {
							BlockStorage.addBlockInfo(b.getLocation(), "coolant", String.valueOf(coolant+minus));
							menu.consumeItem(i,minus);
						}else {
							BlockStorage.addBlockInfo(b.getLocation(), "coolant", String.valueOf(coolant+amount));
							menu.consumeItem(i,amount);
						}
						
					}
				}
			}
		}
	}
	
	public void saveUran(Block b, BlockMenu menu) {
		for(int i : inputs_uran) {
			final int uran = Integer.parseInt(BlockStorage.getLocationInfo(b.getLocation(), "uran").replaceAll("[^0-9]", ""));
			ItemStack items = menu.getItemInSlot(i);
			if(items!=null) {
				SlimefunItem sfitems = SlimefunItem.getByItem(items);
				if(sfitems.isItem(SlimefunItems.URANIUM)) {
					if(uran<=maxuran) {
						int amount = items.getAmount();
						int minus = maxuran-uran;
						
						if(amount>minus) {
							BlockStorage.addBlockInfo(b.getLocation(), "uran", String.valueOf(uran+minus));
							menu.consumeItem(i,minus);
						}else {
							BlockStorage.addBlockInfo(b.getLocation(), "uran", String.valueOf(uran+amount));
							menu.consumeItem(i,amount);
						}
						
					}
				}
			}
		}
	}
	public void uniqueTickk(Block b, BlockMenu menu) {
		boolean stat = setState(b);
		menu.replaceExistingItem(4, status(stat,menu,b));
		final String isBuild = BlockStorage.getLocationInfo(b.getLocation(),"build");
		final int coolant = Integer.parseInt(BlockStorage.getLocationInfo(b.getLocation(), "coolant").replaceAll("[^0-9]", ""));
		if(isBuild.equals("true")) {
			waterLevel(b,coolant);
		}
		
		
	}
	
	@SuppressWarnings("deprecation")
	public ItemStack status(Boolean b, BlockMenu menu,Block Block) {
		ItemStack item2 = new ItemStack(Material.FLINT_AND_STEEL);
		ItemMeta m = item2.getItemMeta();
		m.setDisplayName(ChatColor.RESET+"Status");
		List<String> lore = new ArrayList<String>();
		
		if(b) {
			lore.add(ChatColor.GREEN+"Multiblock complete "+ChatColor.DARK_GREEN+"✔");
		}else {
			lore.add(ChatColor.RED+"Multiblock not complete "+ChatColor.DARK_RED+"✘");
			lore.add(ChatColor.GRAY+"(Click to show "+ChatColor.AQUA+"Reactor Core Hologram"+ChatColor.GRAY+")");
			menu.addMenuClickHandler(4, (p, slot, item, action) -> {
				spawnParticeReactor(Block);
                return false;
            });
		}
		m.setLore(lore);
		item2.setItemMeta(m);
		return item2;
		
	}
	public boolean setState(Block b) {
		if(checkBuild(b)) {
			BlockStorage.addBlockInfo(b, "build", "true");
			return true;
		}else {
			BlockStorage.addBlockInfo(b, "build", "false");
			return false;
		}
	}
	public BlockPlaceHandler BlockPlaceHandler() {
		return new BlockPlaceHandler(false) {

			@Override
			public void onPlayerPlace(BlockPlaceEvent e) {
				
				if(checkBuild(e.getBlock())) {
					BlockStorage.addBlockInfo(e.getBlock(), "build", "true");
					e.getPlayer().sendMessage(ChatColor.AQUA+"Reactor Built");
				}else {
					BlockStorage.addBlockInfo(e.getBlock(), "build", "false");
					BlockMenu inv= BlockStorage.getInventory(e.getBlock());
					inv.replaceExistingItem(4, status(false,inv,e.getBlock()));
					
				}
				ticks.put(e.getBlock().getLocation(), 0);
				BlockStorage.addBlockInfo(e.getBlock(),"coolant","0");
				BlockStorage.addBlockInfo(e.getBlock(),"uran","0");
				BlockStorage.addBlockInfo(e.getBlock(),"uranPer","1");
				BlockStorage.addBlockInfo(e.getBlock(),"coolantPer","2");
				BlockStorage.addBlockInfo(e.getBlock(),"owner",e.getPlayer().getName());
				spawnParticeReactor(e.getBlock());
				
				
			}
			
		};
	}
	public void spawnParticeReactor(Block b) {
		renderBadBlocks(hologramTime,b);
		renderParticles(hologramTime,b);
	}
	public void renderParticles(int time,Block b){
		Directional dir = (Directional) b.getBlockData();

		int rot = Methodes.fac(dir.getFacing());


		int scheduleId = Bukkit.getScheduler().scheduleSyncRepeatingTask(BetterNuclearReactor.instance,()->{
			for(Map.Entry<Vector, SlimefunItemStack> entry : blocks.entrySet()) {

				final Vector relative = Methodes.rotVector(entry.getKey(), rot);
				final SlimefunItemStack relativeItemStack = entry.getValue();
				final Material relativeMaterial = relativeItemStack.getType();
				final Block relativeBlock = b.getRelative(relative.getBlockX(), relative.getBlockY(), relative.getBlockZ());

				if(!relativeBlock.getType().equals(relativeMaterial)) {
					particle(relativeBlock, relativeMaterial);
				}
			}
		},0,15);
		Bukkit.getScheduler().runTaskLater(BetterNuclearReactor.instance, ()-> {
			Bukkit.getScheduler().cancelTask(scheduleId);
		}, time);
	}
	public void renderBadBlocks(int killTime, Block b){
		Directional dir = (Directional) b.getBlockData();

		int rot = Methodes.fac(dir.getFacing());

		Team badBlockTeam = Bukkit.getScoreboardManager().getMainScoreboard().getTeam("badBlock");

		List<MagmaCube> magmaCubes = new ArrayList<MagmaCube>();

		for(Map.Entry<Vector, SlimefunItemStack> entry : blocks.entrySet()) {

			final Vector relative = Methodes.rotVector(entry.getKey(), rot);
			final SlimefunItemStack relativeItemStack = entry.getValue();
			final Material relativeMaterial = relativeItemStack.getType();
			final Block relativeBlock = b.getRelative(relative.getBlockX(), relative.getBlockY(), relative.getBlockZ());

			if(!relativeBlock.getType().equals(relativeMaterial)&&!relativeBlock.getType().equals(Material.AIR)) {
				MagmaCube magmaCube = (MagmaCube) relativeBlock.getWorld().spawnEntity(relativeBlock.getLocation().clone().add(.5,.25,.5), EntityType.MAGMA_CUBE);
				magmaCube.setAI(false);
				magmaCube.setSize(1);
				magmaCube.setInvulnerable(true);
				magmaCube.setCollidable(false);
				magmaCube.setGravity(false);
				magmaCube.setSilent(true);
				magmaCube.setGlowing(true);
				magmaCube.setInvisible(true);

				badBlockTeam.addEntry(magmaCube.getUniqueId().toString());
				magmaCubes.add(magmaCube);
			}
		}
		Bukkit.getScheduler().runTaskLater(BetterNuclearReactor.instance, ()-> {
				for(MagmaCube magmaCube : magmaCubes){
					magmaCube.remove();
				}
		}, killTime);
	}
	public boolean checkBuild(Block b) {
		Directional dir = (Directional) b.getBlockData();
		
		int rot = Methodes.fac(dir.getFacing());
		
		for(Map.Entry<Vector, SlimefunItemStack> entry : blocks.entrySet()) {
			
			final Vector relativeTemp = entry.getKey();
			final Vector relative = Methodes.rotVector(relativeTemp, rot);
            final SlimefunItemStack relativeItemStack = entry.getValue();
            final String relativeId = relativeItemStack.getItemId();
            
            final Material relativeMaterial = relativeItemStack.getType();
            final Block relativeBlock = b.getRelative(relative.getBlockX(), relative.getBlockY(), relative.getBlockZ());
            
            if(relativeBlock.getType().equals(relativeMaterial)) {
            	final String id = BlockStorage.getLocationInfo(relativeBlock.getLocation(), "id");
            	if(id==null) {
            		
            		return false;
            	}
            	if(id.equals(relativeId)) {
            		
            	}else {
            		
            		return false;
            	}
            }else {
            	
            	return false;
            }
            
		}
		return true;
	}
	public void particle(Block b, Material m) {
		Color c = geColor(m);
		//b.getWorld().spawnFallingBlock(b.getLocation(), new MaterialData(m));
		if(!b.getType().equals(m)) {
			b.getWorld().spawnParticle(Particle.REDSTONE, b.getLocation().getX() +0.5,b.getLocation().getY() +0.5,b.getLocation().getZ() +0.5, particles, 0.1 , 0.1 , 0.1 ,new DustOptions(c,1));
		}
		
	}
	public Color geColor(Material m) {
		if(m.equals(Material.GRAY_STAINED_GLASS)) {
			return Color.GRAY;
		}else if(m.equals(Material.IRON_BLOCK)) {
			return Color.WHITE;
		}else if(m.equals(Material.RED_WOOL)) {
			return Color.RED;
		}else if(m.equals(Material.LIGHT_BLUE_WOOL)) {
			return Color.BLUE;
		}else if(m.equals(Material.ANCIENT_DEBRIS)) {
			return Color.ORANGE;
		}else if(m.equals(Material.PLAYER_HEAD)) {
			return Color.GREEN;
		}
		return null;
	}
	@Override
	public int[] getInputSlots() {
		// TODO Auto-generated method stub
		return inputs;
	}

	@Override
	public int[] getOutputSlots() {
		// TODO Auto-generated method stub
		return outputs;
	}
	@Override
	public EnergyNetComponentType getEnergyComponentType() {
		// TODO Auto-generated method stub
		return EnergyNetComponentType.GENERATOR;
	}

	@Override
	public int getCapacity() {
		// TODO Auto-generated method stub
		return powerPer*maxUraniumPer*2;
	}
	public BlockBreakHandler onBreak() {
        return new BlockBreakHandler(false, false) {

            @Override
            public void onPlayerBreak(BlockBreakEvent e, ItemStack item, List<ItemStack> drops) {
            	
                Block b = e.getBlock();
                BlockMenu inv = BlockStorage.getInventory(b);

                
                
                if (inv != null) {
                    inv.dropItems(b.getLocation(), getInputSlots());
                    inv.dropItems(b.getLocation(), getOutputSlots());
                }
                
				
                
            }
        };
    }
	@SuppressWarnings("deprecation")
	private void constructMenu(BlockMenuPreset preset) {
		preset.addItem(10, new CustomItemStack(SlimefunItems.REACTOR_COOLANT_CELL,"&bCoolant Slot", "", "&fThis Slot accepts Coolant Cells"),
                ChestMenuUtils.getEmptyClickHandler());
		preset.addItem(16, new CustomItemStack(SlimefunItems.URANIUM,"&7Fuel Slot", "", "&fThis Slot accepts radioactive Fuel such as:", "&2Uranium"),
                ChestMenuUtils.getEmptyClickHandler());
    	
        for (int i : border) {
            preset.addItem(i, new CustomItemStack(new ItemStack(Material.GRAY_STAINED_GLASS_PANE), " "),
                ChestMenuUtils.getEmptyClickHandler());
        }
        for (int i : coolantBorder) {
            preset.addItem(i, new CustomItemStack(new ItemStack(Material.CYAN_STAINED_GLASS_PANE), " "),
                ChestMenuUtils.getEmptyClickHandler());
        }
        for (int i : uranBorder) {
            preset.addItem(i, new CustomItemStack(new ItemStack(Material.ORANGE_STAINED_GLASS_PANE), " "),
                ChestMenuUtils.getEmptyClickHandler());
        }
        for (int i : coolantoutputborder) {
            preset.addItem(i, new CustomItemStack(new ItemStack(Material.BLUE_STAINED_GLASS_PANE), " "),
                ChestMenuUtils.getEmptyClickHandler());
        }
        for (int i : uranoutputborder) {
            preset.addItem(i, new CustomItemStack(new ItemStack(Material.RED_STAINED_GLASS_PANE), " "),
                ChestMenuUtils.getEmptyClickHandler());
        }

        for (int i : getOutputSlots()) {
            preset.addMenuClickHandler(i, new ChestMenu.AdvancedMenuClickHandler() {

                @Override
                public boolean onClick(Player p, int slot, ItemStack cursor, ClickAction action) {
                    return false;
                }

                @Override
                public boolean onClick(InventoryClickEvent e, Player p, int slot, ItemStack cursor,
                                       ClickAction action) {
                    return cursor == null || cursor.getType() == Material.AIR;
                }
            });
        }
	}

	@Override
	public int getGeneratedOutput(Location l, Config data) {
		// TODO Auto-generated method stub
		return 0;
	}

}
