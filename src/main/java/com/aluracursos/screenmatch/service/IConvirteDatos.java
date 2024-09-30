package com.aluracursos.screenmatch.service;

public interface IConvirteDatos {
    <T> T obtenerDatos(String json, Class<T> clase );

}
