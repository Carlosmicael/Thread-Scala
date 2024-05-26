import akka.actor._
case object HighPriorityMessage
case object MediumPriorityMessage
case object LowPriorityMessage
class LowPriorityActor extends Actor {
  def receive: Receive = {
    case LowPriorityMessage =>
      println("LowPriorityActor: Procesando mensaje de baja prioridad")
      sender() ! Done
  }
}
class MediumPriorityActor extends Actor {
  def receive: Receive = {
    case MediumPriorityMessage =>
      println("MediumPriorityActor: Procesando mensaje de media prioridad")
      sender() ! Done
  }
}
class HighPriorityActor(mediumPriorityActor: ActorRef) extends Actor {
  def receive: Receive = {
    case HighPriorityMessage =>
      println("HighPriorityActor: Procesando mensaje de alta prioridad")
      mediumPriorityActor ! MediumPriorityMessage
      sender() ! Done
  }
}

object PriorityInversionSolution extends App {
  val system = ActorSystem("PriorityInversionSystem")
  private val lowPriorityActor = system.actorOf(Props[LowPriorityActor], "lowPriorityActor")
  private val mediumPriorityActor = system.actorOf(Props[MediumPriorityActor], "mediumPriorityActor")
  private val highPriorityActor = system.actorOf(Props(new HighPriorityActor(mediumPriorityActor)), "highPriorityActor")
  highPriorityActor ! HighPriorityMessage
  lowPriorityActor ! LowPriorityMessage
}
