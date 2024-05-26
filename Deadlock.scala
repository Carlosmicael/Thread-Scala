import akka.actor._
class ActorA extends Actor {
  var resourceA: Int = 0
  def receive: Receive = {
    case IncrementA =>
      resourceA += 1
      println(s"ActorA: Incrementando recurso A a $resourceA")
      sender() ! Ack
    case GetResourceA =>
      sender() ! ResourceAValue(resourceA)
  }
}

class ActorB extends Actor {
  var resourceB: Int = 0
  def receive: Receive = {
    case IncrementB =>
      resourceB += 1
      println(s"ActorB: Incrementando recurso B a $resourceB")
      sender() ! Ack
    case GetResourceB =>
      sender() ! ResourceBValue(resourceB)
  }
}
case object IncrementA
case object IncrementB
case object GetResourceA
case object GetResourceB
case object Ack
case class ResourceAValue(value: Int)
case class ResourceBValue(value: Int)

object AkkaDeadlockExample extends App {
  val system = ActorSystem("DeadlockSystem")
  private val actorA = system.actorOf(Props[ActorA], "actorA")
  private val actorB = system.actorOf(Props[ActorB], "actorB")
  actorA ! IncrementA
  actorB ! IncrementB
  actorA ! GetResourceA
  actorB ! GetResourceB
  system.terminate()
}

