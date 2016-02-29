package com.innovecs.flights

import java.io.BufferedInputStream
import java.io.File
import java.io.FileInputStream
import java.io.PrintWriter
import java.util.zip.GZIPInputStream

import scala.collection.mutable.HashMap
import scala.io.BufferedSource
import scala.io.Source

object FlightsStats {

  def main(args: Array[String]): Unit = {
    // get the input filename from first argument, else use default one
    val inputFileName = if (args.length > 0) args(0) else "src/test/resources/planes_log.csv.gz"
    // get the output filenames from 2nd and 3rd argument, else use default ones
    val arrivalsFileName = if (args.length > 1) args(1) else "arrivals.txt"
    val deltaFileName = if (args.length > 2) args(2) else "delta.txt"

    // getting the buffered source
    val source = bufferedSource(inputFileName)

    // parsing the source file and calculating the statistics
    val (arrivalsStats, deltaStats) = calculateStats(source)

    // writing calculated statistics to corresponding files
    statsToFile(arrivalsStats, arrivalsFileName)
    statsToFile(deltaStats, deltaFileName)
  }

  /**
   * Reads from the GZip input stream
   * @param fileName name of file to read from
   * @return buffered stream for filename
   */
  def bufferedSource(fileName: String): BufferedSource = {
    val bs = new BufferedInputStream(new GZIPInputStream(new FileInputStream(fileName)))
    Source.fromInputStream(bs)
  }

  /**
   * Calculates the statistics for arrivals and difference
   * @param source Buffered source
   * @return Tuple w/ calculated statistics
   */
  def calculateStats(source: Source): (HashMap[String, Int], HashMap[String, Int]) = {
    // resulting maps where key is airport code, value is numeric value
    var arrivalsMap, deltaMap = new HashMap[String, Int]

    // reading the first line which contains column names
    val header = source.getLines().next()

    /**
     * Returns index of columnName in header (first line in file)
     * @param columnName
     * @return column index
     */
    def col(columnName: String): Int = header.split(",").indexOf("\"" + columnName + "\"")

    /**
     * Updates arrivalsMap based on contents of line
     * Arrival statistics is calculated basing on "DEST" column of CSV file
     * The counter for an airport increments when the airport is in the line's "DEST" column
     * @param line Comma-separated String
     * @return HashMap updated according to line contents
     */
    def arrival(line: String): HashMap[String, Int] = {
      // obtain airport code from a flight DEST
      val key = line.split(",")(col("DEST"))
      // obtain current count of arrivals || 0 if first arrival
      val currCount = arrivalsMap.get(key).getOrElse(0)
      // update map with incremented value
      arrivalsMap(key) = currCount + 1
      arrivalsMap
    }

    /**
     * Updates deltaMap based on contents of line
     * Delta is difference between “Planes left the airport” and “Planes came to airport”
     * The counter for an airport decrements when the airport is in the line's "ORIGIN" column
     * The counter for an airport increments when the airport is in the line's "DEST" column
     * @param line Comma-separated String
     * @return HashMap updated according to line contents
     */
    def delta(line: String): HashMap[String, Int] = {
      // obtain airport code from a flight ORIGIN
      val airportdep = line.split(",")(col("ORIGIN"))
      // update map with incremented value
      deltaMap(airportdep) = deltaMap.get(airportdep).getOrElse(0) - 1

      // obtain airport code from a flight ORIGIN
      val airportarr = line.split(",")(col("DEST"))
      // update map with incremented value
      deltaMap(airportarr) = deltaMap.get(airportarr).getOrElse(0) + 1

      deltaMap
    }

    // main loop
    for (line <- source.getLines()) {
      arrivalsMap = arrival(line)
      deltaMap = delta(line)
    }

    // get rid of airports where delta is zero
    for ((airport, delta) <- deltaMap)
      deltaMap = delta match {
        case 0 => deltaMap - airport
        case _ => deltaMap
      }

    (arrivalsMap, deltaMap)
  }

  /**
   * Writes the statistics to the file
   * @param statsMap Map containing statistics
   * @param fileName file name of the resulting file
   */
  def statsToFile(statsMap: HashMap[String, Int], fileName: String): Unit = {
    val writer = new PrintWriter(new File(fileName))
    for ((k, v) <- statsMap) writer.write((f"$k  $v\n"))
    writer.close()
    println(f"====> Open file $fileName to see the statistics.") 
  }
}
