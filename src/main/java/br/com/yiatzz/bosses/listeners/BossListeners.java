package br.com.yiatzz.bosses.listeners;

import br.com.yiatzz.bosses.BossConstants;
import br.com.yiatzz.bosses.BossPlugin;
import br.com.yiatzz.bosses.blade.BladeUtils;
import br.com.yiatzz.bosses.misc.utils.NBTUtil;
import br.com.yiatzz.bosses.object.Boss;
import br.com.yiatzz.bosses.registry.BossRegistry;
import br.com.yiatzz.bosses.repository.BossRankingRepository;
import lombok.RequiredArgsConstructor;
import net.minecraft.server.v1_8_R3.ChatComponentText;
import net.minecraft.server.v1_8_R3.EntityLiving;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.event.CraftEventFactory;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
public class BossListeners implements Listener {

    private final BossRegistry registry;
    private final BossRankingRepository rankingRepository;

    @EventHandler(priority = EventPriority.HIGHEST)
    public void on(EntityDamageByEntityEvent event) {
        Entity entity = event.getEntity();
        if (!entity.hasMetadata(Boss.METADATA)) {
            return;
        }

        if (entity instanceof LivingEntity && event.getDamager() instanceof Player) {
            LivingEntity livingEntity = (LivingEntity) entity;

            Player player = (Player) event.getDamager();
            ItemStack itemInHand = player.getItemInHand();

            if (itemInHand == null || itemInHand.getType() == Material.AIR) {
                return;
            }

            int damage = BladeUtils.identifyDamage(itemInHand);
            if (damage > 0) {
                event.setDamage(damage == Integer.MAX_VALUE ? livingEntity.getMaxHealth() : damage);

                if (damage >= livingEntity.getHealth()) {
                    entity.remove();

                    net.minecraft.server.v1_8_R3.Entity handle = ((CraftEntity) entity).getHandle();
                    ((EntityLiving) handle).killer = ((CraftPlayer) player).getHandle();

                    CraftEventFactory.callEntityDeathEvent((EntityLiving) handle, new ArrayList<>());
                }
            }

            double health = livingEntity.getHealth() - event.getDamage();
            double maxHealth = livingEntity.getMaxHealth();

            sendActionText(
                    ((Player) event.getDamager()),
                    BossConstants.ACTION_BAR_MESSAGE
                            .replace("<health>", String.valueOf(health))
                            .replace("<maxHealth>", String.valueOf(maxHealth))
            );
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void on(EntityDeathEvent event) {
        Entity entity = event.getEntity();
        if (!entity.hasMetadata(Boss.METADATA)) {
            return;
        }

        Player killer = event.getEntity().getKiller();
        if (killer != null) {
            String identifier = entity.getMetadata(Boss.METADATA).get(0).asString();

            Boss boss = registry.get(identifier);
            if (boss == null) {
                return;
            }

            boss.reward(killer, entity.getLocation());

            CompletableFuture.runAsync(() -> rankingRepository.insertOrUpdate(killer.getName()));
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void on(PlayerInteractEvent event) {
        if (event.isCancelled() || event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        Player player = event.getPlayer();

        ItemStack item = player.getInventory().getItemInHand();

        if (item == null || item.getType() == Material.AIR) {
            return;
        }

        String bossId = NBTUtil.nbtString(item, Boss.METADATA);
        if (bossId == null) {
            return;
        }

        event.setCancelled(true);

        Boss boss = registry.get(bossId);
        if (boss == null) {
            return;
        }

        Location location = event.getClickedBlock()
                .getRelative(event.getBlockFace())
                .getLocation()
                .clone()
                .add(.5, 0, .5);

        boss.spawn(location);

        int amount = item.getAmount();

        if (amount > 1) {
            item.setAmount(amount - 1);
            player.setItemInHand(item);
        } else {
            item.setAmount(0);
            item.setType(Material.AIR);
            item.setData(new MaterialData(Material.AIR));
            item.setItemMeta(null);
            player.setItemInHand(new ItemStack(Material.AIR));
        }
    }

    private void sendActionText(Player player, String message) {
        PacketPlayOutChat packet = new PacketPlayOutChat(new ChatComponentText(message), (byte) 2);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }
}
