package models.event

import models.IEventWeek
import org.joda.time.DateTime

case class EventWeek(eventDays: List[EventDay]) extends IEventWeek {

}

class ParsedWeek(events: List[Event]) extends IEventWeek {
  implicit def ordering[A <: DateTime]: Ordering[DateTime] = Ordering.by(dateTime => dateTime.getMillis)
  val parsedDays = events.groupBy(event => event.startDate.withMillisOfDay(0))
  val filledDays = {
    val tempDays = parsedDays.toList.sortBy(f => f._1)
    val sortedDays = tempDays.map( day => day.copy(day._1, day._2.sortBy(event => event.startDate)))
    val firstDayOfWeek = sortedDays.head._1.dayOfWeek().get()
    val q = for (i <- firstDayOfWeek - 1 to firstDayOfWeek + 5)
    yield (getDay(sortedDays, i, sortedDays.filter(m => m._1.dayOfWeek().get() - 1 == i % 7)))
    q.toList
  }

  def getDay(sortedDays: List[(DateTime, List[Event])], i: Int, days: List[(DateTime, List[Event])]): (DateTime, List[Event]) = {
    if (days.isEmpty) {
      (sortedDays.head._1.withDayOfWeek((i % 7) + 1), List())
    } else {
      days.head
    }
  }

  override val eventDays: List[EventDay] = filledDays.map(f => EventDayImpl(f._1, f._2))
}