package spirit.parttime.cat.logic;

import android.util.Log;
import spirit.parttime.cat.Game;

public class Cat extends Place {
	public static  enum Direction{left,leftTop,rightTop,right,rightDown,leftDown};
	public  final int everyAdd = 6;
	public Cat(Place place) {
		// TODO Auto-generated constructor stub
		super();
		//���õ�ǰè��λ��
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
		
		//ÿ��ֻ��ѡ����СȨֵ�ķ�������
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
		//�ж�Сè�Ƿ����
		if(this.getDirectionWeight(min)<this.MaxWeight){
			//����+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
			//min = Direction.right;
			this.jump(min);
			return min ;
		}else{
			//null����Сè���������޷�����
			return null ;
		}*/
		Cat.Direction min = Game.game.mapData.findCatNextJump(this);
		if(min==null){
			return null ;//è������
		}else{
			this.jump(min);//��
			return min;
		}
	}
	
	public int getSmaller(Place now,int time,int weight){
		//��ǰ���ҵ�����С����
		if(now!=null&&now.weight==0){//ʤ��λ��
			return time;//�ڼ����ҵ�
		}
		if(time > 6){
			return 6;
		}
		if(now.weight<weight){
			//ֻ������С�ļ�������
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
			//�������ֱ�ӷ������ֵ
			return this.MaxWeight;
		}
		
	}
	public boolean escape(){
		//�ߵ�ȨֵΪ0 �򵽴������
	/*	if(this.weight==this.everyAdd){//��С��
			return true ;
		}else{
			return false ;
		}*/
		return this.isExit();
	}
	//private
	private boolean jump(Direction d){
		//ѡ�����ж��Ƿ����
		
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
		//��ǰλ�ø�����place
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
		//��ǰλ�ø�����place
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
