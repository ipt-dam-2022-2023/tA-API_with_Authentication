package pt.ipt.dam2022.api.retrofit.service

import pt.ipt.dam2022.api.model.APIResult
import pt.ipt.dam2022.api.model.Note
import pt.ipt.dam2022.api.model.TokenJWT
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
    @GET("api/getNotesJWT.php")
    fun listJWT(@Header("Authorization") token:String): Call<List<Note>>

    /**
     * function to ADD (write) data to API
     */
    @FormUrlEncoded
    @POST("API/addNote.php")
    fun addNote(@Field ("title") title:String?,
                @Field("description") description :String?
    ): Call<APIResult>

    // we will need here some more code to work with JWT...
    @FormUrlEncoded
    @POST("API/loginJWT.php")
    fun loginJWT(@Field("username") username: String?,
                 @Field("password") password:String?
                 ): Call<TokenJWT>







/*
    @DELETE("API/deleteNote.php")
    fun deleteNote():Call<APIResult>
*/

}


