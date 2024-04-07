package me.CAPS123987.Machines;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import me.CAPS123987.Item.Items;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockTicker;
import org.bukkit.block.Block;

public class ReactorStop extends SlimefunItem {
    public ReactorStop() {
        super(Items.betterReactor, Items.REACTOR_STOP, RecipeType.ENHANCED_CRAFTING_TABLE, Items.recipe_TEST_ITEM);
    }
    public BlockTicker getItemHandler() {
        return new BlockTicker() {
            @Override
            public boolean isSynchronized() {
                return false;
            }

            @Override
            public void tick(Block b, SlimefunItem item, Config data) {

            }
        };
    }
}
