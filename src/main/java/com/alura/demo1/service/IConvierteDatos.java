package com.alura.demo1.service;

public interface IConvierteDatos {
    <T> T obtenerDatos(String json, Class<T> clase);
}