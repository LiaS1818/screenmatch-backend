package com.aluracursos.screenmatch.principal;

import com.aluracursos.screenmatch.model.*;
import com.aluracursos.screenmatch.repository.SerieRepository;
import com.aluracursos.screenmatch.service.ConsumoAPI;
import com.aluracursos.screenmatch.service.ConvierteDatos;

import java.util.*;
import java.util.stream.Collectors;

public class Principal2 {
    private Scanner teclado = new Scanner(System.in);
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private final String URL_BASE = "http://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=f2c080b5";
    private ConvierteDatos conversor = new ConvierteDatos();
    private List<DatosSerie> datosSeries = new ArrayList<>();
    private SerieRepository repositorio;
    private List<Serie> series;
    private Optional<Serie> serieBuscada;

    public Principal2(SerieRepository repository) {
        this.repositorio = repository;
    }

    public void muestraElMenu(){
        var opcion = -1;

        while (opcion != 0){
            var menu = """
                    1 - Buscar series
                    2 - Buscar episodios
                    3 - Mostrar series buscadas
                    4 - Buscar series por título
                    5 - Top 5 Mejores Series
                    6 - Buscar Series por Categoria
                    7 - Buscar Serie por un maximo de temporadas y calificacion
                    8 - Buscar episodios por titulo
                    9 - Top 5 mejores episodios por Serie
                    0 - Salir
                    
                    """;
            System.out.println(menu);
            opcion = teclado.nextInt();
            teclado.nextLine();

            switch (opcion){
                case 1:
                    buscarSerieWeb();
                    break;
                case 2:
                    buscarEpisodioPorSerie();
                    break;
                case 3:
                    mostrarSeriesBuscadas();
                    break;
                case 4:
                    buscarSeriePorTitulo();
                    break;
                case 5:
                    buscarTop5Series();
                    break;
                case 6:
                    buscarSeriesPorCategoria();
                    break;
                case 7:
                    buscarPorTemporadasYEvalucion();
                    break;
                case 8:
                    buscarEpisodiosPorTitulo();
                    break;
                case 9:
                    buscarTop5Episodios();
                    break;
                case 0:
                    System.out.println("Cerrando la aplicacion...");
                    break;
                default:
                    System.out.println("Opcion no valida")
                    ;
            }
        }
    }

    private void buscarEpisodioPorSerie() {
        mostrarSeriesBuscadas();
        System.out.println("Escribe el nombre de la serie de la cual quieres ver los episodios");
        var nombreSerie = teclado.nextLine();

        Optional<Serie> serieOptional = series.stream()
                //filtramos el nombre de la serie con una expresion lambda, y tratamos lo que venga de la base de datos a minusculas para compararlo con lo que introduzca el usuario, de igual manera en minusculas
                .filter(s -> s.getTitulo().toLowerCase().contains(nombreSerie.toLowerCase()))
                .findFirst();
        if (serieOptional.isPresent()){
            var serieEncontrada = serieOptional.get(); //si hay una serie contrada, le asignamos el valor encontrado
            List<DatosTemporadas> temporadas = new ArrayList<>();

            if (serieEncontrada.getEpisodios().isEmpty()){

                for (int i = 1; i<= serieEncontrada.getTotalDeTemporadas(); i++){
                    var json = consumoAPI.obtenerDatos(URL_BASE + serieEncontrada.getTitulo().replace(" ", "+")+ "&Season=" + i + API_KEY);
                    DatosTemporadas datosTemporadas = conversor.obtenerDatos(json, DatosTemporadas.class);
                    temporadas.add(datosTemporadas);
                }

                temporadas.forEach(System.out::println);
                List<Episodio> episodios = temporadas.stream()
                        .flatMap(d -> d.episodios().stream()
                                .map(e -> new Episodio(d.numero(), e)))
                        .collect(Collectors.toList());
                serieEncontrada.setEpisodios(episodios);

                repositorio.save(serieEncontrada);
            }else {
                System.out.println("Ya se han guardado los episodios de esta series");
            }
        }
    }

    private DatosSerie getDatosSerie() {
        System.out.println("Escribe el nombre de la serie que deseas buscar");
        var nombreSerie = teclado.nextLine();
        var json = consumoAPI.obtenerDatos(URL_BASE + nombreSerie.replace(" ", "+") + API_KEY);
        System.out.println(json);
        return conversor.obtenerDatos(json, DatosSerie.class);
    }

    private void buscarSerieWeb() {
        DatosSerie datos = getDatosSerie();
        Serie serie = new Serie(datos);
        repositorio.save(serie);
        //datosSeries.add(datos);
        System.out.println(datos);
    }

    private void mostrarSeriesBuscadas() {
        series  = repositorio.findAll();
        series.stream()
                .sorted(Comparator.comparing(Serie::getGenero))
                .forEach(System.out::println);
    }

    private void buscarSeriePorTitulo(){
        mostrarSeriesBuscadas();
        System.out.println("Escribe el nombre de la serie que quieres buscar");
        var nombreSerie = teclado.nextLine();

        serieBuscada = repositorio.findByTituloContainsIgnoreCase(nombreSerie);
        // tratamiento de optional
        if (serieBuscada.isPresent()){
            System.out.println("La serie buscada es: "+ serieBuscada.get());
        }else {
            System.out.println("Serie no encontrada");
        }
    }

    private void buscarTop5Series(){
        List<Serie> top5Series = new ArrayList<>();
        top5Series = repositorio.findTop5ByOrderByEvaluacionDesc();

        if (top5Series.isEmpty()){
            System.out.println("No hay series registradas en la base de datos");
        } else {
            top5Series.forEach( serie -> {
                System.out.println(" Serie: " + serie.getTitulo() + " Evaluacion: " + serie.getEvaluacion());
            });
        }
    }// buscarTop5Series

    private void buscarSeriesPorCategoria(){
        System.out.println("Escriba el genero/categoria de la serie que desea buscar");
        var genero = teclado.nextLine();
        var categoria = Categoria.fromEspanol(genero);
        List<Serie> seriesPorCategoria = repositorio.findByGenero(categoria);
        System.out.println("Las series econtradas con esta categoría" + genero);
        seriesPorCategoria.forEach(System.out::println);
    }// buscarSeriesPorCategoria

    private void buscarPorTemporadasYEvalucion(){
        System.out.println("Escribe el numero de temporadas");
        int numeroTemporadas = teclado.nextInt();
        System.out.println("Escribe la evalucion minima para la serie");
        double evaluacion = teclado.nextDouble();

        List<Serie> seriesFiltradas = repositorio.seriesPorTemporadaYEvalucion(numeroTemporadas, evaluacion);
        System.out.println("*** Series Filtradas ***");
        seriesFiltradas.forEach(s -> {
            System.out.println(s.getTitulo() + " - evalucion: " + s.getEvaluacion());
        });
    }

    private void buscarEpisodiosPorTitulo(){
        System.out.println("Escribe el nombre del episodio que deseas buscar");
        var nombreEpisodio = teclado.nextLine();
        List<Episodio> episodiosEncontrados = repositorio.episodiosPorNombre(nombreEpisodio);
        episodiosEncontrados.forEach(e ->
                System.out.printf("Serie: %s - Temporada  %s - Episodio %s - No %s - Evaluación %s",
                        e.getSerie().getTitulo(),
                        e.getTemporada(), e.getTitulo(),
                        e.getNumeroEpisodio(),
                        e.getEvaluacion()));
    }

    private void buscarTop5Episodios(){
       buscarSeriePorTitulo();
       if (serieBuscada.isPresent()){
           Serie serie = serieBuscada.get();
           List<Episodio> topEpisodios = repositorio.top5Episodios(serie);
           topEpisodios.forEach(e ->
                   System.out.printf("Serie: %s - Temporada  %s - Episodio %s - No %s - Evaluación %s \n",
                           e.getSerie().getTitulo(),
                           e.getTemporada(), e.getTitulo(),
                           e.getNumeroEpisodio(),
                           e.getEvaluacion()));
       }
    }
}
