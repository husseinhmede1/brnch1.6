package com.mdsl.model.mapper;

import org.mapstruct.Mapper;

import com.mdsl.model.dto.request.ProvinceResquestDto;
import com.mdsl.model.dto.response.ProvinceResponseDto;
import com.mdsl.model.entity.Province;

@Mapper
public interface ProvinceMapper {
	ProvinceResponseDto toDto (Province province); 
	Province toEntity(ProvinceResquestDto provinceResquestDto);
}
