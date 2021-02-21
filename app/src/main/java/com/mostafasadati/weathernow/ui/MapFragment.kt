package com.mostafasadati.weathernow.ui

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.webkit.CookieManager
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.mostafasadati.weathernow.R
import com.mostafasadati.weathernow.Setting.Companion.lat
import com.mostafasadati.weathernow.Setting.Companion.lon
import kotlinx.android.synthetic.main.map_fragment.*

class MapFragment : Fragment(R.layout.map_fragment) {
    var loadingError = false

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val url =
            "https://openweathermap.org/weathermap?basemap=map&cities=false&layer=temperature&lat=" +
                    lat + "&lon=" + lon + "&zoom=5"

        web_view.settings.javaScriptEnabled = true

        CookieManager.getInstance().setAcceptThirdPartyCookies(web_view, true)
        CookieManager.getInstance().setCookie(url, "stick-footer-panel=false")

        web_view.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                loadingError = false
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                if (!loadingError) {
                    web_view?.loadUrl(
                        "javascript:(function() { " +
                                "document.getElementsByClassName('stick-footer-panel')[0].style.display='none'; })()"
                    )
                    web_view?.loadUrl(
                        "javascript:(function() { " +
                                "document.getElementsByClassName('leaflet-control-attribution leaflet-control')[0].style.display='none'; })()"
                    )

                    web_view?.loadUrl(
                        "javascript:(function() { " +
                                "document.getElementById('header-website').style.display='none';})()"
                    )
                }

                //leaflet-control-attribution leaflet-control
            }
        }

        web_view.loadUrl(url)
        web_view.requestFocus(View.FOCUS_DOWN)


    }

}