

import scala.io.Source
import java.io.BufferedInputStream
import java.util.zip.GZIPInputStream
import java.io.FileInputStream
import scala.collection.mutable.HashMap

object Main {

  //  var YEAR, QUARTER, MONTH,DAY_OF_MONTH,DAY_OF_WEEK,FL_DATE,ORIGIN,DEST: Int = -1
  var header: Array[String] = new Array[String](8)

  //,"QUARTER","MONTH","DAY_OF_MONTH","DAY_OF_WEEK","FL_DATE","ORIGIN","DEST",

  def main(args: Array[String]): Unit = {
    val fileName = if (args.length > 0) args(0) else "planes_log.csv.gz"
    val source = readFile(fileName)

    val deltaStats = calculateDelta(source)
    printToFile(deltaStats)
    var arrivalsStats = calculateArrivals(source)
    printToFile(arrivalsStats)
  }

  def readFile(fileName: String): Source = {
    val bs = new BufferedInputStream(new GZIPInputStream(new FileInputStream(fileName)))
    val source = Source.fromInputStream(bs)
    header = source.getLines().take(1).next().split(",")
    source
  }

  def col(columnName: String): Int = header.indexOf("\"" + columnName + "\"")

  def calculateArrivals(source: Source): HashMap[String, Int] = {
    var result = new HashMap[String, Int]()
    for (line <- source.getLines()) {
      var key = line.split(",")(col("DEST"))
      var currCount: Int = result.get(key).getOrElse(0)
      result.update(key, currCount + 1)
    }
    result
  }

  def calculateDelta(source: Source): HashMap[String, Int] = {
    var result = new HashMap[String, Int]
    for (line <- source.getLines()) {
      var airportdep = line.split(",")(col("ORIGIN"))
      result.update(airportdep, result.get(airportdep).getOrElse(0) - 1)
      var airportarr = line.split(",")(col("DEST"))
      result.update(airportarr, result.get(airportarr).getOrElse(0) + 1)
      //      result(line.split(",")(7)) -= 1
      //      result(line.split(",")(8)) += 1
      //      result.put(key, value)
    }
    //      if (result.get(airport) == 0) {
    //        print("!")        
    //    	  result.remove(airport)
    //      }
    //    }
    //    var m = new HashMap[String, Int]
    for (airport <- result.keySet) {
      result = result(airport) match {
        case 0 => {

          result - airport
        }
        case _ => {
          result
        }
      }
    }
    result
  }

  def printToFile(statsMap: HashMap[String, Int]) = {
    println(statsMap)
    //        for((k,v) <- arrivalsStats ) printf("%s  %s\n", k, v)
  }

}