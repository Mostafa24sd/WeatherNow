package com.mostafasadati.weathernow.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.IntentSender
import android.location.Location
import android.os.Bundle
import android.os.Looper.getMainLooper
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import com.mostafasadati.weathernow.R
import com.mostafasadati.weathernow.Resource
import com.mostafasadati.weathernow.Setting
import com.mostafasadati.weathernow.Status
import com.mostafasadati.weathernow.data.SearchCityAdapter
import com.mostafasadati.weathernow.databinding.SearchFragmentBinding
import com.mostafasadati.weathernow.model.SearchCity
import com.mostafasadati.weathernow.viewmodel.SearchViewModel
import com.nabinbhandari.android.permissions.PermissionHandler
import com.nabinbhandari.android.permissions.Permissions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.search_fragment.*

@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.search_fragment) {
    private val viewModel by viewModels<SearchViewModel>()
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var request: LocationRequest
    private lateinit var builder: LocationSettingsRequest.Builder
    private lateinit var locationCallback: LocationCallback
    private lateinit var bindings: SearchFragmentBinding

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

        bindings = SearchFragmentBinding.bind(view)

        bindings.status = Status.START

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        gps.setOnClickListener {
            hideKeyboard()
            checkPermission()
        }

        setHasOptionsMenu(true)
    }

    private fun searchByGPS(location: Location) {
        viewModel.searchByGPS(latitude = location.latitude, longitudes = location.longitude)
            .observe(viewLifecycleOwner) {
                setData(it)
            }
    }

    private fun searchByName(query: String) {
        viewModel.searchByName(query)
            .observe(viewLifecycleOwner) {
                setData(it)
            }
    }

    private fun checkPermission() {

        val permissions =
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )

        Permissions.check(requireContext(), permissions, null, null, object : PermissionHandler() {
            override fun onGranted() {
                bindings.status = Status.LOADING

                request = LocationRequest.create()
                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)

                builder = LocationSettingsRequest.Builder().addLocationRequest(request)

                val result: Task<LocationSettingsResponse> =
                    LocationServices.getSettingsClient(context)
                        .checkLocationSettings(builder.build())

                result.addOnFailureListener {
                    if (it is ResolvableApiException) {
                        try {
                            val resolvable = it
                            resolvable.startResolutionForResult(requireActivity(), 8990)
                        } catch (ex: IntentSender.SendIntentException) {
                            ex.printStackTrace()
                        }
                    }
                }
                locationCallback = object : LocationCallback() {
                    override fun onLocationResult(locationResult: LocationResult?) {
                        locationResult ?: return

                        if (locationResult.locations.isNotEmpty()) {
                            val location = locationResult.lastLocation
                            searchByGPS(location)
                            return
                        }
                    }
                }
                startLocationUpdates()
            }
        })
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        if (this::fusedLocationClient.isInitialized && this::request.isInitialized && this::locationCallback.isInitialized)
            fusedLocationClient.requestLocationUpdates(
                request,
                locationCallback,
                getMainLooper()
            )
    }

    override fun onResume() {
        super.onResume()
        startLocationUpdates()
    }

    override fun onPause() {
        super.onPause()
        if (this::fusedLocationClient.isInitialized && this::locationCallback.isInitialized)
            fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.search_menu, menu)

        val searchItem = menu.findItem(R.id.searchview)

        val searchView = searchItem.actionView as SearchView

        searchView.onActionViewExpanded()

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null && query.trim().isNotEmpty()) {
                    searchByName(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty()) {
                    bindings.status = Status.START
                }
                return false
            }
        })
    }

    private fun setRecyclerview(data: List<SearchCity>) {
        search_result_recyclerview.adapter = SearchCityAdapter(data) {
            Setting.SHOULD_UPDATE = true
            val action = SearchFragmentDirections.actionSearchFragmentToMainFragment(it)
            findNavController().navigate(action)
        }
    }

    private fun setData(it: Resource<List<SearchCity>>) {
        bindings.status = it.status

        when (it.status) {
            Status.SUCCESS -> {
                if (it.data != null && it.data.isNotEmpty()) {
                    setRecyclerview(it.data)
                } else
                    bindings.status = Status.NOT_FOUND
            }
        }
    }

    override fun onDestroy() {
        hideKeyboard()
        super.onDestroy()
    }

    private fun hideKeyboard() {
        val imm: InputMethodManager =
            requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager

        var view = requireActivity().currentFocus
        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}