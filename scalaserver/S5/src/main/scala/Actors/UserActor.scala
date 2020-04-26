package Actors

import Dao.UserOperations
import App.Main.bugActor
import App.Main.fishActor
import Model.Bug_._
import Model.Fish_._
import Model.User_._
import akka.actor.{Actor, ActorLogging}
import akka.util.Timeout

import scala.concurrent.Await
import scala.concurrent.duration._
import akka.pattern.ask

object UserActor {
	case class Create_One_User(user : User )
	case class Read_One_User(username : String)
	case class Update_One_User_With_Creature(user : User)
	case class Delete_One_Creature_From_Pocket(selling : sellCreatureArgs)
	case class Delete_All_Creature_From_Pocket(selling : sellCreatureArgs)
}

class UserActor extends Actor with ActorLogging{
	final val BUG = "bug"
	final val FISH = "fish"
	import UserActor._
	implicit val timeout = Timeout(5 seconds)
	override def receive: Receive = {

		case Create_One_User(user) =>
			log.info(s"[Create_One_User] Inserting USER in database with new creature in pocket")
			UserOperations.createOneUser(user)

		case Read_One_User(username) =>
			log.info(s"[Read_One_User] Getting USER with username $username")

			val userSeq = UserOperations.readOneUser(username)
			val userExists = userSeq.nonEmpty

			if (userExists) {
				log.info(s"[Read_One_User] USER $username found")
				sender() ! userSeq.head
			} else {
				log.info(s"[Read_One_User] USER $username does not exist")
				sender() ! User()
			}


		//TODO This might need to refactoring
		case Update_One_User_With_Creature(user) =>
			log.info(s"[Update_One_User_With_Creature] Adding BUG/FISH to potential ${user.username}'s pocket")
			UserOperations.updateUserPocket(user)
			log.info(s"[Update_One_User_With_Creature] Verifying if USER with username ${user.username} exists")
			val exists = UserOperations.readOneUser(user.username).length == 1
			if (exists){
				log.info("[Update_One_User_With_Creature] USER exists")
			}else
				log.info("[Update_One_User_With_Creature] USER does not exist")
			sender() ! exists

		case Delete_One_Creature_From_Pocket(selling) =>
			log.info(s"[Delete_One_Creature_From_Pocket] Selling and deleting ${selling.creaturename} in ${selling.username}'s pocket")
			if (selling.species == BUG){
				val bells =  Await.result((bugActor ? BugActor.Read_One_Bug_By_Name(selling.creaturename)).mapTo[Bug], 3 seconds).bells
				UserOperations.deleteOneForUser(selling, bells)
				sender() ! bells
			}else if (selling.species == FISH){
				val bells = Await.result((fishActor ? FishActor.Read_One_Fish_By_Name(selling.creaturename)).mapTo[Fish], 3 seconds).bells
				UserOperations.deleteOneForUser(selling, bells)
				sender() ! bells
			}

		//Todo remove comments
		case Delete_All_Creature_From_Pocket(selling) =>
			log.info(s"[Delete_All_Creature_From_Pocket] Selling and deleting all creatures from ${selling.username}'s pocket")
			//			val bugBells =  Await.result((bugActor ? BugActor.Read_One_By_Name(selling.creature)).mapTo[Bug], 3 seconds).bells
			//			val fishBells = Await.result((fishActor ? FishActor.Read_One_By_Name(selling.creature)).mapTo[Fish], 3 seconds).bells
			//			val creatureBells = bugBells + fishBells
			sender() ! UserOperations.deleteAllForUser(selling)
	}
}