package com.mdsl.service;

import java.util.List;
import java.util.Optional;

import com.mdsl.exceptionHandling.BusinessException;
import com.mdsl.utils.ResponseCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.mdsl.model.entity.ChargeMethod;
import com.mdsl.repository.ChargeMethodRepository;

@Service
public class ChargeMethodService {
	@Autowired
	private ChargeMethodRepository chargeMethodRepository;

	public List<ChargeMethod> getAllChargeMethod() {
		List<ChargeMethod> chargeMethods = chargeMethodRepository
				.findAll(Sort.by(Sort.Direction.ASC, "chargeMethodId"));

		return Optional.ofNullable(chargeMethods)
				.filter(list -> !list.isEmpty())
				.orElseThrow(() ->
						new BusinessException(ResponseCode.CFG_CHARGE_METHOD_NOT_FOUND, HttpStatus.NOT_FOUND)
				);
	}

	public ChargeMethod saveChargeMethod(ChargeMethod chargeMethod) {
		if (chargeMethod == null) {
			throw new BusinessException(ResponseCode.VAL_ERROR_OCCURRED, HttpStatus.BAD_REQUEST);
		}
		try {
			ChargeMethod saved = chargeMethodRepository.save(chargeMethod);
			return saved;
		} catch (Exception ex) {
			throw new BusinessException(ResponseCode.VAL_ERROR_OCCURRED, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
