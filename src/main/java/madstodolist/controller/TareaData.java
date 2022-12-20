package madstodolist.controller;

import java.time.LocalDate;

public class TareaData {
    private String titulo;

    private LocalDate fechaFinal;

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public LocalDate getFechaFinal() { return fechaFinal; }

    public void setFechaFinal(String fechaFinal) { this.fechaFinal = LocalDate.parse(fechaFinal); }
}
