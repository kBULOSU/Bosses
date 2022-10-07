package br.com.yiatzz.bosses.amplifier;

import br.com.yiatzz.bosses.BossPlugin;
import br.com.yiatzz.bosses.misc.utils.ColorUtil;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;
import java.util.stream.Collectors;

public class AmplifierConstants {

    public static final String NBT_TAG = "amplifier";

    public static final String DISPLAY_NAME;
    public static final List<String> LORE;

    public static final Material MATERIAL;

    static {
        FileConfiguration config = BossPlugin.getInstance().getConfig();

        ConfigurationSection section = config.getConfigurationSection("Amplifier");

        DISPLAY_NAME = ColorUtil.colorIt(section.getString("display-name"));

        LORE = section.getStringList("lore")
                .stream()
                .map(ColorUtil::colorIt)
                .collect(Collectors.toList());

        MATERIAL = Material.matchMaterial(section.getString("material"));
    }

}
