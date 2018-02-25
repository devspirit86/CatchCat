package spirit.parttime.cat;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.List;

import org.xml.sax.InputSource;
import org.xmlpull.v1.XmlPullParserException;

import spirit.parttime.cat.StopListFromXML.ItemXY;

import android.app.Activity;
import android.content.res.XmlResourceParser;
import android.os.Bundle;

import android.widget.TextView;

public class TestSAX extends Activity {
	private TextView showText ;
	private List<ItemXY> list ;
	private StopListFromXML lfx;
	 public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	   //     setContentView(R.layout.text);
	//        this.showText = (TextView)this.findViewById(R.id.showText);
	    try {
	    	lfx  = new StopListFromXML(this, 2, R.xml.gameinit);
			list = lfx.getStopList(1);
			String text = "";
			for(int i = 0; i<list.size();i++){
				ItemXY item = list.get(i);
				text += "("+item.x+","+item.y+")\n";
			}
			showText.setText(text);
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 }

}
