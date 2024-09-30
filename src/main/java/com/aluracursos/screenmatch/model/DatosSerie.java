package com.aluracursos.screenmatch.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
//Json alias puede leer los datos que vienen de la API y transformarlos a los que yo requiera. Solo permite leer

@JsonIgnoreProperties(ignoreUnknown = true)
public record DatosSerie(@JsonAlias("Title") String titulo,
                         @JsonAlias("totalSeasons") Integer totalDeTemporadas,
                         @JsonAlias("imdbRating") String evaluacion,
                         @JsonAlias("Poster") String poster,
                         @JsonAlias("Year") String anio,
                         @JsonAlias("Genre") String genero,
                         @JsonAlias("Plot") String sinopsis) {
}
