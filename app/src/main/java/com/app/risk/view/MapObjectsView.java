package com.app.risk.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class MapObjectsView extends View {
    private Paint paint;
    public float xPosition;
    public float yPosition;
    public int continentColor;


    public MapObjectsView(Context context) {
        super(context);
        // create the Paint and set its color
        paint = new Paint();
        paint.setColor(continentColor);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.BLUE);
        canvas.drawCircle(100, 100, 100, paint);
    }
}
