package com.ccamargo.reserve.model.flight;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FlightMapper {
    FlightDTO toFlightDTO(FlightEntity flight);
    @InheritInverseConfiguration
    FlightEntity toFlight(FlightDTO flightDTO);
}
