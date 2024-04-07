package me.CAPS123987.Machines;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import me.CAPS123987.Item.Items;
import me.CAPS123987.Utils.ETInventoryBlock;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockTicker;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Lightable;
import org.bukkit.inventory.ItemStack;

public class ReactorStop extends SlimefunItem implements ETInventoryBlock {
    private int uniqueTick = 0;
    private final int slot1 = 12;
    private final int slot2 = 14;
    public final static int[] border = {0,1,2,3,4,5,6,7,8,9,10,11,13,15,16,17,18,19,20,21,22,23,24,25,26};
    public ReactorStop() {
        super(Items.betterReactor, Items.REACTOR_STOP, RecipeType.ENHANCED_CRAFTING_TABLE, Items.recipe_TEST_ITEM);
        createPreset(this, this::constructMenu);
        addItemHandler(getItemHandler());
    }
    public BlockTicker getItemHandler() {
        return new BlockTicker() {
            @Override
            public boolean isSynchronized() {
                return false;
            }
            public void uniqueTick() {
                if (uniqueTick == 6) {
                    uniqueTick = 0;
                    return;
                }
                uniqueTick++;

            }

            @Override
            public void tick(Block b, SlimefunItem item, Config data) {
                if (uniqueTick != 5) {
                    return;
                }
                Lightable light = (Lightable) b.getBlockData();
                if(!light.isLit()) {
                    BlockStorage.addBlockInfo(b, "status", "ON");
                    return;
                }
                BlockMenu menu = BlockStorage.getInventory(b);
                ItemStack item1 = menu.getItemInSlot(slot1);
                ItemStack item2 = menu.getItemInSlot(slot2);

                if (item1 == null || item2 == null) {
                    return;
                }

                SlimefunItem sfitem1 = SlimefunItem.getByItem(item1);
                SlimefunItem sfitem2 = SlimefunItem.getByItem(item2);

                if (sfitem1 == null || sfitem2 == null) {
                    return;
                }

                if(sfitem1.isItem(Items.GRAPHITE) && sfitem2.isItem(Items.GRAPHITE)) {
                    menu.replaceExistingItem(slot1, new ItemStack(Material.AIR));
                    menu.replaceExistingItem(slot2, new ItemStack(Material.AIR));
                    BlockStorage.addBlockInfo(b, "status", "OFF");
                }
            }
        };
    }
    private void constructMenu(BlockMenuPreset preset) {
        for (int i : border) {
            preset.addItem(i, ChestMenuUtils.getBackground(),
                    ChestMenuUtils.getEmptyClickHandler());
        }
    }

    @Override
    public int[] getInputSlots() {
        return new int[0];
    }

    @Override
    public int[] getOutputSlots() {
        return new int[0];
    }
}
