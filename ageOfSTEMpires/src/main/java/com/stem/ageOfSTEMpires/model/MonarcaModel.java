package com.stem.ageOfSTEMpires.model;

import java.sql.*;
import java.util.ArrayList;

public class MonarcaModel {
    private MyConnection myConnection = new MyConnection();

    public int obtenerIdMonarca() throws SQLException {
        Statement sentencia = myConnection.connect().createStatement();
        String consulta = "SELECT MAX(ID_MONARCA) AS ID_MONARCA FROM MONARCA";
        ResultSet resultado = sentencia.executeQuery(consulta);
        int idMonarca = 0;

        if(resultado.next()) {
            idMonarca = resultado.getInt("ID_MONARCA");
        }

        sentencia.close();
        myConnection.disconnect();
        
        return idMonarca;
    }

    /**
     * Metodo para obtener los nombres compatibles con una dinastia
     * @param nombreDinastia
     * @return
     * @throws SQLException
     */
    public ArrayList<String> obtenerNombresEnDinastia(String nombreDinastia) throws SQLException {
        String query = "SELECT * FROM DINASTIA_TIENE_NOMBRE WHERE NOMBRE_DINASTIA = ?";
        PreparedStatement statement = myConnection.connect().prepareStatement(query);

        statement.setString(1, nombreDinastia);
        ResultSet resultSet = statement.executeQuery(); 

        ArrayList<String> listaNombresValidos = new ArrayList<>();

        while (resultSet.next()) {
            String nombrePropio = resultSet.getString("NOMBRE_PROPIO");
            listaNombresValidos.add(nombrePropio);
        }

        statement.close();
        myConnection.disconnect();

        return listaNombresValidos;
    }

    public ArrayList<Monarca> obtenerListaMonarcas() throws SQLException {
        Statement sentencia = myConnection.connect().createStatement();
        String consulta = "SELECT * FROM MONARCA";
        ResultSet resultado = sentencia.executeQuery(consulta);

        ArrayList<Monarca> listaMonarcas = resultSetToArrayList(resultado);

        sentencia.close();
        myConnection.disconnect();

        return listaMonarcas;
    }

    public int cantidadNombreMonarcaEnDinastia(Monarca nuevoMonarca) throws SQLException{
        int count = 0;

        String query = "SELECT COUNT(*) FROM MONARCA WHERE NOMBRE = ? AND NOMBRE_DINASTIA = ?";

        PreparedStatement statement = myConnection.connect().prepareStatement(query);

        statement.setString(2, nuevoMonarca.getDinastia());
        statement.setString(1, nuevoMonarca.getNombre());

        ResultSet resultado = statement.executeQuery();

        if (resultado.next()){
            count = resultado.getInt("COUNT(*)");
        }

        statement.close();
        myConnection.disconnect();

        return count;
    }

    private ArrayList<Monarca> resultSetToArrayList(ResultSet resultSet) throws SQLException {
        ArrayList<Monarca> listaMonarcas = new ArrayList<Monarca>();

        while (resultSet.next()) {
            int id = resultSet.getInt("ID_MONARCA");
            String dinastia = resultSet.getString("NOMBRE_DINASTIA");
            String nombre = resultSet.getString("NOMBRE");
            String ordinalNombre = resultSet.getString("ORDINAL_NOMBRE");
            String escudoMonarca = resultSet.getString("ESCUDO");
            String descripcion = resultSet.getString("DESCRIPCION");
            int nivelEstrategia = resultSet.getInt("NIVEL_ESTRATEGIA");
            int nivelDiplomatico = resultSet.getInt("NIVEL_DIPLOMACIA");
            int nivelExperiencia = resultSet.getInt("NIVEL_EXPERIENCIA");
            boolean estado = resultSet.getBoolean("VIVO");

            Monarca monarca = new Monarca(id,dinastia,nombre,ordinalNombre,escudoMonarca,descripcion,nivelEstrategia,nivelDiplomatico,nivelExperiencia,estado);
            
            listaMonarcas.add(monarca);
        }

        return listaMonarcas;
    }

    public Monarca getMonarcaById(int idBuscado) throws SQLException {
        Monarca monarca = new Monarca();

        String query = "SELECT * FROM MONARCA WHERE ID_MONARCA = ?";
        PreparedStatement statement = myConnection.connect().prepareStatement(query);

        statement.setInt(1, idBuscado);
        ResultSet resultSet = statement.executeQuery(); 

        if (resultSet.next()) {
            int id = resultSet.getInt("ID_MONARCA");
            String dinastia = resultSet.getString("NOMBRE_DINASTIA");
            String nombre = resultSet.getString("NOMBRE");
            String ordinalNombre = resultSet.getString("ORDINAL_NOMBRE");
            String escudoMonarca = resultSet.getString("ESCUDO");
            String descripcion = resultSet.getString("DESCRIPCION");
            int nivelEstrategia = resultSet.getInt("NIVEL_ESTRATEGIA");
            int nivelDiplomatico = resultSet.getInt("NIVEL_DIPLOMACIA");
            int nivelExperiencia = resultSet.getInt("NIVEL_EXPERIENCIA");
            boolean estado = resultSet.getBoolean("VIVO");

            monarca = new Monarca(id,dinastia,nombre,ordinalNombre,escudoMonarca,descripcion,nivelEstrategia,nivelDiplomatico,nivelExperiencia,estado);
        }

        statement.close();
        myConnection.disconnect();

        return monarca;
    }

    public int insertMonarca(Monarca nuevoMonarca) throws SQLException{

        int resultado = 0;

        String query = "INSERT INTO MONARCA "
                        + "(NOMBRE_DINASTIA, NOMBRE, ORDINAL_NOMBRE, ESCUDO, DESCRIPCION, NIVEL_ESTRATEGIA, "
                        + " NIVEL_DIPLOMACIA, NIVEL_EXPERIENCIA, VIVO) VALUES (?,?,?,?,?,?,?,?,true)";

        PreparedStatement statement = myConnection.connect().prepareStatement(query);

        statement.setString(1, nuevoMonarca.getDinastia());
        statement.setString(2, nuevoMonarca.getNombre());
        statement.setString(3, nuevoMonarca.getOrdinalNombre()); // METODO CONVERSOR A NUMEROS ROMANOS EN EL CONTROLLER, Y DESPUÉS AQUÍ USAR ESE MÉTODO
        statement.setString(4, nuevoMonarca.getEscudoMonarca());
        statement.setString(5, nuevoMonarca.getDescripcion());
        statement.setInt(6, nuevoMonarca.getNivelEstrategia());
        statement.setInt(7, nuevoMonarca.getNivelDiplomatico());
        statement.setInt(8, nuevoMonarca.getNivelExperiencia());

        int affectedrRows = statement.executeUpdate();

        statement.close();
        myConnection.disconnect();

        if (affectedrRows > 0) resultado = obtenerIdMonarca();

        return resultado;
        //return affectedrRows > 0;
    }

    public boolean asesinarMonarca(Monarca asesMonarca) throws SQLException {

        String query = "UPDATE MONARCA SET VIVO = 0 WHERE ID_MONARCA = ? ";
                        
        PreparedStatement statement = myConnection.connect().prepareStatement(query);

        statement.setInt(1, asesMonarca.getId()); 

        int affectedRows = statement.executeUpdate();

        statement.close();
        myConnection.disconnect();

        return affectedRows > 0;
    }

    public boolean actualizarMonarca(Monarca actuMonarca) throws SQLException {

        boolean resultado = false; 

        String query = "UPDATE MONARCA SET NIVEL_ESTRATEGIA = ?, NIVEL_DIPLOMACIA = ?, NIVEL_EXPERIENCIA = ? WHERE ID_MONARCA = ? ";
                        
        PreparedStatement statement = myConnection.connect().prepareStatement(query);

        statement.setInt(1, actuMonarca.getNivelEstrategia());
        statement.setInt(2, actuMonarca.getNivelDiplomatico());
        statement.setInt(3, actuMonarca.getNivelExperiencia());
        statement.setInt(4, actuMonarca.getId()); 

        int affectedRows = statement.executeUpdate();

        statement.close();
        myConnection.disconnect();

        if (affectedRows > 0) resultado = true;

        return resultado;
    }

    public boolean deleteMonarca(Monarca monarca) throws SQLException {

        String query = "DELETE FROM MONARCA WHERE ID_MONARCA = ?";
        PreparedStatement statement = myConnection.connect().prepareStatement(query);

        statement.setInt(1, monarca.getId());

        int affectedrRows = statement.executeUpdate();

        statement.close();
        myConnection.disconnect();

        return affectedrRows > 0;
    }
}
