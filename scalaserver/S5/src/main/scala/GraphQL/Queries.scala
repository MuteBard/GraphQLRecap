package GraphQL
import Model.Bug_._
import Model.Fish_._
import Model.MovementRecord_._
import Model.TurnipTransaction_._
import Model.User_._
import Service._
import zio.{IO, UIO}

object Queries {

	//Type Definitions
	case class Queries(
		//User
		getUser:                        usernameArgs => IO[NotFound, User],

	    //TurnipTransaction
		validatePendingTransaction:     pendingTransactionArgs => UIO[TurnipTransaction],

	    //MovementRecord
//		getDayRecords:                  UIO[MovementRecord],
		getDayRecords:                  UIO[Int],
		getMonthRecords:                UIO[List[MovementRecord]],
		getTurnipPrices:                UIO[Int],

		//Bug
		getAllBugs:                     UIO[List[Bug]],
		getAllBugsByMonth:              bugMonthsArgs => IO[NotFound, List[Bug]],
		getAllRareBugsByMonth:          bugMonthsArgs => IO[NotFound, List[Bug]],
		getBugById:                     bugIdArgs => IO[NotFound, Bug],
		getBugByName:                   bugNameArgs => IO[NotFound, Bug],
		getBugByRandom:                 bugMonthsArgs => IO[NotFound, Bug],
		//Fish
		getAllFishes:                   UIO[List[Fish]],
		getAllFishesByMonth:            fishMonthsArgs => IO[NotFound, List[Fish]],
		getAllRareFishesByMonth:        fishMonthsArgs => IO[NotFound, List[Fish]],
		getFishById:                    fishIdArgs => IO[NotFound, Fish],
		getFishByName:                  fishNameArgs => IO[NotFound, Fish],
		getFishByRandom:                fishMonthsArgs => IO[NotFound, Fish],
	)
	val cbs : CrossingBotService = new CBS()


	val allQueries = Queries(
		args => cbs.getUser(args.username),
		args => cbs.validatePendingTransaction(args.username, args.business, args.quantity),
		cbs.getDayRecords,
		cbs.getMonthRecords,
		cbs.getTurnipPrices,
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
