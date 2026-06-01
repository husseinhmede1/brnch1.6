package com.mdsl.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.mdsl.exceptionHandling.BusinessException;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.mdsl.model.dto.response.FileElementResponseDto;
import com.mdsl.model.dto.response.FileResponseDto;
import com.mdsl.model.dto.response.ListFileElementResponseDto;
import com.mdsl.model.entity.File;
import com.mdsl.model.entity.FileElement;
import com.mdsl.model.mapper.FileElementMapper;
import com.mdsl.model.mapper.FileMapper;
import com.mdsl.repository.FileElementRepository;
import com.mdsl.repository.FileRepository;
import com.mdsl.utils.ResponseCode;
import com.mdsl.utils.Validations;
import com.mdsl.utils.enumerations.StatusEnum;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FileService {
	
	private final FileRepository fileRepository;
	private final FileElementRepository elementRepository;
	
	private final FileMapper fileMapper;
	private final FileElementMapper elementMapper;

	/*
	 * Returns the list of all files from table MD_CFG_FILES
	 */
	@Cacheable(key="#root.methodName", cacheResolver="cacheResolver")
	public List<FileResponseDto> getAllFiles(){
		List<File> files = fileRepository.findAll();
		List<FileResponseDto> allFiles= new ArrayList<FileResponseDto>();
		files.forEach((file) -> {
			allFiles.add(convertFileToFileResponseDto(file)); 
	    });
		Validations.isEmpty(allFiles);
		return allFiles;
	}
	
	/*
	 * Returns the list of all active files (status = 1) from table MD_CFG_FILES 
	 */
	@Cacheable(key="#root.methodName", cacheResolver="cacheResolver")
	public List<FileResponseDto> getActiveFiles(){
		List<FileResponseDto> allActiveFiles= new ArrayList<FileResponseDto>(); 
		List<File> activeFiles = fileRepository.findByStatus(String.valueOf(StatusEnum.ENABLED.getValue()));
		activeFiles.forEach((file) -> {
			allActiveFiles.add(convertFileToFileResponseDto(file)); 
	    });
		Validations.isEmpty(allActiveFiles);
		return allActiveFiles;
	}
	
	/*
	 * Return the elements from table MD_CFG_FILE_ELEMENTS based on the file id
	 */
	@Cacheable(key = "#fileId", cacheResolver = "cacheResolver")
	public ListFileElementResponseDto getAllElementsByFileId(int fileId) {
		File file = fileRepository.findById(fileId).orElseThrow(()-> new BusinessException(ResponseCode.CFG_INVALID_FILE, HttpStatus.NOT_FOUND));

		List<FileElementResponseDto> allElements = new ArrayList<FileElementResponseDto>();
		List<FileElement> elements = elementRepository.findByFileId(fileId);
		elements.forEach((element) -> {
			allElements.add(convertElementToElementsResponseDto(element, file.getFileName()));
		});
		Validations.isEmpty(allElements);
		ListFileElementResponseDto listFileElementResponseDto = new ListFileElementResponseDto();
		listFileElementResponseDto.setFileElementResponseDto(allElements);
		listFileElementResponseDto.setFileId(file.getFileId());
		listFileElementResponseDto.setFileName(file.getFileName());
		return listFileElementResponseDto;
	}

	/*
	 * Converts Element object to ElementsResponseDto
	 */
	private FileElementResponseDto convertElementToElementsResponseDto(FileElement element, String fileName) {
		FileElementResponseDto elementsResponseDto=elementMapper.toDto(element, fileName);
		elementsResponseDto.setValidationFormat(Objects.nonNull(elementsResponseDto.getValidationFormat())?elementsResponseDto.getValidationFormat().trim():null);
		return elementsResponseDto;
	}
	
	/*
	 * Converts File object to FileResponseDto
	 */
	private FileResponseDto convertFileToFileResponseDto (File file) {
		FileResponseDto fileResponseDto= fileMapper.toDto(file);
		fileResponseDto.setFileName(fileResponseDto.getFileName().trim());
		fileResponseDto.setFileTypeId(file.getFileType().getFileTypeId());
		fileResponseDto.setFileTypeCode(file.getFileType().getFileTypeCode());
		return fileResponseDto;
	}
}