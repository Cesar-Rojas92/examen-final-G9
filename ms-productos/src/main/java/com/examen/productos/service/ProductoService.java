package com.examen.productos.service;

import com.examen.productos.dto.ProductoRequest;
import com.examen.productos.dto.ProductoResponse;
import com.examen.productos.entity.Producto;
import com.examen.productos.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoResponse crearProducto(ProductoRequest request) {
        Producto producto = Producto.builder()
                .nombre(request.getNombre())
                .precio(request.getPrecio())
                .categoria(request.getCategoria())
                .build();

        Producto guardado = productoRepository.save(producto);

        return ProductoResponse.builder()
                .id(guardado.getId())
                .nombre(guardado.getNombre())
                .precio(guardado.getPrecio())
                .categoria(guardado.getCategoria())
                .build();
    }

    public List<ProductoResponse> listarProductos() {
        return productoRepository.findAll().stream()
                .map(p -> ProductoResponse.builder()
                        .id(p.getId())
                        .nombre(p.getNombre())
                        .precio(p.getPrecio())
                        .categoria(p.getCategoria())
                        .build())
                .collect(Collectors.toList());
    }

    public ProductoResponse actualizarProducto(Long id, ProductoRequest request) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        producto.setNombre(request.getNombre());
        producto.setPrecio(request.getPrecio());
        producto.setCategoria(request.getCategoria());

        Producto actualizado = productoRepository.save(producto);

        return ProductoResponse.builder()
                .id(actualizado.getId())
                .nombre(actualizado.getNombre())
                .precio(actualizado.getPrecio())
                .categoria(actualizado.getCategoria())
                .build();
    }

    public void eliminarProducto(Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        productoRepository.delete(producto);
    }
}
