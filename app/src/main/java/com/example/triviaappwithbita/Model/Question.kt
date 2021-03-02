package com.example.triviaappwithbita.Model

class Question() {

    private var _answer: String? = null
    private var _trueOrFalse: Boolean? = null

    constructor(answer: String?, trueOrFalse: Boolean?):this(){

        _answer = answer
        _trueOrFalse = trueOrFalse
    }

    var answer: String?
        get() = _answer
        set(value){
            _answer = value
        }


    var trueOrFalse: Boolean?
        get() = _trueOrFalse
        set(value){
            _trueOrFalse = value
        }





}