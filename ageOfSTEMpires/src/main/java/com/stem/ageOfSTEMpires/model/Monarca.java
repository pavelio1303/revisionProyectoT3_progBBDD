package com.stem.ageOfSTEMpires.model;

public class Monarca {
    private int id;
    private String dinastia;
    private String nombre;
    private String ordinalNombre;
    private String escudoMonarca;
    private String descripcion;
    private int nivelEstrategia;
    private int nivelDiplomatico;
    private int nivelExperiencia;
    private boolean estado;

    public Monarca(){

    }

    Monarca(int id, String dinastia, String nombre, String ordinalNombre, String escudoMonarca, String descripcion,int nivelEstrategia,int nivelDiplomatico,int nivelExperiencia,boolean estado){
        this.id = id;
        this.dinastia = dinastia;
        this.nombre = nombre;
        this.ordinalNombre = ordinalNombre;
        this.escudoMonarca = escudoMonarca;
        this.descripcion = descripcion;
        this.nivelEstrategia = nivelEstrategia;
        this.nivelDiplomatico = nivelDiplomatico;
        this.nivelExperiencia = nivelExperiencia;
        this.estado = estado;
    }

    // CREAR UN METODO boolean comprobarMonarca() QUE COMPRUEBE SI TODOS LOS DATOS DEL MONARCA SON VÁLIDOS Y , EN CASO DE SERLOS, INCLUIR EL MONARCA EN LA BBDD.
    // comprobarMonarca() además debe comprobar que no haya valores a null.
    public boolean comprobarMonarcaStats(){
        boolean respuesta = true;

        if ((this.nivelEstrategia < 1 || this.nivelDiplomatico < 1) || (this.nivelEstrategia + this.nivelDiplomatico != 5)) {
            respuesta = false;
        }
        return respuesta;
    }

    public void nivelAleatorioInicial(){
        int minimo = 14;
        int maximo = 35;

        int diferencia = maximo-minimo;
        this.nivelExperiencia = (int)Math.floor((Math.random()*diferencia)+minimo);
    }

    public int getId() {
        return this.id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public String getOrdinalNombre() {
        return this.ordinalNombre;
    }

    public String getDinastia() {
        return this.dinastia;
    }

    public String getEscudoMonarca() {
        return this.escudoMonarca;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public int getNivelEstrategia() {
        return this.nivelEstrategia;
    }

    public int getNivelDiplomatico() {
        return this.nivelDiplomatico;
    }

    public int getNivelExperiencia() {
        return this.nivelExperiencia;
    }

    public boolean getEstado() {
        return this.estado;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre.toUpperCase();
    }

    public void setOrdinalNombre(String ordinalNombre) {
        this.ordinalNombre = ordinalNombre;
    }

    public void setDinastia(String dinastia) {
        this.dinastia = dinastia.toUpperCase();
    }

    public void setEscudoMonarca(String escudoMonarca) {
        this.escudoMonarca = escudoMonarca;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setNivelEstrategia(int nivelEstrategia) {
        this.nivelEstrategia = nivelEstrategia;
    }

    public void setNivelDiplomatico(int nivelDiplomatico) {
        this.nivelDiplomatico = nivelDiplomatico;
    }

    public void setNivelExperiencia(int nivelExperiencia) {
        this.nivelExperiencia = nivelExperiencia;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public String toString() {
        return "Monarca [nombre=" + nombre + " " + ordinalNombre 
                + ", dinastia=" + dinastia + ", escudo=" + escudoMonarca 
                + ", descripcion=" + descripcion + ", estrategia=" + nivelEstrategia 
                + ", diplomatico=" + nivelDiplomatico + "experiencia=" + nivelExperiencia 
                + ", estado= " + estado + "]";
    }
}


