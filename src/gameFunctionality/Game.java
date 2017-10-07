package gameFunctionality;

import guiPackage.Connect;

public class Game extends Thread{

	private static Player p1;
	private static Player p2;
	private static Player winner;


	public  Game() {
		/**
		 * TA SXOLIA APO KATW EINAI H KANONIKI ROH THS KLASHS ALLA GIA NA GINEI H SUNENWSH 
		 * ME TO ALLO POAKETO MPHKANW SE ME8ODOUS 
		 */

		//dhmiourgia paixtwn 

		//TheGuisMother mom= new TheGuisMother();
		//		p1=new Player("Player1");
		//		if(Connect.getMode()==0){
		//			p2=new Player("Player2");	
		//		}else{
		////			for(int j=0;j<100;j++)
		////				System.out.print("-");
		//			p2=new Computer("Player2",Connect.getLevelPC());
		//		}
		//		p1=new Computer("Player1", 1);
		//		p2= new Computer("Player2", 2);

		//p2.hit(p1.getTab());

		//einai h diadikasia kata thn opoia o paixths topo8eti ta ploia tou
		//		p1.setPosition();
		//		print(p1);
		//		p2.setPosition();
		//		print(p2);
		//kalisma tou anadromikou algori8mou tou paixnidioy
		//		Connect.setStatus(true);
		//		Player winner=TheGame(p1,p2);
		//		Connect.setStatus(false);

		///////////////////////////////////////////////////////////////////////////
		//		for(int j=0;j<100;j++)
		//			System.out.print("-");
		//		//System.out.print("AND THE WINNER IS: "+winner.getName() +" \n");
		//		for(int j=0;j<100;j++)
		//			System.out.print("-");
		///////////////////////////////////////////////////////////////////////////
		//	Connect.setWinner(winner.getName());


	}


	// ta duo print voh8an na ginei h ektipwsh sto console 
	private static void print (Player p){
		/**
		 * VOh8ane sto na vlepeis tous pinakes sthn Consola 
		 */
		System.out.print("The table of player "+p.getName() +" \n");
		for(int j=0;j<10;j++){
			for(int i=0;i<10;i++){
				System.out.print(p.getTab().getCoordinate(i, j) + " ");
			}
			System.out.print("\n");
		}
	}

	public static void print (Table table){
		//System.out.print("The table of player "+p.getName() +" \n");
		for(int j=0;j<10;j++){
			for(int i=0;i<10;i++){
				System.out.print(table.getCoordinate(i, j) + " ");
			}
			System.out.print("\n");
		}
	}

	private static Player TheGame(Player p1, Player p2){
		Player winner;
		if (p1.Alive() && p2.Alive()){
			//ean ka8e paixths zei pername sthn diadikasia paixnidiou
			int [] coordinates=p1.hit(p2);//o paixths p1 xripaei ston pinaka tou paixth p2
			/*
			 * 0=tatastash hremias
			 * 1=katastash apotixias
			 * 2=upar3h Ship
			 * -1=katastash epitixias
			 * 3=vi8istike
			 * */
			//edw prosdiorizete poios paixths paizei epomenos mias kai ama petixenei ton stoxo enas p, 3ana paizei
			//////////////gia na emfanizontai kai sto console///////////////////////////////
			//			print(p1);
			//			print(p2);
			//			System.out.println("****************************************");
			///////////////////////////////////////////////////////////////////////////////
			if(p2.getTab().getCoordinate(coordinates[0], coordinates[1])==1){
				winner=TheGame(p2,p1);
			}else{
				winner=TheGame(p1,p2);
			}
		}else{
			//otan pai8anei enas apo tous duo paixtes painoume sto else kai vlepoume poios apo tous duo paixtes pai8ane kai epistrefoume ton nikith
			if(!p1.Alive()){
				return p2;
			}else
				return p1;
		}

		return winner;

	}

	public static void createPlayers(){
		p1=new Player("Player1");
		if(Connect.getMode()==0){
			p2=new Player("Player2");	
		}else{
			p2=new Computer("Computer",Connect.getLevelPC());
		}
	}



	public static void setPositions(int m){
		if(Connect.getMode()==0){
			switch (m){
			case 0:
				p1.setPosition();
				break;
			case 1:
				p2.setPosition();
				break;
			}
		}else{
			p1.setPosition();
			//			print(p1);
			p2.setPosition();
			//			print(p2);
		}
	}

	//    public static void begin(){
	//        Connect.setGameStatus(true);
	//    	winner=TheGame(p1,p2);
	//    	Connect.setGameStatus(false);
	//    	Connect.setWinner(winner.getName());
	//    }


	public void run() {
		Connect.setGameStatus(true);
		winner=TheGame(p1,p2);
		Connect.setWinner(winner.getName());
		Connect.setGameStatus(false);
	}
}