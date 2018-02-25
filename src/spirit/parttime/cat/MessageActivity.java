package spirit.parttime.cat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import spirit.parttime.cat.music.Music;

public class MessageActivity extends Activity {
	LinearLayout layout ;
	public static final int levelShow =0 ,overShow = 1,timeUp = 2,showBound=3;
	public static final int showOption = 4,showHelp = 5,showTop=6;
	public static final int showFailure = 7, showWin = 8;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.message);
		WindowManager.LayoutParams lap = this.getWindow().getAttributes();
		lap.dimAmount = 0.3f ;
		lap.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND ;
		this.getWindow().setAttributes(lap);
		this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
		//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
		
		layout = (LinearLayout)this.findViewById(R.id.layout);
		Intent intent = this.getIntent();
		int showState = intent.getIntExtra("showState", -1);
		switch(showState){
			case levelShow:{
				GameLevel gl =new GameLevel(this);
				gl.setNumStr(intent.getStringExtra("level"));
				layout.addView(gl);
				TimeEnd te = new TimeEnd(this,3000, levelShow);
				te.start();
			}break;
			case overShow:{
				Music.playShort(this,R.raw.gameover, false);
				GameOver go =new GameOver(this);
				go.setNumStr(intent.getStringExtra("score"));
				layout.addView(go);
				TimeEnd te = new TimeEnd(this,5000, overShow);
				te.start();
			}break;
			case timeUp:{
				Bitmap b = BitmapFactory.decodeResource(this.getResources(), R.drawable.timeup);
				ImageView iv = new ImageView(this);
				iv.setLayoutParams(new LayoutParams(b.getWidth(),b.getHeight()));
				iv.setImageBitmap(b);
				layout.addView(iv);
				TimeEnd te = new TimeEnd(this,3000, timeUp);
				te.start();
			}break;
			case showBound:{
				Music.playShort(this, R.raw.bonus, false);
				ShowBound sb = new ShowBound(this);
				sb.setNumStr(""+intent.getIntExtra("score", 0));
				layout.addView(sb);
				TimeEnd te = new TimeEnd(this,3000, showBound);
				te.start();
				
			}break;
			case showOption:{
				Options sb = new Options(this);
				layout.addView(sb);
			}break;
			case showHelp:{
				Help sb = new Help(this);
				layout.addView(sb);
			}break;
			case showTop:{	
				int[] scoreList = Game.game.getScoreList();
				for(int i = 0;i<scoreList.length;i++){
					Log.d("now", i+":"+scoreList[i]) ;
				}
				TopScore sb = new TopScore(this,scoreList);
				layout.addView(sb);
			}break;
			case showFailure:{
				Bitmap b = BitmapFactory.decodeResource(this.getResources(),R.drawable.lose);
				View v = new View(this);
				v.setLayoutParams(new LayoutParams(b.getWidth(),b.getHeight()));
				v.setBackgroundDrawable(new BitmapDrawable(b));
				Music.playShort(this, R.raw.gamefail, false);
				layout.addView(v);
				TimeEnd te = new TimeEnd(this,3000, showFailure);
				te.start();
			}break;
			case showWin:{
				Bitmap b = BitmapFactory.decodeResource(this.getResources(),R.drawable.win);
				View v = new View(this);
				v.setLayoutParams(new LayoutParams(b.getWidth(),b.getHeight()));
				v.setBackgroundDrawable(new BitmapDrawable(b));
				layout.addView(v);
				Music.playShort(this, R.raw.gamewin, false);
				TimeEnd te = new TimeEnd(this,3000, showWin);
				te.start();
			}break;
		}

	}
	protected void onPause(){
		super.onPause();
		Music.stopLong();
	}
	public class GameLevel extends View{
		private Bitmap level ;
		private Bitmap num ;
		private Bitmap[] eachNum ;
		private String numStr="0";
		private final int numwidth = 35;
		public String getNumStr() {
			return numStr;
		}
		public void setNumStr(String numStr) {
			this.numStr = numStr;
		}
		public GameLevel(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
			level=((BitmapDrawable)this.getResources().getDrawable(R.drawable.level)).getBitmap();
			num=((BitmapDrawable)this.getResources().getDrawable(R.drawable.num_level)).getBitmap();
			eachNum = new Bitmap[10];
			
			eachNum[0] = Bitmap.createBitmap(num, 8, 0, numwidth, 40);
			eachNum[1] = Bitmap.createBitmap(num, 46+6, 0, numwidth, 40);
			eachNum[2] = Bitmap.createBitmap(num, 46*2+8, 0, numwidth, 40);
			eachNum[3] = Bitmap.createBitmap(num, 46*3+8, 0, numwidth, 40);
			eachNum[4] = Bitmap.createBitmap(num, 46*4+5, 0, numwidth, 40);
			eachNum[5] = Bitmap.createBitmap(num, 46*5+9, 0, numwidth, 40);
			eachNum[6] = Bitmap.createBitmap(num, 46*6+8, 0, numwidth, 40);
			eachNum[7] = Bitmap.createBitmap(num, 46*7+8, 0, numwidth, 40);
			eachNum[8] = Bitmap.createBitmap(num, 46*8+8, 0, numwidth, 40);
			eachNum[9] = Bitmap.createBitmap(num, 46*9+8, 0, numwidth, 40);
			
			
			this.setLayoutParams(new LayoutParams(level.getWidth()+num.getWidth(),level.getHeight()));
			//this.numStr="0";
			
		}
		public void onDraw(Canvas canvas){
			super.onDraw(canvas);
			Paint paint = new Paint();
			Bitmap newB =this.createBitmap(this.numStr);
			canvas.drawBitmap(newB, (this.getWidth()/2)-newB.getWidth()/2, 0, paint);
		}
		public Bitmap createBitmap(String num){
			int eachWidth = numwidth;
			int width = (num.length()+1)*eachWidth+level.getWidth();
			int height = 40;
			Bitmap b = Bitmap.createBitmap(width, height, Config.ARGB_4444);
			Canvas canvas = new Canvas(b);
			Paint paint = new Paint();
			canvas.drawBitmap(level, 0, 0, paint);
			for(int i = 0 ; i< num.length();i++){
				int nowNum = numStr.codePointAt(i);//当前数字
				Bitmap draw = this.eachNum[nowNum-48] ;
				canvas.drawBitmap(draw, level.getWidth()+(1+i)*eachWidth, 0, paint);
			}
			return b ;
		}
	}
	
	
	
	
	public class GameOver extends View{
		private Bitmap over ;
		private Bitmap num ;
		private Bitmap[] eachNum ;
		private String numStr="0";
		public String getNumStr() {
			return numStr;
		}
		public void setNumStr(String numStr) {
			this.numStr = numStr;
		}
		public GameOver(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
			over=((BitmapDrawable)this.getResources().getDrawable(R.drawable.gameover)).getBitmap();
			num=((BitmapDrawable)this.getResources().getDrawable(R.drawable.num_gameover)).getBitmap();
			eachNum = new Bitmap[10];
			int numwidth = 22;
			eachNum[0] = Bitmap.createBitmap(num, 3, 0, numwidth, 40);
			eachNum[1] = Bitmap.createBitmap(num, 44+5, 0, numwidth, 40);
			eachNum[2] = Bitmap.createBitmap(num, 44*2+10, 0, numwidth, 40);
			eachNum[3] = Bitmap.createBitmap(num, 44*3+10, 0, numwidth, 40);
			eachNum[4] = Bitmap.createBitmap(num, 44*4+12, 0, numwidth, 40);
			eachNum[5] = Bitmap.createBitmap(num, 44*5+15, 0, numwidth, 40);
			eachNum[6] = Bitmap.createBitmap(num, 44*6+17, 0, numwidth, 40);
			eachNum[7] = Bitmap.createBitmap(num, 44*7+20, 0, numwidth, 40);
			eachNum[8] = Bitmap.createBitmap(num, 44*8+20, 0, numwidth, 40);
			eachNum[9] = Bitmap.createBitmap(num, 44*9+22, 0, numwidth, 40);
			
			
			this.setLayoutParams(new LayoutParams(num.getWidth(),over.getHeight()+num.getHeight()));
			//this.numStr="0";
			
		}
		public void onDraw(Canvas canvas){
			super.onDraw(canvas);
			Paint paint = new Paint();
			canvas.drawBitmap(over, (this.getWidth()/2)-over.getWidth()/2, 0, paint);
			if(this.numStr!=null){
			Bitmap newNum =this.createBitmap(this.numStr);
			canvas.drawBitmap(newNum, (this.getWidth()/2)-newNum.getWidth()/2, over.getHeight(), paint);
			}
		}
		public Bitmap createBitmap(String num){
			int eachWidth = 25;
			int width = num.length()*eachWidth;
			int height = 40;
			Bitmap b = Bitmap.createBitmap(width, height, Config.ARGB_4444);
			Canvas canvas = new Canvas(b);
			Paint paint = new Paint();
			for(int i = 0 ; i< num.length();i++){
				int nowNum = numStr.codePointAt(i);//当前数字
				Bitmap draw = this.eachNum[nowNum-48] ;
				canvas.drawBitmap(draw, i*eachWidth, 0, paint);
			}
			return b ;
		}
	}
	
	public class TimeEnd extends Thread {
		int time ;
		Activity a;
		int resultCode ;
		public TimeEnd(Activity a,int time,int resultCode){
			this.time = time ;
			this.a = a;
			this.resultCode = resultCode;
		}
		public void run(){
			try {
				Thread.sleep(time);
				a.setResult(resultCode);
				a.finish();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	
	public class ShowBound extends View{
		private Bitmap bound ;
		private Bitmap num ;
		private Bitmap[] eachNum ;
		private String numStr="0";
		private final int numwidth = 22;
		public String getNumStr() {
			return numStr;
		}
		public void setNumStr(String numStr) {
			this.numStr = numStr;
		}
		public ShowBound(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
			bound=((BitmapDrawable)this.getResources().getDrawable(R.drawable.bonus)).getBitmap();
			num=((BitmapDrawable)this.getResources().getDrawable(R.drawable.num_gameover)).getBitmap();
			eachNum = new Bitmap[10];
			
			int numwidth = 22;
			eachNum[0] = Bitmap.createBitmap(num, 3, 0, numwidth, 40);
			eachNum[1] = Bitmap.createBitmap(num, 44+5, 0, numwidth, 40);
			eachNum[2] = Bitmap.createBitmap(num, 44*2+10, 0, numwidth, 40);
			eachNum[3] = Bitmap.createBitmap(num, 44*3+10, 0, numwidth, 40);
			eachNum[4] = Bitmap.createBitmap(num, 44*4+12, 0, numwidth, 40);
			eachNum[5] = Bitmap.createBitmap(num, 44*5+15, 0, numwidth, 40);
			eachNum[6] = Bitmap.createBitmap(num, 44*6+17, 0, numwidth, 40);
			eachNum[7] = Bitmap.createBitmap(num, 44*7+20, 0, numwidth, 40);
			eachNum[8] = Bitmap.createBitmap(num, 44*8+20, 0, numwidth, 40);
			eachNum[9] = Bitmap.createBitmap(num, 44*9+22, 0, numwidth, 40);
			
			
			this.setLayoutParams(new LayoutParams(bound.getWidth()+num.getWidth(),bound.getHeight()+num.getHeight()));
			//this.numStr="0";
			
		}
		public void onDraw(Canvas canvas){
			super.onDraw(canvas);
			Paint paint = new Paint();
			Bitmap newB =this.createBitmap(this.numStr);
			canvas.drawBitmap(newB, (this.getWidth()/2)-newB.getWidth()/2, 0, paint);
		}
		public Bitmap createBitmap(String num){
			int eachWidth = numwidth;
			int width = num.length()*eachWidth>bound.getWidth()?num.length()*eachWidth:bound.getWidth();
			int height = bound.getHeight()+this.num.getHeight();
			Bitmap b = Bitmap.createBitmap(width, height, Config.ARGB_4444);
			Canvas canvas = new Canvas(b);
			Paint paint = new Paint();
			canvas.drawBitmap(bound, 0, 0, paint);
			for(int i = 0 ; i< num.length();i++){
				int nowNum = numStr.codePointAt(i);//当前数字
				Bitmap draw = this.eachNum[nowNum-48] ;
				canvas.drawBitmap(draw, eachWidth+i*eachWidth, bound.getHeight(), paint);
			}
			return b ;
		}
	}
	
	public class Options extends View implements OnTouchListener{
		private Bitmap  onB ,offB;//开关图片
		public final static int on = 0,off = 1;//开，关
		private int state = on;//状态
		private final int lw = 410 ,lh = 307;
		private final int weight = 33,height = 30 ; 
		private Paint paint ;
		private Rect dst ;//目标位置
		private Rect backRect ;//关闭位置
		private Context context ;
		public Options(Context context) {
			super(context);
			this.context = context;
			// TODO Auto-generated constructor stub
			this.setBackgroundResource(R.drawable.option);
			Bitmap src = BitmapFactory.decodeResource(context.getResources(), R.drawable.onoff);
			onB = Bitmap.createBitmap(src, 0, 0, weight, height);
			offB = Bitmap.createBitmap(src,0,height,weight,height);
			src.recycle();
			src = null ;
			paint = new Paint();
			dst = new Rect(263,140,263+weight,140+height);
			backRect = new Rect(370,5,400,36);
			if(Music.playable==true){
				this.state = on ;
			}else{
				this.state = off;
			}

			this.setLayoutParams(new LayoutParams(lw, lh));
			this.setOnTouchListener(this);
		}
		@Override
		protected void onDraw(Canvas canvas){
			super.onDraw(canvas);
			Bitmap b = null;
			if(state==on){
				b = onB;
			}else if(state == off){
				b = offB;
			}
			if(b!=null)
			canvas.drawBitmap(b, dst.left, dst.top, paint);
		}
		@Override
		public boolean onTouch(View arg0, MotionEvent arg1) {
			// TODO Auto-generated method stub
			Log.d("point", arg1.getX()+":"+arg1.getY());
			int wucha = 10 ;//误差在5个像素点
			if(arg1.getX()>=dst.left-wucha&&arg1.getX()-wucha<=dst.right&&arg1.getY()>=dst.top-wucha&&arg1.getY()-wucha<=dst.bottom){
				//点击中了onoff
				this.state = (this.state == on)?off:on;
				this.invalidate();
				if(state==on){
					Music.playable = true;
					Music.playLong(context, R.raw.musicbg, true);
				}else{
					Music.playable = false;
					Music.stopLong();
				}
			}else if(arg1.getX()>=backRect.left&&arg1.getX()<=backRect.right&&arg1.getY()>=backRect.top&&arg1.getY()<=backRect.bottom){
				MessageActivity.this.finish();
			}
			return false;
		}
		
		
	}
	
	public class Help extends View implements OnTouchListener{
		private Rect backRect ;
		private final int lw = 410 ,lh = 307;
		public Help(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
			backRect = new Rect(370,5,400,36);
			this.setBackgroundResource(R.drawable.help);
			this.setLayoutParams(new LayoutParams(lw, lh));
			this.setOnTouchListener(this);
		}

		@Override
		public boolean onTouch(View arg0, MotionEvent arg1) {
			// TODO Auto-generated method stub
			if(arg1.getX()>backRect.left&&arg1.getX()<backRect.right&&arg1.getY()>backRect.top&&arg1.getY()<backRect.bottom){
				MessageActivity.this.finish();
			}
			return false;
		}
		
	}
	
	public class TopScore extends View implements OnTouchListener{
		private Rect backRect ;
		private final int lw = 410 ,lh = 306;
		private int[] scoreList;
		private Bitmap num ;
		private Bitmap[] eachNum ;
		private Bitmap[] scoreP ;
		private int padTop = 54 ,padLeft=33 ,padHang = 8,padHalfLeft= 222;
		private int numWidth = 22;
		private Bitmap topIcon ;//icon
		private final int iconWidth ;
		private final int icon_Num = 10;//用于定义Icon 跟数字间的距离
		private final int icon_NumHeight = 15 ;//icon相对于数字垂直位移
		public TopScore(Context context,int[] scoreList) {
			super(context);
			// TODO Auto-generated constructor stub
			backRect = new Rect(370,5,400,36);
			num=((BitmapDrawable)this.getResources().getDrawable(R.drawable.num_gameover)).getBitmap();
			topIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.top10_icon);
			iconWidth = topIcon.getWidth();
			eachNum = new Bitmap[10];
			int numwidth = numWidth;
			eachNum[0] = Bitmap.createBitmap(num, 3, 0, numwidth, 40);
			eachNum[1] = Bitmap.createBitmap(num, 44+5, 0, numwidth, 40);
			eachNum[2] = Bitmap.createBitmap(num, 44*2+10, 0, numwidth, 40);
			eachNum[3] = Bitmap.createBitmap(num, 44*3+10, 0, numwidth, 40);
			eachNum[4] = Bitmap.createBitmap(num, 44*4+12, 0, numwidth, 40);
			eachNum[5] = Bitmap.createBitmap(num, 44*5+15, 0, numwidth, 40);
			eachNum[6] = Bitmap.createBitmap(num, 44*6+17, 0, numwidth, 40);
			eachNum[7] = Bitmap.createBitmap(num, 44*7+20, 0, numwidth, 40);
			eachNum[8] = Bitmap.createBitmap(num, 44*8+20, 0, numwidth, 40);
			eachNum[9] = Bitmap.createBitmap(num, 44*9+22, 0, numwidth, 40);
			
			this.scoreList = scoreList ;
			int num = 0;
			while(num < scoreList.length ){
				if(scoreList[num]>=0){
					num ++;
				}else{
					break;
				}
			}
			scoreP = new Bitmap[scoreList.length];
			for(int i = 0;i<this.scoreList.length&&i<num;i++){
				scoreP[i] = this.createBitmap(""+scoreList[i]);
			}
			
			this.setBackgroundResource(R.drawable.top10_bg);
			this.setLayoutParams(new LayoutParams(lw, lh));
			this.setOnTouchListener(this);
		}
		@Override
        protected void onDraw(Canvas canvas){
			Paint paint = new Paint();
        	for(int i = 0 ;i<5 ;i++){
        		canvas.drawBitmap(this.topIcon, this.padLeft, this.padTop+icon_NumHeight+i*(40+padHang), paint);
        		if(i<scoreP.length)
        			if(scoreP[i]!=null)
        		canvas.drawBitmap(scoreP[i], this.padLeft+iconWidth+icon_Num, this.padTop+i*(40+padHang), paint);
        	}
        	for(int i = 5 ;i<10 ;i++){
        		canvas.drawBitmap(this.topIcon, this.padHalfLeft, this.padTop+icon_NumHeight+(i-5)*(40+padHang), paint);
        		if(i<scoreP.length)
        			if(scoreP[i]!=null)
            		canvas.drawBitmap(scoreP[i], this.padHalfLeft+iconWidth+icon_Num, this.padTop+(i-5)*(40+padHang), paint);
        	}
        }
		@Override
		public boolean onTouch(View arg0, MotionEvent arg1) {
			// TODO Auto-generated method stub
			if(arg1.getX()>backRect.left&&arg1.getX()<backRect.right&&arg1.getY()>backRect.top&&arg1.getY()<backRect.bottom){
				MessageActivity.this.setResult(MessageActivity.showTop);
				MessageActivity.this.finish();
			}
			return false;
		}
		
		public void setScoreList(int[] scoreList) {
			this.scoreList = scoreList;
		}
		
		public Bitmap createBitmap(String num){
			int eachWidth = 25;
			int width = num.length()*eachWidth;
			int height = 40;
			Bitmap b = Bitmap.createBitmap(width, height, Config.ARGB_4444);
			Canvas canvas = new Canvas(b);
			Paint paint = new Paint();
			for(int i = 0 ; i< num.length();i++){
				int nowNum = num.codePointAt(i);//当前数字
				Bitmap draw = this.eachNum[nowNum-48] ;
				canvas.drawBitmap(draw, i*eachWidth, 0, paint);
			}
			return b ;
		}
	}
	
	
}

