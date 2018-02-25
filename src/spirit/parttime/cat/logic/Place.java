package spirit.parttime.cat.logic;

public class Place {
	//6 个方向
	protected Place left = null ;
	protected Place leftTop = null;
	protected Place rightTop = null;
	protected Place right = null;
	protected Place rightDown = null;
	protected Place leftDown = null;
	protected final int MaxWeight = 10000; 
	//当前位置
	protected int weight = 0;
	public int x = 0 ;//当前在矩阵中横坐标
	public int y = 0 ;//当前在矩阵中纵坐标
	//构造函数
	public Place(){
	
	}
	public Place(int x, int y){
		this.x = x ;
		this.y = y ;
	}
	public Place(int weight ,int x , int y){
		//初始化
		this.weight = weight;
		this.x = x ;
		this.y = y ;
	}
	//set and get
	public Place getLeft() {
		return left;
	}
	public void setLeft(Place left) {
		this.left = left;
	}
	public Place getLeftTop() {
		return leftTop;
	}
	public void setLeftTop(Place leftTop) {
		this.leftTop = leftTop;
	}
	public Place getRightTop() {
		return rightTop;
	}
	public void setRightTop(Place rightTop) {
		this.rightTop = rightTop;
	}
	public Place getRight() {
		return right;
	}
	public void setRight(Place right) {
		this.right = right;
	}
	public Place getRightDown() {
		return rightDown;
	}
	public void setRightDown(Place rightDown) {
		this.rightDown = rightDown;
	}
	public Place getLeftDown() {
		return leftDown;
	}
	public void setLeftDown(Place leftDown) {
		this.leftDown = leftDown;
	}
	public int getWeight() {
		return weight;
	}
	public void setWeight(int weight) {
		this.weight = weight;
	}
	//设置障碍====================
	public void setStop(){
		if(!isStop())
		this.setWeight(this.getWeight()+this.MaxWeight);
	}
	public void setPass(){
		if(isStop())
		this.setWeight(this.getWeight()-this.MaxWeight);		
	}
	//判断是否障碍物===============
	public boolean isStop(){
		if(this.weight>=this.MaxWeight){
			return true ;
		}
		return false ;
	}
	//判断是否同一个位置===============
	public boolean isSame(Place place){
		if(place.x==this.x &&place.y==this.y){
			return true ;
		}
		return false ;
	}
	//判断是否为出口==================
	public boolean isExit(){
		return this.weight==0?true:false;
	}
	public Place getArrowPlace(Cat.Direction d){
		switch(d){
		case left : return this.right ;
		case leftTop : return this.rightDown ;
		case leftDown : return this.rightTop ;
		case right : return this.left ;
		case rightTop : return this.leftDown ;
		case rightDown : return this.leftTop ;
		}
		return null ;
	}
}
