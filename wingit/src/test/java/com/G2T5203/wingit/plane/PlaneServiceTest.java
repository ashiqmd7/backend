package com.G2T5203.wingit.plane;

import com.G2T5203.wingit.plane.Plane;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
public class PlaneServiceTest {

    @InjectMocks
    private PlaneService planeService;

    @Mock
    private PlaneRepository planeRepository;

    @Test
    void getAllPlanes_Success() {
        List<Plane> planes = new ArrayList<>();
        when(planeRepository.findAll()).thenReturn(planes);
        List<Plane> result = planeService.getAllPlanes();
        assertEquals(planes, result);
    }

    @Test
    void getById_PlaneExists_Success() {
        String planeId = "Plane123";
        Plane plane = new Plane(planeId, 5, "Plane Model");
        when(planeRepository.findById(planeId)).thenReturn(Optional.of(plane));
        Plane result = planeService.getById(planeId);
        assertNotNull(result);
        assertEquals(planeId, result.getPlaneId());
    }

    @Test
    void getById_PlaneNotFound_Failure() {
        String planeId = "NonExistentPlane";
        when(planeRepository.findById(planeId)).thenReturn(Optional.empty());
        Plane result = planeService.getById(planeId);
        assertNull(result);
    }

    @Test
    void createPlane_Success() {
        Plane newPlane = new Plane("NewPlane", 3, "New Model");
        when(planeRepository.existsById(newPlane.getPlaneId())).thenReturn(false);
        when(planeRepository.save(newPlane)).thenReturn(newPlane);
        Plane result = planeService.createPlane(newPlane);
        assertNotNull(result);
        assertEquals(newPlane.getPlaneId(), result.getPlaneId());
    }

    @Test
    void createPlane_PlaneIdExists_Failure() {
        Plane existingPlane = new Plane("ExistingPlane", 3, "Existing Model");
        when(planeRepository.existsById(existingPlane.getPlaneId())).thenReturn(true);
        PlaneBadRequestException exception = assertThrows(PlaneBadRequestException.class, () -> {
            planeService.createPlane(existingPlane);
        });
        assertEquals("BAD REQUEST: PlaneId already exists.", exception.getMessage());
    }

    @Test
    void deletePlaneById_PlaneExists_Success() {
        String planeId = "Plane123";
        when(planeRepository.existsById(planeId)).thenReturn(true);
        assertDoesNotThrow(() -> planeService.deletePlaneById(planeId));
        verify(planeRepository).deleteById(planeId);
    }

    @Test
    void deletePlaneById_PlaneNotFound_Failure() {
        String planeId = "NonExistentPlane";
        when(planeRepository.existsById(planeId)).thenReturn(false);
        PlaneNotFoundException exception = assertThrows(PlaneNotFoundException.class, () -> {
            planeService.deletePlaneById(planeId);
        });
        assertEquals("Could not find plane " + planeId, exception.getMessage());
    }

    @Test
    void updatePlane_PlaneExists_Success() {
        Plane updatedPlane = new Plane("UpdatedPlane", 4, "Updated Model");
        when(planeRepository.existsById(updatedPlane.getPlaneId())).thenReturn(true);
        when(planeRepository.save(updatedPlane)).thenReturn(updatedPlane);
        Plane result = planeService.updatePlane(updatedPlane);
        assertNotNull(result);
        assertEquals(updatedPlane.getPlaneId(), result.getPlaneId());
    }

    @Test
    void updatePlane_PlaneNotFound_Failure() {
        Plane nonExistentPlane = new Plane("NonExistentPlane", 3, "NonExistent Model");
        when(planeRepository.existsById(nonExistentPlane.getPlaneId())).thenReturn(false);

        PlaneNotFoundException exception = assertThrows(PlaneNotFoundException.class, () -> {
            planeService.updatePlane(nonExistentPlane);
        });
        assertEquals("Could not find plane " + nonExistentPlane.getPlaneId(), exception.getMessage());
    }
}

