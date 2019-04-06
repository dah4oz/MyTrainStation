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
import com.example.mytrainstation.ui.StationFragment
import com.example.mytrainstation.ui.TrainMovementFragment
import okhttp3.ResponseBody

class MainActivity : AppCompatActivity(), StationFragment.TrainSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .add(R.id.content, StationFragment.newInstance(), null)
                .commit()
        }
    }

    override fun onTrainSelected(trainMovement: List<TrainMovement>) {
        Log.d("MainActivity", "Selected train movement size ${trainMovement.size}")

        val fragment = TrainMovementFragment.newInstance()
        fragment.setData(trainMovement)

        supportFragmentManager
            .beginTransaction()
            .add(R.id.content, fragment, null)
            .commit()
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
