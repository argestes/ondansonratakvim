package controllers

import java.awt.image.BufferedImage
import java.io.File
import java.util
import java.util.TimeZone

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.calendar.Calendar
import com.google.api.services.calendar.model.EventDateTime
import models.event.{ParsedWeek, Event, EventDayImpl, EventWeek}
import org.joda.time.format.{ISODateTimeFormat, DateTimeFormatter}
import org.joda.time.{DateTime, DateTimeZone}
import play.api.mvc._

import scala.collection.JavaConversions._

object Application extends Controller {

  def index = Action {

    new DateTime
    val image = new BufferedImage(800, 600, BufferedImage.TYPE_INT_ARGB)
    val graphics = image.createGraphics();

    val eventList = List(Event(DateTime.now(), DateTime.now(), "Test", "Desc","Mekan"))
    val days = for (i <- Range(1, 7))
    yield if (i % 2 == 0) EventDayImpl(getDateTimeForDay(i), eventList) else EventDayImpl(getDateTimeForDay(i), eventList ++ eventList)
    val week = EventWeek(days.toList)
    Ok(views.html.index(week, week.rows))
  }

  def getDateTimeForDay(day: Int): DateTime = {
    new DateTime(2015, 5, day + 1, 0, 0, 0)
  }

  def takvimTest = Action {
    val cred = new GoogleCredential.Builder()
      .setTransport(GoogleNetHttpTransport.newTrustedTransport())
      .setJsonFactory(JacksonFactory.getDefaultInstance())
      .setServiceAccountId("428561488651-hp4mnu5er8u5jdmesoetnsjddeccm220@developer.gserviceaccount.com")
      .setServiceAccountScopes(util.Arrays.asList("https://www.googleapis.com/auth/calendar"))
      .setServiceAccountPrivateKeyFromP12File(new File("conf/CaferTakvim-da610aacb15c.p12"))
      .build()


    val client: Calendar = new Calendar.
    Builder(GoogleNetHttpTransport.newTrustedTransport,
        new JacksonFactory, cred)
      .setApplicationName("10danSonra Takvim").build
    val ondanSonraTakvim = client.calendars().get("9cdpp7n28gspmsut0u8orqnslo@group.calendar.google.com").execute()


    val start = new DateTime(2015, 4, 27, 0, 0, 0, DateTimeZone.forTimeZone(TimeZone.getTimeZone("Europe/Istanbul")))
    val end = start.plusDays(7)
    val eventsname = client.events().list(ondanSonraTakvim.getId)
      .setTimeMin(jodaToGoogleTime(start))
      .setTimeMax(jodaToGoogleTime(end)).execute().getItems.toList.map(event => {
      new Event(event.getStart, event.getEnd, event.getSummary, event.getDescription, event.getLocation)
    });

    val week = new ParsedWeek(eventsname)
    Ok(views.html.index(week, week.rows))
  }

  import com.google.api.client.util.{DateTime => GoogleTime}

  def jodaToGoogleTime(dateTime: DateTime): GoogleTime = {
    val timeZone: TimeZone = dateTime.getZone.toTimeZone
    new GoogleTime(dateTime.toDate, timeZone)
  }

  implicit def googleTimeToCalendarTime(googleTime: EventDateTime): DateTime = {
    if (googleTime == null) {
      return null
    }

    if (googleTime.getDate!= null && googleTime.getDate.isDateOnly) {
      return ISODateTimeFormat.dateParser().parseDateTime(googleTime.getDate.toStringRfc3339)
    }
    val timeZone = if (googleTime.getTimeZone == null) DateTimeZone.forTimeZone(TimeZone.getTimeZone("Europe/Istanbul"))
    else DateTimeZone.forTimeZone(TimeZone.getTimeZone(googleTime.getTimeZone))
    new DateTime(googleTime.getDateTime.toStringRfc3339, timeZone)
  }
}