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
  * @param obj      Boxed object of type `T`
  */
case class Execution[T]
(
  level: Level,
  duration: Long,
  logs: Seq[Log],
  obj: Option[T] = None
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
  def execution[T](block: Context => Option[T]): Execution[T] = {
    val c = new Context

    val startMs = currentTimeMillis
    val r = block(c)
    val endMs = currentTimeMillis

    Execution(c.level, endMs - startMs, c.logs, r)
  }

  /**
    * Makes info execution with single message.
    * @param m Info message.
    * @param o Boxed object.
    * @tparam T Type of the boxed object.
    * @return Returns an [[Execution]] object that wraps
    *         the object in `o` parameter.
    */
  def info[T](m: String, o: Option[T] = None): Execution[T] = {
    execution[T] { c =>
      c.info(m)
      o
    }
  }

  /**
    * Makes success execution with single message.
    * @param m Success message.
    * @param o Boxed object.
    * @tparam T Type of the boxed object.
    * @return Returns an [[Execution]] object that wraps
    *         the object in `o` parameter.
    */
  def success[T](m: String, o: Option[T] = None): Execution[T] = {
    execution[T] { c =>
      c.success(m)
      o
    }
  }

  /**
    * Makes warning execution with single message.
    * @param m Warning message.
    * @param o Boxed object.
    * @tparam T Type of the boxed object.
    * @return Returns an [[Execution]] object that wraps
    *         the object in `o` parameter.
    */
  def warning[T](m: String, o: Option[T] = None): Execution[T] = {
    execution[T] { c =>
      c.warning(m)
      o
    }
  }

  /**
    * Makes error execution with single message.
    * @param m Error message.
    * @param o Boxed object.
    * @tparam T Type of the boxed object.
    * @return Returns an [[Execution]] object that wraps
    *         the object in `o` parameter.
    */
  def error[T](m: String, o: Option[T] = None): Execution[T] = {
    execution[T] { c =>
      c.error(m)
      o
    }
  }

}
