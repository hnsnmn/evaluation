package net.madvirus.eval.command.evalseason;

import net.madvirus.eval.api.RateeMapping;
import net.madvirus.eval.api.evalseaon.EvalSeasonCreatedEvent;
import net.madvirus.eval.api.evalseaon.MappingUpdatedEvent;
import net.madvirus.eval.api.evalseaon.RateeType;
import net.madvirus.eval.api.evalseaon.UpdateMappingCommand;
import org.axonframework.repository.AggregateNotFoundException;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class UpdateMappingsCommandTest extends AbstractEvalSeasonCommandTest {
    public static final String EVALSEASON_ID = "eval-2014";

    @Test
    public void notFound_throwEx() throws Exception {
        fixture.given()
                .when(createUpdateMappingsCommand())
                .expectException(AggregateNotFoundException.class);
    }

    @Test
    public void updateMappings() throws Exception {
        fixture.given(new EvalSeasonCreatedEvent(EVALSEASON_ID, "평가", new Date()))
                .when(createUpdateMappingsCommand())
                .expectEvents(
                        new MappingUpdatedEvent(EVALSEASON_ID, Arrays.asList(createRateeMapping1(), createRateeMapping2()))
                );
    }
    private UpdateMappingCommand createUpdateMappingsCommand() {
        UpdateMappingCommand cmd = new UpdateMappingCommand();
        cmd.setEvalSeasonId(EVALSEASON_ID);
        List<RateeMapping> rateeMappings = new ArrayList<>();

        rateeMappings.add(createRateeMapping1());
        rateeMappings.add(createRateeMapping2());
        cmd.setRateeMappings(rateeMappings);
        return cmd;
    }

    private RateeMapping createRateeMapping1() {
        return new RateeMapping("ratee1", RateeType.TEAM_LEADER, "firstRater", "secondRater", "colleague1", "colleague2");
    }

    private RateeMapping createRateeMapping2() {
        return new RateeMapping("ratee2", RateeType.TEAM_LEADER, "firstRater1", "secondRater1", "colleague1");
    }

}
