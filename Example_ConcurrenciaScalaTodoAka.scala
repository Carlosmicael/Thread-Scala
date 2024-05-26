import akka.actor.typed.{ActorRef, ActorSystem, Behavior}
import akka.actor.typed.scaladsl.Behaviors
import Messages._
object Example_ConcurrenciaScalaTodoAka extends App {
  val system: ActorSystem[ExecuteTask] = ActorSystem(TaskActor(), "taskActorSystem")
  val system2: ActorSystem[ExecuteTask] = ActorSystem(TaskActor2(), "taskActorSystem")
  (1 to 3).foreach { taskId =>
    system ! ExecuteTask(taskId)
    system2 ! ExecuteTask(taskId)
  }
  Thread.sleep(4000)
  system.terminate()
  system2.terminate()
}

object Messages {
  final case class ExecuteTask(taskId: Int)
}

object TaskActor {
  def apply(): Behavior[ExecuteTask] = {
    Behaviors.receive { (context, message) =>
      println(s"Executing task ${message.taskId}")
      val sum=20+4+6
      println(sum)
      Thread.sleep(1000)
      println(s"Task ${message.taskId} completed")
      Behaviors.same
    }
  }
}
object TaskActor2 {
  def apply(): Behavior[ExecuteTask] = {
    Behaviors.receive { (context, message) =>
      println(s"Executing task_Hilo2 ${message.taskId}")
      val sum = 5+5+5
      println(sum)
      Thread.sleep(1000)
      println(s"Task_Hilo2 ${message.taskId} completed")
      Behaviors.same
    }
  }
}





