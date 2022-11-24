package pt.ipt.dam2022.api.model

import com.google.gson.annotations.SerializedName

/**
 * class used to store the TOKEN used to access API
 */
data class TokenJWT(
        @SerializedName("token") val token: String?
)