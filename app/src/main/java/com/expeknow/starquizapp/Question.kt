package com.expeknow.starquizapp

import android.text.Html
import org.json.JSONArray
import org.json.JSONObject

data class MultipleQuestion(
    val question: String,
    val option1 : String,
    val option2 : String,
    val option3 : String,
    val option4 : String,
    val correctAns : String,
)

data class BooleanQuestion(
    val question: String,
    val correctAns: String
)




fun getMultiplQuestionsList(response : JSONObject) : ArrayList<MultipleQuestion> {
    val results: JSONArray = response.get("results") as JSONArray
    val multipleQuestionList: ArrayList<MultipleQuestion> = ArrayList()
    for (i in 0 until results.length()) {
        val question = results.getJSONObject(i)
        val options = getShuffledOptions(
            question.getJSONArray("incorrect_answers"),
            question.get("correct_answer").toString()
        )
        val questionText = Html.fromHtml(question.get("question").toString()).toString()
        multipleQuestionList.add(
            MultipleQuestion(
                questionText,
                Html.fromHtml(options[0].toString()).toString(),
                Html.fromHtml(options[1].toString()).toString(),
                Html.fromHtml(options[2].toString()).toString(),
                Html.fromHtml(options[3].toString()).toString(),
                Html.fromHtml(question.get("correct_answer").toString()).toString()
            )
        )
    }

    return multipleQuestionList
}

fun getBooleanQuestionList(response: JSONObject) : ArrayList<BooleanQuestion> {
    val results: JSONArray = response.get("results") as JSONArray
    val questionList: ArrayList<BooleanQuestion> = ArrayList()
    for(i in 0 until results.length()) {
            val question = results.getJSONObject(i)
            val questionText= Html.fromHtml(question.get("question").toString()).toString()
            questionList.add(
                BooleanQuestion(questionText, question.get("correct_answer").toString())
            )
        }
    return questionList
}



fun getShuffledOptions(options: JSONArray, answer: String) : Array<String?> {
    var answers = arrayOfNulls<String>(4)
    answers[0]= answer

    for(i in 0 until options.length()){
        answers[i+1] = options.get(i).toString()
    }

    val listOfAnswers = answers.toMutableList()
    listOfAnswers.shuffle()
    answers = listOfAnswers.toTypedArray()

    return answers
}

