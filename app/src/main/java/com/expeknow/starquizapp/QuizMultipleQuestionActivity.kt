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

class QuizMultipleQuestionActivity : AppCompatActivity(), View.OnClickListener{

    var submitButton: Button? = null
    var multipleQuestionList : ArrayList<MultipleQuestion>? = null
    var questionText : TextView? = null
    var option1 : TextView? = null
    var option2 : TextView? = null
    var option3 : TextView? = null
    var option4 : TextView? = null
    var currentQuestionNumber = 1
    var totalQuestionNumber = 0
    var progressCount: TextView? = null
    var progressBar: ProgressBar? = null
    var correctAnswer: String = ""
    var selectedAnswerId: Int = 0
    var correctAnswerCount : Int = 0



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_multiple_quiz_question)

        questionText = findViewById(R.id.question_text)
        option1 = findViewById(R.id.option1)
        option2 = findViewById(R.id.option2)
        option3 = findViewById(R.id.option3)
        option4 = findViewById(R.id.option4)
        progressCount = findViewById(R.id.progress_count)
        progressBar = findViewById(R.id.progress_bar)
        submitButton = findViewById(R.id.submit_button)

        option1?.setOnClickListener(this)
        option2?.setOnClickListener(this)
        option3?.setOnClickListener(this)
        option4?.setOnClickListener(this)
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
                    "&type=multiple"

        }else{
            "https://opentdb.com/api.php?amount=$questionCount&category=$category" +
                    "&difficulty=$questionDifficulty&type=multiple"
        }


        val request = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                multipleQuestionList = getMultiplQuestionsList(response)
                setQuestion(multipleQuestionList)
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

    private fun setQuestion(multipleQuestionList: ArrayList<MultipleQuestion>?){

        if(multipleQuestionList.isNullOrEmpty()){
            return
        }
        val currentQuestion = multipleQuestionList[currentQuestionNumber-1]
        questionText?.text = currentQuestion.question
        option1?.text = currentQuestion.option1
        option2?.text = currentQuestion.option2
        option3?.text = currentQuestion.option3
        option4?.text = currentQuestion.option4
        progressBar?.setProgress(currentQuestionNumber)
        val progressText = "$currentQuestionNumber/$totalQuestionNumber"
        progressCount?.text = progressText
        currentQuestionNumber++
        correctAnswer = currentQuestion.correctAns

    }

    fun correctOption(view: TextView?){
        view?.typeface = Typeface.DEFAULT_BOLD
        view?.background =ContextCompat.getDrawable(this, R.drawable.correct_option_border_bg)
    }

    fun incorrectOption(view: TextView?){
        view?.typeface = Typeface.DEFAULT_BOLD
        view?.background =ContextCompat.getDrawable(this, R.drawable.incorrect_option_border_bg)
    }

    fun selectOption(view: TextView?){
        if(submitButton?.text == "NEXT QUESTION") return
        defaultOptionSetter()
        selectedAnswerId = view?.id!!
        view.typeface = Typeface.DEFAULT_BOLD
        view.background =ContextCompat.getDrawable(this, R.drawable.selected_option_border_bg)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.option1 -> selectOption(option1)
            R.id.option2 -> selectOption(option2)
            R.id.option3 -> selectOption(option3)
            R.id.option4 -> selectOption(option4)
            R.id.submit_button -> {

                if(multipleQuestionList.isNullOrEmpty()){
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
                    selectedAnswerId = 0
                    setQuestion(multipleQuestionList)
                }
                else{
                    val selectedTextView : TextView? = if(selectedAnswerId != 0) findViewById(selectedAnswerId) else null
                    if(correctAnswer == selectedTextView?.text.toString()){
                        correctOption(selectedTextView)
                        correctAnswerCount++
                    }else{
                        incorrectOption(selectedTextView)
                        when(correctAnswer){
                            option1?.text -> correctOption(option1)
                            option2?.text -> correctOption(option2)
                            option3?.text -> correctOption(option3)
                            option4?.text -> correctOption(option4)
                        }
                    }
                    if(currentQuestionNumber == totalQuestionNumber+1)
                        submitButton?.text = "GET RESULTS"
                    else
                        submitButton?.text = "NEXT QUESTION"

                }

            }

        }
    }

    private fun defaultOptionSetter(){
        val optionsViews = ArrayList<TextView>()
        option1?.let { optionsViews.add(it) }
        option2?.let { optionsViews.add(it) }
        option3?.let { optionsViews.add(it) }
        option4?.let { optionsViews.add(it) }

        for (option in optionsViews){
            option.typeface = Typeface.DEFAULT
            option.setTextColor(getColorStateList(R.color.default_options_text_color))
            option.background =ContextCompat.getDrawable(this, R.drawable.default_option_border_bg)
        }

    }

}


