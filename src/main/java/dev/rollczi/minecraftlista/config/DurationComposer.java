package dev.rollczi.minecraftlista.config;

import dev.rollczi.litecommands.shared.EstimatedTemporalAmountParser;
import net.dzikoysk.cdn.serdes.Composer;
import net.dzikoysk.cdn.serdes.SimpleDeserializer;
import net.dzikoysk.cdn.serdes.SimpleSerializer;
import panda.std.Result;

import java.time.Duration;

class DurationComposer implements Composer<Duration>, SimpleSerializer<Duration>, SimpleDeserializer<Duration> {

    @Override
    public Result<Duration, Exception> deserialize(String duration) {
        return Result.supplyThrowing(IllegalArgumentException.class, () -> EstimatedTemporalAmountParser.DATE_TIME_UNITS.parse(duration));
    }

    @Override
    public Result<String, Exception> serialize(Duration duration) {
        return Result.ok(EstimatedTemporalAmountParser.DATE_TIME_UNITS.format(duration));
    }

}
