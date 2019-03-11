package io.conekta.helloconekta

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import io.conekta.conektasdk.Card
import io.conekta.conektasdk.Conekta
import io.conekta.conektasdk.Token

class Form : AppCompatActivity() {
  private lateinit var btnTokenize: Button
  private lateinit var outputView: TextView
  private lateinit var uuidDevice: TextView
  private lateinit var numberText: EditText
  private lateinit var monthText: EditText
  private lateinit var yearText: EditText
  private lateinit var cvcText: EditText
  private lateinit var nameText: EditText

  private val isOnline: Boolean
    get() {
      val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
      val netInfo = cm.activeNetworkInfo
      return netInfo != null && netInfo.isConnected
    }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setContentView(R.layout.activity_form)

    btnTokenize = findViewById<View>(R.id.btnTokenize) as Button
    outputView = findViewById<View>(R.id.outputView) as TextView
    uuidDevice = findViewById<View>(R.id.uuidDevice) as TextView
    numberText = findViewById<View>(R.id.numberText) as EditText
    nameText = findViewById<View>(R.id.nameText) as EditText
    monthText = findViewById<View>(R.id.monthText) as EditText
    yearText = findViewById<View>(R.id.yearText) as EditText
    cvcText = findViewById<View>(R.id.cvcText) as EditText

    btnTokenize.setOnClickListener {
      val haveInternet = isOnline
      if(!haveInternet) {
        outputView.text = "You don't have internet"
        return@setOnClickListener
      }

      Conekta.publicKey = "zbp4axNG4xVUMcDzTLNz"
      Conekta.apiVersion = "0.3.0"
      Conekta.collectDevice(this)
      val card = Card(
          nameText.text.toString(),
          numberText.text.toString(),
          cvcText.text.toString(),
          monthText.text.toString(),
          yearText.text.toString()
      )
      val token = Token(this)

      //Listen when token is returned
      token.onCreateTokenListener { data ->
        try {
          Log.d("The token::::", data.getString("id"))
          outputView.text = "Token id: " + data.getString("id")
        } catch(err: Exception) {
          outputView.text = "Error: $err"
        }

        uuidDevice.text = "Uuid device: " + Conekta.deviceFingerPrint(this)
      }

      //Request for create token
      token.create(card)

    }
  }


  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    // Inflate the menu; this adds items to the action bar if it is present.
    menuInflater.inflate(R.menu.menu_form, menu)
    return true
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    val id = item.itemId


    return if(id == R.id.action_settings) {
      true
    } else super.onOptionsItemSelected(item)

  }
}
