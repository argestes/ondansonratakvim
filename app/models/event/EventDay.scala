package models.event

import java.util.Locale

import org.joda.time.DateTime
import org.joda.time.format.{DateTimeFormat, DateTimeFormatter}

trait EventDay {
  val dtfOut = DateTimeFormat.forPattern("dd MMMM yyyy").withLocale(Locale.forLanguageTag("tr"))

  val dayOfTheWeek: DateTime

  def dayName = {
    dayOfTheWeek.dayOfWeek().getAsText(Locale.forLanguageTag("tr"))
  }

  def dayDate = {
    dtfOut.print(dayOfTheWeek)
  }

  val events: List[Event]
}

case class EventDayImpl(dayOfTheWeek: DateTime, events: List[Event]) extends EventDay