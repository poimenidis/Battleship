package gameFunctionality;

import gameFunctionality.Ship.Direction;
import guiPackage.Connect;

/**
 * h table apotelei mia apo tis pio shmantikes klaseis tou paixnidiou,
 * einai o pinakas ston opoio tapo8etountai ta ploia kai ustera xtipan oi paixtes
 * oles h me8odoi tou Player kai tou Ship pou kanoun topo8ethsh h xtupan kai genikws oti exei na kanei 
 * me ton pinaka, kaloun me8odous ths Table kai olo to "duskolo" kai to sumantiko komati to analamvanei h  Table
 * 
 *
 */
public class Table {

	private int[][] table = new int[10][10]; //δημιουργία πίνακα 10χ10


	public Table(){

		for(int i=0; i<10; i++)
			for(int j=0; j<10; j++)
				table[i][j] = 0;
	}

	public boolean plaseShip (Ship ship) {
		/**dexetai: to ploio pou prokeitai na topo8ethsh
		 * Leitourgia: elenxei an einai dunaton h topo8ethsh elenxontas sugkekrimena ta e3h pragmata:
		 *             an sto shmeio pou zhteitai na topo8eti8ei to ploio den uparxei kanena allo
		 *             an xwraei na mpei ekei kai den vgenei apo ta oria tou pinaka
		 *             an mia 8esh girw apo to ploio den upaxei kanena allo 
		 *epistrefei: mia logikh katastash pou upodhlwnei an h topo8etish pragmatopoih8ike h oxi 
		 * 
		 */

		int x = ship.getCoordinates()[0];  
		int y = ship.getCoordinates()[1];
		boolean flag;

		if(ship.getDirection()==Direction.HORIZONTAL) { 
			if(x+ship.getSize()-1>9)
				return false;
			////////////// h epanali9h pou frontizei kai elenxei apo pisw kai apo mporosta na mhn exei tpt////////////////////////////
			for(int i=-1;i<=1;i++){
				boolean A=true;
				boolean B=true;
				if(x-1<0){
					A=false;
				}else if(!(x+ship.getSize()<10)){
					B=false;
				}
				if(y+i<0|| y+i>9){
					A=false;
					B=false;
				}
				if(A&&table[x-1][y+i]!=0 || B&&table[x+ship.getSize()][y+i]!=0)
					return false;
			}
			/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

			/////////sthn 8esh pou 8a mpei to ploio  kai panw katw na mhn exei tpt///////////////////////////////////////////////////////

			for(int i=x; i<x+ship.getSize(); i++){
				boolean A=true;
				boolean B=true;
				if(y-1<0){
					A=false;
				}else if(y+1>9){
					B=false;
				}
				//	System.out.println("i="+i+" y="+y+" ");
				flag =table[i][y]!=0 || B&&table[i][y+1]!=0 || A&&table[i][y-1]!=0;
				if(flag)
					return false;


			}
			////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

			///topo8eth to ploio
			for(int i=x; i<x+ship.getSize(); i++)
				table[i][y] = 2;
			return true;
		}else{
			if(y+ship.getSize()-1>9)
				return false;
			////////////// h epanali9h pou frontizei kai elenxei apo pisw kai apo mporosta na mhn exei tpt////////////////////////////
			for(int i=-1;i<=1;i++){
				boolean A=true;
				boolean B=true;
				if(y-1<0){
					A=false;
				}else if(!(y+ship.getSize()<10)){
					B=false;
				}
				if(x+i<0|| x+i>9){
					A=false;
					B=false;
				}
				if(A&&table[x+i][y-1]!=0 || B&&table[x+i][y+ship.getSize()]!=0)
					return false;
			}
			/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

			/////////sthn 8esh pou 8a mpei to ploio  kai panw katw na mhn exei tpt///////////////////////////////////////////////////////

			for(int i=y; i<y+ship.getSize(); i++){
				boolean A=true;
				boolean B=true;
				if(x-1<0){
					A=false;
				}else if(x+1>9){
					B=false;
				}
				flag =table[x][i]!=0 || B&&table[x+1][i]!=0 || A&&table[x-1][i]!=0;
				if(flag)
					return false;


			}
			////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

			///topo8eth to ploio
			for(int i=y; i<y+ship.getSize(); i++)
				table[x][i] = 2;
			return true;
		}
	}




	public boolean check(int x, int y, Ship[] ship) 
	/** Dexetai: coordinates kai ola ta ploia enos paixth
	 * leitourgia : pernei tis suntetagmenes ( enos hit) kai ola ta ploia enos paixth 
	 *              kai elenxei an petixe auto to hit kapoio ploio
	 *              epishs elenxei an to ploio vi8istike kai kalei mia sxetikh me8odos
	 *  epistrefei: epistrefei false se periptwsei pou sto shmeio pou egine to hit den einai 
	 *             epitrepto-dunato na ginei , kai epistrefei false wste o paixths na dwseis  new coordinates
	 * 
	 */
	/*     H times me tis opoies gemizei o Table[10][10]
	 * 0 = den iparxei ploio kai den exei xtypithei
	 * 1 = den iparxei ploio kai exei xtypithei
	 * 2 = upar3h Ship alla oxi xtupima
	 * -1 = upar3h Ship kai  xtupima
	 * 3= vi8istike
	 * */
	{


		if(table[x][y] == 0 )
			table[x][y] = 1;
		else if (table[x][y] == 2 ){
			table[x][y] = -1;
			int i=0;
			boolean flag=false;
			while(!flag&& i<6){
				flag=ship[i].updateLive(x, y);
				i++;
			}
			if(!ship[i-1].getStatus()){
				sankShip(ship[i-1]);
			}
		}else
			return false;

		return true;

	}


	public void sankShip(Ship ship){
		/** dexetai : ena ploio to poio 8ewritai oti vi8istike
		 *  Leitourgia: pairnei tis 8eseis tou "vi8ismenou poloiu kai tis kanei 3
		 *              kai tis gurw tou tis kanei 1
		 * 
		 */

		int x = ship.getCoordinates()[0];  
		int y = ship.getCoordinates()[1];
		int d;

		if(ship.getDirection()==Direction.HORIZONTAL) { 
			//////////////h epanali9h pou frontizei kai topo8etei miss(1) apo pisw kai apo mporosta tou ploiou////////////////////////////
			d=0;
			for(int i=-1;i<=1;i++){
				boolean A=true;
				boolean B=true;
				if(x-1<0){
					A=false;
				}else if(!(x+ship.getSize()<10)){
					B=false;
				}
				if(y+i<0|| y+i>9){
					A=false;
					B=false;
				}
				if(A)
					table[x-1][y+i]=1;
				if(B)
					table[x+ship.getSize()][y+i]=1;
			}
			/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


			for(int i=x; i<x+ship.getSize(); i++){
				if(y+1<10)
					table[i][y+1]=1;
				if(y-1>=0)
					table[i][y-1]=1;
				table[i][y] = 3;

			}
		}else{
			//////////////h epanali9h pou frontizei kai topo8etei miss(1) apo pisw kai apo mporosta tou ploiou////////////////////////////
			d=1;
			for(int i=-1;i<=1;i++){
				boolean A=true;
				boolean B=true;
				if(y-1<0){
					A=false;
				}if(!(y+ship.getSize()<10)){
					B=false;
				}
				if(x+i<0|| x+i>9){
					A=false;
					B=false;
				}
				if(A)
					table[x+i][y-1]=1;
				if(B)
					table[x+i][y+ship.getSize()]=1;
			}
			/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

			for(int i=y; i<y+ship.getSize(); i++){
				if(x+1<10)
					table[x+1][i]=1;
				if(x-1>=0)
					table[x-1][i]=1;
				table[x][i] = 3;

			}
		}

		Connect.setSunkShip(x, y, ship.getSize(), d);
	}


	public int getCoordinate(int i, int j) {
		return table[i][j];
	}

}
