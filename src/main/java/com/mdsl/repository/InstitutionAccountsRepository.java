package com.mdsl.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mdsl.model.entity.InstitutionAccounts;

@Repository
public interface InstitutionAccountsRepository extends JpaRepository<InstitutionAccounts, Integer> {
  List<InstitutionAccounts> findByInstitutionId(String paramString);
  List<InstitutionAccounts> findByInstitutionIdAndChargingInstitutionAndBankCode(String paramString, String institutionId, String bankCode);
  List<InstitutionAccounts> findByInstitutionIdAndChargingInstitution(String paramString, String institutionId);
  
  @Query( value = "SELECT DISTINCT I.ACCOUNT_TYPE AS ACCOUNT_TYPE FROM MD_INSTITUTION_ACCOUNTS I "
			+ " WHERE I.INSTITUTION_ID = :institutionId "
			+ " AND I.ACCOUNT_ORIGIN = :accountOrigin "
			+ " AND DECODE(I.CHARGING_INSTITUTION, NULL, 'B',  I.CHARGING_INSTITUTION) = NVL(:destinationInstitution, 'B') "
			+ " AND I.BANK_CODE = :bankCode", nativeQuery=true)
  List<String> findBySearchCriteria (@Param("institutionId") String institutionId, @Param("accountOrigin") String accountOrigin, @Param("destinationInstitution") String destinationInstitution,@Param("bankCode") String bankCode);
  
//  boolean existsByInstitutionIdAndAccountTypeAndCardSchemeIdAndIssuerAcqProfileAndCurrencyCodeAndAccountOriginAndChargingInstitution(String institutionId, String accountType, String cardScheme, String issuerAcqProfile, String currencyCode, String accountOrigin, String chargingInstitution);
//  boolean existsByInstitutionIdAndAccountTypeAndCardSchemeIdAndIssuerAcqProfileAndCurrencyCodeAndAccountOriginAndChargingInstitutionAndInstitutionAcctsIdNot(String institutionId, String accountType, String cardScheme, String issuerAcqProfile, String currencyCode, String accountOrigin, String chargingInstitution, Integer institutionAcctsId);
  boolean existsByIssuerAcqProfileAndInstitutionId(String issuerAcqProfile,String instId);
    boolean existsByInstitutionIdAndAccountTypeAndCardSchemeIdAndIssuerAcqProfileAndCurrencyCodeAndBankCodeAndAccountOriginAndChargingInstitution(
            String institutionId,
            String accountType,
            String cardSchemeId,
            String issuerAcqProfile,
            String currencyCode,
            String bankCode,
            String accountOrigin,
            String chargingInstitution
    );

    boolean existsByInstitutionIdAndAccountTypeAndCardSchemeIdAndIssuerAcqProfileAndCurrencyCodeAndBankCodeAndAccountOriginAndChargingInstitutionAndInstitutionAcctsIdNot(
            String institutionId,
            String accountType,
            String cardSchemeId,
            String issuerAcqProfile,
            String currencyCode,
            String bankCode,
            String accountOrigin,
            String chargingInstitution,
            Integer institutionAcctsId
    );

}