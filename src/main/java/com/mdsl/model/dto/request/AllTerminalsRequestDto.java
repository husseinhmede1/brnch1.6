package com.mdsl.model.dto.request;

import com.mdsl.model.dto.response.SortDTO;
import com.mdsl.utils.ResponseCode;
import lombok.Getter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Getter
public class AllTerminalsRequestDto {

    @NotEmpty
    @NotEmpty(message = ResponseCode.INVALID_ENTITY_ID)
    @Size(max = 30, message = ResponseCode.INVALID_ENTITY_ID)
    private String entityId;

    private Integer pageNo = 0;

    private Integer pageSize = 20;

    private List<SortDTO> sort;

    public List<SortDTO> getSort() {
        if (this.sort == null || this.sort.isEmpty()) {
            return new ArrayList<SortDTO>();
        }
        return this.sort;
    }

}
