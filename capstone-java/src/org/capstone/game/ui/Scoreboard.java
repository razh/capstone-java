package org.capstone.game.ui;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import org.capstone.game.MeshGroup;
import org.capstone.game.MeshStage;
import org.capstone.game.State;
import org.capstone.game.TextMeshGroup;
import org.capstone.game.TextMeshGroup.Alignment;
import org.capstone.game.network.Database;

import com.badlogic.gdx.graphics.Color;

public class Scoreboard extends MeshGroup {
	private int numScores = 5;
	
	public Scoreboard() {
		ArrayList<String> scoreStrings = getScores();

		String name, score;
		float nameX = State.getWidth() * 0.2f;
		float scoreX = State.getWidth() * 0.6f;
		float y = State.getHeight() * 0.6f;
		for (int i = 0; i < numScores; i++) {
			if (scoreStrings.size() <= 2 * i + 1) {
				break;
			}

			name = scoreStrings.get(2 * i);
			score = scoreStrings.get(2 * i + 1);

			addActor(new TextMeshGroup(name, nameX, y, Color.WHITE, 20, 40, 20, 3.0f, Alignment.LEFT));
			addActor(new TextMeshGroup(score, scoreX, y, Color.WHITE, 20, 40, 20, 3.0f, Alignment.LEFT));
			
			y -= 60;
		}		
	}
	
	public ArrayList<String> getScores2() {
		ArrayList<String> strings = new ArrayList<String>();
		strings.add("tom");
		strings.add("0128");
//		strings.add("pamela");
//		strings.add("49289");
		return strings;
	}
	
	public ArrayList<String> getScores() {
		ArrayList<String> scoreStrings = new ArrayList<String>();
		String getScoreString = "SELECT * FROM PLAYER ORDER BY PLAYER_SCORE DESC";
		try {
			Connection connection = Database.getConnection();
			//String query = "UPDATE TD_SCORE SET player_score = 4 WHERE PLAYER_ID = 1";

			Statement stmt = connection.createStatement();
//			stmt.executeUpdate(getScoreString);

	        ResultSet rs = stmt.executeQuery(getScoreString);
	        while (rs.next()) {
	        	scoreStrings.add(rs.getString(2));
	        	scoreStrings.add(rs.getString(3));
	        }
		} catch (Exception e) {
			e.printStackTrace();
		}

		return scoreStrings;
	}	
}
