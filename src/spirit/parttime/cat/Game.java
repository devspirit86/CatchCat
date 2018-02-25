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
	private MyMapView mapView = null ;//主界面
	private MyTextView level = null ;//等级
	private MyTextView score = null ;//分数
	private MyLivesTextView lives = null ;//命
	private MyTextView hscore = null ;//最高分数
	private MyImageButton menu = null ;//菜单按钮
	private MyImageButton undo = null ;//暂停按钮
	public SecondView secondView = null;//倒计时
	public static Game game = null ;//游戏本身
	private TimeBarView timeBar = null ;//时间条
	//=================全局状态============================= 
	//表示resume时 要运行倒计时
	//通过gameStop（）函数暂停的游戏，只能通过gameStart（）来恢复，不能被resume打断
	//而非gameStop（）暂停的游戏，可以通过onresume来恢复
	public  boolean resumeRun ;
	public static final int AI = 1;
	public static final int User = 0;
	public static int nowTurn = User ;
	//==================Game State========================
	private int levelInt=0,scoreInt =0 ,livesInt =3 ,hscoreInt= 0;
	static final int stop = 0 ,running = 1;//游戏是否running
	public final int failureOver = 0 , winOver = 1,failure=2;//over的状态
	public static int gameState = running ;
	public  boolean overAble = false ;
	private MyMapView.ItemXY catXY = null ;//当前猫的位置
	private MyMapView.ItemXY[] orStopSet = null ;//原始障碍物位置集合
	public  Map mapData = null ;//地图数据信息
	private Cat catDate = null ;//猫的数据信息
	private int timeUsed =0;//用户已经花了多少时间
	private int linshiscore =0 ;//临时基数
	private int[] stopLevel ;
	//===================Undo===============================
	private int nowStopX = -1 ,nowStopY= -1;//当前障碍点
	private int nowCatX = -1 , nowCatY = -1;//当前猫的坐标点
    public static boolean undoAble = false ;
	//==================线程===============================
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
        resumeRun = true;//每次重开时都为true；
        //数据结构
        this.mapData = new Map(this.mapView.rowSum);
        this.catDate = new Cat(this.mapData.map[mapData.center][mapData.center]);
		this.mapView.setOnTouchListener(this);	
		this.game = this;
		//=======================启动======================================
		this.startGame();
    }
    //游戏状态
    public void initGameMap(int level){
    	if(level<=10){
    		//前10关 
    		this.stopLevel = this.getResources().getIntArray(R.array.stopin10level);
    	}else if(level>20){
    		//20关后
    		this.stopLevel = this.getResources().getIntArray(R.array.out20);
    	}else{
    		this.stopLevel = this.getResources().getIntArray(R.array.stop10to20);
    	}
    	//默认一步步取 
    	int stopNum = this.stopLevel[level%10];
    	this.createNewMap(stopNum);
    	//初始化文字信息
    	this.mapView.resetCatArrow();//重新设置猫的位置
    	this.updateText();
    }
    
    public void startGame(){
    	//获取游戏状态
    	Intent intent = this.getIntent();
		int gameState = intent.getIntExtra("gamestate", Menu.newGame);	
		this.linshiscore = intent.getIntExtra("linshiscore", -1);//临时基数
		//获取最高分数
		int[] scoreList = this.getScoreList();
		if(scoreList[0]>=0){
			this.hscoreInt = scoreList[0];//最高分数
        }else{
        	this.hscoreInt = 0;
        }
        this.livesInt = intent.getIntExtra("lives", -1) ;//当前命数
        this.timeBar.setMaxtime(intent.getIntExtra("totaltime", 0));//游戏总时间
        this.secondView.setNumber(intent.getIntExtra("parttime", 0));//每一部的时间
		if(gameState==Menu.newGame){
			//重新开始游戏
			this.levelInt = 1;//第一关 
	        this.scoreInt = 0 ;//分数为0
	        this.timeBar.setTime(0);//当前游戏时间
	        this.nowTurn = this.User;//用户先 
	        this.timeUsed = 0;//已经走了0步
	        this.mapView.cleanMapStop();
	    	this.mapData.reinitMap();
	    	//添加猫位置
	  //  	this.catDate = new Cat(this.mapData.map[5][5]);
	   // 	this.mapView.changeCatPlace(this.mapView.new ItemXY(5,5));	
			this.initGameMap(this.levelInt);//生成地图信息
	        this.updateText();
	        return ;
    	}else if(gameState==Menu.reStart){
    		//上一次游戏
			this.levelInt = intent.getIntExtra("level", -1);//第一关
			
	        this.scoreInt = intent.getIntExtra("score", 0);//分数为0
	       
	        this.timeBar.setTime(intent.getIntExtra("nowtime",0));//当前游戏时间
	       
	        this.nowTurn = intent.getIntExtra("nowTurn", this.User);//当前到谁
	      
	        this.timeUsed = intent.getIntExtra("timeUsed", 0);//花了多少步了
	     
	        String stopString = intent.getStringExtra("stop");
	      
	        this.setStopState(stopString);
    	}
		 this.updateText();
    	
    }
    
    public void showLevel(){
    	//显示当前关数 
    		Intent intent = new Intent(this,MessageActivity.class);
    		intent.putExtra("showState", MessageActivity.levelShow);
    		intent.putExtra("level", ""+this.levelInt);
    		this.startActivityForResult(intent,messageCode);
    	
    }
    public void gameStop(){
    	this.gameState = this.stop;
    	this.secondView.daoJiStop();
    	this.timeBar.jiShiStop();
    	this.resumeRun = false ;//这时候不需要被唤醒
    }
    public void gameStart(){
    	this.gameState = this.running;
    	this.secondView.daoJiStart();
    	this.timeBar.jiShiStart();
    	this.resumeRun = true ;//此时又开始可以被唤醒
    }
    public void undoInit(){
    	this.undoAble = false;
    	this.nowCatX = -1;
    	this.nowCatY = -1;
    	this.nowStopX = -1;
    	this.nowStopY = -1;
    }
    public void gameOver(int state){
    	//结束
    	undoInit();//避免游戏结束后，影响下一关
    	//此时的所有跳转都不需要resume 后 onstart
    	if(this.levelInt>5){
    		//免费版，大于五级就
    		this.finish();
    	}
    	if(state==this.failureOver){
    		this.overAble = true;//告诉游戏结束，不再存储游戏状态
	    	this.gameStop();
	    	this.updateText();
	    	Intent intent = new Intent(this,MessageActivity.class);
	    	intent.putExtra("showState", MessageActivity.overShow);
	    	intent.putExtra("score", ""+this.scoreInt);
			this.startActivityForResult(intent,messageCode);
			//+++++++++++++++++
	    	int[] scoreList = this.getScoreList();//已经排序
	    	if(scoreList[9]<this.scoreInt){//跟最后的一个比
	    		scoreList[9] = this.scoreInt ;
	    	}
	    	//存储
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
			this.updateText();//减少猫头
			Intent intent = new Intent(this,MessageActivity.class);
	    	intent.putExtra("showState", MessageActivity.showFailure);
			this.startActivityForResult(intent,messageCode);
		}
    	
    }
    //view=======================
    public void updateText(){
    	//左边Text显示
    	this.level.setText(""+this.levelInt);
    	this.score.setText(""+this.scoreInt);
    	this.hscore.setText(""+this.hscoreInt);
    	this.lives.setText(this.livesInt);
    	//刷新
    	this.level.postInvalidate();
    	this.score.postInvalidate();
    	this.hscore.postInvalidate();
    	this.lives.postInvalidate();
    	//中间格子 	
    }
    public void catJump(){
    	//猫选路
    	if(this.catDate.escape()){
			//输了
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
			//当前猫的位置
			nowCatX = catDate.x;
			nowCatY = catDate.y;
			
			Cat.Direction nextArrow = this.catDate.nextJump();
			
			Log.d("weight", ""+this.catDate.getWeight());
			if(nextArrow==null){
			//赢了 
			    Log.d("gameState", "you win!!");
			    //获取所用时间
			    this.gameOver(this.winOver);
			   
			}else{
				//继续玩更新猫的动画
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
    	//地图数据重启
		this.mapData.reinitMap();
		for(int i = 0;i<this.mapView.rowSum;i++)
			for(int j = 0;j<this.mapView.columnSum;j++,k++){
				int s = Integer.parseInt(""+state.charAt(k));
				//回复mapview图
				this.mapView.placeState[i][j] = s;	
				if(s==this.mapView.orStop||s==this.mapView.newStop){
					this.mapData.setStop(i, j);
				}
				//初始化猫的位置
				if(s==this.mapView.catPlace){
					this.catDate = new Cat(this.mapData.map[i][j]);
					this.mapView.changeCatPlace(this.mapView.new ItemXY(i,j));	
				}
			}
    }
    
    private void createNewMap(int stopNum){//生成一个新的地图障碍
    	//清空所有障碍与猫
    	this.mapView.cleanMapStop();
    	this.mapData.reinitMap();
    	int sNum = 0;//当前障碍数
    	while(sNum<stopNum){
    		//获取随机坐标
    		int x =(int) (Math.random()*10);
    		int y =(int) (Math.random()*10);
    		if(x==5){
    			if(y==5)//猫的位置
    				continue;
    			else if(y==4)//猫的左
    				continue;
    		}else if(x==4){
    			if(y==5)//猫的正上
    				continue ;
    		}
    		//设置障碍
    		if(this.mapView.setStop(x, y, this.mapView.orStop)){
    			//障碍设置成功
    			this.mapData.setStop(x, y);
    			sNum ++;
    		}
    	}
    	//添加猫位置
    	this.catDate = new Cat(this.mapData.map[5][5]);
    	this.mapView.changeCatPlace(this.mapView.new ItemXY(5,5));	
    
    }
    public void playLevelGame(){
    	this.timeBar.setTime(0);//当前游戏时间
   	 	this.nowTurn = this.User;//用户先 
	    this.timeUsed = 0;//已经走了0步
	    this.initGameMap(this.levelInt);//生成地图信息
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
		//游戏停顿
	//	this.gameState = this.stop;
    	this.secondView.daoJiStop();
    	this.timeBar.jiShiStop();
    	//
		Music.stopLong();
		if(gameState==this.running&&this.overAble==false){//游戏运行时并且还没失败前退出才保存
			gameState = this.stop;
		SharedPreferences sp =this.getSharedPreferences("LastGame",this.MODE_PRIVATE);
		Editor editor = sp.edit();     
		editor.putString("stop", this.mapView.getNowStop());//障碍
		editor.putInt("level",this.levelInt);//等级 
		editor.putInt("score", this.scoreInt);//分数
		editor.putInt("lives", this.lives.getText());//生命数 
		editor.putInt("nowTurn", this.nowTurn);//当前到谁
		editor.putInt("stepUsed", this.timeUsed);//使用多少步
		editor.putInt("nowtime", this.timeBar.getTime());//总时间
		editor.commit();
		}
		super.onPause();
	}
	public boolean onTouch(View arg0, MotionEvent arg1) {
		// TODO Auto-generated method stub
		if(Game.nowTurn==Game.User&&this.gameState==this.running){
			Log.d("state", "get point1 "+arg1.getX()+":"+arg1.getY());
			//user运行中
			ItemXY itemXY = this.mapView.getItemXY(arg1.getX(), arg1.getY());
			if(itemXY!=null){
				Place place = this.mapData.map[itemXY.x][itemXY.y];
				if(place.isStop()||place.isSame(this.catDate)){
					//此处有障碍与猫，不能再设置障碍
					return false ;
				}else{
					//User go
					this.nowStopX = place.x;
					this.nowStopY = place.y;
					this.undoAble = false;//不能再点击了
					
					place.setStop();//数据设为障碍 
					this.mapView.placeState[place.x][place.y] = this.mapView.newStop ;//图中数据跟新
					//CatTurn
					Game.nowTurn = Game.AI;
					this.secondView.daoJiStop();
					Music.playShort(this, R.raw.catstep, false);
					this.mapView.postInvalidate();//图更新
					this.catJump();
				}
			}
		}
		return false;
	}
	
	public void cancelAction(){
			if(undoAble){
				if(nowStopX!=-1&&nowStopY!=-1){//人走过的
					//地图数据
					Place place = this.mapData.map[nowStopX][nowStopY];
				    place.setPass();
				    //地图显示
				    this.mapView.placeState[place.x][place.y] = this.mapView.pass ;
			    }
				if(nowCatX!=-1&&nowCatY!=-1){
				    //猫猫数据
				    this.catDate.changeBackTo(mapData.map[nowCatX][nowCatY]);
				    this.mapView.changeCatPlace( mapView.new ItemXY(nowCatX,nowCatY));
			    }
			    //秒表			    
				this.game.secondView.setNumber(9);
				this.mapView.postInvalidate();
				this.undoAble = false;//不能undo
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
				//游戏处于运行，并且用户可动，并且还没撤销过的情况下
				this.gameStop();
				this.cancelAction();//取消上次操作
			}
		}break;
		}
	}
	
	public void onSecondUp(){
		 this.nowStopX=-1;
		 this.nowStopY=-1; //用户没有移动
		 this.catJump();
	}
	public  int[] getScoreList(){
		SharedPreferences myP = this.getSharedPreferences("LastGame",this.MODE_PRIVATE);
		int[] scoreList = new int[10];
		for(int i = 0;i<10;i++){
			scoreList[i]=myP.getInt("score"+i, -1);
		}
		//直接冒泡排列
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
            	//倒计时开始运作
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
            	//获取分数
    			this.scoreInt +=(this.timeBar.getMaxtime()-this.timeUsed)*this.linshiscore;
    			this.updateText();//让分数提前刷新
    			this.levelInt ++;//关数加一 
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
            	//获取分数
    			this.levelInt ++;//关数加一 
    			this.showLevel();	
    		
            }
        }else if(requestCode == Game.showMenu){
        	switch(resultCode){
        	case GameMenu.resumeB:{
        		//继续游戏
        		this.gameStart();
        	}break;
        	case GameMenu.playB:{
        		//重新开始
        		this.setResult(GameMenu.playB);
        		this.finish();//退出后，再重启
        	}break;
        	case GameMenu.exitB:{
        		//彻底退出
        		this.setResult(GameMenu.exitB);
        		this.finish();
        	}break;
        	}
        }
    }

	
}