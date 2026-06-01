package com.mdsl.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mdsl.model.entity.PaymentAccount;

@Repository
public interface PaymentAccountRepository extends JpaRepository<PaymentAccount, Integer> {
	
	@Query("SELECT pa FROM PaymentAccount pa WHERE pa.institution.institutionId = :instId")
	List<PaymentAccount> findPaymentAccountByInstitutionId(String instId, Sort sort);

//	@Query(value="SELECT * FROM MD_ACQ_ENTITY_PAYMENT_ACCOUNTS WHERE ENTITY_ID = :entitiesId",nativeQuery=true)
//	List<PaymentAccount> findPaymentAccountsByEntityId(String entitiesId);

	@Query("SELECT pa FROM PaymentAccount pa WHERE pa.entityObject.entityId = :entitiesId")
	List<PaymentAccount> findPaymentAccountsByEntityId(String entitiesId);
	
	List<PaymentAccount> findByEntityObject_EntityId(String entityId);

	List<PaymentAccount> findByCreatedBy(String string);

}
