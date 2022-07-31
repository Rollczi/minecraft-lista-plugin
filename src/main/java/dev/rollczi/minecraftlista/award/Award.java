package dev.rollczi.minecraftlista.award;

import org.bukkit.entity.Player;

import java.util.List;

public interface Award {

    List<String> getCommands(Player player);

}
