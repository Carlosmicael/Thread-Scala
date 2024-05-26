import scala.concurrent.{ExecutionContext, Future, Promise, Await}
import scala.util.{Failure, Success}
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

object Futures_ConcurrenciaScala_Akka {
  def main(args: Array[String]): Unit = {
    def function1(): Int = {
      println("Starting function 1")
      Thread.sleep(1000)
      println("Completed function 1")
      1
    }

    def function2(): Int = {
      println("Starting function 2")
      Thread.sleep(1000)
      println("Completed function 2")
      2
    }

    val promise1 = Promise[Int]()
    val promise2 = Promise[Int]()

    val future1: Future[Int] = Future {
      val result = function1()
      promise1.success(result)
      result
    }
    val future2: Future[Int] = Future {
      val result = function2()
      promise2.success(result)
      result
    }
    val combinedFuture = for {
      result1 <- promise1.future
      result2 <- promise2.future
    } yield (result1, result2)
    val results = Await.result(combinedFuture, Duration.Inf)
    println(s"Function 1 result: ${results._1}")
    println(s"Function 2 result: ${results._2}")
  }
}


