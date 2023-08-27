package me.CAPS123987.Cargo;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockBreakHandler;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockPlaceHandler;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.implementation.items.SimpleSlimefunItem;
import io.github.thebusybiscuit.slimefun4.implementation.items.misc.CoolantCell;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun4.libraries.dough.protection.Interaction;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import me.CAPS123987.IIIDmultiblock.ReactorCore;
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
import me.mrCookieSlime.Slimefun.api.inventory.DirtyChestMenu;
import me.mrCookieSlime.Slimefun.api.item_transport.ItemTransportFlow;
import net.md_5.bungee.api.ChatColor;

public class ReactorInput extends SimpleSlimefunItem<BlockTicker> /*implements ETInventoryBlock*/{
	private static final int[] inputs = {10,11,12,13,15,16,19,20,21,22,24,25,28,29,30,31,33,34,37,38,39,40,42,43};
	private static final int[] outputs = {};
	private static final int[] border = {};
	private static final int[] inputBorder = {0,1,3,4,5,7,8,9,17,18,26,27,35,36,44,45,46,47,48,49,50,51,52,53,14,23,32,41};
	private static final int[] outputBorder = {};
	private static final Vector[] sides = {new Vector(1,0,0),new Vector(-1,0,0),new Vector(0,0,1),new Vector(0,0,-1),};
	private static final int[] coolant = {10,11,12,13,19,20,21,22,28,29,30,31,37,38,39,40};
	private static final int[] uran = {15,16,24,25,33,34,42,43};
	
	public ReactorInput() {
		super(Items.betterReactor, Items.REACTOR_INPUT ,RecipeType.ENHANCED_CRAFTING_TABLE , Items.recipe_REACTOR_INPUT);
		//createPreset(this, this::constructMenu);
		addItemHandler(BlockPlaceHandler(),onBreak());
		
		new BlockMenuPreset(getId(),this.getItemName()) {

			@Override
			public void init() {
				constructMenu(this);
				
			}

			@Override
			public boolean canOpen(Block b, Player p) {
				return p.hasPermission("slimefun.inventory.bypass") || Slimefun.getProtectionManager().hasPermission(p, b.getLocation(), Interaction.INTERACT_BLOCK);
			}

			@Override
			public int[] getSlotsAccessedByItemTransport(ItemTransportFlow flow) {
				if (flow == ItemTransportFlow.INSERT) {
                    return getInputSlots();
                } else {
                    return getOutputSlots();
                }
			}
			@Override
            public int[] getSlotsAccessedByItemTransport(DirtyChestMenu menu, ItemTransportFlow flow, ItemStack item) {
                if (flow == ItemTransportFlow.INSERT) {
                    if (SlimefunItem.getByItem(item) instanceof CoolantCell) {
                        return coolant;
                    } else {
                        return uran;
                    }
                } else {
                    return getOutputSlots();
                }
            }
			
		};
	}
	@Override
	public BlockTicker getItemHandler() {
		// TODO Auto-generated method stub
		return new BlockTicker() {

			@Override
			public boolean isSynchronized() {
				// TODO Auto-generated method stub
				return true;
			}

			@Override
			public void tick(Block b, SlimefunItem item, Config data) {
				Location l = b.getLocation();
				int X = Methodes.toInt(BlockStorage.getLocationInfo(l, "X"));
				int Y = Methodes.toInt(BlockStorage.getLocationInfo(l, "Y"));
				int Z = Methodes.toInt(BlockStorage.getLocationInfo(l, "Z"));
				String world = BlockStorage.getLocationInfo(b.getLocation(), "World");
				Location loc = new Location(Bukkit.getWorld(world),X,Y,Z);
				BlockMenu menu = BlockStorage.getInventory(b);
				BlockMenu menuReactor = BlockStorage.getInventory(loc.getBlock());
				if(menuReactor==null) {
					menu.dropItems(b.getLocation(), getInputSlots());
                    menu.dropItems(b.getLocation(), getOutputSlots());
                    Object[] colec = item.getDrops().toArray();
                    b.getWorld().dropItemNaturally(loc, (ItemStack) colec[0]);
					b.setType(Material.AIR);
					BlockStorage.clearBlockInfo(b);
				}
				for(int i : inputs) {
					if(menuReactor==null) {
						return;
					}
					ItemStack itemT = menu.getItemInSlot(i);
					if(itemT!=null) {
						SlimefunItem sfitem = SlimefunItem.getByItem(itemT);
						if(sfitem instanceof CoolantCell) {
							ItemStack over = menuReactor.pushItem(itemT, ReactorCore.inputs_coolant);
							if(over == null) {
								menu.replaceExistingItem(i, new ItemStack(Material.AIR));
							}
						}
						if(sfitem!=null) {
							if(sfitem.isItem(SlimefunItems.URANIUM)) {
								ItemStack over = menuReactor.pushItem(itemT, ReactorCore.inputs_uran);
								if(over == null) {
									menu.replaceExistingItem(i, new ItemStack(Material.AIR));
								}
							}
						}
					}
				}
				
			}
			
		};
	}
	public BlockPlaceHandler BlockPlaceHandler() {
		return new BlockPlaceHandler(false) {

			@Override
			public void onPlayerPlace(BlockPlaceEvent e) {
				// TODO Auto-generated method stub
				Block b = e.getBlock();
				boolean found = false;
				for(Vector v: sides) {
					final Block newBlock = b.getRelative(v.getBlockX(), v.getBlockY(), v.getBlockZ());
					SlimefunItem item = BlockStorage.check(newBlock.getLocation());
					if(item instanceof ReactorCore) {
						BlockStorage.addBlockInfo(b, "X", String.valueOf(newBlock.getX()));
						BlockStorage.addBlockInfo(b, "Y", String.valueOf(newBlock.getY()));
						BlockStorage.addBlockInfo(b, "Z", String.valueOf(newBlock.getZ()));
						BlockStorage.addBlockInfo(b, "World", String.valueOf(newBlock.getWorld().getName()));
						found = true;
					}
				}
				if(found == false) {
					e.getPlayer().sendMessage(ChatColor.RED+"No Reactor Core found");
					BlockStorage.clearBlockInfo(b);
					e.setCancelled(true);
				}
				
			}
			
		};
	}

	public int[] getInputSlots() {
		// TODO Auto-generated method stub
		return inputs;
	}

	public int[] getOutputSlots() {
		// TODO Auto-generated method stub
		return outputs;
	}
	@SuppressWarnings("deprecation")
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
        
        preset.addItem(2, new CustomItemStack(SlimefunItems.REACTOR_COOLANT_CELL,ChatColor.AQUA+"Reactor Coolant Cell",""),ChestMenuUtils.getEmptyClickHandler());
        
        preset.addItem(6, new CustomItemStack(SlimefunItems.URANIUM,ChatColor.DARK_RED+"Uranium",""),ChestMenuUtils.getEmptyClickHandler());
        

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
	
}
