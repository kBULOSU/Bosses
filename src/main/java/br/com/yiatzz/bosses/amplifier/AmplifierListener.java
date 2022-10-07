package br.com.yiatzz.bosses.amplifier;

import br.com.yiatzz.bosses.blade.BladeConstants;
import br.com.yiatzz.bosses.blade.BladeUtils;
import br.com.yiatzz.bosses.misc.utils.ColorUtil;
import br.com.yiatzz.bosses.misc.utils.NBTUtil;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class AmplifierListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void on(InventoryClickEvent event) {
        if (event.getAction() != InventoryAction.SWAP_WITH_CURSOR) {
            return;
        }

        Player player = (Player) event.getWhoClicked();

        if (player.getGameMode() != GameMode.SURVIVAL) {
            player.sendMessage(ColorUtil.colorIt("&cVocê só pode utilizar livros estando no modo de sobrevivência."));
            return;
        }

        ItemStack amplifierItem = event.getCursor();
        if (amplifierItem == null || amplifierItem.getType() != Material.REDSTONE) {
            return;
        }

        int amplifier = NBTUtil.nbtInt(amplifierItem, AmplifierConstants.NBT_TAG);
        if (amplifier == 0) {
            return;
        }

        ItemStack bladeItem = event.getCurrentItem();
        if (bladeItem == null || bladeItem.getType() == Material.AIR) {
            return;
        }

        int bladeDamage = NBTUtil.nbtInt(bladeItem, BladeConstants.NBT_TAG);
        if (bladeDamage == 0) {
            player.sendMessage(ColorUtil.colorIt("&cO amplificador só funciona em lâminas de bosses."));
            return;
        }

        int newDamage = bladeDamage + amplifier;

        event.setCancelled(true);
        event.setCursor(new ItemStack(Material.AIR));
        event.setCurrentItem(new ItemStack(Material.AIR));

        //  player.getInventory().remove(amplifierItem);
        //player.getInventory().remove(bladeItem);

        player.updateInventory();

        player.getInventory().addItem(BladeUtils.asItemStack(newDamage));
    }
}
