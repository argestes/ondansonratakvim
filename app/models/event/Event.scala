package models.event


import org.joda.time.DateTime
import org.joda.time.format.{DateTimeFormat, DateTimePrinter, DateTimeFormatter}

case class Event(startDate: DateTime,
                 endDate: DateTime,
                 name: String,
                 description: String,
                  place:String) {
  def hour = {
    DateTimeFormat.forPattern("HH:mm").print(startDate)
  }
}
