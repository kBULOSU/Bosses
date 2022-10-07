package br.com.yiatzz.bosses.inventories.holders;

import br.com.yiatzz.bosses.BossConstants;
import br.com.yiatzz.bosses.BossPlugin;
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

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;


public class KillsRankingHolder implements InventoryHolder {

    private final List<ItemStack> icons = new LinkedList<>();
    private Long updateCooldown;

    private boolean updating;

    public void update() {
        if (updateCooldown == null || updateCooldown <= System.currentTimeMillis()) {
            updating = true;

            this.icons.clear();

            Executor executor = command -> Bukkit.getScheduler().runTask(BossPlugin.getInstance(), command);

            CompletableFuture.supplyAsync(() -> BossPlugin.getInstance().getRankingRepository().fetch())
                    .thenAcceptAsync(ranking -> {

                        int index = 1;

                        for (Map.Entry<String, Integer> entry : ranking.entrySet()) {
                            String userName = entry.getKey();
                            Integer kills = entry.getValue();

                            ItemStack skullItem = new ItemStack(HeadsLocalCache.getPlayerHead(userName));
                            ItemMeta itemMeta = skullItem.getItemMeta();

                            itemMeta.setDisplayName(ColorUtil.colorIt(String.format(
                                    "&e%s° %s",
                                    index++,
                                    userName
                            )));

                            itemMeta.setLore(Lists.newArrayList(
                                    ColorUtil.colorIt(String.format(
                                            "&7Matou &f%s &7bosses.",
                                            kills
                                    ))
                            ));

                            skullItem.setItemMeta(itemMeta);

                            icons.add(skullItem);

                            updating = false;
                        }

                    }, executor);

            this.updateCooldown = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(10);
        }
    }

    @Override
    public Inventory getInventory() {

        Inventory inventory = Bukkit.createInventory(this, updating ? 3 * 9 : 5 * 9, "Ranking de Kills");


        if (updating) {

            ItemStack updatingItem = new ItemStack(Material.WEB);
            ItemMeta updatingItemMeta = updatingItem.getItemMeta();
            updatingItemMeta.setDisplayName(ColorUtil.colorIt("&cAtualizando..."));
            updatingItem.setItemMeta(updatingItemMeta);

            inventory.setItem(
                    13,
                    updatingItem
            );

            inventory.setItem(
                    22,
                    BossConstants.BACK_ARROW
            );

            return inventory;
        }

        int slot = 10;

        for (ItemStack icon : icons) {
            if ((slot + 1) % 9 == 0) {
                slot += 2;
            }

            inventory.setItem(
                    slot++,
                    icon
            );
        }

        inventory.setItem(
                40,
                BossConstants.BACK_ARROW
        );

        return inventory;
    }

    @Override
    public World getWorld() {
        return null;
    }
}
