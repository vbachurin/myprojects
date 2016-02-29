package com.innovecs.flights

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class FlightsStatsTest {
  
  // "beforeClass"
	val source = FlightsStats.bufferedSource("src/test/resources/planes_log.csv.gz")

  @Test
  def testArrivals(): Unit = {
    // given
    
    // when
    val (arrivals, delta) = FlightsStats.calculateStats(source)
    
    // then
    assertEquals(8535, arrivals.get("\"LGA\"").get)
  }
	
	@Test
  def testDelta(): Unit = {
    // given
    
    // when
    val (arrivals, delta) = FlightsStats.calculateStats(source)
    
    // then
    assertEquals(5, delta.get("\"LGA\"").get)
  }

	@Test
  def testNoHeaderInStats(): Unit = {
    // given
    
    // when
    val (arrivals, delta) = FlightsStats.calculateStats(source)
    
    // then
    assertNull(arrivals.get("\"ORIGIN\"").getOrElse(null))
    assertNull(delta.get("\"DEST\"").getOrElse(null))
  }

}