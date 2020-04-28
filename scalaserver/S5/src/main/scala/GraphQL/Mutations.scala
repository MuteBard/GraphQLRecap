package GraphQL
import Model.TurnipTransaction_._
import Model.User_._
import Service._
import zio.{IO, UIO}

object Mutations {

	case class Mutations(
	    //User
	    catchCreature :           catchCreatureArgs => IO[NotFound, String],
	    finalizeUserCreation :     finalizeUserArgs => IO[NotFound, String],
	    sellOneCreature :         sellCreatureArgs => UIO[Int],
	    sellAllCreatures :        usernameArgs => UIO[Int],
	    acknowledgeTransaction:   authorizedTransactionArgs => IO[NotFound, String],
	    populate:                 UIO[String],
	    startMarket:              UIO[String],
	    stopMarket:               UIO[String]

	)
	val cbs : CrossingBotService = new CBS()

	val allMutations = Mutations(
		args => cbs.catchCreature(args.username, args.species, args.months),
		args => cbs.finalizeUserCreation(args.username, args.id, args.avatar),
		args => cbs.sellOneCreature(args.username, args.species, args.creatureName),
		args => cbs.sellAllCreatures(args.username),
		args => cbs.acknowledgeTransaction(args.username, args.business, args.quantity, args.marketPrice, args.totalBells),
		cbs.populate(),
		cbs.startMarket(),
		cbs.stopMarket()
	)
}
