package net.madvirus.eval.command.evalseason;

import org.axonframework.commandhandling.annotation.TargetAggregateIdentifier;

public class StartColleagueEvalCommand {
    @TargetAggregateIdentifier
    private String evalSeasonId;

    public StartColleagueEvalCommand(String evalSeasonId) {
        this.evalSeasonId = evalSeasonId;
    }

    public String getEvalSeasonId() {
        return evalSeasonId;
    }
}
