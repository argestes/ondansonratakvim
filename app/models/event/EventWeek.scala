package models.event

import models.IEventWeek
import org.joda.time.DateTime

case class EventWeek(eventDays: List[EventDay]) extends IEventWeek {

}

class ParsedWeek(events: List[Event]) extends IEventWeek {
  implicit def ordering[A <: DateTime]: Ordering[DateTime] = Ordering.by(dateTime => dateTime.getMillis)

  private class WeekWrapper(val wrappedWeek: List[(DateTime, List[Event])]) {

    val q = wrappedWeek.map(dayEntry => EventDayImpl(dayEntry._1, dayEntry._2))
    val t = q.toList.sortBy(day => day.dayOfTheWeek.getMillis)
    val p = t.groupBy(day => day.dayOfTheWeek.dayOfYear().get())
    val firstDay = t.head
    val firstDayOfYear = firstDay.dayOfTheWeek.dayOfYear().get()

    def get(i: Int): EventDayImpl = {
      if (p.contains(i + firstDayOfYear))
        p(i + firstDayOfYear).head
      else
        firstDay.copy(firstDay.dayOfTheWeek.plusDays(i), List())
    }
  }


  val parsedDays = events.groupBy(event => event.startDate.withMillisOfDay(0))
  val filledDays = {
    val tempDays = parsedDays.toList.sortBy(f => f._1)
    val sortedDays = tempDays.map(day => day.copy(day._1, day._2.sortBy(event => event.startDate)))
    val wrappedWeek = new WeekWrapper(sortedDays)

    val q = for (i <- 0 to 6)
      yield wrappedWeek.get(i)
    q.toList
  }

  override val eventDays: List[EventDay] = filledDays
}