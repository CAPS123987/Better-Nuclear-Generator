package me.CAPS123987.IIIDmultiblock;

import java.util.List;
import java.util.ArrayList;
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
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockBreakHandler;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockPlaceHandler;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockUseHandler;
import io.github.thebusybiscuit.slimefun4.core.networks.energy.EnergyNetComponentType;
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
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockTicker;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import net.md_5.bungee.api.ChatColor;

public class ReactorCore extends SimpleSlimefunItem<BlockTicker> implements EnergyNetComponent, ETInventoryBlock{
	
	public final static int[] inputs = {19,28,37};
	public final static int[] outputs = {25,34,43};
	public final int[] border = {0,1,2,3,4,5,6,7,8};
	public final int[] inputBorder = {9,10,11,18,20,27,29,36,38,45,46,47};
	public final int[] outputBorder = {15,16,17,24,26,33,35,42,44,51,52,53};
	private int uniqueTick = 0;
	public final static int maxcoolant = 128;

	
	private final Map<Vector, SlimefunItemStack> blocks;
	
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
				BlockMenu menu = BlockStorage.getInventory(b);
				Location l = b.getLocation();
				if(uniqueTick==5) {
					uniqueTickk(b, menu);
				}
				
				final String isBuild = BlockStorage.getLocationInfo(l,"build");
				final int coolant = Integer.parseInt(BlockStorage.getLocationInfo(l, "coolant").replaceAll("[^0-9]", ""));
				
				if(isBuild.equals("false")) {
					return;
				}
				saveCoolant(b,menu,coolant);
				
				
				
			}
			
		};
	}
	public void saveCoolant(Block b, BlockMenu menu, int coolant) {
		for(int i : inputs) {
			ItemStack items = menu.getItemInSlot(i);
			Bukkit.broadcastMessage("jop1");
			if(items==null) {
				break;
			}
			SlimefunItem sfitems = SlimefunItem.getByItem(items);
			Bukkit.broadcastMessage("jop2");
			if(sfitems instanceof CoolantCell) {
				Bukkit.broadcastMessage("jop3");
				if(coolant<maxcoolant) {
					int amount = items.getAmount();
					BlockStorage.addBlockInfo(b.getLocation(), "coolant", String.valueOf(coolant+amount));
				}
			}
		}
	}
	public void uniqueTickk(Block b, BlockMenu menu) {
		boolean stat = setState(b);
		menu.replaceExistingItem(4, status(stat));
		
	}
	public ItemStack status(Boolean b) {
		ItemStack item = new ItemStack(Material.FLINT_AND_STEEL);
		ItemMeta m = item.getItemMeta();
		m.setDisplayName(ChatColor.RESET+"Status");
		List<String> lore = new ArrayList<String>();
		
		if(b) {
			lore.add(ChatColor.GREEN+"Multiblock complete "+ChatColor.DARK_GREEN+"✔");
		}else {
			lore.add(ChatColor.RED+"Multiblock not complete "+ChatColor.DARK_RED+"✘");
		}
		m.setLore(lore);
		item.setItemMeta(m);
		return item;
		
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
					
				}
				BlockStorage.addBlockInfo(e.getBlock(),"coolant","0");
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
			return Color.BLACK;
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

                e.getBlock().getWorld().setChunkForceLoaded(b.getLocation().getChunk().getX(), b.getLocation().getChunk().getZ(), false);
                
                if (inv != null) {
                    inv.dropItems(b.getLocation(), getInputSlots());
                    inv.dropItems(b.getLocation(), getOutputSlots());
                }
                
				
                
            }
        };
    }
	private void constructMenu(BlockMenuPreset preset) {
    	
    	
        for (int i : border) {
            preset.addItem(i, new CustomItemStack(new ItemStack(Material.GRAY_STAINED_GLASS_PANE), " "),
                ChestMenuUtils.getEmptyClickHandler());
        }
        for (int i : inputBorder) {
            preset.addItem(i, new CustomItemStack(new ItemStack(Material.CYAN_STAINED_GLASS_PANE), " "),
                ChestMenuUtils.getEmptyClickHandler());
        }
        for (int i : outputBorder) {
            preset.addItem(i, new CustomItemStack(new ItemStack(Material.ORANGE_STAINED_GLASS_PANE), " "),
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

}
