package net.madvirus.eval.command.evalseason;

import net.madvirus.eval.domain.evalseason.EvalSeason;
import org.axonframework.commandhandling.annotation.AggregateAnnotationCommandHandler;
import org.axonframework.test.FixtureConfiguration;
import org.axonframework.test.Fixtures;
import org.junit.Before;

public class AbstractEvalSeasonCommandTest {
    protected FixtureConfiguration fixture;

    @Before
    public void setUpCommonCommandHandler() throws Exception {
        fixture = Fixtures.newGivenWhenThenFixture(EvalSeason.class);
        fixture.registerAnnotatedCommandHandler(new AggregateAnnotationCommandHandler(EvalSeason.class, fixture.getRepository()));
        fixture.registerAnnotatedCommandHandler(new CreateEvalSeasonCommandHandler(fixture.getRepository()));
    }
}
