package com.G2T5203.wingit.seat;

import com.G2T5203.wingit.entities.Plane;
import com.G2T5203.wingit.entities.Seat;
import com.G2T5203.wingit.entities.SeatPk;
import com.G2T5203.wingit.plane.PlaneNotFoundException;
import com.G2T5203.wingit.plane.PlaneRepository;
import com.G2T5203.wingit.seat.SeatBadRequestException;
import com.G2T5203.wingit.seat.SeatRepository;
import com.G2T5203.wingit.seat.SeatService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SeatServiceTest {

    @InjectMocks
    private SeatService seatService;

    @Mock
    private SeatRepository seatRepository;

    @Mock
    private PlaneRepository planeRepository;

    @Test
    void createSeat_Success() {
        SeatSimpleJson seatSimpleJson = new SeatSimpleJson("Plane123", "A1", "Economy", 1.0);

        Plane plane = new Plane("Plane123", 5, "Plane Model");
        when(planeRepository.findById("Plane123")).thenReturn(Optional.of(plane));
        // create a new Seat with valid seatClass
        Seat createdSeat = new Seat();
        createdSeat.setSeatClass("Economy"); // Set the seatClass
        createdSeat.setPriceFactor(1.0);
        // mock the save operation to return the createdSeat
        when(seatRepository.save(any(Seat.class))).thenReturn(createdSeat);
        createdSeat = seatService.createSeat(seatSimpleJson);
        assertNotNull(createdSeat);
        assertEquals("Economy", createdSeat.getSeatClass()); // Check the seatClass
        assertEquals(1.0, createdSeat.getPriceFactor()); // Check the priceFactor
    }



    @Test
    void createSeat_PlaneNotFound() {
        SeatSimpleJson seatSimpleJson = new SeatSimpleJson("NonExistentPlane", "A1", "Economy", 1.0);
        when(planeRepository.findById("NonExistentPlane")).thenReturn(Optional.empty());

        PlaneNotFoundException exception = org.junit.jupiter.api.Assertions.assertThrows(
                PlaneNotFoundException.class,
                () -> seatService.createSeat(seatSimpleJson)
        );

        assertEquals("Could not find plane NonExistentPlane", exception.getMessage());
        verify(planeRepository).findById("NonExistentPlane");
    }

    @Test
    void createSeat_SeatAlreadyExists() {
        SeatSimpleJson seatSimpleJson = new SeatSimpleJson("Plane123", "A1", "Economy", 1.0);
        Plane plane = new Plane("Plane123", 5, "Plane Model");
        when(planeRepository.findById("Plane123")).thenReturn(Optional.of(plane));
        SeatPk seatPk = new SeatPk(plane, "A1");
        when(seatRepository.existsById(seatPk)).thenReturn(true);

        SeatBadRequestException exception = org.junit.jupiter.api.Assertions.assertThrows(
                SeatBadRequestException.class,
                () -> seatService.createSeat(seatSimpleJson)
        );
        assertEquals("BAD REQUEST: Seat already exists.", exception.getMessage());

        verify(planeRepository).findById("Plane123");
        verify(seatRepository).existsById(seatPk);
    }
}

