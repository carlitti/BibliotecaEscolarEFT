package modelo;

import java.time.LocalDate;

public class Prestamo {

    private int id;
    private int idEstudiante;
    private int idLibro;

    private LocalDate fechaPrestamo;
    private LocalDate fechaVencimiento;
    private LocalDate fechaDevolucion;

    private boolean devuelto;

    public Prestamo() {
    }

    public Prestamo(
            int id,
            int idEstudiante,
            int idLibro,
            LocalDate fechaPrestamo,
            LocalDate fechaVencimiento,
            LocalDate fechaDevolucion,
            boolean devuelto
    ) {

        this.id = id;
        this.idEstudiante = idEstudiante;
        this.idLibro = idLibro;
        this.fechaPrestamo = fechaPrestamo;
        this.fechaVencimiento = fechaVencimiento;
        this.fechaDevolucion = fechaDevolucion;
        this.devuelto = devuelto;
    }

    public Prestamo(
            int idEstudiante,
            int idLibro,
            LocalDate fechaPrestamo,
            LocalDate fechaVencimiento
    ) {

        this.idEstudiante = idEstudiante;
        this.idLibro = idLibro;
        this.fechaPrestamo = fechaPrestamo;
        this.fechaVencimiento = fechaVencimiento;
        this.fechaDevolucion = null;
        this.devuelto = false;
    }

    public int getId() {
        return id;
    }

    public void setId(
            int id
    ) {
        this.id = id;
    }

    public int getIdEstudiante() {
        return idEstudiante;
    }

    public void setIdEstudiante(
            int idEstudiante
    ) {
        this.idEstudiante = idEstudiante;
    }

    public int getIdLibro() {
        return idLibro;
    }

    public void setIdLibro(
            int idLibro
    ) {
        this.idLibro = idLibro;
    }

    public LocalDate getFechaPrestamo() {
        return fechaPrestamo;
    }

    public void setFechaPrestamo(
            LocalDate fechaPrestamo
    ) {
        this.fechaPrestamo = fechaPrestamo;
    }

    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(
            LocalDate fechaVencimiento
    ) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public LocalDate getFechaDevolucion() {
        return fechaDevolucion;
    }

    public void setFechaDevolucion(
            LocalDate fechaDevolucion
    ) {
        this.fechaDevolucion = fechaDevolucion;
    }

    public boolean isDevuelto() {
        return devuelto;
    }

    public void setDevuelto(
            boolean devuelto
    ) {
        this.devuelto = devuelto;
    }


    // =========================================
    // DETECTAR ATRASO
    // =========================================

    public boolean estaAtrasado() {

        if (devuelto) {

            if (fechaDevolucion == null) {
                return false;
            }

            return fechaDevolucion.isAfter(
                    fechaVencimiento
            );
        }

        return LocalDate.now().isAfter(
                fechaVencimiento
        );
    }


    @Override
    public String toString() {

        return "Préstamo #"
                + id
                + " - Libro "
                + idLibro;
    }
}