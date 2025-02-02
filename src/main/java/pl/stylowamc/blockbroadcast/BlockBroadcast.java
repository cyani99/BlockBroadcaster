package pl.stylowamc.blockbroadcast;

import org.bukkit.plugin.java.JavaPlugin;
import pl.stylowamc.blockbroadcast.commands.SetBroadcastBlock;
import pl.stylowamc.blockbroadcast.events.BroadcastBlockClick;

public final class BlockBroadcast extends JavaPlugin {


    @Override
    public void onEnable() {
        this.getConfig().options().copyDefaults(true); // Kopiowanie domyślnych ustawień configu
        saveDefaultConfig();

        this.getCommand("setbroadcastblock").setExecutor(new SetBroadcastBlock(this.getConfig()));
        getServer().getPluginManager().registerEvents(new BroadcastBlockClick(this.getConfig()), this);

        getLogger().info("BlockBroadcast enabled!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
