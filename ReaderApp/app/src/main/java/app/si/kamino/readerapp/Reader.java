package app.si.kamino.readerapp;

/**
 * Created by DenisK on 17. 07. 2017.
 *
 * This view represents speed reading UI.
 * It draw words from text in specified speed (words per minute).
 */

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class Reader extends View {

    private String[] textSplit;
    private int width, height, textSize, speedWordPerMin, index = 0;
    private Paint paint;
    protected ArrayList<Word> words;
    private Subscription subscription;


    public Reader(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        words = new ArrayList<>();

        //Get attribute values from layout
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.Reader);
        try {
            textSize = a.getInt(R.styleable.Reader_textSize, 100);
            String text = a.getString(R.styleable.Reader_text);
            textSplit = text != null ? text.split("\\s") : new String[0];
            speedWordPerMin = a.getInt(R.styleable.Reader_speedWordsPerMin, 150);
        } finally {
            a.recycle();
        }

        for (String str : textSplit) {
            words.add(new Word(str, paint, textSize));
        }

        //if 150 words per minute is 400ms per word = 60sec*1000ms/150words
        int wordTime = 60000 / speedWordPerMin;
        subscription = Observable.interval(wordTime, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Long>() {
                    public void call(Long aLong) {
                        if (index < words.size() - 1) {
                            index++;
                            invalidate();
                        } else {
                            subscription.unsubscribe();
                        }
                    }
                });
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        this.width = MeasureSpec.getSize(widthMeasureSpec);
        this.height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(width, height);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(8f);

        //horizontal lines
        canvas.drawLine(0, height / 2 - textSize, width, height / 2 - textSize, paint);
        canvas.drawLine(0, height / 2 + textSize, width, height / 2 + textSize, paint);

        //vertical mini lines
        int lineLenght = 30;
        canvas.drawLine(width / 2, height / 2 - textSize, width / 2, height / 2 - textSize + lineLenght, paint);
        canvas.drawLine(width / 2, height / 2 + textSize - lineLenght, width / 2, height / 2 + textSize, paint);

        //Each word can draw itself
        words.get(index).Draw(canvas, width, height);
    }
}