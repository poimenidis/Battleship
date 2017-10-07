package guiPackage;

public class Connect {

	private static int levelPC;
	private static int mode;
	private static boolean status;
	private static boolean placementStatus = true;
	private static int ship[][] = new int[6][3];
	private static int hit[] = new int[2];
	private static int pcHit[] = new int[2];
	private static int statusHit;
	private static int sunkShip[] = new int[4];
	private static String winner;
	private static boolean empty = true;
	private boolean pcEmpty = true;
	private static boolean gameStatus;
	private static boolean control;
	
	
	public Connect(){
		placementStatus = true;
		ship = new int[6][3];
		hit = new int[2];
		pcHit = new int[2];
		sunkShip = new int[4];
		empty = true;
		pcEmpty = true;
	}

	public static void setGameMode(String choice) {
		if(choice.equals("pvp")) {
			mode = 0;
		}
		else if(choice.equals("easy")) {
			levelPC = 0;
			mode = 1;
		}
		else if(choice.equals("medium")) {
			levelPC = 1;
			mode = 1;
		}
		else {
			levelPC = 2;
			mode = 1;
		}
	}

	public static int getMode() {
		return mode;
	}

	public static void setCoordinates(int i, int x, int y, boolean direction) {
		//direction == true, horizontal
		//direction == false, vertical
		ship[i][0] = x;
		ship[i][1] = y;

		if(direction) {
			ship[i][2] = 0;
		}
		else {
			ship[i][2] = 1;
		}
	}

	public static int[] getCoordinates(int i) {
		int[] array = new int[3];
		array[0] = ship[i][0];
		array[1] = ship[i][1];
		array[2] = ship[i][2];
		return array;
	}

	public static int getLevelPC() {
		return levelPC;
	}

	public synchronized void setHit(int x, int y ) {
		while(!empty){
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		empty = false;

		hit[0] = x;
		hit[1] = y;

		notifyAll();
	}

	public synchronized int[] getHit() {
		while(empty) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		empty = true;

		notifyAll();

		return hit;
	}

	public static boolean getPlacementStatus() {
		return placementStatus;
	}

	public static void setPlacementStatus(boolean status) {
		Connect.placementStatus = status;
	}

	public static void PlacementStatus(boolean status) {
		Connect.placementStatus = Connect.placementStatus && status;
	}

	public static boolean getStatus() {
		return status;
	}

	public static void setStatus(boolean status) {
		Connect.status = status;
	}

	public static void setStatusHit(int statusHit) {
		Connect.statusHit = statusHit;
	}

	public static int getStatusHit() {
		return statusHit;
	}

	public static int[] getSunkShip() {
		return sunkShip;
	}

	public static void setSunkShip(int x, int y , int size, int direction) {
		sunkShip[0] = x;
		sunkShip[1] = y;
		sunkShip[2] = size;
		sunkShip[3] = direction; //0 horizontal, 1 vertical
	}

	public static String getWinner() {
		return winner;
	}

	public static void setWinner(String winner) {
		Connect.winner = winner;
	}

	public static void setGameStatus(boolean gameStatus) {
		Connect.gameStatus = gameStatus;
	}

	public static boolean getGameStatus() {
		return gameStatus;	
	}

	public synchronized int[] getPcHit() {

		while(pcEmpty){


			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}


		pcEmpty = true;

		notifyAll();

		return pcHit;
	}

	public synchronized void setPcHit(int x, int y ) {

		while(!pcEmpty || !control) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		pcEmpty = false;
		control = false;
		pcHit[0] = x;
		pcHit[1] = y;
		
		notifyAll();
	}

	public synchronized void setControl(boolean control){
		notifyAll();
		Connect.control = control;
	}

}
