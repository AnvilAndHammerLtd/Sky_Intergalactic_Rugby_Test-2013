package team;


public class Team implements Comparable<Team>{
	private String name;
	private int gamesPlayed = 0, gamesWon = 0, gamesDrawn = 0, gamesLost = 0, overallPoints = 0, totalGoals = 0;
	//constructor
	public Team(String name){
		this.name = name;
	}

			
	//Getters
	public String getName(){
		return name;
	}
	public int getGamesPlayed(){
		return gamesPlayed;
	}
	public int getGamesWon(){
		return gamesWon;
	}
	public int getGamesDrawn(){
		return gamesDrawn;
	}
	public int getGamesLost(){
		return gamesLost;
	}
	public int getOverallPoints(){
		return overallPoints;
	}
	public int getTotalGoals(){
		return totalGoals;
	}
	
	//Setters
	public void setGamesPlayed(int gamesPlayed){
		this.gamesPlayed = gamesPlayed;
	}
	public void setGamesWon(int gamesWon){
		this.gamesWon = gamesWon;
	}
	public void setGamesDrawn(int gamesDrawn){
		this.gamesDrawn = gamesDrawn;
	}
	public void setGamesLost(int gamesLost){
		this.gamesLost = gamesLost;
	}
	public void setOverallPoints(int overallPoints){
		this.overallPoints = overallPoints;
	}
	public void setTotalGoals(int totalGoals){
		this.totalGoals = totalGoals;
	}

	@Override
	public int compareTo(Team team)throws ClassCastException {
			//check if a team object is given
		 	if (!(team instanceof Team))
		      throw new ClassCastException("A Team object expected.");
		    int teamOverallPoints = team.getOverallPoints();
		    int teamTotalGoals = team.getTotalGoals();
		    
		    //if overall points are equal the sort by total goals
		    if(teamOverallPoints == this.overallPoints){
		    	return  teamTotalGoals - this.totalGoals;
		    }
		    else{
		    	return  teamOverallPoints - this.overallPoints; 
		    }		    
	}
}