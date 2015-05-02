package models

import models.event.{Event, EventDay}

trait IEventWeek {

  val eventDays : List[EventDay]

  def get(x: Int, y: Int): Option[Event] = {
    try {
      Some(eventDays(x).events(y))
    } catch {
      case _: Throwable => None
    }
  }

  def getRow(y:Int) : List[Option[Event]] = {


    val row = for (i <- 0 until eventDays.size) yield get(i,y)
    row.toList
  }

  def rows = {
    val max = eventDays.map(day => {
      day.events.length
    }).max

    (for (i <- 0 until max) yield getRow(i)).toList
  }
}
