package br.com.yiatzz.bosses.inventories.holders;

import br.com.yiatzz.bosses.cache.local.HeadsLocalCache;
import br.com.yiatzz.bosses.misc.utils.ColorUtil;
import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class IndexHolder implements InventoryHolder {

    public static final int INFO_SLOT = 11;
    public static final int RANK_SLOT = 13;
    public static final int LIST_SLOT = 15;

    private static final String INFO_HEAD_URLKEY = "d01afe973c5482fdc71e6aa10698833c79c437f21308ea9a1a095746ec274a0f";

    @Override
    public Inventory getInventory() {
        Inventory inventory = Bukkit.createInventory(this, 3 * 9, "Bosses");

        ItemStack infoIcon = new ItemStack(HeadsLocalCache.getUrlHead(INFO_HEAD_URLKEY));
        ItemMeta infoIconMeta = infoIcon.getItemMeta();
        infoIconMeta.setDisplayName(ColorUtil.colorIt("&eInformações"));
        infoIconMeta.setLore(Lists.newArrayList(
                ColorUtil.colorIt("&7Você ganhará diversos itens"),
                ColorUtil.colorIt("&7matando esses bosses com sua"),
                ColorUtil.colorIt("&7matadora de bosses."),
                "",
                ColorUtil.colorIt("&7Você também pode adicionar dano"),
                ColorUtil.colorIt("&7extras em sua lâmina para torná-la"),
                ColorUtil.colorIt("&7ainda mais forte."),
                "",
                ColorUtil.colorIt("&fEsperando o que para matá-los?")
        ));
        infoIcon.setItemMeta(infoIconMeta);

        inventory.setItem(
                INFO_SLOT,
                infoIcon
        );

        ItemStack rankIcon = new ItemStack(Material.GOLD_BLOCK);
        ItemMeta rankItemMeta = rankIcon.getItemMeta();
        rankItemMeta.setDisplayName(ColorUtil.colorIt("&eRanking de Kills"));
        rankItemMeta.setLore(Lists.newArrayList(
                ColorUtil.colorIt("&7Visualize o ranking de"),
                ColorUtil.colorIt("&7jogadores que mais mataram"),
                ColorUtil.colorIt("&7bosses no servidor.")
        ));
        rankIcon.setItemMeta(rankItemMeta);

        inventory.setItem(
                RANK_SLOT,
                rankIcon
        );

        ItemStack bossesIcon = new ItemStack(Material.MONSTER_EGG);
        ItemMeta bossIconMeta = bossesIcon.getItemMeta();
        bossIconMeta.setDisplayName(ColorUtil.colorIt("&eBosses"));
        bossIconMeta.setLore(Lists.newArrayList(
                ColorUtil.colorIt("&7Visualize a lista de bosses.")
        ));
        bossesIcon.setItemMeta(bossIconMeta);

        inventory.setItem(
                LIST_SLOT,
                bossesIcon
        );

        return inventory;
    }

    @Override
    public World getWorld() {
        return null;
    }
}
