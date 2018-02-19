package spirit.parttime.cat.logic;

import android.util.Log;
import spirit.parttime.cat.Game;

public class Cat extends Place {
	public static  enum Direction{left,leftTop,rightTop,right,rightDown,leftDown};
	public  final int everyAdd = 6;
	public Cat(Place place) {
		// TODO Auto-generated constructor stub
		super();
		//设置当前猫的位置
		changePlaceTo(place);
	}
	//public
	public int getCatWeight(){
		return this.weight ;
	}
	public int getDirectionWeight(Direction d){
		switch(d){
		case left :return this.left.weight ;
		case leftTop :return this.leftTop.weight ;
		case rightTop :return this.rightTop.weight ;
		case right :return this.right.weight ;
		case rightDown :return this.rightDown.weight ;
		case leftDown :return this.leftDown.weight ;
		}
		return -1 ;
	}
	public Direction nextJump(){
		
		//每次只是选择最小权值的方向来跳
	/*	int i = (int)(Math.random()*5);
		Direction min = Direction.left ;
		switch(i){
		case 0:min = Direction.left ;break;
		case 1:min = Direction.leftTop ;break;
		case 2:min = Direction.leftDown ;break;
		case 3:min = Direction.right ;break;
		case 4:min = Direction.rightTop ;break;
		case 5:min = Direction.rightDown ;break;
		}
		if(this.getDirectionWeight(min)>this.leftTop.weight){
			min = Direction.leftTop;
		}
		if(this.getDirectionWeight(min)>this.rightTop.weight){
			min = Direction.rightTop;
		}
		if(this.getDirectionWeight(min)>this.right.weight){
			min = Direction.right;
		}
		if(this.getDirectionWeight(min)>this.rightDown.weight){
			min = Direction.rightDown;
		}
		if(this.getDirectionWeight(min)>this.leftDown.weight){
			min = Direction.leftDown;
		}
		if(this.getDirectionWeight(min)>this.left.weight){
			min = Direction.left;
		}
		//判断小猫是否可跳
		if(this.getDirectionWeight(min)<this.MaxWeight){
			//可跳+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
			//min = Direction.right;
			this.jump(min);
			return min ;
		}else{
			//null代表小猫被困死，无法逃脱
			return null ;
		}*/
		Cat.Direction min = Game.game.mapData.findCatNextJump(this);
		if(min==null){
			return null ;//猫被困死
		}else{
			this.jump(min);//跳
			return min;
		}
	}
	
	public int getSmaller(Place now,int time,int weight){
		//当前能找到的最小方向
		if(now!=null&&now.weight==0){//胜利位置
			return time;//第几次找到
		}
		if(time > 6){
			return 6;
		}
		if(now.weight<weight){
			//只有碰到小的继续搜索
			int left = getSmaller(now.left,time +1 ,now.weight);
			int leftTop = getSmaller(now.leftTop,time +1 ,now.weight);
			int leftDown = getSmaller(now.leftDown,time +1 ,now.weight);
			int right = getSmaller(now.right,time +1 ,now.weight);
			int rightTop = getSmaller(now.rightTop,time +1 ,now.weight);
			int rightDown = getSmaller(now.rightDown,time +1 ,now.weight);
			
			int timeUse = left>leftTop?leftTop:left;
			timeUse = timeUse>leftDown?leftDown:timeUse;
			timeUse = timeUse>right?right:timeUse;
			timeUse = timeUse>rightTop?rightTop:timeUse;
			timeUse = timeUse>rightDown?rightDown:timeUse;
			return timeUse ;
		}else{
			//碰到大的直接返回最大值
			return this.MaxWeight;
		}
		
	}
	public boolean escape(){
		//走到权值为0 则到达最外层
	/*	if(this.weight==this.everyAdd){//最小了
			return true ;
		}else{
			return false ;
		}*/
		return this.isExit();
	}
	//private
	private boolean jump(Direction d){
		//选择方向，判断是否可跳
		
		switch(d){
			case left:{
					if(this.left==null){
						return false ;
					}else{
						this.changePlaceTo(this.left);
					}
				}break;
			case leftTop:{
				if(this.leftTop==null){
					return false ;
				}else{
					this.changePlaceTo(this.leftTop);
				}
			}break;
			case leftDown:{
				if(this.leftDown==null){
					return false ;
				}else{
					this.changePlaceTo(this.leftDown);
				}
			}break;
			case right:{
				if(this.right==null){
					return false ;
				}else{
					this.changePlaceTo(this.right);
				}
			}break;
			case rightDown:{
				if(this.rightDown==null){
					return false ;
				}else{
					this.changePlaceTo(this.rightDown);
				}
			}break;
			case rightTop:{
				if(this.rightTop==null){
					return false ;
				}else{
					this.changePlaceTo(this.rightTop);
				}
			}break;
		}
		return true;
	}
	private void changePlaceTo(Place place){
	//	place.weight += this.everyAdd;
		//当前位置跟换到place
		this.weight = place.weight;
		this.left = place.left;
		this.leftDown = place.leftDown;
		this.leftTop = place.leftTop;
		this.right = place.right;
		this.rightDown = place.rightDown;
		this.rightTop = place.rightTop;
		this.x = place.x;
		this.y = place.y;
	}
	public void changeBackTo(Place place){
	//	this.weight -= this.everyAdd;
		//当前位置跟换到place
		this.weight = place.weight;
		this.left = place.left;
		this.leftDown = place.leftDown;
		this.leftTop = place.leftTop;
		this.right = place.right;
		this.rightDown = place.rightDown;
		this.rightTop = place.rightTop;
		this.x = place.x;
		this.y = place.y;
	}
}
