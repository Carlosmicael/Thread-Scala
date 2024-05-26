import akka.actor.{Actor, ActorSystem, Props}
import akka.pattern.ask
import akka.util.Timeout
import scala.concurrent.duration._


class CounterActor extends Actor {
  var counter: Int = 1
  def receive: Receive = {
    case CounterActor.Increment =>
      counter += 1
    case CounterActor.Decrement =>
      counter -= 1
    case CounterActor.GetValue =>
      sender() ! counter
  }
}
object CounterActor {
  case object Increment
  case object Decrement
  case object GetValue
}

object AkkaActorExample extends App {
  val system = ActorSystem("CounterSystem")
  private val counterActor = system.actorOf(Props[CounterActor], "counterActor")
  counterActor ! CounterActor.Increment
  counterActor ! CounterActor.Increment
  counterActor ! CounterActor.Decrement
  counterActor ! CounterActor.GetValue
  implicit val timeout: Timeout = Timeout(5.seconds)
  import system.dispatcher
  val result = counterActor ? CounterActor.GetValue
  result.onComplete { value =>
    println(s"Valor final del contador: ${value.get}")
    system.terminate()
  }
}
