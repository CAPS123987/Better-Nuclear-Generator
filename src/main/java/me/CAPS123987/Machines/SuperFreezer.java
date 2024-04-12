package me.CAPS123987.Machines;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.attributes.EnergyNetComponent;
import io.github.thebusybiscuit.slimefun4.core.networks.energy.EnergyNetComponentType;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.implementation.items.SimpleSlimefunItem;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import me.CAPS123987.Item.Items;
import me.CAPS123987.Utils.ETInventoryBlock;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ClickAction;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockTicker;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;

public class SuperFreezer extends SimpleSlimefunItem<BlockTicker> implements EnergyNetComponent, ETInventoryBlock{
	public final static int[] inputs = {19,20};
	public final static int[] outputs = {24,25};
	public final static int[] input_border = {9,10,11,12,18,21,27,28,29,30};
	public final static int[] output_border = {14,15,16,17,23,26,32,33,34,35};
	public final static int[] border = {0,1,2,3,4,5,6,7,8,13,22,31,36,37,38,39,40,41,42,43,44};
	public SuperFreezer() {
		super(Items.betterReactor,Items.SUPER_FREEZER,RecipeType.ENHANCED_CRAFTING_TABLE,Items.recipe_SUPER_FREEZER);
		createPreset(this, this::constructMenu);
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
			public void tick(Block b, SlimefunItem item2, Config data) {
				BlockMenu menu = BlockStorage.getInventory(b);
				if(getCharge(b.getLocation())>=50) {
					for(int i:inputs) {
						ItemStack item = menu.getItemInSlot(i);
						if(item != null) {
							SlimefunItem sfitem = SlimefunItem.getByItem(item);
							if(sfitem !=null) {
								if(sfitem.isItem(Items.HEATED_COOLANT)) {
									if(menu.pushItem(new CustomItemStack(SlimefunItems.REACTOR_COOLANT_CELL), outputs)==null) {
										menu.consumeItem(i,1);
									}
								}
								
							}
							if(item.isSimilar(new ItemStack(Material.WATER_BUCKET))) {
								
								if(menu.getItemInSlot(outputs[0])==null||menu.fits(new ItemStack(Material.BUCKET), outputs[0])) {
									
									if(menu.pushItem(new ItemStack(Material.BUCKET), outputs[0])==null) {
										
										if(menu.getItemInSlot(outputs[1])==null||menu.fits(new CustomItemStack(SlimefunItems.REACTOR_COOLANT_CELL), outputs[1])) {
											menu.pushItem(new CustomItemStack(SlimefunItems.REACTOR_COOLANT_CELL), outputs[1]);
											menu.consumeItem(i);
											removeCharge(b.getLocation(),50);
										}else {
											menu.consumeItem(outputs[0]);
										}
									}
	
								}
							}
							if(item.isSimilar(new ItemStack(Material.ICE))) {
								if(menu.pushItem(new CustomItemStack(SlimefunItems.REACTOR_COOLANT_CELL), outputs)==null) {
									menu.consumeItem(i);
									removeCharge(b.getLocation(),50);
								}
							}
							if(item.isSimilar(new ItemStack(Material.PACKED_ICE))) {
								if(menu.pushItem(new CustomItemStack(SlimefunItems.REACTOR_COOLANT_CELL), outputs)==null) {
									menu.consumeItem(i);
									removeCharge(b.getLocation(),50);
								}
							}
							if(item.isSimilar(new ItemStack(Material.BLUE_ICE))) {
								if(menu.pushItem(new CustomItemStack(SlimefunItems.REACTOR_COOLANT_CELL), outputs)==null) {
									menu.consumeItem(i);
									removeCharge(b.getLocation(),50);
								}
							}
							
						}
						
					}
				}
				
			}
			
		};
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
		return EnergyNetComponentType.CONSUMER;
	}

	@Override
	public int getCapacity() {
		// TODO Auto-generated method stub
		return 300;
	}
	@SuppressWarnings("deprecation")
	private void constructMenu(BlockMenuPreset preset) {
        for (int i : border) {
            preset.addItem(i, new CustomItemStack(new ItemStack(Material.GRAY_STAINED_GLASS_PANE), " "),
                ChestMenuUtils.getEmptyClickHandler());
        }
        for (int i : input_border) {
            preset.addItem(i, new CustomItemStack(new ItemStack(Material.CYAN_STAINED_GLASS_PANE), " "),
                ChestMenuUtils.getEmptyClickHandler());
        }
        for (int i : output_border) {
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
