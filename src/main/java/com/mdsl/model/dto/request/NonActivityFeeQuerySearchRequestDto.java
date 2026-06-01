package com.mdsl.model.dto.request;

import com.mdsl.model.dto.response.SortDTO;
import com.mdsl.utils.ResponseCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NonActivityFeeQuerySearchRequestDto {

    private String institutionId;

    private int nonActivityFeeQueryId;

    private String fromProcessingDate;

    private String toProcessingDate;

    private List<SortDTO> sort;

    private String sortOrder = "";

    private Integer pageNo = 0;

    private Integer pageSize = 20;
}

