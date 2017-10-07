package gameFunctionality;

import gameFunctionality.Ship.Direction;
import guiPackage.Connect;
import guiPackage.NewGame;

/** h klash Computer apotelei automato paixth gia ton paixnidi naymaxia
 *  exei treis algor8mous analoga me thn dhskolia
 *  ---= ginete ligo xamoulhs=---
 * 
 * 
 * @author GEORGE
 *
 */

public class Computer extends Player {
	
	
	private int difficulty;
	private boolean isShipDetected;
	private int[] lastHit;
	private int[] enemyShip;

	public Computer(String name,int d) {
		super(name);
		difficulty=d;
		this.isShipDetected=false;
		this.lastHit=new int[2];
		enemyShip=new int[6];
		enemyShip[0]=2;
		enemyShip[1]=2;
		enemyShip[2]=3;
		enemyShip[3]=3;
		enemyShip[4]=4;
		enemyShip[5]=5;
		
	}



	private int[] hitNormal(Table table) {
		int[] ca=new int[2];//cadidate
		int[][] pos=new int[2][4];//stack with all possible coordinates
		int p1=1;
		int p2=1;
		int count=4;
		//*********************gemisma tou pos*******************//
		for(int i=0;i<4;i++){
			if (i%2==0){
				//horizntal
				pos[0][i]=lastHit[0]+p1;
				pos[1][i]=lastHit[1];
				p1=p1*(-1);
				
			}else{
				//vertical
				pos[0][i]=lastHit[0];
				pos[1][i]=lastHit[1]+p2;
				p2=p2*(-1);
			}
			if(pos[0][i]<0 || pos[0][i]>9 || pos[1][i]<0 || pos[1][i]>9){
				//overflow of table
				pos[1][i]=-1;
				pos[0][i]=-1;
				//count--;
			}
		}
		//*********************************************************//
		
		//*************************vasikh epanali9h****************//
		int i=0;
		while(pos[1][i]==-1){
			//auto ginete gia na mhn mpei me timh pou den uparxei ston pinaka
			i++;
			}
		while(i<count){//BUG MAYBE
			ca[0]=pos[0][i];
			ca[1]=pos[1][i];
			switch(i){
			case 0:
				p1=1;
				p2=0;
			break;
			case 1:
				p1=0;
				p2=1;
			break;
			case 2:
				p1=-1;
				p2=0;
			break;
			case 3:
				p1=0;
				p2=-1;
			break;
			
			
			
			}
			//****************if -1 3ekinima ths diadikasias********//	
			while(table.getCoordinate(ca[0], ca[1])==-1){
				ca[0]=ca[0]+p1;
				ca[1]=ca[1]+p2;
				//!(pos[0][i]<10&&pos[0][i]>=0&&pos[1][i]>=0&&pos[1][i]<10)
				//table overflow
				if(!(ca[0]<10&&ca[0]>=0&&ca[1]>=0&&ca[1]<10)){
					ca[0]=lastHit[0];
					ca[1]=lastHit[1];
					p1=p1*(-1);
					p2=p2*(-1);
					pos[1][i]=-1;
					pos[0][i]=-1;
				}
				
				if(table.getCoordinate(ca[0], ca[1])==1){
					//vgazw apo ton pos to coor auto gt 8a to dokimasei mesa sthn while auth
					ca[0]=lastHit[0];
					ca[1]=lastHit[1];
					p1=p1*(-1);
					p2=p2*(-1);
					pos[1][i]=-1;
					pos[0][i]=-1;
				}else if(table.getCoordinate(ca[0], ca[1])!=-1){
					return ca;
				}
			}
			//*****************************************************//
			do{
			i++;
			}while(i<count&&pos[1][i]==-1);
		}
		//*********************************************************//
		//*************************tuxaia epilogh apo ta pos********************************//
		for(i=0;i<count;i++){
			if(pos[1][i]!=-1&&table.getCoordinate(pos[0][i],pos[1][i])!=1){
				ca[0]=pos[0][i];
				ca[1]=pos[1][i];
				return ca;//
			}
		}
		//*********************************************************//
		return null;
	}

	private int[] hitHard(Table table) {
		int[][] proTable=probabilityTable(table);
		/***********************************************************/
//		System.out.print("\n");
//		for(int i=0; i<10; i++){
//			for(int j=0; j<10; j++){
//				System.out.print(proTable[i][j]+" ");
//			}
//			System.out.print("\n");
//		}
		int max=0;
		int[] ca=new int[2];//cadidate
		for(int i=0; i<10; i++){
			for(int j=0; j<10; j++){
				if(proTable[i][j]>max){
					max=proTable[i][j];
					ca[0]=i;
					ca[1]=j;
				}
			}
		}
		return ca;
	}
	
	private int[][] probabilityTable(Table enemyTable){
		//*********arxikopoiohsh tou pinaka twn apotelesmatwn*****************//
		int table[][]=new int[10][10];
		for(int i=0; i<10; i++)
			for(int j=0; j<10; j++)
				table[i][j] = 0;
		//*******************euresh tou megalitourou Ship pou emeine**********//
		int max=0;
		for(int i=0;i<6;i++){
			if(enemyShip[i]>max){
				max=enemyShip[i];
			}
		}
		//*********************************************************************//
		
		//?*********************upologismos twn pi8anothtwn********************//
		for(int i=0;i<10;i++){
			for(int j=0;j<10;j++){
				if((j+max-1)<10){
					boolean flag=true;
					for(int k=j;k<j+max;k++){
						if(enemyTable.getCoordinate(i, k)!=0 && enemyTable.getCoordinate(i, k)!=2){
							flag=false;
						}
					}
					if(flag){
						for(int k=j;k<j+max;k++)
							table[i][k]++;
					}
				}else
					break;
			}
		}
		for(int i=0;i<10;i++){
			for(int j=0;j<10;j++){
				if((j+max-1)<10){
					boolean flag=true;
					for(int k=j;k<j+max;k++){
						if(enemyTable.getCoordinate(k, i)!=0 && enemyTable.getCoordinate(k, i)!=2){
							flag=false;
						}
					}
					if(flag){
						for(int k=j;k<j+max;k++)
							table[k][i]++;
					}
				}
				else
					break;
			}
		}
		//***************************************************************************************//
		return table;
	}
	
	private void setEnemyShips(int size){
		for(int i=0;i<6;i++){
			if(enemyShip[i]==size){
				enemyShip[i]=-1;
				break;
			}
		}
	}
	
	
	
	public int[] hit(Player p){
		
		int[] hitCoordinates= new int[2];
		// hitCoordinates[0]=x, hitCoordinates[1]=y
		boolean hited=false;
		Table table=p.getTab();
		
		while(!hited){
			/************************hit input*******************************************/
			switch (difficulty){
			case 0:
				hitCoordinates[0]=(int) (Math.random()*10);
				hitCoordinates[1]=(int) (Math.random()*10);
			break;
			case 1:
				if(isShipDetected){
					hitCoordinates=hitNormal(table);
				}else{
					hitCoordinates[0]=(int) (Math.random()*10);
					hitCoordinates[1]=(int) (Math.random()*10);
					
				}
			break;
			case 2:
				if(isShipDetected){
					hitCoordinates=hitNormal(table);					
				}else{
					hitCoordinates=hitHard(table);
				}
				
			break;		
			}
			/****************************************************************************/
			hited = table.check(hitCoordinates[0],hitCoordinates[1], p.getShip());
			if(table.getCoordinate(hitCoordinates[0], hitCoordinates[1])==-1)
				this.isShipDetected=true;
			else if(table.getCoordinate(hitCoordinates[0], hitCoordinates[1])==3){
				this.isShipDetected=false;
				int size=1;
				int i=1;
				int flag=0;
				int p1,p2;
				//elenxw direction
				if(hitCoordinates[0]!=lastHit[0]){
					//HORIZONTAL
					p1=1;
					p2=0;
				}else{
					//VERTICAL
					p1=0;
					p2=1;
				}
				
				//elenxw poio ploio vi8isa (dld metraw to size tou 
				while(true){
					boolean A=hitCoordinates[0]+i*p1>=0&&hitCoordinates[0]+i*p1<10
							&&hitCoordinates[1]+i*p2>=0&&hitCoordinates[1]+i*p2<10;
					if(A&&table.getCoordinate(hitCoordinates[0]+i*p1, hitCoordinates[1]+i*p2)==3){
						size++;
						i++;
					}else{
						i=1;
						p1=p1*(-1);
						p2=p2*(-1);
						flag++;
					}
					
					if(flag==2)
						break;
				}
				//katagrafw to Vi8ismeno
				this.setEnemyShips(size);
			}
		}
		if(table.getCoordinate(lastHit[0], lastHit[1])==-1){
			if(table.getCoordinate(hitCoordinates[0], hitCoordinates[1])==-1){
				lastHit[0]=hitCoordinates[0];
				lastHit[1]=hitCoordinates[1];
			}
		}else{
			lastHit[0]=hitCoordinates[0];
			lastHit[1]=hitCoordinates[1];
		}
		
		NewGame.co.setPcHit(hitCoordinates[0], hitCoordinates[1]);
		//		Connect.setStatus(hited);
		Connect.setStatusHit(table.getCoordinate(hitCoordinates[0], hitCoordinates[1]));
		checkStatus(p);
		return hitCoordinates;
		
	}
	
	public void setPosition(){
		setTab(new Table());
		for(int i=0;i<6;i++){
			int[] coor=new int[2];
			boolean flag=false;
			while(!flag){
				 coor[0]=(int) (Math.random()*10);
				 coor[1]=(int) (Math.random()*10);
				 double d=(Math.random());
				 getShip(i).setCoordinates(coor);
				 if(d<0.5){
						getShip(i).setDirection(Direction.HORIZONTAL);
					}else{
						getShip(i).setDirection(Direction.VERTICAL);
					}
				 flag=getTab().plaseShip(getShip(i));
			 }
		}
	}
	
	

}
