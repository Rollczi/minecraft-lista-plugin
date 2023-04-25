package dev.rollczi.minecraftlista;

import dev.rollczi.litecommands.LiteCommands;
import dev.rollczi.litecommands.bukkit.LiteBukkitFactory;
import dev.rollczi.litecommands.bukkit.tools.BukkitOnlyPlayerContextual;
import dev.rollczi.minecraftlista.award.AwardCommand;
import dev.rollczi.minecraftlista.award.AwardFacade;
import dev.rollczi.minecraftlista.vote.VoteFacade;
import dev.rollczi.minecraftlista.config.ConfigManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class MinecraftListaPlugin extends JavaPlugin {

    private LiteCommands<CommandSender> liteCommands;

    @Override
    public void onEnable() {
        ConfigManager configManager = new ConfigManager(this.getDataFolder());
        MinecraftListaConfig config = configManager.load(new MinecraftListaConfig());
        MinecraftListaMessagesConfig messages = configManager.load(new MinecraftListaMessagesConfig());

        VoteFacade voteFacade = VoteFacade.configure(config);
        AwardFacade awardFacade = AwardFacade.configureBukkit(config, messages, this, voteFacade, configManager);

        this.liteCommands = LiteBukkitFactory.builder(this.getServer(), "minecraft-lista-plugin")
                .contextualBind(Player.class, new BukkitOnlyPlayerContextual<>(messages.onlyPlayerMessage))
                .commandInstance(
                        new AwardCommand(awardFacade),
                        new MinecraftListaCommand(configManager)
                )
                .commandEditor("award", state -> state.name(config.command))
                .register();
    }

    @Override
    public void onDisable() {
        this.liteCommands.getPlatform().unregisterAll();
    }

}
