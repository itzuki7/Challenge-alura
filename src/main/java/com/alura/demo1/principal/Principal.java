package com.alura.demo1.principal;

import com.alura.demo1.model.*;
import com.alura.demo1.repository.AutorRepository;
import com.alura.demo1.repository.LibroRepository;
import com.alura.demo1.service.ConsumoAPI;
import com.alura.demo1.service.ConvierteDatos;

import java.util.Comparator;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Principal {
    private Scanner teclado = new Scanner(System.in);
    private ConsumoAPI consumoApi = new ConsumoAPI();
    private ConvierteDatos conversor = new ConvierteDatos();
    private final String URL_BASE = "https://gutendex.com/books/";

    private LibroRepository libroRepository;
    private AutorRepository autorRepository;

    public Principal(LibroRepository libroRepository, AutorRepository autorRepository) {
        this.libroRepository = libroRepository;
        this.autorRepository = autorRepository;
    }

    public void muestraElMenu() {
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                    ***************************************************
                    Elija su opcion:
                    1 - Buscar libro por título 
                    2 - Lista de libros registrados
                    3 - Lista de autores registrados
                    4 - Lista de autores vivos en un determinado año
                    5 - Lista de libros por idioma (ES / EN)
                    6 - Generar estadísticas de descargas
                    
                    0 - Salir
                    ***************************************************
                    """;
            System.out.println(menu);

            if (teclado.hasNextInt()) {
                opcion = teclado.nextInt();
                teclado.nextLine();

                switch (opcion) {
                    case 1 -> buscarLibroWeb();
                    case 2 -> listarLibrosRegistrados();
                    case 3 -> listarAutoresRegistrados();
                    case 4 -> listarAutoresVivosEnAnio();
                    case 5 -> listarLibrosPorIdioma();
                    case 6 -> mostrarEstadisticas();
                    case 0 -> System.out.println("Cerrando la aplicación...");
                    default -> System.out.println("Opción inválida.");
                }
            } else {
                System.out.println("Opción inválida. Ingrese un número correcto.");
                teclado.nextLine();
            }
        }
    }

    private DatosLibro getDatosLibro() {
        System.out.println("Ingrese el nombre del libro:");
        var nombreLibro = teclado.nextLine();
        var json = consumoApi.obtenerDatos(URL_BASE + "?search=" + nombreLibro.replace(" ", "+"));
        Datos datosBusqueda = conversor.obtenerDatos(json, Datos.class);

        return datosBusqueda.resultados().stream()
                .filter(l -> l.titulo().toUpperCase().contains(nombreLibro.toUpperCase()))
                .findFirst()
                .orElse(null);
    }

    private void buscarLibroWeb() {
        DatosLibro datos = getDatosLibro();

        if (datos != null) {
            if (libroRepository.existsByTitulo(datos.titulo())) {
                System.out.println("No se puede insertar el mismo libro más de una vez.");
                return;
            }

            Libro libro = new Libro(datos);
            DatosAutor datosAutor = datos.autor().get(0);
            Autor autor = autorRepository.findByNombre(datosAutor.nombre())
                    .orElseGet(() -> autorRepository.save(new Autor(datosAutor)));

            libro.setAutor(autor);
            libroRepository.save(libro);
            System.out.println(libro);
        } else {
            System.out.println("Libro no encontrado.");
        }
    }

    private void listarLibrosRegistrados() {
        List<Libro> libros = libroRepository.findAll();
        if (libros.isEmpty()) {
            System.out.println("No hay libros registrados.");
        } else {
            libros.stream()
                    .sorted(Comparator.comparing(Libro::getTitulo))
                    .forEach(System.out::println);
        }
    }

    private void listarAutoresRegistrados() {
        List<Autor> autores = autorRepository.findAll();
        if (autores.isEmpty()) {
            System.out.println("No hay autores registrados.");
        } else {
            autores.forEach(a -> {
                String librosStr = a.getLibros().stream()
                        .map(Libro::getTitulo)
                        .collect(Collectors.joining(", "));
                System.out.println("\nAutor: " + a.getNombre() +
                        "\nFecha de nacimiento: " + a.getFechaDeNacimiento() +
                        "\nFecha de fallecimiento: " + (a.getFechaDeMuerte() != null ? a.getFechaDeMuerte() : "N/A") +
                        "\nLibros: [" + librosStr + "]");
            });
        }
    }

    private void listarAutoresVivosEnAnio() {
        System.out.println("Ingrese el año para buscar autores vivos:");
        var input = teclado.nextLine();

        try {
            Integer anio = Integer.parseInt(input);
            List<Autor> autoresVivos = autorRepository.buscarAutoresVivosEnDeterminadoAnio(anio);

            if (autoresVivos.isEmpty()) {
                System.out.println("No se encontraron autores vivos en el año " + anio);
            } else {
                System.out.println("\n--- Autores vivos en " + anio + " ---");
                autoresVivos.forEach(a -> System.out.println("- " + a.getNombre()));
            }
        } catch (NumberFormatException e) {
            System.out.println("Error: Debe ingresar un año válido en formato numérico.");
        }
    }

    private void listarLibrosPorIdioma() {
        System.out.println("""
                Ingrese el idioma para buscar:
                es - español
                en - inglés
                """);
        var idioma = teclado.nextLine().toLowerCase();

        if (idioma.equals("es") || idioma.equals("en")) {
            List<Libro> libros = libroRepository.findByIdioma(idioma);
            if (libros.isEmpty()) {
                System.out.println("No hay libros registrados en ese idioma.");
            } else {
                libros.forEach(System.out::println);
            }
        } else {
            System.out.println("Idioma no válido.");
        }
    }

    private void mostrarEstadisticas() {
        List<Libro> libros = libroRepository.findAll();
        if (libros.isEmpty()) {
            System.out.println("No hay datos suficientes.");
            return;
        }

        System.out.println("\n--- CONTEO POR IDIOMA ---");
        System.out.println("Español (es): " + libroRepository.countByIdioma("es"));
        System.out.println("Inglés (en): " + libroRepository.countByIdioma("en"));

        DoubleSummaryStatistics stats = libros.stream()
                .mapToDouble(Libro::getNumeroDeDescargas)
                .summaryStatistics();

        System.out.println("\n--- ESTADÍSTICAS DE DESCARGAS ---");
        System.out.println("Media: " + String.format("%.2f", stats.getAverage()));
        System.out.println("Máxima: " + stats.getMax());
        System.out.println("Mínima: " + stats.getMin());
        System.out.println("Total evaluados: " + stats.getCount());
    }
}