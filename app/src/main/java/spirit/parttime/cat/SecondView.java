package spirit.parttime.cat;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class SecondView extends View {
	private Bitmap bgView ;
	private Bitmap numbersView;
	private int number = 9;
	private Paint paint ;
	private Rect bgsrc ,numberRect;
	private Rect bgdst ,numberDst;
	private DaoJiAnimation animation ;
	private boolean daojirun = true ;
//	private MediaPlayer secondMP ;
	public SecondView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub	
		final int bgViewId ,numbersViewId;
		bgViewId = attrs.getAttributeResourceValue(null, "bgview", -1);	
		numbersViewId = attrs.getAttributeResourceValue(null, "numbersview", -1);	
		if(bgViewId!=-1){
			this.bgView = ((BitmapDrawable)this.getResources().getDrawable(bgViewId)).getBitmap();
		}
		if(numbersViewId!=-1){
			this.numbersView = ((BitmapDrawable)this.getResources().getDrawable(numbersViewId)).getBitmap();
		}
		paint = new Paint();
		bgsrc = new Rect(0,0,bgView.getWidth(),bgView.getHeight());
		bgdst = new Rect(0,0,bgView.getWidth(),bgView.getHeight());
		this.numberRect = new Rect(0,0,15,29);
		this.numberDst = new Rect(//初始文字位置,因为第一个字符后面的处理所以先减，后再加。
				this.bgdst.centerX()-(this.numberRect.width()/2),//左偏移
				this.bgdst.centerY()-this.numberRect.height()/2,
				this.bgdst.centerX()-(this.numberRect.width()/2)+this.numberRect.width(),
				this.bgdst.centerY()+(this.numberRect.height()/2));
	}
	
	 class DaoJiAnimation extends Thread{
		 int time ;
		 boolean daojirun;
		 DaoJiAnimation(int time){
			 this.time = time ;
			 this.daojirun = SecondView.this.daojirun;
		 }
		 public void stopRun(){
			 this.daojirun = false;
		 }
		 public void run(){
			 while(this.daojirun){
				 if(time >=0){
					 try {
						SecondView.this.setNumber(time);
						SecondView.this.postInvalidate();
					//	secondMP.start();
						this.sleep(1000);
						time --;
							
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				 }else{
					 Game.game.onSecondUp();
					 break;
				 }
				 
			  }
			
		 }
	 }
	 
	protected void onDraw(Canvas canvas){
		super.onDraw(canvas);
		canvas.drawBitmap(bgView,bgsrc ,bgdst, paint);
		Rect src =  new Rect(this.numberRect);//数字图片当前数字框		
		Log.d("num111", ""+this.number);
		Log.d("num112", ""+src.width());
		Log.d("num113", ""+src.height());
		
		switch(this.number){
			case 0 :{//0
				src.left += 6;
				src.right += 6;
				canvas.drawBitmap(this.numbersView, src, numberDst, paint);
			}break;
			case 1 :{//1
				src.left += 22;
				src.right += 22;
				canvas.drawBitmap(this.numbersView, src, numberDst, paint);
			}break;
			case 2 :{//2
				src.left += 39;
				src.right += 39;
				canvas.drawBitmap(this.numbersView, src, numberDst, paint);
			}break;
			case 3 :{//3
				src.left += 55;
				src.right += 55;
				canvas.drawBitmap(this.numbersView, src, numberDst, paint);
			}break;
			case 4 :{//4
				src.left += 72;
				src.right += 72;
				canvas.drawBitmap(this.numbersView, src, numberDst, paint);
			}break;
			case 5 :{//5
				src.left += 88;
				src.right += 88;
				canvas.drawBitmap(this.numbersView, src, numberDst, paint);
			}break;
			case 6 :{//6
				src.left += 104;
				src.right += 104;
				canvas.drawBitmap(this.numbersView, src, numberDst, paint);
			}break;
			case 7 :{//7
				src.left += 120;
				src.right += 120;
				canvas.drawBitmap(this.numbersView, src, numberDst, paint);
			}break;
			case 8 :{//8
				src.left += 136;
				src.right += 136;
				canvas.drawBitmap(this.numbersView, src, numberDst, paint);
			}break;
			case 9 :{//9
				src.left += 152;
				src.right += 152;
				canvas.drawBitmap(this.numbersView, src, numberDst, paint);
			}break;
			
		}
	}

	public int getNumber() {
		return number;
	}
    public boolean isOver(){
    	if(this.number == 0)
    		return true ;
    	else
    		return false ;
    }
    public void reStart(){
    	this.number = 9;
    	this.daojirun =true;
    	daoJiStop();
    	this.animation = new DaoJiAnimation(this.number);
    	this.animation.start();
    	//Log.d("running1111111111", "running");
    }
    public void daoJiStart(){
    	reStart();
    }
    public void daoJiStop(){
    	if(this.animation!=null){
    		this.animation.stopRun();
    		this.animation = null;
    	}	
    }
	public void setNumber(int number) {
		this.number = number;
		//this.invalidate();
	}

}
