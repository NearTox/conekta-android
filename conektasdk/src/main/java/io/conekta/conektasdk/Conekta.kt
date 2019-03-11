package io.conekta.conektasdk

import android.os.Build
import android.provider.Settings
import android.webkit.CookieManager
import android.webkit.WebView

import androidx.appcompat.app.AppCompatActivity

object Conekta {
  var apiVersion = "0.3.0"
  const val baseUri = "https://api.conekta.io"
  var language = "es"
  var publicKey = ""

  fun collectDevice(activity: AppCompatActivity) {
    val sessionId = Conekta.deviceFingerPrint(activity)
    if(publicKey.isEmpty())
      throw RuntimeException("publicKey empty")

    val html = "<!DOCTYPE html><html><head></head><body style=\"background: blue;\"><script type=\"text/javascript\" src=\"https://conektaapi.s3.amazonaws.com/v0.5.0/js/conekta.js\" data-conekta-public-key=\"$publicKey\" data-conekta-session-id=\"$sessionId\"></script></body></html>"

    val webView = WebView(activity)

    CookieManager.getInstance().setAcceptCookie(true)
    if(Build.VERSION.SDK_INT >= 21)
      CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true)

    webView.settings.javaScriptEnabled = true
    webView.settings.allowContentAccess = true
    webView.settings.databaseEnabled = true
    webView.settings.domStorageEnabled = true
    webView.loadDataWithBaseURL("https://conektaapi.s3.amazonaws.com/v0.5.0/js/conekta.js", html, "text/html", "UTF-8", null)
  }

  fun deviceFingerPrint(activity: AppCompatActivity): String {
    return Settings.Secure.getString(activity.applicationContext.contentResolver, Settings.Secure.ANDROID_ID)
  }
}