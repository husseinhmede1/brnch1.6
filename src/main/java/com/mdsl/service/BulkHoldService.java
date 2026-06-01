package com.mdsl.service;

import com.mdsl.model.dto.response.FileDirectoryResponseDto;
import com.mdsl.repository.BulkHoldRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BulkHoldService {

    private final BulkHoldRepository bulkHoldRepository;

    public List<FileDirectoryResponseDto> getDistinctFileNames(String institutionId) {
        List<String> fileNames = bulkHoldRepository.getDistinctFileNames(institutionId);

        if (fileNames == null || fileNames.isEmpty()) {
            return Collections.emptyList();
        }

        return fileNames.stream()
                .filter(Objects::nonNull)
                .map(fileName -> new FileDirectoryResponseDto(fileName, fileName))
                .collect(Collectors.toList());
    }

}
