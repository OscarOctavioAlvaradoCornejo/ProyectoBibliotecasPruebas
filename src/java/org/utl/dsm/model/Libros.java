
package org.utl.dsm.model;


public class Libros {
    private int idLibro;
    private String nombreLibro;
    private String autor;
    private String genero;
    private String estatus;
    private String archivoBase64;

    public Libros() {
    }

    public Libros(int idLibro, String nombreLibro, String autor, String genero, String estatus, String archivoBase64) {
        this.idLibro = idLibro;
        this.nombreLibro = nombreLibro;
        this.autor = autor;
        this.genero = genero;
        this.estatus = estatus;
        this.archivoBase64 = archivoBase64;
    }

    public int getIdLibro() {
        return idLibro;
    }

    public void setIdLibro(int idLibro) {
        this.idLibro = idLibro;
    }

    public String getNombreLibro() {
        return nombreLibro;
    }

    public void setNombreLibro(String nombreLibro) {
        this.nombreLibro = nombreLibro;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public String getArchivoBase64() {
        return archivoBase64;
    }

    public void setArchivoBase64(String archivoBase64) {
        this.archivoBase64 = archivoBase64;
    }    
}
