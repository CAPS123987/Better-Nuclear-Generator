package me.CAPS123987.Item;

import java.util.LinkedHashMap;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.core.attributes.MachineTier;
import io.github.thebusybiscuit.slimefun4.core.attributes.MachineType;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun4.utils.LoreBuilder;
import me.CAPS123987.BetterReactor.BetterReactor;

public class Items {
	
	
	
	public static final ItemGroup betterReactor = new ItemGroup(new NamespacedKey(BetterReactor.getInstance(),
	        "Better_Reactor"),
	        new CustomItemStack(Material.OBSIDIAN, "&dBetter Reactor")
	    );
	
	public static final SlimefunItemStack REACTOR_CORE = new SlimefunItemStack("REACTOR_CORE",
			Material.MAGENTA_GLAZED_TERRACOTTA,
	        "REACTOR_CORE",
	        "",
	        LoreBuilder.machine(MachineTier.END_GAME, MachineType.GENERATOR),
	        LoreBuilder.powerBuffer(4096),
	        LoreBuilder.powerPerSecond(1024)
	    );
	public static final SlimefunItemStack HEATED_COOLANT = new SlimefunItemStack("HEATED_COOLANT",
			"de4073be40cb3deb310a0be959b4cac68e825372728fafb6c2973e4e7c33",
	        "HEATED_COOLANT",
	        ""
	    );
	
	public static final SlimefunItemStack LEAD_BLOCK = new SlimefunItemStack("LEAD_BLOCK",
			Material.IRON_BLOCK,
	        "LEAD_BLOCK",
	        "",
	        "&fWhite &7in Reactor Core Hologram"
	    );
	
	public static final SlimefunItemStack LEAD_GLASS = new SlimefunItemStack("LEAD_GLASS",
			Material.GRAY_STAINED_GLASS,
	        "LEAD_GLASS",
	        "",
	        "&7Gray in Reactor Core Hologram"
	    );
	
	public static final SlimefunItemStack GRAFIT_ROD = new SlimefunItemStack("GRAFIT_ROD",
			Material.ANCIENT_DEBRIS,
	        "GRAFIT_ROD",
	        "",
	        "&6Orange &7in Reactor Core Hologram"
	    );
	
	public static final SlimefunItemStack GRAFIT = new SlimefunItemStack("GRAFIT",
			Material.GUNPOWDER,
	        "GRAFIT",
	        ""
	    );
	public static final SlimefunItemStack REACTOR_INPUT = new SlimefunItemStack("REACTOR_INPUT",
			Material.LIGHT_BLUE_WOOL,
	        "REACTOR_INPUT",
	        "",
	        "&9Blue &7in Reactor Core Hologram"
	    );
	public static final SlimefunItemStack REACTOR_OUTPUT = new SlimefunItemStack("REACTOR_OUTPUT",
			Material.RED_WOOL,
	        "REACTOR_OUTPUT",
	        "",
	        "&cRed &7in Reactor Core Hologram"
	    );
	//https://minecraft-heads.com/custom-heads/miscellaneous/50973-fancy-cube
	public static final SlimefunItemStack REACTOR_HATCH = new SlimefunItemStack("REACTOR_HATCH",
			"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWU4MzI4NDE2MDdkZjc2ZWM1NjE5MGVhODdjMzE2MDUwYTI3N2E1YTU4ZjFkM2JjODJhMGU5NGVkYjk1MzUzIn19fQ==",
	        "REACTOR_HATCH",
	        "",
	        "&aGreen &7in Reactor Core Hologram"
	    );
	
	
	public static final ItemStack[] recipe_LEAD_GLASS= {
			Items.LEAD_BLOCK,Items.LEAD_BLOCK,Items.LEAD_BLOCK,
			Items.LEAD_BLOCK,new ItemStack(Material.GLASS),Items.LEAD_BLOCK,
			Items.LEAD_BLOCK,Items.LEAD_BLOCK,Items.LEAD_BLOCK
	};
	
	public static final ItemStack[] recipe_GRAFIT_ROD= {
			Items.GRAFIT,Items.GRAFIT,Items.GRAFIT,
			Items.GRAFIT,new ItemStack(Material.IRON_BLOCK),Items.GRAFIT,
			Items.GRAFIT,Items.GRAFIT,Items.GRAFIT
	};
	
	public static final ItemStack[] recipe_LEAD_BLOCK= {
			SlimefunItems.LEAD_INGOT,SlimefunItems.LEAD_INGOT,SlimefunItems.LEAD_INGOT,
			SlimefunItems.LEAD_INGOT,SlimefunItems.LEAD_INGOT,SlimefunItems.LEAD_INGOT,
			SlimefunItems.LEAD_INGOT,SlimefunItems.LEAD_INGOT,SlimefunItems.LEAD_INGOT
	};
	
	public static final ItemStack[] recipe_TEST_ITEM= {
			null,null,null,
			null,new ItemStack(Material.PINK_WOOL),null,
			null,null,null
	};
}
