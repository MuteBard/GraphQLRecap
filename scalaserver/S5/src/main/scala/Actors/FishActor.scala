package Actors

import Dao.FishOperations
import Model.Fish_.Fish
import akka.actor.{Actor, ActorLogging}

import scala.util.Random

object FishActor {
	case object Read_Fish_All
	case class Read_All_Fish_By_Month(month : List[String])
	case class Read_All_Rarest_Fish_By_Month(month : List[String])
	case class Read_One_Fish_By_Id(fishId : String)
	case class Read_One_Fish_By_Name(name : String)
	case class Read_One_Fish_By_Random(month : List[String])

}

class FishActor extends Actor with ActorLogging{
	import FishActor._

	override def receive: Receive = {
		case Read_Fish_All =>
			log.info("[Read_Bug_All] Selecting all FISH")
			sender() ! FishOperations.readAll()

		case Read_One_Fish_By_Random(month : List[String]) =>
			log.info(s"[Read_One_Fish_By_Random] Selecting FISH by random")
			val fish = FishOperations.readOneByRarityAndMonth(rarityValue, month)
			log.info(s"[Read_One_Fish_By_Random] Found FISH ${fish.name}")
			sender() ! fish

		case Read_One_Fish_By_Id(fId : String) =>
			log.info(s"[Read_One_Fish_By_Id] Selecting FISH with id : $fId")
			val fishSeq = FishOperations.readOneById(fId)
			val fishExists = fishSeq.nonEmpty
			if(fishExists){
				log.info(s"[Read_One_Fish_By_Id] FISH with id $fId found")
				sender() ! fishSeq.head
			}else {
				log.info(s"[Read_One_Fish_By_Id] FISH with id $fId does not exist")
				sender() ! Fish()
			}

		case Read_One_Fish_By_Name(name : String) =>
			log.info(s"[Read_One_Fish_By_Name] Selecting FISH with name : $name")
			val fishSeq = FishOperations.readOneByName(name)
			val fishExists = fishSeq.nonEmpty
			if(fishExists){
				log.info(s"[Read_One_Fish_By_Name] FISH with name $name found")
				sender() ! fishSeq.head
			}else {
				log.info(s"[Read_One_Fish_By_Name] FISH with name $name does not exist")
				sender() ! Fish()
			}

		case Read_All_Fish_By_Month(month : List[String]) =>
			log.info(s"[Read_All_Fish_By_Month] Selecting FISH based on month(s) provided")
			sender() ! FishOperations.readAllByMonth(month)

		case Read_All_Rarest_Fish_By_Month(month : List[String]) =>
			log.info(s"[Read_All_Rarest_Fish_By_Month] Selecting FISH based on rarity")
			sender() ! FishOperations.readAllRarestByMonth(month)

	}

	def rarityValue : Int = {
		val random = new Random()
		val chance = random.nextInt(512)+1
		if(chance % 500 == 0) 5
		else if (chance % 80 == 0) 4
		else if(chance % 20 == 0) 3
		else if(chance % 3 == 0) 2
		else 1
	}
}
