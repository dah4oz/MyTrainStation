package com.example.mytrainstation.http

import android.util.Log
import android.widget.Toast
import com.example.mytrainstation.model.StationData
import com.example.mytrainstation.parser.StationParser
import kotlinx.coroutines.*
import okhttp3.ResponseBody
import retrofit2.Response

class BackgroundWorker {

     fun executeTask(request: Deferred<Response<ResponseBody>>, handler: ResponseHandler) {
        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.Main) {
                try {
                    val response = request.await()

                    if(response.isSuccessful) {
                        //Log.d("BackgroundWorker", "Body: ${response.body()!!.string()}")
                        handler.handleResponse(response.body())
                    } else {
                        Log.d("BackgroundWorker", "Error getting data ${response.message()}")
                        handler.handleError("Error fetching data - ${response.message()}")
                    }
                } catch (e: Throwable) {
                    Log.d("BackgroundWorker", "Exception ${e.message}}")
                    handler.handleError("Error during communication - ${e.message}")
                    e.printStackTrace()
                }
            }
        }
    }
}