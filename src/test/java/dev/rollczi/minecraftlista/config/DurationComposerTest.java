package dev.rollczi.minecraftlista.config;

import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

class DurationComposerTest {

    @Test
    void deserialize() {
        DurationComposer composer = new DurationComposer();

        assertEquals(1, composer.deserialize("1d").get().toDays());
        assertEquals(1, composer.deserialize("1h").get().toHours());
        assertEquals(1, composer.deserialize("1m").get().toMinutes());
        assertEquals(1, composer.deserialize("1s").get().getSeconds());
        assertEquals(1, composer.deserialize("1ms").get().toMillis());
        assertEquals(1, composer.deserialize("1us").get().toNanos() / 1000);
        assertEquals(1, composer.deserialize("1ns").get().toNanos());
    }

    @Test
    void serialize() {
        DurationComposer composer = new DurationComposer();

        assertEquals("1d", composer.serialize(Duration.ofDays(1)).get());
        assertEquals("1h", composer.serialize(Duration.ofHours(1)).get());
        assertEquals("1m", composer.serialize(Duration.ofMinutes(1)).get());
        assertEquals("1s", composer.serialize(Duration.ofSeconds(1)).get());
        assertEquals("1ms", composer.serialize(Duration.ofMillis(1)).get());
        assertEquals("1us", composer.serialize(Duration.ofNanos(1000)).get());
        assertEquals("1ns", composer.serialize(Duration.ofNanos(1)).get());
    }

}