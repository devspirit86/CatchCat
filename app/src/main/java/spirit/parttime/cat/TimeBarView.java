package spirit.parttime.cat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import spirit.parttime.cat.music.Music;

public class TimeBarView extends View {
	private Bitmap barBgView ;
	private Bitmap barView;
	private int time = 0;//当前时间
	private int maxtime ;//最大时间
	private float scaleY = 1.0f ;//比例 
	private Paint paint ;
	private Rect tiaoDst ;
	private Matrix matrix;
	private boolean isRunning = true;
	private DaoJiAnimation animation ;
	private Context context ;
	public TimeBarView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		// TODO Auto-generated constructor stub	
		int max = attrs.getAttributeIntValue(null, "maxtime",-1);
		if(max != -1){
			this.maxtime = max ;
		}else{
			this.maxtime = 60 ;//默认为60秒 
		} 
		int barBgViewId ,barViewId;
		barBgViewId = attrs.getAttributeResourceValue(null, "barbgview", -1);	
		barViewId = attrs.getAttributeResourceValue(null, "barview", -1);	
		if(barBgViewId!=-1){
			this.barBgView = ((BitmapDrawable)this.getResources().getDrawable(barBgViewId)).getBitmap();
		}
		if(barViewId!=-1){
			this.barView = ((BitmapDrawable)this.getResources().getDrawable(barViewId)).getBitmap();
		}
		this.matrix = new Matrix();
		paint = new Paint();
		tiaoDst = new Rect(0,0,this.barBgView.getWidth(),this.barBgView.getHeight()-53);
		
	}
	
	protected void onDraw(Canvas canvas){
		super.onDraw(canvas);
		scaleY = ((float)this.time/(float)this.maxtime)*barBgView.getHeight();
		Rect src = new Rect(0,(int)scaleY,barBgView.getWidth(),barBgView.getHeight());
		Rect dst = new Rect(0,(int)scaleY,barBgView.getWidth(),barBgView.getHeight());
		canvas.drawBitmap(barBgView, src, dst, paint);
		canvas.drawBitmap(this.barView, matrix, paint);
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
		this.postInvalidate();
	}
	public boolean isOver(){
		if(this.time==this.maxtime)
			return true ;
		else
			return false ;
	}
	public void jiShiStart(){
		if(this.animation!=null){
			this.animation.stopRun();
		}
		this.animation = new DaoJiAnimation(this.time);
		animation.start();
		
	}
	public void jiShiStop(){
		if(this.animation!=null){
			this.animation.stopRun();
		}
		
	}
	public int getMaxtime() {
		return maxtime;
	}
	
	public void setMaxtime(int maxtime) {
		this.maxtime = maxtime;
	}
	public int getTimeAnimation(){
		 this.jiShiStop();
		 int useTime = this.time ;
		 BoundAnimation b = new BoundAnimation(this.time);
		 b.start();
		 return useTime;
	 }
	 class DaoJiAnimation extends Thread{
		 int time2 ;
		 boolean isRunnable = true ;
		 DaoJiAnimation(int time){
			 this.time2 = time ;
		 }
		 public void stopRun(){
			 isRunnable = false ;
		 }
		 public void startRun(){
			 isRunnable = true ;
			 this.start();
		 }
		 public void run(){
			 while(isRunnable){
				 if(TimeBarView.this.time <= TimeBarView.this.maxtime){
					 Log.d("running", "running");
					 try {
						 TimeBarView.this.time = time2;
						 TimeBarView.this.postInvalidate();
						 if(time == maxtime-10){
							 Music.playShort(context, R.raw.timetip, false);
						 }else if(time==maxtime){
							 Music.playShort(context, R.raw.timeup, false);
						 }
						this.sleep(1000);
						time2 ++;
							
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				 }else{
					 Game.game.showTimeUp();
					 break ;
				 }
				 
			  }
			
		 }
	 }
	
	 class BoundAnimation extends Thread{
		 int time2 ;
		 BoundAnimation(int time){
			 this.time2 = time ;
		 }
		 public void run(){
			 Music.stopLong();//关背景音乐
			 Music.playShort(context, R.raw.countscore, true);
			 while(true){
				 if(TimeBarView.this.time <= TimeBarView.this.maxtime){
					 Log.d("running", "running");
					 try {
						 TimeBarView.this.time = time2;
						 TimeBarView.this.postInvalidate();
						//boundMp.start();
						this.sleep(50);//提速
						time2 ++;
							
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				 }else{
					 Music.stopShort();
					 Game.game.showBoundAdd();
					// boundMp.stop();
					 break ;
				 }
				 
			  }
			
		 }
	 }

}
