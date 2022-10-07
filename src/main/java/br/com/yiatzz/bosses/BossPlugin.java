package br.com.yiatzz.bosses;

import br.com.yiatzz.bosses.amplifier.AmplifierCommand;
import br.com.yiatzz.bosses.amplifier.AmplifierListener;
import br.com.yiatzz.bosses.commands.BladeCommand;
import br.com.yiatzz.bosses.commands.BossCommand;
import br.com.yiatzz.bosses.config.loader.BossConfigLoader;
import br.com.yiatzz.bosses.database.MysqlDatabase;
import br.com.yiatzz.bosses.listeners.BossInventoryListener;
import br.com.yiatzz.bosses.listeners.BossListeners;
import br.com.yiatzz.bosses.registry.BossRegistry;
import br.com.yiatzz.bosses.repository.BossRankingRepository;
import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.net.InetSocketAddress;

public class BossPlugin extends JavaPlugin {

    @Getter
    private static BossPlugin instance;

    @Getter
    private BossRegistry bossRegistry;

    @Getter
    private MysqlDatabase mysqlDatabase;

    @Getter
    private BossRankingRepository rankingRepository;

    @Override
    public void onEnable() {
        super.onEnable();

        instance = this;

        saveDefaultConfig();

        ConfigurationSection databaseSection = getConfig().getConfigurationSection("database");

        this.mysqlDatabase = new MysqlDatabase(
                new InetSocketAddress(
                        databaseSection.getString("host"),
                        3306
                ),
                databaseSection.getString("user"),
                databaseSection.getString("pass"),
                databaseSection.getString("name")
        );

        this.mysqlDatabase.openConnection();

        this.rankingRepository = new BossRankingRepository(mysqlDatabase);
        this.rankingRepository.createTable();

        this.bossRegistry = new BossRegistry();

        new BossConfigLoader().load(this.bossRegistry, getConfig());

        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new BossListeners(this.bossRegistry, this.rankingRepository), this);
        pluginManager.registerEvents(new BossInventoryListener(this.bossRegistry), this);
        pluginManager.registerEvents(new AmplifierListener(), this);

        getCommand("blade").setExecutor(new BladeCommand());
        getCommand("boss").setExecutor(new BossCommand(this.bossRegistry));
        getCommand("amplifier").setExecutor(new AmplifierCommand());
    }

    @Override
    public void onDisable() {
        super.onDisable();

        this.mysqlDatabase.closeConnection();
    }
}
