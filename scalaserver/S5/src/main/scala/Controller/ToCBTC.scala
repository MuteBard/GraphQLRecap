package Controller

import zio.{Runtime, ZEnv}

import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import caliban.interop.circe.AkkaHttpCirceAdapter

import scala.concurrent.ExecutionContextExecutor
import scala.io.StdIn
import caliban.GraphQL.graphQL
import caliban.RootResolver
import App.Main.system
import GraphQL.Queries.allQueries
import GraphQL.Mutations.allMutations

object ToCBTC extends App with AkkaHttpCirceAdapter{


	implicit val executionContext: ExecutionContextExecutor = system.dispatcher
	implicit val runtime: Runtime[ZEnv] = Runtime.default
	val api = graphQL(RootResolver(allQueries, allMutations))
	val interpreter = runtime.unsafeRun(api.interpreter)

	val route =
		path("api" / "graphql") {
			adapter.makeHttpService(interpreter)
		} ~ path("graphiql") {
			getFromResource("graphiql.html")
		}

	val bindingFuture = Http().bindAndHandle(route, "localhost", 4774)
	println(s"Server online at http://localhost:4774/graphiql\nPress RETURN to stop...")
	StdIn.readLine()
	bindingFuture
		.flatMap(_.unbind())
		.onComplete(_ => system.terminate())
}

//https://medium.com/@ghostdogpr/graphql-in-scala-with-caliban-part-1-8ceb6099c3c2
//https://github.com/ghostdogpr/caliban/blob/master/examples/src/main/scala/caliban/akkahttp/ExampleApp.scala