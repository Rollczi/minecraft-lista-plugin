package dev.rollczi.minecraftlista.award;

import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.section.Section;
import dev.rollczi.litecommands.platform.LiteSender;
import dev.rollczi.minecraftlista.config.MessagesConfig;
import org.bukkit.entity.Player;

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
        this.awardService.applyAward(player).whenComplete((success, throwable) -> {
            if (throwable != null) {
                throwable.printStackTrace();
                return;
            }

            liteSender.sendMessage(success ? messagesConfig.awardSuccess : messagesConfig.awardFailure);
        });
    }

}
