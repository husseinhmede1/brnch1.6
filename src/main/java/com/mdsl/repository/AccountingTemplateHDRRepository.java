package com.mdsl.repository;

import com.mdsl.model.entity.AccountingTemplateHDR;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public abstract interface AccountingTemplateHDRRepository extends JpaRepository<AccountingTemplateHDR, Integer> {
  List<AccountingTemplateHDR> findByInstitutionId(String paramString);

  boolean existsByInstitutionIdAndAccountTemplate(String paramString1, String paramString2);

  boolean existsByInstitutionIdAndAccountTemplateAndAcctTemplateHdrIdNot(String paramString1, String paramString2, int paramInt);
}