package com.expeknow.starquizapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.expeknow.starquizapp.R

class ResultActivity : AppCompatActivity() {

    var startNewQuizBtn : Button? = null
    var scoreText : TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)
        supportActionBar?.hide()
        scoreText = findViewById(R.id.score_text)

        val ScoreMessage : String = "You scored ${intent.getIntExtra(Constants.CORRECT_ANSWER_COUNT, 0)}" +
                " out of ${intent.getIntExtra(Constants.TOTAL_QUESTIONS_COUNT, 0)}"

        scoreText?.text = ScoreMessage

        startNewQuizBtn = findViewById(R.id.finish_quiz_button)
        startNewQuizBtn?.setOnClickListener {
            val intent : Intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}