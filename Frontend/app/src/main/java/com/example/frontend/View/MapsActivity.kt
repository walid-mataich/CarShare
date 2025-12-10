package com.example.frontend.View

import android.content.Intent
import android.location.Geocoder
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.frontend.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.util.Locale

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment

        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {

        mMap = googleMap

        val elJadida = LatLng(33.2541, -8.5010)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(elJadida, 12f))

        mMap.setOnMapClickListener { latLng ->

            mMap.clear()

            mMap.addMarker(
                MarkerOptions()
                    .position(latLng)
                    .title("Position sélectionnée")
            )

            // ✅ Conversion coordonnées -> adresse réelle
            val geocoder = Geocoder(this, Locale.getDefault())

            val addressList = geocoder.getFromLocation(
                latLng.latitude,
                latLng.longitude,
                1
            )

            val address = if (!addressList.isNullOrEmpty()) {
                addressList[0].getAddressLine(0)
            } else {
                "Adresse inconnue"
            }

            val resultIntent = Intent().apply {
                putExtra("lat", latLng.latitude)
                putExtra("lng", latLng.longitude)
                putExtra("address", address)
            }

            setResult(RESULT_OK, resultIntent)
            finish()
        }
    }
}
