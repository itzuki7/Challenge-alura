package com.alura.demo1.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "autores")
public class Autor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String nombre;
    private Integer fechaDeNacimiento;
    private Integer fechaDeMuerte;

    @OneToMany(mappedBy = "autor", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Libro> libros;

    public Autor() {}
    public Autor(DatosAutor d) {
        this.nombre = d.nombre();
        this.fechaDeNacimiento = d.fechaDeNacimiento();
        this.fechaDeMuerte = d.fechaDeMuerte();
    }

    public String getNombre() { return nombre; }
    public Integer getFechaDeNacimiento() { return fechaDeNacimiento; }
    public Integer getFechaDeMuerte() { return fechaDeMuerte; }
    public List<Libro> getLibros() { return libros; }
    public void setLibros(List<Libro> libros) { this.libros = libros; }
}