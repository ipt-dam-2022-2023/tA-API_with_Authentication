package pt.ipt.dam2022.api.ui.activity

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import okhttp3.Credentials
import pt.ipt.dam2022.api.R
import pt.ipt.dam2022.api.model.APIResult
import pt.ipt.dam2022.api.model.Note
import pt.ipt.dam2022.api.model.TokenJWT
import pt.ipt.dam2022.api.retrofit.RetrofitInitializer
import pt.ipt.dam2022.api.ui.adapter.NoteListAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.random.Random


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //********************************************************
        // here we must ask for permission to access internet
        // please, see code from last classes
        //********************************************************


        // add reference to button that we are going to use
        // to add some random data to API
        val btNewNote = findViewById<Button>(R.id.bt_new_note)
        btNewNote.setOnClickListener {
            // define the task that app will do when a user click the button
            addNewNote()
        }

        // add reference to button to read data with no authentication
        val btListNotes = findViewById<Button>(R.id.bt_readNotes)
        btListNotes.setOnClickListener {
            listNotes()
        }

        // add reference to button to read data with Basic Authentication
        val btListNotesBA = findViewById<Button>(R.id.bt_getNotesWithBA)
        btListNotesBA.setOnClickListener {
            listNotesBA()
        }

        // add reference to button to read data with Basic Authentication
        val btListNotesJWT = findViewById<Button>(R.id.bt_getNotesWithJWT)
        btListNotesJWT.setOnClickListener {
            listNotesJWT()
        }

        // clean screen
        val btClear = findViewById<Button>(R.id.bt_ClearScreen)
        btClear.setOnClickListener {
            // do something to clear screen
            configureList(emptyList())
        }
    }

    /**
     * access the API with Basic Authentication
     */
    private fun listNotesBA() {
        val call = RetrofitInitializer().noteService().listBA(Credentials.basic("admin", "admin"))
        processList(call)
    }

    /**
     * access to data using JWToken
     */
    private fun listNotesJWT() {
        loginJWT("admin", "admin") {
            Toast.makeText(
                    this, "Token: $it?.token", Toast.LENGTH_LONG
            ).show()
            val call = RetrofitInitializer()
                           .noteService()
                           .listJWT(token = "Bearer " + it?.token)
            processList(call)
        }
    }

    private fun loginJWT(username: String,
                         password: String,
                         onResult: (TokenJWT?) -> Unit ) {
        // ask Retrofit to authenticate with the supplied
        // 'username' and 'password'
        val call = RetrofitInitializer()
                      .noteService()
                      .loginJWT(username,password)
        // if we have received the TOKEN, we can do some work...
        call.enqueue(object : Callback<TokenJWT>{
            override fun onFailure(call: Call<TokenJWT>, t: Throwable) {
                // writes on console the error
                t.printStackTrace()
                // returns no data
                onResult(null)
            }
            override fun onResponse(call: Call<TokenJWT>, response: Response<TokenJWT>) {
                // 'reads' the TOKEN
                val tokenResult = response.body()
                // returns the TOKEN
                onResult(tokenResult)
            }
        })
    }







    /**
     * create a random Note
     */
    private fun addNewNote() {

        var i = Random(GregorianCalendar.getInstance().timeInMillis).nextInt(100)
        var note = Note("Note $i", "Description of Note " + i)

        addNote(note) {
            Toast.makeText(
                    this, "new Note added" + it?.description, Toast.LENGTH_SHORT
            ).show()
            // refresh the list of Notes on screen
            listNotes()
        }
    }

    /**
     * add the random Note to the API
     */
    private fun addNote(note: Note, onResult: (APIResult?) -> Unit) {
        // prepare Retrofit to add the Note
        val call = RetrofitInitializer()
                        .noteService()
                        .addNote(note.title, note.description)

        // Tell Retrofit to do the work
        call.enqueue(object : Callback<APIResult> {
            override fun onFailure(call: Call<APIResult>, t: Throwable) {
                t.printStackTrace()
                onResult(null)
            }

            override fun onResponse(
                    call: Call<APIResult>, response: Response<APIResult>
            ) {
                val addedNote = response.body()
                onResult(addedNote)
            }
        })
    }

    /**
     * access API without authentication
     */
    private fun listNotes() {
        // ask retrofit to read the API data
        val call = RetrofitInitializer().noteService().list()
        // draw Notes on screen
        processList(call)
    }

    /**
     * add the Notes to the interface
     */
    private fun processList(call: Call<List<Note>>) {
        // use data read
        call.enqueue(object : Callback<List<Note>?> {
            override fun onResponse(
                    call: Call<List<Note>?>?, response: Response<List<Note>?>?
            ) {
                response?.body()?.let {
                    val notes: List<Note> = it
                    // takes the data read from API
                    // and formats that data to the interface
                    configureList(notes)
                }
            }

            override fun onFailure(call: Call<List<Note>?>?, t: Throwable?) {
                t?.printStackTrace()
                t?.message?.let { Log.e("I can not read data...", it) }
            }
        })
    }

    /**
     * configure each 'fragment' to present data
     */
    private fun configureList(notes: List<Note>) {
        val recyclerView = findViewById<RecyclerView>(R.id.nodeListRecyclerView)

        recyclerView.adapter = NoteListAdapter(notes, this)

        val layoutManager = StaggeredGridLayoutManager(
                2, StaggeredGridLayoutManager.VERTICAL
        )

        recyclerView.layoutManager = layoutManager
    }
}



