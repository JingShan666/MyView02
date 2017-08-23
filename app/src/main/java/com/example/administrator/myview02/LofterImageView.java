package com.example.administrator.myview02;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Liu on 2017/8/22 0022.
 * 加载进度框
 */

public class LofterImageView extends View {
    /**
     背景圆形矩形配置
     */
    //背景圆角矩形
    private RectF mBgRect;
    //背景圆角矩形的宽度
    private int mBgWith;
    //背景圆角矩形的颜色
    private int mBgColor;
    //背景圆角矩形的圆角半径
    private int mBgCornerRadius;

    //控件中心x轴的位置
    private int centerX;
    //控件中心y轴的位置
    private int centerY;

    //背景圆角矩形画笔
    private Paint mBgRectPaint;

    /**
     静态圆环配置
     */
    //静态圆环半径
    private int innerRadius;
    //静态圆环颜色
    private int innerColor;
    //静态圆环宽度
    private int innerWith;
    //静态圆环画笔
    private Paint mInnerPaint;

    /**
     进度条圆弧配置
     */
    //进度条圆弧颜色
    private int outColor;
    //进度条圆弧画笔
    private Paint mOutPaint;
    //圆弧外切的矩形
    private RectF mOval;
    //圆弧扫过的角度
    private float progress;

    /**
     文字配置
     */
    //文字（百分比）的宽度
    private float mTextWidth;
    //文字（百分比）的高度
    private float mTextHeight;
    //字体颜色
    private int mTextColor;

    //字体大小
    private int mTextSize;
    //文字画笔
    private Paint mTextPaint;
    //字体内容
    private String percentText;

    /**
     进度配置
     */
    //当前进度
    private int mCurrentProgress = 0;
    //总进度
    private int mTotalProgress = 1;
    //百分比（外部直接设置百分比）
    private int mPercent;

    public LofterImageView(Context context) {
        this(context, null);
    }

    public LofterImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LofterImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        //初始化自定义属性
        initAttrs(context,attrs);
        //初始化配置
        initProSettings();
    }



    private void initAttrs(Context context, AttributeSet attrs) {
        /**
         获取背景圆形矩形属性
         */
        TypedArray typedArray= context.obtainStyledAttributes(attrs,R.styleable.ProgressView);
        //获取背景圆角矩形的宽
        mBgWith= typedArray.getDimensionPixelSize(R.styleable.ProgressView_bgWidth,210);
        //获取背景圆角矩形的颜色
        mBgColor= typedArray.getColor(R.styleable.ProgressView_bgColor, Color.WHITE);
        //获取背景圆角矩形的圆角半径
        mBgCornerRadius= typedArray.getDimensionPixelSize(R.styleable.ProgressView_bgCornerRadius,28);
        /**
         获取静态圆环属性
         */
        //获取静态圆环半径
        innerRadius= typedArray.getDimensionPixelSize(R.styleable.ProgressView_innerRadius,50);
        //获取静态圆环颜色
        innerColor= typedArray.getColor(R.styleable.ProgressView_edgeColor,Color.LTGRAY);
        //获取静态圆环宽度
        innerWith= typedArray.getDimensionPixelSize(R.styleable.ProgressView_ringWidth,10);
        /**
         获取进度条圆弧属性
         */
        //获取进度条圆弧颜色
        outColor= typedArray.getColor(R.styleable.ProgressView_ringColor,Color.RED);
        /**
         获取字体属性
         */
        //获取字体颜色
        mTextColor= typedArray.getColor(R.styleable.ProgressView_percentColor,Color.GRAY);
        //获取字体大小
        mTextSize= typedArray.getDimensionPixelSize(R.styleable.ProgressView_percentSize,30);


        typedArray.recycle();


    }

    private void initProSettings() {
        /**
         背景矩形画笔
         */
        mBgRect= new RectF();
        mBgRectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBgRectPaint.setColor(mBgColor);
        mBgRectPaint.setStyle(Paint.Style.FILL);

        /**
         静态圆环画笔
         */
        mInnerPaint= new Paint(Paint.ANTI_ALIAS_FLAG);
        mInnerPaint.setColor(innerColor);
        mInnerPaint.setStrokeCap(Paint.Cap.ROUND);
        mInnerPaint.setStrokeWidth(innerWith);
        mInnerPaint.setStyle(Paint.Style.STROKE);

        /**
         动态进度条圆弧画笔
         */
        mOutPaint= new Paint(Paint.ANTI_ALIAS_FLAG);
        mOutPaint.setColor(outColor);
        mOutPaint.setStrokeWidth(innerWith);
        mOutPaint.setStyle(Paint.Style.STROKE);

        mOval= new RectF();

        /**
         文字画笔
         */
        mTextPaint= new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(outColor);
        mTextPaint.setStyle(Paint.Style.STROKE);
        mTextPaint.setTextSize(mTextSize);


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        /**
         * getMeasuredWidth()/2表示屏幕宽度的一半
         * getMeasuredHeight()/2表示屏幕高度的一半
         */

        centerX= getMeasuredWidth()/2;
        centerY= getMeasuredHeight()/2;

        /**
         * 确定背景圆角图片在坐标系的位置
         * 参数left,top,right,bottom表示矩形左上角和右下角的坐标
         * mBgWith/2表示矩形实际宽度的一半
         *
         */
        float left= centerX-mBgWith/2;
        float top= centerY-mBgWith/2;
        float right= centerX+mBgWith/2;
        float bottom= centerY+mBgWith/2;

        mBgRect.set(left,top,right,bottom);

        /**
         * 计算圆弧外切矩形的位置
         */
        mOval.left= centerX - innerRadius;
        mOval.top = centerY - innerRadius;
        mOval.right = centerX + innerRadius;
        mOval.bottom = centerY + innerRadius;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        //绘制背景圆角矩形
        canvas.drawRoundRect(mBgRect,mBgCornerRadius,mBgCornerRadius,mBgRectPaint);

        //画静态圆环，相当于进度为0时候的显示
        canvas.drawCircle(centerX,centerY,innerRadius,mInnerPaint);




        /**
         * mPercent是指下载进度
         * 圆，一周是360度
         * 这里圆弧扫过的角度必须要乘以360
         */
        if (mPercent != 0) {
            progress = (float) mPercent / 100 * 360;
            percentText = mPercent + "%";
        } else {
            progress = (float) mCurrentProgress / mTotalProgress * 360;
            percentText = (int) (mCurrentProgress * 1.0f / mTotalProgress * 100) + "%";
        }

        //画进度条圆环
        //第二个参数是指圆弧的起点在哪，这里是以3点钟方向为0度，因此我们这里写-90
        //第三个参数true的意思就是要将圆弧与圆心连起来，也就是话一个扇形，画一个饼，这里我们不需要，设置为false
        canvas.drawArc(mOval, -90, progress, false, mOutPaint);
        //绘制字体高度
        Paint.FontMetrics fm = mTextPaint.getFontMetrics();
        mTextHeight = (int) Math.ceil(fm.descent - fm.ascent);
        //绘制字体（先调整一下文字的位置，使文字处于中间的位置）
        //计算字体的宽度
        mTextWidth = mTextPaint.measureText(percentText, 0, percentText.length());
        //画文字
        canvas.drawText(percentText, centerX - mTextWidth / 2, centerY + mTextHeight / 4, mTextPaint);

    }


    /**
     * 直接从外部设置百分比
     *
     * @param percent 百分比
     */
    public void setPercent(int percent) {
        this.mPercent = percent;
        //重绘
        postInvalidate();
    }

    /**
     * 外部接口，设置当前进度
     *
     * @param progress 进度
     */
    public void setProgress(int progress) {
        this.mCurrentProgress = progress;
        postInvalidate();
    }

    /**
     * 外部接口，设置总进度
     *
     * @param totalProgress 总进度
     */
    public void setTotalProgress(int totalProgress) {
        this.mTotalProgress = totalProgress;
    }


}
