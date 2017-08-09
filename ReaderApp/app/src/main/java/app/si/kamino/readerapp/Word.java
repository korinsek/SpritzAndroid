package app.si.kamino.readerapp;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;

/**
 * Created by DenisK on 18. 07. 2017.
 * <p>
 * This class represents word parameters. We use Spritz algorithm to calculate pivot index.
 * Pivot letter is drawn in RED color. We draw pivot letter as new text, because its faster for calculations and draw as spiting words.
 * Object calculate coordinates and offsets itself when we call it. It also draw itself when onDraw need it.
 */

public class Word {
    private String word;
    private String pivotLetter;
    private int pivot;

    //Word draw parameters
    private float textWidth;
    private float letterWidth;
    private float xWordOffset, yWordOffset, xLetterOffset, yLetterOffset;
    private Paint paint;

    public Word(String word, Paint p, float textSize) {
        this.word = word;
        this.pivot = Spritz.GetPivot(word);
        this.pivotLetter = word.substring(pivot, pivot + 1);
        this.paint = p;
        paint.setStyle(Paint.Style.FILL);
        paint.setTypeface(Typeface.MONOSPACE);
        paint.setTextSize(textSize);
        paint.setAntiAlias(true);
        textWidth = paint.measureText(word);
        letterWidth = textWidth / word.length(); //calculate width for one letter
        xWordOffset = letterWidth / 2 + letterWidth * pivot; //set offset to center of pivot letter
        yWordOffset = textSize / 3;
        xLetterOffset = letterWidth * pivot - xWordOffset; //pivot offset
        yLetterOffset = yWordOffset;
    }

    public void Draw(Canvas canvas, int width, int height) {
        int halfWidth = width / 2;
        int halfHeight = height / 2;
        paint.setColor(Color.BLACK);
        canvas.drawText(word, halfWidth - xWordOffset, halfHeight + yWordOffset, paint);
        paint.setColor(Color.RED);
        canvas.drawText(pivotLetter, halfWidth + xLetterOffset, halfHeight + yLetterOffset, paint);
    }

    public String getWord() {
        return word;
    }
}
