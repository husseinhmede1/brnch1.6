package com.mdsl.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mdsl.model.entity.AccountingTemplateDetails;

@Repository
public abstract interface AccountingTemplateDetailsRepository extends JpaRepository<AccountingTemplateDetails, Integer> {
  List<AccountingTemplateDetails> findByAcctTemplateHdrSubId(int paramInt, Sort sort);
  List<AccountingTemplateDetails> findByAcctTemplateHdrSubIdAndInstitutionId(int paramInt,String instId, Sort sort);

  @Transactional
  void deleteByAcctTemplateHdrSubId (int acctTemplateHdrSubId); 
}