import java.util.Objects;
import java.util.Random;
import java.util.ArrayList;

public class DataManagement {
	
	private int mapHeight;
	private int mapWidth;
	private int terrainVisibilityToggle;
	private int currentTerrainX;
	private int currentTerrainY;
	private int currentUnit;
	private int currentCity;
	private int currentPlayer;
	private int turnNumber;
	
	protected Random r;
	
	private int[] mapSeed;//temperature scale
	private int[] numberOfBiomes;
	private int[] octaves;
	private int[] mapBounds;
		
	private int[][] biomes;
	
	private int [][][] biomeRanges;
	
	private Terrain[][] terrain;
	
	private ArrayList<Player> players;
	
	private InfoDatabase infoDB;
		
	public DataManagement(){
		
		players = new ArrayList<Player>();
		
		infoDB = new InfoDatabase();
		
		mapHeight = 50;
		mapWidth = 80;
		turnNumber = 0;
		terrainVisibilityToggle = 0;
		currentTerrainY = 0;
		currentTerrainX = 0;
		currentUnit = 0;
		currentCity = 0;
		currentPlayer = 0;
		turnNumber = 0;
		
		r = new Random();
		
		mapSeed = new int[1];
		numberOfBiomes = new int[2];
		octaves = new int[2];
		mapBounds = new int[] {0,mapHeight,0,mapWidth};
		
		octaves[0] = r.nextInt(10) + 10;
		octaves[1] = r.nextInt(10) + 10;
		
		numberOfBiomes[0] = r.nextInt(5) + octaves[0];
		numberOfBiomes[1] = r.nextInt(5) + octaves[1];
		
		biomes = new int[numberOfBiomes[0]][numberOfBiomes[1]];
		
		biomeRanges = new int[numberOfBiomes[0]][numberOfBiomes[1]][4];

		for(int i = 0; i < numberOfBiomes[0]; i++){
			for(int j = 0; j < numberOfBiomes[1]; j++){
				int flag = r.nextInt(10) + 1;
				
				if(flag < 3)
					biomes[i][j] = 0;
				else if(flag >= 3 && flag < 6)
					biomes[i][j] = 3;
				else if(flag >= 6 && flag < 8)
					biomes[i][j] = 1;
				else if(flag >= 8)
					biomes[i][j] = 2;
						
				biomeRanges[i][j][0] = (mapHeight / numberOfBiomes[0])* i; 
				biomeRanges[i][j][1] = (mapHeight / numberOfBiomes[0])* (i + 1);
				biomeRanges[i][j][2] = (mapWidth / numberOfBiomes[1])* j; 
				biomeRanges[i][j][3] = (mapWidth / numberOfBiomes[1])* (j + 1);
			}				
		}
		
		mapSeed[0] = r.nextInt(4) + 1;
		
		terrain  = new Terrain[mapHeight][mapWidth];
		
		players.add(new Player());
			
		setupGame();		
	}
	
	//setters
	public void setCurrentTerrainX(int newX){
		currentTerrainX = newX;
	}
	public void setCurrentTerrainY(int newY){
		currentTerrainY = newY;
	}
	public void setCurrentUnit(int newIndex){
		currentUnit = newIndex;
	}
	public void setCurrentCity(int newIndex){
		currentCity = newIndex;
	}
	public void setTurnNumber(){
		turnNumber++;
	}
	
	//getters
	public int getMapHeight(){
		return mapHeight;
	}
	public int getMapWidth(){
		return mapWidth;
	}
	public int getCurrentTerrainX(){
		return currentTerrainX;
	}
	public int getCurrentTerrainY(){
		return currentTerrainY;
	}
	public int getCurrentUnit(){
		return currentUnit;
	}
	public int getCurrentCity(){
		return currentCity;
	}
	public int getTurnNumber(){
		return turnNumber;
	}
	
	public Terrain[][] getTerrain(){
		return terrain;
	}
	
	public ArrayList<Player> getPlayers(){
		return players;
	}
		
	//general game data *******************************
	public void setupGame(){
			
		generateTerrain();
		
		checkForCoastalWater();
		
		spawnUnits();
		
		checkTerrainVisibility();
	}
	
	public void endTurn(){
		turnNumber++;
		
		for(int k = 0; k < players.size(); k++){
			for(int i = 0; i < players.get(k).getUnits().size(); i++){
				players.get(k).getUnits().get(i).setMovementPoints(players.get(k).getUnits().get(i).getMaxMovementPoints());
				
				players.get(k).getUnits().get(currentUnit).setUsedSpecial(1);
			}
			
			for(int i = 0; i < players.get(k).getCities().size(); i++){
				//build units
				if(players.get(k).getCities().get(i).getBuildQueue().size() > 0){
					players.get(k).getCities().get(i).getBuildQueue().get(0).setProductionCost(players.get(k).getCities().get(i).getBuildQueue().get(0).getProductionCost() - players.get(k).getCities().get(i).getProduction());
					
					if(players.get(k).getCities().get(i).getBuildQueue().get(0).getProductionCost() <= 0){
						players.get(k).getUnits().add(players.get(k).getCities().get(i).getBuildQueue().get(0));
						
						players.get(k).getUnits().get(players.get(k).getUnits().size() - 1).moveUnit(players.get(k).getCities().get(i).getX(), players.get(k).getCities().get(i).getY(),0,"");			
						
						players.get(k).getCities().get(i).getBuildQueue().remove(0);
					}				
				}
				
				//grow city
				if(players.get(k).getCities().get(i).turnsToGrowth() <= 0){
					players.get(k).getCities().get(i).setPopulation(players.get(k).getCities().get(i).getPopulation() + 1);
					
					players.get(k).getCities().get(i).setFoodForGrowth(players.get(k).getCities().get(i).getMaxFoodForGrowth() * players.get(k).getCities().get(i).getPopulation());
					
					players.get(k).getCities().get(i).maximizeYield();
				}
				else
					players.get(k).getCities().get(i).setFoodForGrowth(players.get(k).getCities().get(i).getFoodForGrowth() - players.get(k).getCities().get(i).getFood());
				
			}
			
			players.get(k).calculateYields();
		}
	}
	
	//terrain functions *******************************

	public void generateTerrain(){
		
		int i,j,terrainType,rainFall,forestFlag,waterAmount,biomeIDX,biomeIDY,height;
		
		double temperature;
		
		boolean flag;
		
		waterAmount = 100;
		biomeIDX = biomeIDY = 0;
						
		for(i = 0; i < mapHeight; i++){
			for(j = 0; j < mapWidth; j++){
				
				terrain[i][j] = new Terrain(j,i);
				
				terrainType = r.nextInt(100);
				rainFall = r.nextInt(2);
				height = r.nextInt(5);
				
				temperature = -Math.pow(Math.abs((mapHeight / 2) - i),2) + 100*(5 / mapSeed[0]);
				
				terrain[i][j].setLandHeight(height);
				terrain[i][j].setTemperature(temperature);	
				
				flag = false;			
				
				for(int k = 0; k < numberOfBiomes[0]; k++){
					for(int l = 0; l < numberOfBiomes[1]; l++){										
						if(i >=  biomeRanges[k][l][0] && i < biomeRanges[k][l][1] && j >= biomeRanges[k][l][2] && j < biomeRanges[k][l][3]){
							biomeIDX = l;
							biomeIDY = k;
							
							flag = true;
							
							break;
						}					
					}
					if(flag == true)
						break;
				}
							
				if(biomes[biomeIDY][biomeIDX] == 0) //continents / pangea
					waterAmount = 5;
				else if(biomes[biomeIDY][biomeIDX] == 1) //large islands
					waterAmount = 25;
				else if(biomes[biomeIDY][biomeIDX] == 2) //many island
					waterAmount = 75;
				else if(biomes[biomeIDY][biomeIDX] == 3) //many small islands
					waterAmount = 99;			
				
				if(terrainType <  waterAmount){
					terrain[i][j].setTerrainType("Ocean");
					
					terrain[i][j].setFood(1);
					terrain[i][j].setProduction(0);
					terrain[i][j].setValue(0);
					terrain[i][j].setScience(1);
					
					terrain[i][j].setMovementCost(1);
					
					terrain[i][j].setLandHeight(-1);
				}		
				else if(terrainType >= waterAmount){
					
					if(rainFall == 0 && temperature >= 90){
						terrain[i][j].setTerrainType("Desert");
						
						terrain[i][j].setFood(1);
						terrain[i][j].setProduction(1);
						terrain[i][j].setValue(1);
						terrain[i][j].setScience(1);
						
						terrain[i][j].setMovementCost(3);
					}					
					else if(rainFall == 0 && temperature < 90 && temperature >= 35){
						terrain[i][j].setTerrainType("Plains");
						
						terrain[i][j].setFood(1);
						terrain[i][j].setProduction(2);
						terrain[i][j].setValue(1);
						terrain[i][j].setScience(1);
						
						terrain[i][j].setMovementCost(1);
					}					
					else if(rainFall == 0 && temperature < 35){
						terrain[i][j].setTerrainType("Tundra");
						
						terrain[i][j].setFood(1);
						terrain[i][j].setProduction(0);
						terrain[i][j].setValue(0);
						terrain[i][j].setScience(1);
						
						terrain[i][j].setMovementCost(1);
						
						forestFlag = r.nextInt(5);
						
						if(terrain[i][j].getLandHeight() != 2){
							if(forestFlag > 3){
								terrain[i][j].setForestFlag(1);
								
								terrain[i][j].setValue(terrain[i][j].getValue() + 1);
								terrain[i][j].setProduction(terrain[i][j].getProduction() + 1);
								terrain[i][j].setFood(terrain[i][j].getFood() + 1);
								terrain[i][j].setScience(terrain[i][j].getScience() + 1);
								
								terrain[i][j].setMovementCost(terrain[i][j].getMovementCost() + 1);
							}				
						}
					}			
					else if(rainFall == 1 && temperature >= 90){
						terrain[i][j].setTerrainType("Jungle");
						
						terrain[i][j].setFood(2);
						terrain[i][j].setProduction(1);
						terrain[i][j].setValue(1);	
						terrain[i][j].setScience(2);
						
						terrain[i][j].setMovementCost(2);
					}
					else if(rainFall == 1 && temperature < 90 && temperature >= 30){
						terrain[i][j].setTerrainType("Grassland");
						
						terrain[i][j].setFood(1);
						terrain[i][j].setProduction(1);
						terrain[i][j].setValue(1);
						terrain[i][j].setScience(1);
						
						terrain[i][j].setMovementCost(1);
						
						forestFlag = r.nextInt(5);
						
						if(terrain[i][j].getLandHeight() != 2){
							if(forestFlag > 3){
								terrain[i][j].setForestFlag(1);
								
								terrain[i][j].setValue(terrain[i][j].getValue() + 1);
								terrain[i][j].setProduction(terrain[i][j].getProduction() + 1);
								terrain[i][j].setFood(terrain[i][j].getFood() + 1);
								terrain[i][j].setScience(terrain[i][j].getScience() + 1);
								
								terrain[i][j].setMovementCost(terrain[i][j].getMovementCost() + 1);
							}				
						}
					}
					else if(rainFall == 1 && temperature < 30 && temperature >= -25){
						terrain[i][j].setTerrainType("Tundra");
						
						terrain[i][j].setFood(1);
						terrain[i][j].setProduction(0);
						terrain[i][j].setValue(0);
						terrain[i][j].setScience(1);
						
						terrain[i][j].setMovementCost(1);
						
						forestFlag = r.nextInt(5);
						
						if(terrain[i][j].getLandHeight() != 2){
							if(forestFlag > 3){
								terrain[i][j].setForestFlag(1);
								
								terrain[i][j].setValue(terrain[i][j].getValue() + 1);
								terrain[i][j].setProduction(terrain[i][j].getProduction() + 1);
								terrain[i][j].setFood(terrain[i][j].getFood() + 1);
								terrain[i][j].setScience(terrain[i][j].getScience() + 1);
								
								terrain[i][j].setMovementCost(terrain[i][j].getMovementCost() + 1);
							}				
						}
					}
					else if(rainFall == 1 && temperature < -25){
						terrain[i][j].setTerrainType("Snow");
						
						terrain[i][j].setFood(0);
						terrain[i][j].setProduction(1);
						terrain[i][j].setValue(0);
						terrain[i][j].setScience(0);
						
						terrain[i][j].setMovementCost(2);
					}
					
					if(terrain[i][j].getLandHeight() == 2){
						terrain[i][j].setMountainFlag(1);
						
						terrain[i][j].setMovementCost(terrain[i][j].getMovementCost() + 3);
						terrain[i][j].setProduction(terrain[i][j].getProduction() + 2);
						terrain[i][j].setScience(terrain[i][j].getScience() + 1);
					}
						
				}
				
				terrain[i][j].generateResources();
			}
		}	
	}
	public void checkForCoastalWater(){
		
		for(int i = 0; i < mapHeight; i++){
			for(int j = 0; j < mapWidth; j++){						
				for(int k = i - 1; k < i + 2; k++){
					if(Objects.equals(terrain[i][j].getTerrainType(), "Ocean") || Objects.equals(terrain[i][j].getTerrainType(), "Coast"))
						break;
					
					for(int l = j - 1; l < j + 2; l++){				
						if(k > -1 && k < mapHeight && l > -1 && l < mapWidth){
							if(Objects.equals(terrain[k][l].getTerrainType(), "Ocean")){
								
								terrain[k][l].setTerrainType("Coast");
								
								terrain[k][l].setFood(2);
								terrain[k][l].setProduction(1);
							}							
						}
					}
				}
			}
		}
	}

	public void checkTerrainVisibility(){
		for(int i = 0; i < players.get(currentPlayer).getUnits().size(); i++){
			for(int k = players.get(currentPlayer).getUnits().get(i).getY() - 1; k < players.get(currentPlayer).getUnits().get(i).getY() + 2; k++){
				for(int l = players.get(currentPlayer).getUnits().get(i).getX() - 1; l < players.get(currentPlayer).getUnits().get(i).getX() + 2; l++){					
					if(k > -1 && k < mapHeight && l > -1 && l < mapWidth){
						if(terrain[k][l].getVisibility() == 0)
							terrain[k][l].setVisibility(1);
					}
				}
			}
		}
			
		for(int i = 0; i < players.get(currentPlayer).getCities().size(); i++){
			for(int k = players.get(currentPlayer).getCities().get(i).getY() - 2; k < players.get(currentPlayer).getCities().get(i).getY() + 3; k++){
				for(int l = players.get(currentPlayer).getCities().get(i).getX() - 2; l < players.get(currentPlayer).getCities().get(i).getX() + 3; l++){					
					if(k > -1 && k < mapHeight && l > -1 && l < mapWidth){
						if(terrain[k][l].getVisibility() == 0)
							terrain[k][l].setVisibility(1);
					}
				}
			}
		}
	}
	public void setAllTerrainVisible(){
		for(int i = 0; i < mapHeight; i++){
			for(int j = 0; j < mapWidth; j++){
				if(terrainVisibilityToggle == 0)
					terrain[i][j].setVisibility(1);
				else if(terrainVisibilityToggle == 1){
					terrain[i][j].setVisibility(0);
					
					checkTerrainVisibility();
				}
			}						
		}
		
		if(terrainVisibilityToggle == 0)
			terrainVisibilityToggle = 1;
		else if(terrainVisibilityToggle == 1)
			terrainVisibilityToggle = 0;
	}
	
	//units data *******************************
	public void spawnUnits(){
		int spawnX;
		int spawnY;
	
		players.get(0).getUnits().add(new Settler(mapBounds));
		players.get(0).getUnits().add(new Scout(mapBounds));
		
		spawnX = r.nextInt(mapWidth);
		spawnY = r.nextInt(mapHeight);
		
		while(players.get(0).getUnits().get(0).checkTerrain(terrain[spawnY][spawnX].getTerrainType()) == true){
			spawnX = r.nextInt(mapWidth);
			spawnY = r.nextInt(mapHeight);			
		}
		
		players.get(0).getUnits().get(0).moveUnit(players.get(0).getUnits().get(0).getX() + spawnX, players.get(0).getUnits().get(0).getY() + spawnY,0,terrain[spawnY][spawnX].getTerrainType());			
		
		spawnX = r.nextInt(mapWidth);
		spawnY = r.nextInt(mapHeight);
		
		while(players.get(0).getUnits().get(1).checkTerrain(terrain[spawnY][spawnX].getTerrainType()) == true){
			spawnX = r.nextInt(mapWidth);
			spawnY = r.nextInt(mapHeight);
		}
		
		players.get(0).getUnits().get(1).moveUnit(players.get(0).getUnits().get(1).getX() + spawnX, players.get(0).getUnits().get(1).getY() + spawnY,0,terrain[spawnY][spawnX].getTerrainType());			
	
	}
	
	public void setCurrentUnit(int currentX, int currentY){
		for(int i = 0; i < players.get(0).getUnits().size(); i++){
			if(players.get(0).getUnits().get(i).getX() == currentX && players.get(0).getUnits().get(i).getY() == currentY){
				currentUnit = i;
				
				break;
			}
		}
	}
	//units special abilities *******************************
	public void useSpecialAbility(){
		if(players.get(currentPlayer).getUnits().get(currentUnit).getUsedSpecial() == 1){
			players.get(currentPlayer).getUnits().get(currentUnit).setUsedSpecial(0);
			
			if(Objects.equals(players.get(currentPlayer).getUnits().get(currentUnit).getSpecialAbility(), "Settle"))
				settleCity();
			else if(Objects.equals(players.get(currentPlayer).getUnits().get(currentUnit).getSpecialAbility(), "Extra Move"))
				extraMove();
			else if(Objects.equals(players.get(currentPlayer).getUnits().get(currentUnit).getSpecialAbility(), "Improve Tile"))
				improveTile();
		}
	}
	
	public void settleCity(){
		
		players.get(currentPlayer).getCities().add(new City(players.get(currentPlayer).getUnits().get(currentUnit).getX(),players.get(currentPlayer).getUnits().get(currentUnit).getY(),infoDB.getRandomCityName()));
		
		currentCity = players.get(currentPlayer).getCities().size() - 1;
			
		for(int k = players.get(currentPlayer).getUnits().get(currentUnit).getY() - 1; k < players.get(currentPlayer).getUnits().get(currentUnit).getY() + 2; k++){
			for(int l = players.get(currentPlayer).getUnits().get(currentUnit).getX() - 1; l < players.get(currentPlayer).getUnits().get(currentUnit).getX() + 2; l++){
				if(k > -1 && k < mapHeight && l > -1 && l < mapWidth)
					players.get(currentPlayer).getCities().get(currentCity).addCityTile(terrain[k][l]);
			}			
		}
		
		players.get(currentPlayer).getCities().get(players.get(currentPlayer).getCities().size() - 1).maximizeYield();
			
		players.get(currentPlayer).getUnits().remove(currentUnit);
			
		currentUnit = 0;
			
		checkTerrainVisibility();
		
		players.get(currentPlayer).getCities().get(players.get(currentPlayer).getCities().size() - 1).addItemToBuildQueue(new Worker(mapBounds));
	}
	public void extraMove(){
		players.get(currentPlayer).getUnits().get(currentUnit).setMovementPoints(players.get(currentPlayer).getUnits().get(currentUnit).getMovementPoints() + 3);
	}
	public void improveTile(){
		terrain[players.get(currentPlayer).getUnits().get(currentUnit).getY()][players.get(currentPlayer).getUnits().get(currentUnit).getX()].setTileImprovement("Farm");
		
		terrain[players.get(currentPlayer).getUnits().get(currentUnit).getY()][players.get(currentPlayer).getUnits().get(currentUnit).getX()].setFood(terrain[players.get(currentPlayer).getUnits().get(currentUnit).getY()][players.get(currentPlayer).getUnits().get(currentUnit).getX()].getFood() + 1);
	}
	//city data*************************************
	public void setCurrentCity(int currentX, int currentY){

		for(int i = 0; i < players.get(currentPlayer).getCities().size(); i++){
			if(players.get(currentPlayer).getCities().get(i).getX() == currentX && players.get(currentPlayer).getCities().get(i).getY() == currentY){
				currentCity = i;
				
				break;
			}
		}
	}
}
