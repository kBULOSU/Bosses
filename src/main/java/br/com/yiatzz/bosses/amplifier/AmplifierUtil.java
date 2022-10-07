package br.com.yiatzz.bosses.amplifier;

import br.com.yiatzz.bosses.misc.utils.NBTUtil;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.stream.Collectors;

public class AmplifierUtil {

    public static ItemStack asItemStack(int amplifier, int amount) {
        ItemStack itemStack = new ItemStack(Material.REDSTONE, amount);
        ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.setDisplayName(AmplifierConstants.DISPLAY_NAME);
        itemMeta.setLore(
                AmplifierConstants.LORE
                        .stream()
                        .map(line -> line.replace("<amplifier>", String.valueOf(amplifier)))
                        .collect(Collectors.toList())
        );

        itemStack.setItemMeta(itemMeta);

        NBTUtil.modify(itemStack, compound -> compound.setInt(AmplifierConstants.NBT_TAG, amplifier));

        return itemStack;
    }

}
