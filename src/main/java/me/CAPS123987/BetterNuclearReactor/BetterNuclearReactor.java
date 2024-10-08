package me.CAPS123987.BetterNuclearReactor;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import io.github.thebusybiscuit.slimefun4.libraries.dough.updater.BlobBuildUpdater;
import me.CAPS123987.Item.Graphite;
import me.CAPS123987.Machines.HeatSensor;
import me.CAPS123987.Machines.ReactorStop;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.Vector;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.libraries.dough.config.Config;
import me.CAPS123987.Cargo.ReactorInput;
import me.CAPS123987.Cargo.ReactorOutput;
import me.CAPS123987.IIIDmultiblock.ReactorCore;
import me.CAPS123987.Item.Borium;
import me.CAPS123987.Utils.Methodes;
import me.CAPS123987.Machines.SuperFreezer;
import me.CAPS123987.Item.Items;

public class BetterNuclearReactor extends JavaPlugin implements SlimefunAddon {
	public static BetterNuclearReactor instance;
	private static final Map<Vector, SlimefunItemStack> reactor = new LinkedHashMap<>();
	
    @Override
    public void onEnable() {
        // Read something from your config.yml
        Config cfg = new Config(this);

        if(Objects.requireNonNull(Bukkit.getScoreboardManager()).getMainScoreboard().getTeam("badBlock")==null){
            Team badBlockTeam = Bukkit.getScoreboardManager().getMainScoreboard().registerNewTeam("badBlock");
            badBlockTeam.setColor(ChatColor.RED);
        }

        if (cfg.getBoolean("options.auto-update") && getDescription().getVersion().startsWith("DEV - ")) {
            BlobBuildUpdater
        	updater = new BlobBuildUpdater(this, this.getFile(), "CAPS123987/Better-Nuclear-Generator/master");
        	updater.start();
        }
        
        instance = this;
        new SlimefunItem(Items.betterReactor, Items.LEAD_GLASS, RecipeType.ENHANCED_CRAFTING_TABLE , Items.recipe_LEAD_GLASS,new SlimefunItemStack(Items.LEAD_GLASS, 8)).register(this);
        new SlimefunItem(Items.betterReactor, Items.LEAD_BLOCK, RecipeType.ENHANCED_CRAFTING_TABLE , Items.recipe_LEAD_BLOCK).register(this);
        new SlimefunItem(Items.betterReactor, Items.BORIUM_ROD, RecipeType.ENHANCED_CRAFTING_TABLE , Items.recipe_BORIUM_ROD).register(this);
        new SlimefunItem(Items.betterReactor, Items.REACTOR_HATCH, RecipeType.ENHANCED_CRAFTING_TABLE, Items.recipe_REACTOR_HATCH).register(this);
        new SlimefunItem(Items.betterReactor, Items.HEATED_COOLANT ,RecipeType.NULL, new ItemStack[0]).register(this);
        new Borium().register(this);
        new Graphite().register(this);
        new ReactorInput().register(this);
        new ReactorOutput().register(this);
        new HeatSensor().register(this);
        new ReactorStop().register(this);
        
        //bottom
        Methodes.areaList(-2, -1, 0, 2, -1, 4, Items.LEAD_BLOCK, reactor);
        //top
        Methodes.areaList(-2, 5, 0, 2, 5, 4, Items.LEAD_BLOCK, reactor);
        //sides
        Methodes.areaList(-1, 0, 4, 1, 4, 4, Items.LEAD_GLASS, reactor);
        Methodes.areaList(-2, 0, 1, -2, 4, 3, Items.LEAD_GLASS, reactor);
        Methodes.areaList(2, 0, 1, 2, 4, 3, Items.LEAD_GLASS, reactor);
        Methodes.areaList(-1, 1, 0,1, 4, 0, Items.LEAD_GLASS, reactor);
        //pillar
        Methodes.areaList(-2, 0, 0, -2, 4, 0, Items.LEAD_BLOCK, reactor);
        Methodes.areaList(2, 0, 0, 2, 4, 0, Items.LEAD_BLOCK, reactor);
        Methodes.areaList(-2, 0, 4, -2, 4, 4, Items.LEAD_BLOCK, reactor);
        Methodes.areaList(2, 0, 4, 2, 4, 4, Items.LEAD_BLOCK, reactor);
        //rods
        Methodes.areaList(-1, 0, 1, -1, 3, 1, Items.BORIUM_ROD, reactor);
        Methodes.areaList(1, 0, 1, 1, 3, 1, Items.BORIUM_ROD, reactor);
        Methodes.areaList(1, 0, 3, 1, 3, 3, Items.BORIUM_ROD, reactor);
        Methodes.areaList(-1, 0, 3, -1, 3, 3, Items.BORIUM_ROD, reactor);
        Methodes.areaList(0, 0, 2, 0, 3, 2, Items.BORIUM_ROD, reactor);
        //hatches
        reactor.put(new Vector(-1,4,1), Items.REACTOR_HATCH);
        reactor.put(new Vector(1,4,1), Items.REACTOR_HATCH);
        reactor.put(new Vector(0,4,2), Items.REACTOR_HATCH);
        reactor.put(new Vector(-1,4,3), Items.REACTOR_HATCH);
        reactor.put(new Vector(1,4,3), Items.REACTOR_HATCH);
        //output
        reactor.put(new Vector(-1,0,0), Items.REACTOR_INPUT);
        reactor.put(new Vector(1,0,0), Items.REACTOR_OUTPUT);
        
        new ReactorCore(reactor).register(this);
        new SuperFreezer().register(this);
        
        
    }

    @Override
    public void onDisable() {
        // Logic for disabling the plugin...
    }

    @Override
    public String getBugTrackerURL() {
        // You can return a link to your Bug Tracker instead of null here
        return null;
    }

    @Override
    public JavaPlugin getJavaPlugin() {
        /*
         * You will need to return a reference to your Plugin here.
         * If you are using your main class for this, simply return "this".
         */
        return this;
    }

	public static BetterNuclearReactor getInstance() {
        return instance;
    }

}
