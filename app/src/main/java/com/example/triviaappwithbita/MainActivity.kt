package com.example.triviaappwithbita

import android.annotation.SuppressLint
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.cardview.widget.CardView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.triviaappwithbita.Model.Question
import com.example.triviaappwithbita.Model.Score
import com.example.triviaappwithbita.Util.Prefs
import org.json.JSONArray
import java.util.*

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private var trueButton: Button? = null
    private var falseButton: Button? = null
    private var prevButton: ImageButton? = null
    private var nextButton: ImageButton? = null
    private var questionText: TextView? = null
    private var questionCounterText: TextView? = null


    private var questionList = arrayListOf<Question>()

    private var currentQuestionIndex = 0

    private var requestQueue: RequestQueue? = null

    private var startButton: Button?= null
    private var linearLayout: LinearLayout? = null
    private var cardView: CardView? = null
    private var highScoreText: TextView? = null
    private var scoreText: TextView? = null

    private var scoreCounter: Int = 0
    private var score: Score? = null

    private var prefs: Prefs? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)
        prevButton = findViewById(R.id.button_prev)
        nextButton = findViewById(R.id.button_next)
        questionText = findViewById(R.id.question_text)
        questionCounterText = findViewById(R.id.counter)


        trueButton?.setOnClickListener(this)
        falseButton?.setOnClickListener(this)
        prevButton?.setOnClickListener(this)
        nextButton?.setOnClickListener(this)

        startButton = findViewById(R.id.startButton)
        linearLayout = findViewById(R.id.buttons_linear_layout)
        cardView = findViewById(R.id.cardView)
        highScoreText = findViewById(R.id.high_score)
        scoreText = findViewById(R.id.score)


        startButton?.setOnClickListener(this)



        requestQueue = Volley.newRequestQueue(this)


        getQuestions()

        score = Score()
        prefs = Prefs(this)










    }




    private fun getQuestions() {

        val url: String = "https://raw.githubusercontent.com/curiousily/simple-quiz/master/script/statements-data.json"

        val stringReq = StringRequest(Request.Method.GET, url,
                { response ->

                    val strResp = response.toString()
                    val jsonArray: JSONArray = JSONArray(strResp)


                    for (i in 0 until  jsonArray.length()) {
                        val question = Question()
                        question.answer = jsonArray.getJSONArray(i).get(0).toString()
                        question.trueOrFalse = jsonArray.getJSONArray(i).getBoolean(1)

                        questionList.add(question)


                     }

                },
                { error -> error.printStackTrace()})


        requestQueue?.add(stringReq)






    }

    @SuppressLint("SetTextI18n")
    override fun onClick(v: View?) {

        when(v?.id){

            R.id.button_next -> {
                currentQuestionIndex = (currentQuestionIndex + 1) % questionList.size
                updateQuestion()

            }

            R.id.button_prev -> {

                if (currentQuestionIndex > 0){
                    currentQuestionIndex = (currentQuestionIndex - 1) % questionList.size
                    updateQuestion()

                }
            }

            R.id.true_button -> {
                checkAnswer(true)
            }

            R.id.false_button -> {
                checkAnswer(false)
            }

            R.id.startButton -> {

                currentQuestionIndex = prefs?.getState() ?: 0

                updateQuestion()
                highScoreText?.text = "High Score = " + prefs?.getHighScore().toString()


                startButton?.visibility = View.INVISIBLE
                cardView?.visibility = View.VISIBLE
                linearLayout?.visibility = View.VISIBLE
                highScoreText?.visibility = View.VISIBLE
                scoreText?.visibility = View.VISIBLE


            }


        }
    }

    @SuppressLint("SetTextI18n")
    fun addPoints(){

        scoreCounter++
        score?.score =scoreCounter
        scoreText?.text = "Score = $scoreCounter"

    }

    @SuppressLint("SetTextI18n")
    fun deductPoint(){
        if (scoreCounter > 0){
            scoreCounter--
            score?.score = scoreCounter
            scoreText?.text = "Score = $scoreCounter"
        }
    }


    private fun checkAnswer(userAnswer: Boolean) {

        if (questionList[currentQuestionIndex].trueOrFalse == userAnswer){
            fadeView()
            addPoints()
            Toast.makeText(this@MainActivity, "Correct", Toast.LENGTH_LONG).show()
        }else{

            shakeAnimation()
            deductPoint()
            Toast.makeText(this@MainActivity, "Wrong", Toast.LENGTH_LONG).show()

        }


    }

    private fun shakeAnimation() {
        var shake: Animation = AnimationUtils.loadAnimation(this@MainActivity, R.anim.shake_animation)
        val cardView: CardView = findViewById(R.id.cardView)
        cardView.animation = shake

        shake.setAnimationListener(object:Animation.AnimationListener {
            override fun onAnimationStart(animation:Animation) {
                cardView.setCardBackgroundColor(Color.RED)
            }
            override fun onAnimationEnd(animation:Animation) {
                cardView.setCardBackgroundColor(Color.WHITE)
                goNext()

            }
            override fun onAnimationRepeat(animation:Animation) {
            }
        })


    }

    private fun fadeView() {

        val cardView: CardView = findViewById(R.id.cardView)
        var alphaAnimation: AlphaAnimation = AlphaAnimation(1.0f, 0.0f)

        alphaAnimation.duration = 350
        alphaAnimation.repeatCount = 1
        alphaAnimation.repeatMode = Animation.REVERSE

        cardView.animation = alphaAnimation

        alphaAnimation.setAnimationListener(object: Animation.AnimationListener{
            override fun onAnimationStart(animation: Animation?) {
                cardView.setCardBackgroundColor(Color.GREEN)
            }

            override fun onAnimationEnd(animation: Animation?) {
                cardView.setCardBackgroundColor(Color.WHITE)
                goNext()
            }

            override fun onAnimationRepeat(animation: Animation?) {

            }
        })
    }

    private fun goNext(){
        currentQuestionIndex = (currentQuestionIndex + 1) % questionList.size
        updateQuestion()


    }

    @SuppressLint("SetTextI18n")
    private fun updateQuestion(){

        val question: String? = questionList[currentQuestionIndex].answer
        questionText?.text = question
        questionCounterText?.text = currentQuestionIndex.toString() + " / " + questionList.size.toString()
    }

    override fun onPause() {

        prefs?.savingHighScore(score?.score)

        prefs?.setState(currentQuestionIndex)

        super.onPause()


    }
}