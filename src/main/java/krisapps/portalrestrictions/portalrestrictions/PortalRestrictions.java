package krisapps.portalrestrictions.portalrestrictions;

import krisapps.portalrestrictions.portalrestrictions.commands.RemoveRestriction;
import krisapps.portalrestrictions.portalrestrictions.commands.SetRestriction;
import krisapps.portalrestrictions.portalrestrictions.commands.ViewRestriction;
import krisapps.portalrestrictions.portalrestrictions.datefollowing.RestrictionValidator;
import krisapps.portalrestrictions.portalrestrictions.datefollowing.ValidateRestrictions;
import krisapps.portalrestrictions.portalrestrictions.events.OnPortalCreateAttemptHandler;
import krisapps.portalrestrictions.portalrestrictions.events.OnPortalEnterAttemptHandler;
import krisapps.portalrestrictions.portalrestrictions.tabcompletion.RemoveRestrictionTab;
import krisapps.portalrestrictions.portalrestrictions.tabcompletion.SetRestrictionTab;
import krisapps.portalrestrictions.portalrestrictions.tabcompletion.ViewRestrictionTab;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public final class PortalRestrictions extends JavaPlugin {

    public final File configFile = new File(getDataFolder(), "config.yml");
    public final File dataFile = new File(getDataFolder(), "restriction_data.yml");

    public FileConfiguration config;
    public FileConfiguration restrictiondata;

    @Override
    public void onEnable() {
        loadfiles();
        register();
    }

    //Register all commands, tab completion and event listeners.
    private void register() {

        //Commands

        getCommand("setrestriction").setExecutor(new SetRestriction(this));
        getCommand("viewrestriction").setExecutor(new ViewRestriction(this));
        getCommand("removerestriction").setExecutor(new RemoveRestriction(this));
        getCommand("validaterestrictions").setExecutor(new ValidateRestrictions(this));


        //Tab Completion

        getCommand("setrestriction").setTabCompleter(new SetRestrictionTab());
        getCommand("viewrestriction").setTabCompleter(new ViewRestrictionTab());
        getCommand("removerestriction").setTabCompleter(new RemoveRestrictionTab());

        //Event Handlers

        getServer().getPluginManager().registerEvents(new OnPortalEnterAttemptHandler(this), this);
        getServer().getPluginManager().registerEvents(new OnPortalCreateAttemptHandler(this), this);

        getServer().getScheduler().scheduleAsyncRepeatingTask(this, new RestrictionValidator(this), 40, 20 * 30);

    }

    //Load required files. (Config and data file)
    private void loadfiles() {
        getLogger().info("Loading files [...]");

        if (!dataFile.getParentFile().exists() || !dataFile.exists()){
            getLogger().info("Creating new data file...");
            try {
                dataFile.createNewFile();
            } catch (IOException e) {
                getLogger().severe("Could not create plugin data file!");
                e.printStackTrace();
            }
        }

        if (!configFile.getParentFile().exists() || !configFile.exists()){
            getLogger().info("Creating new config file...");
            saveResource("config.yml", true);
        }

        config = new YamlConfiguration();
        restrictiondata = new YamlConfiguration();

        try {
            config.load(configFile);
        }catch (IOException | InvalidConfigurationException e){
            getLogger().warning("An error occurred while loading the config file.\n");
            e.printStackTrace();
        }

        try {
            restrictiondata.load(dataFile);
        }catch (IOException | InvalidConfigurationException e){
            getLogger().severe("An error occurred while loading the data file.\n");
            e.printStackTrace();
        }


        getLogger().info("Finished loading files [.]");

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
