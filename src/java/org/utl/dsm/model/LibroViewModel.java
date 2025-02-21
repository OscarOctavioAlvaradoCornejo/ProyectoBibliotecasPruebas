package org.utl.dsm.model;

/**
 *
 * @author oscar
 */
public class LibroViewModel {

    private int identificador;
    private String nombreDelLibro;
    private String nombreDelAutor;
    private String categoria;
    private String estado;
    private String archivoPdf;

    public LibroViewModel(int identificador, String nombreDelLibro, String nombreDelAutor, String categoria, String estado, String archivoPdf) {
        this.identificador = identificador;
        this.nombreDelLibro = nombreDelLibro;
        this.nombreDelAutor = nombreDelAutor;
        this.categoria = categoria;
        this.estado = estado;
        this.archivoPdf = archivoPdf;
    }

    public int getIdentificador() {
        return identificador;
    }

    public String getNombreDelLibro() {
        return nombreDelLibro;
    }

    public String getNombreDelAutor() {
        return nombreDelAutor;
    }

    public String getCategoria() {
        return categoria;
    }

    public String getEstado() {
        return estado;
    }

    public String getArchivoPdf() {
        return archivoPdf;
    }

    public void setIdentificador(int identificador) {
        this.identificador = identificador;
    }

    public void setNombreDelLibro(String nombreDelLibro) {
        this.nombreDelLibro = nombreDelLibro;
    }

    public void setNombreDelAutor(String nombreDelAutor) {
        this.nombreDelAutor = nombreDelAutor;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void setArchivoPdf(String archivoPdf) {
        this.archivoPdf = archivoPdf;
    }

    

}
