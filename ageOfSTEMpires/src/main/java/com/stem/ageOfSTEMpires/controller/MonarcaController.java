package com.stem.ageOfSTEMpires.controller;

import java.sql.SQLException;
import java.util.ArrayList;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stem.ageOfSTEMpires.model.MonarcaModel;
import com.stem.ageOfSTEMpires.model.Reino;
import com.stem.ageOfSTEMpires.model.Monarca;

@RestController
@RequestMapping("/monarca")
public class MonarcaController {

    private MonarcaModel miMonarcaModel = new MonarcaModel();

    /**
     * Metodo al que se le pasa un INT y nos lo convierte en números romanos (String)
     * @param numeroDado
     * @return
     */
    public static String conversorIntARomano(int numeroDado){
        int m;
        String resultado = "";

        String unidad[] = {"", "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX"}; //Declaro un array el cual pongo los numero romano los cuales voy a usar
        String decena[] = {"", "X", "XX", "XXX", "XL", "L", "LX", "LXX", "LXXX", "XC"};
        String centena[] = {"", "C", "CC", "CCC", "CD", "D", "DC", "DCC", "DCCC", "CM"};
        String miles = "";

        int n = numeroDado;
        int resto = n;
        m = resto / 1000; //3950/1000=3
        resto = resto % 1000;  //950

        int c = resto / 100; //9
        resto = resto % 100; // 50

        int d = resto / 10; //5
        resto = resto % 10; // 0

        int u = resto;
        for (int i = 1; i <= m; i++) {
            miles += "M";
        }
        if (n >= 1000) {
            resultado = miles + centena[c] + decena[d] + unidad[u];
        } else if (n >= 100) {
            resultado = centena[c] + decena[d] + unidad[u];
        } else {
            if (n >= 10) {
                resultado = decena[d] + unidad[u];
            } else {
                resultado = unidad[n];
            }
        }

        return resultado;
    }

    /**
     * Método que recorre la lista de nombres válidos para la dinastia obtenida, y en caso de que el nombre se encuentre en esta lo convierte a válido
     * @param nombreDado
     * @param nombreDinastia
     * @return
     * @throws SQLException
     */
    public boolean comprobarNombreEnDinastia(String nombreDado, String nombreDinastia) throws SQLException {
        ArrayList<String> listaNombresValidos = miMonarcaModel.obtenerNombresEnDinastia(nombreDinastia);
        boolean nombreValido = false;
    
        for (String nombreL : listaNombresValidos) {
            if (nombreDado.toUpperCase().equals(nombreL.toUpperCase())) nombreValido = true;
        }
    
        return nombreValido;
    }

    /**
     * Metodo que cuenta cuantos monarcas con el msmo nombre dentro de la dinastía hay.
     * @param nombreDado
     * @param dinastia
     * @return
     * @throws SQLException
     */
    public int contarNombreEnMonarcas(String nombreDado, String dinastia) throws SQLException {
        ArrayList<Monarca> listaMonarcas = miMonarcaModel.obtenerListaMonarcas();
        int monarcaRepetido = 0;
    
        for (Monarca monarcaL : listaMonarcas) {
            if (nombreDado.toUpperCase().equals(monarcaL.getNombre().toUpperCase()) && dinastia.toUpperCase().equals(monarcaL.getDinastia().toUpperCase())) monarcaRepetido += 1;
        }
    
        return monarcaRepetido;
    }

    @GetMapping
    public ArrayList<Monarca> obtenerListaMonarcas() throws SQLException{
        return miMonarcaModel.obtenerListaMonarcas();
    }

    @GetMapping("/{idBuscado}")
    public Monarca getMonarcaById(@PathVariable int idBuscado) throws SQLException{
        return miMonarcaModel.getMonarcaById(idBuscado);
    }

    @PostMapping
    public int insertMonarca(@RequestBody Monarca newMonarca) throws SQLException{
        boolean comprobanteStats = newMonarca.comprobarMonarcaStats();
        boolean comprobanteNombre = comprobarNombreEnDinastia(newMonarca.getNombre(), newMonarca.getDinastia());
        int monarcaInsertado = 0;
        int cantidadNombre = 0;
        String ordinalMonarca = "";

        // Haciendo uso del método contarNombreEnMonarcas() obtenemos cuántos monarcas tenemos dentro de una dinastía con el mimso nombre.
        cantidadNombre = contarNombreEnMonarcas(newMonarca.getNombre(), newMonarca.getDinastia());
        // Convertimos a número romano.
        ordinalMonarca = conversorIntARomano(cantidadNombre+1);  // Se suma puesto que en caso de que sea el primero en lugar de 0 seria I y asi consecutivamente.
        
        // Establecemos el ordinal del monarca pasado 
        newMonarca.setOrdinalNombre(ordinalMonarca);

        newMonarca.nivelAleatorioInicial();

        // En caso de tener stats y nombre válidos lo insertamos
        if (comprobanteStats && comprobanteNombre) {  
            monarcaInsertado = miMonarcaModel.insertMonarca(newMonarca);
        }

        return monarcaInsertado;
    }

    @DeleteMapping
    public boolean asesinarMonarca(@RequestBody Monarca asesMonarca) throws SQLException{
        return miMonarcaModel.asesinarMonarca(asesMonarca); 
    }

    @PutMapping
    public boolean actualizarMonarca(@RequestBody Monarca actuMonarca) throws SQLException{
        return miMonarcaModel.actualizarMonarca(actuMonarca); 
    }

    @DeleteMapping("/borrar")
    public boolean deleteMonarca(@RequestBody Monarca delMonarca) throws SQLException{
        return miMonarcaModel.deleteMonarca(delMonarca);
    }

    // Solo usé esto para comprobar que funcionase la contabilización de monarcas repetidos.
    // @GetMapping("/ordinal")
    // public int cantidadNombreMonarcaEnDinastia(@RequestBody Monarca monarca) throws SQLException{
    //     return miMonarcaModel.cantidadNombreMonarcaEnDinastia(monarca);
    // }
}
