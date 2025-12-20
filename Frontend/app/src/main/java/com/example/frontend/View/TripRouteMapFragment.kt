package com.example.frontend.View

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.frontend.R
import com.example.frontend.model.Trip
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions

class TripRouteMapFragment : Fragment(), OnMapReadyCallback {

    private lateinit var trip: Trip
    private lateinit var mMap: GoogleMap

    companion object {
        fun newInstance(trip: Trip): TripRouteMapFragment {
            val fragment = TripRouteMapFragment()
            fragment.arguments = Bundle().apply {
                putParcelable("trip", trip)
            }
            return fragment
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        trip = requireArguments().getParcelable("trip")!!
    }

    override fun onCreateView(
        inflater: android.view.LayoutInflater,
        container: android.view.ViewGroup?,
        savedInstanceState: Bundle?
    ): android.view.View {
        return inflater.inflate(R.layout.fragment_trip_route_map, container, false)
    }

    override fun onViewCreated(view: android.view.View, savedInstanceState: Bundle?) {
        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val origin = LatLng(trip.originLat, trip.originLng)
        val destination = LatLng(trip.destinationLat, trip.destinationLng)

        mMap.addMarker(
            MarkerOptions()
                .position(origin)
                .title("Origine")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)) // vert
        )

        mMap.addMarker(
            MarkerOptions()
                .position(destination)
                .title("Destination")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)) // rouge
        )

        mMap.animateCamera(
            CameraUpdateFactory.newLatLngZoom(origin, 11f)
        )
    }
}
