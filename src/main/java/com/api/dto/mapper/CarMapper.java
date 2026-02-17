package com.api.dto.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import com.api.dto.CarDTO;
import com.api.model.Car;

@Component
public class CarMapper {

	public List<CarDTO> carTOdto(List<Car> cars) {
		List<CarDTO> cardto = cars.stream()
				.map(c -> new CarDTO(
						c.getId(), 
						c.getModel(), 
						c.getPlateNo(), 
						c.getSeats(), 
						c.getOwner().getId()
						))
				.toList();
					
		return cardto;
	}
	
}
