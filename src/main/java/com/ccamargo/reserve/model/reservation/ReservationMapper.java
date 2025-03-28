package com.ccamargo.reserve.model.reservation;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReservationMapper {
    @Mappings({
            @Mapping(source = "flight.id", target = "flightId")
    })
    ReservationDTO toReservationDTO(ReservationEntity reservation);

    @InheritInverseConfiguration
    ReservationEntity toReservation(ReservationDTO reservationDTO);

    List<ReservationDTO> toListReservationDTO(List<ReservationEntity> reservationEntity);
}

