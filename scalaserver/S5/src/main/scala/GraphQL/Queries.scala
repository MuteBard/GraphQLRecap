package GraphQL
//import Model.User_._
import Model.Bug_._
import Model.Fish_._
import Service._
import zio.UIO

object Queries {

	case class Queries(
          //User
          //		getUser : UIO[User],

          //Bug
          getAllBugs :                                UIO[List[Bug]],
          getAllBugsByMonth: bugMonthsArgs =>         UIO[List[Bug]],
          getAllRareBugsByMonth: bugMonthsArgs =>     UIO[List[Bug]],
          getBugById: bugIdArgs =>                    UIO[Bug],
          getBugByName: bugNameArgs =>                UIO[Bug],
          getBugByRandom : bugMonthsArgs =>           UIO[Bug],
	      //Fish
          getAllFishes :                              UIO[List[Fish]],
          getAllFishesByMonth: fishMonthsArgs =>       UIO[List[Fish]],
          getAllRareFishesByMonth: fishMonthsArgs =>   UIO[List[Fish]],
          getFishById: fishIdArgs =>                   UIO[Fish],
          getFishByName: fishNameArgs =>               UIO[Fish],
          getFishByRandom : fishMonthsArgs =>          UIO[Fish],
	)
	val cbs : CrossingBotService = new CBS()

	val allQueries = Queries(
		cbs.getAllBugs,
		args => cbs.getAllBugsByMonth(args.months),
		args => cbs.getAllRareBugsByMonth(args.months),
		args => cbs.getBugById(args.bugId),
		args => cbs.getBugByName(args.name),
		args => cbs.getBugByRandom(args.months),
		cbs.getAllFishes,
		args => cbs.getAllFishesByMonth(args.months),
		args => cbs.getAllRareFishesByMonth(args.months),
		args => cbs.getFishById(args.fishId),
		args => cbs.getFishByName(args.name),
		args => cbs.getFishByRandom(args.months)
	)
}
