package com.mdsl.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.mdsl.exceptionHandling.BusinessException;
import com.mdsl.model.entity.ChargeMethod;
import com.mdsl.repository.ChargeMethodRepository;
import com.mdsl.utils.MakerCheckerEngine;
import com.mdsl.utils.ResponseCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChargeMethodService {
	@Autowired
	private ChargeMethodRepository chargeMethodRepository;
    private final MakerCheckerEngine makerCheckerEngine;

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
			if (makerCheckerEngine.processIfRequired(chargeMethod, ChargeMethodService.class.getName(), "saveChargeMethod", "")) {
				return null;
			}
			ChargeMethod saved = chargeMethodRepository.save(chargeMethod);
			return saved;
		} catch (Exception ex) {
			throw new BusinessException(ResponseCode.VAL_ERROR_OCCURRED, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
