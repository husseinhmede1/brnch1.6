package com.mdsl.model.entity.keys;

import java.io.Serializable;

import lombok.Getter;

@Getter
public class CardTypeServiceCompositeKey implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String servicePosition; 
	private String serviceValue;
	private int cardTypeId;
	private int instId; 
}