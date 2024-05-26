import akka.actor._
class Resource extends Actor {
  def receive: Receive = {
    case Request =>
      println(s"Resource: Recibida solicitud de ${sender()}")
      Thread.sleep(100)
      sender() ! Done

    case Release =>
      println("Resource: Recurso liberado y disponible nuevamente")
  }
}
class Worker(resource: ActorRef) extends Actor {
  def receive: Receive = {
    case Request =>
      println(s"Worker: Solicitando recurso a ${resource}")
      resource ! Request

    case Done =>
      println("Worker: Proceso completado, liberando recurso")
      resource ! Release
  }
}
case object Request
case object Release
case object Done

object ResourceStarvationSolution extends App {
  val system = ActorSystem("ResourceStarvationSystem")
  private val resource = system.actorOf(Props[Resource], "resource")
  private val worker1 = system.actorOf(Props(new Worker(resource)), "worker1")
  private val worker2 = system.actorOf(Props(new Worker(resource)), "worker2")
  private val worker3 = system.actorOf(Props(new Worker(resource)), "worker3")
  worker1 ! Request
  worker2 ! Request
  worker3 ! Request
}
