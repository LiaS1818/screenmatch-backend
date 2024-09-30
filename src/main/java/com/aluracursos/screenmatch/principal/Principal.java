package com.aluracursos.screenmatch.principal;

import com.aluracursos.screenmatch.model.DatosEpisodio;
import com.aluracursos.screenmatch.model.DatosSerie;
import com.aluracursos.screenmatch.model.DatosTemporadas;
import com.aluracursos.screenmatch.model.Episodio;
import com.aluracursos.screenmatch.service.ConsumoAPI;
import com.aluracursos.screenmatch.service.ConvierteDatos;
import org.w3c.dom.ls.LSOutput;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class Principal {
    private Scanner teclado = new Scanner(System.in);
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private final String URL_BASE = "http://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=f2c080b5";
    private ConvierteDatos conversor = new ConvierteDatos();


    public void muestraElMenu(){
        System.out.println("Por favor escribe el nombre de la serie que deseas buscar");
        //Busca los datos generales de las series
        var nombreSerie = teclado.nextLine();
        var json = consumoAPI.obtenerDatos(URL_BASE + nombreSerie.replace(" ", "+")  + API_KEY);
        var datos = conversor.obtenerDatos(json, DatosSerie.class);
       // System.out.println(datos);

        //Obtner los datos de las temporadas
        ArrayList<DatosTemporadas> listaTemporadas = new ArrayList<>();
        for (int i = 1; i <= datos.totalDeTemporadas() ; i++) {

            json = consumoAPI.obtenerDatos(URL_BASE + nombreSerie.replace(" ", "+") + "&Season=" + i + API_KEY);
            var datosTemporadas = conversor.obtenerDatos(json, DatosTemporadas.class);
            listaTemporadas.add(datosTemporadas);
        }

        listaTemporadas.forEach(System.out::println);

        // Mostrar solo el titulo de los episodios para las temporadas
//        for (int i = 0; i < datos.totalDeTemporadas(); i++){
//            //traer la lista de episodios de cada temporada
//            List<DatosEpisodio> episodiosTemporada = listaTemporadas.get(i).episodios();
//            for (DatosEpisodio datosEpisodio : episodiosTemporada) {
//                System.out.println(datosEpisodio.titulo());
//            }
//        }

        // Forma de simplificar el codigo anterior con funciones lamdas
        AtomicInteger cont = new AtomicInteger();
        listaTemporadas.forEach(t -> {
            t.episodios().forEach(e -> {
                System.out.println(cont.incrementAndGet() + ".-" + e.titulo());
            });
        });

        //Transformar los datos a una unica lista

        List<DatosEpisodio> datosEpisodioList = listaTemporadas.stream()
                .flatMap(t -> t.episodios().stream())
                .collect(Collectors.toList());

        //Obtener los 5 episodios
//        System.out.println("Top 5 mejores ipisodios");
//        datosEpisodioList.stream()
//                .filter(e -> !e.evaluacion().equalsIgnoreCase("N/A"))
//                .peek(e -> System.out.println("Primer filtro (n/A" + e))
//                .sorted(Comparator.comparing(DatosEpisodio::evaluacion).reversed())
//                .peek(e -> System.out.println("Ordenacion (M>m)" + e))
//                .limit(5)
//                .forEach(System.out::println);
        //Convirtiendo los datos a una lista de tipo episodio
        List<Episodio> episodios = listaTemporadas.stream()
                .flatMap(t -> t.episodios().stream()
                        .map(d -> new Episodio(t.numero(), d)))
                .collect(Collectors.toList());
        episodios.forEach(System.out::println);

        //Busqueda de episodios apartir de X año
//        System.out.println("Por favor indica el año a partir el cual deseas ver los episodios");
//        var fecha = teclado.nextInt();
//        teclado.nextLine();
//
//        LocalDate fechaBusqueda = LocalDate.of(fecha, 1, 1);
//
//        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//        episodios.stream()
//                .filter(e -> e.getFechaLanzamiento() != null && e.getFechaLanzamiento().isAfter(fechaBusqueda))
//                .forEach(e -> System.out.println(
//                    " Temporada " + e.getTemporada() +
//                    " Episodio " + e.getTitulo() +
//                    " No. " + e.getNumeroEpisodio() +
//                    " Fecha de Lanzamiento " + e.getFechaLanzamiento().format(dtf)
//                ));

        // Busca episodio por pedazo de titulo
//        System.out.println("Por favor escriba el titulo del episodio que desea ver");
//        var titulo = teclado.nextLine();
//        Optional<Episodio> episodioBuscado = episodios.stream()
//                .filter(e -> e.getTitulo().toUpperCase().contains(titulo.toUpperCase()))
//                .findFirst();
//        if (episodioBuscado.isPresent()){
//            System.out.println("Episodio encontrado");
//            System.out.println("Los datos son "+ episodioBuscado.get());
//        }else {
//            System.out.println("Episodio con encontrado");
//        }
        //
        Map<Integer, Double> evaluacionesPorTemporada = episodios.stream()
                .filter(e -> e.getEvaluacion() > 0.0)
                .collect(Collectors.groupingBy(Episodio::getTemporada,
                        Collectors.averagingDouble(Episodio::getEvaluacion)));

        System.out.println(evaluacionesPorTemporada);

        DoubleSummaryStatistics est = episodios.stream()
                .filter(e -> e.getEvaluacion() > 0.0)
                .collect(Collectors.summarizingDouble(Episodio::getEvaluacion));
        System.out.println("Total de episodios calificados" + est.getCount());
        System.out.println("Calaficacion mínima" + est.getMin());
        System.out.println("Calificacion máxima" + est.getMax());

    }
}
