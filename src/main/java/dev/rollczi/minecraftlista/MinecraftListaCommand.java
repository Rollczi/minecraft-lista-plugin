package dev.rollczi.minecraftlista;

import dev.rollczi.litecommands.command.async.Async;
import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.permission.Permission;
import dev.rollczi.litecommands.command.route.Route;
import dev.rollczi.minecraftlista.config.ConfigManager;

@Route(name = "minecraft-lista-plugin")
@Permission("minecraftlista.admin")
class MinecraftListaCommand {

    private final ConfigManager configManager;

    MinecraftListaCommand(ConfigManager configManager) {
        this.configManager = configManager;
    }

    @Async
    @Execute(route = "reload")
    String execute() {
        this.configManager.reload();

        return "&aConfig reloaded";
    }

}
