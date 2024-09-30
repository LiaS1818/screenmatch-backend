package com.aluracursos.screenmatch.model;

import com.aluracursos.screenmatch.service.ConsultaChatGPT;
import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.persistence.*;

import java.util.List;
import java.util.OptionalDouble;

@Entity// Le decimos a hibernate que este sera una identidad de nuestra BD
@Table(name = "series")// Cambiamos el nombre de la tabla de Serie -> series
public class Serie {
    @Id// indicar nuestro identificador
    @GeneratedValue(strategy = GenerationType.IDENTITY)//id auto incrmental
    private long Id;
    @Column(unique = true)
    private String titulo;
    private Integer totalDeTemporadas;
    private Double evaluacion;
    private String poster;
    private String fechaLanzamiento;
    @Enumerated(EnumType.STRING) // para evitar problemas utilizamos string, ya que en algun momento la posicion ya no sea la misma para cada genero
    private  Categoria genero;
    private String sinopsis;
    //@Transient // Le indico a JPA que dentro de la identidad existe una lista episodios pero que por el momento no se va a utilizar
    @OneToMany(mappedBy = "serie", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Episodio> episodios;

    public Serie(){}

    public Serie(DatosSerie datosSerie){
        this.titulo = datosSerie.titulo();
        this.totalDeTemporadas = datosSerie.totalDeTemporadas();
        this.evaluacion = OptionalDouble.of(Double.valueOf(datosSerie.evaluacion())).orElse(0);
        this.poster = datosSerie.poster();
        this.fechaLanzamiento = datosSerie.anio();
        this.genero = Categoria.fromString(datosSerie.genero().split(",")[0].trim()); //trim evita trer datos vacios
        this.sinopsis = datosSerie.sinopsis();
        // this.sinopsis = ConsultaChatGPT.obtenerTraduccion(datosSerie.sinopsis());
    }

    public List<Episodio> getEpisodios() {
        return episodios;
    }

    public void setEpisodios(List<Episodio> episodios) {
        episodios.forEach(e -> e.setSerie(this));
        this.episodios = episodios;
    }

    public String getTitulo() {
        return titulo;
    }

    public Integer getTotalDeTemporadas() {
        return totalDeTemporadas;
    }

    public Double getEvaluacion() {
        return evaluacion;
    }


    public Categoria getGenero() {
        return genero;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }



    public String getAnio() {
        return fechaLanzamiento;
    }

    public void setAnio(String fechaDeLanzamiento) {
        this.fechaLanzamiento = Serie.this.fechaLanzamiento;
    }

    public String getSinopsis() {
        return sinopsis;
    }

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }

    @Override
    public String toString() {
        return
                " genero=" + genero +
                " titulo='" + titulo + '\'' +
                ", totalDeTemporadas=" + totalDeTemporadas +
                ", evaluacion=" + evaluacion +
                ", poster='" + poster + '\'' +
                ", fechaDeLanzamiento='" + fechaLanzamiento + '\'' +
                ", sinopsis='" + sinopsis + '\''+
                ", Episodios='" +  episodios +'\''
                ;
    }
}
