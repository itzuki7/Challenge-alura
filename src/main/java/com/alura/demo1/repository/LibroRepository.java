package com.alura.demo1.repository;

import com.alura.demo1.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LibroRepository extends JpaRepository<Libro, Long> {
    boolean existsByTitulo(String titulo);
    List<Libro> findByIdioma(String idioma);

    Long countByIdioma(String idioma);
}