import scala.concurrent.duration._

object ParallelVsSequentialExample {
  def main(args: Array[String]): Unit = {
    val list = (1 to 1000000).toList
    def complexOperation(n: Int): Int = {
      var result = 0.0
      for (i <- 1 to 1000) {
        result += Math.sqrt(n + i)
      }
      result.toInt
    }
    def measureTime[T](block: => T): Long = {
      val startTime = System.nanoTime()
      block
      val endTime = System.nanoTime()
      (endTime - startTime).nanos.toMillis
    }
    val durationSeq = (1 to 5).map { _ =>
      measureTime {
        val filteredSeq = list.filter(_ % 2 == 0).map(complexOperation)
      }
    }.sum / 5
    val parallelList = list.par
    val durationPar = (1 to 5).map { _ =>
      measureTime {
        val filteredPar = parallelList.filter(_ % 2 == 0).map(complexOperation)
      }
    }.sum / 5
    println(s"Tiempo de ejecución secuencial: $durationSeq ms")
    println(s"Tiempo de ejecución paralelo: $durationPar ms")
  }
}

