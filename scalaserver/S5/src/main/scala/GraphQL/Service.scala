package GraphQL
import Actors.{BugActor, FishActor, MarketActor, StartActor, UserActor}
import Model.User_._
import Model.Bug_._
import Model.Fish_._
import App.Main._
import Model.MovementRecord_.MovementRecord
import Model.TurnipTransaction_.TurnipTransaction
import zio.{IO, Queue, UIO}
import akka.pattern.ask
import akka.util.Timeout
import zio.stream.ZStream

import scala.concurrent.Await
import scala.concurrent.duration._

object Service {

	case class NotFound(message: String) extends Throwable
	case class NotValid(message: String) extends Throwable
	trait CrossingBotService {

		//--User--
    	//Queries
		def getUser(username : String):                     IO[NotFound, User]
		def validatePendingTransaction(
			username : String,
			business : String,
			quantity : Int
		):                                                  UIO[TurnipTransaction]

		//--MovementRecord--
		//Queries
		def getDayRecords:                                  UIO[MovementRecord]
		def getMonthRecords:                                UIO[List[MovementRecord]]
		def getTurnipPrices:                                UIO[Int]


		//--Bug--
    	//Queries
		def getAllBugs:                                     UIO[List[Bug]]
		def getAllBugsByMonth(months : List[String]):       IO[NotFound, List[Bug]]
		def getAllRareBugsByMonth(months : List[String]):   IO[NotFound, List[Bug]]
		def getBugById(bugId : String):                     IO[NotFound, Bug]
		def getBugByName(name : String):                    IO[NotFound, Bug]
		def getBugByRandom(months : List[String]):          IO[NotFound, Bug]

		//--Fish--
		//Queries
		def getAllFishes:                                   UIO[List[Fish]]
		def getAllFishesByMonth(months : List[String]):     IO[NotFound, List[Fish]]
		def getAllRareFishesByMonth(months : List[String]): IO[NotFound, List[Fish]]
		def getFishById(bugId : String):                    IO[NotFound, Fish]
		def getFishByName(name : String):                   IO[NotFound, Fish]
		def getFishByRandom(months : List[String]):         IO[NotFound, Fish]

		//Mutations
		//--Start--
		def populate():                                     UIO[String]
		def startMarket():                                  UIO[String]
		def stopMarket():                                   UIO[String]

		def catchCreature(
			 username: String,
			 species : String,
			 months : List[String]
		):                                                  IO[NotFound, String]
		def finalizeUserCreation(
			username: String,
			id : Int,
			avatar : String
		):                                                  IO[NotFound, String]

		def acknowledgeTransaction(
			username : String,
			business : String,
			quantity : Int,
			marketPrice: Int,
			totalBells: Int
		):                                                  IO[NotFound, String]

		def sellOneCreature(
		    username: String,
		    species : String,
		    creatureName : String
		):                                                  UIO[Int]

		def sellAllCreatures(username : String):            UIO[Int]
	}

	class CBS extends CrossingBotService{
		implicit val timeout = Timeout(5 seconds)
		var num = 0
		//--User--
		//Queries
		def getUser(username : String) : IO[NotFound, User] = {
			val user = Await.result((userActor ? UserActor.Read_One_User(username)).mapTo[User], 2 seconds)
			if(user.id != -2) IO.succeed(user)
			else IO.fail(NotFound(""))
		}
		def validatePendingTransaction(username: String, business : String, quantity : Int) : UIO[TurnipTransaction] = {
			val turnipTransaction = Await.result((userActor ? UserActor.Read_One_User_With_Pending_Turnip_Transaction(username, business, quantity)).mapTo[TurnipTransaction], 2 seconds)
			IO.succeed(turnipTransaction)
		}
		//--MovementRecord--

		def getDayRecords: UIO[MovementRecord] = {
			val movementRecord = Await.result((marketActor ? MarketActor.Read_Latest_Movement_Record_Day).mapTo[MovementRecord], 2 seconds)
			IO.succeed(movementRecord)
		}

		def getMonthRecords: UIO[List[MovementRecord]] = {
			val movementRecordsForMonth = Await.result((marketActor ? MarketActor.Read_Latest_Movement_Record_Month).mapTo[List[MovementRecord]], 2 seconds)
			IO.succeed(movementRecordsForMonth)
		}

		def getTurnipPrices: UIO[Int] = {
			val turnips = Await.result((marketActor ? MarketActor.Request_Turnip_Price).mapTo[Int], 2 seconds)
			IO.succeed(turnips)
		}

		//--Bug--
		def getAllBugs : UIO[List[Bug]] = {
			val allBugs = Await.result((bugActor ? BugActor.Read_Bug_All).mapTo[List[Bug]], 2 seconds)
			IO.succeed(allBugs)
		}
		def getAllBugsByMonth(months : List[String]) : IO[NotFound, List[Bug]] = {
			val allBugs = Await.result((bugActor ? BugActor.Read_All_Bug_By_Month(months)).mapTo[List[Bug]], 2 seconds)
			if(allBugs.nonEmpty) IO.succeed(allBugs)
			else IO.fail(NotFound(""))
		}
		def getAllRareBugsByMonth(months : List[String]) : IO[NotFound, List[Bug]] = {
			val allBugs = Await.result((bugActor ? BugActor.Read_All_Rarest_Bug_By_Month(months)).mapTo[List[Bug]], 2 seconds)
			if(allBugs.nonEmpty) IO.succeed(allBugs)
			else IO.fail(NotFound(""))
		}
		def getBugById(bugId: String): IO[NotFound, Bug] = {
			val bug = Await.result((bugActor ? BugActor.Read_One_Bug_By_Id(bugId)).mapTo[Bug], 2 seconds)
			if(bug.id != -1) IO.succeed(bug)
			else IO.fail(NotFound(""))
		}
		def getBugByName(name : String): IO[NotFound, Bug] = {
			val bug = Await.result((bugActor ? BugActor.Read_One_Bug_By_Name(name)).mapTo[Bug], 2 seconds)
			if(bug.id != -1) IO.succeed(bug)
			else IO.fail(NotFound(""))
		}
		def getBugByRandom(months : List[String]): IO[NotFound, Bug] = {
			val bug = Await.result((bugActor ? BugActor.Read_One_Bug_By_Random(months)).mapTo[Bug], 2 seconds)
			if(bug.id != -1) IO.succeed(bug)
			else IO.fail(NotFound(""))
		}


		//--Fish--
		def getAllFishes : UIO[List[Fish]] = {
			val allFishes = Await.result((fishActor ? FishActor.Read_Fish_All).mapTo[List[Fish]], 2 seconds)
			IO.succeed(allFishes)
		}
		def getAllFishesByMonth(months : List[String]) : IO[NotFound, List[Fish]] = {
			val allFishes = Await.result((fishActor ? FishActor.Read_All_Fish_By_Month(months)).mapTo[List[Fish]], 2 seconds)
			if(allFishes.nonEmpty) IO.succeed(allFishes)
			else IO.fail(NotFound(""))
		}
		def getAllRareFishesByMonth(months : List[String]) : IO[NotFound, List[Fish]] = {
			val allFishes = Await.result((fishActor ? FishActor.Read_All_Rarest_Fish_By_Month(months)).mapTo[List[Fish]], 2 seconds)
			if(allFishes.nonEmpty) IO.succeed(allFishes)
			else IO.fail(NotFound(""))
		}
		def getFishById(fishId: String): IO[NotFound, Fish] = {
			val fish = Await.result((fishActor ? FishActor.Read_One_Fish_By_Id(fishId)).mapTo[Fish], 2 seconds)
			if(fish.id != -1) IO.succeed(fish)
			else IO.fail(NotFound(""))
		}
		def getFishByName(name : String): IO[NotFound, Fish] = {
			val fish = Await.result((fishActor ? FishActor.Read_One_Fish_By_Name(name)).mapTo[Fish], 2 seconds)
			if(fish.id != -1) IO.succeed(fish)
			else IO.fail(NotFound(""))
		}
		def getFishByRandom(months : List[String]): IO[NotFound, Fish] = {
			val fish = Await.result((fishActor ? FishActor.Read_One_Fish_By_Random(months)).mapTo[Fish], 2 seconds)
			if(fish.id != -1) IO.succeed(fish)
			else IO.fail(NotFound(""))
		}
		//Mutations
		def catchCreature(username: String, species: String, months: List[String]): IO[NotFound, String] = {
			val status = Await.result((userActor ? UserActor.Update_One_User_With_Creature(username, species, months)).mapTo[String], 4 seconds)
			if(status == "Success"){
				IO.succeed("Success")
			}else{
				IO.fail(NotFound(""))
			}
		}

		def finalizeUserCreation(username: String, id: Int, avatar: String): IO[NotFound, String] = {
			val status = Await.result((userActor ? UserActor.FinalizeUserCreation(username, id, avatar)).mapTo[String], 4 seconds)
			if(status == "Success"){
				IO.succeed("Success")
			}else{
				IO.fail(NotFound(""))
			}

		}

		def acknowledgeTransaction(username : String, business: String, quantity : Int, marketPrice: Int, totalBells: Int) : IO[NotFound, String] = {
			val status = Await.result((userActor ? UserActor.Update_One_User_With_Executing_Turnip_Transaction(username, business, quantity, marketPrice, totalBells)).mapTo[String], 3 seconds)
			if(status == "Success"){
				IO.succeed("Success")
			}else{
				IO.fail(NotFound(""))
			}
		}

		def sellOneCreature(username: String, species : String, creatureName : String): UIO[Int] = {
			val bells = Await.result((userActor ? UserActor.Delete_One_Creature_From_Pocket(username, species, creatureName)).mapTo[Int], 3 seconds)
			IO.succeed(bells)

		}

		def sellAllCreatures(username : String): UIO[Int] = {
			val bells = Await.result((userActor ? UserActor.Delete_All_Creatures_From_Pocket(username)).mapTo[Int], 3 seconds)
			IO.succeed(bells)
		}

		def populate() : UIO[String] = {
			val status = Await.result((startActor ? StartActor.Create_Creatures_All).mapTo[String], 3 seconds)
			IO.succeed(status)
		}

		def startMarket() : UIO[String] = {
			val status = Await.result((startActor ? StartActor.Start_Market_Timers).mapTo[String], 3 seconds)
			IO.succeed(status)
		}

		def stopMarket() : UIO[String] = {
			val status = Await.result((startActor ? StartActor.Stop_Market_Timers).mapTo[String], 3 seconds)
			IO.succeed(status)
		}

	}
}
