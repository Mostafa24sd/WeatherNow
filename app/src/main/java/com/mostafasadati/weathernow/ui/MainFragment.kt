package com.mostafasadati.weathernow.ui

import android.content.Intent
import android.content.res.AssetFileDescriptor
import android.content.res.AssetManager
import android.media.MediaPlayer
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.mostafasadati.weathernow.R
import com.mostafasadati.weathernow.Setting
import com.mostafasadati.weathernow.SettingData
import com.mostafasadati.weathernow.Status
import com.mostafasadati.weathernow.data.ForecastAdapter
import com.mostafasadati.weathernow.databinding.MainFragmentBinding
import com.mostafasadati.weathernow.model.CurrentWeather
import com.mostafasadati.weathernow.model.ForecastWeather
import com.mostafasadati.weathernow.viewmodel.WeatherViewModel
import com.mostafasadati.weathernow.widgets.WidgetCurrentProvider
import com.mostafasadati.weathernow.widgets.WidgetForecastProvider
import com.mostafasadati.weathernow.widgets.WidgetPollutionProvider
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.current_top_state.*
import kotlinx.android.synthetic.main.main_fragment.*
import java.util.*

@AndroidEntryPoint
class MainFragment : Fragment(R.layout.main_fragment) {

    private val args: MainFragmentArgs by navArgs()

    private lateinit var mediaPlayer: MediaPlayer

    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<WeatherViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        requireContext().theme.applyStyle(R.style.Theme_WeatherNow, true)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)

        _binding = MainFragmentBinding.bind(view)

        if (args.SearchResult != null) {
            if (args.SearchResult!!.local_names != null)
                Setting.city = args.SearchResult!!.local_names!!.feature_name!!
            else
                Setting.city = args.SearchResult!!.name

            Setting.country = args.SearchResult!!.country
            Setting.lat = args.SearchResult!!.lat
            Setting.lon = args.SearchResult!!.lon
            SettingData.saveSetting(requireContext())
        }

        binding.status = Status.LOADING

        mediaPlayer = MediaPlayer()

        loadData()

        pollution_layout.setOnClickListener {
            if (expandable_layout_0.isExpanded)
                expandable_layout_0.collapse()
            else
                expandable_layout_0.expand()
        }

    }

    private fun loadData() {
        viewModel.getCurrent()
            .observe(viewLifecycleOwner) {

                binding.status = it.status

                when (it.status) {
                    Status.LOADING_DB_FULL -> {
                        setCurrentData(it.data!!)
                    }
                    Status.ERROR -> {
                        Toast.makeText(
                            context,
                            getString(R.string.network_error),
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                    Status.SUCCESS -> {
                        setCurrentData(it.data!!)
                        Toast.makeText(context, "Updated", Toast.LENGTH_SHORT).show()
                    }
                }
            }

        viewModel.getForecast()
            .observe(viewLifecycleOwner) {
                when (it.status) {
                    Status.LOADING_DB_FULL -> {
                        setForecastData(it.data!!)
                    }
                    Status.ERROR -> {
                        Toast.makeText(
                            context,
                            getString(R.string.network_error),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    Status.SUCCESS -> {
                        setForecastData(it.data!!)
                    }
                }
            }

        viewModel.getPollution()
            .observe(viewLifecycleOwner) {
                when (it.status) {
                    Status.LOADING_DB_FULL -> {
                        binding.pollution = it.data
                    }
                    Status.ERROR -> {
                        Toast.makeText(
                            context,
                            getString(R.string.network_error),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    Status.SUCCESS -> {
                        binding.pollution = it.data
                    }
                }
            }
    }

    private fun setCurrentData(it: CurrentWeather) {
        binding.currentWeather = it

        if (Setting.audio)
            setAudioResource(it)

        last_update_txt.text = getDate(Setting.lastUpdate)

        refreshWidgets()
    }

    private fun setAudioResource(currentWeather: CurrentWeather) {
        when (currentWeather.weather[0].icon) {
            "01d" -> playAudio("sounds/sunny.ogg")
            "01n" -> playAudio("sounds/moon.ogg")
            "09n" -> playAudio("sounds/heavy_rain.ogg")
            "10n" -> playAudio("sounds/medium_rain.ogg")
            "11n" -> playAudio("sounds/thunder.ogg")
            "50n" -> playAudio("sounds/mist.ogg")
            else -> stopAudio()
        }
    }

    private fun refreshWidgets() {
        val updateWidgetIntent = Intent(context, WidgetCurrentProvider::class.java)
        requireContext().sendBroadcast(updateWidgetIntent)

        val updateFWidgetIntent = Intent(context, WidgetForecastProvider::class.java)
        requireContext().sendBroadcast(updateFWidgetIntent)

        val updatePWidgetIntent = Intent(context, WidgetPollutionProvider::class.java)
        requireContext().sendBroadcast(updatePWidgetIntent)
    }

    private fun setForecastData(it: ForecastWeather) {
        val adapter = ForecastAdapter(it)

        forecast_recyclerview.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        forecast_recyclerview.adapter = adapter
    }

    private fun playAudio(name: String) {

        val asset: AssetManager = requireContext().assets
        val afd: AssetFileDescriptor = asset.openFd(name)

        mediaPlayer.reset()
        mediaPlayer.setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
        mediaPlayer.prepare()

        afd.close()

        if (mediaPlayer.audioSessionId != 0)
            mediaPlayer.start()

    }

    private fun stopAudio() {
        if (::mediaPlayer.isInitialized && mediaPlayer.isPlaying) {
            mediaPlayer.stop()
            mediaPlayer.release()
        }
    }

    override fun onPause() {
        super.onPause()
        if (this::mediaPlayer.isInitialized) {
            if (mediaPlayer.isPlaying)
                mediaPlayer.pause()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.main_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_search -> {
                openSearch()
                true
            }
            R.id.refresh -> {
                Setting.SHOULD_UPDATE = true
                loadData()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun openSearch() {
        val action = MainFragmentDirections.actionMainFragmentToSearchFragment()
        findNavController().navigate(action)
    }

    private fun getDate(lastTime: Long): String {
        val timeNow = Calendar.getInstance(TimeZone.getDefault()).timeInMillis

        val diff = timeNow - lastTime

        val diffSeconds = diff / 1000 % 60
        val diffMinutes = diff / (60 * 1000) % 60
        val diffHours = diff / (60 * 60 * 1000) % 24
        val diffDays = diff / (24 * 60 * 60 * 1000)

        var out = getString(R.string.last_update)

        if (diffDays > 0) {
            if (diffDays == 1L)
                out += " $diffDays " + getString(R.string.day)
            if (diffDays > 1)
                out += " $diffDays " + getString(R.string.days)
        } else if (diffHours > 0) {
            if (diffHours == 1L)
                out += " $diffHours " + getString(R.string.hour)
            if (diffHours > 1)
                out += " $diffHours " + getString(R.string.hours)
        } else if (diffMinutes > 0) {
            if (diffMinutes == 1L)
                out += " $diffMinutes " + getString(R.string.min)
            if (diffMinutes > 1)
                out += " $diffMinutes " + getString(R.string.mins)
        } else if (diffSeconds > 0) {
            if (diffSeconds == 1L)
                out += " $diffSeconds " + getString(R.string.sec)
            if (diffSeconds > 1)
                out += " $diffSeconds " + getString(R.string.secs)
        } else if (diffSeconds == 0L) return out + getString(R.string.just_now)

        Setting.lastUpdate = timeNow
        SettingData.saveSetting(requireContext())

        return "$out " + getString(R.string.ago)
    }

    override fun onDestroy() {
        super.onDestroy()
        stopAudio()
        _binding = null
    }
}