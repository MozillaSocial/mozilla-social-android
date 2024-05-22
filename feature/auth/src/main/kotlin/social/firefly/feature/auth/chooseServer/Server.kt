package social.firefly.feature.auth.chooseServer

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Server(
    @SerialName("server")
    val name: String,
    @SerialName("mau")
    val monthlyActiveUsers: Int
)
