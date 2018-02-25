package spirit.parttime.cat;

import android.content.Context;
import android.graphics.Bitmap;
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
	final int titleId ;//用于寻找titlePicture
	final Rect title_rect;//title图片范围
	//numbers===================
	final int numberId ;//用于寻找数字图片
	final Rect number_rect;//number图片范围
	//text======================
	private String text ;//中部显示的文字
	final Rect text_rect ;//text的区域
	//图片=======================
	final Bitmap title ;//上部显示的图片
	final Bitmap numbers;
	
	public MyTextView(Context context, AttributeSet attrs, int defStyle ) {
		super(context, attrs, defStyle);
		this.text= attrs.getAttributeValue(null, "text");
		//初始化text============================================================
		this.text= attrs.getAttributeValue(null, "text");
		int textrect_X,textrect_Y,textrect_width,textrect_height;
		textrect_X =attrs.getAttributeIntValue(null, "textrect_X", 0);
		textrect_Y =attrs.getAttributeIntValue(null, "textrect_Y", 0);
		textrect_width =attrs.getAttributeIntValue(null, "textrect_width", 0);
		textrect_height =attrs.getAttributeIntValue(null, "textrect_height", 0);
		this.text_rect= new Rect(
				textrect_X,
				textrect_Y,
				textrect_X+textrect_width,
				textrect_Y+textrect_height);
		//初始化title============================================================
		this.titleId = attrs.getAttributeResourceValue(null, "titleId", -1);
		int title_startX =0,title_startY=0,title_width=0,title_height=0;
		title_startX =attrs.getAttributeIntValue(null, "title_startX", 0);
		title_startY =attrs.getAttributeIntValue(null, "title_startY", 0);
		title_width =attrs.getAttributeIntValue(null, "title_width", 0);
		title_height =attrs.getAttributeIntValue(null, "title_height", 0);
		this.title_rect= new Rect(
				title_startX,
				title_startY,
				title_startX+title_width,
				title_startY+title_height);
		if(this.titleId!= -1){
			//获取title图片
			this.title = Bitmap.createBitmap(((BitmapDrawable)this.getResources().getDrawable(titleId)).getBitmap(), this.title_rect.left, this.title_rect.top, this.title_rect.width(), this.title_rect.height());
		}else{
			this.title =null ;
		}
		//初始化numbers================================================================
		this.numberId = attrs.getAttributeResourceValue(null, "numberId", -1);
		int number_startX=0,number_startY=0,number_onewidth=0,number_oneheight=0;
		number_startX =attrs.getAttributeIntValue(null, "number_startX", 0);
		number_startY =attrs.getAttributeIntValue(null, "number_startY", 0);
		number_onewidth =attrs.getAttributeIntValue(null, "number_onewidth", 0);
		number_oneheight =attrs.getAttributeIntValue(null, "number_oneheight", 0);
		this.number_rect=new Rect(
				number_startY,
				number_startY,
				number_startY+number_onewidth,
				number_startY+number_oneheight);
		if(this.numberId!= -1){
			//获取numbers图片
			this.numbers = ((BitmapDrawable)this.getResources().getDrawable(numberId)).getBitmap();
		}else{
			this.numbers =null ;
		}
		
		// TODO Auto-generated constructor stub
	}
	
	public MyTextView(Context context, AttributeSet attrs) {
		this(context,attrs,0);	
	}
	
	
	/*protected void onSizeChanged(int w, int h ,int oldw, int oldh){
		
	}*/
	protected void onDraw(Canvas canvas){
		Paint paint = new Paint();
		if(this.title!=null){
			canvas.drawBitmap(this.title, 0,0, paint);
		}
		if(this.numbers!=null){
			int num = this.text.length();//数字数目
			
			for(int i = 0 ; i< num;i++){
				int nowNum = text.codePointAt(i);//当前数字
				Rect dst = new Rect(//初始文字位置,因为第一个字符后面的处理所以先减，后再加。
						this.text_rect.centerX()-(num*this.number_rect.width())/2+i*this.number_rect.width(),//左偏移
						this.text_rect.centerY()-this.number_rect.height()/2,
						this.text_rect.centerX()-(num*this.number_rect.width())/2+(i+1)*this.number_rect.width(),
						this.text_rect.centerY()+this.number_rect.height()/2);
				Rect src =  new Rect(this.number_rect);//数字图片当前数字框			
				switch(nowNum){
					case 48 :{//0
						src.left += 6;
						src.right += 6;
						canvas.drawBitmap(this.numbers, src, dst, paint);
					}break;
					case 49 :{//1
						src.left += 22;
						src.right += 22;
						canvas.drawBitmap(this.numbers, src, dst, paint);
					}break;
					case 50 :{//2
						src.left += 39;
						src.right += 39;
						canvas.drawBitmap(this.numbers, src, dst, paint);
					}break;
					case 51 :{//3
						src.left += 55;
						src.right += 55;
						canvas.drawBitmap(this.numbers, src, dst, paint);
					}break;
					case 52 :{//4
						src.left += 72;
						src.right += 72;
						canvas.drawBitmap(this.numbers, src, dst, paint);
					}break;
					case 53 :{//5
						src.left += 88;
						src.right += 88;
						canvas.drawBitmap(this.numbers, src, dst, paint);
					}break;
					case 54 :{//6
						src.left += 104;
						src.right += 104;
						canvas.drawBitmap(this.numbers, src, dst, paint);
					}break;
					case 55 :{//7
						src.left += 120;
						src.right += 120;
						canvas.drawBitmap(this.numbers, src, dst, paint);
					}break;
					case 56 :{//8
						src.left += 136;
						src.right += 136;
						canvas.drawBitmap(this.numbers, src, dst, paint);
					}break;
					case 57 :{//9
						src.left += 152;
						src.right += 152;
						canvas.drawBitmap(this.numbers, src, dst, paint);
					}break;
					default:{
						canvas.drawText("?", dst.centerX(), dst.exactCenterY(), paint);
					}
				}
			}
		}else{
         //   Rect rect = new Rect(0,this.getHeight()/2,this.getWidth(),this.getHeight()/2);
			Log.d("111111","no");
			Rect rect = new Rect(90,43,50,29);
			canvas.drawText(this.text, rect.exactCenterX(),rect.exactCenterY(), paint);
		}
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
    	this.postInvalidate(text_rect.left, text_rect.top,text_rect.right, text_rect.bottom);
	}
	

}
