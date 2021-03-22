package android.example.italiainhistoryquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import java.text.NumberFormat;

/**
 * This app calculate the score for a quiz about italian history.
 */

public class MainActivity extends AppCompatActivity {
    // In this global variable we put the score of the quiz
    int score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * add 1 to the score if it is clicked the right answer on the first radio button group
     */
    public void onFirstRadioButtonClicked() {
        // is clicked the right answer?
        RadioButton radioCorrectAnswer = findViewById(R.id.question_1_answer_3_rb);
        if (radioCorrectAnswer.isChecked()) {
            score += 1;
        }
    }

    /**
     * add 1 to the score for every right checkbox clicked
     *
     * @param isRomolus tell if the checkbox with Romolus is clicked
     * @param isAncus   tell the checkbox with Ancus is clicked
     * @param isPompeo  tell the checkbox with Pompeo is clicked
     */
    public void calcualteScoreOnCheckBox(boolean isRomolus, boolean isAncus, boolean isPompeo) {

        // add 1 if Romolus is checked
        if (isRomolus && isAncus && !isPompeo) {
            score += 1;
        }

    }

    /**
     * add 1 to the score if it is clicked the right answer on the second radio button group
     */
    public void onSecondRadioButtonClicked() {
        // is clicked the right answer?
        RadioButton radioCorrectAnswer = findViewById(R.id.question_2_answer_2_rb);
        if (radioCorrectAnswer.isChecked()) {
            score += 1;
        }
    }

    /**
     * @param name the name of the user
     *             Create the score summary
     */
    private String scoreSummary(String name) {

        return getString(R.string.greetings) + " " + name + getString(R.string.score_text) + " " + score + " " + getString(R.string.thanks_text);
    }

    /**
     * @param historicalFigure text present in the second editext about hisorical figure
     *                         add 1 to the score if some text is present.
     */
    private void calcScoreOpenQuestion(String historicalFigure) {

        //verify if there is something written in the text
        if (historicalFigure.trim().length() > 0) {
            // add 4 to the score  if the answer is Leonardo
            if (historicalFigure.equalsIgnoreCase("LEONARDO")) {
                score += 4;
                // add 2 to the score  if the answer is Dante
            } else if (historicalFigure.equalsIgnoreCase("DANTE")) {
                score += 2;
                // add 3 to the score  if the answer is Galileo
            } else if (historicalFigure.equalsIgnoreCase("GALILEO")) {
                score += 3;
            }
        }
    }

    public void submitQuizButton(View view) {
        // reset score
        score = 0;
        //take the user name
        EditText nameField = findViewById(R.id.name_field);
        String name = nameField.getText().toString();

        //take the text of the open question
        EditText answerHistoricalField = findViewById(R.id.historical_figure_field);
        String historicalFigure = answerHistoricalField.getText().toString();

        //calculate score for the open question
        calcScoreOpenQuestion(historicalFigure);

        //calculate score for the first radio group
        onFirstRadioButtonClicked();

        //calculate score for the second radio group
        onSecondRadioButtonClicked();

        //Figure out if the user checked the first checkbox (romulus)
        CheckBox isRomulus = findViewById(R.id.question_4_answer_1_cb);
        boolean romulus = isRomulus.isChecked();

        //Figure out if the user checked the second checkbox (ancus)
        CheckBox isAncus = findViewById(R.id.question_4_answer_2_cb);
        boolean ancus = isAncus.isChecked();

        //Figure out if the user checked the third checkbox (pompeo)
        CheckBox isPompeo = findViewById(R.id.question_4_answer_3_cb);
        boolean pompeo = isPompeo.isChecked();

        //calculate score for right answer on checkboxes
        calcualteScoreOnCheckBox(romulus, ancus, pompeo);

        // if score = 0 then send a toast message
        if (score == 0) {
            Toast.makeText(this, R.string.try_again, Toast.LENGTH_LONG).show();
        }

        // create toast message
        String scoreSummaryText = scoreSummary(name);
        Toast.makeText(this, scoreSummaryText, Toast.LENGTH_LONG).show();

        //send the score summary with email throught intent
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); //only emails apps should handle this
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.score_summary_email_subject) + " " + name);
        intent.putExtra(Intent.EXTRA_TEXT, scoreSummaryText);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}