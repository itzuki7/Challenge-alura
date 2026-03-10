package com.alura.demo1.model;

import jakarta.persistence.*;

@Entity
@Table(name = "libros")
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String titulo;
    private String idioma;
    private Double numeroDeDescargas;
    @ManyToOne
    private Autor autor;

    public Libro() {}
    public Libro(DatosLibro d) {
        this.titulo = d.titulo();
        this.idioma = d.idiomas().get(0);
        this.numeroDeDescargas = d.numeroDeDescargas();
    }

    // Getters y Setters completos
    public String getTitulo() { return titulo; }
    public String getIdioma() { return idioma; }
    public Double getNumeroDeDescargas() { return numeroDeDescargas; }
    public void setAutor(Autor autor) { this.autor = autor; }
}