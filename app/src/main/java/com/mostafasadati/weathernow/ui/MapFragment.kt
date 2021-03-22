package com.mostafasadati.weathernow.ui

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.webkit.*
import androidx.fragment.app.Fragment
import com.mostafasadati.weathernow.R
import com.mostafasadati.weathernow.Setting.Companion.lat
import com.mostafasadati.weathernow.Setting.Companion.lon
import com.mostafasadati.weathernow.Status
import com.mostafasadati.weathernow.databinding.MapFragmentBinding
import kotlinx.android.synthetic.main.map_fragment.*

class MapFragment : Fragment(R.layout.map_fragment) {
    var status = Status.START
    lateinit var binding: MapFragmentBinding
    private var url =
        "https://openweathermap.org/weathermap?basemap=map&cities=false&layer=temperature&lat=" +
                lat + "&lon=" + lon + "&zoom=5"

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = MapFragmentBinding.bind(view)
        binding.status = status

        web_view.settings.javaScriptEnabled = true

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            CookieManager.getInstance().setAcceptThirdPartyCookies(web_view, true)
            CookieManager.getInstance().setCookie(url, "stick-footer-panel=false")
        }

        web_view.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                status = Status.LOADING
                binding.status = status
            }

            override fun onLoadResource(view: WebView?, url: String?) {
                super.onLoadResource(view, url)
                try {
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

                    web_view?.loadUrl(
                        "javascript:(function() { " +
                                "document.getElementsByClassName('leaflet-top leaflet-right')[0].style.display='none'; })()"
                    )

                    web_view?.loadUrl(
                        "javascript:(function() { " +
                                "document.getElementsByClassName('scale-details')[0].style.display='none'; })()"
                    )

                    web_view?.loadUrl(
                        "javascript:(function() { " +
                                "document.getElementsByClassName('leaflet-control-container)[0].style.display='none'; })()"
                    )

                } catch (e: Exception) {
                    e.printStackTrace()
                    status = Status.ERROR
                    binding.status = status
                }
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                status = Status.SUCCESS
                binding.status = status
            }

            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?
            ) {
                super.onReceivedError(view, request, error)
                status = Status.ERROR
                binding.status = status
            }
        }

        web_view.loadUrl(url)
        web_view.requestFocus(View.FOCUS_DOWN)

        windButton.setOnClickListener {
            newUrl(url,"windspeed")
        }
        rainButton.setOnClickListener {
            newUrl(url,"precipitation")
        }
        tempButton.setOnClickListener {
            newUrl(url,"temperature")
        }
        cloudsButton.setOnClickListener {
            newUrl(url,"clouds")
        }
        pressureButton.setOnClickListener {
            newUrl(url,"pressure")
        }


    }

    private fun newUrl(q: String, layer: String) {
        var out = ""
        val newUrl = q.split("layer=".toRegex()).toTypedArray()
        val newUrl2 = newUrl[1].split("&".toRegex()).toTypedArray()
        out = newUrl[0] + "layer=" + layer + "&" + newUrl2[1] + "&" + newUrl2[2] + "&" + newUrl2[3]
        web_view.loadUrl(out)
    }


}