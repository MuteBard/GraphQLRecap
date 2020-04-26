package Model

import Model.HourBlock_.HourBlock
import Model.QuarterBlock_.QuarterBlock
import scala.util.Random

object Day_ {

	case class Day(hourBlocks : List[HourBlock] = null){
		def generate() : Day = {
			val hour = HourBlock()
			val random = new Random
			val hourBlocks = (0 to 23).map(hourBlockId => {
				val value = random.nextInt(10) + 1
				value match {
					case x if (1 to 3).contains(x) => hour.sleepy(hourBlockId)
					case x if (4 to 5).contains(x) =>  hour.normal(hourBlockId)
					case x if (6 to 8).contains(x) => hour.good(hourBlockId)
					case 9 => hour.bad(hourBlockId)
					case 10 => hour.risky(hourBlockId)
				}
			}).toList
			Day(hourBlocks)
		}

		def getHourBlock(index : Int): HourBlock = {
			hourBlocks(index)
		}

		def getQuarterBlock(indexH : Int, indexQ : Int) : QuarterBlock = {
			hourBlocks(indexH).quarterBlocks(indexQ)
		}

		def getHourBlockHistory(index : Int) : List[HourBlock] = {
			hourBlocks.slice(0, index + 1)
		}

		//(0,0) represents List(x)
		//(0,1) represents List(x,x)
		//(0,2) represents List(x,x,x)
		//(0,3) represents List(x,x,x,x)
		//(1,0) represents List(x,x,x,x,x)
		//(23,3) is max with List().length == 96

		def getQuarterBlockHistory(indexH : Int, indexQ : Int) : List[QuarterBlock] = {
			val trueIndexH = indexH + 1
			val trueIndexQ = (indexH * 4 + indexQ) + 1
			getHourBlockHistory(trueIndexH).flatMap(hour => hour.quarterBlocks).slice(0, trueIndexQ)
		}
	}
}