package pt.ipt.dam2022.api.retrofit.service

import pt.ipt.dam2022.api.model.APIResult
import pt.ipt.dam2022.api.model.Note
import retrofit2.Call
import retrofit2.http.*

/**
 * specify the part of 'url' that we want to access
 */
interface NoteService {

    /**
     * function to READ data from API
     */
    @GET("API/getNotes.php")
    fun list(): Call<List<Note>>

    /**
     * function to read data from API with Basic Authentication
     */
    @GET("API/getNotesBA.php")
    fun listBA(@Header("Authorization") authorization:String): Call<List<Note>>

    /**
     * function to read data from API with JWTokens
     */
    //    @GET("api/getNotesBA.php")
    //    fun listJWT(): Call<List<Note>>

    /**
     * function to ADD (write) data to API
     */
    @POST("API/addNote.php")
    fun addNote(@Field ("title") title:String?,
                @Field("description") description :String?
    ): Call<APIResult>

    // we will need here some more code to work with JWT...
}


