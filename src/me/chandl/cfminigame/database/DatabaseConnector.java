package me.chandl.cfminigame.database;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector  {
    private  static Connection connection;
    private  String host, database, username, password;
    private  int port;
    private  static DatabaseConnector connector;

    private DatabaseConnector(String host, String database, String username, String password, int port) {
        this.host = host;
        this.database = database;
        this.username = username;
        this.password = password;
        this.port = port;
    }

    public static DatabaseConnector getConnector () {
        if(connector != null){
            return connector;
        }else{
            return null;
        }
    }

    public static DatabaseConnector getConnector(String host, String database, String username, String password, int port){
        if(connector == null ){
            return new DatabaseConnector(host,database,username,password,port);
        }else{
            return connector;
        }
    }

    public void openConnection () throws SQLException, ClassNotFoundException {
        if (connection != null && !connection.isClosed()){
            return;
        }

        synchronized (this){
            if(connection != null && !connection.isClosed()){
                return;
            }
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database, this.username, this.password);
        }
    }

}
