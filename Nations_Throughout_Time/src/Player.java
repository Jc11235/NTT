import java.util.ArrayList;

public class Player {
	
	private int money;
	private int science;
	
	private ArrayList<Unit> units;
	private ArrayList<City> cities;
	
	public Player(){
		
		money = 0;
		
		units = new ArrayList<Unit>();
		cities = new ArrayList<City>();	
	}
	
	//getters
	public int getMoney(){
		return money;
	}
	public int getScience(){
		return science;
	}
	
	public ArrayList<Unit> getUnits(){
		return units;
	}
	public ArrayList<City> getCities(){
		return cities;
	}
	
	//setters
	public void setMoney(int newMoney){
		money = newMoney;
	}
	public void setScience(int newScience){
		science = newScience;
	}
	
	//other functions
	public void calculateYields(){
		for(int i = 0; i < cities.size(); i++){
			money += cities.get(i).getValue();
			
			science += cities.get(i).getScience();
		}		
	}
}
