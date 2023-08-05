package com.expeknow.starquizapp

import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.expeknow.starquizapp.R

class QuizBooleanQuestionActivity : AppCompatActivity(), View.OnClickListener {

    var submitButton: Button? = null
    var booleanQuestionList : ArrayList<BooleanQuestion> = ArrayList()
    var questionText : TextView? = null
    var trueOption : TextView? = null
    var falseOption : TextView? = null
    var currentQuestionNumber = 1
    var totalQuestionNumber = 0
    var progressCount: TextView? = null
    var progressBar: ProgressBar? = null
    var correctAnswer: String = ""
    var selectedAnswer: String = "null"
    var correctAnswerCount : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_boolean_question)
        supportActionBar?.hide()

        submitButton = findViewById(R.id.submit_button)
        questionText = findViewById(R.id.question_text)
        trueOption = findViewById(R.id.trueOption)
        falseOption = findViewById(R.id.falseOption)
        progressBar = findViewById(R.id.progress_bar)
        progressCount = findViewById(R.id.progress_count)

        trueOption?.setOnClickListener(this)
        falseOption?.setOnClickListener(this)
        submitButton?.setOnClickListener(this)

        totalQuestionNumber = intent.getIntExtra(Constants.QUESTION_COUNT,1)
        progressBar?.max = totalQuestionNumber

        getQuestions(intent.getIntExtra(Constants.CATEGORY, 8), intent.getIntExtra(Constants.QUESTION_COUNT, 10),
            intent.getStringExtra(Constants.QUESTION_DIFFICULTY))
    }

    private fun getQuestions(category: Int, questionCount: Int, questionDifficulty: String?)
    {
        val url: String
        url = if(category == 8){
            "https://opentdb.com/api.php?amount=${questionCount}&difficulty=${questionDifficulty.toString()}" +
                    "&type=boolean"

        }else{
            "https://opentdb.com/api.php?amount=$questionCount&category=$category" +
                    "&difficulty=$questionDifficulty&type=boolean"
        }


        val request = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                booleanQuestionList = getBooleanQuestionList(response)
                setQuestion(booleanQuestionList)
            },
            { error ->
                error.printStackTrace()
                Toast.makeText(this, "No internet connection. Please check your internet connect and try again.", Toast.LENGTH_LONG).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            })


        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(request)

    }

    private fun setQuestion(booleanQuestionList: ArrayList<BooleanQuestion>?){

        if(booleanQuestionList.isNullOrEmpty()){
            return
        }
        val currentQuestion = booleanQuestionList[currentQuestionNumber-1]
        questionText?.text = currentQuestion.question
        trueOption?.text = "True"
        falseOption?.text = "False"

        progressBar?.setProgress(currentQuestionNumber)
        val progressText = "$currentQuestionNumber/$totalQuestionNumber"
        progressCount?.text = progressText
        currentQuestionNumber++
        correctAnswer = currentQuestion.correctAns

    }

    private fun correctOption(view: TextView?){
        view?.typeface = Typeface.DEFAULT_BOLD
        view?.background = ContextCompat.getDrawable(this, R.drawable.correct_option_border_bg)
    }

    private fun selectOption(view: TextView?){
        if(submitButton?.text == "NEXT QUESTION") return
        defaultOptionSetter()
        view?.typeface = Typeface.DEFAULT_BOLD
        view?.background = ContextCompat.getDrawable(this, R.drawable.selected_option_border_bg)
    }

    private fun defaultOptionSetter(){
        val optionsViews = ArrayList<TextView>()
        trueOption?.let { optionsViews.add(it) }
        falseOption?.let { optionsViews.add(it) }

        for (option in optionsViews){
            option.typeface = Typeface.DEFAULT
            option.setTextColor(getColorStateList(R.color.default_options_text_color))
            option.background =ContextCompat.getDrawable(this, R.drawable.default_option_border_bg)
        }
    }

    override fun onClick(v: View?) {

        when(v?.id){
            R.id.trueOption -> {
                selectOption(trueOption)
                selectedAnswer = "True"
            }
            R.id.falseOption -> {
                selectOption(falseOption)
                selectedAnswer = "False"
            }

            R.id.submit_button -> {

                if(booleanQuestionList.isEmpty()){
                    questionText?.text = "Please wait... If the questions don't show, try again different category."
                    return
                }
                else if(submitButton?.text == "GET RESULTS"){
                    val intent = Intent(this, ResultActivity::class.java)
                    intent.putExtra(Constants.TOTAL_QUESTIONS_COUNT, totalQuestionNumber)
                    intent.putExtra(Constants.CORRECT_ANSWER_COUNT, correctAnswerCount)
                    startActivity(intent)
                    finish()
                }
                else if(submitButton?.text == "NEXT QUESTION"){
                    submitButton?.text = "SUBMIT"
                    defaultOptionSetter()
                    selectedAnswer = "null"
                    setQuestion(booleanQuestionList)
                }
                else{
                    if(selectedAnswer == "True") selectOption(trueOption)
                    else if(selectedAnswer == "False") selectOption(falseOption)

                    if(correctAnswer == "True"){
                        correctOption(trueOption)
                    }else{
                        correctOption(falseOption)
                    }

                    if(correctAnswer == selectedAnswer){
                        correctAnswerCount++
                    }
                    if(currentQuestionNumber == totalQuestionNumber +1)
                        submitButton?.text = "GET RESULTS"
                    else
                        submitButton?.text = "NEXT QUESTION"

                }

            }

        }
    }
}