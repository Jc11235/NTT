
public class ClassStructure {
	
	DataManagement dataManagement;
	
	GamePanel gamePanel;
	
	public ClassStructure(GamePanel gp, DataManagement dm){
		dataManagement = dm;
		
		gamePanel = gp;		
	}
	
	//other functions
	public void addDMToPanel(){
		gamePanel.setDataManagement(dataManagement);
	}
}
