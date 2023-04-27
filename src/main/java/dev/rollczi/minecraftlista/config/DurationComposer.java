package dev.rollczi.minecraftlista.config;

import dev.rollczi.litecommands.shared.EstimatedTemporalAmountParser;
import net.dzikoysk.cdn.serdes.Composer;
import net.dzikoysk.cdn.serdes.SimpleDeserializer;
import net.dzikoysk.cdn.serdes.SimpleSerializer;
import panda.std.Result;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

class DurationComposer implements Composer<Duration>, SimpleSerializer<Duration>, SimpleDeserializer<Duration> {

    private static final EstimatedTemporalAmountParser<Duration> PARSER = EstimatedTemporalAmountParser.createDuration()
            .withUnit("ms", ChronoUnit.MILLIS)
            .withUnit("sec", ChronoUnit.SECONDS)
            .withUnit("s", ChronoUnit.SECONDS)
            .withUnit("m", ChronoUnit.MINUTES)
            .withUnit("h", ChronoUnit.HOURS)
            .withUnit("d", ChronoUnit.DAYS)
            ;

    @Override
    public Result<Duration, Exception> deserialize(String duration) {
        return Result.supplyThrowing(IllegalArgumentException.class, () -> PARSER.parse(duration));
    }

    @Override
    public Result<String, Exception> serialize(Duration duration) {
        return Result.ok(PARSER.format(duration));
    }

}
