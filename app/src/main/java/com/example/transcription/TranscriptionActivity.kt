package com.example.transcription

import android.annotation.SuppressLint
import android.content.ContentProvider
import android.content.ContentResolver
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings.Global.getString
import android.widget.Button
import kotlinx.android.synthetic.main.activity_transcription.*
import org.w3c.dom.Text
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.URL
import java.nio.charset.Charset
import java.util.*
import android.content.ContentProviderClient as ContentProviderClient1

class TranscriptionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transcription)
        var inputText = editTextWord.text

        val btn_start: Button = findViewById(R.id.btn_start)
        btn_start.setOnClickListener{
            var transcription : GetTranscription = GetTranscription()
            val urlTrans = getString(R.string.transcription_domain)
            transcription.execute(urlTrans.plus(inputText.toString().toLowerCase(Locale.ROOT)),
                inputText.toString().toLowerCase(Locale.ROOT))
            val test = transcription.get()
            setTranscription(test)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setTranscription(trns : String) {
        textView_transcription.text = "[$trns]"
    }


    class GetTranscription : AsyncTask<String, Void, String>() {

        lateinit var trns : String

        override fun doInBackground(vararg params: String): String? {
//            TODO("Not yet implemented")
            try {
                val outText = params[0]
                val word = params[1]
                val url = URL(outText)
                var result = url.readText()

                val indexUK : Int
                indexUK = result.indexOf("британская транскрипция слова ")

                if (indexUK != -1) {
                    result = result.substring(indexUK + word.length + 55, indexUK + word.length + 75)
                    val index1 = result.indexOf("|")
                    val index2 = result.lastIndexOf("|")
                    try {
                        result = result.substring(index1 + 1, index2)
                    } catch (e: java.lang.Exception) {
                        println("EXCEPTION SUBSTRING")
                        result = "nullPoint"
                    }
                }
                else result = "nullPoint"
                trns = result
                println(result)
            } catch (e: Exception) {
                println(e)
            }
            return trns
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)

        }


    }
}
