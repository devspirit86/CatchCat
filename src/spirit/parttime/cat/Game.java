package spirit.parttime.cat;

import spirit.parttime.cat.MyMapView.ItemXY;
import spirit.parttime.cat.logic.Cat;
import spirit.parttime.cat.logic.Map;
import spirit.parttime.cat.logic.Place;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import spirit.parttime.cat.music.Music;

public class Game extends Activity implements OnTouchListener,OnClickListener{
	//==================UI==============================
	private MyMapView mapView = null ;//������
	private MyTextView level = null ;//�ȼ�
	private MyTextView score = null ;//����
	private MyLivesTextView lives = null ;//��
	private MyTextView hscore = null ;//��߷���
	private MyImageButton menu = null ;//�˵���ť
	private MyImageButton undo = null ;//��ͣ��ť
	public SecondView secondView = null;//����ʱ
	public static Game game = null ;//��Ϸ����
	private TimeBarView timeBar = null ;//ʱ����
	//=================ȫ��״̬============================= 
	//��ʾresumeʱ Ҫ���е���ʱ
	//ͨ��gameStop����������ͣ����Ϸ��ֻ��ͨ��gameStart�������ָ������ܱ�resume���
	//����gameStop������ͣ����Ϸ������ͨ��onresume���ָ�
	public  boolean resumeRun ;
	public static final int AI = 1;
	public static final int User = 0;
	public static int nowTurn = User ;
	//==================Game State========================
	private int levelInt=0,scoreInt =0 ,livesInt =3 ,hscoreInt= 0;
	static final int stop = 0 ,running = 1;//��Ϸ�Ƿ�running
	public final int failureOver = 0 , winOver = 1,failure=2;//over��״̬
	public static int gameState = running ;
	public  boolean overAble = false ;
	private MyMapView.ItemXY catXY = null ;//��ǰè��λ��
	private MyMapView.ItemXY[] orStopSet = null ;//ԭʼ�ϰ���λ�ü���
	public  Map mapData = null ;//��ͼ������Ϣ
	private Cat catDate = null ;//è��������Ϣ
	private int timeUsed =0;//�û��Ѿ����˶���ʱ��
	private int linshiscore =0 ;//��ʱ����
	private int[] stopLevel ;
	//===================Undo===============================
	private int nowStopX = -1 ,nowStopY= -1;//��ǰ�ϰ���
	private int nowCatX = -1 , nowCatY = -1;//��ǰè�������
    public static boolean undoAble = false ;
	//==================�߳�===============================
	public static final int messageCode = 1,showMenu=2;
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        this.mapView = (MyMapView)this.findViewById(R.id.mapView);
        this.level = (MyTextView)this.findViewById(R.id.level);
        this.score = (MyTextView)this.findViewById(R.id.score);
        this.lives = (MyLivesTextView)this.findViewById(R.id.lives);
        this.hscore = (MyTextView)this.findViewById(R.id.hscore);
        this.secondView =(SecondView)this.findViewById(R.id.secondView);
        this.timeBar = (TimeBarView)this.findViewById(R.id.barView);
        
        this.menu = (MyImageButton)this.findViewById(R.id.menu);
        this.undo = (MyImageButton)this.findViewById(R.id.undo);
        this.menu.setOnClickListener(this);
        this.undo.setOnClickListener(this);
        /*this.menu.setFocusableInTouchMode(true);
        this.undo.setFocusableInTouchMode(true);*/
        resumeRun = true;//ÿ���ؿ�ʱ��Ϊtrue��
        //���ݽṹ
        this.mapData = new Map(this.mapView.rowSum);
        this.catDate = new Cat(this.mapData.map[mapData.center][mapData.center]);
		this.mapView.setOnTouchListener(this);	
		this.game = this;
		//=======================����======================================
		this.startGame();
    }
    //��Ϸ״̬
    public void initGameMap(int level){
    	if(level<=10){
    		//ǰ10�� 
    		this.stopLevel = this.getResources().getIntArray(R.array.stopin10level);
    	}else if(level>20){
    		//20�غ�
    		this.stopLevel = this.getResources().getIntArray(R.array.out20);
    	}else{
    		this.stopLevel = this.getResources().getIntArray(R.array.stop10to20);
    	}
    	//Ĭ��һ����ȡ 
    	int stopNum = this.stopLevel[level%10];
    	this.createNewMap(stopNum);
    	//��ʼ��������Ϣ
    	this.mapView.resetCatArrow();//��������è��λ��
    	this.updateText();
    }
    
    public void startGame(){
    	//��ȡ��Ϸ״̬
    	Intent intent = this.getIntent();
		int gameState = intent.getIntExtra("gamestate", Menu.newGame);	
		this.linshiscore = intent.getIntExtra("linshiscore", -1);//��ʱ����
		//��ȡ��߷���
		int[] scoreList = this.getScoreList();
		if(scoreList[0]>=0){
			this.hscoreInt = scoreList[0];//��߷���
        }else{
        	this.hscoreInt = 0;
        }
        this.livesInt = intent.getIntExtra("lives", -1) ;//��ǰ����
        this.timeBar.setMaxtime(intent.getIntExtra("totaltime", 0));//��Ϸ��ʱ��
        this.secondView.setNumber(intent.getIntExtra("parttime", 0));//ÿһ����ʱ��
		if(gameState==Menu.newGame){
			//���¿�ʼ��Ϸ
			this.levelInt = 1;//��һ�� 
	        this.scoreInt = 0 ;//����Ϊ0
	        this.timeBar.setTime(0);//��ǰ��Ϸʱ��
	        this.nowTurn = this.User;//�û��� 
	        this.timeUsed = 0;//�Ѿ�����0��
	        this.mapView.cleanMapStop();
	    	this.mapData.reinitMap();
	    	//���èλ��
	  //  	this.catDate = new Cat(this.mapData.map[5][5]);
	   // 	this.mapView.changeCatPlace(this.mapView.new ItemXY(5,5));	
			this.initGameMap(this.levelInt);//���ɵ�ͼ��Ϣ
	        this.updateText();
	        return ;
    	}else if(gameState==Menu.reStart){
    		//��һ����Ϸ
			this.levelInt = intent.getIntExtra("level", -1);//��һ��
			
	        this.scoreInt = intent.getIntExtra("score", 0);//����Ϊ0
	       
	        this.timeBar.setTime(intent.getIntExtra("nowtime",0));//��ǰ��Ϸʱ��
	       
	        this.nowTurn = intent.getIntExtra("nowTurn", this.User);//��ǰ��˭
	      
	        this.timeUsed = intent.getIntExtra("timeUsed", 0);//���˶��ٲ���
	     
	        String stopString = intent.getStringExtra("stop");
	      
	        this.setStopState(stopString);
    	}
		 this.updateText();
    	
    }
    
    public void showLevel(){
    	//��ʾ��ǰ���� 
    		Intent intent = new Intent(this,MessageActivity.class);
    		intent.putExtra("showState", MessageActivity.levelShow);
    		intent.putExtra("level", ""+this.levelInt);
    		this.startActivityForResult(intent,messageCode);
    	
    }
    public void gameStop(){
    	this.gameState = this.stop;
    	this.secondView.daoJiStop();
    	this.timeBar.jiShiStop();
    	this.resumeRun = false ;//��ʱ����Ҫ������
    }
    public void gameStart(){
    	this.gameState = this.running;
    	this.secondView.daoJiStart();
    	this.timeBar.jiShiStart();
    	this.resumeRun = true ;//��ʱ�ֿ�ʼ���Ա�����
    }
    public void undoInit(){
    	this.undoAble = false;
    	this.nowCatX = -1;
    	this.nowCatY = -1;
    	this.nowStopX = -1;
    	this.nowStopY = -1;
    }
    public void gameOver(int state){
    	//����
    	undoInit();//������Ϸ������Ӱ����һ��
    	//��ʱ��������ת������Ҫresume �� onstart
    	if(this.levelInt>5){
    		//��Ѱ棬�����弶��
    		this.finish();
    	}
    	if(state==this.failureOver){
    		this.overAble = true;//������Ϸ���������ٴ洢��Ϸ״̬
	    	this.gameStop();
	    	this.updateText();
	    	Intent intent = new Intent(this,MessageActivity.class);
	    	intent.putExtra("showState", MessageActivity.overShow);
	    	intent.putExtra("score", ""+this.scoreInt);
			this.startActivityForResult(intent,messageCode);
			//+++++++++++++++++
	    	int[] scoreList = this.getScoreList();//�Ѿ�����
	    	if(scoreList[9]<this.scoreInt){//������һ����
	    		scoreList[9] = this.scoreInt ;
	    	}
	    	//�洢
	    	SharedPreferences sp =this.getSharedPreferences("LastGame",this.MODE_PRIVATE);
			Editor editor = sp.edit();
			for(int i = 0;i<10;i++){
				editor.putInt("score"+i, scoreList[i]);
				}
			editor.commit();
		}else if(state==this.winOver){
			Log.d("state", "game win");
			this.gameStop();
			Intent intent = new Intent(this,MessageActivity.class);
	    	intent.putExtra("showState", MessageActivity.showWin);
			this.startActivityForResult(intent,messageCode);
			
		}else if(state==this.failure){
			this.gameStop();
			this.updateText();//����èͷ
			Intent intent = new Intent(this,MessageActivity.class);
	    	intent.putExtra("showState", MessageActivity.showFailure);
			this.startActivityForResult(intent,messageCode);
		}
    	
    }
    //view=======================
    public void updateText(){
    	//���Text��ʾ
    	this.level.setText(""+this.levelInt);
    	this.score.setText(""+this.scoreInt);
    	this.hscore.setText(""+this.hscoreInt);
    	this.lives.setText(this.livesInt);
    	//ˢ��
    	this.level.postInvalidate();
    	this.score.postInvalidate();
    	this.hscore.postInvalidate();
    	this.lives.postInvalidate();
    	//�м���� 	
    }
    public void catJump(){
    	//èѡ·
    	if(this.catDate.escape()){
			//����
			Log.d("gameState", "you failure!!");
			Music.playShort(this, R.raw.gamefail,false);
			this.livesInt--;
			if(this.livesInt>0){
				this.lives.postInvalidate();
				this.gameOver(this.failure);
			}else{
				this.gameOver(this.failureOver);
			} 
			
		}else{ 
			//��ǰè��λ��
			nowCatX = catDate.x;
			nowCatY = catDate.y;
			
			Cat.Direction nextArrow = this.catDate.nextJump();
			
			Log.d("weight", ""+this.catDate.getWeight());
			if(nextArrow==null){
			//Ӯ�� 
			    Log.d("gameState", "you win!!");
			    //��ȡ����ʱ��
			    this.gameOver(this.winOver);
			   
			}else{
				//���������è�Ķ���
				Log.d("gameState", "cat running!!");
				ItemXY newPlace = this.mapView.new ItemXY(this.catDate.x,this.catDate.y);
				switch(nextArrow){
				case left :this.mapView.catRunArrowAnimation(this.mapView.left,newPlace);break;
				case leftTop :this.mapView.catRunArrowAnimation(this.mapView.leftTop,newPlace) ;break;
				case rightTop :this.mapView.catRunArrowAnimation(this.mapView.rightTop,newPlace) ;break;
				case right :this.mapView.catRunArrowAnimation(this.mapView.right,newPlace) ;break;
				case rightDown :this.mapView.catRunArrowAnimation(this.mapView.rightDown,newPlace) ;break;
				case leftDown :this.mapView.catRunArrowAnimation(this.mapView.leftDown,newPlace) ;break;
				}
			}
			
		}
    }
    public void setStopState(String state){
    	int k = 0;
    	//��ͼ��������
		this.mapData.reinitMap();
		for(int i = 0;i<this.mapView.rowSum;i++)
			for(int j = 0;j<this.mapView.columnSum;j++,k++){
				int s = Integer.parseInt(""+state.charAt(k));
				//�ظ�mapviewͼ
				this.mapView.placeState[i][j] = s;	
				if(s==this.mapView.orStop||s==this.mapView.newStop){
					this.mapData.setStop(i, j);
				}
				//��ʼ��è��λ��
				if(s==this.mapView.catPlace){
					this.catDate = new Cat(this.mapData.map[i][j]);
					this.mapView.changeCatPlace(this.mapView.new ItemXY(i,j));	
				}
			}
    }
    
    private void createNewMap(int stopNum){//����һ���µĵ�ͼ�ϰ�
    	//��������ϰ���è
    	this.mapView.cleanMapStop();
    	this.mapData.reinitMap();
    	int sNum = 0;//��ǰ�ϰ���
    	while(sNum<stopNum){
    		//��ȡ�������
    		int x =(int) (Math.random()*10);
    		int y =(int) (Math.random()*10);
    		if(x==5){
    			if(y==5)//è��λ��
    				continue;
    			else if(y==4)//è����
    				continue;
    		}else if(x==4){
    			if(y==5)//è������
    				continue ;
    		}
    		//�����ϰ�
    		if(this.mapView.setStop(x, y, this.mapView.orStop)){
    			//�ϰ����óɹ�
    			this.mapData.setStop(x, y);
    			sNum ++;
    		}
    	}
    	//���èλ��
    	this.catDate = new Cat(this.mapData.map[5][5]);
    	this.mapView.changeCatPlace(this.mapView.new ItemXY(5,5));	
    
    }
    public void playLevelGame(){
    	this.timeBar.setTime(0);//��ǰ��Ϸʱ��
   	 	this.nowTurn = this.User;//�û��� 
	    this.timeUsed = 0;//�Ѿ�����0��
	    this.initGameMap(this.levelInt);//���ɵ�ͼ��Ϣ
    }
    
    public void showTimeUp(){
    	 Intent intent = new Intent(this,MessageActivity.class);
    	 intent.putExtra("showState", MessageActivity.timeUp);
    	 this.startActivityForResult(intent, messageCode);
    }
    public void showBoundAdd(){
    	 Intent intent = new Intent(this,MessageActivity.class);
    	 intent.putExtra("showState", MessageActivity.showBound);
    	 intent.putExtra("score", (this.timeBar.getMaxtime()-this.timeUsed)*this.linshiscore);
    	 this.startActivityForResult(intent, messageCode);
    }
    @Override
    protected void onResume(){
    	super.onResume();
    	if(resumeRun){
    		this.gameState = this.running;
        	this.secondView.daoJiStart();
        	this.timeBar.jiShiStart();
    	}
    	Music.playLong(this, R.raw.musicbg, true);
    	
    }
	@Override
	protected void onPause () {
		//��Ϸͣ��
	//	this.gameState = this.stop;
    	this.secondView.daoJiStop();
    	this.timeBar.jiShiStop();
    	//
		Music.stopLong();
		if(gameState==this.running&&this.overAble==false){//��Ϸ����ʱ���һ�ûʧ��ǰ�˳��ű���
			gameState = this.stop;
		SharedPreferences sp =this.getSharedPreferences("LastGame",this.MODE_PRIVATE);
		Editor editor = sp.edit();     
		editor.putString("stop", this.mapView.getNowStop());//�ϰ�
		editor.putInt("level",this.levelInt);//�ȼ� 
		editor.putInt("score", this.scoreInt);//����
		editor.putInt("lives", this.lives.getText());//������ 
		editor.putInt("nowTurn", this.nowTurn);//��ǰ��˭
		editor.putInt("stepUsed", this.timeUsed);//ʹ�ö��ٲ�
		editor.putInt("nowtime", this.timeBar.getTime());//��ʱ��
		editor.commit();
		}
		super.onPause();
	}
	public boolean onTouch(View arg0, MotionEvent arg1) {
		// TODO Auto-generated method stub
		if(Game.nowTurn==Game.User&&this.gameState==this.running){
			Log.d("state", "get point1 "+arg1.getX()+":"+arg1.getY());
			//user������
			ItemXY itemXY = this.mapView.getItemXY(arg1.getX(), arg1.getY());
			if(itemXY!=null){
				Place place = this.mapData.map[itemXY.x][itemXY.y];
				if(place.isStop()||place.isSame(this.catDate)){
					//�˴����ϰ���è�������������ϰ�
					return false ;
				}else{
					//User go
					this.nowStopX = place.x;
					this.nowStopY = place.y;
					this.undoAble = false;//�����ٵ����
					
					place.setStop();//������Ϊ�ϰ� 
					this.mapView.placeState[place.x][place.y] = this.mapView.newStop ;//ͼ�����ݸ���
					//CatTurn
					Game.nowTurn = Game.AI;
					this.secondView.daoJiStop();
					Music.playShort(this, R.raw.catstep, false);
					this.mapView.postInvalidate();//ͼ����
					this.catJump();
				}
			}
		}
		return false;
	}
	
	public void cancelAction(){
			if(undoAble){
				if(nowStopX!=-1&&nowStopY!=-1){//���߹���
					//��ͼ����
					Place place = this.mapData.map[nowStopX][nowStopY];
				    place.setPass();
				    //��ͼ��ʾ
				    this.mapView.placeState[place.x][place.y] = this.mapView.pass ;
			    }
				if(nowCatX!=-1&&nowCatY!=-1){
				    //èè����
				    this.catDate.changeBackTo(mapData.map[nowCatX][nowCatY]);
				    this.mapView.changeCatPlace( mapView.new ItemXY(nowCatX,nowCatY));
			    }
			    //���			    
				this.game.secondView.setNumber(9);
				this.mapView.postInvalidate();
				this.undoAble = false;//����undo
				this.nowTurn = Game.User;
				this.gameStart();
			}
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Music.playShort(this, R.raw.menubtn, false);
		switch(v.getId()){
		case R.id.menu:{
			this.gameStop();
			Intent intent = new Intent(this,GameMenu.class);
			this.startActivityForResult(intent, showMenu);
			
			}break;
		case R.id.undo:{
			Log.d("undo", ""+undoAble);
			if(undoAble&&this.nowTurn==this.User&&this.gameState==this.running){
				//��Ϸ�������У������û��ɶ������һ�û�������������
				this.gameStop();
				this.cancelAction();//ȡ���ϴβ���
			}
		}break;
		}
	}
	
	public void onSecondUp(){
		 this.nowStopX=-1;
		 this.nowStopY=-1; //�û�û���ƶ�
		 this.catJump();
	}
	public  int[] getScoreList(){
		SharedPreferences myP = this.getSharedPreferences("LastGame",this.MODE_PRIVATE);
		int[] scoreList = new int[10];
		for(int i = 0;i<10;i++){
			scoreList[i]=myP.getInt("score"+i, -1);
		}
		//ֱ��ð������
		for(int j = 0;j< 9;j++)
		for(int i= j+1;i<10;i++){
			if(scoreList[j]<scoreList[i]){
				int tem = scoreList[i];
				scoreList[i] =scoreList[j];
				scoreList[j] = tem;
			}
		}
		return scoreList ;
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
            Intent data) {
        if (requestCode == Game.messageCode) {
            if (resultCode == MessageActivity.overShow) {
                //the show message Activiyt finish
            	Intent intent = new Intent(this,MessageActivity.class);
    			intent.putExtra("showState", MessageActivity.showTop);
    			this.startActivityForResult(intent, Game.messageCode);	
            }
            if (resultCode == MessageActivity.levelShow) {
            	//����ʱ��ʼ����
            	this.playLevelGame();      
            	this.gameStart();
            }
            if (resultCode == MessageActivity.timeUp) {
            	Music.playShort(this, R.raw.gamefail,false);
    			this.livesInt--;
    			if(this.livesInt>0){
    				this.lives.postInvalidate();
    				this.gameOver(this.failure);
    			}else{
    				this.gameOver(this.failureOver);
    			}          
            }
            if (resultCode == MessageActivity.showBound) {
            	this.gameStop();
            	//��ȡ����
    			this.scoreInt +=(this.timeBar.getMaxtime()-this.timeUsed)*this.linshiscore;
    			this.updateText();//�÷�����ǰˢ��
    			this.levelInt ++;//������һ 
    			this.showLevel();	
    			      
            }
            if(resultCode == MessageActivity.showTop){
            	this.finish();
            }
            if(resultCode == MessageActivity.showWin){
            	this.gameStop();
            	this.timeUsed=this.timeBar.getTimeAnimation();
            	
            }
            if(resultCode == MessageActivity.showFailure){
            	//��ȡ����
    			this.levelInt ++;//������һ 
    			this.showLevel();	
    		
            }
        }else if(requestCode == Game.showMenu){
        	switch(resultCode){
        	case GameMenu.resumeB:{
        		//������Ϸ
        		this.gameStart();
        	}break;
        	case GameMenu.playB:{
        		//���¿�ʼ
        		this.setResult(GameMenu.playB);
        		this.finish();//�˳���������
        	}break;
        	case GameMenu.exitB:{
        		//�����˳�
        		this.setResult(GameMenu.exitB);
        		this.finish();
        	}break;
        	}
        }
    }

	
}