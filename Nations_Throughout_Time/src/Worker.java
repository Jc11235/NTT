import java.awt.Color;

public class Worker extends Unit{
	public Worker(int[] xyBounds){
		
		x = 0;
		y = 0;
		
		usedSpecial = 1;
		
		movementPoints = 4;
		maxMovementPoints = movementPoints;
		productionCost = 20;
		
		specialAbility = "Improve Tile";
		name = "Worker";
		
		color = new Color(230,0,200);
		
		coordinateBounds = xyBounds;
		
		impassableTerrain = new String[] {"Ocean","Coast"};		
	}
}
