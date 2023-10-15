package com.G2T5203.wingit.route;

import com.G2T5203.wingit.entities.Route;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class RouteServiceTest {

    private RouteService routeService;

    @Mock
    private RouteRepository routeRepository;

    @BeforeEach
    public void setUp() {
        routeService = new RouteService(routeRepository);
    }

    @Test
    void createRoute_Success() {
        Route newRoute = new Route();
        newRoute.setRouteId(1);
        when(routeRepository.existsById(1)).thenReturn(false);
        when(routeRepository.save(newRoute)).thenReturn(newRoute);
        Route createdRoute = routeService.createRoute(newRoute);
        verify(routeRepository).existsById(1);
        verify(routeRepository).save(newRoute);
        assertEquals(1, createdRoute.getRouteId());
    }

    @Test
    void createRoute_RouteIdExists() {
        Route newRoute = new Route();
        newRoute.setRouteId(1);
        when(routeRepository.existsById(1)).thenReturn(true);
        RouteBadRequestException exception = assertThrows(RouteBadRequestException.class, () -> routeService.createRoute(newRoute));
        verify(routeRepository).existsById(1);
        assertEquals("BAD REQUEST: RouteId already exists", exception.getMessage());
    }

    @Test
    void deleteRoute_Success() {
        when(routeRepository.existsById(1)).thenReturn(true);
        routeService.deleteRoute(1);
        verify(routeRepository).existsById(1);
        verify(routeRepository).deleteById(1);
    }

    @Test
    void deleteRoute_RouteNotFound() {
        when(routeRepository.existsById(1)).thenReturn(false);
        RouteNotFoundException exception = assertThrows(RouteNotFoundException.class, () -> routeService.deleteRoute(1));
        verify(routeRepository).existsById(1);
        assertEquals("Could not find route 1", exception.getMessage());
    }

    @Test
    void updateRoute_Success() {
        Route updatedRoute = new Route();
        updatedRoute.setRouteId(1);
        when(routeRepository.existsById(1)).thenReturn(true);
        when(routeRepository.save(updatedRoute)).thenReturn(updatedRoute);
        Route result = routeService.updateRoute(updatedRoute);
        verify(routeRepository).existsById(1);
        verify(routeRepository).save(updatedRoute);
        assertEquals(1, result.getRouteId());
    }

    @Test
    void updateRoute_RouteNotFound() {
        Route updatedRoute = new Route();
        updatedRoute.setRouteId(1);
        when(routeRepository.existsById(1)).thenReturn(false);
        RouteNotFoundException exception = assertThrows(RouteNotFoundException.class, () -> routeService.updateRoute(updatedRoute));
        verify(routeRepository).existsById(1);
        assertEquals("Could not find route 1", exception.getMessage());
    }


    @Test
    void getAllRoutes_Success() {
        List<Route> routes = Collections.singletonList(new Route());
        when(routeRepository.findAll()).thenReturn(routes);
        List<Route> result = routeService.getAllRoutes();
        verify(routeRepository).findAll();
        assertEquals(routes, result);
    }
}



