package com.kim.dragdrop;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by KIM on 15/12/25.
 */
public class DrawView extends View {

    private Ball[] balls = new Ball[3];
    private int ballID = -1;

    public DrawView(Context context) {
        super(context);
        setFocusable(true);

        Point point1 = new Point();
        point1.x = 50;
        point1.y = 20;
        Point point2 = new Point();
        point2.x = 100;
        point2.y = 20;
        Point point3 = new Point();
        point3.x = 150;
        point3.y = 20;

        balls[0] = new Ball(context, R.drawable.red_ball, point1);
        balls[1] = new Ball(context, R.drawable.blue_ball, point2);
        balls[2] = new Ball(context, R.drawable.green_ball, point3);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (Ball ball : balls) {
            canvas.drawBitmap(ball.getBitmap(), ball.getX(), ball.getY(), null);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int X = (int) event.getX();
        int Y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                ballID = -1;
                for (int i = 0; i < 3; i++) {
                    Ball ball = balls[i];
                    int centerX = ball.getX() + 25;
                    int centerY = ball.getY() + 25;

                    double radCircle = Math.sqrt((double) (((centerX - X) * (centerX - X)) + ((centerY - Y) * (centerY - Y))));

                    if (radCircle < 23) {
                        ballID = i;
                        break;
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (ballID >= 0) {
                    balls[ballID].setX(X - 25);
                    balls[ballID].setY(Y - 25);
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        invalidate();
        return true;
    }
}
