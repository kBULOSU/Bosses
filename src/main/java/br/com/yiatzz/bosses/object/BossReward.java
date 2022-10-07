package br.com.yiatzz.bosses.object;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.inventory.ItemStack;

@RequiredArgsConstructor
@Getter
public class BossReward {

    private final ItemStack icon;
    private final String command;

}
