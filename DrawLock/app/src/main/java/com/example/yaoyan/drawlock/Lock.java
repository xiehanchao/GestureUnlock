package com.example.yaoyan.drawlock;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yaoyan on 2017/8/6.
 */

public class Lock extends View {
    //结果是否正确
    private boolean isCorrect = false;
    private List<Point> points;
    //圆的中心X轴
    private int centerX;
    //圆的中心Y轴
    private int centerY;
    //小圆半径
    private int smallRadius = 15;
    //大圆半径
    private int bigRadius = 80;
    //控件的宽
    private int width;
    //控件的高
    private int height;
    //设置原点间距
    private int space = 100;
    //普通的圆
    private Paint bigNormalPaint;
    private Paint smallNormalPaint;
    private List<Rect> pointsRect;
    private List<Rect> selectPoints;

    private Canvas canvas;
    private Paint correctPaint;
    private Paint corrBigNormalPaint;
    //正确密码
    List<Integer> correctPassword = new ArrayList<Integer>();
    private List<Integer> number;
    private Paint errorBigNormalPaint;
    private Toast correctToast;
    private Toast errorToast;

    public Lock(Context context) {
        super(context);
        init();
    }

    public Lock(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Lock(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

        number = new ArrayList<Integer>();
        correctPassword.add(0);
        correctPassword.add(1);
        correctPassword.add(2);
        correctPassword.add(5);

        points = new ArrayList<Point>();
        selectPoints = new ArrayList<Rect>();

        //大圆画笔
        bigNormalPaint = new Paint();
        bigNormalPaint.setColor(Color.BLACK);
        bigNormalPaint.setStyle(Paint.Style.STROKE);
        bigNormalPaint.setAntiAlias(true);
        bigNormalPaint.setStrokeWidth(8);
        //小圆画笔
        smallNormalPaint = new Paint();
        smallNormalPaint.setColor(Color.GRAY);
        smallNormalPaint.setStyle(Paint.Style.STROKE);
        smallNormalPaint.setAntiAlias(true);
        smallNormalPaint.setStrokeWidth(15);

        //正确圆的颜色
        corrBigNormalPaint = new Paint();
        corrBigNormalPaint.setColor(Color.BLUE);
        corrBigNormalPaint.setStyle(Paint.Style.STROKE);
        corrBigNormalPaint.setAntiAlias(true);
        corrBigNormalPaint.setStrokeWidth(8);

        // 正确线颜色
        correctPaint = new Paint();
        correctPaint.setColor(Color.BLUE);
        correctPaint.setStyle(Paint.Style.STROKE);
        correctPaint.setStrokeWidth(15);

        //错误圆的颜色
        errorBigNormalPaint = new Paint();
        errorBigNormalPaint.setColor(Color.RED);
        errorBigNormalPaint.setStyle(Paint.Style.STROKE);
        errorBigNormalPaint.setAntiAlias(true);
        errorBigNormalPaint.setStrokeWidth(8);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);

        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        if (widthMode == MeasureSpec.AT_MOST || heightMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(1000, 1000);
        } else if (widthMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(1000, heightSize);
        } else if (heightMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(widthSize, 1000);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {

        initPoints(canvas);
        drawLine(canvas);

    }


    private void initPoints(Canvas canvas) {

        width = getMeasuredWidth();
        height = getMeasuredHeight();

        int startY = space + bigRadius;
        int temp = 0;
        for (int i = 0; i < 3; i++) {
            int startX = 0;
            for (int j = 0; j < 3; j++) {
                if (j == 0) {

                    temp = startX + space + bigRadius;
                    points.add(new Point(temp, startY));
                    startX = temp;
                } else {
                    temp = startX + space + bigRadius * 2;
                    points.add(new Point(temp, startY));
                    startX = temp;
                }
            }
            startY = startY + space + bigRadius * 2;
        }
        pointsRect = new ArrayList<Rect>();
        for (Point point : points) {

            pointsRect.add(new Rect(point.x - bigRadius, point.y - bigRadius, point.x + bigRadius, point.y + bigRadius));
            canvas.drawCircle(point.x, point.y, bigRadius, bigNormalPaint);
            canvas.drawCircle(point.x, point.y, smallRadius, smallNormalPaint);
        }


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int touchX = (int) event.getX();
        int touchY = (int) event.getY();

        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                addPoint(touchX, touchY);
                break;
            case MotionEvent.ACTION_MOVE:
                addPoint(touchX, touchY);
                break;
            case MotionEvent.ACTION_UP:
                System.out.println("selectPoints = " + selectPoints);


                invalidate();

                break;
        }

        return true;
    }

    private void drawLine(Canvas canvas) {

        if (selectPoints.size() > 0) {


            Rect rect = selectPoints.get(0);
            Rect firstRect = null;
            if (rect != null) {
                firstRect = rect;
                Path path = new Path();

                System.out.println("firstRect.centerX() = " + firstRect.centerX());
                System.out.println("firstRect.centerY() = " + firstRect.centerY());
                path.moveTo(firstRect.centerX(), firstRect.centerY());
                for (int i = 1; i < selectPoints.size(); i++) {
                    Rect nextRect = selectPoints.get(i);
                    path.lineTo(nextRect.centerX(), nextRect.centerY());

                    System.out.println("nextRect.centerX() = " + nextRect.centerX());
                    System.out.println("nextRect.centerY() = " + nextRect.centerY());

                }
                System.out.println("number = " + number);
                System.out.println("correctPassword = " + correctPassword);
                if (number.size() == correctPassword.size()) {
                    for (int i = 0; i < number.size(); i++) {
                        if (number.get(i) == correctPassword.get(i)) {
                            isCorrect = true;
                        } else {
                            isCorrect = false;
                            break;
                        }
                    }

                }
                //重新绘制圆
                for (int i = 0; i < selectPoints.size(); i++) {
                    Rect selectPoint = selectPoints.get(i);
                    if (isCorrect) {
                        canvas.drawCircle(selectPoint.centerX(), selectPoint.centerY(), bigRadius, corrBigNormalPaint);
                        canvas.drawPath(path, correctPaint);
//                        if (errorToast != null) {
//                            errorToast.cancel();
//                        }
//                        correctToast = Toast.makeText(getContext(), "密码输入正确", Toast.LENGTH_SHORT);
//                        correctToast.show();

                    } else {
                        canvas.drawCircle(selectPoint.centerX(), selectPoint.centerY(), bigRadius, errorBigNormalPaint);
                        canvas.drawPath(path, errorBigNormalPaint);
//                        if (correctToast != null) {
//                            correctToast.cancel();
//
//                        }
//                        errorToast = Toast.makeText(getContext(), "密码输入错误", Toast.LENGTH_SHORT);
//                        errorToast.show();
                    }
                }
                if (isCorrect) {
                    correctToast = Toast.makeText(getContext(), "密码输入正确", Toast.LENGTH_SHORT);
                    correctToast.show();

                } else {
                    errorToast = Toast.makeText(getContext(), "密码输入错误", Toast.LENGTH_SHORT);
                    errorToast.show();
                }

                postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        invalidate();
                    }
                }, 2000);
            }
        }
        selectPoints.clear();
        number.clear();
        isCorrect = false;
    }

    private void addPoint(int touchX, int touchY) {
        for (int i = 0; i < pointsRect.size(); i++) {
            Rect rect = pointsRect.get(i);
            if (rect.contains(touchX, touchY)) {
                if (!selectPoints.contains(rect)) {
                    selectPoints.add(rect);
                    number.add(i);
                }
            }
        }
    }
}
