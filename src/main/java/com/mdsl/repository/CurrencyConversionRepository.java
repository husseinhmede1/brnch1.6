package com.mdsl.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.mdsl.model.entity.CurrencyConversion;
import com.mdsl.model.entity.CurrencyRate;

@Repository
public interface CurrencyConversionRepository extends JpaRepository<CurrencyConversion, Integer>{
	Optional<CurrencyConversion> findByCurrencyConversionId(int id);
	
	List<CurrencyConversion> findByInstitution_InstitutionId(String institutionId, Sort by);

	List<CurrencyConversion> findByUserCreate(String string);

//	List<CurrencyConversion> findByCurrency_CurrencyCodeEqualsIgnoreCaseAndBaseCurrency_CurrencyCodeEqualsIgnoreCaseAndInstitution_InstitutionIdEqualsIgnoreCaseAndRoundingRuleEqualsIgnoreCaseAndRateExpressionEqualsIgnoreCaseAndMidRateUsedEqualsIgnoreCase(
//			String currencyCode, String currencyCode2, String institutionId, String roundingRule, String rateExpression,
//			String midRateUsed);
//
//	List<CurrencyConversion> findByCurrency_CurrencyCodeEqualsIgnoreCaseAndBaseCurrency_CurrencyCodeEqualsIgnoreCaseAndInstitution_InstitutionIdEqualsIgnoreCaseAndRoundingRuleEqualsIgnoreCaseAndRateExpressionEqualsIgnoreCaseAndMidRateUsedEqualsIgnoreCaseAndCurrencyConversionIdNot(
//			String currencyCode, String currencyCode2, String institutionId, String roundingRule, String rateExpression,
//			String midRateUsed, int currencyConversionId);

//	List<CurrencyConversion> findByCurrency_CurrencyIdAndBaseCurrency_CurrencyIdAndInstitution_InstitutionId(
//			Integer currency, Integer baseCurrency, String instId);
//
//	List<CurrencyConversion> findByCurrency_CurrencyIdAndBaseCurrency_CurrencyIdAndInstitution_InstitutionIdAndCurrencyConversionId(
//			Integer currency, Integer baseCurrency, String instId, Integer recordSeqId);

	List<CurrencyConversion> findByCurrency_CurrencyCodeIgnoreCaseAndBaseCurrency_CurrencyCodeIgnoreCaseAndInstitution_InstitutionId(
			String currency, String baseCurrency, String instId);

//	List<CurrencyConversion> findByCurrency_CurrencyCodeIgnoreCaseAndBaseCurrency_CurrencyCodeIgnoreCaseAndInstitution_InstitutionIdAndCurrencyConversionId(
//			String currency, String baseCurrency, String instId, Integer recordSeqId);

	List<CurrencyConversion> findByCurrency_CurrencyCodeIgnoreCaseAndBaseCurrency_CurrencyCodeIgnoreCaseAndInstitution_InstitutionIdAndCurrencyConversionIdNot(
			String currency, String baseCurrency, String instId, Integer recordSeqId);

	

}