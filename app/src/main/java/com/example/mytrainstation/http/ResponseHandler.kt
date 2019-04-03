package com.example.mytrainstation.http

import okhttp3.ResponseBody

interface ResponseHandler {

    fun handleResponse(response: ResponseBody?)

    fun handleError(error: String)
}