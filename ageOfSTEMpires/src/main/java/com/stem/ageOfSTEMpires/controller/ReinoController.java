package com.stem.ageOfSTEMpires.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stem.ageOfSTEMpires.model.Monarca;
import com.stem.ageOfSTEMpires.model.MonarcaModel;
import com.stem.ageOfSTEMpires.model.Reino;
import com.stem.ageOfSTEMpires.model.ReinoModel;

@RestController
@RequestMapping("/reino")
public class ReinoController {
    private ReinoModel miReinoModel = new ReinoModel();
    private MonarcaModel miMonarcaModel = new MonarcaModel();

    private MonarcaController miMonarcaController = new MonarcaController();
    

    public Double obternerSuperficieMayor(ArrayList<Reino> listaReinos) throws SQLException{
        Double superficie=0.00;

        for (Reino reino:listaReinos){
            if (superficie<=reino.getSuperficie())superficie=reino.getSuperficie();
        }
        return superficie;
    }

    public Double obternerSuperficieMenor(ArrayList<Reino> listaReinos) throws SQLException{
        Reino reinoInicial = getReinoById(1);
        Double superficie=reinoInicial.getSuperficie();

        for (Reino reino:listaReinos){
            if (superficie>reino.getSuperficie())superficie=reino.getSuperficie();
        }
        return superficie;
    }

    public int obtenerPoblacionMayor(ArrayList<Reino> listaReinos) throws SQLException{
        int poblacion=0;

        for (Reino reino:listaReinos){
            if (poblacion<=reino.getPoblacion())poblacion=reino.getPoblacion();
        }
        return poblacion;
    }

    public int obtenerPoblacionMenor(ArrayList<Reino> listaReinos) throws SQLException{
        int poblacion=0;

        for (Reino reino:listaReinos){
            if (poblacion<=reino.getPoblacion())poblacion=reino.getPoblacion();
        }
        return poblacion;
    }

    public int calcularPiedraMadera(Reino reino) throws SQLException{
        ArrayList<Reino> listaReinos=obtenerListaReinos();

        double superficieMaxima=obternerSuperficieMayor(listaReinos);
        double superficie=reino.getSuperficie();
        
        int cantidadRecursoMaximo=(int) Math.round(superficie/superficieMaxima*500+500);

        Random rand = new Random();
        int recurso=rand.nextInt(cantidadRecursoMaximo + 1);

        return recurso;
    }

    public int calcularOro(Reino reino) throws SQLException{
        ArrayList<Reino> listaReinos=obtenerListaReinos();

        int poblacionMaxima=obtenerPoblacionMayor(listaReinos);
        int poblacion=reino.getPoblacion();

        int cantidadOroMaximo=poblacion/poblacionMaxima*500+500;

        Random rand = new Random();
        int oro=rand.nextInt(cantidadOroMaximo + 1);

        return oro;
    }

    public int calcularAumentoPoblacion(Reino reino) throws SQLException{
        ArrayList<Reino> listaReinos=obtenerListaReinos();

        int poblacionMaxima=obtenerPoblacionMayor(listaReinos);
        int poblacion=reino.getPoblacion();

        int cantidadOroMaximo=poblacion/poblacionMaxima*5000+5000;

        Random rand = new Random();
        int oro=rand.nextInt(cantidadOroMaximo + 1);

        return oro;
    }

    public boolean comprobarReino(Reino reino){
        boolean respuesta = true;

        if (!comprobarNombreReino(reino) || !comprobarAnioFundacion(reino) || !comprobarNombreCapital(reino) || !comprobarPoblacion(reino)) {
            respuesta = false;
        }
        return respuesta;
    }

    public boolean comprobarPoblacion(Reino reino){
        boolean respuesta = true;
        int poblacion = reino.getPoblacion();

        if (poblacion < 1 || (poblacion/reino.getSuperficie() > 10)) respuesta = false;

        return respuesta;
    }

    public boolean comprobarAnioFundacion(Reino reino){
        boolean respuesta = true;
        int anioReino = reino.getAnioFundacion();

        if (anioReino < 400 && anioReino > 1400) respuesta = false;

        return respuesta;
    }

    public boolean comprobarNombreReino(Reino reino){
        boolean respuesta = true;
        String nombre = reino.getNombre();

        // Comprueba que no tenga más de 20 caracteres y al menos 1.
        if (nombre.length() > 20 && nombre.length() == 0) respuesta = false;

        // A través de una expresión regular que no haya números.
        // ".*\\d.*" -> cero o más caracteres, seguidos de un dígito, seguidos de cero o más caracteres.
        if (nombre.matches(".*\\d.*")) {
            return false;
        }
        
        // Forma de comprobar que sean mayus la primera letra de cada palabra.
        String[] palabras = nombre.split(" ");
        for (String palabra : palabras) {
            if (Character.isLowerCase(palabra.charAt(0))) {
                return false;
            }
        }

        return respuesta;
    }

    public boolean comprobarNombreCapital(Reino reino){
        boolean respuesta = true;
        String nombre = reino.getCapital();

        // Comprueba que no tenga más de 20 caracteres y al menos 1.
        if (nombre.length() > 20 && nombre.length() == 0) respuesta = false;

        // A través de una expresión regular que no haya números.
        // ".*\\d.*" -> cero o más caracteres, seguidos de un dígito, seguidos de cero o más caracteres.
        if (nombre.matches(".*\\d.*")) {
            return false;
        }
        
        // Forma de comprobar que sean mayus la primera letra de cada palabra.
        String[] palabras = nombre.split(" ");
        for (String palabra : palabras) {
            if (Character.isLowerCase(palabra.charAt(0))) {
                return false;
            }
        }

        return respuesta;
    }


    @GetMapping("/{idBuscado}")
    public Reino getReinoById(@PathVariable int idBuscado) throws SQLException{
        return miReinoModel.getReinoById(idBuscado);
    }

    @GetMapping
    public ArrayList<Reino> obtenerListaReinos() throws SQLException{
        return miReinoModel.obtenerListaReinos();
    }

    @PostMapping
    public int insertarReino(@RequestBody Reino nuevoReino) throws SQLException{
        int reinoInsertado = 0;

        if (comprobarReino(nuevoReino)){
            nuevoReino.setCantPiedra(calcularPiedraMadera(nuevoReino));
            nuevoReino.setCantMadera(calcularPiedraMadera(nuevoReino));
            nuevoReino.setOro(calcularOro(nuevoReino));

            reinoInsertado = miReinoModel.insertReino(nuevoReino);
        }

        return reinoInsertado;
    }

    @DeleteMapping()
    public boolean deleteReino(@RequestBody Reino delReino) throws SQLException{
        return miReinoModel.deleteReino(delReino);
    }

    @PutMapping
    public boolean actualizarReino(@RequestBody Reino actuReino) throws SQLException{
        
        return miReinoModel.actualizarReino(actuReino); 
    }

    
    @GetMapping("/pasar")
    public void pasarTurno() throws SQLException{
        ArrayList<Reino> reinos = obtenerListaReinos();

        Random rand = new Random();
        
        for (Reino reino : reinos){
            
            // Actualizamos la edad del monarca
            Monarca monarca = reino.getMonarca();

            int aniosSumados=rand.nextInt(10 + 1);

            int edadNueva=monarca.getNivelExperiencia() + aniosSumados;
            monarca.setNivelExperiencia(edadNueva);

            miMonarcaModel.actualizarMonarca(monarca);

            // Comprobar si este muere o no
            boolean monarcaVivoMuerto = monarcaViveOMuere(monarca);

            if (!monarcaVivoMuerto){

                String dinastiaMonarca = monarca.getDinastia();
                ArrayList<String> nombresEnDinastia = miMonarcaModel.obtenerNombresEnDinastia(dinastiaMonarca);
                
                String nuevoNombre = nombresEnDinastia.get(rand.nextInt(nombresEnDinastia.size()));
                Monarca nuevoMonarca = new Monarca();

                nuevoMonarca.setDinastia(dinastiaMonarca);
                nuevoMonarca.setNombre(nuevoNombre);
                nuevoMonarca.setDescripcion("Quien heredó legítimamente el trono.");
                nuevoMonarca.setEscudoMonarca(monarca.getEscudoMonarca());
                nuevoMonarca.setNivelEstrategia(3);
                nuevoMonarca.setNivelDiplomatico(2);

                miMonarcaController.insertMonarca(nuevoMonarca);

                miReinoModel.actualizarMonarcaReino(reino, nuevoMonarca);
            }

            reino.setCantPiedra(reino.getCantPiedra() + calcularPiedraMadera(reino));
            reino.setCantMadera(reino.getCantMadera() + calcularPiedraMadera(reino));
            reino.setOro(reino.getOro() + calcularOro(reino));
            reino.setPoblacion(reino.getPoblacion() + calcularAumentoPoblacion(reino));
            
            miReinoModel.actualizarReinoTurno(reino);
        }
    }

    @GetMapping("/reino/atacar/{idAtacante}/{idDefensor}")
    public int atacar(@PathVariable Integer idAtacante, @PathVariable Integer idDefensor) throws SQLException{

    } 

    public int calcularPuntuacionReino(Reino reino) throws SQLException{
        Random rand = new Random();

        int puntuacionTotal = 0;
        int numeroRandom = rand.nextInt(30);

        puntuacionTotal = puntPoblacion(reino)+puntTerritorio(reino)+puntEdad(reino)+numeroRandom;
    
        return puntuacionTotal;
    }

    public int puntPoblacion(Reino reino) throws SQLException{
        int poblacionReino=reino.getPoblacion();
        // double superficieMinima=obternerSuperficieMenor(obtenerListaReinos());
        // double superficieMaxima=obternerSuperficieMayor(obtenerListaReinos());
        int poblacionMax = obtenerPoblacionMayor(obtenerListaReinos());
        int poblacionMin = obtenerPoblacionMenor(obtenerListaReinos());

        int puntuacion=((poblacionReino-poblacionMin)/(poblacionMax-poblacionMin))*10;
        
        return puntuacion;
    }

    public int puntTerritorio(Reino reino) throws SQLException{
        double territorioReino=reino.getSuperficie();
        double superMin=obternerSuperficieMenor(obtenerListaReinos());
        double superMax=obternerSuperficieMayor(obtenerListaReinos());

        int puntuacion=(int) Math.round(((territorioReino-superMin)/(superMax-superMin))*10);
        
        return puntuacion;
    }

    public int puntEdad(Reino reino) throws SQLException{
        int puntuacion = 0;
        int edadMonarca = reino.getMonarca().getNivelExperiencia();

        if (edadMonarca >= 80){
            puntuacion = 0;
        }else if (edadMonarca < 80){
            puntuacion = (-(1/160)*(reino.getMonarca().getNivelExperiencia()-40)^2)+10;
        }

        return puntuacion;
    } 

    public boolean monarcaViveOMuere(Monarca monarca) throws SQLException{
        Random rand = new Random();
        boolean estado = monarca.getEstado();

        int primerNum=rand.nextInt(100 + 1);
        int segundoNum=rand.nextInt(monarca.getNivelExperiencia() + 1);

        monarca.setNivelExperiencia(segundoNum);

        if (segundoNum>primerNum){
            estado = miMonarcaController.asesinarMonarca(monarca);
        } 

        return estado;
    }
}
