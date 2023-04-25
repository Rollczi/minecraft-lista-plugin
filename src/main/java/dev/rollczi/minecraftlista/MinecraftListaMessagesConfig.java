package dev.rollczi.minecraftlista;

import dev.rollczi.minecraftlista.award.AwardFacade;
import dev.rollczi.minecraftlista.config.ReloadableConfig;
import net.dzikoysk.cdn.source.Resource;
import net.dzikoysk.cdn.source.Source;

import java.io.File;

class MinecraftListaMessagesConfig implements ReloadableConfig, AwardFacade.Messages {

    String awardSuccess = "&aAward applied!";
    String awardFailure = "&cYou must vote to get this award!";
    String awardCoolDown = "&cWait a little bit before trying to claim your reward again!";
    String onlyPlayerMessage = "Only players can use this command!";

    @Override
    public Resource resource(File folder) {
        return Source.of(folder, "messages.yml");
    }

    @Override
    public String awardSuccess() {
        return awardSuccess;
    }

    @Override
    public String awardFailure() {
        return awardFailure;
    }

    @Override
    public String awardCoolDown() {
        return awardCoolDown;
    }

}
