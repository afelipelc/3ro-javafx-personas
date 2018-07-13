package personasapp.datasource;

import personasapp.model.Persona;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase que será la proveedora
 * de datos (registros de personas)
 */
public class PersonasDataSource {

    private  static  DBConnection dbConnection = new DBConnection();

    //crear la lista vacía de objetos Persona
    private static List<Persona> personasList = new ArrayList<Persona>();

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * método que devuelve una lista de personas
     * @return
     */
    public static List<Persona> ListaPersonas(){
        //devolver la lista de personas desde la BD
        //crear el query para obtener todos los
        //registros de la tabla personas
        String query = "select * from Personas";
        return procesarListaPersonasResult(query);
    }

    private static List<Persona> procesarListaPersonasResult(String query){
        //1. abrir conexión a la BD
        //2. ejecutar el query
        //3. procesar el resultSet
        //   --> crear un objeto persona
        //4. devolver la lista de objetos persona

        Statement statement = null;
        ResultSet resultSet = null;
        //crear una lista vacía
        List<Persona> personas  = new ArrayList<Persona>();

        try {
            //abrir conexión
            statement = dbConnection.OpenConnection().createStatement();

            //ejecutar query (consulta)
            resultSet = statement.executeQuery(query);

            //procesar los resultados
            while (resultSet.next()){
                //crear nuevo objeto persona
                Persona persona = new Persona();
                //leer los datos del registro
                //y ponerlos en el objeto Persona
                persona.setId(resultSet.getInt("id"));
                persona.setNombre(resultSet.getString("nombre"));
                persona.setApellidos(resultSet.getString("apellidos"));
                persona.setSexo(resultSet.getString("sexo"));
                persona.setFechaNacimiento(resultSet.getDate("fechaNacimiento").toLocalDate());

                //agregar el objeto persona a la lista
                personas.add(persona);
            } //end while

            // ya procesado el resultado
            //devolver la lista de personas
            return personas;

        } catch (SQLException ex) {
            System.out.println(ex);
            return null; //devolver nada
        }finally {
            //after all work, try free resulset memory
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException sqlEx) {

                } // ignore
            }

            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException sqlEx) {
                } // ignore
            }
            //close DB connection
            dbConnection.CloseConnection();
        }
    }

    /**
     * Guardar el objeto persona en la BD
     * @param persona
     */
    public static Persona GuardarPersona(Persona persona) {
        //si persona no tiene ID
        //insertarlo
        //si si tiene ID, actualizarlo
        //devolver objeto
        if(persona.getId() == 0){
            return insertarPersona(persona);
        }else
        {
            actualizarPersona(persona);
            return  persona;
        }
    }

    public static Persona insertarPersona(Persona persona){
        //store persona in DB
        Statement statement = null;
        ResultSet rs = null;
        try {
            //prepare connection for insertion
            statement = dbConnection.OpenConnection().createStatement(
                    java.sql.ResultSet.TYPE_FORWARD_ONLY, java.sql.ResultSet.CONCUR_UPDATABLE
            );

            //prepare query
            String SQL = "INSERT INTO " +
                    "Personas(nombre, apellidos, sexo, fechaNacimiento)  " +
                    "values('" + persona.getNombre() + "', '"
                    +persona.getApellidos() + "','"
                    + persona.getSexo() + "','"
                    + persona.getFechaNacimiento().format(formatter) + "')";

            //System.out.println(SQL);

            statement.executeUpdate(SQL, Statement.RETURN_GENERATED_KEYS);

            //read Id assigned
            int autoIncKeyFromApi = -1;
            rs = statement.getGeneratedKeys();
            if (rs.next()) {
                autoIncKeyFromApi = rs.getInt(1);
            } else {
                autoIncKeyFromApi = 0;
            }
            rs.close();
            rs = null;

            //store id in current cliente object
            persona.setId(autoIncKeyFromApi);

        } catch (SQLException ex) {
            System.out.print("Error inserting Cliente: " + ex.getMessage());
            return null;
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException sqlEx) {
                } // ignore
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException sqlEx) {
                } // ignore
            }

            dbConnection.CloseConnection();
        }

        //return saved cliente
        return persona;
    }

    private static boolean actualizarPersona(Persona persona) {
        Statement statement = null;

        try {
            //prepare connection to send update request
            statement = dbConnection.OpenConnection().createStatement(
                    java.sql.ResultSet.TYPE_FORWARD_ONLY, java.sql.ResultSet.CONCUR_UPDATABLE
            );

            String SQL = "UPDATE Personas set "
                    + " nombre='" + persona.getNombre() + "', "
                    + " apellidos ='" + persona.getApellidos() + "', "
                    + " sexo = '" + persona.getSexo() + "', "
                    + " fechaNacimiento = '" + persona.getFechaNacimiento().format(formatter) + "' "
                    + " where Id = " + persona.getId() + " limit 1";
            //System.out.println(SQL);
            statement.executeUpdate(SQL);
            return true;

        } catch (SQLException ex) {
            System.out.print("Error: " + ex.getMessage());
            return false;
        } finally {

            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException sqlEx) {
                } // ignore
            }

            dbConnection.CloseConnection();
        }
    }

}
