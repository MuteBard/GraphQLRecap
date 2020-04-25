package GraphQL
import Model.User_._
import Model.Bug_._
import Service._
import zio.UIO

object Queries {

	case class Queries(
		getUser : UIO[User],
		getBugBaseOnMonth : monthArgs => UIO[Bug]
	)

	val cbs : CrossingBotService = new CBS()

	val allQueries = Queries(
		cbs.getUser,
		args => cbs.getBugBaseOnMonth(args.month)
	)
}
