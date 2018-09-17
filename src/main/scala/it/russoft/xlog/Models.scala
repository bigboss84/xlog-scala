package it.russoft.xlog

import java.util.Date

import it.russoft.xenum.Enum
import it.russoft.xlog.Level.Level

/**
  * Defines log level enumeration.
  */
object Level extends Enum {
  type Level = EnumVal

  val Info: Level = value("INFO")
  val Success: Level = value("SUCCESS")
  val Warning: Level = value("WARNING")
  val Error: Level = value("ERROR")
}

/**
  * Defines a log that can be registered as event into an execution context.
  *
  * @param level The level severity of the log.
  * @param message The message of the event log.
  * @param trace Refers to an [[Option]] that can boxes more event details,
  *              for example it can contains technical details.
  * @param date The log date.
  */
case class Log
(
  level: Level,
  message: String,
  trace: Option[String],
  date: Date
)
