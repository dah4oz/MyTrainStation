package com.example.mytrainstation

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.example.mytrainstation.http.BackgroundWorker
import com.example.mytrainstation.http.HttpClient
import com.example.mytrainstation.http.ResponseHandler
import com.example.mytrainstation.model.Station
import com.example.mytrainstation.model.StationData
import com.example.mytrainstation.model.TrainMovement
import com.example.mytrainstation.parser.StationParser
import com.example.mytrainstation.parser.TrainParser
import okhttp3.ResponseBody

class MainActivity : AppCompatActivity() {

    //val TAG = MainActivity::class.simpleName

    val sourceCode = "ARKLW"
    val destinationCode = "SKILL"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //retrieveStationData(sourceCode)
        //retrieveStationData(destinationCode)

        //getTrainMovement("A609", DateUtil.currentDateFormatted())
        getRunningTrains()
    }

    fun retrieveStationList() {
        val service = HttpClient.provideHttpClient()
        val request = service.getStations()

        val worker = BackgroundWorker()

        val handler = object : ResponseHandler {
            override fun handleResponse(response: ResponseBody?) {
                if(response != null) {
                    //Log.d("MainActivity", "Response = ${response}")
                    var stations: List<Station>
                    val parser = StationParser()
                    stations = parser.parseStation(response!!.byteStream())

                    Log.d("MainActivity", "Parsed stations: ${stations.size}")
                    Log.d("MainActivity", "First station name: ${stations[0].name}")
                }
            }

            override fun handleError(error: String) {
                Toast.makeText(applicationContext, "Exception $error", Toast.LENGTH_SHORT).show()
            }
        }

        worker.executeTask(request, handler)
    }

    private fun retrieveStationData(byCode: String) {
        val service = HttpClient.provideHttpClient()
        val request = service.getStationData(byCode)

        val worker = BackgroundWorker()
        val handler = object: ResponseHandler {

            override fun handleError(error: String) {
                Toast.makeText(applicationContext, "Exception $error", Toast.LENGTH_SHORT).show()
            }

            override fun handleResponse(response: ResponseBody?) {
                if(response != null) {
                    val parser = StationParser()
                    var stationData: List<StationData> = parser.parseStationData(response!!.byteStream())

                    if(!stationData.isEmpty()) {
                        Log.d("MainActivity", "Station code = $byCode fetched data size = ${stationData.size}")
                        Log.d("MainActivity", "Last train code info ${stationData[stationData.size - 1].trainCode}")
                    } else {
                        Log.d("MainActivity", "NO train info found for station code $byCode")
                    }
                }
            }

        }

        worker.executeTask(request, handler)
    }

    private fun getTrainMovement(trainCode: String, date: String) {
        val service = HttpClient.provideHttpClient()
        //var request = service.getTrainMovement("A609", "03 apr 2019")
        var request = service.getTrainMovement(trainCode, date)
        val worker = BackgroundWorker()

        val handler = object: ResponseHandler {

            override fun handleError(error: String) {
                Toast.makeText(applicationContext, "Exception $error", Toast.LENGTH_SHORT).show()
            }

            override fun handleResponse(response: ResponseBody?) {
                val parser = TrainParser()
                var trainMovementData: List<TrainMovement> = parser.parseTrainMovement2(response!!.byteStream())

                if(!trainMovementData.isEmpty()) {
                    Log.d("MainActivity", "Train code = $trainCode fetched data size = ${trainMovementData.size}")
                    Log.d("MainActivity", "Last location info ${trainMovementData[trainMovementData.size - 1].locationFullName}")
                } else {
                    Log.d("MainActivity", "NO train info found for station code $trainCode")
                }
            }

        }

        worker.executeTask(request, handler)
    }

    private fun getRunningTrains() {
        val service = HttpClient.provideHttpClient()
        var request = service.getRunningTrains()
        val worker = BackgroundWorker()
        worker.executeTask(request, object: ResponseHandler {
            override fun handleResponse(response: ResponseBody?) {
                if(response != null) {
                    val parser = TrainParser()
                    var runningTrains = parser.serializeTrainXmlInput(response.byteStream())

                    if(!runningTrains.isEmpty()) {
                        Log.d("MainActivity", "Last train info =  ${runningTrains[runningTrains.size - 1].publicMessage}")
                    }
                }
            }

            override fun handleError(error: String) {
                Toast.makeText(applicationContext, "Exception $error", Toast.LENGTH_SHORT).show()
            }

        })
    }
}
