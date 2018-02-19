package spirit.parttime.cat;

import java.io.IOException;

import spirit.parttime.cat.music.Music;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.LinearLayout;


public class GameMenu extends Activity implements OnTouchListener{
//	protected ImageButton resume ;
//	protected ImageButton play ;
//	protected ImageButton exit ;
//	protected ImageButton options ;
//	protected ImageButton help ;
	protected Button resume ;
	protected Button play ;
	protected Button exit ;
	protected Button options ;
	protected Button help ;
	public static final int resumeB = 0,playB=1,exitB=2,optionsB=3,helpB=4;
	protected Bitmap[] unpressB ;
	protected Bitmap[] pressB ;
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.gamemenu);
		LinearLayout layout = (LinearLayout)this.findViewById(R.id.layout);
		WindowManager.LayoutParams lap = this.getWindow().getAttributes();
		lap.dimAmount = 0.3f ;
		lap.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND ;
		this.getWindow().setAttributes(lap);
		this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
		
		//button++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		play = (Button) this.findViewById(R.id.play);
		resume = (Button)this.findViewById(R.id.resume);
		exit = (Button)this.findViewById(R.id.exit);
		options = (Button)this.findViewById(R.id.options);
		help = (Button)this.findViewById(R.id.help);
		/*play.setOnClickListener(this);
		resume.setOnClickListener(this);
		exit.setOnClickListener(this);
		options.setOnClickListener(this);
		help.setOnClickListener(this);*/
		
		play.setOnTouchListener(this);
		resume.setOnTouchListener(this);
		exit.setOnTouchListener(this);
		options.setOnTouchListener(this);
		help.setOnTouchListener(this);
		//button view++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		Bitmap unbitmap = ((BitmapDrawable)this.getResources().getDrawable(R.drawable.btn_blue)).getBitmap();
		Bitmap pbitmap = ((BitmapDrawable)this.getResources().getDrawable(R.drawable.btn_orange)).getBitmap();
		unpressB = new Bitmap[5];
		pressB = new Bitmap[5];
		for(int i=0;i<5;i++){
			unpressB[i] = Bitmap.createBitmap(unbitmap, 0, i*65, 203, 65);
			pressB[i] = Bitmap.createBitmap(pbitmap, 0, i*65, 203, 65);
		}
//		play.setBackgroundDrawable(new BitmapDrawable(unpressB[0]));
		play.setFocusableInTouchMode(true);
//		resume.setBackgroundDrawable(new BitmapDrawable(unpressB[1]));
		resume.setFocusableInTouchMode(true);
//		options.setBackgroundDrawable(new BitmapDrawable(unpressB[2]));
		options.setFocusableInTouchMode(true);
//		help.setBackgroundDrawable(new BitmapDrawable(unpressB[3]));
		help.setFocusableInTouchMode(true);
//		exit.setBackgroundDrawable(new BitmapDrawable(unpressB[4]));
		exit.setFocusableInTouchMode(true);
	}

	public Bitmap getBitmap(){
		Bitmap b = Bitmap.createBitmap(400, 400,Config.ALPHA_8);
		Canvas canvas = new Canvas(b);
		canvas.drawColor(Color.argb(0, 0, 0, 0));
		return b ;
	}
	@Override
	public void onResume(){
		//播放背景音乐
		Music.playLong(this, R.raw.musicbg,true);
		super.onResume();
	}
	public void onPause(){
		//关闭背景音乐
		Music.stopLong();
		super.onPause();
	}
	/*@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Music.playShort(this, R.raw.menubtn, false);
		switch(v.getId()){
		case R.id.resume:{
			this.setResult(resumeB);
			finish();//返回
		}break;
		case R.id.play:{
			this.setResult(playB);
			finish();//返回
		}break;
		case R.id.exit:{
			this.setResult(exitB);
			finish();//返回;
		}break;
		case R.id.options:{
			Intent i = new Intent(this,MessageActivity.class);
			i.putExtra("showState", MessageActivity.showOption);
			this.startActivity(i);
		}break;
		case R.id.help:{
			Intent i = new Intent(this,MessageActivity.class);
			i.putExtra("showState", MessageActivity.showHelp);
			this.startActivity(i);
		}break;
		}
	}*/
	@Override
	public boolean onTouch(View v, MotionEvent arg1) {
		// TODO Auto-generated method stub
		int pnum = 0;
		switch(v.getId()){
			case R.id.play: pnum = 0 ;break;
			case R.id.resume: pnum = 1 ;break;
			case R.id.options: pnum = 2 ;break;
			case R.id.help: pnum = 3 ;break;
			case R.id.exit: pnum = 4 ;break;
		}
		if(arg1.getAction()==MotionEvent.ACTION_DOWN){
			v.setBackgroundDrawable(new BitmapDrawable(this.pressB[pnum]));
			Music.playShort(this, R.raw.menubtn, false);
			return true ;
			
		}else if(arg1.getAction()==MotionEvent.ACTION_UP){
			v.setBackgroundDrawable(new BitmapDrawable(this.unpressB[pnum]));
			//点击结束后触发事件
			switch(v.getId()){
			case R.id.resume:{
				this.setResult(resumeB);
				finish();//返回
			}break;
			case R.id.play:{
				this.setResult(playB);
				finish();//返回
			}break;
			case R.id.exit:{
				this.setResult(exitB);
				finish();//返回;
			}break;
			case R.id.options:{
				Intent i = new Intent(this,MessageActivity.class);
				i.putExtra("showState", MessageActivity.showOption);
				this.startActivity(i);
			}break;
			case R.id.help:{
				Intent i = new Intent(this,MessageActivity.class);
				i.putExtra("showState", MessageActivity.showHelp);
				this.startActivity(i);
			}break;
			}
			return false ;
		}
		return super.onTouchEvent(arg1);
	}
}
