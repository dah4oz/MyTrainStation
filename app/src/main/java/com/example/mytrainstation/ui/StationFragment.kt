package com.example.mytrainstation.ui

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.example.mytrainstation.DateUtil
import com.example.mytrainstation.R
import com.example.mytrainstation.http.BackgroundWorker
import com.example.mytrainstation.http.HttpClient
import com.example.mytrainstation.http.ResponseHandler
import com.example.mytrainstation.model.StationData
import com.example.mytrainstation.model.TrainMovement
import com.example.mytrainstation.parser.StationParser
import com.example.mytrainstation.parser.TrainParser
import okhttp3.ResponseBody

class StationFragment : Fragment(), TrainAdapter.TrainItemClicked {

    val loadMockData = false

    val sourceCode = "ARKLW"

    val destinationCode = "SKILL"

    var arklowStationData: List<StationData> = ArrayList()

    var movementMap = HashMap<String, List<TrainMovement>>()

    private var noTrainTv: TextView? = null

    private var recyclerView: RecyclerView? = null

    private var progress: ProgressBar? = null

    private var adapter: TrainAdapter? = null

    private lateinit var callback: TrainSelectedListener

    interface TrainSelectedListener {
        fun onTrainSelected(trainMovement: List<TrainMovement>)
    }

    companion object {

        fun newInstance(): StationFragment {
            return StationFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view  = inflater.inflate(R.layout.fragment_station, container, false)

        recyclerView = view.findViewById(R.id.recycler)
        recyclerView?.layoutManager = LinearLayoutManager(activity)

        progress = view.findViewById(R.id.progress)
        noTrainTv = view.findViewById(R.id.no_train_tv)

        return view
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if (context is TrainSelectedListener) {
            callback = context
        } else {
            throw ClassCastException(context.toString() + " must implement TrainSelectedListener.")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadData()
    }

    override fun onTrainClicked(code: String) {
        Log.d("StationFragment", "CLicked train code $code")

        val trainMovement = movementMap[code]

        if(trainMovement != null) {
            callback.onTrainSelected(trainMovement)
        }
    }

    private fun updateUI() {
        if(adapter != null) {
            adapter?.setTrainData(arklowStationData)
        } else {
            adapter = TrainAdapter(arklowStationData.toMutableList(), context!!)
            adapter?.setTrainClickListener(this)
            recyclerView?.adapter = adapter
        }
    }

    private fun loadData() {
        if(loadMockData) {
            loadStationDataMock()
        } else {
            retrieveStationData(sourceCode)
        }
    }

    private fun checkActiveTrains(arklowStation: List<StationData>) {
        for(item in arklowStation) {
            if(!item.trainCode.isNullOrBlank()) {
                getTrainMovement(item.trainCode!!, DateUtil.currentDateFormatted())
            }
        }
    }

    private fun loadStationDataMock() {
        val inputStream = resources.openRawResource(R.raw.arklw_station)
        val parser = StationParser()
        arklowStationData = parser.serializeStationDataXmlInput(inputStream)
        checkActiveTrains(arklowStationData)
    }

    private fun retrieveStationData(byCode: String) {
        if(!HttpClient.checkConnectivity(context!!)) {
            showDialog()
            return
        }

        progress?.visibility = View.VISIBLE

        val service = HttpClient.provideHttpClient()
        val request = service.getStationData(byCode)

        val worker = BackgroundWorker()

        worker.executeTask(request, object: ResponseHandler {

            override fun handleError(error: String) {
                Toast.makeText(context, "Exception $error", Toast.LENGTH_SHORT).show()
                progress?.visibility = View.GONE
                showNOTrainInfo()
            }

            override fun handleResponse(response: ResponseBody?) {
                progress?.visibility = View.GONE

                if(response != null) {
                    val received = response.byteStream()
                    val parser = StationParser()
                    val stationDataList = parser.serializeStationDataXmlInput(received)

                    if(!stationDataList.isNullOrEmpty()) {
                        arklowStationData = stationDataList
                        checkActiveTrains(stationDataList)
                    } else {
                        showNOTrainInfo()
                    }
                }
            }

        })
    }

    private fun getTrainMovement(trainCode: String, date: String) {
        val service = HttpClient.provideHttpClient()
        var request = service.getTrainMovement(trainCode, date)
        val worker = BackgroundWorker()

        val handler = object: ResponseHandler {

            override fun handleError(error: String) {
                Toast.makeText(context, "Exception $error", Toast.LENGTH_SHORT).show()
            }

            override fun handleResponse(response: ResponseBody?) {
                if(response != null) {
                    val received = response.byteStream()
                    val parser = TrainParser()
                    //var trainMovementData: List<TrainMovement> = parser.parseTrainMovement2(response!!.byteStream())
                    val trainMovementList = parser.parseTrainMovement2(received)
                    if(checkIfTrainGoesToShankill(trainMovementList)) {
                        movementMap[trainCode] = trainMovementList
                        Log.d("StationFragment", "@@@MapSize -> ${movementMap.size}")
                        updateUI()
                    }
                }
            }

        }

        worker.executeTask(request, handler)
    }

    private fun checkIfTrainGoesToShankill(movement: List<TrainMovement>): Boolean {
        for(item in movement) {
            if(item.locationCode.equals(destinationCode, true)) {
                return true
            }
        }

        return false
    }

    private fun showDialog() {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Error")
        builder.setMessage("Check your data connection!")

        builder.setNeutralButton("OK") { dialog, which ->
            dialog.dismiss()
        }
        builder.show()
    }

    private fun showNOTrainInfo() {
        noTrainTv?.visibility = View.VISIBLE
        recyclerView?.visibility = View.GONE
    }
}