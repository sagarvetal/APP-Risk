package com.app.risk.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

/**
 * Class to display the map on the screen in its given x and y position
 *
 * @author Urvi Mali
 * @version 1.0.0
 */
public class MapObjectsView extends View {
    private Paint paint;
    public float xPosition;
    public float yPosition;
    public int continentColor;

    /**
     * Constructor which creates paint and assigns the continent's color to it
     * @param context application context
     */
    public MapObjectsView(Context context) {
        super(context);
        // create the Paint and set its color
        paint = new Paint();
        paint.setColor(continentColor);
    }

    /**
     * {@inheritDoc}
     * @param canvas canvas to draw on
     */
    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.BLUE);
        canvas.drawCircle(100, 100, 100, paint);
    }
}
