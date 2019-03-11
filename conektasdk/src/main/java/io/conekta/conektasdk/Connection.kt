package io.conekta.conektasdk

import android.os.AsyncTask
import android.util.Base64
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.*

class Connection(
    private val endPoint: String,
    private val nameValuePairs: Map<String, String>
) {
  private var listener: ((String) -> Unit)? = null

  interface ConecktaApi {
    @FormUrlEncoded
    @POST("{endpoint}")
    @Headers("Conekta-Client-User-Agent: {\"agent\": \"Conekta Android SDK\"}")
    fun listRepos(
        @Path("endpoint") endPoint: String,
        @Header("Accept") accept: String,
        @Header("Authorization") authorization: String,
        @Header("Accept-Language") language: String,
        @FieldMap fields: Map<String, String>
    ): Call<String>
  }


  // Assign the listener implementing events interface that will receive the events
  fun onRequestListener(listener: (String) -> Unit) {
    this.listener = listener
  }

  fun request() {
    runTask(this)
  }

  companion object {
    private class Task(private val pThis: Connection) : AsyncTask<Void, Void, String>() {

      override fun doInBackground(vararg params: Void): String {

        val encoding = Base64.encodeToString(Conekta.publicKey.toByteArray(), Base64.NO_WRAP)

        val retrofit = Retrofit.Builder()
            .addConverterFactory(ScalarsConverterFactory.create())
            .baseUrl(Conekta.baseUri)
            .build()

        val service = retrofit.create(ConecktaApi::class.java)
        val call = service.listRepos(
            pThis.endPoint,
            "application/vnd.conekta-v${Conekta.apiVersion}+json",
            "Basic $encoding",
            Conekta.language,
            pThis.nameValuePairs
        )
        return call.execute().body() ?: ""
      }

      override fun onPreExecute() {
      }

      override fun onPostExecute(result: String) {
        pThis.listener?.invoke(result)
      }

    }

    private fun runTask(pThis: Connection) {
      Task(pThis).execute()
    }

  }
}
