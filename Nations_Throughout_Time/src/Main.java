
public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		GamePanel gamePanel = new GamePanel();
				
		DataManagement dataManagement = new DataManagement();
		
		ClassStructure classStructure = new ClassStructure(gamePanel,dataManagement);
		
		classStructure.addDMToPanel();
		
		new GameWindow(gamePanel);		
	}
}
