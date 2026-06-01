package com.mdsl.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.mdsl.model.entity.Contact;
import com.mdsl.model.entity.Institution;

@Repository
@Transactional(rollbackOn=Exception.class)
public interface ContactRepository extends JpaRepository<Contact, Integer>{

	@Modifying
	@Query("UPDATE MD_ACQ_ENTITY_CONTACTS SET contactStatus = :contactStatus"
			+ " WHERE contactId = :contactId")
	void UpdateStatus(int contactId, char contactStatus);

	List<Contact> findByContactStatus(char value, Sort sort);

	List<Contact> findContactsByEntityAndInstitution(String entity,Institution institution, Sort by);

	List<Contact> findByEntityObject_EntityId(String entityId);

	List<Contact> findByUserCreate(String string);

}
