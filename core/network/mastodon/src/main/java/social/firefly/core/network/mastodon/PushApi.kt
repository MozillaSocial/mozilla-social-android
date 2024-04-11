package social.firefly.core.network.mastodon

import retrofit2.http.Field
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import social.firefly.core.network.mastodon.model.NetworkWebPushSubscription

interface PushApi {

    @FormUrlEncoded
    @POST("/api/v1/push/subscription")
    suspend fun subscribe(
        @Field("subscription[endpoint]") endpoint: String,
        @Field("subscription[keys][p256dh]") p256dh: String,
        @Field("subscription[keys][auth]") auth: String,
        @Field("data[alerts][mention]") mention: Boolean = true,
        @Field("data[alerts][status]") status: Boolean = true,
        @Field("data[alerts][reblog]") reblog: Boolean = true,
        @Field("data[alerts][follow]") follow: Boolean = true,
        @Field("data[alerts][follow_request]") followRequest: Boolean = true,
        @Field("data[alerts][favourite]") favourite: Boolean = true,
        @Field("data[alerts][poll]") poll: Boolean = true,
        @Field("data[alerts][update]") update: Boolean = true,
        @Field("data[alerts][admin.sign_up]") adminSignUp: Boolean = false,
        @Field("data[alerts][admin.report]") adminReport: Boolean = false,
        @Field("data[policy]") policy: String = "all"
    ): NetworkWebPushSubscription

    @GET("/api/v1/push/subscription")
    suspend fun getSubscription(): NetworkWebPushSubscription
}