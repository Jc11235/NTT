import java.util.ArrayList;
import java.util.Objects;

public class City {
	
	protected int x;
	protected int y;
	protected int population;
	protected int production;
	protected int value;
	protected int science;
	protected int food;
	protected int foodForGrowth;
	protected int maxFoodForGrowth;
	
	protected String yieldFocus;
	protected String name;
	
	private ArrayList<Terrain> cityTiles;
	
	private ArrayList<Unit> buildQueue;
	
	public City(int newX, int newY, String newName){
		x = newX;
		y = newY;
		
		population = 1;
		foodForGrowth = 25;
		maxFoodForGrowth = foodForGrowth;
		production = 0;
		value = 0;
		science = 0;
		food = 0;
		
		yieldFocus = "Production";
		name = newName;
		
		cityTiles = new ArrayList<Terrain>();
		
		buildQueue = new ArrayList<Unit>();
	}
	
	//setters
	public void setX(int newX){
		x = newX;
	}
	public void setY(int newY){
		y = newY;
	}
	public void setProduction(int newProduction){
		production = newProduction;
	}
	public void setPopulation(int newPopulation){
		population = newPopulation;
	}
	public void setValue(int newValue){
		value = newValue;
	}
	public void setScience(int newScience){
		science = newScience;
	}
	public void setFoodForGrowth(int newAmount){
		foodForGrowth = newAmount;
	}
	public void setFood(int newFood){
		food = newFood;
	}
	
	public void setYieldFocus(String newFocus){
		yieldFocus = newFocus;
	}
	
	public void addCityTile(Terrain terrain){
		cityTiles.add(terrain);	
	}
	public void addItemToBuildQueue(Unit unit){
		buildQueue.add(unit);
	}
	//getters
	public int getX(){
		return x;
	}
	public int getY(){
		return y;
	}
	public int getProduction(){
		return production;
	}
	public int getPopulation(){
		return population;
	}
	public int getValue(){
		return value;
	}
	public int getScience(){
		return science;
	}
	public int getFoodForGrowth(){
		return foodForGrowth;
	}
	public int getMaxFoodForGrowth(){
		return maxFoodForGrowth;
	}
	public int getFood(){
		return food;
	}
	
	public String getYieldFocus(){
		return yieldFocus;
	}
	public String getName(){
		return name;
	}
	
	public ArrayList<Terrain> getCityTiles(){
		return cityTiles;
	}
	public ArrayList<Unit> getBuildQueue(){
		return buildQueue;
	}
	
	//other functions
	public void maximizeYield(){
		int maxProduction = -1;
		int maxValue = -1;
		int maxScience = -1;
		int maxFood = -1;
		int maxID = -1;
		
		boolean flag = false;
		
		ArrayList<Integer> indexList = new ArrayList<Integer>();
		
		production = 0;
		
		if(Objects.equals(yieldFocus, "Production")){
			for(int i = 0; i < population; i++){
				maxProduction = -1;
				
				for(int j = 0; j < cityTiles.size(); j++){
					flag = false;
					
					for(int k = 0; k < indexList.size(); k++){
						if(j == indexList.get(k)){
							flag = true;
							
							break;
						}
					}
					if(flag == false){
						if(cityTiles.get(j).getProduction() > maxProduction){
							maxProduction = cityTiles.get(j).getProduction();
							maxValue = cityTiles.get(j).getValue();
							maxScience = cityTiles.get(j).getScience();
							maxFood = cityTiles.get(j).getFood();
							
							maxID = j;
						}	
					}
				}
				
				indexList.add(maxID);
				
				production += maxProduction;
				value += maxValue;
				science += maxScience;
				food += maxFood;
			}
		}
	}
	public int turnsToBuildItem(){
		int turnsToBuild = 0;

		if(production > 0){
			if(buildQueue.size() > 0)
				turnsToBuild = (int) Math.floor((double) (buildQueue.get(0).getProductionCost() / production));
			else
				turnsToBuild = 1000000;
		}			
		else
			turnsToBuild = 1000000;
		
		return turnsToBuild;
	}
	public int turnsToGrowth(){
		int turnsToGrowth = 0;
		
		if(food > 0)
			turnsToGrowth = (int) Math.floor((double) foodForGrowth / food);
		else
			turnsToGrowth = 10000;
		
		return turnsToGrowth;
	}
}
