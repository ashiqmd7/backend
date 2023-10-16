package com.G2T5203.wingit.route;

<<<<<<< HEAD
=======
import com.G2T5203.wingit.route.Route;
>>>>>>> abff0c6463a63aeb6745b00a6f86d7b2dd90e005
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

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
    void getAllRoutes_Success() {
        List<Route> routes = Collections.singletonList(new Route());
        when(routeRepository.findAll()).thenReturn(routes);
        List<Route> result = routeService.getAllRoutes();
        verify(routeRepository).findAll();
        assertEquals(routes, result);
    }

    @Test
    void getRouteById_Success() {
        RouteService routeService = new RouteService(routeRepository);
        int routeId = 1;
        Route expectedRoute = new Route();
        expectedRoute.setRouteId(routeId);
        when(routeRepository.findById(routeId)).thenReturn(Optional.of(expectedRoute));
        Route result = routeService.getRoute(routeId);
        verify(routeRepository).findById(routeId);
        assertEquals(expectedRoute, result);
    }

    @Test
    void getRouteById_RouteNotExist_Failure() {
        RouteService routeService = new RouteService(routeRepository);
        int routeId = 1;
        when(routeRepository.findById(routeId)).thenReturn(Optional.empty());
        RouteNotFoundException exception = assertThrows(RouteNotFoundException.class, () -> routeService.getRoute(routeId));
        verify(routeRepository).findById(routeId);
        assertEquals("Could not find route 1", exception.getMessage());
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
    void createRoute_DuplicateRouteId_Failure() {
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
    void deleteRoute_RouteNotFound_Failure() {
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
    void updateRoute_RouteNotFound_Failure() {
        Route updatedRoute = new Route();
        updatedRoute.setRouteId(1);
        when(routeRepository.existsById(1)).thenReturn(false);
        RouteNotFoundException exception = assertThrows(RouteNotFoundException.class, () -> routeService.updateRoute(updatedRoute));
        verify(routeRepository).existsById(1);
        assertEquals("Could not find route 1", exception.getMessage());
    }
}