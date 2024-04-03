package me.CAPS123987.Item;

import org.bukkit.NamespacedKey;
import org.bukkit.World.Environment;
import org.bukkit.block.Biome;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.geo.GEOResource;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.implementation.items.blocks.UnplaceableBlock;
import me.CAPS123987.BetterNuclearReactor.BetterNuclearReactor;

public class Grafit extends UnplaceableBlock implements GEOResource{
	public Grafit() {
        super(Items.betterReactor, Items.BORIUM, RecipeType.GEO_MINER, new ItemStack[0]);
        register();
    }
	private final NamespacedKey key = new NamespacedKey(BetterNuclearReactor.getInstance(), "Borium");
	@Override
	public NamespacedKey getKey() {
		// TODO Auto-generated method stub
		return key;
	}

	@Override
	public int getDefaultSupply(Environment environment, Biome biome) {
		// TODO Auto-generated method stub
		return 18;
	}

	@Override
	public int getMaxDeviation() {
		// TODO Auto-generated method stub
		return 3;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Borium";
	}

	@Override
	public boolean isObtainableFromGEOMiner() {
		// TODO Auto-generated method stub
		return true;
	}
}
