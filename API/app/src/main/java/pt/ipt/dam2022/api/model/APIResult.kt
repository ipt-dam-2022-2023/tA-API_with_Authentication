package pt.ipt.dam2022.api.model

import com.google.gson.annotations.SerializedName


/**
 * define data structure to add data to API
 */
data class APIResult (
        @SerializedName("code") val code: String?,
        @SerializedName("description") val description: String?
        )