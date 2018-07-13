package personasapp.datasource;

import javafx.scene.control.Alert;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    //MySQL Server credetials
    private static String host ="localhost", db="personasDB", user = "root", password = "";


    //JDBC connection path to MySQL Server
    private static String DbUrl = "jdbc:mysql://"+host+"/"+db;
    //jdbc:mysq://localhost/personasDB
    //conection object that'll be opened and closed in each query

    private Connection connectionDB;

    /**
     * Perform open MySQL Server connection for execute queries while is open
     * @return
     */
    public Connection OpenConnection(){

        try{
            connectionDB = DriverManager.getConnection(DbUrl, user, password);
            return connectionDB;
        }catch (SQLException ex){

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("DB Connection Error");
            alert.setHeaderText("Un error ha ocurrido al conectarse a la BD");
            alert.setContentText("Abra y cierre el programa, si el error persiste entonces\ncontacte a Soporte Técnico.");
            alert.showAndWait();

            return null;
        }
    }

    public void CloseConnection(){
        try{
            //if connections is not null and connection is not closed, then close current opened connection
            if(connectionDB != null && !connectionDB.isClosed()){
                connectionDB.close();
            }
        }catch (SQLException ex){

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("DB Connection Error");
            alert.setHeaderText("Un error ha ocurrido con la conexion a la BD");
            alert.setContentText("Continúe usando la aplicación, si el error persiste\nentonces contacte a Soporte Técnico.");
            alert.showAndWait();
        }
    }
}
