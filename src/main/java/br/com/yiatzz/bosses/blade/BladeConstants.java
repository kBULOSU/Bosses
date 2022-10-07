package br.com.yiatzz.bosses.blade;

import br.com.yiatzz.bosses.BossPlugin;
import br.com.yiatzz.bosses.misc.utils.ColorUtil;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

public class BladeConstants {

    public static final String NBT_TAG = "bladeDamage";
    public static final String DISPLAY_NAME;
    public static final Material MATERIAL;

    static {

        FileConfiguration config = BossPlugin.getInstance().getConfig();

        ConfigurationSection section = config.getConfigurationSection("Blade");

        DISPLAY_NAME = ColorUtil.colorIt(section.getString("display-name"));
        MATERIAL = Material.matchMaterial(section.getString("material"));
    }
}
