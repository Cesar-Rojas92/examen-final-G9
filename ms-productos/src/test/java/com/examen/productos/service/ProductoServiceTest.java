package com.examen.productos.service;

import com.examen.productos.dto.ProductoRequest;
import com.examen.productos.entity.Producto;
import com.examen.productos.repository.ProductoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private ProductoService productoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCrearProducto() {
        ProductoRequest request = new ProductoRequest("Espada", 500.0, "Armas");
        Producto producto = Producto.builder()
                .id(1L)
                .nombre("Espada")
                .precio(500.0)
                .categoria("Armas")
                .build();

        when(productoRepository.save(any())).thenReturn(producto);

        var response = productoService.crearProducto(request);

        assertEquals("Espada", response.getNombre());
    }

    @Test
    void testListarProductos() {
        when(productoRepository.findAll()).thenReturn(List.of(
                new Producto(1L, "Espada", 500.0, "Armas")
        ));

        var productos = productoService.listarProductos();

        assertEquals(1, productos.size());
    }

    @Test
    void testActualizarProducto() {
        Producto productoExistente = new Producto(1L, "Espada vieja", 400.0, "Armas");
        ProductoRequest request = new ProductoRequest("Espada nueva", 600.0, "Armas mejoradas");

        when(productoRepository.findById(1L)).thenReturn(Optional.of(productoExistente));
        when(productoRepository.save(any(Producto.class))).thenReturn(productoExistente);

        var response = productoService.actualizarProducto(1L, request);

        assertEquals("Espada nueva", response.getNombre());
        assertEquals(600.0, response.getPrecio());
    }

    @Test
    void testEliminarProducto() {
        Producto productoExistente = new Producto(1L, "Espada", 500.0, "Armas");

        when(productoRepository.findById(1L)).thenReturn(Optional.of(productoExistente));
        doNothing().when(productoRepository).delete(productoExistente);

        productoService.eliminarProducto(1L);

        verify(productoRepository, times(1)).delete(productoExistente);
    }
}
