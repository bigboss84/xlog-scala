package it.russoft.xlog

import java.lang.Thread.sleep

import it.russoft.xlog.Execution._
import it.russoft.xlog.Level.{Error, Info, Warning, Success}
import org.scalatest.{FlatSpec, Matchers}

/**
  * Tests [[Execution]] class.
  */
class ExecutionTest extends FlatSpec with Matchers {

  private val x1 = execution { c =>
    c.info("i-message")
    sleep(3)
  }

  "x1.level" should "be equal to Level.Info" in {
    x1.level shouldEqual Info
  }

  "x1.duration" should "be greater than zero" in {
    x1.duration > 0 shouldEqual true
  }

  "x1.log.length" should "be have size one" in {
    x1.logs.length shouldEqual 1
  }

  "x1.log.head.level" should "be equal to Level.Info" in {
    x1.logs.head.level shouldEqual Info
  }

  "x1.log.head.message" should "be equal to 'i-message'" in {
    x1.logs.head.message shouldEqual "i-message"
  }

  "x1.log.head.trace" should "be equal to None" in {
    x1.logs.head.trace shouldEqual None
  }

  private val x2 = execution { c =>
    c.info("i-message")
    c.success("s-message", Some("nerd-details"))
  }

  "x2.level" should "be equal to Level.Success" in {
    x2.level shouldEqual Success
  }

  "x2.log.length" should "be have size two" in {
    x2.logs.length shouldEqual 2
  }

  "x2.log(1).level" should "be equal to Level.Success" in {
    x2.logs(1).level shouldEqual Success
  }

  "x2.log(1).message" should "be equal to 's-message'" in {
    x2.logs(1).message shouldEqual "s-message"
  }

  private val x3 = execution { c =>
    c.info("i-message")
    c.error("e-message", Some("nerd-details"))
    c.success("s-message")
  }

  "x3.level" should "be equal to Level.Error" in {
    x3.level shouldEqual Error
  }

  "x3.log.length" should "be have size three" in {
    x3.logs.length shouldEqual 3
  }

  "x3.log(2).level" should "be equal to Level.Success" in {
    x3.logs(2).level shouldEqual Success
  }

  "x3.log(1).message" should "be equal to 'e-message'" in {
    x3.logs(1).message shouldEqual "e-message"
  }

  "x3.log(1).trace" should "be Some(nerd-details)" in {
    x3.logs(1).trace shouldEqual Some("nerd-details")
  }

  private val x4 = execution[Int] { c =>
    c.info("i-message")
    7
  }

  "x4.obj" should "be equal to 7" in {
    x4.obj shouldEqual 7
  }

  //
  // single info execution
  //
  private val ix = Execution.info[Map[String, String]]("i-message", Map("ik" -> "iv"))

  "ix.level" should "be equal to Level.Error" in {
    ix.level shouldEqual Info
  }

  "ix.logs" should "have size one" in {
    ix.logs.length shouldEqual 1
  }

  "ix.logs.head.message" should "be equal to 'i-message'" in {
    ix.logs.head.message shouldEqual "i-message"
  }

  "ix.obj('ik')" should "be equal to 'iv'" in {
    ix.obj("ik") shouldEqual "iv"
  }

  //
  // single success execution
  //
  private val sx = Execution.success[Map[String, String]]("s-message", Map("sk" -> "sv"))

  "sx.level" should "be equal to Level.Success" in {
    sx.level shouldEqual Success
  }

  "sx.logs" should "have size one" in {
    sx.logs.length shouldEqual 1
  }

  "sx.logs.head.message" should "be equal to 's-message'" in {
    sx.logs.head.message shouldEqual "s-message"
  }

  "sx.obj('sk')" should "be equal to 'sv'" in {
    sx.obj("sk") shouldEqual "sv"
  }

  //
  // single warning execution
  //
  private val wx = Execution.warning[Map[String, String]]("w-message", Map("wk" -> "wv"))

  "wx.level" should "be equal to Level.Warning" in {
    wx.level shouldEqual Warning
  }

  "wx.logs" should "have size one" in {
    wx.logs.length shouldEqual 1
  }

  "wx.logs.head.message" should "be equal to 'w-message'" in {
    wx.logs.head.message shouldEqual "w-message"
  }

  "wx.obj('wk')" should "be equal to 'wv'" in {
    wx.obj("wk") shouldEqual "wv"
  }

  //
  // single error execution
  //
  private val ex = Execution.error[Map[String, String]]("e-message", Map("ek" -> "ev"))

  "ex.level" should "be equal to Level.Error" in {
    ex.level shouldEqual Error
  }

  "ex.logs" should "have size one" in {
    ex.logs.length shouldEqual 1
  }

  "ex.logs.head.message" should "be equal to 'e-message'" in {
    ex.logs.head.message shouldEqual "e-message"
  }

  "ex.obj('ek')" should "be equal to 'ev'" in {
    ex.obj("ek") shouldEqual "ev"
  }

}
