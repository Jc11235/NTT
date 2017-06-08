import java.awt.Color;

public class Scout extends Unit {
	
	public Scout(int[] xyBounds){
		
		x = 0;
		y = 0;
		
		usedSpecial = 1;
		
		movementPoints = 7;
		maxMovementPoints = movementPoints;
		productionCost = 10;
		
		specialAbility = "Extra Move";
		name = "Scout";
		
		color = new Color(150,10,200);
		
		coordinateBounds = xyBounds;
		
		impassableTerrain = new String[] {"Ocean","Coast"};		
	}
}
