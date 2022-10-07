package br.com.yiatzz.bosses.cache.local;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.apache.commons.codec.binary.Base64;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class HeadsLocalCache {

    private static final Cache<String, ItemStack> HEADS_LOCAL_CACHE = CacheBuilder.newBuilder()
            .expireAfterAccess(1L, TimeUnit.MINUTES)
            .build();

    public static ItemStack getUrlHead(String urlKey) {
        try {
            return HEADS_LOCAL_CACHE.get(urlKey, () -> {
                ItemStack itemStack = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
                SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();

                GameProfile gameProfile = new GameProfile(UUID.randomUUID(), null);
                byte[] encodedData;

                if (urlKey.startsWith("http://") || urlKey.startsWith("https://")) {
                    encodedData = Base64.encodeBase64(String.format("{textures:{SKIN:{url:\"%s\"}}}", urlKey).getBytes());
                } else {
                    encodedData = Base64.encodeBase64(String.format("{textures:{SKIN:{url:\"http://textures.minecraft.net/texture/%s\"}}}", urlKey).getBytes());
                }

                gameProfile.getProperties().put("textures", new Property("textures", new String(encodedData), null));
                Field profileField = null;

                try {
                    profileField = skullMeta.getClass().getDeclaredField("profile");
                } catch (NoSuchFieldException | SecurityException e) {
                    e.printStackTrace();
                }

                profileField.setAccessible(true);

                try {
                    profileField.set(skullMeta, gameProfile);
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    e.printStackTrace();
                }

                itemStack.setItemMeta(skullMeta);

                return itemStack;
            });
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public static ItemStack getPlayerHead(String userName) {
        try {
            return HEADS_LOCAL_CACHE.get(userName.toLowerCase(), () -> {
                ItemStack itemStack = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
                SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();
                skullMeta.setOwner(userName);
                itemStack.setItemMeta(skullMeta);
                return itemStack;
            });
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public static void invalidate(String urlKey) {
        HEADS_LOCAL_CACHE.invalidate(urlKey);
    }

}
