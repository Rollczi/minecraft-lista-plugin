package dev.rollczi.minecraftlista.config;

import net.dzikoysk.cdn.source.Resource;
import net.dzikoysk.cdn.source.Source;

import java.io.File;

public class MessagesConfig implements ReloadableConfig {

    public String awardSuccess = "&aAward applied!";
    public String awardFailure = "&cYou must vote to get this award!";
    public String onlyPlayerMessage = "Only players can use this command!";

    @Override
    public Resource resource(File folder) {
        return Source.of(folder, "messages.yml");
    }

}
