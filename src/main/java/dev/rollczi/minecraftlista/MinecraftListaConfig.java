package dev.rollczi.minecraftlista;

import dev.rollczi.minecraftlista.config.ReloadableConfig;
import dev.rollczi.minecraftlista.request.RequestSettings;
import net.dzikoysk.cdn.source.Resource;
import net.dzikoysk.cdn.source.Source;

import java.io.File;

class MinecraftListaConfig implements ReloadableConfig, RequestSettings {

    String command = "award";

    String apiKey = "api-key";
    String serverId = "server-id";

    @Override
    public String apiKey() {
        return apiKey;
    }

    @Override
    public String serverId() {
        return serverId;
    }

    @Override
    public Resource resource(File folder) {
        return Source.of(folder, "config.yml");
    }

}
