package spirit.parttime.cat;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class ShowLogo extends Activity {
	private TextView tv ;
	private AnimationDrawable ad ;
	private Thread checkEnd ;
	private Intent intent ;
	@Override
	public void onCreate(Bundle savedInstanceState){
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
	    this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.logo);
		intent = new Intent(this,Menu.class);
		checkEnd = new CheckEnd(this);
		tv = (TextView) this.findViewById(R.id.showLogo);
		ad = (AnimationDrawable)tv.getBackground();
		checkEnd.start();
	}
	class CheckEnd extends Thread{
		Activity activity;
		CheckEnd(Activity a){
			activity = a;
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				sleep(2000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			int dSum = 0;
			for(int i = 0;i<10;i++){
				dSum+=ad.getDuration(i);
			}
			ad.start();
			while(ad.isRunning()){
				Log.d("animation", "running");
		         try {
					this.sleep(dSum);
					ad.stop();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			Log.d("animation", "stop");
			activity.startActivity(intent);
			activity.finish();
		}
	   
		
	}
}
