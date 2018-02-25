package spirit.parttime.cat;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageButton;

public class MyImageButton extends ImageButton {
	final Rect unFocusShow ;
	final Rect focusShow ;
	final Bitmap unFocus ;
	final Bitmap focus ;
	public MyImageButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		//初始化focus图片
		int focusShow_X = 0,focusShow_Y=0,focusShow_width=0,focusShow_height=0;
		focusShow_X = attrs.getAttributeIntValue(null, "focusShow_X", 0);
		focusShow_Y = attrs.getAttributeIntValue(null, "focusShow_Y", 0);
		focusShow_width = attrs.getAttributeIntValue(null, "focusShow_width", 0);
		focusShow_height = attrs.getAttributeIntValue(null, "focusShow_height", 0);
		this.focusShow = new Rect(
				focusShow_X,
				focusShow_Y,
				focusShow_X+focusShow_width,
				focusShow_Y+focusShow_height
				);
		this.focus = Bitmap.createBitmap(((BitmapDrawable)this.getBackground()).getBitmap(),focusShow.left,focusShow.top,focusShow.width(),focusShow.height() );
		//初始化unfocus图片
		focusShow_X = attrs.getAttributeIntValue(null, "unfocusShow_X", 0);
		focusShow_Y = attrs.getAttributeIntValue(null, "unfocusShow_Y", 0);
		focusShow_width = attrs.getAttributeIntValue(null, "unfocusShow_width", 0);
		focusShow_height = attrs.getAttributeIntValue(null, "unfocusShow_height", 0);
		this.unFocusShow = new Rect(
				focusShow_X,
				focusShow_Y,
				focusShow_X+focusShow_width,
				focusShow_Y+focusShow_height
				);
		this.unFocus = Bitmap.createBitmap(((BitmapDrawable)this.getBackground()).getBitmap(),unFocusShow.left,unFocusShow.top,unFocusShow.width(),unFocusShow.height() );
		// TODO Auto-generated constructor stub
		// TODO Auto-generated constructor stub
	}
	public MyImageButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		//初始化focus图片
		int focusShow_X = 0,focusShow_Y=0,focusShow_width=0,focusShow_height=0;
		focusShow_X = attrs.getAttributeIntValue(null, "focusShow_X", 0);
		focusShow_Y = attrs.getAttributeIntValue(null, "focusShow_Y", 0);
		focusShow_width = attrs.getAttributeIntValue(null, "focusShow_width", 0);
		focusShow_height = attrs.getAttributeIntValue(null, "focusShow_height", 0);
		this.focusShow = new Rect(
				focusShow_X,
				focusShow_Y,
				focusShow_X+focusShow_width,
				focusShow_Y+focusShow_height
				);
		this.focus = Bitmap.createBitmap(((BitmapDrawable)this.getBackground()).getBitmap(),focusShow.left,focusShow.top,focusShow.width(),focusShow.height() );
		//初始化unfocus图片
		focusShow_X = attrs.getAttributeIntValue(null, "unfocusShow_X", 0);
		focusShow_Y = attrs.getAttributeIntValue(null, "unfocusShow_Y", 0);
		focusShow_width = attrs.getAttributeIntValue(null, "unfocusShow_width", 0);
		focusShow_height = attrs.getAttributeIntValue(null, "unfocusShow_height", 0);
		this.unFocusShow = new Rect(
				focusShow_X,
				focusShow_Y,
				focusShow_X+focusShow_width,
				focusShow_Y+focusShow_height
				);
		this.unFocus = Bitmap.createBitmap(((BitmapDrawable)this.getBackground()).getBitmap(),unFocusShow.left,unFocusShow.top,unFocusShow.width(),unFocusShow.height() );
		// TODO Auto-generated constructor stub
		
	}
	public MyImageButton(Context context) {
		super(context);
		this.focus = null ;
		this.focusShow= new Rect(0,0,0,0);
		this.unFocus=null ;
		this.unFocusShow= new Rect(0,0,0,0);
		// TODO Auto-generated constructor stub
	}
	protected void onFocusChanged (boolean gainFocus, int direction, Rect previouslyFocusedRect){
		super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
	}
	protected void onDraw(Canvas canvas){
		Paint paint = new Paint();
		if(this.isFocused()){
			canvas.drawBitmap(this.focus, 0, 0, paint);
		}else{
			canvas.drawBitmap(this.unFocus, 0, 0, paint);
		}
		
	}
}
