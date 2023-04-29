package me.CAPS123987.Cargo;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.implementation.items.SimpleSlimefunItem;
import me.CAPS123987.Item.Items;
import me.CAPS123987.Utils.ETInventoryBlock;

public class ReactorOutput extends SlimefunItem implements ETInventoryBlock{
	public ReactorOutput() {
		super(Items.betterReactor, Items.REACTOR_OUTPUT,RecipeType.ENHANCED_CRAFTING_TABLE , Items.recipe_TEST_ITEM);
	}

	@Override
	public int[] getInputSlots() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int[] getOutputSlots() {
		// TODO Auto-generated method stub
		return null;
	}

}
