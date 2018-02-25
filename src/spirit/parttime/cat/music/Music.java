package spirit.parttime.cat.music;

import android.content.Context;
import android.media.MediaPlayer;

public class Music {
	private static MediaPlayer longbg ,shortPlay;
	public static boolean playable = true ; 
	public static void playLong(Context context ,int resid,boolean loop){
		if(playable){
			stopLong();
			longbg = MediaPlayer.create(context, resid);
			longbg.setLooping(loop);
			longbg.start();
		}
	}
	public static void stopLong(){
		if(longbg!=null){
			longbg.stop();
			longbg.release();
			longbg = null ;
		}
	}
	public static void playShort(Context context ,int resid,boolean loop){
		if(playable){
			stopShort();
			shortPlay = MediaPlayer.create(context, resid);
			shortPlay.setLooping(loop);
			shortPlay.start();
		}
	}
	
	public static void stopShort(){
		if(shortPlay!=null){
			shortPlay.stop();
			shortPlay.release();
			shortPlay = null ;
		}
	}
	
}
