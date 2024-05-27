package com.stem.ageOfSTEMpires.model;

import java.util.ArrayList;

public class Reino {
    private int id;
    private String nombre;
    private String descripcion;
    private int anioFundacion;
    private String capital;
    private Monarca monarca;    // Luego en el modelo te quedas solo con el ID
    private double superficie;
    private int poblacion;
    private String bandera;

    private ArrayList<Monarca> antiguosMonarcas;
    private int cantPiedra;
    private int cantMadera;
    private int oro;

    Reino(){

    }

    Reino(int id,String nombre,String descripcion,int anioFundacion,String capital,Monarca monarca,double superficie,int poblacion,String bandera,ArrayList<Monarca> antiguosMonarcas,int cantPiedra,int cantMadera,int oro){
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.anioFundacion = anioFundacion;
        this.capital = capital;
        this.monarca = monarca;
        this.superficie = superficie;
        this.poblacion = poblacion;
        this.bandera = bandera;
        this.antiguosMonarcas = antiguosMonarcas;
        this.cantPiedra = cantPiedra;
        this.cantMadera = cantMadera;
        this.oro = oro;
    }

    public int getId() {
        return this.id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public int getAnioFundacion() {
        return this.anioFundacion;
    }

    public String getCapital() {
        return this.capital;
    }

    public Monarca getMonarca() {
        return this.monarca;
    }

    public double getSuperficie() {
        return this.superficie;
    }

    public int getPoblacion() {
        return this.poblacion;
    }

    public String getBandera() {
        return this.bandera;
    }

    public ArrayList<Monarca> getAntiguosMonarcas() {
        return this.antiguosMonarcas;
    }

    public int getCantPiedra() {
        return this.cantPiedra;
    }

    public int getCantMadera() {
        return this.cantMadera;
    }

    public int getOro() {
        return this.oro;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setAnioFundacion(int anioFundacion) {
        this.anioFundacion = anioFundacion;
    }

    public void setCapital(String capital) {
        this.capital = capital;
    }

    public void setMonarca(Monarca monarca) {
        this.monarca = monarca;
    }

    public void setSuperficie(double superficie) {
        this.superficie = superficie;
    }

    public void setPoblacion(int poblacion) {
        this.poblacion = poblacion;
    }

    public void setBandera(String bandera) {
        this.bandera = bandera;
    }

    public void setCantPiedra(int cantPiedra) {
        this.cantPiedra = cantPiedra;
    }

    public void setCantMadera(int cantMadera) {
        this.cantMadera = cantMadera;
    }

    public void setOro(int oro) {
        this.oro = oro;
    }

    public String toString() {
        return "Reino [nombre=" + nombre + " "
                + ", descripcion=" + descripcion + ", año fundación=" + anioFundacion 
                + ", capital=" + capital + ", monarca=" + monarca 
                + ", superficie=" + superficie + ", población=" + poblacion 
                + ", bandera= " + bandera + ", cantidad de piedra=" + cantPiedra
                + ", cantidad de madera=" + cantMadera + ", oro=" + oro;
    }
}
