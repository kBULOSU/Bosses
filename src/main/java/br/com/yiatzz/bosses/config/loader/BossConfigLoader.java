package br.com.yiatzz.bosses.config.loader;

import br.com.yiatzz.bosses.cache.local.HeadsLocalCache;
import br.com.yiatzz.bosses.misc.utils.ColorUtil;
import br.com.yiatzz.bosses.object.Boss;
import br.com.yiatzz.bosses.object.BossReward;
import br.com.yiatzz.bosses.registry.BossRegistry;
import com.google.common.base.Enums;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class BossConfigLoader {

    public void load(BossRegistry registry, FileConfiguration config) {
        ConfigurationSection section = config.getConfigurationSection("Bosses");

        for (String key : section.getKeys(false)) {
            EntityType entityType = Enums.getIfPresent(EntityType.class, section.getString(key + ".entity-name")).orNull();
            if (entityType == null) {
                continue;
            }

            int life = section.getInt(key + ".life");

            String iconDisplayName = ColorUtil.colorIt(section.getString(key + ".icon.display-name"));

            List<String> lore = section.getStringList(key + ".icon.lore").stream()
                    .map(ColorUtil::colorIt)
                    .collect(Collectors.toList());

            List<BossReward> rewards = new LinkedList<>();

            ConfigurationSection rewardsSection = section.getConfigurationSection(key + ".rewards");

            for (String rewardsSectionKey : rewardsSection.getKeys(false)) {
                Material rewardMaterial = Material.matchMaterial(rewardsSection.getString(rewardsSectionKey + ".material"));
                if (rewardMaterial == null) {
                    continue;
                }

                int rewardMaterialData = rewardsSection.getInt(rewardsSectionKey + ".data");
                int rewardAmount = rewardsSection.getInt(rewardsSectionKey + ".amount");
                String rewardDisplayName = ColorUtil.colorIt(rewardsSection.getString(rewardsSectionKey + ".display-name"));

                List<String> rewardLore = rewardsSection.getStringList(rewardsSectionKey + ".lore")
                        .stream()
                        .map(ColorUtil::colorIt)
                        .collect(Collectors.toList());

                String rewardCommand = rewardsSection.getString(rewardsSectionKey + ".command");

                ItemStack rewardIcon = new ItemStack(rewardMaterial, rewardAmount, (short) rewardMaterialData);

                ItemMeta rewardIconItemMeta = rewardIcon.getItemMeta();
                rewardIconItemMeta.setDisplayName(rewardDisplayName);
                rewardIconItemMeta.setLore(rewardLore);
                rewardIcon.setItemMeta(rewardIconItemMeta);

                rewards.add(
                        new BossReward(
                                rewardIcon,
                                rewardCommand
                        )
                );
            }

            ItemStack icon;

            String urlKey = section.getString(key + ".icon.head-url");
            if (urlKey.isEmpty()) {
                Material material = Material.matchMaterial(section.getString(key + ".icon.material"));
                if (material == null) {
                    continue;
                }

                int materialData = section.getInt(key + ".icon.data");

                icon = new ItemStack(material, 1, (short) materialData);
            } else {
                icon = new ItemStack(HeadsLocalCache.getUrlHead(urlKey));
            }

            ItemMeta iconMeta = icon.getItemMeta();

            iconMeta.setDisplayName(iconDisplayName);
            iconMeta.setLore(lore);
            icon.setItemMeta(iconMeta);

            registry.register(
                    new Boss(
                            key,
                            iconDisplayName,
                            entityType,
                            life,
                            icon,
                            rewards
                    )
            );
        }
    }
}
