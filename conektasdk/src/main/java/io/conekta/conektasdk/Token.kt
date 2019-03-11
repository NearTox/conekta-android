package io.conekta.conektasdk

import androidx.appcompat.app.AppCompatActivity
import org.json.JSONObject

class Token(private val activity: AppCompatActivity) {
  private var onCreateToken: ((JSONObject) -> Unit)? = null

  fun onCreateTokenListener(listener: (JSONObject) -> Unit) {
    this.onCreateToken = listener
  }

  fun create(card: Card) {
    val nameValuePair = card.run {
      mapOf(
          "card[number]" to number,
          "card[name]" to name,
          "card[cvc]" to cvc,
          "card[exp_month]" to expMonth,
          "card[exp_year]" to expYear,
          "card[device_fingerprint]" to Conekta.deviceFingerPrint(activity)
      )
    }

    val connection = Connection(endPoint, nameValuePair)

    connection.onRequestListener { data: String ->
      val obj = try {
        JSONObject(data)
      } catch(err: Exception) {
        JSONObject()
      }

      onCreateToken?.invoke(obj)
    }
    connection.request()
  }

  companion object {
    private const val endPoint = "tokens"
  }
}
