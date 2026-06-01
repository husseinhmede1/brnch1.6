package com.mdsl.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.mdsl.model.entity.Currency;

@Repository
public interface CurrencyRepository extends JpaRepository<Currency, Integer>{
	Optional<Currency> findByCurrencyCode(String code);

	List<Currency> findByStatus(char value, Sort sort);

	Currency findByCurrencyCodeEqualsIgnoreCase(String currencyCode);

	List<Currency> findByCurrencyCodeEqualsIgnoreCaseOrCurrencyNameEqualsIgnoreCase(String currencyCode, String currencyName);
	
	@Query("from MD_CURRENCY_TABLE c where (upper(c.currencyCode))=upper(:currencyCode)")
	List<Currency> findByCurrencyCodeIgnoreCase(String currencyCode);
	
	List<Currency> findByCurrencyNameEqualsIgnoreCase(String currencyName);

	List<Currency> findByCurrencyCodeEqualsIgnoreCaseOrCurrencyNameEqualsIgnoreCaseAndCurrencyId(String currencyCode, String currencyName,int currencyId);

	List<Currency> findByCurrencyCodeEqualsIgnoreCaseAndCurrencyNameEqualsIgnoreCaseAndCurrencyId(String currencyCode,
			String currencyName, int currencyId);

	List<Currency> findByUserCreate(String string);

//	@Query("from MD_CURRENCY_TABLE c where (upper(c.currencyCode)=upper(:currencyCode) or upper(c.currencyName)=upper(:currencyName)) and c.currencyId!=:currencyId")
//	List<Currency> findByCurrencyCodeEqualsIgnoreCaseOrCurrencyNameEqualsIgnoreCaseAndCurrencyIdNot(String currencyCode,
//			String currencyName, int currencyId);
	
	@Query("from MD_CURRENCY_TABLE c where (upper(c.currencyCode)=upper(:currencyCode)) and c.currencyId!=:currencyId")
	List<Currency> findByCurrencyCodeEqualsIgnoreCaseAndCurrencyIdNot(String currencyCode, int currencyId);
	
	@Query("from MD_CURRENCY_TABLE c where (upper(c.currencyName)=upper(:currencyName)) and c.currencyId!=:currencyId")
	List<Currency> findByCurrencyNameEqualsIgnoreCaseAndCurrencyIdNot(String currencyName, int currencyId);
	
	
}
