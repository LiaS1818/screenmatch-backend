package com.aluracursos.screenmatch.controller;

import com.aluracursos.screenmatch.dto.EpisodioDTO;
import com.aluracursos.screenmatch.dto.SerieDTO;
import com.aluracursos.screenmatch.model.Serie;
import com.aluracursos.screenmatch.repository.SerieRepository;
import com.aluracursos.screenmatch.service.SerieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController //Modelo Rest
@RequestMapping("/series")
public class SerieController {

    @Autowired
    private SerieService servicio;

    @GetMapping()
    public List<SerieDTO> obtnerTodasLasSeries(){
        return servicio.obtnerTodasLasSeries();
    }

    @GetMapping("/top5")
    public List<SerieDTO> obtnerTop5(){
        return servicio.obtnerTop5();
    }

    @GetMapping("/lanzamientos")
    public  List<SerieDTO> obtnerLanzamientoMasRecientes(){
        return servicio.obtnerLanzamientosMasRecientes();
    }



    @GetMapping("/inicio")
    public String obtnerMensaje(){
        return "Probado LiveReloading";
    }

    @GetMapping("/{id}")
    public SerieDTO obtenerPorId(@PathVariable Long id){
        return servicio.obtenerPorId(id);
    }

    @GetMapping("/{id}/temporadas/todas")
    public List<EpisodioDTO> obtenerTodasLasTemporadas(@PathVariable Long id){
        return servicio.obtenerTodasLasTemporadas(id);
    }

    @GetMapping("/{id}/temporadas/{id_temporada}")
    public List<EpisodioDTO> obtnerTemporadaPorId(@PathVariable Long id, @PathVariable Long id_temporada){
        return servicio.obtnerTempodasPorId(id, id_temporada);
    }

    @GetMapping("/categoria/{nombreGenero}")
    public List<SerieDTO> obtenerSeriesPorCategoria(@PathVariable String nombreGenero){
        return servicio.obtnerSeriesPorCategoria(nombreGenero);
    }
}
