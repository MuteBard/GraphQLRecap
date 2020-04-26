package Actors

import java.util.Calendar

import Helper.Auxiliary._
import Dao.MarketOperations
import Model.Day_.Day
import Model.MovementRecord_.MovementRecord
import akka.actor.{Actor, ActorLogging}

object MarketActor {
	case class  Create_New_Movement_Record(hourBlock :Int, quarterBlock : Int)
	case object Read_Latest_Movement_Record_Day
	case object Read_Latest_Movement_Record_Month
	case object Request_Turnip_Price
	case class  Update_Stalks_Purchased(amount : Int, business : String)
	case object Delete_Earliest_Movement_Records
	case object Start_Todays_Market

}

class MarketActor extends Actor with ActorLogging {

	import Actors.MarketActor._

	var todayMarket = Day()
	var dateMarketCreated = ""
	var currentHourBlockId: Int = -1
	var currentQuarterBlockId: Int = -1

	override def receive: Receive = {

		//AUTOMATED
		case Create_New_Movement_Record(newHourBlockId, newQuarterBlockId) =>

			//			log.info(s"[Create_New_Movement_Record] Checking for difference in block ids ($newHourBlockId,$newQuarterBlockId)")
			val suspectMovementRecord = MarketOperations.readLatestMovementRecord()
			val mr =
				if (suspectMovementRecord._id == todayDateId()) { //if this movement record is within today
					suspectMovementRecord
				} else if (suspectMovementRecord._id == "") { //if this is the very first movement record
					MovementRecord()
				} else { //if this is a new day and a previous movement record has a date Id
					MovementRecord(
						_id = suspectMovementRecord._id,
						latestTurnipPrice = suspectMovementRecord.latestTurnipPrice,
						todayHigh = suspectMovementRecord.latestTurnipPrice,
						todayLow = suspectMovementRecord.latestTurnipPrice,
						stalksPurchased = suspectMovementRecord.stalksPurchased,
						turnipPriceHistory = List(suspectMovementRecord.turnipPriceHistory.head)
					)
				}

			if ((currentQuarterBlockId != newQuarterBlockId) || (currentHourBlockId == -1 && currentQuarterBlockId == -1)) {
				//				log.info(s"[Create_New_Movement_Record] Change in block ids found")

				if (dateMarketCreated != todayDateId()) {
					log.info(s"[Create_New_Movement_Record] A new day has been detected ($newHourBlockId,$newQuarterBlockId)")
					log.info(s"[Start_Todays_Market] Generating all block patterns for the day")
					todayMarket = Day().generate()
					dateMarketCreated = todayDateId()
					log.info(s"[Start_Todays_Market] Today's Market: $todayMarket")
				}

				val newTurnipPrice = mr.latestTurnipPrice + todayMarket.getQuarterBlock(newHourBlockId, newQuarterBlockId).change
				val _id = todayDateId()
				val high = Math.max(newTurnipPrice, mr.todayHigh)
				val low = Math.min(newTurnipPrice, mr.todayLow)
				val turnipPriceHistory = newTurnipPrice +: mr.turnipPriceHistory
				val stalksPurchased = mr.stalksPurchased
				val latestHourBlock = todayMarket.getHourBlock(newHourBlockId)
				val latestHourBlockName = todayMarket.getHourBlock(newHourBlockId).name
				val latestQuarterBlock = todayMarket.getQuarterBlock(newHourBlockId, newQuarterBlockId)
				val quarterBlockHistory = todayMarket.getQuarterBlockHistory(newHourBlockId, newQuarterBlockId)
				val monthForMR = month()
				val dayForMR = day()

				val newMr = MovementRecord(_id, newHourBlockId, newQuarterBlockId, high, low, stalksPurchased, newTurnipPrice, turnipPriceHistory, latestHourBlockName, latestHourBlock,
					latestQuarterBlock, quarterBlockHistory, monthForMR, dayForMR
				)

				if ((newHourBlockId == 0 && newQuarterBlockId == 0) || ((currentHourBlockId == -1 && currentQuarterBlockId == -1) && mr._id == "")) {
					log.info(s"[Create_New_Movement_Record] Creating new Movement Record ($newHourBlockId,$newQuarterBlockId)")
					MarketOperations.createMovementRecord(newMr)
				} else {
					log.info(s"[Create_New_Movement_Record] Updating Movement Record")
					MarketOperations.massUpdateMovementRecord(newMr)
				}

				currentHourBlockId = newHourBlockId
				currentQuarterBlockId = newQuarterBlockId
			}
		//AUTOMATED
		case Delete_Earliest_Movement_Records =>
			log.info(s"[Delete_Earliest_Movement_Records] Getting earliest Movement Record")
			val dt = Calendar.getInstance()
			val currentMonth = dt.get(Calendar.MONTH) + 1
			val oldMonth = MarketOperations.readEarliestMovementRecord().month
			if (currentMonth - oldMonth > 2) {
				log.info(s"[Delete_Earliest_Movement_Records] Deleting old Movement Records")
				MarketOperations.deleteOldestMovementRecords(oldMonth)
			}

		case Read_Latest_Movement_Record_Day =>
			log.info(s"[Read_Latest_Movement_Record_Day] Getting latest Movement Record")
			sender() ! MarketOperations.readLatestMovementRecord()

		case Update_Stalks_Purchased(amountBought, business) =>
			log.info(s"[Read_Latest_Movement_Record] Updating total stalks purchased")
			if (business == "sell") {
				val amountSold = amountBought * -1
				MarketOperations.updateStalksPurchased(amountSold)
			} else {
				MarketOperations.updateStalksPurchased(amountBought)
			}

		case Request_Turnip_Price =>
			log.info(s"[Request_Turnip_Price] Getting turnip price")
			sender() ! MarketOperations.readLatestMovementRecord().latestTurnipPrice

		case Read_Latest_Movement_Record_Month =>
			val currentMonth = month()
			log.info(s"[Read_Latest_Movement_Record_Month] Getting all movement records")
			sender() ! MarketOperations.readMovementRecordListByMonth(currentMonth)
	}
}