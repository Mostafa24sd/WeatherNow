package com.mostafasadati.weathernow.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Looper.getMainLooper
import android.provider.Settings
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.mostafasadati.weathernow.R
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.mostafasadati.weathernow.*
import com.mostafasadati.weathernow.data.SearchCityAdapter
import com.mostafasadati.weathernow.databinding.SearchFragmentBinding
import com.mostafasadati.weathernow.model.SearchCity
import com.mostafasadati.weathernow.viewmodel.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.search_fragment.*


@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.search_fragment) {
    private val LOCATION_REQUEST_CODE = 2
    val permissions = arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    )

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
            view.hideKeyboard()
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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            when {
                hasPermissions(requireContext(), *permissions) -> startGPS()
                shouldShowRequestPermissionRationale(permissions[0]) -> permissionExplanation()
                shouldShowRequestPermissionRationale(permissions[1]) -> permissionExplanation()
                else -> requestPermissions(
                    permissions,
                    LOCATION_REQUEST_CODE
                )
            }
        }
    }

    private fun startGPS() {
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

    override fun onStop() {
        super.onStop()
        view?.hideKeyboard()
    }

    private fun hasPermissions(context: Context, vararg permissions: String): Boolean =
        permissions.all {
            ActivityCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == LOCATION_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                startGPS()
            } else {
                val showRationale = shouldShowRequestPermissionRationale(permissions[0])
                if (!showRationale) {
                    //Never ask again
                    permissionExplanation()
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun openPermissionSetting() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri: Uri = Uri.fromParts("package", activity?.packageName, null)
        intent.data = uri
        startActivityForResult(intent, 0)
    }

    private fun permissionExplanation() {
        val alertDialog: AlertDialog? = activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle(getString(R.string.location_required))
            builder.setMessage(getString(R.string.access_to_gps))
            builder.apply {
                setPositiveButton(
                    R.string.ok
                ) { dialog, _ ->
                    dialog.dismiss()
                    if (shouldShowRequestPermissionRationale(permissions[0]) || shouldShowRequestPermissionRationale(
                            permissions[1]
                        )
                    )
                        requestPermissions(
                            permissions,
                            LOCATION_REQUEST_CODE
                        )
                    else
                        openPermissionSetting()
                }
                setNegativeButton(
                    R.string.cancel
                ) { dialog, _ ->
                    dialog.dismiss()
                }
            }
            builder.create()
            builder.show()
        }
    }
}