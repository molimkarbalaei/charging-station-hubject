package com.hubject.chargingstation.service;


import com.hubject.chargingstation.dto.ChargingStationRequestDto;
import com.hubject.chargingstation.dto.ChargingStationResponseDto;
import com.hubject.chargingstation.entity.ChargingStation;
import com.hubject.chargingstation.entity.GoogleCoordinate;
import com.hubject.chargingstation.repository.ChargingStationRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@SpringBootTest
public class ChargingStationServiceImplTest {
// mock the repository and mock the reponse of the repository

    @Mock
    private ChargingStationRepository repository;

    @Mock
    private SpecificationResolver specificationResolver;

    @InjectMocks
    private ChargingStationServiceImpl service;

    @Test
    public void testGetAllChargingStations_shouldReturnCS() {
        //given
        ChargingStation stations = new ChargingStation("1", "Berlin", BigDecimal.valueOf(50.0), new GoogleCoordinate(48.7, 9.11), "12345");
        when(repository.findAll()).thenReturn(List.of(stations));

        //when
        List<ChargingStationResponseDto> result = service.getAllChargingStations();

        //then
        assertEquals(1, result.size());
        assertEquals("Berlin", result.get(0).getChargerName());
        verify(repository).findAll();
    }

    @Test
    public void filterChargingStations_withCoordinate() {
        //given
        ChargingStationRequestDto request = new ChargingStationRequestDto();
        request.setCoordinate(new GoogleCoordinate(48.7, 9.11));
        request.setDistanceKm(2.0);

        //specification resolver
        Specification<ChargingStation> specification = ((root, query, criteriaBuilder) -> null);
        when(specificationResolver.resolveSpecification(request)).thenReturn(specification);

        ChargingStation Berlin1 = new ChargingStation("1", "Berlin1", BigDecimal.valueOf(50.0), new GoogleCoordinate(48.7, 9.11), "12345");
        ChargingStation Berlin2 = new ChargingStation("2", "Berlin2", BigDecimal.valueOf(50.0), new GoogleCoordinate(48.8, 9.11), "12345");

        when(repository.findAll(specification)).thenReturn(List.of(Berlin1, Berlin2));

        //then

        List<ChargingStationResponseDto> resultFillter = service.filterChargingStations(request);
        assertEquals(1, resultFillter.size());
        assertEquals("Berlin1", resultFillter.get(0).getChargerName());
        verify(repository).findAll(specification);


    }

}
