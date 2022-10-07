package br.com.yiatzz.bosses.registry;

import br.com.yiatzz.bosses.BossPlugin;
import br.com.yiatzz.bosses.object.Boss;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class BossRegistry {

    private final Map<String, Boss> CACHE_BY_NAME = new HashMap<>();

    public void register(Boss boss) {
        CACHE_BY_NAME.putIfAbsent(boss.getIdentifier(), boss);

        BossPlugin.getInstance().getLogger().info("Boss '" + boss.getDisplayName() + " registrado.");
    }

    public Boss get(String name) {
        return CACHE_BY_NAME.get(name);
    }

    public Collection<Boss> values() {
        return CACHE_BY_NAME.values();
    }

    public void invalidate(String name) {
        CACHE_BY_NAME.remove(name);
    }

}
