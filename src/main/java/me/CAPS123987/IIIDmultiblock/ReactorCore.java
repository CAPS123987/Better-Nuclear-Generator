package me.CAPS123987.IIIDmultiblock;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.block.Block;
import org.bukkit.block.data.Directional;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;
import org.bukkit.util.Vector;

import io.github.thebusybiscuit.slimefun4.api.events.PlayerRightClickEvent;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.attributes.EnergyNetComponent;
import io.github.thebusybiscuit.slimefun4.core.attributes.EnergyNetProvider;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockBreakHandler;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockPlaceHandler;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockUseHandler;
import io.github.thebusybiscuit.slimefun4.core.networks.energy.EnergyNetComponentType;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.implementation.items.SimpleSlimefunItem;
import io.github.thebusybiscuit.slimefun4.implementation.items.misc.CoolantCell;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import me.CAPS123987.Item.Items;
import me.CAPS123987.Utils.ETInventoryBlock;
import me.CAPS123987.Utils.Methodes;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ClickAction;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineFuel;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockTicker;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import net.md_5.bungee.api.ChatColor;

public class ReactorCore extends SimpleSlimefunItem<BlockTicker> implements EnergyNetProvider, ETInventoryBlock{
	
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
	private final static int burnTime = 1000;

	
	private final Map<Vector, SlimefunItemStack> blocks;
	public HashMap<Location,Integer> ticks = new HashMap<Location,Integer>();
	
	public ReactorCore(final Map<Vector, SlimefunItemStack> blocks) {
		super(Items.betterReactor,Items.REACTOR_CORE, RecipeType.ENHANCED_CRAFTING_TABLE, Items.recipe_TEST_ITEM);
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
					return;
				}
				
				saveCoolant(b,menu);
				saveUran(b,menu);
				coolant_status(b,menu,coolant);
				uran_status(b,menu,uran);
				runReaction(b, menu);
			}
			
		};
	}
	public void runReaction(Block b,BlockMenu menu) {
		int coolant = Integer.parseInt(BlockStorage.getLocationInfo(b.getLocation(), "coolant"));
		int uran = Integer.parseInt(BlockStorage.getLocationInfo(b.getLocation(), "uran"));
		if(isRunning(b)) {
			int tick = ticks.get(b.getLocation());
			
			if(tick==1) {
				menu.pushItem(new CustomItemStack(SlimefunItems.NEPTUNIUM,1), outputuran);
				BlockStorage.addBlockInfo(b,"uran", String.valueOf(uran-1));
			}
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
			
			updateStatus(tick, menu, coolant_out, uran_out);
			
			if(!hasCoolant(b)|| coolant_out==64||uran_out==64) {
				expolode(b);
				ticks.remove(b.getLocation());
				
			}else {
				ticks.replace(b.getLocation(), tick-1);
				addCharge(b.getLocation(),2048);
				BlockStorage.addBlockInfo(b,"coolant", String.valueOf(coolant-3));
				menu.pushItem(new CustomItemStack(Items.HEATED_COOLANT,1), outputcoolant);
				
			}
			
			return;
		}
		if(!hasFuel(b)) {
			return;
		}
		if(ticks.containsKey(b.getLocation())) {
			ticks.replace(b.getLocation(), burnTime);
		}else {
			ticks.put(b.getLocation(), burnTime);
		}
		
	}
	public void expolode(Block b) {
		Bukkit.broadcastMessage("Boom");
	}
	public void updateStatus(int time,BlockMenu menu, int coolant_out, int uran_out) {
		CustomItemStack item = new CustomItemStack(Material.FLINT_AND_STEEL,"Remaining Time: "+String.valueOf(time)+"");
		ItemMeta meta = item.getItemMeta();
		List<String> lore = new ArrayList<String>();
		if(coolant_out>32) {
			lore.add(ChatColor.RED+"Heated Coolant in output");
		}
		if(uran_out>32) {
			lore.add(ChatColor.RED+"Trash in output");
		}
		meta.setLore(lore);
		item.setItemMeta(meta);
		menu.replaceExistingItem(full_status,item);
	}
	public void coolant_status(Block b,BlockMenu menu,int coolant) {
		if(menu.hasViewer()) {
			double percent = (Double.valueOf(coolant)/Double.valueOf(maxcoolant))*100;
			percent= Math.round(percent);
			menu.replaceExistingItem(coolant_status, new CustomItemStack(SlimefunItems.REACTOR_COOLANT_CELL,"&bCoolant Status: &9"+String.valueOf(percent)+"%",""));
			
			}
		}
	public void uran_status(Block b,BlockMenu menu,int uran) {
		if(menu.hasViewer()) {
			double percent = (Double.valueOf(uran)/Double.valueOf(maxuran))*100;
			percent= Math.round(percent);
			menu.replaceExistingItem(uran_status, new CustomItemStack(SlimefunItems.URANIUM,"&cFuel Status: &4"+String.valueOf(percent)+"%",""));
		}
	}
	public boolean hasFuel(Block b) {
		final int uran = Integer.parseInt(BlockStorage.getLocationInfo(b.getLocation(), "uran"));
		if(uran>0) {
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
		double waterLevel = (coolant/128.0)*4.0;
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
			lore.add(ChatColor.GRAY+"(Click to show help)");
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
				spawnParticeReactor(e.getBlock());
				
				
			}
			
		};
	}
	public void spawnParticeReactor(Block b) {
		Directional dir = (Directional) b.getBlockData();
		
		int rot = Methodes.fac(dir.getFacing());
		
		
		for(Map.Entry<Vector, SlimefunItemStack> entry : blocks.entrySet()) {
			
			final Vector relativeTemp = entry.getKey();
			
			final Vector relative = Methodes.rotVector(relativeTemp, rot);
			
            final SlimefunItemStack relativeItemStack = entry.getValue();
            final Material relativeMaterial = relativeItemStack.getType();
            final Block relativeBlock = b.getRelative(relative.getBlockX(), relative.getBlockY(), relative.getBlockZ());
            
            
            
            particle(relativeBlock, relativeMaterial);
            //e.getBlock().getWorld().spawnParticle(Particle.REDSTONE, X, Y, Z, 50, 1 , 1 , 1 ,new DustOptions(Color.BLACK,1));
            //relativeBlock.setType(relativeMaterial);		
		}
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
			b.getWorld().spawnParticle(Particle.REDSTONE, b.getLocation().getX() +0.5,b.getLocation().getY() +0.5,b.getLocation().getZ() +0.5, 100, 0.1 , 0.1 , 0.1 ,new DustOptions(c,1));
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
		return 4096;
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
