package com.stem.ageOfSTEMpires.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase para manejar la conexión a la base de datos MySQL.
 */
public class MyConnection {
    private String host;
    private int port;
    private String database;
    private String user;
    private String pass;

    private Connection MyConnection;

    /**
     * Constructor predeterminado que inicializa los valores predeterminados para la conexión.
     */
    public MyConnection(){
        this.host = "localhost";
        this.port = 3306;
        this.database = "age_of_stempires";
        this.user = "root";
        this.pass = "1234";
    }

    /**
     * Constructor que permite especificar los detalles de la conexión.
     * @param host El host de la base de datos.
     * @param port El puerto de la base de datos.
     * @param database El nombre de la base de datos.
     * @param user El nombre de usuario de la base de datos.
     * @param pass La contraseña de la base de datos.
     */
    public MyConnection(String host, int port, String database, String user, String pass) {
        this.host = host;
        this.port = port;
        this.database = database;
        this.user = user;
        this.pass = pass;
    }

    /**
     * Método para establecer la conexión a la base de datos.
     * @return La conexión establecida.
     * @throws SQLException Si ocurre un error al establecer la conexión.
     */
    public Connection connect() throws SQLException{
        String url = "jdbc:mysql://"+this.host+":"+this.port+"/"+this.database;
        this.MyConnection = DriverManager.getConnection(url, this.user, this.pass);
        return this.MyConnection;
    }

    /**
     * Método para cerrar la conexión a la base de datos.
     * @throws SQLException Si ocurre un error al cerrar la conexión.
     */
    public void disconnect() throws SQLException{
        this.MyConnection.close();
    }

}