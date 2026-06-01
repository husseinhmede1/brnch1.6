package com.mdsl.model.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class PaginationResponseDto {

	private Boolean response;
	private String message;
	private Object data;
	private Integer totalPages;
	private Long totalRecords;
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	public Boolean getResponse() {
		return response;
	}
	public void setResponse(Boolean response) {
		this.response = response;
	}

	public PaginationResponseDto(Boolean response, Object data) {
    super();
    this.response = response;
    this.data = data;
  }
	public PaginationResponseDto(Boolean response, String message, Object data) {
    super();
    this.response = response;
    this.message = message;
    this.data = data;
  }
	public PaginationResponseDto(
      Boolean response, String message, Object data, Integer totalPages, Long totalRecords) {
    super();
    this.response = response;
    this.message = message;
    this.data = data;
    this.totalPages = totalPages;
    this.totalRecords = totalRecords;
  }

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Integer getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(Integer totalPages) {
		this.totalPages = totalPages;
	}

	public Long getTotalRecords() {
		return totalRecords;
	}

	public void setTotalRecords(Long totalRecords) {
		this.totalRecords = totalRecords;
	}
}