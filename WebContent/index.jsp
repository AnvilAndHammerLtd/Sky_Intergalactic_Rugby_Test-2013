<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import="java.util.ArrayList"%>
<%@page import="team.Team"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>

<%
//get the values for the scoreBoard
ArrayList<Team> teamsArray = (ArrayList<Team>) request.getSession().getAttribute("scoreBoard");
%>

<div>
	<form action="UpdateResultsXmlServlet" method="POST">
	
		<div style="float:left;">
			<h1>Home Team</h1><br>
			<select name="homeTeamName">
				<option value="Dantooine Destroyers">Dantooine Destroyers</option>
				<option value="Wayland Warriors">Wayland Warriors</option>
				<option value="Coruscant Crusaders">Coruscant Crusaders</option>
				<option value="Tatooine Travellers">Tatooine Travellers</option>
				<option value="Romulas Rejects">Romulas Rejects</option>
				<option value="Tottenham Skaro">Tottenham Skaro</option>
				<option value="Dynamo New Earth">Dynamo New Earth</option>
				<option value="Dagobah Dragons">Dagobah Dragons</option>
				<option value="Rura Penthe Massive">Rura Penthe Massive</option>
				<option value="Cardassia Prime Wanderers">Cardassia Prime Wanderers</option>
			</select>	
			<input type="text" name="homeTeamScore" size="1">
		</div>
			
		<div style="float:right;">
			<h1 align="right">Away Team</h1><br>
			<input type="text" name="awayTeamScore" size="1">
			<select name="awayTeamName">
				<option value="Dantooine Destroyers">Dantooine Destroyers</option>
				<option value="Wayland Warriors">Wayland Warriors</option>
				<option value="Coruscant Crusaders">Coruscant Crusaders</option>
				<option value="Tatooine Travellers">Tatooine Travellers</option>
				<option value="Romulas Rejects">Romulas Rejects</option>
				<option value="Tottenham Skaro">Tottenham Skaro</option>
				<option value="Dynamo New Earth">Dynamo New Earth</option>
				<option value="Dagobah Dragons">Dagobah Dragons</option>
				<option value="Rura Penthe Massive">Rura Penthe Massive</option>
				<option value="Cardassia Prime Wanderers">Cardassia Prime Wanderers</option>
			</select>	
		</div> 	
		
		<br><br><br>	
		 	
		<div align="center">
		 	 <input type="submit" value="Submit">
		</div>
		<br><br><br>
	</form>
	
	<!-- refresh button -->
	<form action="RefreshResultsServlet" method="POST">
		<input type="submit" value="Refresh Score Board">
	</form>
	
	
	<table border="3" align="center">
		<tr>
			<td>Team</td>
			<td>Played</td>
			<td>Won</td>
			<td>Drawn</td>
			<td>Lost</td>
			<td>Overall Points</td>
		</tr>

<%
//display statistics of the league table
if(teamsArray != null){
	for(int i=0; i<teamsArray.size(); i++){
		System.out.print(teamsArray.get(i).getName() + ": ");
		System.out.println(teamsArray.get(i).getTotalGoals());
%>
		<tr>
			<td><% out.print(teamsArray.get(i).getName()); %></td>
			<td><% out.print(teamsArray.get(i).getGamesPlayed()); %></td>
			<td><% out.print(teamsArray.get(i).getGamesWon()); %></td>
			<td><% out.print(teamsArray.get(i).getGamesDrawn()); %></td>
			<td><% out.print(teamsArray.get(i).getGamesLost()); %></td>
			<td><% out.print(teamsArray.get(i).getOverallPoints()); %></td>
		</tr>
<%
	}//for
}//if
%>		
	</table>
</div>
</body>
</html>