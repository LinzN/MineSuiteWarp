package de.nlinz.xeonSuite.warp.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

	private final String url;
	private final String user;
	private final String pass;

	public Connection create() throws SQLException {
		return DriverManager.getConnection(url, user, pass);
	}

	public ConnectionFactory(String url, String user, String pass) {
		this.url = url;
		this.user = user;
		this.pass = pass;
	}
}