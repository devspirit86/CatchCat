
package spirit.parttime.cat;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import spirit.parttime.cat.music.Music;

public class MyMapView extends View {
    //Image
	private Context context ;
	//=============相对位移
	private final int weiX = 15,weiY = 10;
	//==============圈=================================
	private Bitmap item = null;//圆圈
	private int item_id = -1;//图片Id
	private Rect item_rect = null ;//圆圈大小
	//==============阻碍物==============================
	private Bitmap orStopImage = null;//原阻碍物
	private Bitmap newStopImage = null;//新障碍物
	private int stopImage_id = -1;//阻碍物Id
	private Rect stop_rect = null ;
	private ItemXY cat_XY = null;
	//==============猫=================================
	private Bitmap cat_side = null;//左猫
	private int cat_sideid = -1;//左猫Id
	private Bitmap cat_back = null;//左上猫
	private int cat_backid = -1;//左上猫Id
	private Bitmap cat_front = null;//左下猫
	private int cat_frontid = -1;//左下猫Id
	
	private Bitmap cat_side_r = null;//左猫
	private int cat_siderid = -1;//左猫Id
	private Bitmap cat_back_r = null;//左上猫
	private int cat_backrid = -1;//左上猫Id
	private Bitmap cat_front_r = null;//左下猫
	private int cat_frontrid = -1;//左下猫Id
	
	public int cat_nowArrow = left ;//当前猫的方向
	public int cat_nowNum = 5 ;//当前猫的图片
	private Rect cat_rect = null;//猫的大小
	private Rect catrectInPicture = null;//猫在图中的
	private static final int catRun = 0x111;
	private Rect catPlaceRect = null ;//猫所处的位置 
	private Bitmap catNowPicture = null ;//当前所选择的图片 
	private Bitmap[] catTail = null ;//猫的尾巴图片
	
	//private Bitmap[] tailCat = null ;//猫的尾巴
	//=============cat动画动作
	private final int catStand = 5;//猫站的
	//=============从left开始顺时针
	public static final int left = 1 ;
	public static final int leftTop = 2 ;
	public static final int rightTop = 3 ;
	public static final int right = 4 ;
	public static final int rightDown = 5 ;
	public static final int leftDown = 6 ;
	
	
	//==============数据容器=============================
	private Rect[][] itemPlaceSet = null;//item所在位置
    public int[][] placeState = null;//位置状态
    public final int rowSum = 11;//总item行数
    public final int columnSum = 11;//总item列数
	public final int pass = 0;//可通过
    public final int orStop = 1;//原阻碍物
    public final int newStop = 2;//新阻碍物
    public final int catPlace = 3;//猫的座位
    
	//inner class
    public class ItemXY {
    	public int x,y ;
    	ItemXY(int x,int y){
    		this.x= x;//行
    		this.y= y;//列
    	}
    }
    
    class CatRunAnimation extends Thread {
    	ItemXY newPlace ;
    	CatRunAnimation(ItemXY newPlace){
    		super();
    		this.newPlace = newPlace;
    	}
    	public void run() {
    		// TODO Auto-generated method stub
    		    //判断方向
    			switch(MyMapView.this.cat_nowArrow){
    			case left:{
    				for(int i = 4;i>=0;i--){
    					MyMapView.this.cat_nowNum = i ;
    					MyMapView.this.postInvalidate();
	        			try {
	        				this.sleep(150);//沉睡
	        			} catch (InterruptedException e) {
	        				// TODO Auto-generated catch block
	        				e.printStackTrace();
	        			}
        			}
    			}break;
    			case leftTop:{
   
    				for(int i = 4;i>=0;i--){
    					MyMapView.this.cat_nowNum = i ;
    					MyMapView.this.postInvalidate();
	        			try {
	        				this.sleep(150);//沉睡
	        			} catch (InterruptedException e) {
	        				// TODO Auto-generated catch block
	        				e.printStackTrace();
	        			}
        			}
    			}break;
    			case rightTop:{
    				for(int i = 4;i>=0;i--){
    					MyMapView.this.cat_nowNum = i ;
    					MyMapView.this.postInvalidate();
	        			try {
	        				this.sleep(150);//沉睡
	        			} catch (InterruptedException e) {
	        				// TODO Auto-generated catch block
	        				e.printStackTrace();
	        			}
        			}
    				
    			}break;
    			case right:{
    				for(int i = 4;i>=0;i--){
    					MyMapView.this.cat_nowNum = i ;
    					MyMapView.this.postInvalidate();
	        			try {
	        				this.sleep(150);//沉睡
	        			} catch (InterruptedException e) {
	        				// TODO Auto-generated catch block
	        				e.printStackTrace();
	        			}
        			}
    		
    			}break;
    			case rightDown:{
    				for(int i = 4;i>=0;i--){
    					MyMapView.this.cat_nowNum = i ;
    					MyMapView.this.postInvalidate();
	        			try {
	        				this.sleep(150);//沉睡
	        			} catch (InterruptedException e) {
	        				// TODO Auto-generated catch block
	        				e.printStackTrace();
	        			}
        			}
    				
    			}break;
    			case leftDown:{
    				for(int i = 4;i>=0;i--){
    					MyMapView.this.cat_nowNum = i ;
    					MyMapView.this.postInvalidate();
	        			try {
	        				this.sleep(150);//沉睡
	        			} catch (InterruptedException e) {
	        				// TODO Auto-generated catch block
	        				e.printStackTrace();
	        			}
        			}
    				
    							
    			}break;
    			}
    			
    			MyMapView.this.changeCatPlace(newPlace);
    			if(Game.gameState==Game.running){
    				Game.nowTurn = Game.User;
    				Game.undoAble = true;//可以回退了
    				Game.game.secondView.reStart();
    			}
    		}
    	}

	protected void onDraw(Canvas canvas){
		Paint paint = new Paint();
		//===========================背景================================================== 
		for(int i = 0 ;i <this.rowSum;i++){
			for(int j = 0 ;j <this.columnSum;j++){
				switch(this.placeState[i][j]){
					case orStop :{//原阻碍
						Rect dst = new Rect(this.itemPlaceSet[i][j].left,this.itemPlaceSet[i][j].bottom-this.stop_rect.height(),this.itemPlaceSet[i][j].left+this.stop_rect.width(),this.itemPlaceSet[i][j].bottom) ;
						canvas.drawBitmap(this.orStopImage, this.stop_rect,dst, paint);
						}break;
					case newStop :{//新阻碍
						Rect dst = new Rect(this.itemPlaceSet[i][j].left,this.itemPlaceSet[i][j].bottom-this.stop_rect.height(),this.itemPlaceSet[i][j].left+this.stop_rect.width(),this.itemPlaceSet[i][j].bottom) ;
						canvas.drawBitmap(this.newStopImage, this.stop_rect, dst, paint);
						}break;
					default :{//画格子
						canvas.drawBitmap(this.item, this.item_rect, this.itemPlaceSet[i][j], paint);
						}
					}
				}
			//=========================Test================================================
			 //   this.cat_nowArrow = left;
			//	this.cat_nowNum = 4;
			//=========================画猫=================================================  
				int temp = this.cat_nowNum ;
				if(temp == catStand){//起始位置
					temp = 0;
				}
				//调整猫猫大小
				int width = 0 , height = 0;
				switch(this.cat_nowArrow){
				case left :  width = 81 ;height = 73 ;break;
				case right :  width = 81 ;height = 73 ;break;
				case leftTop :  width = 64 ;height = 95 ;break;
				case rightTop : width = 64 ;height = 95 ;break;
				case leftDown :  width = 64 ;height = 70 ;break;
				case rightDown :  width = 64 ;height = 70 ;break;
				}
				Rect src = new Rect(//将要画的相对于内部，按照固定平移
						temp*width,
						0,
						width+temp*width,
						height
						);
				//画猫粗位置
				Rect dst = new Rect(//猫的矩阵与所处位置按left top 进行对齐 
						catPlaceRect.left,
						catPlaceRect.top,
						catPlaceRect.left+width,
						catPlaceRect.top+height
						) ;
				Bitmap tailB = null ;
				Rect tailDst = null;//尾巴的目标位置
			switch(this.cat_nowArrow){
			//++++++++++++++++++++++++++++++++++++++++left+++++++++++++++++++++++++++++++++
			case left :{
				this.catNowPicture = this.cat_side ;
				switch(this.cat_nowNum){
				case 0:{
					
					int sx = 0,sy =0 ,dx = -60 , dy = -40 ;
					//图精细
					src.left += sx;
					src.right += sx;
					src.top  += sy ;
					src.bottom += sy ;
					//画精细
					dst.left += dx;
					dst.right += dx;
					dst.top  += dy ;
					dst.bottom += dy ;
					//尾巴：
					tailB = this.catTail[0];
					dx = -33 ;
					dy = -5 ;
					tailDst= new Rect(
							dst.right+dx,
							dst.bottom-tailB.getHeight()+dy,
							dst.right+tailB.getWidth()+dx,
							dst.bottom+dy			
					);
				}break ;
				case 1:{
					int sx = 0,sy =0 ,dx = -65 , dy = -40 ;
					//图精细
					src.left += sx;
					src.right += sx;
					src.top  += sy ;
					src.bottom += sy ;
					//画精细
					dst.left += dx;
					dst.right += dx;
					dst.top  += dy ;
					dst.bottom += dy ;
					//尾巴：
					tailB = this.catTail[4];
					dx = -18 ;
					dy = -25 ;
					tailDst= new Rect(
							dst.right+dx,
							dst.bottom-tailB.getHeight()+dy,
							dst.right+tailB.getWidth()+dx,
							dst.bottom+dy			
					);
				}break ;
				case 2:{
					
					int sx = 0,sy =0 ,dx = -55 , dy = -40  ;
					
					//图精细
					src.left += sx;
					src.right += sx;
					src.top  += sy ;
					src.bottom += sy ;
					//画精细
					dst.left += dx;
					dst.right += dx;
					dst.top  += dy ;
					dst.bottom += dy ;
					//尾巴：
					tailB = this.catTail[2];
					dx = -13 ;
					dy = -29 ;
					tailDst= new Rect(
							dst.right+dx,
							dst.bottom-tailB.getHeight()+dy,
							dst.right+tailB.getWidth()+dx,
							dst.bottom+dy			
					);
				}break ;
				case 3:{
					int sx = 1,sy =0 ,dx = -52 , dy = -45 ;
					//图精细
					src.left += sx;
					src.right += sx;
					src.top  += sy ;
					src.bottom += sy ;
					//画精细
					dst.left += dx;
					dst.right += dx;
					dst.top  += dy ;
					dst.bottom += dy ;
					//尾巴：
					tailB = this.catTail[3];
					dx = -13 ;
					dy = -27 ;
					tailDst= new Rect(
							dst.right+dx,
							dst.bottom-tailB.getHeight()+dy,
							dst.right+tailB.getWidth()+dx,
							dst.bottom+dy			
					);
				}break ;
				case 4:{
					int sx = 3,sy =0 ,dx = -42 , dy = -45 ;
					//图精细
					src.left += sx;
					src.right += sx;
					src.top  += sy ;
					src.bottom += sy ;
					//画精细
					dst.left += dx;
					dst.right += dx;
					dst.top  += dy ;
					dst.bottom += dy ;
					//尾巴：
					tailB = this.catTail[1];
					dx = -13 ;
					dy = -27 ;
					tailDst= new Rect(
							dst.right+dx,
							dst.bottom-tailB.getHeight()+dy,
							dst.right+tailB.getWidth()+dx,
							dst.bottom+dy			
					);
				}break ;
				case catStand:{//原始位置
					int sx = 0,sy =0 ,dx = -18 , dy = -40 ;
					//图精细
					src.left += sx;
					src.right += sx;
					src.top  += sy ;
					src.bottom += sy ;
					//画精细
					dst.left += dx;
					dst.right += dx;
					dst.top  += dy ;
					dst.bottom += dy ;
					//尾巴：
					tailB = this.catTail[0];
					dx = -33 ;
					dy = -5 ;
					tailDst= new Rect(
							dst.right+dx,
							dst.bottom-tailB.getHeight()+dy,
							dst.right+tailB.getWidth()+dx,
							dst.bottom+dy			
					);
				}break ;
				}
				
			}break;
			//++++++++++++++++++++++++++++++++++++++++lefttop+++++++++++++++++++++++++++++++++
			case leftTop :{
				this.catNowPicture = this.cat_back ;
				switch(this.cat_nowNum){
				case 0:{
					int sx = 0,sy =0 ,dx = -30 , dy = -102 ;
					//图精细
					src.left += sx;
					src.right += sx;
					src.top  += sy ;
					src.bottom += sy ;
					//画精细
					dst.left += dx;
					dst.right += dx;
					dst.top  += dy ;
					dst.bottom += dy ;
					//尾巴的位置
					tailB = this.catTail[10];
					dx = -33 ;
					dy = -10 ;
					tailDst= new Rect(
							dst.right+dx,
							dst.bottom-tailB.getHeight()+dy,
							dst.right+tailB.getWidth()+dx,
							dst.bottom+dy			
					);
				}break ;
				case 1:{
					int sx = 0,sy =0 ,dx = -35 , dy = -102 ;
					//图精细
					src.left += sx;
					src.right += sx;
					src.top  += sy ;
					src.bottom += sy ;
					//画精细
					dst.left += dx;
					dst.right += dx;
					dst.top  += dy ;
					dst.bottom += dy ;
					//尾巴的位置
					tailB = this.catTail[11];
					dx = -25 ;
					dy = -18 ;
					tailDst= new Rect(
							dst.right+dx,
							dst.bottom-tailB.getHeight()+dy,
							dst.right+tailB.getWidth()+dx,
							dst.bottom+dy			
					);
				}break ;
				case 2:{
					int sx = 0,sy =0 ,dx = -25 , dy = -85 ;
					//图精细
					src.left += sx;
					src.right += sx;
					src.top  += sy ;
					src.bottom += sy ;
					//画精细
					dst.left += dx;
					dst.right += dx;
					dst.top  += dy ;
					dst.bottom += dy ;
					//尾巴的位置
					tailB = this.catTail[12];
					dx = -25 ;
					dy = -20 ;
					tailDst= new Rect(
							dst.right+dx,
							dst.bottom-tailB.getHeight()+dy,
							dst.right+tailB.getWidth()+dx,
							dst.bottom+dy			
					);
				}break ;
				case 3:{
					int sx = 0,sy =0 ,dx = -25 , dy = -60 ;
					//图精细
					src.left += sx;
					src.right += sx;
					src.top  += sy ;
					src.bottom += sy ;
					//画精细
					dst.left += dx;
					dst.right += dx;
					dst.top  += dy ;
					dst.bottom += dy ;
					//尾巴的位置
					tailB = this.catTail[13];
					dx = -20 ;
					dy = -35 ;
					tailDst= new Rect(
							dst.right+dx,
							dst.bottom-tailB.getHeight()+dy,
							dst.right+tailB.getWidth()+dx,
							dst.bottom+dy			
					);
				}break ;
				case 4:{
					int sx = 0,sy =0 ,dx = -10 , dy = -60 ;
					//图精细
					src.left += sx;
					src.right += sx;
					src.top  += sy ;
					src.bottom += sy ;
					//画精细
					dst.left += dx;
					dst.right += dx;
					dst.top  += dy ;
					dst.bottom += dy ;
					//尾巴的位置
					tailB = this.catTail[14];
					dx = -28 ;
					dy = -17 ;
					tailDst= new Rect(
							dst.right+dx,
							dst.bottom-tailB.getHeight()+dy,
							dst.right+tailB.getWidth()+dx,
							dst.bottom+dy			
					);
				}break ;
				case catStand:{//原始位置
					int sx = 0,sy =0 ,dx = -10 , dy = -60 ;
					//图精细
					src.left += sx;
					src.right += sx;
					src.top  += sy ;
					src.bottom += sy ;
					//画精细
					dst.left += dx;
					dst.right += dx;
					dst.top  += dy ;
					dst.bottom += dy ;
					//尾巴的位置
					tailB = this.catTail[10];
					dx = -33 ;
					dy = -10 ;
					tailDst= new Rect(
							dst.right+dx,
							dst.bottom-tailB.getHeight()+dy,
							dst.right+tailB.getWidth()+dx,
							dst.bottom+dy			
					);
				}break ;
				}
				
			}break;
			//+++++++++++++++++++++++++++++++++++++++rightTop+++++++++++++++++++++++++++++++++
			case rightTop :{this.catNowPicture = this.cat_back_r ;
			switch(this.cat_nowNum){
			case 0:{
				int sx = 0,sy =0 ,dx = 6 , dy = -102 ;
				//图精细
				src.left += sx;
				src.right += sx;
				src.top  += sy ;
				src.bottom += sy ;
				//画精细
				dst.left += dx;
				dst.right += dx;
				dst.top  += dy ;
				dst.bottom += dy ;
				//尾巴的位置
				tailB = this.catTail[25];
				dx = 0 ;
				dy = -10 ;
				tailDst= new Rect(
						dst.left+dx,
						dst.bottom-tailB.getHeight()+dy,
						dst.left+tailB.getWidth()+dx,
						dst.bottom+dy			
				);
			}break ;
			case 1:{
				int sx = 0,sy =0 ,dx = 15 , dy = -102 ;
				//图精细
				src.left += sx;
				src.right += sx;
				src.top  += sy ;
				src.bottom += sy ;
				//画精细
				dst.left += dx;
				dst.right += dx;
				dst.top  += dy ;
				dst.bottom += dy ;
				//尾巴的位置
				tailB = this.catTail[29];
				dx = -15 ;
				dy = -18 ;
				tailDst= new Rect(
						dst.left+dx,
						dst.bottom-tailB.getHeight()+dy,
						dst.left+tailB.getWidth()+dx,
						dst.bottom+dy			
				);
			}break ;
			case 2:{
				int sx = 0,sy =0 ,dx = 10 , dy = -85 ;
				//图精细
				src.left += sx;
				src.right += sx;
				src.top  += sy ;
				src.bottom += sy ;
				//画精细
				dst.left += dx;
				dst.right += dx;
				dst.top  += dy ;
				dst.bottom += dy ;
				//尾巴的位置
				tailB = this.catTail[28];
				dx = -35 ;
				dy = -25 ;
				tailDst= new Rect(
						dst.left+dx,
						dst.bottom-tailB.getHeight()+dy,
						dst.left+tailB.getWidth()+dx,
						dst.bottom+dy			
				);
			}break ;
			case 3:{
				int sx = 0,sy =0 ,dx = 5 , dy = -60 ;
				//图精细
				src.left += sx;
				src.right += sx;
				src.top  += sy ;
				src.bottom += sy ;
				//画精细
				dst.left += dx;
				dst.right += dx;
				dst.top  += dy ;
				dst.bottom += dy ;
				//尾巴的位置
				tailB = this.catTail[27];
				dx = -25 ;
				dy = -38 ;
				tailDst= new Rect(
						dst.left+dx,
						dst.bottom-tailB.getHeight()+dy,
						dst.left+tailB.getWidth()+dx,
						dst.bottom+dy			
				);
			}break ;
			case 4:{
				int sx = 0,sy =0 ,dx = -10 , dy = -60 ;
				//图精细
				src.left += sx;
				src.right += sx;
				src.top  += sy ;
				src.bottom += sy ;
				//画精细
				dst.left += dx;
				dst.right += dx;
				dst.top  += dy ;
				dst.bottom += dy ;
				//尾巴的位置
				tailB = this.catTail[26];
				dx = -5 ;
				dy = -17 ;
				tailDst= new Rect(
						dst.left+dx,
						dst.bottom-tailB.getHeight()+dy,
						dst.left+tailB.getWidth()+dx,
						dst.bottom+dy			
				);
			}break ;
			case catStand:{//原始位置
				int sx = 0,sy =0 ,dx = -15 , dy = -60 ;
				//图精细
				src.left += sx;
				src.right += sx;
				src.top  += sy ;
				src.bottom += sy ;
				//画精细
				dst.left += dx;
				dst.right += dx;
				dst.top  += dy ;
				dst.bottom += dy ;
				//尾巴的位置
				tailB = this.catTail[25];
				dx = 0 ;
				dy = -10 ;
				tailDst= new Rect(
						dst.left+dx,
						dst.bottom-tailB.getHeight()+dy,
						dst.left+tailB.getWidth()+dx,
						dst.bottom+dy			
				);
				
			}break ;
			}
			
		}break;
			//++++++++++++++++++++++++++++++++++++++++right+++++++++++++++++++++++++++++++++
			case right :{
				this.catNowPicture = this.cat_side_r ;
					switch(this.cat_nowNum){
						case 0:{
							int sx = 0,sy =0 ,dx = 44 , dy = -40 ;
							//图精细
							src.left += sx;
							src.right += sx;
							src.top  += sy ;
							src.bottom += sy ;
							//画精细
							dst.left += dx;
							dst.right += dx;
							dst.top  += dy ;
							dst.bottom += dy ;
							//尾巴：
							tailB = this.catTail[15];
							dx = -98 ;
							dy = -5 ;
							tailDst= new Rect(
									dst.right+dx,
									dst.bottom-tailB.getHeight()+dy,
									dst.right+tailB.getWidth()+dx,
									dst.bottom+dy			
							);
						}break ;
						case 1:{
							int sx = 0,sy =0 ,dx = 24 , dy = -40 ;
							//图精细
							src.left += sx;
							src.right += sx;
							src.top  += sy ;
							src.bottom += sy ;
							//画精细
							dst.left += dx;
							dst.right += dx;
							dst.top  += dy ;
							dst.bottom += dy ;
							//尾巴：
							tailB = this.catTail[16];
							dx = -85 ;
							dy = -25 ;
							tailDst= new Rect(
									dst.right+dx,
									dst.bottom-tailB.getHeight()+dy,
									dst.right+tailB.getWidth()+dx,
									dst.bottom+dy			
							);
						}break ;
						case 2:{
							int sx = 0,sy =0 ,dx = 22 , dy = -40 ;
							//图精细
							src.left += sx;
							src.right += sx;
							src.top  += sy ;
							src.bottom += sy ;
							//画精细
							dst.left += dx;
							dst.right += dx;
							dst.top  += dy ;
							dst.bottom += dy ;
							//尾巴：
							tailB = this.catTail[17];
							dx = -98 ;
							dy = -29 ;
							tailDst= new Rect(
									dst.right+dx,
									dst.bottom-tailB.getHeight()+dy,
									dst.right+tailB.getWidth()+dx,
									dst.bottom+dy			
							);
						}break ;
						case 3:{
							int sx = 0,sy =0 ,dx = 9 , dy = -45 ;
							//图精细
							src.left += sx;
							src.right += sx;
							src.top  += sy ;
							src.bottom += sy ;
							//画精细
							dst.left += dx;
							dst.right += dx;
							dst.top  += dy ;
							dst.bottom += dy ;
							//尾巴：
							tailB = this.catTail[18];
							dx = -125 ;
							dy = -27 ;
							tailDst= new Rect(
									dst.right+dx,
									dst.bottom-tailB.getHeight()+dy,
									dst.right+tailB.getWidth()+dx,
									dst.bottom+dy			
							);
							
						}break ;
						case 4:{
							int sx = 0,sy =0 ,dx = 9 , dy = -45 ;
							//图精细
							src.left += sx;
							src.right += sx;
							src.top  += sy ;
							src.bottom += sy ;
							//画精细
							dst.left += dx;
							dst.right += dx;
							dst.top  += dy ;
							dst.bottom += dy ;
							//尾巴：
							tailB = this.catTail[19];
							dx = -98 ;
							dy = -27 ;
							tailDst= new Rect(
									dst.right+dx,
									dst.bottom-tailB.getHeight()+dy,
									dst.right+tailB.getWidth()+dx,
									dst.bottom+dy			
							);
							
						}break ;
						case catStand:{//原始位置
							int sx = 0,sy =0 ,dx =2 , dy = -40 ;
							//图精细
							src.left += sx;
							src.right += sx;
							src.top  += sy ;
							src.bottom += sy ;
							//画精细
							dst.left += dx;
							dst.right += dx;
							dst.top  += dy ;
							dst.bottom += dy ;
							//尾巴：
							tailB = this.catTail[15];
							dx = -98 ;
							dy = -5 ;
							tailDst= new Rect(
									dst.right+dx,
									dst.bottom-tailB.getHeight()+dy,
									dst.right+tailB.getWidth()+dx,
									dst.bottom+dy			
							);
						}break ;
					}
				}break;
				//++++++++++++++++++++++++++++++++++++++++rightdown+++++++++++++++++++++++++++++++++
			case rightDown :{this.catNowPicture = this.cat_front_r ;
			switch(this.cat_nowNum){
			case 0:{
				int sx = 0,sy =0 ,dx = 6 , dy = 7 ;
				//图精细
				src.left += sx;
				src.right += sx;
				src.top  += sy ;
				src.bottom += sy ;
				//画精细
				dst.left += dx;
				dst.right += dx;
				dst.top  += dy ;
				dst.bottom += dy ;
				//尾巴的位置
				tailB = this.catTail[20];
				dx = -60 ;
				dy = -10 ;
				tailDst= new Rect(
						dst.right+dx,
						dst.bottom-tailB.getHeight()+dy,
						dst.right+tailB.getWidth()+dx,
						dst.bottom+dy			
				);
			}break ;
			case 1:{
				int sx = 0,sy =0 ,dx = 15 , dy = 9 ;
				//图精细
				src.left += sx;
				src.right += sx;
				src.top  += sy ;
				src.bottom += sy ;
				//画精细
				dst.left += dx;
				dst.right += dx;
				dst.top  += dy ;
				dst.bottom += dy ;
				//尾巴的位置
				tailB = this.catTail[24];
				dx = -70 ;
				dy = -35 ;
				tailDst= new Rect(
						dst.right+dx,
						dst.bottom-tailB.getHeight()+dy,
						dst.right+tailB.getWidth()+dx,
						dst.bottom+dy			
				);
			}break ;
			case 2:{
				int sx = 0,sy =0 ,dx = 5 , dy = 9 ;
				//图精细
				src.left += sx;
				src.right += sx;
				src.top  += sy ;
				src.bottom += sy ;
				//画精细
				dst.left += dx;
				dst.right += dx;
				dst.top  += dy ;
				dst.bottom += dy ;
				//尾巴的位置
				tailB = this.catTail[23];
				dx = -53 ;
				dy = -60 ;
				tailDst= new Rect(
						dst.right+dx,
						dst.bottom-tailB.getHeight()+dy,
						dst.right+tailB.getWidth()+dx,
						dst.bottom+dy			
				);
			}break ;
			case 3:{
				int sx = 0,sy =0 ,dx = 0 , dy = -6 ;
				//图精细
				src.left += sx;
				src.right += sx;
				src.top  += sy ;
				src.bottom += sy ;
				//画精细
				dst.left += dx;
				dst.right += dx;
				dst.top  += dy ;
				dst.bottom += dy ;
				//尾巴的位置
				tailB = this.catTail[22];
				dx = -57 ;
				dy = -60 ;
				tailDst= new Rect(
						dst.right+dx,
						dst.bottom-tailB.getHeight()+dy,
						dst.right+tailB.getWidth()+dx,
						dst.bottom+dy			
				);
			}break ;
			case 4:{
				int sx = 0,sy =0 ,dx = -10 , dy = -35 ;
				//图精细
				src.left += sx;
				src.right += sx;
				src.top  += sy ;
				src.bottom += sy ;
				//画精细
				dst.left += dx;
				dst.right += dx;
				dst.top  += dy ;
				dst.bottom += dy ;
				//尾巴的位置
				tailB = this.catTail[21];
				dx = -63 ;
				dy = -45 ;
				tailDst= new Rect(
						dst.right+dx,
						dst.bottom-tailB.getHeight()+dy,
						dst.right+tailB.getWidth()+dx,
						dst.bottom+dy			
				);
			}break ;
			case catStand:{//原始位置
				int sx = 0,sy =0 ,dx = -15 , dy = -35 ;
				//图精细
				src.left += sx;
				src.right += sx;
				src.top  += sy ;
				src.bottom += sy ;
				//画精细
				dst.left += dx;
				dst.right += dx;
				dst.top  += dy ;
				dst.bottom += dy ;
				//尾巴的位置
				tailB = this.catTail[20];
				dx = -60 ;
				dy = -10 ;
				tailDst= new Rect(
						dst.right+dx,
						dst.bottom-tailB.getHeight()+dy,
						dst.right+tailB.getWidth()+dx,
						dst.bottom+dy			
				);
			}break ;
			}
			
		}break;
			//++++++++++++++++++++++++++++++++++++++++leftdown+++++++++++++++++++++++++++++++++
			case leftDown :{this.catNowPicture = this.cat_front ;
			switch(this.cat_nowNum){
			case 0:{
				int sx = 0,sy =0 ,dx = -31 , dy = 7 ;
				//图精细
				src.left += sx;
				src.right += sx;
				src.top  += sy ;
				src.bottom += sy ;
				//画精细
				dst.left += dx;
				dst.right += dx;
				dst.top  += dy ;
				dst.bottom += dy ;
				//尾巴的位置
				tailB = this.catTail[5];
				dx = -33 ;
				dy = -10 ;
				tailDst= new Rect(
						dst.right+dx,
						dst.bottom-tailB.getHeight()+dy,
						dst.right+tailB.getWidth()+dx,
						dst.bottom+dy			
				);
			}break ;
			case 1:{
				int sx = 0,sy =0 ,dx = -35 , dy = 9 ;
				//图精细
				src.left += sx;
				src.right += sx;
				src.top  += sy ;
				src.bottom += sy ;
				//画精细
				dst.left += dx;
				dst.right += dx;
				dst.top  += dy ;
				dst.bottom += dy ;
				//尾巴的位置
				tailB = this.catTail[9];
				dx = -23 ;
				dy = -30 ;
				tailDst= new Rect(
						dst.right+dx,
						dst.bottom-tailB.getHeight()+dy,
						dst.right+tailB.getWidth()+dx,
						dst.bottom+dy			
				);
			}break ;
			case 2:{
				int sx = 1,sy =0 ,dx = -28 , dy = 9 ;
				//图精细
				src.left += sx;
				src.right += sx;
				src.top  += sy ;
				src.bottom += sy ;
				//画精细
				dst.left += dx;
				dst.right += dx;
				dst.top  += dy ;
				dst.bottom += dy ;
				//尾巴的位置
				tailB = this.catTail[8];
				dx = -33 ;
				dy = -60 ;
				tailDst= new Rect(
						dst.right+dx,
						dst.bottom-tailB.getHeight()+dy,
						dst.right+tailB.getWidth()+dx,
						dst.bottom+dy			
				);
			}break ;
			case 3:{
				int sx = 0,sy =0 ,dx = -31 , dy = -6 ;
				//图精细
				src.left += sx;
				src.right += sx;
				src.top  += sy ;
				src.bottom += sy ;
				//画精细
				dst.left += dx;
				dst.right += dx;
				dst.top  += dy ;
				dst.bottom += dy ;
				//尾巴的位置
				tailB = this.catTail[7];
				dx = -37 ;
				dy = -60 ;
				tailDst= new Rect(
						dst.right+dx,
						dst.bottom-tailB.getHeight()+dy,
						dst.right+tailB.getWidth()+dx,
						dst.bottom+dy			
				);
			}break ;
			case 4:{
				int sx = 0,sy =0 ,dx = -10 , dy = -35 ;
				//图精细
				src.left += sx;
				src.right += sx;
				src.top  += sy ;
				src.bottom += sy ;
				//画精细
				dst.left += dx;
				dst.right += dx;
				dst.top  += dy ;
				dst.bottom += dy ;
				//尾巴的位置
				tailB = this.catTail[6];
				dx = -23 ;
				dy = -40 ;
				tailDst= new Rect(
						dst.right+dx,
						dst.bottom-tailB.getHeight()+dy,
						dst.right+tailB.getWidth()+dx,
						dst.bottom+dy			
				);
			}break ;
			case catStand:{//原始位置
				int sx = 0,sy =0 ,dx = -10 , dy = -35 ;
				//图精细
				src.left += sx;
				src.right += sx;
				src.top  += sy ;
				src.bottom += sy ;
				//画精细
				dst.left += dx;
				dst.right += dx;
				dst.top  += dy ;
				dst.bottom += dy ;
				//尾巴的位置
				tailB = this.catTail[5];
				dx = -33 ;
				dy = -10 ;
				tailDst= new Rect(
						dst.right+dx,
						dst.bottom-tailB.getHeight()+dy,
						dst.right+tailB.getWidth()+dx,
						dst.bottom+dy			
				);
			}break ;
			}
			
		}break;
			}   
			if(tailB!=null&&tailDst!=null)
			canvas.drawBitmap(tailB, tailDst.left, tailDst.top, paint);
			canvas.drawBitmap(catNowPicture, src, dst, paint);
			
		}
		
		
	}
    //
	public MyMapView(Context arg0, AttributeSet attrs) {
		this(arg0,attrs,0);	
		// TODO Auto-generated constructor stub
	}

	public MyMapView(Context arg0, AttributeSet attrs, int arg2) {
		super(arg0,attrs,arg2);
		this.context = arg0;
		this.initMapView(attrs);	
	}
	//初始化视图
	 void initMapView(AttributeSet attrs){
		//初始化image==================================================================
		//================初始化item
		this.item_id = attrs.getAttributeResourceValue(null, "item_id", -1);
		int item_X = 0, item_Y = 0,item_width=0 ,item_height=0;
		item_X = attrs.getAttributeIntValue(null, "item_X", 0);
		item_Y = attrs.getAttributeIntValue(null, "item_Y", 0);
		item_width = attrs.getAttributeIntValue(null, "item_width", 0);
		item_height = attrs.getAttributeIntValue(null, "item_height", 0);
		this.item_rect = new Rect(item_X,item_Y,item_X+item_width,item_Y+item_height);
		this.item=Bitmap.createBitmap(((BitmapDrawable)this.getResources().getDrawable(item_id)).getBitmap(), this.item_rect.left, this.item_rect.top, this.item_rect.width(), this.item_rect.height());	
		//================初始化stop
		this.stopImage_id = attrs.getAttributeResourceValue(null, "stopImage_id", -1);
		int stop_X = 0, stop_Y = 0,stop_width=0 ,stop_height=0;
		stop_X = attrs.getAttributeIntValue(null, "stop_X", 0);
		stop_Y = attrs.getAttributeIntValue(null, "stop_Y", 0);
		stop_width = attrs.getAttributeIntValue(null, "stop_width", 0);
		stop_height = attrs.getAttributeIntValue(null, "stop_height", 0);
		this.stop_rect = new Rect(stop_X,stop_Y,stop_X+stop_width,stop_Y+stop_height);
		this.newStopImage=Bitmap.createBitmap(((BitmapDrawable)this.getResources().getDrawable(stopImage_id)).getBitmap(), this.stop_rect.left, this.stop_rect.top, this.stop_rect.width(), this.stop_rect.height());
		this.orStopImage=Bitmap.createBitmap(((BitmapDrawable)this.getResources().getDrawable(stopImage_id)).getBitmap(), this.stop_rect.left+this.stop_rect.width(), this.stop_rect.top, this.stop_rect.width(), this.stop_rect.height());
		//================初始化Cat
		int cat_X = 0, cat_Y = 0,cat_width=0 ,cat_height=0;
		cat_X = attrs.getAttributeIntValue(null, "cat_X", 0);
		cat_Y = attrs.getAttributeIntValue(null, "cat_Y", 0);
		cat_width = attrs.getAttributeIntValue(null, "cat_width", 0);
		cat_height = attrs.getAttributeIntValue(null, "cat_height", 0);
		this.cat_rect = new Rect(cat_X,cat_Y,cat_X+cat_width,cat_Y+cat_height);
		this.cat_sideid = attrs.getAttributeResourceValue(null, "cat_sideid", -1);
		this.cat_side=((BitmapDrawable)this.getResources().getDrawable(cat_sideid)).getBitmap();
		this.cat_backid = attrs.getAttributeResourceValue(null, "cat_backid", -1);
		this.cat_back=((BitmapDrawable)this.getResources().getDrawable(cat_backid)).getBitmap();
		this.cat_frontid = attrs.getAttributeResourceValue(null, "cat_frontid", -1);
		this.cat_front=((BitmapDrawable)this.getResources().getDrawable(cat_frontid)).getBitmap();
	
		this.cat_siderid = attrs.getAttributeResourceValue(null, "cat_siderid", -1);
		this.cat_side_r=((BitmapDrawable)this.getResources().getDrawable(cat_siderid)).getBitmap();
		this.cat_backrid = attrs.getAttributeResourceValue(null, "cat_backrid", -1);
		this.cat_back_r=((BitmapDrawable)this.getResources().getDrawable(cat_backrid)).getBitmap();
		this.cat_frontrid = attrs.getAttributeResourceValue(null, "cat_frontrid", -1);
		this.cat_front_r=((BitmapDrawable)this.getResources().getDrawable(cat_frontrid)).getBitmap();
		//猫的尾巴====================================================================================
		this.catTail = new Bitmap[30];
		//===============side=============
		this.catTail[0] = BitmapFactory.decodeResource(this.getResources(), R.drawable.side_tail1);
		this.catTail[1] = BitmapFactory.decodeResource(this.getResources(), R.drawable.side_tail2);
		this.catTail[2] = BitmapFactory.decodeResource(this.getResources(), R.drawable.side_tail3);
		this.catTail[3] = BitmapFactory.decodeResource(this.getResources(), R.drawable.side_tail4);
		this.catTail[4] = BitmapFactory.decodeResource(this.getResources(), R.drawable.side_tail5);
		//===============front============
		this.catTail[5] = BitmapFactory.decodeResource(this.getResources(), R.drawable.front_tail1);
		this.catTail[6] = BitmapFactory.decodeResource(this.getResources(), R.drawable.front_tail2);
		this.catTail[7] = BitmapFactory.decodeResource(this.getResources(), R.drawable.front_tail3);
		this.catTail[8] = BitmapFactory.decodeResource(this.getResources(), R.drawable.front_tail4);
		this.catTail[9] = BitmapFactory.decodeResource(this.getResources(), R.drawable.front_tail5);
		//===============back=============
		this.catTail[10] = BitmapFactory.decodeResource(this.getResources(), R.drawable.back_tail1);
		this.catTail[11] = BitmapFactory.decodeResource(this.getResources(), R.drawable.back_tail2);
		this.catTail[12] = BitmapFactory.decodeResource(this.getResources(), R.drawable.back_tail3);
		this.catTail[13] = BitmapFactory.decodeResource(this.getResources(), R.drawable.back_tail4);
		this.catTail[14] = BitmapFactory.decodeResource(this.getResources(), R.drawable.back_tail5);
		//===============sideR=============
		this.catTail[15] = BitmapFactory.decodeResource(this.getResources(), R.drawable.side_r_tail1);
		this.catTail[16] = BitmapFactory.decodeResource(this.getResources(), R.drawable.side_r_tail2);
		this.catTail[17] = BitmapFactory.decodeResource(this.getResources(), R.drawable.side_r_tail3);
		this.catTail[18] = BitmapFactory.decodeResource(this.getResources(), R.drawable.side_r_tail4);
		this.catTail[19] = BitmapFactory.decodeResource(this.getResources(), R.drawable.side_r_tail5);
		//===============frontR============
		this.catTail[20] = BitmapFactory.decodeResource(this.getResources(), R.drawable.front_r_tail1);
		this.catTail[21] = BitmapFactory.decodeResource(this.getResources(), R.drawable.front_r_tail2);
		this.catTail[22] = BitmapFactory.decodeResource(this.getResources(), R.drawable.front_r_tail3);
		this.catTail[23] = BitmapFactory.decodeResource(this.getResources(), R.drawable.front_r_tail4);
		this.catTail[24] = BitmapFactory.decodeResource(this.getResources(), R.drawable.front_r_tail5);
		//===============backR=============
		this.catTail[25] = BitmapFactory.decodeResource(this.getResources(), R.drawable.back_r_tail1);
		this.catTail[26] = BitmapFactory.decodeResource(this.getResources(), R.drawable.back_r_tail2);
		this.catTail[27] = BitmapFactory.decodeResource(this.getResources(), R.drawable.back_r_tail3);
		this.catTail[28] = BitmapFactory.decodeResource(this.getResources(), R.drawable.back_r_tail4);
		this.catTail[29] = BitmapFactory.decodeResource(this.getResources(), R.drawable.back_r_tail5);
		//初始化坐标列
		this.itemPlaceSet = new Rect[this.rowSum][this.columnSum];
		//y 向下走了10， x向右走了 15
		for(int i =0,y = weiY; i<this.rowSum;i++,y += this.item_rect.height()){
			//全部height位移
			int x = weiX ;
			if(i%2!=0){
				//奇数行
				x += this.item_rect.width()/2;//缩进半个width
			}
			this.itemPlaceSet[i][0]= new Rect(x,y,x+this.item_rect.width(),y+this.item_rect.height());
		}
		for(int i = 0 ; i<this.rowSum;i++){
			for(int j= 1; j<this.columnSum;j++){
				this.itemPlaceSet[i][j] =new Rect(//全部width位移
						this.itemPlaceSet[i][j-1].left+this.item_rect.width(),
						this.itemPlaceSet[i][j-1].top,
						this.itemPlaceSet[i][j-1].right+this.item_rect.width(),
						this.itemPlaceSet[i][j-1].bottom
						);
			}
		}
		
		//初始化地图数据结构
		this.placeState =new int[this.rowSum][this.columnSum];
		this.cleanMapStop();
		//初始化
	}
	
	//========================public============================================
    public boolean setStop(int xh,int yl ,int state){
    	if(xh<0||xh>this.rowSum-1||yl<0||yl>this.columnSum-1){
    		//越界
    		return false ;
    	}
    	if(this.placeState[xh][yl] == pass){//可通行
    		this.placeState[xh][yl] = state ;
    		return true ;
    	}
    	//重复
    	return false ;
    }
    public void reDraw(Rect rect){
    	if(rect!=null){
    		this.postInvalidate(rect.left, rect.top,rect.right, rect.bottom);
    	}else{
    		this.postInvalidate();
    	}
    }
    public void cleanMapStop(){
    	//清空所有障碍与猫
    	for(int i = 0;i<this.columnSum;i++)
    	for(int j = 0;j<this.columnSum;j++)
    		this.placeState[i][j] =this.pass;
    	
    }
	public void drawTouchPlace(ItemXY itemXY) {
		if(itemXY != null){
			//设置路障
			this.placeState[itemXY.x][itemXY.y] = newStop;
			Rect dst = new Rect(this.itemPlaceSet[itemXY.x][itemXY.y].left,itemPlaceSet[itemXY.x][itemXY.y].bottom-this.stop_rect.height(),itemPlaceSet[itemXY.x][itemXY.y].left+this.stop_rect.width(),itemPlaceSet[itemXY.x][itemXY.y].bottom) ;
			this.reDraw(dst);
			
		}
	}
	
    public ItemXY getItemXY(float x,float y){
        //获取相对位移
    	Log.d("state", "Local "+(int )y);
    	//粗计算
    	float x_WY = x - this.weiX;//相对位移
    	float y_WY = y - this.weiY;//相对位移
    	int xh , yl;//所在的行列
    	//计算行
    	xh = (int)(y_WY/this.item_rect.height()); //第几行用绝对位移，因为item中记载的是从位移开始的。
    	if(xh%2!=0){//基数行（1开始）
    		x_WY -= this.item_rect.width()/2;//缩进的也要算进去
    	}
    	yl = (int)(x_WY/this.item_rect.width());//第几列
    	Log.d("state", "h "+xh+":l "+yl);
    	
    	if(xh<0||xh>this.rowSum-1||yl<0||yl>this.columnSum-1){
    		Log.d("state", "null 1");//不符合条件跳出
    		return null ;
    	}
    	
	    //精计算(行)
    	float testY = y  ;//造假
	    if(testY>=this.itemPlaceSet[xh][0].top){
	    	//准确位置
	    	Log.d("state", "in "+xh);
	    	if(testY>=this.itemPlaceSet[xh][0].bottom){
	    		xh += 1;//下一行
	    		Log.d("state", "out "+xh);
	    		if(xh > this.rowSum-1){
	    			//返回超出返回空
	    			Log.d("state", "out Game Bound"+xh);
	    			return null ;
	    			
	    		}
	    	}
	    }else{
	    	Log.d("state", "less in "+y +" < "+this.itemPlaceSet[xh][0].top);
	    	xh -= 1 ;//上一行
	    	if(xh < 0){
    			//返回超出返回空
	    		Log.d("state", "less game bound ");
    			return null ;
    		}
	   	}
	   
	    //精计算（列）
		if(x>=this.itemPlaceSet[xh][yl].left){
		    //准确位置
		    if(yl>=this.itemPlaceSet[xh][yl].right){
		    	yl += 1;//右一列
		    }
		    if(yl > this.columnSum-1){
    			//返回超出返回空
    			return null ;
    		}
		}else{
			yl -= 1 ;//左一列
			if(yl < 0){
	    		//返回超出返回空
	    	    return null ;
	    	}
		}  		
    	return new ItemXY(xh,yl);
    }
    public void catRunArrowAnimation(int arrow,ItemXY newPlace){
    	//this.cat_nowArrow = arrow;
    	this.cat_nowArrow = arrow;
   // 	this.cat_nowArrow = this.right;
    //	newPlace = this.cat_XY;
    	new CatRunAnimation(newPlace).start();
    }
    
    public void changeCatPlace(ItemXY newxy){
    	if(this.cat_XY!=null){//处理原本
    		this.placeState[cat_XY.x][cat_XY.y] = pass ;
    	}
    	this.cat_XY = new ItemXY(newxy.x,newxy.y) ;
    	this.catPlaceRect = new Rect(this.itemPlaceSet[cat_XY.x][cat_XY.y]);
    	this.placeState[cat_XY.x][cat_XY.y] = catPlace ;
    	this.cat_nowNum = this.catStand;
    	this.postInvalidate();
    }
	public String getNowStop(){
		String state = "";
		for(int i = 0;i<this.rowSum;i++)
			for(int j = 0;j<this.columnSum;j++)
				state += this.placeState[i][j] ;
		return state;
	}
	public void resetCatArrow(){
		this.cat_nowArrow = this.leftDown;
	}
}

