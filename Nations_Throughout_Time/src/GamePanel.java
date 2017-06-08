import java.awt.*;
import java.util.*;  
import javax.swing.*; 
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.KeyEvent;
import java.awt.Font;

public class GamePanel extends JPanel implements KeyListener,MouseMotionListener,MouseListener {
	
	DataManagement dataManagement;
	
	private int tileWidth;
	private int tileHeight;
	private int mapWidth;
	
	private Font playerData;
	
	public GamePanel(){
		
		tileWidth = tileHeight = 15;
		mapWidth = 1200;
		
		playerData = new Font("Algerian",Font.BOLD,12);	
	}
	
	//setters
	public void setDataManagement(DataManagement dm){
		dataManagement = dm;
	}
	
	//getters
	
	//other functions
		
	public void paintComponent(Graphics g)
	{
		g.setColor(Color.WHITE);
		g.fillRect(0,0,1600,900);
	
		//paint terrain
		for(int i = 0; i < dataManagement.getMapHeight(); i++){
			for(int j = 0; j < dataManagement.getMapWidth(); j++){			
				if(dataManagement.getTerrain()[i][j].getVisibility() == 1){
					//paint terrain types
					if(Objects.equals(dataManagement.getTerrain()[i][j].getTerrainType(), "Grassland"))
						g.setColor(Color.green);
					else if(Objects.equals(dataManagement.getTerrain()[i][j].getTerrainType(), "Desert"))
						g.setColor(Color.yellow);
					else if(Objects.equals(dataManagement.getTerrain()[i][j].getTerrainType(), "Jungle"))
						g.setColor(new Color(51,102,0));
					else if(Objects.equals(dataManagement.getTerrain()[i][j].getTerrainType(), "Plains"))
						g.setColor(new Color(204,204,0));
					else if(Objects.equals(dataManagement.getTerrain()[i][j].getTerrainType(), "Tundra"))
						g.setColor(new Color(128,128,128));
					else if(Objects.equals(dataManagement.getTerrain()[i][j].getTerrainType(), "Snow"))
						g.setColor(new Color(215,255,255));
					else if(Objects.equals(dataManagement.getTerrain()[i][j].getTerrainType(), "Ocean"))
						g.setColor(Color.blue);
					else if(Objects.equals(dataManagement.getTerrain()[i][j].getTerrainType(), "Coast"))
						g.setColor(new Color(0,204,204));
					
					g.fillRect(j * tileWidth,i * tileHeight,tileWidth,tileHeight);
					
					//paint mountains
					if(Objects.equals(dataManagement.getTerrain()[i][j].getMountainFlag(), 1)){
						g.setColor(new Color(139,69,19));
						
						int [] x = {j*tileWidth,j*tileWidth + (tileWidth / 2), j * tileWidth + tileWidth};
						int [] y = {i*tileHeight + (tileHeight / 2),i*tileHeight, i * tileHeight + (tileHeight / 2)};
						
						g.fillPolygon(x,y,3);
					}
					
					//pain forests
					if(Objects.equals(dataManagement.getTerrain()[i][j].getForestFlag(), 1)){
						g.setColor(new Color(0,153,0));
						
						g.fillRect(j * tileWidth + (tileWidth / 4) ,i * tileHeight + (tileHeight / 4),tileWidth / 2,tileHeight / 2);
					}
					
					//paints farms
					if(Objects.equals(dataManagement.getTerrain()[i][j].getTileImprovement(), "Farm")){
						g.setColor(new Color(255,255,0));						
						g.fillRect(j * tileWidth,i* tileHeight,tileWidth / 2,tileHeight / 2);
				
						g.setColor(new Color(255,51,51));						
						g.fillRect(j * tileWidth + (tileWidth / 2) ,i * tileHeight,tileWidth / 2,tileHeight / 2);
				
						g.setColor(new Color(255,52,255));						
						g.fillRect(j * tileWidth ,i * tileHeight + (tileHeight / 2),tileWidth / 2,tileHeight / 2);
				
						g.setColor(new Color(0,204,0));						
						g.fillRect(j * tileWidth + (tileWidth / 2) ,i * tileHeight + (tileHeight / 2),tileWidth / 2,tileHeight / 2);			
					}
				}
				else{
					g.setColor(Color.black);
					g.fillRect(j * tileWidth,i * tileHeight,tileWidth,tileHeight);
				}			
			}
		}
	
		for(int p = 0; p < dataManagement.getPlayers().size(); p++){
			//paint cities
			for(int i = 0; i < dataManagement.getPlayers().get(p).getCities().size(); i++){
				
				for(int j = 0; j < dataManagement.getPlayers().get(p).getCities().get(i).getCityTiles().size(); j++){
					g.setColor(new Color(0.1f,0.1f,0.8f,0.3f));
					
					g.fillRect(dataManagement.getPlayers().get(p).getCities().get(i).getCityTiles().get(j).getX() * tileWidth,dataManagement.getPlayers().get(p).getCities().get(i).getCityTiles().get(j).getY()  * tileHeight,tileWidth,tileHeight);
				}
				
				g.setColor(Color.orange);
				g.fillRect(dataManagement.getPlayers().get(p).getCities().get(i).getX() * tileWidth + (tileWidth / 4), dataManagement.getPlayers().get(p).getCities().get(i).getY()* tileHeight + (tileHeight / 4), tileWidth / 2, tileHeight / 2);		
			}
			
			//paint units
			for(int i = 0; i < dataManagement.getPlayers().get(p).getUnits().size(); i++){
				g.setColor(dataManagement.getPlayers().get(p).getUnits().get(i).getColor());
				g.fillOval(dataManagement.getPlayers().get(p).getUnits().get(i).getX() * tileWidth + (tileWidth / 4), dataManagement.getPlayers().get(p).getUnits().get(i).getY()* tileHeight + (tileHeight / 4), tileWidth / 2, tileHeight / 2);
				
				if(i == dataManagement.getCurrentUnit()){
					g.setColor(Color.blue);
					g.drawOval(dataManagement.getPlayers().get(p).getUnits().get(i).getX() * tileWidth, dataManagement.getPlayers().get(p).getUnits().get(i).getY()* tileHeight, tileWidth, tileHeight);
				}
			}
		}
	
		
		//paint info panel
		g.setColor(Color.gray);
		g.fillRect(mapWidth, 0, 300, 900);
		
		g.setColor(Color.white);
		g.setFont(playerData);
		
		//game info
		g.drawString("Turn: " + dataManagement.getTurnNumber(), 1225, 50);
		
		//terrain info
		if(dataManagement.getTerrain()[dataManagement.getCurrentTerrainY()][dataManagement.getCurrentTerrainX()].getVisibility() == 1){
			g.drawString("Terrain: " + dataManagement.getTerrain()[dataManagement.getCurrentTerrainY()][dataManagement.getCurrentTerrainX()].getTerrainType(), 1225, 75);
			
			if(dataManagement.getTerrain()[dataManagement.getCurrentTerrainY()][dataManagement.getCurrentTerrainX()].getForestFlag() == 1)
				g.drawString("Forest", 1350, 75);
			else if(dataManagement.getTerrain()[dataManagement.getCurrentTerrainY()][dataManagement.getCurrentTerrainX()].getMountainFlag() == 1)
				g.drawString("Mountain", 1350, 75);
			
			g.drawString("Food: " + dataManagement.getTerrain()[dataManagement.getCurrentTerrainY()][dataManagement.getCurrentTerrainX()].getFood(), 1225, 100);
			g.drawString("Production: " + dataManagement.getTerrain()[dataManagement.getCurrentTerrainY()][dataManagement.getCurrentTerrainX()].getProduction(), 1225, 125);
			g.drawString("Value: " + dataManagement.getTerrain()[dataManagement.getCurrentTerrainY()][dataManagement.getCurrentTerrainX()].getValue(), 1225, 150);
			g.drawString("Science: " + dataManagement.getTerrain()[dataManagement.getCurrentTerrainY()][dataManagement.getCurrentTerrainX()].getScience(), 1225, 175);
			g.drawString("Movement Cost: " + dataManagement.getTerrain()[dataManagement.getCurrentTerrainY()][dataManagement.getCurrentTerrainX()].getMovementCost(), 1225, 200);

		}
		
		//player info
		g.drawString("Player Info:", 1225, 275);
		g.drawString("Money: " + dataManagement.getPlayers().get(0).getMoney(), 1225, 300);
		g.drawString("Science: " + dataManagement.getPlayers().get(0).getScience(), 1225, 325);
		
		//unit info
		if(dataManagement.getPlayers().get(0).getUnits().size() > 0){
			g.drawString("Unit: " + dataManagement.getPlayers().get(0).getUnits().get(dataManagement.getCurrentUnit()).getName(), 1225, 400);
			g.drawString("MP: " + dataManagement.getPlayers().get(0).getUnits().get(dataManagement.getCurrentUnit()).getMovementPoints(), 1225, 425);
		}
		
		//city info
		if(dataManagement.getPlayers().get(0).getCities().size() > 0){
			g.drawString("City: " + dataManagement.getPlayers().get(0).getCities().get(dataManagement.getCurrentCity()).getName(), 1225, 500);
			g.drawString("Population: " + dataManagement.getPlayers().get(0).getCities().get(dataManagement.getCurrentCity()).getPopulation(), 1225, 525);
			g.drawString("Production: " + dataManagement.getPlayers().get(0).getCities().get(dataManagement.getCurrentCity()).getProduction(), 1225, 550);
			g.drawString("Science: " + dataManagement.getPlayers().get(0).getCities().get(dataManagement.getCurrentCity()).getScience(), 1225, 575);
			g.drawString("Food: " + dataManagement.getPlayers().get(0).getCities().get(dataManagement.getCurrentCity()).getFood(), 1225, 600);
			g.drawString("Turns To Growth: " + dataManagement.getPlayers().get(0).getCities().get(dataManagement.getCurrentCity()).turnsToGrowth(), 1225, 625);

			if(dataManagement.getPlayers().get(0).getCities().get(dataManagement.getCurrentCity()).getBuildQueue().size() > 0)
				g.drawString("Constructing: " + dataManagement.getPlayers().get(0).getCities().get(dataManagement.getCurrentCity()).getBuildQueue().get(0).getName(), 1225, 650);
			else
				g.drawString("Constructing: none", 1225, 650);
			
			g.drawString("Turns To Build: " + dataManagement.getPlayers().get(0).getCities().get(dataManagement.getCurrentCity()).turnsToBuildItem(), 1225, 675);
		}
		
		//paint buttons
		g.drawRect(1250, 750, 100, 50);
		g.drawString("End Turn", 1275, 775);

	}
	public void keyPressed(KeyEvent e){

		if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
			System.exit(0);
		else if(e.getKeyCode() == KeyEvent.VK_UP){
			if(dataManagement.getPlayers().get(0).getUnits().size() > 0)
				dataManagement.getPlayers().get(0).getUnits().get(dataManagement.getCurrentUnit()).moveUnit(0,-1,dataManagement.getTerrain()[dataManagement.getPlayers().get(0).getUnits().get(dataManagement.getCurrentUnit()).getY() - 1][dataManagement.getPlayers().get(0).getUnits().get(dataManagement.getCurrentUnit()).getX()].getMovementCost(),dataManagement.getTerrain()[dataManagement.getPlayers().get(0).getUnits().get(dataManagement.getCurrentUnit()).getY() - 1][dataManagement.getPlayers().get(0).getUnits().get(dataManagement.getCurrentUnit()).getX()].getTerrainType());
		}
		else if(e.getKeyCode() == KeyEvent.VK_DOWN){
			if(dataManagement.getPlayers().get(0).getUnits().size() > 0)
				dataManagement.getPlayers().get(0).getUnits().get(dataManagement.getCurrentUnit()).moveUnit(0,1,dataManagement.getTerrain()[dataManagement.getPlayers().get(0).getUnits().get(dataManagement.getCurrentUnit()).getY() + 1][dataManagement.getPlayers().get(0).getUnits().get(dataManagement.getCurrentUnit()).getX()].getMovementCost(),dataManagement.getTerrain()[dataManagement.getPlayers().get(0).getUnits().get(dataManagement.getCurrentUnit()).getY() + 1][dataManagement.getPlayers().get(0).getUnits().get(dataManagement.getCurrentUnit()).getX()].getTerrainType());
		}
		if(e.getKeyCode() == KeyEvent.VK_RIGHT){
			if(dataManagement.getPlayers().get(0).getUnits().size() > 0)
				dataManagement.getPlayers().get(0).getUnits().get(dataManagement.getCurrentUnit()).moveUnit(1,0,dataManagement.getTerrain()[dataManagement.getPlayers().get(0).getUnits().get(dataManagement.getCurrentUnit()).getY()][dataManagement.getPlayers().get(0).getUnits().get(dataManagement.getCurrentUnit()).getX() + 1].getMovementCost(),dataManagement.getTerrain()[dataManagement.getPlayers().get(0).getUnits().get(dataManagement.getCurrentUnit()).getY()][dataManagement.getPlayers().get(0).getUnits().get(dataManagement.getCurrentUnit()).getX() + 1].getTerrainType());
		}		
		if(e.getKeyCode() == KeyEvent.VK_LEFT){
			if(dataManagement.getPlayers().get(0).getUnits().size() > 0)
				dataManagement.getPlayers().get(0).getUnits().get(dataManagement.getCurrentUnit()).moveUnit(-1,0,dataManagement.getTerrain()[dataManagement.getPlayers().get(0).getUnits().get(dataManagement.getCurrentUnit()).getY()][dataManagement.getPlayers().get(0).getUnits().get(dataManagement.getCurrentUnit()).getX() - 1].getMovementCost(),dataManagement.getTerrain()[dataManagement.getPlayers().get(0).getUnits().get(dataManagement.getCurrentUnit()).getY()][dataManagement.getPlayers().get(0).getUnits().get(dataManagement.getCurrentUnit()).getX() - 1].getTerrainType());
		}		
		if(e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_RIGHT)
			dataManagement.checkTerrainVisibility();
		
		if(e.getKeyCode() == KeyEvent.VK_V)
			dataManagement.setAllTerrainVisible();
		if(e.getKeyCode() == KeyEvent.VK_S)
			dataManagement.useSpecialAbility();
	}
	public void keyReleased(KeyEvent e)
	{

	}
	public void keyTyped(KeyEvent e)
	{

	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
						
		int mapX = (int) Math.floor(e.getX() / tileWidth);
		int mapY = (int) Math.floor((e.getY() - 25) / tileHeight);
	
		if(mapY > -1 && mapY < dataManagement.getMapHeight() && mapX > -1 && mapX < dataManagement.getMapWidth()){
			dataManagement.setCurrentTerrainX(mapX);
			dataManagement.setCurrentTerrainY(mapY);
		}			
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
		int mapX = (int) Math.floor(e.getX() / tileWidth);
		int mapY = (int) Math.floor((e.getY() - 25) / tileHeight);
		
		dataManagement.setCurrentUnit(mapX, mapY);
		dataManagement.setCurrentCity(mapX, mapY);
		
		if(e.getX() > 1250 && e.getX() < 1350 && e.getY() > 750 && e.getY() < 800)
			dataManagement.endTurn();
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}
