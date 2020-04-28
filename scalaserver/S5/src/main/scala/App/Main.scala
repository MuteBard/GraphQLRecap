package App

import Actors._
import Helper.Auxiliary._
import Controller.ToCBTC
import akka.actor.{ActorRef, ActorSystem, Props}
import akka.pattern.ask
import akka.util.Timeout
import scala.concurrent.duration._

object Main {
	implicit val system: ActorSystem = ActorSystem("CBAS")
	val bugActor: ActorRef = system.actorOf(Props[BugActor], "BugActor")
	val fishActor: ActorRef = system.actorOf(Props[FishActor], "FishActor")
	val userActor: ActorRef = system.actorOf(Props[UserActor], "UserActor")
	val marketActor: ActorRef = system.actorOf(Props[MarketActor], "MarketActor")
	val startActor: ActorRef = system.actorOf(Props[StartActor], "StartActor")
//	implicit val timeout = Timeout(5 seconds)
//	startActor ? StartActor.Create_Creatures_All
	log.info("Main", "CBAS booting up...")

	ToCBTC

}
