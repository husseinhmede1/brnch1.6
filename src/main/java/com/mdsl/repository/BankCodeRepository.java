package com.mdsl.repository;

import com.mdsl.model.entity.BankCode;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public abstract interface BankCodeRepository extends JpaRepository<BankCode, Integer>
{
  @Query("from MD_BANK_INFO where INSTITUTION_ID = :instId")
  public abstract List<BankCode> findBankCodeByInstitutionId(@Param("instId") String instId, Sort paramSort);

  public abstract Optional<BankCode> findByBankCode(String paramString);
  
  public abstract Optional<BankCode> findByBankCodeAndInstitutionId(@Param("bankCode") String bankCode, @Param("institutionId")String institutionId);
  
  boolean existsByBankCode(String bankCode);
  boolean existsByBankCodeAndBankCodeIdNot(String bankCode, int bankCodeId);
  boolean existsByBankCodeAndInstitutionIdAndBankCodeIdNot(String bankCode,String institutionId, int bankCodeId);
  boolean existsByBankCodeAndInstitutionId(String bankCode,String institutionId);

}
