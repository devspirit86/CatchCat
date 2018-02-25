package spirit.parttime.cat;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.widget.ImageButton;
import spirit.parttime.cat.music.Music;

public class Menu extends Activity implements OnTouchListener{
	protected ImageButton resume ;
	protected ImageButton play ;
	protected ImageButton exit ;
	protected ImageButton options ;
	protected ImageButton help ;
	public static final int reStart = 0;
	public static final int newGame = 1;
	public static final int meunCode = 2;
	protected SharedPreferences myP ;
	protected Bitmap[] unpressB ;
	protected Bitmap[] pressB ;
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
	    this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		this.setContentView(R.layout.menu);
		//button++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		play = (ImageButton)this.findViewById(R.id.play);
		resume = (ImageButton)this.findViewById(R.id.resume);
		exit = (ImageButton)this.findViewById(R.id.exit);
		options = (ImageButton)this.findViewById(R.id.options);
		help = (ImageButton)this.findViewById(R.id.help);
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
		play.setBackgroundDrawable(new BitmapDrawable(unpressB[0]));
		play.setFocusableInTouchMode(true);
		resume.setBackgroundDrawable(new BitmapDrawable(unpressB[1]));
		resume.setFocusableInTouchMode(true);
		options.setBackgroundDrawable(new BitmapDrawable(unpressB[2]));
		options.setFocusableInTouchMode(true);
		help.setBackgroundDrawable(new BitmapDrawable(unpressB[3]));
		help.setFocusableInTouchMode(true);
		exit.setBackgroundDrawable(new BitmapDrawable(unpressB[4]));
		exit.setFocusableInTouchMode(true);
		myP = this.getSharedPreferences("LastGame",this.MODE_PRIVATE);
		
	}
	@Override
	public void onResume(){
		//���ű�������
		Music.playLong(this, R.raw.musicbg,true);
		super.onResume();
	}
	public void onPause(){
		//�رձ�������
		Music.stopLong();
		super.onPause();
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
            Intent data) {
		if(requestCode == this.meunCode){
			if(resultCode == GameMenu.exitB){
				//�˳���Ϸ
				this.finish();
			}
			
			if(resultCode == GameMenu.playB){
				//������Ϸ
				Intent intent = new Intent(this,Game.class);
				//ϵͳĬ��״̬����++++++++++++++++++++++++++++++++++++++++++++++++
				int[] config =this.getResources().getIntArray(R.array.config);
				intent.putExtra("totaltime", config[0]);//һ�ֵ���ʱ��
				intent.putExtra("parttime", config[1]);//һ������˼��
				int hscore = config[2] ;//Ĭ����߷���
				if(myP!=null){//��¼�е���߷���
					if(hscore<myP.getInt("h-score", config[2]))
					hscore = myP.getInt("h-score", config[2]);
				}
				intent.putExtra("h-score", hscore);//��ȡ��߷���
				intent.putExtra("linshiscore", config[3]);//ÿһ�صĳ���
				// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
				intent.putExtra("gamestate",Menu.newGame);	
				intent.putExtra("lives", config[4]);//������
				this.startActivityForResult(intent, meunCode);
			}
		}
	}
	/*@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Music.playShort(this, R.raw.menubtn, false);
		Intent intent = new Intent(this,Game.class);
		//ϵͳĬ��״̬����++++++++++++++++++++++++++++++++++++++++++++++++
		int[] config =this.getResources().getIntArray(R.array.config);
		intent.putExtra("totaltime", config[0]);//һ�ֵ���ʱ��
		intent.putExtra("parttime", config[1]);//һ������˼��
		int hscore = config[2] ;//Ĭ����߷���
		if(myP!=null){//��¼�е���߷���
			if(hscore<myP.getInt("h-score", config[2]))
			hscore = myP.getInt("h-score", config[2]);
		}
		intent.putExtra("h-score", hscore);//��ȡ��߷���
		Log.d("intent", hscore+" send");
		intent.putExtra("linshiscore", config[3]);//ÿһ�صĳ���
		// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		switch(v.getId()){
		case R.id.resume:{
			intent.putExtra("gamestate",Menu.reStart);
			intent.putExtra("stop",myP.getString("stop", null));//�ϰ�
			intent.putExtra("level",myP.getInt("level", -1));//�ȼ� 
			intent.putExtra("score",myP.getInt("score", -1));//���� 
			intent.putExtra("lives",myP.getInt("lives", -1));//������ 
			Log.d("intent", "send lives"+myP.getInt("lives", -1));
			intent.putExtra("nowTurn",myP.getInt("nowTurn", Game.User));//��ǰ��˭
			intent.putExtra("stepUsed",myP.getInt("timeUsed", -1));//ʹ�ö��ٲ� 
			intent.putExtra("nowtime",myP.getInt("nowtime", -1));//��ʱ�� 
			//=====״̬===============================
			this.startActivityForResult(intent, meunCode);
		}break;
		case R.id.play:{
			intent.putExtra("gamestate",Menu.newGame);	
			intent.putExtra("lives", config[4]);//������
			this.startActivityForResult(intent, meunCode);
		}break;
		case R.id.exit:{
			this.finish();
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
	}
*/
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
			Music.playShort(this, R.raw.menubtn, false);
			v.setBackgroundDrawable(new BitmapDrawable(this.pressB[pnum]));
			v.invalidate();
			return true ;
			
		}else if(arg1.getAction()==MotionEvent.ACTION_UP){
			v.setBackgroundDrawable(new BitmapDrawable(this.unpressB[pnum]));
			v.invalidate();
			//��ָ�ſ��󣬴����¼�
			Intent intent = new Intent(this,Game.class);
			//ϵͳĬ��״̬����++++++++++++++++++++++++++++++++++++++++++++++++
			int[] config =this.getResources().getIntArray(R.array.config);
			intent.putExtra("totaltime", config[0]);//һ�ֵ���ʱ��
			intent.putExtra("parttime", config[1]);//һ������˼��
			int hscore = config[2] ;//Ĭ����߷���
			if(myP!=null){//��¼�е���߷���
				if(hscore<myP.getInt("h-score", config[2]))
				hscore = myP.getInt("h-score", config[2]);
			}
			intent.putExtra("h-score", hscore);//��ȡ��߷���
			Log.d("intent", hscore+" send");
			intent.putExtra("linshiscore", config[3]);//ÿһ�صĳ���
			// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
			switch(v.getId()){
			case R.id.resume:{
				intent.putExtra("gamestate",Menu.reStart);
				intent.putExtra("stop",myP.getString("stop", null));//�ϰ�
				intent.putExtra("level",myP.getInt("level", -1));//�ȼ� 
				intent.putExtra("score",myP.getInt("score", -1));//���� 
				intent.putExtra("lives",myP.getInt("lives", -1));//������ 
				Log.d("intent", "send lives"+myP.getInt("lives", -1));
				intent.putExtra("nowTurn",myP.getInt("nowTurn", Game.User));//��ǰ��˭
				intent.putExtra("stepUsed",myP.getInt("timeUsed", -1));//ʹ�ö��ٲ� 
				intent.putExtra("nowtime",myP.getInt("nowtime", -1));//��ʱ�� 
				//=====״̬===============================
				this.startActivityForResult(intent, meunCode);
			}break;
			case R.id.play:{
				intent.putExtra("gamestate",Menu.newGame);	
				intent.putExtra("lives", config[4]);//������
				this.startActivityForResult(intent, meunCode);
			}break;
			case R.id.exit:{
				this.finish();
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
