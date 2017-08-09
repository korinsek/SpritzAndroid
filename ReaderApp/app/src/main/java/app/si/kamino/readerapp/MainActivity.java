package app.si.kamino.readerapp;

import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

public class MainActivity extends AppCompatActivity {
    private Reader reader;
    private ArrayList<Word> words;
    private Subscription subscriptionWords;
    private Button pauseBtn, startBtn;
    private EditText textEt;
    private TextView speedTv;
    private SeekBar seekBar;
    private int wordsCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        reader = (Reader) findViewById(R.id.readerView);
        speedTv = (TextView) findViewById(R.id.speedTv);
        startBtn = (Button) findViewById(R.id.submitBtn);
        textEt = (EditText) findViewById(R.id.textEt);
        pauseBtn = (Button) findViewById(R.id.pauseBtn);
        seekBar = (SeekBar) findViewById(R.id.speedSeekBar);
        speedTv.setText(seekBar.getProgress() + "");

        //Calculate words in background and fill it into word list
        calculateWords();

        textEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence sequence, int start, int before, int count) {
                calculateWords(); //Recalculate text in background when user change text
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        //Seekbar for speed show value in text view
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                speedTv.setText(progress + "");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        //On click check if reading is not in progress then start it and change labels, if reader is running stop it
        startBtn.setOnClickListener(v -> {
                    if (!reader.isRunning()) {
                        findViewById(R.id.speedWrapper).setVisibility(View.INVISIBLE);
                        pauseBtn.setVisibility(View.VISIBLE);
                        pauseBtn.setText(getString(R.string.pause));
                        textEt.setVisibility(View.INVISIBLE);
                        reader.setVisibility(View.VISIBLE);
                        startBtn.setText(R.string.stop);

                        //Run custom view with custom parameters
                        reader.start(words,
                                wordsCount - 1,
                                getResources().getDimension(R.dimen.textSize),
                                getResources().getDimension(R.dimen.lineLength),
                                seekBar.getProgress());
                    } else {
                        findViewById(R.id.speedWrapper).setVisibility(View.VISIBLE);
                        pauseBtn.setVisibility(View.GONE);
                        textEt.setVisibility(View.VISIBLE);
                        reader.setVisibility(View.INVISIBLE);
                        startBtn.setText(R.string.start);
                        reader.stop(); //Stop RxJava flow with intervals
                    }
                }
        );

        //Pause or resume reading
        pauseBtn.setOnClickListener(v -> {
            if (reader.isPaused()) {
                reader.resume();
                pauseBtn.setText(getString(R.string.pause));
            } else {
                reader.pause();
                pauseBtn.setText(getString(R.string.resume));
            }
        });
    }

    private void calculateWords() {
        Paint paint = new Paint();
        words = new ArrayList<>();
        //Split words by whitespaces
        String[] splitText = textEt.getText().toString().trim().replaceAll(" +", " ").split("\\s"); //Remove all multiple spaces in text because we can get random text in edittext
        wordsCount = splitText.length;
        float textSize = getResources().getDimension(R.dimen.textSize); //Get dimension only once because its same for all words

        //Insert words into list - word splitting and coloring is done here
        subscriptionWords = Observable.from(splitText).observeOn(AndroidSchedulers.mainThread()).subscribe(
                s -> words.add(new Word(s, paint, textSize))
        );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!subscriptionWords.isUnsubscribed())
            subscriptionWords.unsubscribe();
        reader.stop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        reader.pause();
        pauseBtn.setText(getString(R.string.resume));
    }
}
