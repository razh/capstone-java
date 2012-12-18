package org.capstone.game.network;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
	public static Connection getConnection() throws SQLException {		
		String url = "jdbc:postgresql://ec2-54-243-248-219.compute-1.amazonaws.com:5432/davstv3g61tu1g?user=mxqmoxrybvecjh&password=p0oIwCgohzkAEoFL9YXXkcDb71&ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory&"; 
		return DriverManager.getConnection (url);   
	}
}
