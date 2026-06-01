package com.mdsl.model.dto.request;

import com.mdsl.model.dto.response.SortDTO;
import com.mdsl.utils.ResponseCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ManualNonActivityFeesPackageRequestDto {

    @NotNull(message = ResponseCode.CFG_INVALID_INSTITUTION_ID)
    @Size(min = 1, max = 10, message = ResponseCode.CFG_INVALID_INSTITUTION_ID)
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = ResponseCode.CFG_INVALID_INSTITUTION_ID)
    private String institutionId;

    private String outletId;

    private String fromTransactionDate;

    private String toTransactionDate;

    private String transactionId;

    private Date transactionDate;

    private List<SortDTO> sort;

    private String sortOrder = "";

    private Integer pageNo = 0;

    private Integer pageSize = 20;
}