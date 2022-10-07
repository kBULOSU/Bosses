package br.com.yiatzz.bosses.listeners;

import br.com.yiatzz.bosses.BossConstants;
import br.com.yiatzz.bosses.inventories.holders.BossesListHolder;
import br.com.yiatzz.bosses.inventories.holders.BossesRewardsListHolder;
import br.com.yiatzz.bosses.inventories.holders.IndexHolder;
import br.com.yiatzz.bosses.inventories.holders.KillsRankingHolder;
import br.com.yiatzz.bosses.misc.utils.NBTUtil;
import br.com.yiatzz.bosses.object.Boss;
import br.com.yiatzz.bosses.registry.BossRegistry;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

@RequiredArgsConstructor
public class BossInventoryListener implements Listener {

    private final BossRegistry bossRegistry;

    @EventHandler(priority = EventPriority.HIGHEST)
    public void on(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();

        Player player = (Player) event.getWhoClicked();
        int slot = event.getSlot();

        if (inventory.getHolder() instanceof IndexHolder) {
            event.setCancelled(true);

            processIndexClick(player, slot);
        } else if (inventory.getHolder() instanceof BossesListHolder) {
            event.setCancelled(true);

            processListClick(player, event.getCurrentItem(), slot);
        } else if (inventory.getHolder() instanceof BossesRewardsListHolder) {
            event.setCancelled(true);

            processRewardsListClick(player, slot);
        } else if (inventory.getHolder() instanceof KillsRankingHolder) {
            event.setCancelled(true);

            processRankingClick(player, slot);
        }
    }

    private void processIndexClick(Player player, int slot) {
        if (slot == IndexHolder.LIST_SLOT) {
            player.openInventory(new BossesListHolder(this.bossRegistry).getInventory());
        } else if (slot == IndexHolder.RANK_SLOT) {
            BossConstants.KILLS_RANKING_HOLDER.update();

            player.openInventory(BossConstants.KILLS_RANKING_HOLDER.getInventory());
        }
    }

    private void processListClick(Player player, ItemStack currItem, int slot) {
        if (slot == 31) {
            player.openInventory(new IndexHolder().getInventory());
            return;
        }

        if (currItem == null || currItem.getType() == Material.AIR) {
            return;
        }

        String bossId = NBTUtil.nbtString(currItem, Boss.METADATA);
        if (bossId == null) {
            return;
        }

        Boss boss = bossRegistry.get(bossId);
        if (boss == null) {
            return;
        }

        player.openInventory(new BossesRewardsListHolder(boss.getRewards()).getInventory());
    }

    private void processRewardsListClick(Player player, int slot) {
        if (slot == 49) {
            player.openInventory(new BossesListHolder(this.bossRegistry).getInventory());
        }
    }

    private void processRankingClick(Player player, int slot) {
        if (slot == 40 || slot == 22) {
            player.openInventory(new IndexHolder().getInventory());
        }
    }
}
