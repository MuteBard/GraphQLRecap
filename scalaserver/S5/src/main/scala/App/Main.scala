package App

import Actors._
import Helper.Auxiliary._
import Controller.ToCBTC
import akka.actor.{ActorSystem, Props}

object Main {
	implicit val system = ActorSystem("CBAS")
	val bugActor = system.actorOf(Props[BugActor], "BugActor")
	val fishActor = system.actorOf(Props[FishActor], "FishActor")
	val userActor = system.actorOf(Props[UserActor], "UserActor")

	log.info("Main", "CBAS booting up...")
	ToCBTC

}
