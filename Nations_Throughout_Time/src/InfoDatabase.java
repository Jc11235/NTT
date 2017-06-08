import java.util.Random;

public class InfoDatabase {
	protected Random r;
	
	private String[] cityNames;
	
	public InfoDatabase(){
		
		r = new Random();
		
		cityNames = new String[] {"Boston","New York","Miami"};
	}

	//getters
	public String getRandomCityName(){
		return cityNames[r.nextInt(cityNames.length)];		
	}
}
