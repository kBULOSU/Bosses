package br.com.yiatzz.bosses.misc.utils;

import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.function.Consumer;

public class NBTUtil {

    public static void modify(ItemStack itemStack, Consumer<NBTTagCompound> consumer) {
        net.minecraft.server.v1_8_R3.ItemStack nmsCopy = CraftItemStack.asNMSCopy(itemStack);
        NBTTagCompound compound = (nmsCopy.hasTag()) ? nmsCopy.getTag() : new NBTTagCompound();

        consumer.accept(compound);

        ItemMeta newMeta = CraftItemStack.asBukkitCopy(nmsCopy).getItemMeta();
        itemStack.setItemMeta(newMeta);
    }

    public static boolean hasNbtKey(ItemStack itemStack, String key) {
        net.minecraft.server.v1_8_R3.ItemStack nmsCopy = CraftItemStack.asNMSCopy(itemStack);
        NBTTagCompound compound = (nmsCopy.hasTag()) ? nmsCopy.getTag() : new NBTTagCompound();

        return compound.hasKey(key);
    }

    public static String nbtString(ItemStack itemStack, String key) {
        net.minecraft.server.v1_8_R3.ItemStack nmsCopy = CraftItemStack.asNMSCopy(itemStack);
        NBTTagCompound compound = (nmsCopy.hasTag()) ? nmsCopy.getTag() : new NBTTagCompound();

        String nbtString = compound.getString(key);
        if (nbtString.isEmpty()) {
            return null;
        }

        return nbtString;
    }

    public static int nbtInt(ItemStack itemStack, String key) {
        net.minecraft.server.v1_8_R3.ItemStack nmsCopy = CraftItemStack.asNMSCopy(itemStack);
        NBTTagCompound compound = (nmsCopy.hasTag()) ? nmsCopy.getTag() : new NBTTagCompound();

        return compound.getInt(key);
    }

}
