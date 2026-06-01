package com.mdsl.utils;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import com.mdsl.model.dto.response.SortDTO;
import com.mdsl.utils.enumerations.CommonSortColumnEnum;

public class PaginationCommonCode {

	public PageRequest getPageRequestForPagination(List<SortDTO> listOfSort, String defaultSortingColumn, int pageNo,int pageSize) {
		Sort sort = null;

		if (listOfSort.isEmpty()) {
			sort = Sort.by(defaultSortingColumn).ascending();
		} else {
			for (SortDTO sortDto : listOfSort) {
				if (null == sort) {
					sort = Sort.by(Direction.fromString(sortDto.getSortOrder().name()),
							CommonSortColumnEnum.valueOf(sortDto.getColumn()).getValue());
				} else {
					sort = sort.and(Sort.by(Direction.fromString(sortDto.getSortOrder().name()),
							CommonSortColumnEnum.valueOf(sortDto.getColumn()).getValue()));
				}
			}
		}

		PageRequest pageRequest = PageRequest.of(pageNo, pageSize,
				sort);
		
		return pageRequest;

	}
	public PageRequest getPageRequestForPaginationNative(List<SortDTO> listOfSort, String defaultSortingColumn, int pageNo,int pageSize) {
		Sort sort = null;

		if (listOfSort.isEmpty()) {
			sort = Sort.by(defaultSortingColumn).ascending();
		} else {
			for (SortDTO sortDto : listOfSort) {
				if (null == sort) {
					sort = Sort.by(Direction.fromString(sortDto.getSortOrder().name()),
							sortDto.getColumn());
				} else {
					sort = sort.and(Sort.by(Direction.fromString(sortDto.getSortOrder().name()),
							sortDto.getColumn()));
				}
			}
		}

        return PageRequest.of(pageNo, pageSize,
                sort);

	}

}
