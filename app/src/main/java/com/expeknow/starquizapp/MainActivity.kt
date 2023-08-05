package com.expeknow.starquizapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.expeknow.starquizapp.R
import com.expeknow.starquizapp.Constants.categoryNames

class MainActivity : AppCompatActivity() {

    var autoCompleteTextView: AutoCompleteTextView? = null
    var arrayAdapter: ArrayAdapter<String>? = null
    var seekBar: SeekBar? = null
    var progressText: TextView? = null
    var difficultyRadioGroup : RadioGroup? = null
    var questionTypeRadioGroup : RadioGroup? = null
    var startQuizBtn : Button? = null
    var selectedCategoryIndex : Int = 8
    var questionCount : Int =5;


    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.hide()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        autoCompleteTextView = findViewById(R.id.auto_complete_textview)
        autoCompleteTextView?.setText(categoryNames[0])
        arrayAdapter = ArrayAdapter(this, R.layout.list_item, categoryNames)
        autoCompleteTextView?.setAdapter(arrayAdapter)
        autoCompleteTextView?.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, position, _ ->
                selectedCategoryIndex = position+8
            }
        difficultyRadioGroup = findViewById(R.id.difficulty_radio_group)
        questionTypeRadioGroup = findViewById(R.id.question_type_radio_group)
        seekBar = findViewById(R.id.seekBar)
        progressText = findViewById(R.id.progress_text)
        startQuizBtn = findViewById(R.id.start_quiz_btn)


        seekBar?.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(
                seek: SeekBar,
                progress: Int, fromUser: Boolean
            ) {
                "$progress/50".also { progressText?.text = it }
                seekBar?.progress = progress
                questionCount = progress
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                val nine = 9
            }
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                val eight = 8
            }
        })

        startQuizBtn?.setOnClickListener {
            startQuiz()
        }
    }

    private fun startQuiz() {
        val selectedDifficultyId : RadioButton= findViewById(difficultyRadioGroup?.checkedRadioButtonId?:1000023)
        val selectedTypeId : RadioButton= findViewById(questionTypeRadioGroup?.checkedRadioButtonId?:1000014)
        val MultipleQuestionIntent = Intent(this, QuizMultipleQuestionActivity::class.java)
        val BooleanQuestionIntent = Intent(this, QuizBooleanQuestionActivity::class.java)

        if( resources.getResourceEntryName(selectedTypeId.id) == "multiple"){
            MultipleQuestionIntent.putExtra(Constants.CATEGORY, selectedCategoryIndex)
            MultipleQuestionIntent.putExtra(Constants.QUESTION_COUNT, questionCount)
            MultipleQuestionIntent.putExtra(Constants.QUESTION_DIFFICULTY, resources.getResourceEntryName(selectedDifficultyId.id))
            startActivity(MultipleQuestionIntent)
        }else{
            BooleanQuestionIntent.putExtra(Constants.CATEGORY, selectedCategoryIndex)
            BooleanQuestionIntent.putExtra(Constants.QUESTION_COUNT, questionCount)
            BooleanQuestionIntent.putExtra(Constants.QUESTION_DIFFICULTY, resources.getResourceEntryName(selectedDifficultyId.id))
            startActivity(BooleanQuestionIntent)
        }
        finish()
    }
}

