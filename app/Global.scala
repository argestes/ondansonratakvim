import controllers.LoggingFilter
import play.api.mvc.WithFilters
import play.filters.gzip.GzipFilter

object Global extends WithFilters(LoggingFilter, new GzipFilter()) {
}