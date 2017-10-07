package gameFunctionality;
import guiPackage.Connect;


public class Ship{

	public static enum Direction {
		HORIZONTAL,
		VERTICAL	    
	}

	//	public enum Condition {
	//      HIT,
	//		SEA,
	//		MISS,
	//		SHIP,
	//      SUNK
	//	}

	private int size;
	private Direction direction;
	private boolean status;
	private int life;
	private int[] coordinates; //2 thesewn x y

	public Ship(int size) {
		this.size = size;
		status = true;
		life=size;
		coordinates = new int[2];

	}


	public void setCoordinates (Table table,int i) {
		boolean swsto=false;
		//to while xris gia na pairnei times mexri na parei thn swsth 
		//omws sthn sugkekrimenh periptwsh den xris gt o elnxos ginete sunolika
		//gia olo to "stolo" kai oxi 3exwrista gia ka8e ploio
		while(!swsto){
			/********************************************************************
			 *                 coor input from connect
			 *******************************************************************/
			int d=0;
			int[] ca;
			ca=Connect.getCoordinates(i);



			this.coordinates[0]= ca[0];
			this.coordinates[1]= ca[1];
			d=ca[2];
			/*******************************************************************/
			if(d==0){
				direction=Direction.HORIZONTAL;
			}else
				direction=Direction.VERTICAL;


			swsto = table.plaseShip(this);
			Connect.PlacementStatus(swsto);
			if(!swsto) break;
		}

	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}


	public int[] getCoordinates() {
		return coordinates;
	}
	public void setCoordinates(int[] coor) {
		coordinates[0]=coor[0];
		coordinates[1]=coor[1];
	}

	public int getSize() {
		return size;
	}

	public Direction getDirection() {
		return direction;
	}

	public boolean updateLive(int x, int y) {
		/**
		 * eisodos: suntetagmenes 
		 * Leitourgia: meiwnei thn zwh tou ploiou tou opoiou einai oi suntetamenes  kata ena kai elenxei an exei vi8istei h oxi
		 * epitrefei : mia logikh katastash pou ypodilwnei pws i diadikasia oloklhrw8ike epitixws( dld vre8ike ploio kai tou afere8ike 1 apo thn zwh
		 * 
		 */
		boolean flag=false;
		switch(direction) {
		case HORIZONTAL:
			if(y==coordinates[1]){
				int i = 0;
				while(!flag&&i<size){
					if(coordinates[0]+i==x){
						life--;
						flag=true;
					}
					i++;
				}
			}
			break;
		case VERTICAL:
			if(x==coordinates[0]){
				int i = 0;
				while(!flag&&i<size){
					if(coordinates[1]+i==y){
						life--;
						flag=true;
					}
					i++;
				}
			}
			break;
		}
		if(life==0){
			status=false;
		}
		return flag;
	}

	public boolean getStatus() {
		return status;
	}


}
