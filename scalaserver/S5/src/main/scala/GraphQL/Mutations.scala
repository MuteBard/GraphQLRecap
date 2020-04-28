package GraphQL
import Model.TurnipTransaction_._
import Model.User_._
import Service._
import zio.{IO, UIO}

object Mutations {

	case class Mutations(
	    //User
	    catchCreature : catchCreatureArgs =>                                IO[NotFound, String],
	    finalizeUserCreation : finalizeUserArgs =>                            IO[NotFound, String],
	    acknowledgeTransaction: authorizedTransactionArgs =>                IO[NotFound, String],
	)
	val cbs : CrossingBotService = new CBS()

	val allMutations = Mutations(
		args => cbs.catchCreature(args.username, args.species, args.months),
		args => cbs.finalizeUserCreation(args.username, args.id, args.avatar),
		args => cbs.acknowledgeTransaction(args.username, args.business, args.quantity, args.marketPrice, args.totalBells)
	)
}
