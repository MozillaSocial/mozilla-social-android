package org.mozilla.social.common.utils

import java.time.format.DateTimeFormatter

object DateTimeFormatters {
    val standard: DateTimeFormatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy")
}