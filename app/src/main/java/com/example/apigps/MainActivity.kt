package com.example.apigps

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import com.example.gps.databinding.ActivityMainBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var mFusedLocationClient: FusedLocationProviderClient

    companion object{
        const val PERMISSION_ID = 33
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        binding.btnLocate.setOnClickListener {
            getLocation()
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLocation(){
        if(checkPermissions()){
            if(isLocationEnable()){
                mFusedLocationClient.lastLocation.addOnSuccessListener(this){
                    binding.tvLatitude.text = it?.latitude.toString()
                    binding.tvLongitude.text = it?.longitude.toString()
                }
            }
        }else{
            requestPermissions()
        }
    }

    //pedir permisos
    private fun requestPermissions(){
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION),
            PERMISSION_ID
        )
    }

    //verificar que el gps este encendido
    private fun isLocationEnable():Boolean{
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    private fun checkPermissions():Boolean{
        return checkGranted(Manifest.permission.ACCESS_FINE_LOCATION)
                && checkGranted(Manifest.permission.ACCESS_COARSE_LOCATION)
    }

    private fun checkGranted(permission: String):Boolean{
        return ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
    }
}