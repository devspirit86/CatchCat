package spirit.parttime.cat.logic;

import android.util.Log;

public class Map{
	//��ͼ��������11*11
	public final int MaxNum ;
	public Place[][] map ;
	public final int center ;
	
//==========��ͼ��ʼ��
	public Map(int maxnum){
		//��ʼ��
		this.MaxNum = maxnum ;
		this.map = new Place[MaxNum][MaxNum];
		this.center = MaxNum/2 ;
		//��ʼ���������ð�����Ȩֵ
		for(int i = 0 ;i<MaxNum ;i++){
			for(int j= 0;j<MaxNum;j++){
			map[i][j] = new Place(getWeightTo(i, j),i,j);
			}
		}
		//���õ�ͼ���ӽṹ
		for(int i = 0 ;i<MaxNum;i++){
			int top = i-1 ;
			int down = i+1 ;
			for(int j= 0;j<MaxNum;j++){
				int left = j-1;
				int right = j+1;		
				if(i%2==0){
					//ż����
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
					//������
					//ż����
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
		//��ʼ�������б�
		initFind();
	}
	//���õ�ͼ�ϰ���========================
	public boolean setStop(int x,int y){
		if(this.map[x][y].isStop()){
			//�Ѿ����ϰ���
			return false ;
		}
		this.map[x][y].setStop();
		return true ;
	} 
	//��ͼ��λ���س�ʼ��=====================
	public void reinitMap(){
		for(int i = 0 ;i<MaxNum ;i++){
			for(int j= 0;j<MaxNum;j++){
			map[i][j].setWeight(getWeightTo(i, j));
			}
		}
	}
	//��ȡ��λ��Ȩֵ========================
	private  int getWeightTo(int x , int y){
		//x����ƫ��
		if(x > center){
			x = x-center ;
		}else{
			x = center-x ;
		}
		//y����ƫ��
		if(y > center){
			y = y-center ;
		}else{
			y = center-y ;
		}
		//��ȡȨֵ
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
		System.out.println("place λ�� :("+place.x+","+place.y+") weight:"+place.weight);
		if(place.leftTop!=null)
		System.out.println("place ���� :("+place.leftTop.x+","+place.leftTop.y+") weight:"+place.leftTop.weight);
		if(place.leftDown!=null)
		System.out.println("place ���� :("+place.leftDown.x+","+place.leftDown.y+") weight:"+place.leftDown.weight);
		if(place.left!=null)
		System.out.println("place �� :("+place.left.x+","+place.left.y+") weight:"+place.left.weight);
		if(place.rightTop!=null)
		System.out.println("place ���� :("+place.rightTop.x+","+place.rightTop.y+") weight:"+place.rightTop.weight);
		if(place.right!=null)
		System.out.println("place �� :("+place.right.x+","+place.right.y+") weight:"+place.right.weight);
		if(place.rightDown!=null)
		System.out.println("place ���� :("+place.rightDown.x+","+place.rightDown.y+") weight:"+place.rightDown.weight);
		
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
	//+++++++++++++++++++++++++++è��Ѱ·�㷨+++++++++++++++++++++++++++++++++++++++=
	private Place[] endSet = new Place[40] ;//40������ 
	private int step ;//ʹ�õ���С�������ոտ�ʼΪһ�����ֵ
	private int useStep ;//��ǰʹ�õĲ���
	private Cat.Direction lastArrow  ;//���һ�εķ���
	private Place nowPlace ;//��ǰ��λ��
	//int nowWeight;//��ǰȨֵ
	//int stop = ;//�ϰ�
	//int pass ;//��ͨ
	//int end ;//�ӵ�
	private Place cat ;//è��λ��
	private final int maxStep = 7 ;//�����
	
	private void initFind(){
		int k = 0;//��ǰ�����
		int i1 = 1;//��һ�д� 1 ~ 9��
		int i2 = 9;//���һ�д�  9~ 1��
		int j1 = 1;//���� �� 1~ 9��
		int j2 = 9 ;//���� �� 9~1��
		while(k<endSet.length-4){
			if(i1<=9){//��һ��
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
		//˲ʱ��
		
		for(int i = 0 ,j = 0 ; i<11 ;i++,j++){
			endSet[j] = this.map[0][i];//��һ�У�0��10��
		}
		for(int i = 0 ,j = 10 ; i<11 ;i++,j++){
			endSet[j] = this.map[i][10];//���У�10��20��
		}
		for(int i = 10 ,j = 20 ; i>=0 ;i--,j++){
			endSet[j] = this.map[10][i];//���һ�У�20��30��
		}
		for(int i = 10 ,j = 30 ; i>0 ;i--,j++){//1,1 ��λ���Ѿ����ֹ�
			endSet[j] = this.map[i][0];//���У�30��39��
		}*/
	}
	private boolean find(Place now){//�����ڿ�ʼ��һ�α�һ�� �ҵ� ������
		useStep ++;//������һ��
		if(now==null){
			useStep --;
			return false ;//Խ����
		}
		if(useStep>=step-1){//�Ѿ���������С������û�б�Ҫ��
			useStep --;
			return false ;
		}
		
		if(now.isExit()&&useStep!=0){//����ǵ�һ��λ�ò����뿼��
			useStep --;
			return false ;//���߻��յ� �϶��������ŵ��㷨�� 
		}
		if(now.isStop()){//�����ϰ�����
			useStep --;
			return false ;
		}
		//�ҵ������
		if(now.left!=null&&now.left.isSame(cat)){
			step = useStep + 1;//��һ���ҵ�
			lastArrow = Cat.Direction.left ;
			useStep -- ;//������һ��
		//	Log.d("break", "("+now.x+","+now.y+")left find "+step);
			return true ;
		}
		if(now.leftTop!=null&&now.leftTop.isSame(cat)){
			step = useStep + 1;//��һ���ҵ�
			lastArrow = Cat.Direction.leftTop ;
			useStep -- ;//������һ��
		//	Log.d("break", "("+now.x+","+now.y+")leftTop find "+step);
			return true ;
		}
		if(now.leftDown!=null&&now.leftDown.isSame(cat)){
			step = useStep + 1;//��һ���ҵ�
			lastArrow = Cat.Direction.leftDown ;
			useStep -- ;//������һ��
		//	Log.d("break", "("+now.x+","+now.y+")leftDown find "+step);
			return true ;
		}
		if(now.right!=null&&now.right.isSame(cat)){
			step = useStep + 1;//��һ���ҵ�
			lastArrow = Cat.Direction.right ;
			useStep -- ;//������һ��
		//	Log.d("break", "("+now.x+","+now.y+")right find "+step);
			return true ;
		}
		if(now.rightTop!=null&&now.rightTop.isSame(cat)){
			step = useStep + 1;//��һ���ҵ�
			lastArrow = Cat.Direction.rightTop ;
			useStep -- ;//������һ��
		//	Log.d("break", "("+now.x+","+now.y+")rightTop find "+step);
			return true ;
		}
		if(now.rightDown!=null&&now.rightDown.isSame(cat)){
			step = useStep + 1;//��һ���ҵ�
			lastArrow = Cat.Direction.rightDown ;
			useStep -- ;//������һ��
			//Log.d("break", "("+now.x+","+now.y+")rightDown find "+step);
			return true ;
		}
		
		//===========��һ���� Ҫ�����Լ������
		//ѡ����뷽��
		Cat.Direction[] d = Cat.Direction.values();
		boolean isFind = false ;
		for(int i = 0; i<d.length;i++){
			if(find(now.getArrowPlace(d[i]))){//��̽�÷���
				isFind = true ;
				if(step==now.weight){//����϶����������
			//		Log.d("break", now.x+":"+now.y);
					return true ;
				}
			}
		}
		if(isFind){
			return true ;
		}
		//6������������û���ҵ�è
		
		useStep --;
		return false ;
	}
	private boolean findCat(){
		//��ʼ��
		boolean canFind = false ;
		this.step = maxStep ;//��С����Ϊ���ֵ
		for(int i = 0;i<endSet.length;i++){
			this.useStep = -1;//ÿ��Ѱ�����¹�-1����һλ�ò���
			if(find(endSet[i])){//��ǰ���ڿ����ҵ�è
				canFind = true ;
			}
		}
		return canFind ;
	}
	public Cat.Direction findCatNextJump(Place catPlace){//������һ����λ��
		this.cat = catPlace ;//��ǰè��λ��
		this.lastArrow = null ;//��ʼʱ����Ϊ��
		boolean canJump = false ;//������
		canJump = findCat();//Ѱ��è
		Log.d("arrow","find"+lastArrow);
		if(canJump){
			//����һ��·��
			switch(this.lastArrow){//���ط����� ��Ϊ���
			case left : return Cat.Direction.right ;
			case leftTop : return Cat.Direction.rightDown ;
			case leftDown : return Cat.Direction.rightTop ;
			case right : return Cat.Direction.left ;
			case rightTop : return Cat.Direction.leftDown ;
			case rightDown : return Cat.Direction.leftTop ;
			}
		}
		//û�г�·�ˣ�è�ڵ���++++++++++++++++++++++++++++++++++++++++++
		if(cat.left!=null&&!cat.left.isStop()){//�ǿշ��谭
			return Cat.Direction.left ;
		}
		if(cat.leftTop!=null&&!cat.leftTop.isStop()){//�ǿշ��谭
			return Cat.Direction.leftTop ;
		}
		if(cat.leftDown!=null&&!cat.leftDown.isStop()){//�ǿշ��谭
			return Cat.Direction.leftDown ;
		}
		if(cat.right!=null&&!cat.right.isStop()){//�ǿշ��谭
			return Cat.Direction.right ;
		}
		if(cat.rightTop!=null&&!cat.rightTop.isStop()){//�ǿշ��谭
			return Cat.Direction.rightTop ;
		}
		if(cat.rightDown!=null&&!cat.rightDown.isStop()){//�ǿշ��谭
			return Cat.Direction.rightDown ;
		}
		//.......
		//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		return null ;//��·����
	}
}
