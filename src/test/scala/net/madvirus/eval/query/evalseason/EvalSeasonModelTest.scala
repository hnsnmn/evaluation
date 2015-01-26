package net.madvirus.eval.query.evalseason

import net.madvirus.eval.api.evalseaon.RateeType
import net.madvirus.eval.query.user.UserModel
import org.axonframework.test.matchers.Matchers._
import org.junit.Assert._
import org.junit.{Before, Test}

class EvalSeasonModelTest {

  var model: EvalSeasonModel = _

  val ratee1: UserModel = new UserModel("ratee1", "피평가자1", "1234")
  val ratee2: UserModel = new UserModel("ratee2", "피평가자2", "1234")
  val ratee3: UserModel = new UserModel("ratee3", "피평가자3", "1234")


  val first1: UserModel = new UserModel("first1", "평가자1", "1234");
  val first2: UserModel = new UserModel("first2", "평가자2", "1234");

  val second1: UserModel = new UserModel("second1", "상위평가자1", "1234");
  val second2: UserModel = new UserModel("second2", "상위평가자2", "1234");

  val colleague1: UserModel = new UserModel("colleague1", "동료1", "1234");
  val colleague2: UserModel = new UserModel("colleague2", "동료1", "1234");
  val colleague3: UserModel = new UserModel("colleague3", "동료1", "1234");
  val colleague4: UserModel = new UserModel("colleague4", "동료1", "1234");

  @Before
  def setup(): Unit = {
    model = new EvalSeasonModel("ID", "name")
      .updateMapping(new RateeMappingModel(ratee1, RateeType.MEMBER, first1, second1, Set(colleague1)))
      .updateMapping(new RateeMappingModel(ratee2, RateeType.TEAM_LEADER, null, second1, Set(colleague2)))
      .updateMapping(new RateeMappingModel(ratee3, RateeType.TEAM_LEADER, first1, second1, Set(colleague3, colleague4)))
  }

  private def assertContains(list: Set[UserModel], contained:List[UserModel]): Unit = {
    contained.foreach(x => assertTrue(s"list must contains $x", list contains x))
  }

  private def assertNotContains(list: Set[UserModel], noContained:List[UserModel]): Unit = {
    noContained.foreach(x => assertFalse(s"list don't have to contains $x", list contains x))
  }

  @Test
  def baseMapping {
    assertThat(model.getId, equalTo("ID"))
    assertThat(model.getName, equalTo("name"))
    assertThat(model.getOpened, equalTo(false))

    val mapping: Option[RateeMappingModel] = model.getRateeMappingOf(ratee1.getId)
    assertThat(mapping.nonEmpty, equalTo(true))

    assertThat(model.getRateeMappingOf("noRatee").isEmpty, equalTo(true))

    val list: Set[UserModel] = model.getRateesOfFirstRater(first1.getId)
    assertContains(list, List(ratee3, ratee1))
    assertNotContains(list, List(ratee2))

    val rateeOfSecond = model.getRateesOfSecondRater(second1.getId);
    assertContains(rateeOfSecond, List(ratee1, ratee2, ratee3))

    val rateeOfColleague1 = model.getRateesOfColleague(colleague1.getId)
    assertContains(rateeOfColleague1, List(ratee1))
    assertNotContains(rateeOfColleague1, List(ratee2, ratee3))

    val rateeOfColleague2 = model.getRateesOfColleague(colleague2.getId)
    assertContains(rateeOfColleague2, List(ratee2))
    assertNotContains(rateeOfColleague2, List(ratee1, ratee3))

    val rateeOfColleague3 = model.getRateesOfColleague(colleague3.getId)
    assertContains(rateeOfColleague3, List(ratee3))
    assertNotContains(rateeOfColleague3, List(ratee1, ratee2))
  }

  @Test
  def changeFirstRaterFromOneToAnother {
    model = model.updateMapping(new RateeMappingModel(ratee3, RateeType.MEMBER, first2, second1, Set(colleague3)))

    val rateesOfFirst1 = model.getRateesOfFirstRater(first1.getId)
    assertContains(rateesOfFirst1, List(ratee1))
    assertNotContains(rateesOfFirst1, List(ratee2, ratee3))

    val rateesOfFirst2 = model.getRateesOfFirstRater(first2.getId)
    assertContains(rateesOfFirst2, List(ratee3))
  }

  @Test
  def changeFirstRaterFromNullToOther {
    model = model.updateMapping(new RateeMappingModel(ratee2, RateeType.MEMBER, first1, second1, Set(colleague2)));

    val rateesOfFirst = model.getRateesOfFirstRater(first1.getId)
    assertContains(rateesOfFirst, List(ratee1, ratee2, ratee3))
  }

  @Test
  def changeFirstRaterFromOneToNull: Unit = {
    model = model.updateMapping(new RateeMappingModel(ratee1, RateeType.MEMBER, null, second1, Set(colleague2)));
    val rateesOfFirst = model.getRateesOfFirstRater(first1.getId)
    assertContains(rateesOfFirst, List(ratee3))
    assertNotContains(rateesOfFirst, List(ratee1, ratee2))
  }

  @Test
  def changeSecondRaterFromOneToOther {
    model = model.updateMapping(new RateeMappingModel(ratee1, RateeType.MEMBER, first1, second2, Set(colleague2)));
    val rateesOfSecond1 = model.getRateesOfSecondRater(second1.getId)
    assertContains(rateesOfSecond1, List(ratee2, ratee3))
    assertNotContains(rateesOfSecond1, List(ratee1))

    val rateesOfSecond2 = model.getRateesOfSecondRater(second2.getId)
    assertContains(rateesOfSecond2, List(ratee1))
    assertNotContains(rateesOfSecond2, List(ratee2, ratee3))
  }

  @Test
  def addNewColleagueRater: Unit = {
    model = model.updateMapping(new RateeMappingModel(ratee1, RateeType.MEMBER, first1, second1, Set(colleague1, colleague2)))

    val rateeOfColleague2 = model.getRateesOfColleague(colleague2.getId)
    assertContains(rateeOfColleague2, List(ratee1, ratee2))
    assertNotContains(rateeOfColleague2, List(ratee3))
  }

  @Test
  def removeExistingColleagues: Unit = {
    model = model.updateMapping(new RateeMappingModel(ratee3, RateeType.MEMBER, first1, second1, Set(colleague3)))

    val rateeOfColleague4 = model.getRateesOfColleague(colleague4.getId)
    assertTrue(rateeOfColleague4.isEmpty)
  }

  @Test
  def replaceColleagues: Unit = {
    model = model.updateMapping(new RateeMappingModel(ratee3, RateeType.MEMBER, first1, second1, Set(colleague1, colleague2)))

    val rateeOfColleague1 = model.getRateesOfColleague(colleague1.getId)
    assertContains(rateeOfColleague1, List(ratee1, ratee3))

    val rateeOfColleague2 = model.getRateesOfColleague(colleague2.getId)
    assertContains(rateeOfColleague2, List(ratee2, ratee3))

    val rateeOfColleague4 = model.getRateesOfColleague(colleague4.getId)
    assertTrue(rateeOfColleague4.isEmpty)

    val rateeOfColleague3 = model.getRateesOfColleague(colleague3.getId)
    assertTrue(rateeOfColleague3.isEmpty)
  }


}
