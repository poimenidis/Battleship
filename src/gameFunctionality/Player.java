package gameFunctionality;

import guiPackage.Connect;
import guiPackage.NewGame;



public class Player {

	private String name;
	private boolean status;
	private Table table;
	private Ship[] ship;

	
	public Player(String name) {
		this.name = name;
		this.status = true;
		
		ship = new Ship[6];
		
		
		ship[0] = new Ship(2);
		ship[1] = new Ship(2);
		ship[2] = new Ship(3);
		ship[3] = new Ship(3);
		ship[4] = new Ship(4);
		ship[5] = new Ship(5);
		
		
		
	} 
	
	public void setPosition(){
		/**
		 * Leitourgia: topo8etei ta ploia stis 8eseis tous kalontas thn antisixei klash tou ship
		 */
		this.table = new Table();
		for(int i=0;i<6;i++)
			ship[i].setCoordinates(table, i);
	}
	
	public int[] hit(Player p){
		/**
		 * eisodos:dexete ton pinaka ston opoio 8a varesei
		 * leitourgia: pairnei ths suntetagmenes apo ton xrish kai xripaei ston table
		 * Epistrefei: to coordinates pou pragmatopoih8ike tlk to hit 
		 */
		int[] hitCoordinates= new int[1];
		// hitCoordinates[0]=x, hitCoordinates[1]=y
		boolean hited=false;
		Table table = p.getTab();
		
		while(!hited){
			/************************hit input*******************************************/
			/** edw 8a mpoun oi me8odoi ths connect pou 8a fernei ta x kai ta y**/
			
			hitCoordinates=NewGame.co.getHit();
			/****************************************************************************/
			
			hited = table.check(hitCoordinates[0],hitCoordinates[1], p.getShip());
			Connect.setStatus(hited);
			Connect.setStatusHit(table.getCoordinate(hitCoordinates[0], hitCoordinates[1]));
		}
		checkStatus(p);
		return hitCoordinates;
		
	}
	
	public boolean Alive(){
		/** epistrefei to staus ama o paixths exase h oxi akoma **/
			return status;
		}
	
	public Table getTab(){
		
		return table;
		
	}
	
	public void setTab(Table table) {
		this.table = table;
	}

	public void checkStatus(Player p){
		/**
		 *  leitourgia: kanei update thn katastash status
		 */
		p.setStatus(false);
		for(int i=0;i<6;i++){
			int[] coor=p.getShip(i).getCoordinates();
			if(p.table.getCoordinate(coor[0], coor[1])!=3){
				p.setStatus(true);
			}
				
		}
		
	}
	
	public String getName(){
		return name;
	}
	

	
	public boolean getStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public Ship getShip(int i){
		return ship[i];
	}
	
	public Ship[] getShip(){
		return ship;
	}
	
	
	
	
	
}
