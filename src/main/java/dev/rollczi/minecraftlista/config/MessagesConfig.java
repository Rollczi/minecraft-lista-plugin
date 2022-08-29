package dev.rollczi.minecraftlista.config;

import dev.rollczi.minecraftlista.award.AwardService;
import net.dzikoysk.cdn.source.Resource;
import net.dzikoysk.cdn.source.Source;

import java.io.File;

public class MessagesConfig implements ReloadableConfig {

    public String awardSuccess = "&aAward applied!";
    public String awardFailure = "&cYou must vote to get this award!";
    public String awardCoolDown = "&cWait a little bit before trying to claim your reward again!";
    public String onlyPlayerMessage = "Only players can use this command!";

    @Override
    public Resource resource(File folder) {
        return Source.of(folder, "messages.yml");
    }

    // kinda shitty, but old version support :<
    public String getMessage(AwardService.Result result) {
        switch (result) {
            case SUCCESS: return awardSuccess;
            case FAILURE: return awardFailure;
            case COOL_DOWN: return awardCoolDown;

            default: throw new IllegalStateException(result + " is not supported!");
        }
    }

}
