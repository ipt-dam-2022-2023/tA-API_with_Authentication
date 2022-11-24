package pt.ipt.dam2022.api.model

import com.google.gson.annotations.SerializedName

/**
 * class to represent the data structure,
 * obtained from API
 */
data class Note(
        @SerializedName("title") val title: String,
        @SerializedName("description") val description: String
)
