package dev.rollczi.minecraftlista.award;

import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.section.Section;
import dev.rollczi.litecommands.platform.LiteSender;
import dev.rollczi.minecraftlista.config.MessagesConfig;
import org.bukkit.entity.Player;

import java.util.concurrent.CompletionException;

@Section(route = "award")
public class AwardCommand {

    private final AwardService awardService;
    private final MessagesConfig messagesConfig;

    public AwardCommand(AwardService awardService, MessagesConfig messagesConfig) {
        this.awardService = awardService;
        this.messagesConfig = messagesConfig;
    }

    @Execute
    public void award(LiteSender liteSender, Player player) {
        this.awardService.applyAward(player).whenComplete((result, throwable) -> {
            if (throwable != null) {
                if (throwable instanceof CompletionException) {
                    throwable = throwable.getCause();
                }

                throwable.printStackTrace();
                return;
            }

            liteSender.sendMessage(messagesConfig.getMessage(result));
        });
    }

}
