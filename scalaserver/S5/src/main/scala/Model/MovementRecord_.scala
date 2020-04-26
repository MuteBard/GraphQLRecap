package Model

import Model.HourBlock_.HourBlock
import Model.QuarterBlock_.QuarterBlock

//import Data.Market.MarketHourBlock.{HourBlock, HourBlockJsonProtocol}
//import Data.Market.MarketQuarterBlock.{QuarterBlock, QuarterBlockJsonProtocol}

object MovementRecord_ {
	case class MovementRecord(
		 _id: String = "",
		 hourBlockId: Int = 0,
		 quarterBlockId: Int = 0,
		 todayHigh: Int = 1000,
		 todayLow: Int = 1000,
		 stalksPurchased: Int = 0,
		 latestTurnipPrice: Int = 1000,
		 turnipPriceHistory: List[Int] = List(1000),
		 hourBlockName: String = "",
		 latestHourBlock: HourBlock = null,
		 latestQuarterBlock: QuarterBlock = null,
		 quarterBlockHistory: List[QuarterBlock] = List(),
		 month: Int = 0,
		 day: Int = 0,
	)
}

