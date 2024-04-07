package me.CAPS123987.Item;

import java.text.DecimalFormat;


import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.core.attributes.MachineTier;
import io.github.thebusybiscuit.slimefun4.core.attributes.MachineType;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun4.utils.LoreBuilder;
import me.CAPS123987.BetterNuclearReactor.BetterNuclearReactor;
import me.CAPS123987.IIIDmultiblock.ReactorCore;
import net.md_5.bungee.api.ChatColor;
public class Items {
	
	
	public static FileConfiguration cfg = BetterNuclearReactor.instance.getConfig();
	public static int uranMax = cfg.getInt("uranMax");
	public static DecimalFormat df = new DecimalFormat("0.00");
	
	public static final ItemGroup betterReactor = new ItemGroup(new NamespacedKey(BetterNuclearReactor.getInstance(),
	        "Better_Reactor"),
	        new CustomItemStack(Material.OBSIDIAN, "&dBetter Reactor")
	    );
	
	public static final SlimefunItemStack REACTOR_CORE = new SlimefunItemStack("REACTOR_CORE",
			Material.MAGENTA_GLAZED_TERRACOTTA,
	        ChatColor.RESET+"Reactor Core",
	        "",
	        LoreBuilder.machine(MachineTier.END_GAME, MachineType.GENERATOR),
	        LoreBuilder.powerBuffer(uranMax*ReactorCore.powerPer*2),
	        LoreBuilder.powerPerSecond(ReactorCore.powerPer)+" per 1 uran (up to "+uranMax*ReactorCore.powerPer+" J/s)",
	        "&8\u21E8 &e\u26A1 &7"+"Lasts "+ReactorCore.burnTime+"t",
	        "&8\u21E8 &e\u26A1 &7"+"Per 1 uran in total: "+df.format(ReactorCore.powerPer*ReactorCore.burnTime/1000000f) +"M J",
	        "&7Core of Reactor, &bshows Reactor Core Hologram",
			"&7Every block required to build, has the &bColor in description"
	    );
	public static final SlimefunItemStack HEAT_SENSOR = new SlimefunItemStack("HEAT_SENSOR",
			Material.POLISHED_DEEPSLATE,
			ChatColor.RESET+"Heat Sensor",
	        "",
	        "&7Detects heat in the reactor",
	        "&7Outputs redstone signal when tepmerature is too high",
			"&7You can replace lead glass with this sensor"
	);
	public static final SlimefunItemStack REACTOR_STOP = new SlimefunItemStack("REACTOR_STOP",
			Material.REDSTONE_LAMP,
			ChatColor.RESET+"Reactor Stop",
			"",
			"&7When powered, stops the reactor",
			"&eConsumes 128 Graphite for one stop"
	);
	public static final SlimefunItemStack HEATED_COOLANT = new SlimefunItemStack("HEATED_COOLANT",
			"de4073be40cb3deb310a0be959b4cac68e825372728fafb6c2973e4e7c33",
	        ChatColor.RESET+"Heated Coolant",
	        ""
	    );
	
	
	public static final SlimefunItemStack SUPER_FREEZER = new SlimefunItemStack("SUPER_FREEZER",
			Material.QUARTZ_BLOCK,
	        ChatColor.RESET+"Super Freezer",
	        "",
	        "&7(&cWarning!&7 more inputs needed for good flow of items)",
	        "&7Water instantly to coolant",
	        LoreBuilder.powerPerSecond(50)
	    );
	
	public static final SlimefunItemStack LEAD_BLOCK = new SlimefunItemStack("LEAD_BLOCK",
			Material.IRON_BLOCK,
	        ChatColor.RESET+"Lead Block",
	        "",
	        "&fWhite &7in &bReactor Core Hologram"
	    );
	
	public static final SlimefunItemStack LEAD_GLASS = new SlimefunItemStack("LEAD_GLASS",
			Material.GRAY_STAINED_GLASS,
			ChatColor.RESET+"Lead Glass",
	        "",
	        "&7Gray in &bReactor Core Hologram"
	    );
	
	public static final SlimefunItemStack BORIUM_ROD = new SlimefunItemStack("BORIUM_ROD",
			Material.ANCIENT_DEBRIS,
			ChatColor.RESET+"Borium Rod",
	        "",
	        "&6Orange &7in &bReactor Core Hologram"
	    );
	
	public static final SlimefunItemStack BORIUM = new SlimefunItemStack("BORIUM",
			Material.GUNPOWDER,
			ChatColor.RESET+"Borium",
	        ""
	    );
	public static final SlimefunItemStack GRAPHITE = new SlimefunItemStack("GRAPHITE",
			Material.GUNPOWDER,
			ChatColor.RESET+"Graphite",
			""
	);
	public static final SlimefunItemStack REACTOR_INPUT = new SlimefunItemStack("REACTOR_INPUT",
			Material.LIGHT_BLUE_WOOL,
	        ChatColor.RESET+"Reactor Input",
	        "",
	        "&9Blue &7in &bReactor Core Hologram"
	    );
	public static final SlimefunItemStack REACTOR_OUTPUT = new SlimefunItemStack("REACTOR_OUTPUT",
			Material.RED_WOOL,
			ChatColor.RESET+"Reactor Output",
	        "",
	        "&cRed &7in &bReactor Core Hologram"
	    );
	//https://minecraft-heads.com/custom-heads/miscellaneous/50973-fancy-cube
	public static final SlimefunItemStack REACTOR_HATCH = new SlimefunItemStack("REACTOR_HATCH",
			"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWU4MzI4NDE2MDdkZjc2ZWM1NjE5MGVhODdjMzE2MDUwYTI3N2E1YTU4ZjFkM2JjODJhMGU5NGVkYjk1MzUzIn19fQ==",
			ChatColor.RESET+"Reactor Hatch",
	        "",
	        "&aGreen &7in &bReactor Core Hologram"
	    );
	
	
	public static final ItemStack[] recipe_LEAD_GLASS= {
			Items.LEAD_BLOCK,Items.LEAD_BLOCK,Items.LEAD_BLOCK,
			Items.LEAD_BLOCK,new ItemStack(Material.GLASS),Items.LEAD_BLOCK,
			Items.LEAD_BLOCK,Items.LEAD_BLOCK,Items.LEAD_BLOCK
	};
	public static final ItemStack[] recipe_SUPER_FREEZER= {
			SlimefunItems.FREEZER_2,SlimefunItems.ELECTRO_MAGNET,SlimefunItems.CARGO_MOTOR,
			SlimefunItems.REACTOR_COOLANT_CELL,new ItemStack(Material.ICE),SlimefunItems.REACTOR_COOLANT_CELL,
			SlimefunItems.ELECTRIC_MOTOR,SlimefunItems.ELECTRO_MAGNET,SlimefunItems.FREEZER_2
	};
	
	public static final ItemStack[] recipe_BORIUM_ROD= {
			Items.BORIUM,Items.BORIUM,Items.BORIUM,
			Items.BORIUM,new ItemStack(Material.IRON_BLOCK),Items.BORIUM,
			Items.BORIUM,Items.BORIUM,Items.BORIUM
	};
	
	public static final ItemStack[] recipe_LEAD_BLOCK= {
			SlimefunItems.LEAD_INGOT,SlimefunItems.LEAD_INGOT,SlimefunItems.LEAD_INGOT,
			SlimefunItems.LEAD_INGOT,SlimefunItems.LEAD_INGOT,SlimefunItems.LEAD_INGOT,
			SlimefunItems.LEAD_INGOT,SlimefunItems.LEAD_INGOT,SlimefunItems.LEAD_INGOT
	};
	public static final ItemStack[] recipe_REACTOR_HATCH= {
			SlimefunItems.LEAD_INGOT,SlimefunItems.ELECTRIC_MOTOR,SlimefunItems.LEAD_INGOT,
			SlimefunItems.LEAD_INGOT,SlimefunItems.NUCLEAR_REACTOR,SlimefunItems.LEAD_INGOT,
			SlimefunItems.LEAD_INGOT,SlimefunItems.ELECTRO_MAGNET,SlimefunItems.LEAD_INGOT
	};
	public static final ItemStack[] recipe_REACTOR_CORE= {
			Items.LEAD_BLOCK,SlimefunItems.GPS_CONTROL_PANEL,Items.LEAD_BLOCK,
			new ItemStack(Material.CHEST),SlimefunItems.REACTOR_ACCESS_PORT,new ItemStack(Material.CHEST),
			Items.LEAD_BLOCK,SlimefunItems.ELECTRO_MAGNET,Items.LEAD_BLOCK
	};
	public static final ItemStack[] recipe_REACTOR_INPUT= {
			Items.LEAD_BLOCK,Items.LEAD_BLOCK,Items.LEAD_BLOCK,
			new ItemStack(Material.CHEST),SlimefunItems.CARGO_INPUT_NODE,new ItemStack(Material.CHEST),
			Items.LEAD_BLOCK,SlimefunItems.ELECTRO_MAGNET,Items.LEAD_BLOCK
	};
	public static final ItemStack[] recipe_REACTOR_OUTPUT= {
			Items.LEAD_BLOCK,Items.LEAD_BLOCK,Items.LEAD_BLOCK,
			new ItemStack(Material.CHEST),SlimefunItems.CARGO_OUTPUT_NODE_2,new ItemStack(Material.CHEST),
			Items.LEAD_BLOCK,SlimefunItems.ELECTRO_MAGNET,Items.LEAD_BLOCK
	};
	
	public static final ItemStack[] recipe_TEST_ITEM= {
			null,null,null,
			null,new ItemStack(Material.PINK_WOOL),null,
			null,null,null
	};
}
