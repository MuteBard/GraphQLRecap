package gql_example

import java.net.URL

import zio.{IO, Runtime, UIO, ZEnv}
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import caliban.interop.circe.AkkaHttpCirceAdapter

import scala.concurrent.ExecutionContextExecutor
import scala.io.StdIn
import scala.util.Try


object gqlServer extends App with AkkaHttpCirceAdapter{

	implicit val system = ActorSystem("CBAS")
	implicit val executionContext: ExecutionContextExecutor = system.dispatcher
	implicit val runtime: Runtime[ZEnv] = Runtime.default

	case class Bug(
		_id : Int = 7,
		bugId : String = "B7",
		name : String = "Agrias Butterfly",
		bells : Int = 3000,
		availability : List[String] = List("JUN","JUL","AUG","SEP"),
		rarity : Int = 4,
		img : String = "Agrias_Butterfly_HHD_Icon.png"
	)

	case class Fish(
		_id : Int = 0,
		fishId : String = "",
		name : String = "NULL/FISH",
		bells : Int = 0,
		availability : List[String] = List(),
		rarity : Int = 0,
		img : String = ""
	)

	case class Pocket(
		 bug : List[Bug] = List(Bug()),
		 fish : List[Fish] = List()
	)



	case class TurnipTransaction(
		business: String = "",
		quantity: Int = 0,
		marketPrice : Int = 0,
		totalBells: Int = 0,
		netGainLossAsBells : Int = 0,
		netGainLossAsPercentage: Int = 0
	)


	case class User(
		_id : Int = 0,
		username : String = "Mutebard",
		fishingPoleLvl : Int = 0,
		bugNetLvl : Int = 0,
		bells : Int = 0,
		pocket : Pocket = Pocket(),
		liveTurnips : TurnipTransaction = TurnipTransaction(),
		turnipTransactionHistory : List[TurnipTransaction] = List(),
		img : String = "",
	)

	case class monthArgs(month : String)

	trait CrossingBotService {
		def getUser: UIO[User]
		def getBugBaseOnMonth(month : String): UIO[Bug]
	}

	case class Queries(
		getUser : UIO[User],
		getBugBaseOnMonth : monthArgs => UIO[Bug]
	)

	class CBS extends CrossingBotService{
		def getUser() : UIO[User] =  IO.succeed(User())
		def getBugBaseOnMonth(month: String): UIO[Bug] = IO.succeed(Bug())
	}


//	val cbs : CrossingBotService = ???
	val cbs : CrossingBotService = new CBS()


	val queries = Queries(
		cbs.getUser,
		args => cbs.getBugBaseOnMonth(args.month)
	)


	import caliban.GraphQL.graphQL
	import caliban.RootResolver

	val api = graphQL(RootResolver(queries))

	import caliban.schema.{ ArgBuilder, Schema }
	import caliban.CalibanError.ExecutionError

	implicit val urlSchema: Schema[Any, URL] = Schema.stringSchema.contramap(_.toString)
	implicit val urlArgBuilder: ArgBuilder[URL] = ArgBuilder.string.flatMap(
		url => Try(new URL(url)).fold(_ => Left(ExecutionError(s"Invalid URL $url")), Right(_))
	)

	val query =
		"""
		  |{
		  |  getuser{
		  |    name
		  |    bells
		  |  }
		  |}
		  |""".stripMargin

	for {
		interpreter <- api.interpreter
		result      <- interpreter.execute(query)
		_           <- zio.console.putStrLn(result.data.toString)
	} yield ()





	val interpreter = runtime.unsafeRun(
		api.interpreter
	)


	val route =
		path("api" / "graphql") {
			adapter.makeHttpService(interpreter)
		} ~ path("graphiql") {
			getFromResource("graphiql.html")
		}


	val bindingFuture = Http().bindAndHandle(route, "localhost", 7000)
	println(s"Server online at http://localhost:7000/\nPress RETURN to stop...")
	StdIn.readLine()
	bindingFuture
		.flatMap(_.unbind())
		.onComplete(_ => system.terminate())

}
