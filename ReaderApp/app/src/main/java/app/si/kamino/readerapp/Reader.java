package app.si.kamino.readerapp;

/**
 * Created by DenisK on 17. 07. 2017.
 * <p>
 * This view represents speed reading UI.
 * It draw words from text in specified speed (words per minute).
 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class Reader extends View {

    private int width;
    private int height;
    private int index;
    private float textSize;
    private float lineLenght;
    private float lineStroke;
    private Paint paint;
    protected ArrayList<Word> words;
    private Subscription subscription;
    private AtomicBoolean paused;
    private boolean isRunning;

    public Reader(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(true); //Disabled onDraw method call at start
        paint = new Paint();
        words = new ArrayList<>();
        paused = new AtomicBoolean(false);
        lineStroke = getResources().getDimension(R.dimen.lineStroke);
        isRunning = false;
    }

    public void start(ArrayList<Word> words, int numOfWords, float textSize, float lineLenght, int speedWordPerMin) {
        setWillNotDraw(false); //Enabled onDraw after invalidate() is called
        index = 0;
        isRunning = true;
        paint = new Paint();
        paused.set(false);
        this.textSize = textSize;
        this.lineLenght = lineLenght;
        this.words = words;

        //If 150 words per minute is 400ms per word = 60sec*1000ms/150words
        int wordTime = 60000 / speedWordPerMin;

        subscription = Observable.interval(wordTime, TimeUnit.MILLISECONDS)
                .filter(tick -> !paused.get())
                .subscribeOn(Schedulers.io())
                .take(numOfWords)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> {
                    index++;
                    invalidate();
                });
    }

    public void stop() {
        isRunning = false;
        if (subscription != null) {
            subscription.unsubscribe();
        }
    }

    public void pause() {
        paused.set(true);
    }

    public void resume() {
        paused.set(false);
    }

    public boolean isPaused() {
        return paused.get();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.width = w;
        this.height = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(lineStroke);

        //horizontal lines
        canvas.drawLine(0, height / 2 - textSize, width, height / 2 - textSize, paint);
        canvas.drawLine(0, height / 2 + textSize, width, height / 2 + textSize, paint);

        //vertical mini lines
        canvas.drawLine(width / 2, height / 2 - textSize, width / 2, height / 2 - textSize + lineLenght, paint);
        canvas.drawLine(width / 2, height / 2 + textSize - lineLenght, width / 2, height / 2 + textSize, paint);

        //Each word can draw itself
        words.get(index).Draw(canvas, width, height);
    }

    public boolean isRunning() {
        return isRunning;
    }
}