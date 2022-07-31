package dev.rollczi.minecraftlista;

import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.permission.Permission;
import dev.rollczi.litecommands.command.section.Section;
import dev.rollczi.minecraftlista.config.ConfigManager;

@Section(route = "minecraft-lista-plugin")
@Permission("dev.rollczi.minecraftlista.admin")
class MinecraftListaCommand {

    private final ConfigManager configManager;

    MinecraftListaCommand(ConfigManager configManager) {
        this.configManager = configManager;
    }

    @Execute(route = "reload")
    String execute() {
        this.configManager.reload();

        return "&aConfig reloaded";
    }

}
