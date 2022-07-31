package dev.rollczi.minecraftlista.config;

import net.dzikoysk.cdn.Cdn;
import net.dzikoysk.cdn.CdnFactory;
import net.dzikoysk.cdn.reflect.Visibility;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class ConfigManager {

    private static final Cdn CDN = CdnFactory.createYamlLike().getSettings()
            .withMemberResolver(Visibility.PACKAGE_PRIVATE)
            .build();

    private final Set<ReloadableConfig> configs = new HashSet<>();
    private final File dataFolder;

    public ConfigManager(File dataFolder) {
        this.dataFolder = dataFolder;
    }

    public <T extends ReloadableConfig> T load(T config) {
        CDN.load(config.resource(this.dataFolder), config)
                .orThrow(RuntimeException::new);

        CDN.render(config, config.resource(this.dataFolder))
                .orThrow(RuntimeException::new);

        this.configs.add(config);
        return config;
    }

    public <T extends ReloadableConfig> void save(T config) {
        CDN.render(config, config.resource(this.dataFolder))
                .orThrow(RuntimeException::new);
    }

    public void reload() {
        for (ReloadableConfig config : configs) {
            load(config);
        }
    }

}