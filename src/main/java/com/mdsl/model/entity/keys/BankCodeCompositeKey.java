package com.mdsl.model.entity.keys;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class BankCodeCompositeKey implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private String bankCode;
    private String institutionId;
}