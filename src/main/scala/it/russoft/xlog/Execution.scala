package it.russoft.xlog

import java.lang.System.currentTimeMillis
import java.util.Date

import it.russoft.xlog.Level.{Info, Level}

/**
  * Defines the execution model that contains information like the maximum
  * registered log level, total duration and the log of events.
  *
  * @param level    Maximum registered log level.
  * @param duration Total execution duration, expressed in millis.
  * @param logs     Sequence of registered logs.
  */
case class Execution
(
  level: Level,
  duration: Long,
  logs: Seq[Log]
)

/**
  * Companion object for generating execution blocks.
  */
object Execution {

  private class Context {
    private var _logs: Seq[Log] = Nil
    private var _level: Level = Info

    def logs: Seq[Log] = _logs

    def level: Level = _level

    def info(m: String, t: Option[String] = None): Unit =
      log(Info, m, t)

    def success(m: String, t: Option[String] = None): Unit =
      log(Level.Success, m, t)

    def warning(m: String, t: Option[String] = None): Unit =
      log(Level.Warning, m, t)

    def error(m: String, t: Option[String] = None): Unit =
      log(Level.Error, m, t)

    def log(l: Level, m: String, t: Option[String] = None): Unit = {
      if (l > _level) _level = l
      _logs = _logs :+ Log(l, m, t, new Date)
    }
  }

  /**
    * Defines and starts new execution block that can log events through its context.
    *
    * @param block A function that takes as parameter an instance of [[Context]]
    *              that represents the actual execution context.
    * @return Returns an [[Execution]] object that summarize
    *         the execution of your defined code block.
    */
  def execution(block: Context => Unit): Execution = {
    val c = new Context

    val startMs = currentTimeMillis
    block(c)
    val endMs = currentTimeMillis

    Execution(c.level, endMs - startMs, c.logs)
  }
}
