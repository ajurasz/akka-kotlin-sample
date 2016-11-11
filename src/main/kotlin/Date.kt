import java.text.DateFormat
import java.text.ParsePosition
import java.text.SimpleDateFormat
import java.util.*

object DateHelper {
    const val DF_SIMPLE_STRING = "yyyy-MM-dd HH:mm:ss"
    @JvmField val DF_SIMPLE_FORMAT = object : ThreadLocal<DateFormat>() {
        override fun initialValue(): DateFormat {
            return SimpleDateFormat(DF_SIMPLE_STRING, Locale.US)
        }
    }
}

fun Date.asString(format: DateFormat): String = format.format(this)

fun Date.asString(format: String): String = asString(SimpleDateFormat(format))

fun Date.asString(): String = DateHelper.DF_SIMPLE_FORMAT.get().format(this)

fun Long.asDateString(): String = Date(this).asString()
