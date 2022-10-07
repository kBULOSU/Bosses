package br.com.yiatzz.bosses.blade;

import br.com.yiatzz.bosses.misc.utils.ColorUtil;
import br.com.yiatzz.bosses.misc.utils.NBTUtil;
import com.google.common.collect.Lists;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class BladeUtils {

    public static ItemStack asItemStack(int damage) {
        ItemStack itemStack = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.setDisplayName(BladeConstants.DISPLAY_NAME);

        itemMeta.setLore(
                Lists.newArrayList(
                        ColorUtil.colorIt("&7Dano: &f" + (damage == Integer.MAX_VALUE ? "Hit Kill" : damage))
                )
        );

        itemStack.setItemMeta(itemMeta);

        NBTUtil.modify(itemStack, compound -> compound.setInt(BladeConstants.NBT_TAG, damage));

        return itemStack;
    }

    public static int identifyDamage(ItemStack itemStack) {
        return NBTUtil.nbtInt(itemStack, BladeConstants.NBT_TAG);
    }
}
