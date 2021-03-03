package com.mostafasadati.weathernow.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.*
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
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private lateinit var bindings: SearchFragmentBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindings = SearchFragmentBinding.bind(view)

        bindings.status = Status.START

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        gps.setOnClickListener {
            checkPermission()
        }

        setHasOptionsMenu(true)
    }

    private fun checkLocation() {

        val manager = context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            showAlertLocation()
        }
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        getLocationUpdates()
    }

    private fun getLocationUpdates() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        locationRequest = LocationRequest()
        locationRequest.interval = 50000
        locationRequest.fastestInterval = 50000
        locationRequest.smallestDisplacement = 170f //170 m = 0.1 mile
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return
                if (locationResult.locations.isNotEmpty()) {
                    val location = locationResult.lastLocation
                    searchByGPS(location)
                }
            }
        }
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

    private fun showAlertLocation() {
        val dialog = AlertDialog.Builder(context)
        dialog.setMessage("Your location settings is set to Off, Please enable location to search by gps")
        dialog.setPositiveButton("Settings") { _, _ ->
            val myIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivity(myIntent)
        }
        dialog.setNegativeButton("Cancel") { _, _ ->
        }
        dialog.setCancelable(false)
        dialog.show()

    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {

        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            null
        )
    }

    private fun stopLocationUpdates() {
        if (this::locationCallback.isInitialized)
            fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
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
                checkLocation()
                startLocationUpdates()
            }
        })
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

    override fun onDetach() {
        hideKeyboard()
        super.onDetach()
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