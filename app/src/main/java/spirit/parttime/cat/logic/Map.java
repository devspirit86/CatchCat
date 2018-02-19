package spirit.parttime.cat.logic;

import android.util.Log;

public class Map{
	//地图具体数据11*11
	public final int MaxNum ;
	public Place[][] map ;
	public final int center ;
	
//==========地图初始化
	public Map(int maxnum){
		//初始化
		this.MaxNum = maxnum ;
		this.map = new Place[MaxNum][MaxNum];
		this.center = MaxNum/2 ;
		//初始化并且设置按距离权值
		for(int i = 0 ;i<MaxNum ;i++){
			for(int j= 0;j<MaxNum;j++){
			map[i][j] = new Place(getWeightTo(i, j),i,j);
			}
		}
		//设置地图格子结构
		for(int i = 0 ;i<MaxNum;i++){
			int top = i-1 ;
			int down = i+1 ;
			for(int j= 0;j<MaxNum;j++){
				int left = j-1;
				int right = j+1;		
				if(i%2==0){
					//偶数行
					if(left>=0)
						map[i][j].left = map[i][left];
					if(right<MaxNum)
						map[i][j].right = map[i][right];
					if(top>=0&&left>=0)
						map[i][j].leftTop = map[top][left];
					if(top>=0)
						map[i][j].rightTop = map[top][j];
					if(down<MaxNum&&left>=0)
						map[i][j].leftDown = map[down][left];
					if(down<MaxNum)
						map[i][j].rightDown = map[down][j];
				}else{
					//奇数行
					//偶数行
					if(left>=0)
						map[i][j].left = map[i][left];
					if(right<MaxNum)
						map[i][j].right = map[i][right];
					if(top>=0)
						map[i][j].leftTop = map[top][j];
					if(top>=0&&right<MaxNum)
						map[i][j].rightTop = map[top][right];
					if(down<MaxNum)
						map[i][j].leftDown = map[down][j];
					if(down<MaxNum&&right<MaxNum)
						map[i][j].rightDown = map[down][right];
				}
			}
		}
		//初始化出口列表
		initFind();
	}
	//设置地图障碍物========================
	public boolean setStop(int x,int y){
		if(this.map[x][y].isStop()){
			//已经是障碍物
			return false ;
		}
		this.map[x][y].setStop();
		return true ;
	} 
	//地图按位置重初始化=====================
	public void reinitMap(){
		for(int i = 0 ;i<MaxNum ;i++){
			for(int j= 0;j<MaxNum;j++){
			map[i][j].setWeight(getWeightTo(i, j));
			}
		}
	}
	//获取该位置权值========================
	private  int getWeightTo(int x , int y){
		//x方向偏移
		if(x > center){
			x = x-center ;
		}else{
			x = center-x ;
		}
		//y方向偏移
		if(y > center){
			y = y-center ;
		}else{
			y = center-y ;
		}
		//获取权值
		if(x>y){
			return (center-x) ;
			}
		else{
			return (center-y);
		}
		
	}
/*	public static void main(String args[]){
		//test
		Map.initMap();
		Place place = Map.map[5][5];
		System.out.println("place 位置 :("+place.x+","+place.y+") weight:"+place.weight);
		if(place.leftTop!=null)
		System.out.println("place 左上 :("+place.leftTop.x+","+place.leftTop.y+") weight:"+place.leftTop.weight);
		if(place.leftDown!=null)
		System.out.println("place 左下 :("+place.leftDown.x+","+place.leftDown.y+") weight:"+place.leftDown.weight);
		if(place.left!=null)
		System.out.println("place 左 :("+place.left.x+","+place.left.y+") weight:"+place.left.weight);
		if(place.rightTop!=null)
		System.out.println("place 右上 :("+place.rightTop.x+","+place.rightTop.y+") weight:"+place.rightTop.weight);
		if(place.right!=null)
		System.out.println("place 右 :("+place.right.x+","+place.right.y+") weight:"+place.right.weight);
		if(place.rightDown!=null)
		System.out.println("place 右下 :("+place.rightDown.x+","+place.rightDown.y+") weight:"+place.rightDown.weight);
		
	}*/
	public Place[][] getMap() {
		return map;
	}
	public int getMaxNum() {
		return MaxNum;
	}
	public int getCenter() {
		return center;
	}
	//+++++++++++++++++++++++++++猫的寻路算法+++++++++++++++++++++++++++++++++++++++=
	private Place[] endSet = new Place[40] ;//40个出口 
	private int step ;//使用的最小步数，刚刚开始为一个最大值
	private int useStep ;//当前使用的步数
	private Cat.Direction lastArrow  ;//最后一次的方向
	private Place nowPlace ;//当前的位置
	//int nowWeight;//当前权值
	//int stop = ;//障碍
	//int pass ;//可通
	//int end ;//钟点
	private Place cat ;//猫的位置
	private final int maxStep = 7 ;//最大步数
	
	private void initFind(){
		int k = 0;//当前结果集
		int i1 = 1;//第一行从 1 ~ 9；
		int i2 = 9;//最后一行从  9~ 1；
		int j1 = 1;//左列 从 1~ 9；
		int j2 = 9 ;//右列 从 9~1；
		while(k<endSet.length-4){
			if(i1<=9){//第一行
				endSet[k++] = this.map[0][i1++];
			}
			if(i2>=1){
				endSet[k++] = this.map[10][i2--];
			}
			if(j1<=9){
				endSet[k++] = this.map[j1++][0];
			}
			if(j2>=1){
				endSet[k++] = this.map[j2--][10];
			}
			
		}
		endSet[k++] = this.map[0][0];
		endSet[k++] = this.map[10][10];
		endSet[k++] = this.map[0][10];
		endSet[k++] = this.map[10][0];
			Log.d("break", ""+k);
	   /*for(int i = 0;i<endSet.length;i++){
			Log.d("break", endSet[i].x+","+endSet[i].y);
		}
		//瞬时针
		
		for(int i = 0 ,j = 0 ; i<11 ;i++,j++){
			endSet[j] = this.map[0][i];//第一行（0，10）
		}
		for(int i = 0 ,j = 10 ; i<11 ;i++,j++){
			endSet[j] = this.map[i][10];//右列（10，20）
		}
		for(int i = 10 ,j = 20 ; i>=0 ;i--,j++){
			endSet[j] = this.map[10][i];//最后一行（20，30）
		}
		for(int i = 10 ,j = 30 ; i>0 ;i--,j++){//1,1 的位置已经出现过
			endSet[j] = this.map[i][0];//左列（30，39）
		}*/
	}
	private boolean find(Place now){//从现在开始找一次比一次 找的 步数少
		useStep ++;//又走了一步
		if(now==null){
			useStep --;
			return false ;//越界了
		}
		if(useStep>=step-1){//已经超过了最小步数就没有必要了
			useStep --;
			return false ;
		}
		
		if(now.isExit()&&useStep!=0){//如果是第一个位置不参与考虑
			useStep --;
			return false ;//又走回终点 肯定不是最优的算法。 
		}
		if(now.isStop()){//遇到障碍返回
			useStep --;
			return false ;
		}
		//找到的情况
		if(now.left!=null&&now.left.isSame(cat)){
			step = useStep + 1;//下一步找到
			lastArrow = Cat.Direction.left ;
			useStep -- ;//返回上一步
		//	Log.d("break", "("+now.x+","+now.y+")left find "+step);
			return true ;
		}
		if(now.leftTop!=null&&now.leftTop.isSame(cat)){
			step = useStep + 1;//下一步找到
			lastArrow = Cat.Direction.leftTop ;
			useStep -- ;//返回上一步
		//	Log.d("break", "("+now.x+","+now.y+")leftTop find "+step);
			return true ;
		}
		if(now.leftDown!=null&&now.leftDown.isSame(cat)){
			step = useStep + 1;//下一步找到
			lastArrow = Cat.Direction.leftDown ;
			useStep -- ;//返回上一步
		//	Log.d("break", "("+now.x+","+now.y+")leftDown find "+step);
			return true ;
		}
		if(now.right!=null&&now.right.isSame(cat)){
			step = useStep + 1;//下一步找到
			lastArrow = Cat.Direction.right ;
			useStep -- ;//返回上一步
		//	Log.d("break", "("+now.x+","+now.y+")right find "+step);
			return true ;
		}
		if(now.rightTop!=null&&now.rightTop.isSame(cat)){
			step = useStep + 1;//下一步找到
			lastArrow = Cat.Direction.rightTop ;
			useStep -- ;//返回上一步
		//	Log.d("break", "("+now.x+","+now.y+")rightTop find "+step);
			return true ;
		}
		if(now.rightDown!=null&&now.rightDown.isSame(cat)){
			step = useStep + 1;//下一步找到
			lastArrow = Cat.Direction.rightDown ;
			useStep -- ;//返回上一步
			//Log.d("break", "("+now.x+","+now.y+")rightDown find "+step);
			return true ;
		}
		
		//===========下一步了 要，可以加入随机
		//选择进入方向
		Cat.Direction[] d = Cat.Direction.values();
		boolean isFind = false ;
		for(int i = 0; i<d.length;i++){
			if(find(now.getArrowPlace(d[i]))){//试探该方向
				isFind = true ;
				if(step==now.weight){//这个肯定属于最短了
			//		Log.d("break", now.x+":"+now.y);
					return true ;
				}
			}
		}
		if(isFind){
			return true ;
		}
		//6个方向的纵深还是没有找到猫
		
		useStep --;
		return false ;
	}
	private boolean findCat(){
		//初始化
		boolean canFind = false ;
		this.step = maxStep ;//最小步伐为最大值
		for(int i = 0;i<endSet.length;i++){
			this.useStep = -1;//每次寻找重新归-1，第一位置不算
			if(find(endSet[i])){//当前出口可以找到猫
				canFind = true ;
			}
		}
		return canFind ;
	}
	public Cat.Direction findCatNextJump(Place catPlace){//返回下一跳的位置
		this.cat = catPlace ;//当前猫的位置
		this.lastArrow = null ;//开始时就设为空
		boolean canJump = false ;//可跳否
		canJump = findCat();//寻找猫
		Log.d("arrow","find"+lastArrow);
		if(canJump){
			//存在一条路劲
			switch(this.lastArrow){//返回反方向 作为输出
			case left : return Cat.Direction.right ;
			case leftTop : return Cat.Direction.rightDown ;
			case leftDown : return Cat.Direction.rightTop ;
			case right : return Cat.Direction.left ;
			case rightTop : return Cat.Direction.leftDown ;
			case rightDown : return Cat.Direction.leftTop ;
			}
		}
		//没有出路了，猫在等死++++++++++++++++++++++++++++++++++++++++++
		if(cat.left!=null&&!cat.left.isStop()){//非空非阻碍
			return Cat.Direction.left ;
		}
		if(cat.leftTop!=null&&!cat.leftTop.isStop()){//非空非阻碍
			return Cat.Direction.leftTop ;
		}
		if(cat.leftDown!=null&&!cat.leftDown.isStop()){//非空非阻碍
			return Cat.Direction.leftDown ;
		}
		if(cat.right!=null&&!cat.right.isStop()){//非空非阻碍
			return Cat.Direction.right ;
		}
		if(cat.rightTop!=null&&!cat.rightTop.isStop()){//非空非阻碍
			return Cat.Direction.rightTop ;
		}
		if(cat.rightDown!=null&&!cat.rightDown.isStop()){//非空非阻碍
			return Cat.Direction.rightDown ;
		}
		//.......
		//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		return null ;//无路可走
	}
}
