package models

import models.event.Event

trait TableWeek {
  val days: Array[String]
  val rows: List[TableRow]
}

trait TableRow {
  val events: Array[Option[Event]]
}
