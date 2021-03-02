package com.example.triviaappwithbita.Util

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import com.example.triviaappwithbita.MainActivity

class Prefs(activity: Activity) {

    private var preferences: SharedPreferences? = null

    init {
        this.preferences = activity.getPreferences(Context.MODE_PRIVATE)
    }

    fun savingHighScore(score: Int?){

        val lastScore: Int = preferences?.getInt("high_score", 0) ?: 0

        if (score!! > lastScore){
            preferences?.edit()?.putInt("high_score", score)?.apply()
        }
    }

    fun getHighScore(): Int? {
        return preferences?.getInt("high_score", 0)
    }


    fun setState(index: Int){
        preferences?.edit()?.putInt("index_state", index)?.apply()
    }

    //for access to the last question that user wanted to answer before exiting the app
    fun getState(): Int? {

        return preferences?.getInt("index_state",0)
    }

}