package com.example.triviaappwithbita.Controller

import android.app.Application
import android.text.TextUtils
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import java.util.*

class AppController: Application() {

    private var mRequestQueue: RequestQueue? = null

    val requestQueue: RequestQueue?
        get() {
            if (mRequestQueue == null){
                mRequestQueue = Volley.newRequestQueue(applicationContext)
            }

            return mRequestQueue

        }

    override fun onCreate(){
        super.onCreate()
        instance = this

    }

    companion object {

        val TAG = AppController::class.java.simpleName
        @get: Synchronized
        var instance: AppController? = null
    }

    fun <T> addRequestToQueue(request: Request<T>, tag: String){
        request.tag = if (TextUtils.isEmpty(tag)) TAG else tag
        requestQueue?.add(request)
    }


    fun <T> addToRequestQueue(req: Request<T>){
        req.tag = TAG
        requestQueue?.add(req)
    }

    fun cancelPendingRequest(tag: Any){

        if (mRequestQueue != null){
            mRequestQueue?.cancelAll(tag)
        }
    }

}