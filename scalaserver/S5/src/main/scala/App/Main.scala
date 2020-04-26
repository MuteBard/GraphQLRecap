package App

import Actors._
import Helper.Auxiliary._
import Controller.ToCBTC
import akka.actor.{ActorRef, ActorSystem, Props}

object Main {
	implicit val system: ActorSystem = ActorSystem("CBAS")
	val bugActor: ActorRef = system.actorOf(Props[BugActor], "BugActor")
	val fishActor: ActorRef = system.actorOf(Props[FishActor], "FishActor")
	val userActor: ActorRef = system.actorOf(Props[UserActor], "UserActor")
	val marketActor: ActorRef = system.actorOf(Props[MarketActor], "MarketActor")

	log.info("Main", "CBAS booting up...")
	ToCBTC

}
