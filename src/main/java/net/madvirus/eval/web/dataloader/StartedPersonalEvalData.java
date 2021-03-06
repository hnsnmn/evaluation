package net.madvirus.eval.web.dataloader;

import net.madvirus.eval.api.personaleval.Grade;
import net.madvirus.eval.domain.evalseason.RateeType;
import net.madvirus.eval.domain.personaleval.*;
import net.madvirus.eval.query.user.UserModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class StartedPersonalEvalData extends PersonalEvalData {
    private PersonalEval personalEval;

    public StartedPersonalEvalData(PersonalEval personalEval, UserModel ratee) {
        super(ratee);
        this.personalEval = personalEval;
    }

    @Override
    public boolean isStarted() {
        return true;
    }

    @Override
    public boolean isSelfPerfEvalDone() {
        return personalEval.isSelfPerfEvalDone();
    }

    @Override
    public boolean isSelfCompeEvalDone() {
        return personalEval.isSelfCompeEvalDone();
    }

    @Override
    public boolean isSelfEvalDone() {
        return isSelfPerfEvalDone() && isSelfCompeEvalDone();
    }

    @Override
    public boolean isColleagueCompeEvalDone(String colleagueId) {
        return personalEval.isColleagueCompeEvalDone(colleagueId);
    }

    @Override
    public boolean isFirstEvalSkipTarget() {
        return personalEval.isFirstEvalSkipTarget();
    }

    @Override
    public boolean isFirstPerfEvalHad() {
        return personalEval.getFirstPerfEvalSet()
                .flatMap(eval -> Optional.of(true)).orElse(false);
    }

    @Override
    public Grade getFirstPerfEvalGrade() {
        return personalEval.getFirstPerfEvalGrade();
    }

    @Override
    public boolean isFirstCompeEvalHad() {
        return personalEval.getFirstCompeEvalSet()
                .flatMap(eval -> Optional.of(true)).orElse(false);
    }

    @Override
    public Grade getFirstCompeEvalGrade() {
        return personalEval.getFirstCompeEvalSet()
                .flatMap(eval -> Optional.ofNullable(eval.getTotalEval().getGrade())).orElse(null);
    }

    @Override
    public Grade getFirstTotalEvalGrade() {
        return personalEval.getFirstTotalEval()
                .flatMap(eval -> Optional.ofNullable(eval.getGrade())).orElse(null);
    }

    @Override
    public boolean isFirstTotalEvalDone() {
        return personalEval.isFirstTotalEvalDone();
    }


    @Override
    public boolean isSecondPerfEvalHad() {
        return personalEval.getSecondPerfEvalSet()
                .flatMap(eval -> Optional.of(true)).orElse(false);
    }

    @Override
    public Grade getSecondPerfEvalGrade() {
        return personalEval.getSecondPerfEvalSet()
                .flatMap(eval -> Optional.of(eval.getTotalEval().getGrade())).orElse(null);
    }

    @Override
    public boolean isSecondCompeEvalHad() {
        return personalEval.getSecondCompeEvalSet()
                .flatMap(eval -> Optional.of(true)).orElse(false);
    }

    @Override
    public Grade getSecondCompeEvalGrade() {
        return personalEval.getSecondCompeEvalSet()
                .flatMap(eval -> Optional.ofNullable(eval.getTotalEval().getGrade())).orElse(null);
    }

    @Override
    public Grade getSecondTotalEvalGrade() {
        return personalEval.getSecondTotalEval()
                .flatMap(eval -> Optional.ofNullable(eval.getGrade())).orElse(null);
    }

    @Override
    public boolean isSecondTotalEvalDone() {
        return personalEval.getSecondTotalEval().flatMap(evl -> Optional.of(evl.isDone())).orElse(false);
    }

    public String getId() {
        return personalEval.getId();
    }

    public String getEvalSeasonId() {
        return personalEval.getEvalSeasonId();
    }

    public RateeType getRateeType() {
        return personalEval.getRateeType();
    }

    public List<PerformanceItemAndAllEval> getPerfItemAndAllEvals() {
        List<PerformanceItem> items = personalEval.getPerformanceItems();
        Optional<PerformanceEvalSet> selfEvalSet = personalEval.getSelfPerfEvalSet();
        Optional<PerformanceEvalSet> firstEvalSet = personalEval.getFirstPerfEvalSet();
        Optional<PerformanceEvalSet> secondEvalSet = personalEval.getSecondPerfEvalSet();
        if (items == null || items.isEmpty()) return Collections.emptyList();

        List<PerformanceItemAndAllEval> result = new ArrayList<>();
        List<ItemEval> selfItemEvals =
                selfEvalSet.flatMap(set -> Optional.ofNullable(set.getEvals())).orElse(null);
        List<ItemEval> firstItemEvals =
                firstEvalSet.flatMap(set -> Optional.ofNullable(set.getEvals())).orElse(null);
        List<ItemEval> secondItemEvals =
                secondEvalSet.flatMap(set -> Optional.ofNullable(set.getEvals())).orElse(null);
        for (int i = 0; i < items.size(); i++) {
            ItemEval selfEval = getItemEval(selfItemEvals, i);
            ItemEval firstEval = getItemEval(firstItemEvals, i);
            ItemEval secondEval = getItemEval(secondItemEvals, i);
            result.add(new PerformanceItemAndAllEval(
                    items.get(i),
                    selfEval,
                    firstEval, secondEval));
        }
        return result;
    }

    private ItemEval getItemEval(List<ItemEval> itemEvals, int idx) {
        if (itemEvals == null) return null;
        if (itemEvals.isEmpty()) return null;
        return itemEvals.get(idx);
    }

    public AllCompeEvals getAllCompeEvals() {
        return personalEval.getAllCompeEvals();
    }

    public ItemEval getFirstPerfTotalEval() {
        return personalEval.getFirstPerfEvalSet()
                .flatMap(eval -> Optional.ofNullable(eval.getTotalEval())).orElse(null);
    }

    public ItemEval getSecondPerfTotalEval() {
        return personalEval.getSecondPerfEvalSet()
                .flatMap(eval -> Optional.ofNullable(eval.getTotalEval())).orElse(null);
    }

    @Override
    public TotalEval getFirstTotalEval() {
        return personalEval.getFirstTotalEval().orElse(null);
    }

    @Override
    public TotalEval getSecondTotalEval() {
        return personalEval.getSecondTotalEval().orElse(null);
    }

    @Override
    public Double getFirstMark() {
        return personalEval.getFirstMark();
    }

    @Override
    public Double getSecondMark() {
        if (!isSecondTotalEvalDone())
            return null;
        else
            return MarkCalculator.calculate(getRateeType(),
                    getSecondPerfEvalGrade(),
                    getAllCompeEvals().getSecondEvalSet().getCommonsAverage(),
                    getAllCompeEvals().getSecondEvalSet().getLeadershipAverage(),
                    getAllCompeEvals().getSecondEvalSet().getAmAverage()
            );
    }

    public boolean checkFirstRater(String firstRaterId) {
        return personalEval.checkFirstRater(firstRaterId);
    }

    public boolean checkSecondRater(String secondRaterId) {
        return personalEval.checkSecondRater(secondRaterId);
    }
}
