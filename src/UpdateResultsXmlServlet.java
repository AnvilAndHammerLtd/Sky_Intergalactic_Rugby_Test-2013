
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import team.Team;

/**
 * Servlet implementation class UpdateResultsXml
 */
@WebServlet("/UpdateResultsXmlServlet")
public class UpdateResultsXmlServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	String xmlFilePath = "C:/skyTask/scores.xml";
	File xmlFile = new File(xmlFilePath);

    /**
     * @see HttpServlet#HttpServlet()
     */
    public UpdateResultsXmlServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//get users data input
		String homeTeamName = request.getParameter("homeTeamName");
		String homeTeamScore = request.getParameter("homeTeamScore");
		String awayTeamName = request.getParameter("awayTeamName");
		String awayTeamScore = request.getParameter("awayTeamScore");
		
		insertNewMatchResult(homeTeamName, homeTeamScore, awayTeamName, awayTeamScore);
		
		request.getSession().setAttribute("scoreBoard", (Object) readScores());		
		response.sendRedirect("index.jsp");
	}
	
	//reads the scores from the xml file do the necessary calculations
	//in the end returns an arraylist with the teams which they also include the values of the score board
	public ArrayList<Team> readScores(){
        String[] teamNames = {
        		"Dantooine Destroyers",
        		"Wayland Warriors",
        		"Coruscant Crusaders",
        		"Tatooine Travellers",
        		"Romulas Rejects",
        		"Tottenham Skaro",
        		"Dynamo New Earth",
        		"Dagobah Dragons",
        		"Rura Penthe Massive",
        		"Cardassia Prime Wanderers"
        };
        
		//create our arraylist which will contain all the teams and there stats
        ArrayList<Team> teamsArray = new ArrayList<Team>();
        
        for(int i=0; i<teamNames.length; i++){
        	teamsArray.add(new Team(teamNames[i]));
        }
        
		try{
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
	        DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
	        Document doc = docBuilder.parse (xmlFile);
	        
	        //get all the names and scores from the matches
	        NodeList nameNodes = doc.getElementsByTagName("name");
	        NodeList scoreNodes = doc.getElementsByTagName("score");
	        
	        int matchCounter = 0;
	        //loop through all the matches
	        for(int i=0; i<nameNodes.getLength(); i=i+2){
	        	matchCounter++;
	        	System.out.println("MATCH " + matchCounter);
	    		
	        	//first team
	        	String firstTeamName = nameNodes.item(i).getTextContent();
	        	int firstTeamScore = Integer.parseInt(scoreNodes.item(i).getTextContent());
        		
        		//second team - which is the node just after the previous node
        		String secondTeamName = nameNodes.item(i+1).getTextContent();
        		int secondTeamScore = Integer.parseInt(scoreNodes.item(i+1).getTextContent());
        		
	        	//compare the scores of the first team with the score of the second team that played
	        	if(firstTeamScore > secondTeamScore){
	        		//first team won
	        		updateWin(teamsArray,firstTeamName, firstTeamScore, secondTeamName, secondTeamScore);
	        	}
	        	else if(firstTeamScore < secondTeamScore){
	        		//second team won
	        		updateWin(teamsArray, secondTeamName, secondTeamScore, firstTeamName, firstTeamScore);
	        	}
	        	else{
	        		//draw
	        		updateDraw(teamsArray, secondTeamName, secondTeamScore, firstTeamName, firstTeamScore);
	        	}
	        }//for
		}//try
		catch(Exception e){
			//Do something with the error
		};
		//sort our teams by overall points and if the teams have equal overall points then sort by totalGoals
		Collections.sort(teamsArray);
		return teamsArray;
	}
	
	//it's called when ever one of the two teams win and it updates the status of both teams in the league table
	public void updateWin(ArrayList<Team> teamsArray, String winningTeamName, int winningTeamScore, String lostTeamName, int lostTeamScore){
		//variable to check if the status of the winning team and the team that has lost have been updated
		//when equal to 2 it will mean that both teams have updated there status
		int areUpdated = 0;
		System.out.println("Winning Team Name: " + winningTeamName);
		System.out.println("Winning Team Score: " + winningTeamScore);
		System.out.println("Lost Team Name: " + lostTeamName);
		System.out.println("Lost Team Score: " + lostTeamScore);
		System.out.println();
		
		//find the current winning team index in the arraylist
		for(int j=0; j<teamsArray.size(); j++){
			//if winning team name is equal to the current team in the loop
			//then make the changes to this team values
			if(winningTeamName.equals(teamsArray.get(j).getName())){
				//get current team that won
				Team winningTeam = teamsArray.get(j);
				
				//winning team make the changes - games played, games won, overall points
				winningTeam.setGamesPlayed(winningTeam.getGamesPlayed()+1);
				winningTeam.setGamesWon(winningTeam.getGamesWon()+1);
				winningTeam.setOverallPoints(winningTeam.getOverallPoints()+3);
				winningTeam.setTotalGoals(winningTeam.getTotalGoals() + winningTeamScore);
				
				areUpdated++;
				if(areUpdated == 2){
					//both teams values have been updated so lets get out of the loop
					break;
				}
				continue;
			}//if
			//if the lost team name is equal to the current team name in the loop
			//then make the changes to this team values
			if(lostTeamName.equals(teamsArray.get(j).getName())){
				//get current team that lost
				Team lostTeam = teamsArray.get(j);
				
				//winning team make the changes - games played, games won, overall points
				lostTeam.setGamesPlayed(lostTeam.getGamesPlayed()+1);
				lostTeam.setGamesLost(lostTeam.getGamesLost()+1);
				lostTeam.setTotalGoals(lostTeam.getTotalGoals() + lostTeamScore);

				areUpdated++;
				if(areUpdated == 2){
					//both teams values have been updated so lets get out of the loop
					break;
				}
			}//if
		}//for
	}
	
	//called when ever the teams have the same score
	public void updateDraw(ArrayList<Team> teamsArray, String firstTeamName, int firstTeamScore, String secondTeamName, int secondTeamScore){
		//variable to check if the status of the winning team and the team that has lost have been updated
		//when equal to 2 it will mean that both teams have updated there status
		int areUpdated = 0;
		System.out.println("DRAW");
		System.out.println("First Team Name: " + firstTeamName);
		System.out.println("First Team Score: " + firstTeamScore);
		System.out.println("Second Team Name: " + secondTeamName);
		System.out.println("Second Team Score: " + secondTeamScore);		
		System.out.println();
		
		//find the current teams index in the arraylist
		for(int j=0; j<teamsArray.size(); j++){
			//if winning team name is equal to the current team in the loop
			//then make the changes to this team values
			if(firstTeamName.equals(teamsArray.get(j).getName())){
				//get current team that won
				Team firstTeam = teamsArray.get(j);
				
				//first team make the changes - games played, games drawn, overall points
				firstTeam.setGamesPlayed(firstTeam.getGamesPlayed()+1);
				firstTeam.setGamesDrawn(firstTeam.getGamesDrawn()+1);
				firstTeam.setOverallPoints(firstTeam.getOverallPoints()+1);
				firstTeam.setTotalGoals(firstTeam.getTotalGoals() + firstTeamScore);

				areUpdated++;
				if(areUpdated == 2){
					//both teams values have been updated so lets get out of the loop
					break;
				}
				continue;
			}//if
			//if the lost team name is equal to the current team name in the loop
			//then make the changes to this team values
			if(secondTeamName.equals(teamsArray.get(j).getName())){
				//get current team that lost
				Team secondTeam = teamsArray.get(j);
				
				//second team make the changes - games played, games drawn, overall points
				secondTeam.setGamesPlayed(secondTeam.getGamesPlayed()+1);
				secondTeam.setGamesDrawn(secondTeam.getGamesDrawn()+1);
				secondTeam.setOverallPoints(secondTeam.getOverallPoints()+1);
				secondTeam.setTotalGoals(secondTeam.getTotalGoals() + secondTeamScore);
				
				areUpdated++;
				if(areUpdated == 2){
					//both teams values have been updated so lets get out of the loop
					break;
				}
			}//if
		}//for
	}
	
	
	//write the new match results into the XML file 
	public void insertNewMatchResult(String homeTeamName, String homeTeamScore, String awayTeamName, String awayTeamScore){
		try{
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
	        DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
	        Document doc;
	        //check if a directory which contains the XML file exist, if not one is created
	        if (xmlFile.exists() == false){
	            //create file as it doesn't exist
	        	File skyTaskFolder = new File("C:/skyTask");
	        	skyTaskFolder.mkdirs();
		        doc = docBuilder.newDocument();
		        
		        //Create <results> element
		        Element resultsElement = doc.createElement("results");
		        doc.appendChild(resultsElement);
		        //Create <title> element
		        Element titleElement = doc.createElement("title");
		        titleElement.setTextContent("Results");
		        resultsElement.appendChild(titleElement);
		    }
	        else{
	        	//xml file already exists so lets open that file so we can append our values
		        doc = docBuilder.parse(xmlFile);
	        }
		        //get the results node so we can append the new result
		        Node resultsNode = doc.getFirstChild();		        
			    
		        //Get how many result nodes we have so we can increment the id value
		        int totalResultNodes = doc.getElementsByTagName("result").getLength();
		        
		        
		        //Create result element which represents the results of a match
		        Element resultElement = doc.createElement("result");
		        //set the unique id attribute of this result
		        resultElement.setAttribute("id", (totalResultNodes+1) + "");
		        //append the new result element to the results element
		        resultsNode.appendChild(resultElement);
		        
		        	//creating the homeTeam element and its content
		        Element homeTeamElement = doc.createElement("homeTeam");
		        
		        //set value and append the name element
		        Element homeTeamNameElement = doc.createElement("name");
		        homeTeamNameElement.setTextContent(homeTeamName);
		        homeTeamElement.appendChild(homeTeamNameElement);
		        
		        //set value and append the score element
		        Element homeTeamScoreElement = doc.createElement("score");
		        homeTeamScoreElement.setTextContent(homeTeamScore);
		        homeTeamElement.appendChild(homeTeamScoreElement);
		        resultElement.appendChild(homeTeamElement);
		       
		        	//creating the awayTeam parent
		        Element awayTeamElement = doc.createElement("awayTeam");
		        
		        //set value and append the name element
		        Element awayTeamNameElement = doc.createElement("name");
		        awayTeamNameElement.setTextContent(awayTeamName);
		        awayTeamElement.appendChild(awayTeamNameElement);
		        
		        //set value and append the score element
		        Element awayTeamScoreElement = doc.createElement("score");
		        awayTeamScoreElement.setTextContent(awayTeamScore);
		        awayTeamElement.appendChild(awayTeamScoreElement);
		        resultElement.appendChild(awayTeamElement);
		        
	        //Output the XML
	        //set up a transformer
	        TransformerFactory transfac = TransformerFactory.newInstance();
	        Transformer trans = transfac.newTransformer();
	      
	        trans.setOutputProperty(OutputKeys.INDENT, "yes");
	        trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
	        trans.setOutputProperty(OutputKeys.METHOD, "xml");
	        trans.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-1");
	        trans.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
		    
		    //create string from xml tree
	        StringWriter sw = new StringWriter();
	        StreamResult result = new StreamResult(sw);
	        DOMSource source = new DOMSource(doc);
	        trans.transform(source, result);
	        String xmlString = sw.toString();
	        
	        // write this values to the XML file
	        OutputStreamWriter oos = new OutputStreamWriter (new FileOutputStream(xmlFilePath));
	        oos.write (xmlString);
	        oos.close();
		}
		catch(Exception e){
			//Do something with the error
		};
	}
}
