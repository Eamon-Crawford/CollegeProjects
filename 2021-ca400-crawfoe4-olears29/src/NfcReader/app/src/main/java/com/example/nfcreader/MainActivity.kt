package com.example.nfcreader

import android.app.PendingIntent
import android.content.Intent
import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.nfcreader.api.ApiClient
import com.example.nfcreader.model.LectureModel
import com.example.nfcreader.parser.NdefMessageParser.parse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class MainActivity : AppCompatActivity() {

    private var nfcAdapter: NfcAdapter? = null
    private var pendingIntent:PendingIntent? = null
    lateinit var buttonSubmit: Button
    lateinit var lecturelist: List<LectureModel>
    lateinit var lecture: LectureModel
    lateinit var studentId: String
    lateinit var setLocation: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        nfcAdapter = NfcAdapter.getDefaultAdapter(this)
        if (nfcAdapter == null) {
            Toast.makeText(this, "No NFC", Toast.LENGTH_SHORT).show()
            finish()
            return
        }
        pendingIntent = PendingIntent.getActivity(
                this, 0,
                Intent(this, this.javaClass)
                        .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0
        )
        setLocation = getLocation()
        findViewById<TextView>(R.id.set_location).text = setLocation
    }

    override fun onResume() {
        super.onResume()
        if (!nfcAdapter!!.isEnabled) showWirelessSettings()
        nfcAdapter!!.enableForegroundDispatch(this, pendingIntent, null, null)
    }

    @Override
    override fun onPause() {
        super.onPause()

        nfcAdapter!!.disableForegroundDispatch(this)
    }

    private fun showWirelessSettings() {
        Toast.makeText(this, "You need to enable NFC", Toast.LENGTH_SHORT).show()
        val intent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
        startActivity(intent)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        resolveIntent(intent)
    }

    private fun resolveIntent(intent: Intent) {
        val action = intent.action
        if (NfcAdapter.ACTION_TAG_DISCOVERED == action || NfcAdapter.ACTION_TECH_DISCOVERED == action || NfcAdapter.ACTION_NDEF_DISCOVERED == action) {
            val rawMsgs =
                    intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)
            val msgs: Array<NdefMessage?>
            if (rawMsgs != null) {
                msgs = arrayOfNulls(rawMsgs.size)
                for (i in rawMsgs.indices) {
                    msgs[i] = rawMsgs[i] as NdefMessage
                }
                setStudentId(msgs)
            }
        }
    }

    private fun setStudentId(msgs: Array<NdefMessage?>?) {
        if (msgs == null || msgs.isEmpty()) return
        val builder = java.lang.StringBuilder()
        val records =
            parse(msgs[0]!!)
        val size = records.size
        for (i in 0 until size) {
            val record = records[i]
            val str = record.str()
            builder.append(str).append("\n")
        }

        studentId = builder.toString().trim()
        Log.d("student number", studentId)
        registerStudentAsAttending()
        tapInView(findViewById(android.R.id.content))
    }

    private fun registerStudentAsAttending() {
        var message = ""
        when {
            studentId.isNotBlank() -> {
                val regCall: Call<String> = ApiClient.getClient.regUserForLecture(studentId, lecture.uuid.toString())

                regCall.enqueue(object : Callback<String> {
                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        if (response.isSuccessful) {
                            Log.d("Info", response.message())
                            message = "Success"
                            studentId = ""
                            return
                        } else {
                            Log.d("Error", response.message())
                            message = "Failure"
                            studentId = ""
                            return
                        }
                    }

                    override fun onFailure(call: Call<String>, t: Throwable) {
                        Log.d("Error", t.localizedMessage)
                    }
                })
            }
        }
        if(message.isNotBlank()) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }

    fun tapInView(view: View) {
        setContentView(R.layout.tap_in)
        findViewById<TextView>(R.id.regResponseText).text = ""

        if (::setLocation.isInitialized && setLocation.isNotBlank()) {
            findViewById<TextView>(R.id.set_location).text = setLocation
            if (!::lecture.isInitialized) {
                getCurrentLecture()
            }

            if (::lecturelist.isInitialized && lecturelist.size == 1) {
                lecture = lecturelist[0]
                findViewById<TextView>(R.id.displayModule).text = String.format(getString(R.string.welcome), lecture.moduleCode)
            }

            if (::lecture.isInitialized) {
                Toast.makeText(this, "Scanning for student cards", Toast.LENGTH_SHORT).show()
                getNFC()
            } else {
                setLocation = ""
                findViewById<TextView>(R.id.set_location).text = setLocation
                setLocationView(findViewById(android.R.id.content))
            }

        } else {
            setLocation = ""
            findViewById<TextView>(R.id.set_location).text = setLocation
            Toast.makeText(this, "No Location Given, Please Set a Location", Toast.LENGTH_SHORT).show()
            setLocationView(findViewById(android.R.id.content))
        }
    }

    private fun getNFC() {
        nfcAdapter = NfcAdapter.getDefaultAdapter(this)
        if (nfcAdapter == null) {
            Toast.makeText(this, "No NFC Adaptor", Toast.LENGTH_SHORT).show()
            finish()
            return
        }
        pendingIntent = PendingIntent.getActivity(
                this, 0,
                Intent(this, this.javaClass)
                        .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0
        )
    }

    private fun getCurrentLecture() {
        val lectureCall: Call<List<LectureModel>> = ApiClient.getClient.getCurrentLectures(setLocation.toUpperCase())
        lectureCall.enqueue(object : Callback<List<LectureModel>> {
            override fun onResponse(call: Call<List<LectureModel>>, response: Response<List<LectureModel>>) {
                if (response.isSuccessful) {
                    setLectureList(response.body()!!)
                } else {
                    Log.d("Error", response.message())
                }
            }

            override fun onFailure(call: Call<List<LectureModel>>, t: Throwable) {
                Log.d("Error", t.localizedMessage)
            }
        })
    }

    private fun setLectureList(lectures: List<LectureModel>){
        lecturelist = when {
            lectures.size == 1 -> {
                lectures
            }
            lectures.size > 1 -> {
                Log.d("Error", "Multiple classes booked in this room")
                Toast.makeText(this, "Multiple classes booked in this room", Toast.LENGTH_SHORT).show()
                lectures
            }
            else -> {
                Log.d("Error", "No class found for this room at this time")
                Toast.makeText(this, "No class found for this room at this time", Toast.LENGTH_SHORT).show()
                lectures
            }
        }
        tapInView(findViewById(android.R.id.content))
    }

    fun setLocationView(view: View) {
        setContentView(R.layout.set_location)
        buttonSubmit = findViewById(R.id.submit_location_button);

        buttonSubmit.setOnClickListener {
            setLocation = findViewById<EditText>(R.id.room_code_input).text.toString().toUpperCase(Locale.ROOT)
            if (::setLocation.isInitialized && setLocation.isNotBlank()) {
                saveLocation(setLocation)
                getCurrentLecture()
            } else {
                Toast.makeText(this, "No location given", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun saveLocation(location: String) {
        val settings = applicationContext.getSharedPreferences("dcu_prefs", 0)
        val editor = settings.edit()
        editor.putString("storedLocation", location)
        editor.apply()
    }

    private fun getLocation(): String {
        val settings = applicationContext.getSharedPreferences("dcu_prefs", 0)
        return settings.getString("storedLocation", "").toString()
    }

}