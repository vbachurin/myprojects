import org.junit.runner.RunWith
import junit.framework.Test
import junit.framework.TestCase
import org.junit.Assert._
import java.util.zip.GZIPInputStream
import java.io.FileInputStream
import java.io.BufferedInputStream
import scala.io.Source
import junit.framework.Assert

//@RunWith(classOf[JUnitRunner])
class MainTest extends TestCase {

  //  @Test
  def testArrivals(): Unit = {
    val source = Main.readFile("planes_log.csv.gz")
    val actual = Main.calculateArrivals(source)
    assertEquals(8535, actual.get("\"LGA\"").get)
  }

  def testNoHeaderInStats(): Unit = {
    val source = Main.readFile("planes_log.csv.gz")
    val actual = Main.calculateArrivals(source)
    assertNull(actual.get("\"ORIGIN\"").getOrElse(null))
    assertNull(actual.get("\"DEST\"").getOrElse(null))
  }

  def testHeader(): Unit = {
    val source = Main.readFile("planes_log.csv.gz")

    assertEquals(7, Main.col("DEST"))
  }
}