package com.mdsl.model.entity.keys;

import java.io.Serializable;

import javax.persistence.Embeddable;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@RequiredArgsConstructor
public class CurrencyId implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String currCode;
	private int institution;

}
