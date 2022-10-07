package br.com.yiatzz.bosses.inventories.holders;

import br.com.yiatzz.bosses.BossConstants;
import br.com.yiatzz.bosses.object.BossReward;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.util.List;

@RequiredArgsConstructor
public class BossesRewardsListHolder implements InventoryHolder {

    private final List<BossReward> rewards;

    @Override
    public Inventory getInventory() {
        Inventory inventory = Bukkit.createInventory(this, 6 * 9, "Recompensas");

        int slot = 10;
        for (BossReward reward : rewards) {
            if ((slot + 1) % 9 == 0) {
                slot += 2;
            }

            inventory.setItem(
                    slot++,
                    reward.getIcon()
            );
        }

        inventory.setItem(
                49,
                BossConstants.BACK_ARROW
        );

        return inventory;
    }

    @Override
    public World getWorld() {
        return null;
    }
}
