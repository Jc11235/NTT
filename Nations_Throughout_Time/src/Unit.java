import java.awt.Color;
import java.util.Objects;

public class Unit{
	
	protected int x;
	protected int y;
	protected int movementPoints;
	protected int maxMovementPoints;
	protected int usedSpecial;
	protected int productionCost;
	
	protected String specialAbility;
	protected String name;
	
	protected Color color;
	
	protected int[] coordinateBounds;
	
	protected String[] impassableTerrain;
	
	public Unit(){
		
	}
	
	//setters
	public void setX(int newX){
		x = newX;
	}
	public void setY(int newY){
		y = newY;
	}
	public void setMovementPoints(int newPoints){
		movementPoints = newPoints;
	}
	public void setUsedSpecial(int newToggle){
		usedSpecial = newToggle;
	}
	public void setProductionCost(int newCost){
		productionCost = newCost;
	}
	//getters
	public int getX(){
		return x;
	}
	public int getY(){
		return y;
	}
	public int getMovementPoints(){
		return movementPoints;
	}
	public int getMaxMovementPoints(){
		return maxMovementPoints;
	}	
	public int getUsedSpecial(){
		return usedSpecial;
	}
	public int getProductionCost(){
		return productionCost;
	}
	
	public String getName(){
		return name;
	}
	public String getSpecialAbility(){
		return specialAbility;
	}
	public Color getColor(){
		return color;
	}
	
	//other functions
	public void moveUnit(int deltaX, int deltaY,int cost,String terrainType){
		boolean flag = false;
			
		int tempX = x + deltaX;
		int tempY = y + deltaY;
			
		if(tempY > coordinateBounds[0] - 1 && tempY < coordinateBounds[1] && tempX > coordinateBounds[2] - 1 &&  tempX < coordinateBounds[3]){		
			
			flag = checkTerrain(terrainType);
			
			if(flag == false){
				if(movementPoints - cost > -1){
					x = tempX;
					y = tempY;
					
					movementPoints -= cost;
				}
			}
		}
	}
	public boolean checkTerrain(String terrainType){
		boolean flag = false;
		
		for(int i = 0; i < impassableTerrain.length; i++){
			if(Objects.equals(terrainType, impassableTerrain[i])){
				flag = true;
				
				break;
			}
		}
		
		return flag;
	}
}
