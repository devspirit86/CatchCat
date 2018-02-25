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


public class MyLivesTextView extends View {
	//title=====================
	final int titleId ;//用于寻找titlePicture
	final Rect title_rect;//title图片范围
	//numbers===================
	final int numberId ;//用于寻找数字图片
	//text======================
	private int text ;//中部显示的文字
	final Rect text_rect ;//text的区域
	//图片=======================
	final Bitmap title ;//上部显示的图片
	final Rect lifesrc ;
	final Bitmap lifes;
	
	public MyLivesTextView(Context context, AttributeSet attrs, int defStyle ) {
		super(context, attrs, defStyle);
		//初始化text============================================================
		this.text= attrs.getAttributeIntValue(null,"text", 0);
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
		if(this.numberId!= -1){
			//获取numbers图片
			this.lifes = ((BitmapDrawable)this.getResources().getDrawable(numberId)).getBitmap();
			this.lifesrc = new Rect(0,0,lifes.getWidth(),lifes.getHeight());
		}else{
			this.lifes =null ;
			this.lifesrc = null;
		}
		
		
		// TODO Auto-generated constructor stub
	}
	
	public MyLivesTextView(Context context, AttributeSet attrs) {
		this(context,attrs,0);	
	}
	
	
	/*protected void onSizeChanged(int w, int h ,int oldw, int oldh){
		
	}*/
	protected void onDraw(Canvas canvas){
		Paint paint = new Paint();
		if(this.title!=null){
			canvas.drawBitmap(this.title, 0,0, paint);
		}
		int x = (this.text_rect.width()-3*lifesrc.width())/2;
		Rect dst = new Rect(
				this.text_rect.left+x,
				this.text_rect.top,
				this.text_rect.left+x+lifesrc.width(),
				this.text_rect.top+lifesrc.height());
		Log.d("drawing111", "drawing");
		for(int i = 0;i<this.text;i++){
			//Log.d("111w", ""+lifesrc);
			canvas.drawBitmap(this.lifes, lifesrc, dst, paint);
			dst.left += this.lifesrc.width();
			dst.right += this.lifesrc.width();
		}
		
	
	}
	public int getText() {
		return text;
	}
	public void setText(int text) {
		this.text = text;
    	this.postInvalidate(text_rect.left, text_rect.top,text_rect.right, text_rect.bottom);
	}
	

}
