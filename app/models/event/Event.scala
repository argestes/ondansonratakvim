package models.event

import org.joda.time.DateTime

case class Event(startDate: DateTime,
                 endDate: DateTime,
                 name: String,
                 description: String)
