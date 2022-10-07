package br.com.yiatzz.bosses.object;

import br.com.yiatzz.bosses.BossPlugin;
import br.com.yiatzz.bosses.misc.utils.NBTUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.List;

@RequiredArgsConstructor
@Getter
public class Boss {

    public static final String METADATA = "bossId";

    private final String identifier;
    private final String displayName;
    private final EntityType entityType;
    private final int hp;
    private final ItemStack icon;
    private final List<BossReward> rewards;

    public ItemStack asItemStack(int amount) {
        ItemStack itemStack = icon.clone();
        itemStack.setAmount(amount);

        NBTUtil.modify(itemStack, compound -> compound.setString(METADATA, this.identifier));

        return itemStack;
    }

    public void spawn(Location location) {

        Entity entity = location.getWorld().spawnEntity(location, entityType);
        if (entity instanceof LivingEntity) {
            LivingEntity livingEntity = (LivingEntity) entity;
            livingEntity.setMaxHealth(hp);
            livingEntity.setHealth(hp);

            livingEntity.setMetadata(METADATA, new FixedMetadataValue(BossPlugin.getInstance(), this.identifier));
            livingEntity.setCustomNameVisible(true);
            livingEntity.setCustomName(ChatColor.translateAlternateColorCodes('§', this.displayName));
        }

        net.minecraft.server.v1_8_R3.Entity nmsEntity = ((CraftEntity) entity).getHandle();

        nmsEntity.getDataWatcher().watch(15, (byte) 1);
    }

    public void reward(Player player, Location location) {
        for (BossReward reward : rewards) {
            if (reward.getCommand().isEmpty()) {

                ItemStack item = reward.getIcon().clone();
                location.getWorld().dropItem(location, item);
                continue;
            }

            Bukkit.dispatchCommand(
                    Bukkit.getConsoleSender(),
                    reward.getCommand().replace("<player>", player.getName())
            );
        }
    }
}
