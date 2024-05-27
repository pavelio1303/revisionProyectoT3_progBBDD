package com.stem.ageOfSTEMpires.model;

import java.sql.*;
import java.util.ArrayList;

public class ReinoModel {
    private MyConnection myConnection = new MyConnection();
    private MonarcaModel miMonarcaModel = new MonarcaModel();

    public int obtenerIdReino() throws SQLException {
        Statement sentencia = myConnection.connect().createStatement();
        String consulta = "SELECT MAX(ID_REINO) AS ID_REINO FROM REINO";
        ResultSet resultado = sentencia.executeQuery(consulta);
        int idReino = 0;

        if(resultado.next()) {
            idReino = resultado.getInt("ID_REINO");
        }

        sentencia.close();
        myConnection.disconnect();
        
        return idReino;
    }

    private ArrayList<Reino> resultSetToArrayList(ResultSet resultSet) throws SQLException {
        ArrayList<Reino> listaReinos = new ArrayList<Reino>();

        while (resultSet.next()) {
            int id = resultSet.getInt("ID_REINO");
            String nombre = resultSet.getString("NOMBRE");
            String descripcion = resultSet.getString("DESCRIPCION");
            int anioFundacion = resultSet.getInt("ANO_FUNDACION");
            String capital = resultSet.getString("CAPITAL");
            int idMonarca = resultSet.getInt("ID_MONARCA");
            double superficie = resultSet.getDouble("SUPERFICIE");
            int poblacion = resultSet.getInt("POBLACION");
            String bandera = resultSet.getString("BANDERA_URL");
            ArrayList<Monarca> monarcasAnteriores = obtenerMonarcasEnReino(id);
            int cantPiedra = resultSet.getInt("PIEDRA");
            int cantMadera = resultSet.getInt("MADERA");
            int oro = resultSet.getInt("ORO");

            // Con el ID pasado obtenemos el monarca ya que aunque en la tabla se hace uso del ID reino contiene un monarca.
            Monarca monarca=miMonarcaModel.getMonarcaById(idMonarca);

            Reino reino = new Reino(id,nombre,descripcion,anioFundacion,capital,monarca,superficie,poblacion,bandera,monarcasAnteriores,cantPiedra,cantMadera,oro);
            
            listaReinos.add(reino);
        }

        return listaReinos;
    }
    
    public ArrayList<Reino> obtenerListaReinos() throws SQLException {
        Statement sentencia = myConnection.connect().createStatement();
        String consulta = "SELECT * FROM REINO";
        ResultSet resultado = sentencia.executeQuery(consulta);

        ArrayList<Reino> listaReinos = resultSetToArrayList(resultado);

        sentencia.close();
        myConnection.disconnect();

        return listaReinos;
    }

    public int insertReino(Reino nuevoReino) throws SQLException {
        int resultado = 0;
        
        Connection conexion = myConnection.connect();
        String consulta = "INSERT INTO REINO "
                            + "(NOMBRE, DESCRIPCION, ANO_FUNDACION, CAPITAL, ID_MONARCA, "
                            + "SUPERFICIE, POBLACION, BANDERA_URL, PIEDRA, MADERA, ORO) "
                            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement sentencia = conexion.prepareStatement(consulta);

        sentencia.setString(1, nuevoReino.getNombre());
        sentencia.setString(2, nuevoReino.getDescripcion());
        sentencia.setInt(3, nuevoReino.getAnioFundacion());
        sentencia.setString(4, nuevoReino.getCapital());
        sentencia.setInt(5, nuevoReino.getMonarca().getId());
        sentencia.setDouble(6, nuevoReino.getSuperficie());
        sentencia.setInt(7, nuevoReino.getPoblacion());
        sentencia.setString(8, nuevoReino.getBandera());
        sentencia.setInt(9, nuevoReino.getCantPiedra());
        sentencia.setInt(10, nuevoReino.getCantMadera());
        sentencia.setInt(11, nuevoReino.getOro());

        int filasAfectadas = sentencia.executeUpdate();

        sentencia.close();
        myConnection.disconnect();

        if (filasAfectadas > 0) resultado = obtenerIdReino();

        return resultado;
    }

    public boolean deleteReino(Reino reino) throws SQLException {

        String query = "DELETE FROM REINO WHERE ID_REINO = ?";
        PreparedStatement statement = myConnection.connect().prepareStatement(query);

        statement.setInt(1, reino.getId());

        int affectedrRows = statement.executeUpdate();

        statement.close();
        myConnection.disconnect();

        return affectedrRows > 0;
    }

    public Reino getReinoById(int idBuscado) throws SQLException {
        Reino reino = new Reino();

        String query = "SELECT * FROM REINO WHERE ID_REINO = ?";
        PreparedStatement statement = myConnection.connect().prepareStatement(query);

        statement.setInt(1, idBuscado);
        ResultSet resultSet = statement.executeQuery(); 

        if (resultSet.next()) {

            int id = resultSet.getInt("ID_REINO");
            String nombre = resultSet.getString("NOMBRE");
            String descripcion = resultSet.getString("DESCRIPCION");
            int anioFundacion = resultSet.getInt("ANO_FUNDACION");
            String capital = resultSet.getString("CAPITAL");
            int idMonarca = resultSet.getInt("ID_MONARCA");
            double superficie = resultSet.getDouble("SUPERFICIE");
            int poblacion = resultSet.getInt("POBLACION");
            String bandera = resultSet.getString("BANDERA_URL");
            ArrayList<Monarca> monarcasAnteriores = obtenerMonarcasEnReino(idBuscado);
            int cantPiedra = resultSet.getInt("PIEDRA");
            int cantMadera = resultSet.getInt("MADERA");
            int oro = resultSet.getInt("ORO");

            Monarca monarca=miMonarcaModel.getMonarcaById(idMonarca);

            reino = new Reino(id,nombre,descripcion,anioFundacion,capital,monarca,superficie,poblacion,bandera,monarcasAnteriores,cantPiedra,cantMadera,oro);
        }

        statement.close();
        myConnection.disconnect();

        return reino;
    }

    public boolean actualizarReino(Reino actuReino) throws SQLException {

        String query = "UPDATE REINO SET CAPITAL = ?, DESCRIPCION = ?, ID_MONARCA = ?, BANDERA_URL = ? WHERE ID_REINO = ? ";
                        
        PreparedStatement statement = myConnection.connect().prepareStatement(query);

        statement.setString(1, actuReino.getCapital());
        statement.setString(2, actuReino.getDescripcion());
        statement.setInt(3, actuReino.getMonarca().getId());
        statement.setString(4, actuReino.getBandera());
        statement.setInt(5, actuReino.getId()); 

        int affectedRows = statement.executeUpdate();

        statement.close();
        myConnection.disconnect();

        return affectedRows > 0;
    }

    public boolean actualizarMonarcaReino(Reino actuReino, Monarca actuMonarca) throws SQLException {

        String query = "UPDATE REINO SET CAPITAL = ?, DESCRIPCION = ?, ID_MONARCA = ?, BANDERA_URL = ? WHERE ID_REINO = ? ";
                        
        PreparedStatement statement = myConnection.connect().prepareStatement(query);

        statement.setString(1, actuReino.getCapital());
        statement.setString(2, actuReino.getDescripcion());
        statement.setInt(3, actuMonarca.getId());
        statement.setString(4, actuReino.getBandera());
        statement.setInt(5, actuReino.getId()); 

        int affectedRows = statement.executeUpdate();

        statement.close();
        myConnection.disconnect();

        return affectedRows > 0;
    }

    public ArrayList<Monarca> obtenerMonarcasEnReino(int idReino) throws SQLException {
        ArrayList<Monarca> listaMonarcas = new ArrayList<Monarca>();

        String consulta = "SELECT MRR.ID_MONARCA FROM MONARCA_REINA_REINO MRR JOIN REINO ON MRR.ID_REINO=REINO.ID_REINO WHERE MRR.ID_REINO = ?";
        PreparedStatement sentencia = myConnection.connect().prepareStatement(consulta);
        sentencia.setInt(1, idReino);
        ResultSet resultado = sentencia.executeQuery();

        while (resultado.next()){
            int idMonarca=resultado.getInt("ID_MONARCA");
            Monarca monarca=miMonarcaModel.getMonarcaById(idMonarca);
            listaMonarcas.add(monarca);
        }

        return listaMonarcas;
    }

    public boolean actualizarReinoTurno(Reino Reino) throws SQLException {
        String consulta ="UPDATE REINO SET ORO = ?, PIEDRA = ?, MADERA = ?, POBLACION = ?, ID_MONARCA = ? WHERE ID_REINO = ?";
        PreparedStatement sentencia = myConnection.connect().prepareStatement(consulta);

        sentencia.setInt(1, Reino.getOro());
        sentencia.setInt(2, Reino.getCantPiedra());
        sentencia.setInt(3, Reino.getCantMadera());
        sentencia.setInt(4, Reino.getPoblacion());
        sentencia.setInt(5, Reino.getMonarca().getId());
        sentencia.setInt(6, Reino.getId());

        int filasAfectadas = sentencia.executeUpdate();

        sentencia.close();
        myConnection.disconnect();

        return filasAfectadas > 0;
    }
}
