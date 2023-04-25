package dev.rollczi.minecraftlista.award;

import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.route.Route;
import org.bukkit.entity.Player;

@Route(name = "award")
public class AwardCommand {

    private final AwardFacade awardFacade;

    public AwardCommand(AwardFacade awardFacade) {
        this.awardFacade = awardFacade;
    }

    @Execute
    public void award(Player player) {
        awardFacade.applyAward(player);
    }

}
