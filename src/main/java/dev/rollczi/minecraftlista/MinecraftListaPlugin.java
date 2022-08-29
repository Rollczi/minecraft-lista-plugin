package dev.rollczi.minecraftlista;

import dev.rollczi.litecommands.LiteCommands;
import dev.rollczi.litecommands.bukkit.LiteBukkitFactory;
import dev.rollczi.litecommands.bukkit.tools.BukkitOnlyPlayerContextual;
import dev.rollczi.minecraftlista.award.AwardCommand;
import dev.rollczi.minecraftlista.award.AwardOperator;
import dev.rollczi.minecraftlista.award.AwardRepository;
import dev.rollczi.minecraftlista.award.AwardService;
import dev.rollczi.minecraftlista.award.BukkitAwardOperatorImpl;
import dev.rollczi.minecraftlista.award.PickupAwardResolver;
import dev.rollczi.minecraftlista.award.AwardRepositoryCdnImpl;
import dev.rollczi.minecraftlista.config.ConfigManager;
import dev.rollczi.minecraftlista.config.MessagesConfig;
import dev.rollczi.minecraftlista.request.HttpAwardResolver;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class MinecraftListaPlugin extends JavaPlugin {

    private LiteCommands<CommandSender> liteCommands;

    @Override
    public void onEnable() {
        ConfigManager configManager = new ConfigManager(this.getDataFolder());
        MinecraftListaConfig config = configManager.load(new MinecraftListaConfig());
        MessagesConfig messages = configManager.load(new MessagesConfig());

        PickupAwardResolver pickupAwardResolver = HttpAwardResolver.create(config);
        AwardRepository awardRepository = configManager.load(new AwardRepositoryCdnImpl());
        AwardOperator awardOperator = new BukkitAwardOperatorImpl(this.getServer(), this);
        AwardService awardService = new AwardService(pickupAwardResolver, awardRepository, config, awardOperator);

        this.liteCommands = LiteBukkitFactory.builder(this.getServer(), "minecraft-lista-plugin")
                .contextualBind(Player.class, new BukkitOnlyPlayerContextual(messages.onlyPlayerMessage))
                .commandInstance(
                        new AwardCommand(awardService, messages),
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
