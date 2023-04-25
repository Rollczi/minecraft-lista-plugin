package dev.rollczi.minecraftlista.award;

import dev.rollczi.minecraftlista.config.ConfigManager;
import dev.rollczi.minecraftlista.vote.VoteFacade;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.concurrent.CompletionException;

public class AwardFacade {

    private final AwardService awardService;
    private final Messages messages;

    private AwardFacade(AwardService awardService, Messages messages) {
        this.awardService = awardService;
        this.messages = messages;
    }

    public void applyAward(Player player) {
        this.awardService.applyAward(player).whenComplete((result, throwable) -> {
            if (throwable != null) {
                if (throwable instanceof CompletionException) {
                    throwable = throwable.getCause();
                }

                throwable.printStackTrace();
                return;
            }

            player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getMessage(result)));
        });
    }

    private String getMessage(AwardService.Result result) {
        switch (result) {
            case SUCCESS: return messages.awardSuccess();
            case FAILURE: return messages.awardFailure();
            case COOL_DOWN: return messages.awardCoolDown();

            default: throw new IllegalStateException(result + " is not supported!");
        }
    }

    public static AwardFacade configureBukkit(AwardSettings settings, Messages messages, Plugin plugin, VoteFacade voteFacade, ConfigManager configManager) {
        AwardRepository awardRepository = configManager.load(new AwardRepositoryCdnImpl());
        AwardOperator awardOperator = new BukkitAwardOperatorImpl(plugin);
        AwardService awardService = new AwardService(voteFacade, awardRepository, settings, awardOperator);

        return new AwardFacade(awardService, messages);
    }

    public interface Messages {
        String awardSuccess();
        String awardFailure();
        String awardCoolDown();
    }

}
