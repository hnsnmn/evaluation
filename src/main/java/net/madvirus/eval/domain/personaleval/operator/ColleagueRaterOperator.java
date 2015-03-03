package net.madvirus.eval.domain.personaleval.operator;

import net.madvirus.eval.api.personaleval.colleague.ColleagueCompetencyEvaluatedEvent;
import net.madvirus.eval.command.personaleval.colleague.UpdateColleagueCompetencyEvalCommand;
import net.madvirus.eval.domain.personaleval.PersonalEval;

public class ColleagueRaterOperator {
    private PersonalEval personalEval;

    public ColleagueRaterOperator(PersonalEval personalEval) {
        this.personalEval = personalEval;
    }

    public void updateColleagueCompetencyEval(UpdateColleagueCompetencyEvalCommand command) {
        // TODO 여기에 동료 검사 권한을 가졌는지 여부를 검사하는 기능을 넣어야 하나?
        // TODO 동료가 이미 평가를 완료했는지 여부를 검사하는 코드가 커맨드 처리 메서드에서 필요!!!
        personalEval.applyEvent(new ColleagueCompetencyEvaluatedEvent(personalEval.getId(), command.getRaterId(), command.getEvalSet()));

    }
}
