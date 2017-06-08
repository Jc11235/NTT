import java.awt.Color;

public class Settler extends Unit {
	
	public Settler(int[] xyBounds){
		
		x = 0;
		y = 0;
		
		usedSpecial = 1;
		
		movementPoints = 5;
		maxMovementPoints = movementPoints;
		productionCost = 20;
		
		specialAbility = "Settle";
		name = "Settler";
		
		color = new Color(139,200,200);
		
		coordinateBounds = xyBounds;
		
		impassableTerrain = new String[] {"Ocean","Coast"};	
	}
}
