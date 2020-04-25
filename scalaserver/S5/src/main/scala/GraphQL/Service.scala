package GraphQL
import Model.User_._
import Model.Bug_._
import Model.Fish_._
import Model.Pocket_._
import zio.{IO, UIO}

object Service {

	trait CrossingBotService {
		def getUser: UIO[User]
		def getBugBaseOnMonth(month : String): UIO[Bug]
		def catchFish : UIO[User]
	}

	class CBS extends CrossingBotService{
		def getUser() : UIO[User] = {
			val Agrias_Butterfly = Bug(7, "B7", "Agrias Butterfly", 3000, List("JUN","JUL","AUG","SEP"), 4 ,"Agrias_Butterfly_HHD_Icon.png")
			val pocket = Pocket(List(Agrias_Butterfly), List())
			IO.succeed(User(750,"MuteBard",0,0,3000,pocket))
		}
		def getBugBaseOnMonth(month: String): UIO[Bug] = {
			val Agrias_Butterfly = Bug(7, "B7", "Agrias Butterfly", 3000, List("JUN","JUL","AUG","SEP"), 4 ,"Agrias_Butterfly_HHD_Icon.png")
			IO.succeed(Agrias_Butterfly)
		}

		def catchFish(): UIO[User] = {
			val Agrias_Butterfly = Bug(7, "B7", "Agrias Butterfly", 3000, List("JUN","JUL","AUG","SEP"), 4 ,"Agrias_Butterfly_HHD_Icon.png")
			val CrawFish = Fish(11, "F11", "Crawfish", 200, List("APR","MAY","JUN","JUL","AUG","SEP"), 2, "Crawfish_HHD_Icon.png")
			val pocket = Pocket(List(Agrias_Butterfly), CrawFish +: List())
			IO.succeed(User(750,"MuteBard",0,0,3000,pocket))
		}
	}

}
