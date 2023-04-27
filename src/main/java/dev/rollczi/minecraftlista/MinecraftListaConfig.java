package dev.rollczi.minecraftlista;

import dev.rollczi.minecraftlista.award.AwardSettings;
import dev.rollczi.minecraftlista.config.ReloadableConfig;
import dev.rollczi.minecraftlista.vote.VoteHttpSettings;
import net.dzikoysk.cdn.entity.Description;
import net.dzikoysk.cdn.source.Resource;
import net.dzikoysk.cdn.source.Source;

import java.io.File;
import java.time.Duration;

class MinecraftListaConfig implements ReloadableConfig, AwardSettings, VoteHttpSettings {

    @Description({
            "# PL - Komenda do odbierania nagrody",
            "# EN - The command to receive the award"
    })
    String command = "award";

    @Description({
            "# PL - Klucz api, znajdziesz go na stronie https://minecraft-lista.pl/nagrody będąc zalogowanym",
            "# EN - Api key, you can find it at https://minecraft-lista.pl/nagrody while logged in"
    })
    String apiKey = "api-key";

    @Description({
            "# PL - ID serwera, znajdziesz je przechodząc na podstronę serwera, którego jesteś właścicielem",
            "# EN - Server ID, you can find it by going to the subpage of the server you own"
    })
    String serverId = "server-id";

    @Description({
            "# PL - Czas oczekiwania na ponowne użycie komendy",
            "# EN - Cooldown time for reusing the command"
    })
    Duration coolDown = Duration.ofSeconds(5);

    @Override
    public String apiKey() {
        return apiKey;
    }

    @Override
    public String serverId() {
        return serverId;
    }

    @Override
    public Duration coolDown() {
        return coolDown;
    }

    @Override
    public Resource resource(File folder) {
        return Source.of(folder, "config.yml");
    }

}
