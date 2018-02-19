package spirit.parttime.cat;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;


public class MyTextView extends View {
    //title=====================
    final int titleId;//用于寻找titlePicture
    final Rect title_rect;//title图片范围
    //numbers===================
    final int numberId;//用于寻找数字图片
    final Rect number_rect;//number图片范围
    //text======================
    private String text;//中部显示的文字
    final Rect text_rect;//text的区域
    //图片=======================
    final Bitmap title;//上部显示的图片
    final Bitmap numbers;
    final Bitmap[] numberSet = new Bitmap[10];
    final int numRectWidth ;
    final int numRectHeight ;

    public MyTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.MyTextView);
        //初始化text============================================================

        this.text = array.getString(R.styleable.MyTextView_text);
        int textrect_X, textrect_Y, textrect_width, textrect_height;
        textrect_X = array.getDimensionPixelSize(R.styleable.MyTextView_text_rect_X, -1);
        textrect_Y = array.getDimensionPixelSize(R.styleable.MyTextView_text_rect_Y, -1);
        textrect_width = array.getDimensionPixelSize(R.styleable.MyTextView_text_rect_width, -1);
        textrect_height = array.getDimensionPixelSize(R.styleable.MyTextView_text_rect_height, -1);

        Log.i("attr_textrect_X", "" + textrect_X);

        this.text_rect = new Rect(
                textrect_X,
                textrect_Y,
                textrect_X + textrect_width,
                textrect_Y + textrect_height);

        //初始化title============================================================
        this.titleId = array.getResourceId(R.styleable.MyTextView_titleId, -1);
        int title_startX = 0, title_startY = 0, title_width = 0, title_height = 0;
        title_startX = array.getDimensionPixelSize(R.styleable.MyTextView_title_src_startX, -1);
        title_startY = array.getDimensionPixelSize(R.styleable.MyTextView_title_src_startY, -1);
        title_width = array.getDimensionPixelSize(R.styleable.MyTextView_title_src_width, -1);
        title_height = array.getDimensionPixelSize(R.styleable.MyTextView_title_src_height, -1);

        if (this.titleId != -1) {
            //获取title图片
            this.title = Bitmap.createBitmap(((BitmapDrawable) this.getResources().getDrawable(titleId)).getBitmap(), title_startX, title_startY, title_width, title_height);
        } else {
            this.title = null;
        }

        int title_rect_startX = 0, title_rect_startY = 0, title_rect_width = 0, title_rect_height = 0;
        title_rect_startX = array.getDimensionPixelSize(R.styleable.MyTextView_title_rect_startX, -1);
        title_rect_startY = array.getDimensionPixelSize(R.styleable.MyTextView_title_rect_startY, -1);
        title_rect_width = array.getDimensionPixelSize(R.styleable.MyTextView_title_rect_width, -1);
        title_rect_height = array.getDimensionPixelSize(R.styleable.MyTextView_title_rect_height, -1);
        this.title_rect = new Rect(
                title_rect_startX,
                title_rect_startY,
                title_rect_startX + title_rect_width,
                title_rect_startY + title_rect_height);

        //初始化numbers================================================================
        this.numberId = array.getResourceId(R.styleable.MyTextView_numberId, -1);
        if (this.numberId != -1) {
            //获取numbers图片
            this.numbers = BitmapFactory.decodeResource(context.getResources(), numberId);
            //	this.numbers = ((BitmapDrawable)this.getResources().getDrawable(numberId)).getBitmap();
        } else {
            this.numbers = null;
        }

        int number_startX = 0, number_startY = 0, number_onewidth = 0, number_oneheight = 0;
        number_startX = array.getDimensionPixelSize(R.styleable.MyTextView_number_src_startX, -1);
        number_startY = array.getDimensionPixelSize(R.styleable.MyTextView_number_src_startY, -1);
        number_onewidth = array.getDimensionPixelSize(R.styleable.MyTextView_number_src_onewidth, -1);
        number_oneheight = array.getDimensionPixelSize(R.styleable.MyTextView_number_src_oneheight, -1);

        this.number_rect = new Rect(
                number_startX,
                number_startY,
                number_startY + number_onewidth,
                number_startY + number_oneheight);

        for (int i = 0; i < 10; i++) {
            switch (i) {
                case 0:
                    numberSet[i] = Bitmap.createBitmap(numbers, 6, 0, number_onewidth, number_oneheight);
                    break;
                case 1:
                    numberSet[i] = Bitmap.createBitmap(numbers, 22, 0, number_onewidth, number_oneheight);
                    break;
                case 2:
                    numberSet[i] = Bitmap.createBitmap(numbers, 39, 0, number_onewidth, number_oneheight);
                    break;
                case 3:
                    numberSet[i] = Bitmap.createBitmap(numbers, 55, 0, number_onewidth, number_oneheight);
                    break;
                case 4:
                    numberSet[i] = Bitmap.createBitmap(numbers, 72, 0, number_onewidth, number_oneheight);
                    break;
                case 5:
                    numberSet[i] = Bitmap.createBitmap(numbers, 88, 0, number_onewidth, number_oneheight);
                    break;
                case 6:
                    numberSet[i] = Bitmap.createBitmap(numbers, 104, 0, number_onewidth, number_oneheight);
                    break;
                case 7:
                    numberSet[i] = Bitmap.createBitmap(numbers, 120, 0, number_onewidth, number_oneheight);
                    break;
                case 8:
                    numberSet[i] = Bitmap.createBitmap(numbers, 136, 0, number_onewidth, number_oneheight);
                    break;
                case 9:
                    numberSet[i] =  Bitmap.createBitmap(numbers, 152, 0, number_onewidth, number_oneheight);
                    break;
            }
        }

        numRectWidth = array.getDimensionPixelSize(R.styleable.MyTextView_number_rect_onewidth, -1);
        numRectHeight =  array.getDimensionPixelSize(R.styleable.MyTextView_number_rect_oneheight, -1);
        array.recycle();
        // TODO Auto-generated constructor stub
    }

    public MyTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }


    /*protected void onSizeChanged(int w, int h ,int oldw, int oldh){

    }*/
    protected void onDraw(Canvas canvas) {
        Paint paint = new Paint();
        if (this.title != null) {
            //	canvas.drawBitmap(this.title, 0,0, paint);
            canvas.drawBitmap(this.title, null, title_rect, paint);
        }
        if (this.numbers != null) {
            int num = this.text.length();//数字数目

            for (int i = 0; i < num; i++) {
                int nowNum = text.codePointAt(i);//当前数字
                Rect dst = new Rect(//初始文字位置,因为第一个字符后面的处理所以先减，后再加。
                        this.text_rect.centerX() - (num * numRectWidth) / 2 + i * numRectWidth,//左偏移
                        this.text_rect.centerY() - numRectHeight / 2,
                        this.text_rect.centerX() - (num * numRectWidth) / 2 + (i + 1) * numRectWidth,
                        this.text_rect.centerY() + numRectHeight / 2);
                Rect src = new Rect(this.number_rect);//数字图片当前数字框

                canvas.drawBitmap(this.numberSet[nowNum - 48], null, dst, paint);

            }
        } else {
            //   Rect rect = new Rect(0,this.getHeight()/2,this.getWidth(),this.getHeight()/2);
            Log.d("111111", "no");
            Rect rect = new Rect(90, 43, 50, 29);
            canvas.drawText(this.text, rect.exactCenterX(), rect.exactCenterY(), paint);
        }
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        this.postInvalidate(text_rect.left, text_rect.top, text_rect.right, text_rect.bottom);
    }


}
