package spirit.parttime.cat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.content.res.XmlResourceParser;
import android.util.Log;

public class StopListFromXML {
	public class ItemXY {
		public int x;
		public int y;
		ItemXY(int x ,int y){
			this.x = x;
			this.y = y;
		}
	}
	
	private int level ;//当前等级
	private List<ItemXY> stopList ;
	private XmlResourceParser parser ;
	private final int fileId ;
	private final Context context ;
	public StopListFromXML(Context context,int level,int fileId) throws XmlPullParserException, IOException {
		// TODO Auto-generated constructor stub
		this.level = level ;
		this.stopList = new ArrayList();
		this.fileId = fileId;
		this.context = context ;
		//this.parser =context.getResources().getXml(fileId) ;
		this.initStopList();
	}
	public List<ItemXY> getStopList(int level2) throws XmlPullParserException, IOException{
		if(this.level == level2)
			return this.stopList;
		else{
			this.level = level2;
			this.initStopList();
			return this.stopList;
		}
		
	}
	public void initStopList() throws XmlPullParserException, IOException{
		if(this.stopList!=null){
			stopList.clear();
		}else{
			stopList = new ArrayList();
		}
		this.parser = context.getResources().getXml(this.fileId);
		while(parser.getEventType()!=XmlResourceParser.END_DOCUMENT){
			Log.d("now", "in xml");
			if(parser.getEventType()==XmlResourceParser.START_TAG){
				Log.d("now", "in xml tag:"+parser.toString());
				if(parser.getName().equals("stopItem")){
					Log.d("now", "in StopItem");
					//进入stop
					int level = parser.getAttributeIntValue(null,"level",-1);
					if(level==this.level){
						int x = parser.getAttributeIntValue(null,"stop_x",-1);
						int y = parser.getAttributeIntValue(null,"stop_y",-1);
						this.stopList.add(new ItemXY(x,y));
						Log.d("now", "add");
					}else if(level< this.level) {
						parser.next();
					}else if(level>this.level){
						break ;
					}	
				}
			}
			parser.next();
		}
		Log.d("now", "leave");
		parser.close();
	}
}
