package me.CAPS123987.Item;

import io.github.thebusybiscuit.slimefun4.api.geo.GEOResource;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.implementation.items.blocks.UnplaceableBlock;
import me.CAPS123987.BetterNuclearReactor.BetterNuclearReactor;
import org.bukkit.NamespacedKey;
import org.bukkit.World.Environment;
import org.bukkit.block.Biome;
import org.bukkit.inventory.ItemStack;

public class Graphite extends UnplaceableBlock implements GEOResource{
	public Graphite() {
        super(Items.betterReactor, Items.GRAPHITE, RecipeType.GEO_MINER, new ItemStack[0]);
        register();
    }
	private final NamespacedKey key = new NamespacedKey(BetterNuclearReactor.getInstance(), "Graphite");
	@Override
	public NamespacedKey getKey() {
		// TODO Auto-generated method stub
		return key;
	}

	@Override
	public int getDefaultSupply(Environment environment, Biome biome) {
		// TODO Auto-generated method stub
		return 15;
	}

	@Override
	public int getMaxDeviation() {
		// TODO Auto-generated method stub
		return 2;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Graphite";
	}

	@Override
	public boolean isObtainableFromGEOMiner() {
		// TODO Auto-generated method stub
		return true;
	}
}
