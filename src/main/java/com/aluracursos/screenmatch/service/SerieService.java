package com.aluracursos.screenmatch.service;

import com.aluracursos.screenmatch.dto.EpisodioDTO;
import com.aluracursos.screenmatch.dto.SerieDTO;
import com.aluracursos.screenmatch.model.Categoria;
import com.aluracursos.screenmatch.model.Episodio;
import com.aluracursos.screenmatch.model.Serie;
import com.aluracursos.screenmatch.repository.SerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SerieService {
    @Autowired
    SerieRepository repository;

    public List<SerieDTO> obtnerTodasLasSeries(){
        return convierteDatos(repository.findAll());

    }

    public List<SerieDTO> obtnerTop5() {
        return convierteDatos(repository.findTop5ByOrderByEvaluacionDesc());
    }

    public List<SerieDTO> convierteDatos(List<Serie> serie){
        return serie.stream().map(s -> new SerieDTO(s.getId(), s.getTitulo(), s.getTotalDeTemporadas(),
                        s.getEvaluacion(), s.getPoster(),
                        s.getAnio(), s.getGenero(), s.getSinopsis()))
                .collect(Collectors.toList());
    }

    public List<SerieDTO> obtnerLanzamientosMasRecientes(){
        return  convierteDatos(repository.lanzamientoMasReciente());
    }

    public SerieDTO obtenerPorId(Long id) {
        Optional<Serie> serieOptional = repository.findById(id);

        if (serieOptional.isPresent()){
            Serie s = serieOptional.get();
            return new SerieDTO(s.getId(), s.getTitulo(), s.getTotalDeTemporadas(),
                    s.getEvaluacion(), s.getPoster(),
                    s.getAnio(), s.getGenero(), s.getSinopsis());
        }else {
            return null;
        }
    }

    public List<EpisodioDTO> obtenerTodasLasTemporadas(Long id) {
        Optional<Serie> serieOptional = repository.findById(id);

        if (serieOptional.isPresent()){
            Serie s = serieOptional.get();
           return  s.getEpisodios().stream().map( e -> new EpisodioDTO(e.getTemporada(), e.getTitulo(), e.getNumeroEpisodio()))
                   .collect(Collectors.toList());
        }else {
            return null;
        }
    }

    public List<EpisodioDTO> obtnerTempodasPorId(Long idSerie, Long numTemporada) {
        return repository.obtenerTemporadasPorNumero(idSerie, numTemporada).stream()
                .map(e -> new EpisodioDTO(e.getTemporada(), e.getTitulo(),
                        e.getNumeroEpisodio()))
                .collect(Collectors.toList());


    }

    public List<SerieDTO> obtnerSeriesPorCategoria(String nombreGenero) {
        Categoria categoria = Categoria.fromEspanol(nombreGenero);
        return convierteDatos(repository.findByGenero(categoria));
    }
}

