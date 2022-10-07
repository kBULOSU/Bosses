package br.com.yiatzz.bosses;

import br.com.yiatzz.bosses.inventories.holders.KillsRankingHolder;
import br.com.yiatzz.bosses.misc.utils.ColorUtil;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class BossConstants {

    public static final ItemStack BACK_ARROW = new ItemStack(Material.ARROW);

    public static final String ACTION_BAR_MESSAGE;

    public static final KillsRankingHolder KILLS_RANKING_HOLDER = new KillsRankingHolder();

    static {
        ItemMeta itemMeta = BACK_ARROW.getItemMeta();
        itemMeta.setDisplayName(ColorUtil.colorIt("&aVoltar"));
        BACK_ARROW.setItemMeta(itemMeta);

        FileConfiguration config = BossPlugin.getInstance().getConfig();

        ACTION_BAR_MESSAGE = ColorUtil.colorIt(config.getString("action-bar"));

    }

}
