package GraphQL
import Actors.{BugActor, FishActor}
import Model.User_._
import Model.Bug_._
import Model.Fish_._
import Model.Pocket_._
import App.Main._
import zio.{IO, UIO}
import akka.pattern.ask
import akka.util.Timeout

import scala.concurrent.Await
import scala.concurrent.duration._

object Service {

	trait CrossingBotService {
		//User
//		def getUser:                                        UIO[User]
//		def catchFish:                                      UIO[User]
//		def catchBug:                                       UIO[User]

		//Bug
		def getAllBugs:                                     UIO[List[Bug]]
		def getAllBugsByMonth(months : List[String]):       UIO[List[Bug]]
		def getAllRareBugsByMonth(months : List[String]):   UIO[List[Bug]]
		def getBugById(bugId : String):                     UIO[Bug]
		def getBugByName(name : String):                    UIO[Bug]
		def getBugByRandom(months : List[String]):          UIO[Bug]

		//Fish
		def getAllFishes:                                   UIO[List[Fish]]
		def getAllFishesByMonth(months : List[String]):     UIO[List[Fish]]
		def getAllRareFishesByMonth(months : List[String]): UIO[List[Fish]]
		def getFishById(bugId : String):                    UIO[Fish]
		def getFishByName(name : String):                   UIO[Fish]
		def getFishByRandom(months : List[String]):         UIO[Fish]

	}

	class CBS extends CrossingBotService{
		implicit val timeout = Timeout(5 seconds)
		//User

		//Bug
		def getAllBugs : UIO[List[Bug]] = {
			val allBugs = Await.result((bugActor ? BugActor.Read_Bug_All).mapTo[List[Bug]], 2 seconds)
			IO.succeed(allBugs)
		}
		def getAllBugsByMonth(months : List[String]) : UIO[List[Bug]] = {
			val allBugs = Await.result((bugActor ? BugActor.Read_All_Bug_By_Month(months)).mapTo[List[Bug]], 2 seconds)
			IO.succeed(allBugs)
		}
		def getAllRareBugsByMonth(months : List[String]) : UIO[List[Bug]] = {
			val allBugs = Await.result((bugActor ? BugActor.Read_All_Rarest_Bug_By_Month(months)).mapTo[List[Bug]], 2 seconds)
			IO.succeed(allBugs)
		}
		def getBugById(bugId: String): UIO[Bug] = {
			val bug = Await.result((bugActor ? BugActor.Read_One_Bug_By_Id(bugId)).mapTo[Bug], 2 seconds)
			IO.succeed(bug)
		}
		def getBugByName(name : String): UIO[Bug] = {
			val bug = Await.result((bugActor ? BugActor.Read_One_Bug_By_Name(name)).mapTo[Bug], 2 seconds)
			IO.succeed(bug)
		}
		def getBugByRandom(months : List[String]): UIO[Bug] = {
			val bug = Await.result((bugActor ? BugActor.Read_One_Bug_By_Random(months)).mapTo[Bug], 2 seconds)
			IO.succeed(bug)
		}


		//Fish
		def getAllFishes : UIO[List[Fish]] = {
			val allFishes = Await.result((fishActor ? FishActor.Read_Fish_All).mapTo[List[Fish]], 2 seconds)
			IO.succeed(allFishes)
		}
		def getAllFishesByMonth(months : List[String]) : UIO[List[Fish]] = {
			val allFishes = Await.result((fishActor ? FishActor.Read_All_Fish_By_Month(months)).mapTo[List[Fish]], 2 seconds)
			IO.succeed(allFishes)
		}
		def getAllRareFishesByMonth(months : List[String]) : UIO[List[Fish]] = {
			val allFishes = Await.result((fishActor ? FishActor.Read_All_Rarest_Fish_By_Month(months)).mapTo[List[Fish]], 2 seconds)
			IO.succeed(allFishes)
		}
		def getFishById(fishId: String): UIO[Fish] = {
			val fish = Await.result((fishActor ? FishActor.Read_One_Fish_By_Id(fishId)).mapTo[Fish], 2 seconds)
			IO.succeed(fish)
		}
		def getFishByName(name : String): UIO[Fish] = {
			val fish = Await.result((fishActor ? FishActor.Read_One_Fish_By_Name(name)).mapTo[Fish], 2 seconds)
			IO.succeed(fish)
		}
		def getFishByRandom(months : List[String]): UIO[Fish] = {
			val fish = Await.result((fishActor ? FishActor.Read_One_Fish_By_Random(months)).mapTo[Fish], 2 seconds)
			IO.succeed(fish)
		}
	}

}
