package org.mozilla.social.core.network

import org.mozilla.social.core.network.model.NetworkInstanceRules
import retrofit2.http.GET

interface InstanceApi {

    @GET("/api/v1/instance/rules")
    suspend fun getRules(): NetworkInstanceRules
}