package com.example.licznik2

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private val questions = listOf(
        "Jaką własność ciała określa stosunek masy do objętości?",
        "Co mierzy siła grawitacji?",
        "Jaka jest jednostka mocy w układzie SI?",
        "Co mierzy się w amperach?",
        "Który gaz stanowi większość składu atmosfery ziemskiej?",
        "Jaka planeta jest najbliższa Słońcu?",
        "Jaką jednostką mierzy się ciśnienie?",
        "Kto wynalazł żarówkę?",
        "Który pierwiastek chemiczny ma symbol 'O'?",
        "Jaka planeta jest znana jako 'Czerwona Planeta'?"
    )

    private val answers = listOf(
        listOf("Prędkość", "Energia kinetyczna", "Gęstość", "Temperatura"),
        listOf("Ciężar", "Masa", "Energia", "Objętość"),
        listOf("Wat", "Joul", "Newton", "Pascal"),
        listOf("Natężenie prądu", "Opór", "Napięcie", "Moc"),
        listOf("Tlen", "Dwutlenek węgla", "Azot", "Hel"),
        listOf("Ziemia", "Mars", "Merkury", "Wenus"),
        listOf("Joul", "Newton", "Pascal", "Wat"),
        listOf("Alexander Graham Bell", "Nikola Tesla", "Thomas Edison", "Michael Faraday"),
        listOf("Wodór", "Tlen", "Złoto", "Węgiel"),
        listOf("Wenus", "Jowisz", "Mars", "Saturn")
    )

    private val correctAnswers = listOf(
        2, // Gęstość
        0, // Ciężar
        0, // Wat
        0, // Natężenie prądu
        2, // Azot
        2, // Merkury
        2, // Pascal
        2, // Thomas Edison
        1, // Tlen
        2  // Mars
    )

    private var currentQuestionIndex = 0
    private var score = 0

    // Elementy interfejsu
    private lateinit var questionLayout: LinearLayout
    private lateinit var resultLayout: LinearLayout
    private lateinit var questionTextView: TextView
    private lateinit var questionNumberTextView: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var radioGroup: RadioGroup
    private lateinit var nextButton: Button
    private lateinit var congratsTextView: TextView
    private lateinit var scoreTextView: TextView
    private lateinit var restartButton: Button

    // Offset dla identyfikatorów RadioButtonów
    private val RADIO_BUTTON_ID_OFFSET = 1000

    // Zachowanie stanu podczas obrotu ekranu
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("currentQuestionIndex", currentQuestionIndex)
        outState.putInt("score", score)
        outState.putInt("answer", radioGroup.checkedRadioButtonId)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        currentQuestionIndex = savedInstanceState.getInt("currentQuestionIndex")
        score = savedInstanceState.getInt("score")
        val ans = savedInstanceState.getInt("answer")
        radioGroup.check((ans))
        // Jeśli quiz został ukończony przed obrotem, pokaż wynik
        if (currentQuestionIndex >= questions.size) {
            showResult()
        } else {
            loadQuestion()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Ukrycie paska akcji (opcjonalnie)
        // supportActionBar?.hide()
        setContentView(R.layout.activity_main)

        // Inicjalizacja elementów interfejsu
        questionLayout = findViewById(R.id.question_layout)
        resultLayout = findViewById(R.id.result_layout)
        questionTextView = findViewById(R.id.question_text)
        questionNumberTextView = findViewById(R.id.question_number)
        progressBar = findViewById(R.id.progressBar)
        radioGroup = findViewById(R.id.radioGroup)
        nextButton = findViewById(R.id.next_button)
        congratsTextView = findViewById(R.id.congrats_text)
        scoreTextView = findViewById(R.id.score_text)
        restartButton = findViewById(R.id.restart_button)

        // Sprawdzenie, czy stan został już przywrócony
        if (savedInstanceState == null) {
            loadQuestion()
        }

        nextButton.setOnClickListener {
            // Sprawdzenie, czy użytkownik zaznaczył odpowiedź
            val selectedOptionId = radioGroup.checkedRadioButtonId
            if (selectedOptionId != -1) {
                // Obliczenie indeksu wybranej odpowiedzi
                val selectedIndex = selectedOptionId - RADIO_BUTTON_ID_OFFSET

                // Sprawdzenie poprawności odpowiedzi
                if (selectedIndex == correctAnswers[currentQuestionIndex]) {
                    score++
                }

                if (currentQuestionIndex < questions.size - 1) {
                    currentQuestionIndex++
                    loadQuestion()
                } else {
                    currentQuestionIndex++
                    showResult()
                }
            } else {
                Toast.makeText(this, "Proszę wybrać odpowiedź", Toast.LENGTH_SHORT).show()
            }
        }

        restartButton.setOnClickListener {
            // Resetowanie quizu
            score = 0
            currentQuestionIndex = 0
            loadQuestion()
            resultLayout.visibility = View.GONE
            questionLayout.visibility = View.VISIBLE
        }
    }

    private fun loadQuestion() {
        radioGroup.clearCheck()
        questionTextView.text = questions[currentQuestionIndex]
        questionNumberTextView.text = "Pytanie ${currentQuestionIndex + 1}/${questions.size}"
        val progress = ((currentQuestionIndex).toFloat() / questions.size) * 100
        progressBar.progress = progress.toInt()
        radioGroup.removeAllViews()
        for ((index, answer) in answers[currentQuestionIndex].withIndex()) {
            val radioButton = RadioButton(this)
            radioButton.id = RADIO_BUTTON_ID_OFFSET + index
            radioButton.text = answer
            radioGroup.addView(radioButton)
        }
        // Upewnij się, że odpowiednie layouty są widoczne
        questionLayout.visibility = View.VISIBLE
        resultLayout.visibility = View.GONE
    }

    private fun showResult() {
        // Ukryj layout z pytaniami i pokaż wynik
        questionLayout.visibility = View.GONE
        resultLayout.visibility = View.VISIBLE

        congratsTextView.text = "Gratulacje!"
        scoreTextView.text = "Twój wynik: $score z ${questions.size}"
    }
}