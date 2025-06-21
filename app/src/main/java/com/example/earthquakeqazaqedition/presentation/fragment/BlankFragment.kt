package com.example.earthquakeqazaqedition.presentation.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.android.volley.Request
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.earthquakeqazaqedition.R
import com.google.gson.Gson
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.compass.CompassOverlay
import java.text.SimpleDateFormat
import java.util.*

class BlankFragment : Fragment() {

    private lateinit var map: MapView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Загружаем layout для фрагмента
        return inflater.inflate(R.layout.map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Настройка OSM
        val ctx = requireContext().applicationContext
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx))

        // Инициализация карты
        map = view.findViewById(R.id.map)
        map.setTileSource(TileSourceFactory.MAPNIK)
        map.setMultiTouchControls(true)

        val city = GeoPoint(43.2220, 76.8512)
        val mapController = map.controller
        mapController.setZoom(9.5)
        mapController.setCenter(city)

        // Добавление компаса
        val compassOverlay = CompassOverlay(requireContext(), map)
        compassOverlay.enableCompass()
        map.overlays.add(compassOverlay)

        // URL для получения данных
        val url =
            "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&latitude=43.2220&longitude=76.8512&maxradiuskm=1000&starttime=2022-01-01&minmagnitude=1"

        fetchEarthquakeData(url)
    }

    private fun fetchEarthquakeData(url: String) {
        val requestQueue = Volley.newRequestQueue(requireContext())

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                try {
                    val gson = Gson()
                    val earthquakeData = gson.fromJson(response.toString(), EarthquakeData::class.java)

                    earthquakeData.features.forEach { feature ->
                        val latitude = feature.geometry.coordinates[1]
                        val longitude = feature.geometry.coordinates[0]
                        val timeInMillis = feature.properties.time

                        val formattedTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                            .format(Date(timeInMillis))

                        // Создаем маркер
                        val marker = Marker(map).apply {
                            title = "Магнитуда: ${feature.properties.mag}"
                            position = GeoPoint(latitude, longitude)
                            setOnMarkerClickListener { _, _ ->
                                AlertDialog.Builder(requireContext())
                                    .setTitle("Жер сілкінісі туралы ақпарат")
                                    .setMessage(
                                        """
                                        Магнитуда: ${feature.properties.mag}
                                        Мекен-жайы: ${feature.properties.place}
                                        Уақыт: $formattedTime
                                        """.trimIndent()
                                    )
                                    .setPositiveButton("OK", null)
                                    .show()
                                true
                            }
                        }
                        map.overlays.add(marker)
                    }
                    map.invalidate()

                } catch (e: Exception) {
                    Log.e("APILOG", "Error parsing data: ${e.message}")
                }
            },
            { error: VolleyError ->
                Log.e("APILOG", "Error fetching data: $error")
            })

        requestQueue.add(jsonObjectRequest)
    }
}

// Модели данных для работы с JSON
data class EarthquakeData(
    val features: List<Feature>
)

data class Feature(
    val geometry: Geometry,
    val properties: Properties
)

data class Geometry(
    val coordinates: List<Double>
)

data class Properties(
    val mag: Double,
    val place: String,
    val time: Long
)
