package com.mdsl.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mdsl.model.entity.Institution;
import com.mdsl.model.entity.TransactionChargeDetails;

public interface TransactionChargesDetailsRepository extends JpaRepository<TransactionChargeDetails,Integer>{
	//@Query("from TransactionChargeDetails tcd where tcd.transactionGroup = :trasactionGroupName ")
	//List<TransactionChargeDetails> findByTransactionGroupId(@Param("trasactionGroupName") String trasactionGroupName);
	
	@Query(value="SELECT * FROM MD_TRANS_ACCT_GROUP_DTL WHERE TRANS_ID_ACCT_GROUP= :trasactionGroupName AND INSTITUTION_ID= :institution", nativeQuery = true)
	List<TransactionChargeDetails> findByTransactionGroupIdAndInstitution(String trasactionGroupName,String institution);

	List<TransactionChargeDetails> findByUserCreate(String string);
}
