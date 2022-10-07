package br.com.yiatzz.bosses.inventories.holders;

import br.com.yiatzz.bosses.BossConstants;
import br.com.yiatzz.bosses.object.Boss;
import br.com.yiatzz.bosses.misc.utils.ColorUtil;
import br.com.yiatzz.bosses.misc.utils.NBTUtil;
import br.com.yiatzz.bosses.registry.BossRegistry;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class BossesListHolder implements InventoryHolder {

    private final BossRegistry bossRegistry;

    @Override
    public Inventory getInventory() {
        Inventory inventory = Bukkit.createInventory(this, 4 * 9, "Bosses");

        int slot = 10;
        for (Boss boss : bossRegistry.values()) {
            if ((slot + 1) % 9 == 0) {
                slot += 2;
            }

            ItemStack icon = boss.getIcon().clone();
            ItemMeta itemMeta = icon.getItemMeta();

            List<String> newLore = itemMeta.hasLore() ? itemMeta.getLore() : new ArrayList<>();
            newLore.addAll(Lists.newArrayList(
                    "",
                    ColorUtil.colorIt("&eClique para visualizar"),
                    ColorUtil.colorIt("&eas recompensas.")
            ));

            itemMeta.setLore(newLore);
            icon.setItemMeta(itemMeta);

            NBTUtil.modify(icon, compound -> compound.setString(Boss.METADATA, boss.getIdentifier()));

            inventory.setItem(
                    slot++,
                    icon
            );
        }

        inventory.setItem(
                31,
                BossConstants.BACK_ARROW
        );

        return inventory;
    }

    @Override
    public World getWorld() {
        return null;
    }
}
