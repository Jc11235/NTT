
public class Terrain {
	
	protected String terrainType;
	
	protected int landHeight;
	protected int forest;
	protected int mountain;
	protected int visibility;	
	protected int x;
	protected int y;
	protected int food;
	protected int production;
	protected int value;
	protected int science;
	protected int movementCost;
	
	protected double temperature;
	
	protected String tileImprovement;

	public Terrain(int newX, int newY){
		forest = 0;
		mountain = 0;
		
		temperature = 0;
		visibility = 0;
		
		production = 0;
		food = 0;
		value = 0;
		
		movementCost = 0;
		
		x = newX;
		y = newY;
		
		terrainType = "Ocean";
		tileImprovement = "";
	}

	//setters
	public void setTerrainType(String newTerrainType){
		terrainType = newTerrainType;
	}
	public void setTileImprovement(String newImprovement){
		tileImprovement = newImprovement;
	}
	
	public void setLandHeight(int newHeight){
		landHeight = newHeight;
	}
	public void setForestFlag(int newFlag){
		forest = newFlag;
	}
	public void setMountainFlag(int newFlag){
		mountain = newFlag;
	}
	public void setVisibility(int newFlag){
		visibility = newFlag;
	}
	public void setX(int newX){
		x = newX;
	}
	public void setY(int newY){
		y = newY;
	}
	public void setFood(int newFood){
		food = newFood;
	}
	public void setProduction(int newProduction){
		production = newProduction;
	}
	public void setValue(int newValue){
		value = newValue;
	}
	public void setScience(int newScience){
		science = newScience;
	}
	public void setMovementCost(int newMC){
		movementCost = newMC;
	}
		
	public void setTemperature(double newTemp){
		temperature = newTemp;
	}
		
	//getters
	public String getTerrainType(){
		return terrainType;
	}
	public String getTileImprovement(){
		return tileImprovement;
	}
	
	public int getLandHeight(){
		return landHeight;
	}
	public int getForestFlag(){
		return forest;
	}
	public int getMountainFlag(){
		return mountain;
	}
	public int getVisibility(){
		return visibility;
	}
	public int getX(){
		return x;
	}
	public int getY(){
		return y;
	}
	public int getFood(){
		return food;
	}
	public int getProduction(){
		return production;
	}
	public int getValue(){
		return value;
	}
	public int getScience(){
		return science;
	}
	public int getMovementCost(){
		return movementCost;
	}
	
	public double getTemperature(){
		return temperature;
	}
	
	//other functions
	public void generateResources(){
		
	}
}
