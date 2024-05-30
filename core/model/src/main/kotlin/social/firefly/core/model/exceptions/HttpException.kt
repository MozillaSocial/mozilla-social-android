package social.firefly.core.model.exceptions

class HttpException(
    val code: Int,
    val errorMessage: String,
) : IllegalStateException()