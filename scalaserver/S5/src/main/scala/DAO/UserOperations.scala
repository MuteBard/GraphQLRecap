
package Dao
import Helper.Auxiliary.log
import Model.Fish_.Fish
import akka.stream.alpakka.mongodb.scaladsl.{MongoSink, MongoSource}
import akka.stream.scaladsl.{Flow, Sink, Source}
import org.bson.codecs.configuration.CodecRegistries.{fromProviders, fromRegistries}
import org.mongodb.scala.MongoClient.DEFAULT_CODEC_REGISTRY
import org.mongodb.scala.bson.codecs.Macros._
import akka.stream.alpakka.mongodb.DocumentUpdate
import org.mongodb.scala.model.{Filters, Updates}

import scala.util.{Failure, Success}
import App.Main.system
import Model.Bug_.Bug
import Model.TurnipTransaction_.TurnipTransaction
import Model.User_._
import Model.Pocket_.Pocket
import scala.concurrent.duration._
import scala.concurrent.Await
import system.dispatcher

object UserOperations extends MongoDBOperations {
	val codecRegistryUser = fromRegistries(fromProviders(classOf[User], classOf[TurnipTransaction],classOf[Pocket], classOf[Bug], classOf[Fish]), DEFAULT_CODEC_REGISTRY)
	val codecRegistryPocket = fromRegistries(fromProviders(classOf[User],classOf[Pocket], classOf[Bug], classOf[Fish]), DEFAULT_CODEC_REGISTRY)

	private val allUsers = db
		.getCollection("user", classOf[User])
		.withCodecRegistry(codecRegistryUser)

	def createOneUser(user : User): Unit = {
		val source = Source(List(user))
		val taskFuture = source.runWith(MongoSink.insertOne(allUsers))
		taskFuture.onComplete{
			case Success(_) => log.info("UserOperations","createOneUser","Success",s"Added USER ${user.username}")
			case Failure (ex) => log.warn("UserOperations","createOneUser","Failure",s"Failed to create USER: $ex")
		}
	}

	def readOneUser(username : String): Seq[User] = {
		val source = MongoSource(allUsers.find(classOf[User])).filter(users => users.username == username)
		val userSeqFuture = source.runWith(Sink.seq)
		val userSeq : Seq[User] = Await.result(userSeqFuture, 1 seconds)
		userSeq
	}

	def updateUserPocket(data : User) : Unit = {
		val pocketKey = getPocketKey(data)
		val source = MongoSource(allUsers.find(classOf[User]))
			.map(user => {
				val updatedPocket = newPocket(pocketKey, user.pocket, data.pocket)
				DocumentUpdate(filter = Filters.eq("username", data.username), update = Updates.set("pocket", updatedPocket))
			})
		val taskFuture = source.runWith(MongoSink.updateOne(allUsers))
		taskFuture.onComplete{
			case Success(_) =>
				if(readOneUser(data.username).nonEmpty)
					log.info("UserOperations","updateUserPocket","True Success",s"Updated ${data.username}'s pocket successfully")
				else {
					log.warn("UserOperations","updateUserPocket","Partial Failure",s"Failed to properly update, user ${data.username}'s does not exist. Creating new user")
				}
			case Failure (ex) =>
				log.warn("UserOperations","updateUserPocket","Failure",s"Failed update: $ex")
		}
	}


	def deleteOneForUser(data : sellCreatureArgs, creatureBells : Int): Unit = {
		if (creatureBells != 0) {
			val userList: List[User] = readOneUser(data.username).toList
			val user: User = userList.head
			val source = Source(userList).map(_ => Filters.eq("username", data.username))
			val taskFuture = source.runWith(MongoSink.deleteOne(allUsers))
			taskFuture.onComplete {
				case Success(_) =>
					val	bug = user.pocket.bug.filter(creature => creature.name != data.creaturename)
					val	fish = user.pocket.fish.filter(creature => creature.name != data.creaturename)
					val updatedPocket = Pocket(bug, fish)
					val updatedBells = user.bells + creatureBells
					val newUser = User(user._id, user.username, user.fishingPoleLvl, user.bugNetLvl, updatedBells, updatedPocket, user.liveTurnips, user.turnipTransactionHistory, user.img)
					createOneUser(newUser)
				case Failure(ex) =>
					log.warn("UserOperations", "deleteOneForUser", "Failure", s"Failed to delete one USER: $ex")
			}
		}else{
			log.warn("UserOperations", "deleteOneForUser", "Failure", s"Failed to delete one USER: Creature did not exist")
		}
	}

	def deleteAllForUser(data : sellCreatureArgs): Int = {
		val userList: List[User] = readOneUser(data.username).toList
		val user: User = userList.head
		val bugBells = Await.result(Source(user.pocket.bug).via(Flow[Bug].fold[Int](0)(_ + _.bells)).runWith(Sink.head), 1 second)
		val fishBells = Await.result(Source(user.pocket.fish).via(Flow[Fish].fold[Int](0)(_ + _.bells)).runWith(Sink.head), 1 second)
		val creatureBells = bugBells + fishBells
		if (creatureBells != 0) {
			val source = Source(userList).map(_ => Filters.eq("username", data.username))
			val taskFuture = source.runWith(MongoSink.deleteMany(allUsers))
			taskFuture.onComplete {
				case Success(_) =>
					log.info("UserOperations", "deleteAllForUser", "Success", s"Deleted USER ${data.username}")
					val bug : List[Bug] = List()
					val fish : List[Fish] = List()
					val updatedPocket = Pocket(bug, fish)
					val updatedBells = user.bells + creatureBells
					val newUser = User(user._id, user.username, user.fishingPoleLvl, user.bugNetLvl, updatedBells, updatedPocket, user.liveTurnips , user.turnipTransactionHistory, user.img)
					createOneUser(newUser)
				case Failure(ex) =>
					log.warn("UserOperations", "deleteAllForUser", "Failure", s"Failed to delete one USER: $ex")
			}
		}else{
			log.warn("UserOperations", "deleteAllForUser", "Failure", s"Failed to delete one USER: Nothing in pocket")

		}
		creatureBells
	}

	def newPocket(species : String, databasePocket: Pocket , queryPocket: Pocket): Pocket = {
		if(species == "bug"){
			val newBugList = databasePocket.bug :+ queryPocket.bug.head
			Pocket(newBugList,databasePocket.fish)
		} else {
			val newFishList = databasePocket.fish :+ queryPocket.fish.head
			Pocket(databasePocket.bug, newFishList)
		}
	}

	def getPocketKey(data: User): String = {
		if (data.pocket.bug.nonEmpty) {
			"bug"
		} else {
			"fish"
		}
	}
}