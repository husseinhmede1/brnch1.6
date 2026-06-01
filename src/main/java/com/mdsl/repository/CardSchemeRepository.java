package com.mdsl.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.mdsl.model.entity.CardScheme;

@Repository
public interface CardSchemeRepository extends JpaRepository<CardScheme, String> {
	
	@Modifying
	@Query("UPDATE MD_CARD_SCHEME SET status = :toggle"
			+ " WHERE CARD_SCHEME_ID = :code")
	void changeStatus(String code, boolean toggle);

	List<CardScheme> findByStatus(char value, Sort by);

	Optional<CardScheme> findByCardSchemeId(String cardSchemeId);

	List<CardScheme> findByCreatedBy(String string);
	
	Optional<CardScheme> findByRecordSequenceNumber(int recordSequenceNumber);
	
	boolean existsByCardSchemeId(String cardSchemeId);

	boolean existsByCardSchemeIdAndRecordSequenceNumber(String cardSchemeId,int recordSequenceNumber);

	boolean existsByCardSchemeIdAndRecordSequenceNumberNot(String cardSchemeId, int recordSequenceNumber);

//	@Transactional
//	@Query(value = "SELECT MD_CARD_SCHEME_SEQ.nextval from DUAL", nativeQuery = true)
//	int findNextRecordSequence();
//
//	@Modifying
//	@Transactional
//	@Query(value = "ALTER SEQUENCE MD_CARD_SCHEME_SEQ INCREMENT BY 1", nativeQuery = true)
//	int updateRecordSequence();
}
