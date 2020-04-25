package GraphQL
import Service._
import Model.User_._
import zio.UIO

object Mutations {

	case class Mutations(
	  catchFish : UIO[User]
	)

	val cbs : CrossingBotService = new CBS()

	val allMutations = Mutations(cbs.catchFish)
}
