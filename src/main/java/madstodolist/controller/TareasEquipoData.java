package madstodolist.controller;

import org.springframework.format.annotation.DateTimeFormat;
import java.util.Date;

public class TareasEquipoData {
    private String titulo;
    private String descripcion;
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date fecha;

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
}
