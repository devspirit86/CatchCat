
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
	//=============���λ��
	private final int weiX = 15,weiY = 10;
	//==============Ȧ=================================
	private Bitmap item = null;//ԲȦ
	private int item_id = -1;//ͼƬId
	private Rect item_rect = null ;//ԲȦ��С
	//==============�谭��==============================
	private Bitmap orStopImage = null;//ԭ�谭��
	private Bitmap newStopImage = null;//���ϰ���
	private int stopImage_id = -1;//�谭��Id
	private Rect stop_rect = null ;
	private ItemXY cat_XY = null;
	//==============è=================================
	private Bitmap cat_side = null;//��è
	private int cat_sideid = -1;//��èId
	private Bitmap cat_back = null;//����è
	private int cat_backid = -1;//����èId
	private Bitmap cat_front = null;//����è
	private int cat_frontid = -1;//����èId
	
	private Bitmap cat_side_r = null;//��è
	private int cat_siderid = -1;//��èId
	private Bitmap cat_back_r = null;//����è
	private int cat_backrid = -1;//����èId
	private Bitmap cat_front_r = null;//����è
	private int cat_frontrid = -1;//����èId
	
	public int cat_nowArrow = left ;//��ǰè�ķ���
	public int cat_nowNum = 5 ;//��ǰè��ͼƬ
	private Rect cat_rect = null;//è�Ĵ�С
	private Rect catrectInPicture = null;//è��ͼ�е�
	private static final int catRun = 0x111;
	private Rect catPlaceRect = null ;//è������λ�� 
	private Bitmap catNowPicture = null ;//��ǰ��ѡ���ͼƬ 
	private Bitmap[] catTail = null ;//è��β��ͼƬ
	
	//private Bitmap[] tailCat = null ;//è��β��
	//=============cat��������
	private final int catStand = 5;//èվ��
	//=============��left��ʼ˳ʱ��
	public static final int left = 1 ;
	public static final int leftTop = 2 ;
	public static final int rightTop = 3 ;
	public static final int right = 4 ;
	public static final int rightDown = 5 ;
	public static final int leftDown = 6 ;
	
	
	//==============��������=============================
	private Rect[][] itemPlaceSet = null;//item����λ��
    public int[][] placeState = null;//λ��״̬
    public final int rowSum = 11;//��item����
    public final int columnSum = 11;//��item����
	public final int pass = 0;//��ͨ��
    public final int orStop = 1;//ԭ�谭��
    public final int newStop = 2;//���谭��
    public final int catPlace = 3;//è����λ
    
	//inner class
    public class ItemXY {
    	public int x,y ;
    	ItemXY(int x,int y){
    		this.x= x;//��
    		this.y= y;//��
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
    		    //�жϷ���
    			switch(MyMapView.this.cat_nowArrow){
    			case left:{
    				for(int i = 4;i>=0;i--){
    					MyMapView.this.cat_nowNum = i ;
    					MyMapView.this.postInvalidate();
	        			try {
	        				this.sleep(150);//��˯
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
	        				this.sleep(150);//��˯
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
	        				this.sleep(150);//��˯
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
	        				this.sleep(150);//��˯
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
	        				this.sleep(150);//��˯
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
	        				this.sleep(150);//��˯
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
    				Game.undoAble = true;//���Ի�����
    				Game.game.secondView.reStart();
    			}
    		}
    	}

	protected void onDraw(Canvas canvas){
		Paint paint = new Paint();
		//===========================����================================================== 
		for(int i = 0 ;i <this.rowSum;i++){
			for(int j = 0 ;j <this.columnSum;j++){
				switch(this.placeState[i][j]){
					case orStop :{//ԭ�谭
						Rect dst = new Rect(this.itemPlaceSet[i][j].left,this.itemPlaceSet[i][j].bottom-this.stop_rect.height(),this.itemPlaceSet[i][j].left+this.stop_rect.width(),this.itemPlaceSet[i][j].bottom) ;
						canvas.drawBitmap(this.orStopImage, this.stop_rect,dst, paint);
						}break;
					case newStop :{//���谭
						Rect dst = new Rect(this.itemPlaceSet[i][j].left,this.itemPlaceSet[i][j].bottom-this.stop_rect.height(),this.itemPlaceSet[i][j].left+this.stop_rect.width(),this.itemPlaceSet[i][j].bottom) ;
						canvas.drawBitmap(this.newStopImage, this.stop_rect, dst, paint);
						}break;
					default :{//������
						canvas.drawBitmap(this.item, this.item_rect, this.itemPlaceSet[i][j], paint);
						}
					}
				}
			//=========================Test================================================
			 //   this.cat_nowArrow = left;
			//	this.cat_nowNum = 4;
			//=========================��è=================================================  
				int temp = this.cat_nowNum ;
				if(temp == catStand){//��ʼλ��
					temp = 0;
				}
				//����èè��С
				int width = 0 , height = 0;
				switch(this.cat_nowArrow){
				case left :  width = 81 ;height = 73 ;break;
				case right :  width = 81 ;height = 73 ;break;
				case leftTop :  width = 64 ;height = 95 ;break;
				case rightTop : width = 64 ;height = 95 ;break;
				case leftDown :  width = 64 ;height = 70 ;break;
				case rightDown :  width = 64 ;height = 70 ;break;
				}
				Rect src = new Rect(//��Ҫ����������ڲ������չ̶�ƽ��
						temp*width,
						0,
						width+temp*width,
						height
						);
				//��è��λ��
				Rect dst = new Rect(//è�ľ���������λ�ð�left top ���ж��� 
						catPlaceRect.left,
						catPlaceRect.top,
						catPlaceRect.left+width,
						catPlaceRect.top+height
						) ;
				Bitmap tailB = null ;
				Rect tailDst = null;//β�͵�Ŀ��λ��
			switch(this.cat_nowArrow){
			//++++++++++++++++++++++++++++++++++++++++left+++++++++++++++++++++++++++++++++
			case left :{
				this.catNowPicture = this.cat_side ;
				switch(this.cat_nowNum){
				case 0:{
					
					int sx = 0,sy =0 ,dx = -60 , dy = -40 ;
					//ͼ��ϸ
					src.left += sx;
					src.right += sx;
					src.top  += sy ;
					src.bottom += sy ;
					//����ϸ
					dst.left += dx;
					dst.right += dx;
					dst.top  += dy ;
					dst.bottom += dy ;
					//β�ͣ�
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
					//ͼ��ϸ
					src.left += sx;
					src.right += sx;
					src.top  += sy ;
					src.bottom += sy ;
					//����ϸ
					dst.left += dx;
					dst.right += dx;
					dst.top  += dy ;
					dst.bottom += dy ;
					//β�ͣ�
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
					
					//ͼ��ϸ
					src.left += sx;
					src.right += sx;
					src.top  += sy ;
					src.bottom += sy ;
					//����ϸ
					dst.left += dx;
					dst.right += dx;
					dst.top  += dy ;
					dst.bottom += dy ;
					//β�ͣ�
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
					//ͼ��ϸ
					src.left += sx;
					src.right += sx;
					src.top  += sy ;
					src.bottom += sy ;
					//����ϸ
					dst.left += dx;
					dst.right += dx;
					dst.top  += dy ;
					dst.bottom += dy ;
					//β�ͣ�
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
					//ͼ��ϸ
					src.left += sx;
					src.right += sx;
					src.top  += sy ;
					src.bottom += sy ;
					//����ϸ
					dst.left += dx;
					dst.right += dx;
					dst.top  += dy ;
					dst.bottom += dy ;
					//β�ͣ�
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
				case catStand:{//ԭʼλ��
					int sx = 0,sy =0 ,dx = -18 , dy = -40 ;
					//ͼ��ϸ
					src.left += sx;
					src.right += sx;
					src.top  += sy ;
					src.bottom += sy ;
					//����ϸ
					dst.left += dx;
					dst.right += dx;
					dst.top  += dy ;
					dst.bottom += dy ;
					//β�ͣ�
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
					//ͼ��ϸ
					src.left += sx;
					src.right += sx;
					src.top  += sy ;
					src.bottom += sy ;
					//����ϸ
					dst.left += dx;
					dst.right += dx;
					dst.top  += dy ;
					dst.bottom += dy ;
					//β�͵�λ��
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
					//ͼ��ϸ
					src.left += sx;
					src.right += sx;
					src.top  += sy ;
					src.bottom += sy ;
					//����ϸ
					dst.left += dx;
					dst.right += dx;
					dst.top  += dy ;
					dst.bottom += dy ;
					//β�͵�λ��
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
					//ͼ��ϸ
					src.left += sx;
					src.right += sx;
					src.top  += sy ;
					src.bottom += sy ;
					//����ϸ
					dst.left += dx;
					dst.right += dx;
					dst.top  += dy ;
					dst.bottom += dy ;
					//β�͵�λ��
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
					//ͼ��ϸ
					src.left += sx;
					src.right += sx;
					src.top  += sy ;
					src.bottom += sy ;
					//����ϸ
					dst.left += dx;
					dst.right += dx;
					dst.top  += dy ;
					dst.bottom += dy ;
					//β�͵�λ��
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
					//ͼ��ϸ
					src.left += sx;
					src.right += sx;
					src.top  += sy ;
					src.bottom += sy ;
					//����ϸ
					dst.left += dx;
					dst.right += dx;
					dst.top  += dy ;
					dst.bottom += dy ;
					//β�͵�λ��
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
				case catStand:{//ԭʼλ��
					int sx = 0,sy =0 ,dx = -10 , dy = -60 ;
					//ͼ��ϸ
					src.left += sx;
					src.right += sx;
					src.top  += sy ;
					src.bottom += sy ;
					//����ϸ
					dst.left += dx;
					dst.right += dx;
					dst.top  += dy ;
					dst.bottom += dy ;
					//β�͵�λ��
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
				//ͼ��ϸ
				src.left += sx;
				src.right += sx;
				src.top  += sy ;
				src.bottom += sy ;
				//����ϸ
				dst.left += dx;
				dst.right += dx;
				dst.top  += dy ;
				dst.bottom += dy ;
				//β�͵�λ��
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
				//ͼ��ϸ
				src.left += sx;
				src.right += sx;
				src.top  += sy ;
				src.bottom += sy ;
				//����ϸ
				dst.left += dx;
				dst.right += dx;
				dst.top  += dy ;
				dst.bottom += dy ;
				//β�͵�λ��
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
				//ͼ��ϸ
				src.left += sx;
				src.right += sx;
				src.top  += sy ;
				src.bottom += sy ;
				//����ϸ
				dst.left += dx;
				dst.right += dx;
				dst.top  += dy ;
				dst.bottom += dy ;
				//β�͵�λ��
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
				//ͼ��ϸ
				src.left += sx;
				src.right += sx;
				src.top  += sy ;
				src.bottom += sy ;
				//����ϸ
				dst.left += dx;
				dst.right += dx;
				dst.top  += dy ;
				dst.bottom += dy ;
				//β�͵�λ��
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
				//ͼ��ϸ
				src.left += sx;
				src.right += sx;
				src.top  += sy ;
				src.bottom += sy ;
				//����ϸ
				dst.left += dx;
				dst.right += dx;
				dst.top  += dy ;
				dst.bottom += dy ;
				//β�͵�λ��
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
			case catStand:{//ԭʼλ��
				int sx = 0,sy =0 ,dx = -15 , dy = -60 ;
				//ͼ��ϸ
				src.left += sx;
				src.right += sx;
				src.top  += sy ;
				src.bottom += sy ;
				//����ϸ
				dst.left += dx;
				dst.right += dx;
				dst.top  += dy ;
				dst.bottom += dy ;
				//β�͵�λ��
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
							//ͼ��ϸ
							src.left += sx;
							src.right += sx;
							src.top  += sy ;
							src.bottom += sy ;
							//����ϸ
							dst.left += dx;
							dst.right += dx;
							dst.top  += dy ;
							dst.bottom += dy ;
							//β�ͣ�
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
							//ͼ��ϸ
							src.left += sx;
							src.right += sx;
							src.top  += sy ;
							src.bottom += sy ;
							//����ϸ
							dst.left += dx;
							dst.right += dx;
							dst.top  += dy ;
							dst.bottom += dy ;
							//β�ͣ�
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
							//ͼ��ϸ
							src.left += sx;
							src.right += sx;
							src.top  += sy ;
							src.bottom += sy ;
							//����ϸ
							dst.left += dx;
							dst.right += dx;
							dst.top  += dy ;
							dst.bottom += dy ;
							//β�ͣ�
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
							//ͼ��ϸ
							src.left += sx;
							src.right += sx;
							src.top  += sy ;
							src.bottom += sy ;
							//����ϸ
							dst.left += dx;
							dst.right += dx;
							dst.top  += dy ;
							dst.bottom += dy ;
							//β�ͣ�
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
							//ͼ��ϸ
							src.left += sx;
							src.right += sx;
							src.top  += sy ;
							src.bottom += sy ;
							//����ϸ
							dst.left += dx;
							dst.right += dx;
							dst.top  += dy ;
							dst.bottom += dy ;
							//β�ͣ�
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
						case catStand:{//ԭʼλ��
							int sx = 0,sy =0 ,dx =2 , dy = -40 ;
							//ͼ��ϸ
							src.left += sx;
							src.right += sx;
							src.top  += sy ;
							src.bottom += sy ;
							//����ϸ
							dst.left += dx;
							dst.right += dx;
							dst.top  += dy ;
							dst.bottom += dy ;
							//β�ͣ�
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
				//ͼ��ϸ
				src.left += sx;
				src.right += sx;
				src.top  += sy ;
				src.bottom += sy ;
				//����ϸ
				dst.left += dx;
				dst.right += dx;
				dst.top  += dy ;
				dst.bottom += dy ;
				//β�͵�λ��
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
				//ͼ��ϸ
				src.left += sx;
				src.right += sx;
				src.top  += sy ;
				src.bottom += sy ;
				//����ϸ
				dst.left += dx;
				dst.right += dx;
				dst.top  += dy ;
				dst.bottom += dy ;
				//β�͵�λ��
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
				//ͼ��ϸ
				src.left += sx;
				src.right += sx;
				src.top  += sy ;
				src.bottom += sy ;
				//����ϸ
				dst.left += dx;
				dst.right += dx;
				dst.top  += dy ;
				dst.bottom += dy ;
				//β�͵�λ��
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
				//ͼ��ϸ
				src.left += sx;
				src.right += sx;
				src.top  += sy ;
				src.bottom += sy ;
				//����ϸ
				dst.left += dx;
				dst.right += dx;
				dst.top  += dy ;
				dst.bottom += dy ;
				//β�͵�λ��
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
				//ͼ��ϸ
				src.left += sx;
				src.right += sx;
				src.top  += sy ;
				src.bottom += sy ;
				//����ϸ
				dst.left += dx;
				dst.right += dx;
				dst.top  += dy ;
				dst.bottom += dy ;
				//β�͵�λ��
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
			case catStand:{//ԭʼλ��
				int sx = 0,sy =0 ,dx = -15 , dy = -35 ;
				//ͼ��ϸ
				src.left += sx;
				src.right += sx;
				src.top  += sy ;
				src.bottom += sy ;
				//����ϸ
				dst.left += dx;
				dst.right += dx;
				dst.top  += dy ;
				dst.bottom += dy ;
				//β�͵�λ��
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
				//ͼ��ϸ
				src.left += sx;
				src.right += sx;
				src.top  += sy ;
				src.bottom += sy ;
				//����ϸ
				dst.left += dx;
				dst.right += dx;
				dst.top  += dy ;
				dst.bottom += dy ;
				//β�͵�λ��
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
				//ͼ��ϸ
				src.left += sx;
				src.right += sx;
				src.top  += sy ;
				src.bottom += sy ;
				//����ϸ
				dst.left += dx;
				dst.right += dx;
				dst.top  += dy ;
				dst.bottom += dy ;
				//β�͵�λ��
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
				//ͼ��ϸ
				src.left += sx;
				src.right += sx;
				src.top  += sy ;
				src.bottom += sy ;
				//����ϸ
				dst.left += dx;
				dst.right += dx;
				dst.top  += dy ;
				dst.bottom += dy ;
				//β�͵�λ��
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
				//ͼ��ϸ
				src.left += sx;
				src.right += sx;
				src.top  += sy ;
				src.bottom += sy ;
				//����ϸ
				dst.left += dx;
				dst.right += dx;
				dst.top  += dy ;
				dst.bottom += dy ;
				//β�͵�λ��
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
				//ͼ��ϸ
				src.left += sx;
				src.right += sx;
				src.top  += sy ;
				src.bottom += sy ;
				//����ϸ
				dst.left += dx;
				dst.right += dx;
				dst.top  += dy ;
				dst.bottom += dy ;
				//β�͵�λ��
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
			case catStand:{//ԭʼλ��
				int sx = 0,sy =0 ,dx = -10 , dy = -35 ;
				//ͼ��ϸ
				src.left += sx;
				src.right += sx;
				src.top  += sy ;
				src.bottom += sy ;
				//����ϸ
				dst.left += dx;
				dst.right += dx;
				dst.top  += dy ;
				dst.bottom += dy ;
				//β�͵�λ��
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
	//��ʼ����ͼ
	 void initMapView(AttributeSet attrs){
		//��ʼ��image==================================================================
		//================��ʼ��item
		this.item_id = attrs.getAttributeResourceValue(null, "item_id", -1);
		int item_X = 0, item_Y = 0,item_width=0 ,item_height=0;
		item_X = attrs.getAttributeIntValue(null, "item_X", 0);
		item_Y = attrs.getAttributeIntValue(null, "item_Y", 0);
		item_width = attrs.getAttributeIntValue(null, "item_width", 0);
		item_height = attrs.getAttributeIntValue(null, "item_height", 0);
		this.item_rect = new Rect(item_X,item_Y,item_X+item_width,item_Y+item_height);
		this.item=Bitmap.createBitmap(((BitmapDrawable)this.getResources().getDrawable(item_id)).getBitmap(), this.item_rect.left, this.item_rect.top, this.item_rect.width(), this.item_rect.height());	
		//================��ʼ��stop
		this.stopImage_id = attrs.getAttributeResourceValue(null, "stopImage_id", -1);
		int stop_X = 0, stop_Y = 0,stop_width=0 ,stop_height=0;
		stop_X = attrs.getAttributeIntValue(null, "stop_X", 0);
		stop_Y = attrs.getAttributeIntValue(null, "stop_Y", 0);
		stop_width = attrs.getAttributeIntValue(null, "stop_width", 0);
		stop_height = attrs.getAttributeIntValue(null, "stop_height", 0);
		this.stop_rect = new Rect(stop_X,stop_Y,stop_X+stop_width,stop_Y+stop_height);
		this.newStopImage=Bitmap.createBitmap(((BitmapDrawable)this.getResources().getDrawable(stopImage_id)).getBitmap(), this.stop_rect.left, this.stop_rect.top, this.stop_rect.width(), this.stop_rect.height());
		this.orStopImage=Bitmap.createBitmap(((BitmapDrawable)this.getResources().getDrawable(stopImage_id)).getBitmap(), this.stop_rect.left+this.stop_rect.width(), this.stop_rect.top, this.stop_rect.width(), this.stop_rect.height());
		//================��ʼ��Cat
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
		//è��β��====================================================================================
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
		//��ʼ��������
		this.itemPlaceSet = new Rect[this.rowSum][this.columnSum];
		//y ��������10�� x�������� 15
		for(int i =0,y = weiY; i<this.rowSum;i++,y += this.item_rect.height()){
			//ȫ��heightλ��
			int x = weiX ;
			if(i%2!=0){
				//������
				x += this.item_rect.width()/2;//�������width
			}
			this.itemPlaceSet[i][0]= new Rect(x,y,x+this.item_rect.width(),y+this.item_rect.height());
		}
		for(int i = 0 ; i<this.rowSum;i++){
			for(int j= 1; j<this.columnSum;j++){
				this.itemPlaceSet[i][j] =new Rect(//ȫ��widthλ��
						this.itemPlaceSet[i][j-1].left+this.item_rect.width(),
						this.itemPlaceSet[i][j-1].top,
						this.itemPlaceSet[i][j-1].right+this.item_rect.width(),
						this.itemPlaceSet[i][j-1].bottom
						);
			}
		}
		
		//��ʼ����ͼ���ݽṹ
		this.placeState =new int[this.rowSum][this.columnSum];
		this.cleanMapStop();
		//��ʼ��
	}
	
	//========================public============================================
    public boolean setStop(int xh,int yl ,int state){
    	if(xh<0||xh>this.rowSum-1||yl<0||yl>this.columnSum-1){
    		//Խ��
    		return false ;
    	}
    	if(this.placeState[xh][yl] == pass){//��ͨ��
    		this.placeState[xh][yl] = state ;
    		return true ;
    	}
    	//�ظ�
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
    	//��������ϰ���è
    	for(int i = 0;i<this.columnSum;i++)
    	for(int j = 0;j<this.columnSum;j++)
    		this.placeState[i][j] =this.pass;
    	
    }
	public void drawTouchPlace(ItemXY itemXY) {
		if(itemXY != null){
			//����·��
			this.placeState[itemXY.x][itemXY.y] = newStop;
			Rect dst = new Rect(this.itemPlaceSet[itemXY.x][itemXY.y].left,itemPlaceSet[itemXY.x][itemXY.y].bottom-this.stop_rect.height(),itemPlaceSet[itemXY.x][itemXY.y].left+this.stop_rect.width(),itemPlaceSet[itemXY.x][itemXY.y].bottom) ;
			this.reDraw(dst);
			
		}
	}
	
    public ItemXY getItemXY(float x,float y){
        //��ȡ���λ��
    	Log.d("state", "Local "+(int )y);
    	//�ּ���
    	float x_WY = x - this.weiX;//���λ��
    	float y_WY = y - this.weiY;//���λ��
    	int xh , yl;//���ڵ�����
    	//������
    	xh = (int)(y_WY/this.item_rect.height()); //�ڼ����þ���λ�ƣ���Ϊitem�м��ص��Ǵ�λ�ƿ�ʼ�ġ�
    	if(xh%2!=0){//�����У�1��ʼ��
    		x_WY -= this.item_rect.width()/2;//������ҲҪ���ȥ
    	}
    	yl = (int)(x_WY/this.item_rect.width());//�ڼ���
    	Log.d("state", "h "+xh+":l "+yl);
    	
    	if(xh<0||xh>this.rowSum-1||yl<0||yl>this.columnSum-1){
    		Log.d("state", "null 1");//��������������
    		return null ;
    	}
    	
	    //������(��)
    	float testY = y  ;//���
	    if(testY>=this.itemPlaceSet[xh][0].top){
	    	//׼ȷλ��
	    	Log.d("state", "in "+xh);
	    	if(testY>=this.itemPlaceSet[xh][0].bottom){
	    		xh += 1;//��һ��
	    		Log.d("state", "out "+xh);
	    		if(xh > this.rowSum-1){
	    			//���س������ؿ�
	    			Log.d("state", "out Game Bound"+xh);
	    			return null ;
	    			
	    		}
	    	}
	    }else{
	    	Log.d("state", "less in "+y +" < "+this.itemPlaceSet[xh][0].top);
	    	xh -= 1 ;//��һ��
	    	if(xh < 0){
    			//���س������ؿ�
	    		Log.d("state", "less game bound ");
    			return null ;
    		}
	   	}
	   
	    //�����㣨�У�
		if(x>=this.itemPlaceSet[xh][yl].left){
		    //׼ȷλ��
		    if(yl>=this.itemPlaceSet[xh][yl].right){
		    	yl += 1;//��һ��
		    }
		    if(yl > this.columnSum-1){
    			//���س������ؿ�
    			return null ;
    		}
		}else{
			yl -= 1 ;//��һ��
			if(yl < 0){
	    		//���س������ؿ�
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
    	if(this.cat_XY!=null){//����ԭ��
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

