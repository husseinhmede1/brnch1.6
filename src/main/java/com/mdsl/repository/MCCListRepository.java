package com.mdsl.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.mdsl.model.entity.MCCList;

public interface MCCListRepository extends JpaRepository<MCCList, Integer> {

	Page<MCCList> findAll(Pageable pageable);

	Page<MCCList> findByMccIgnoreCaseContainingOrDescriptionIgnoreCaseContainingOrCardSchemeTypeMapping_CardSchemeIdIgnoreCaseContainingOrCardSchemeTypeMapping_CardSchemeNameIgnoreCaseContainingOrMerchantType_CodeValueIgnoreCaseContaining(
			PageRequest pageRequest, String search, String search2, String search3, String search4, String search5);

	List<MCCList> findByMcc(String defaultMCC);

	List<MCCList> findByCreatedBy(String string);

	List<MCCList> findByCardSchemeTypeMapping_CardSchemeIdEqualsIgnoreCaseAndMccEqualsIgnoreCaseAndMerchantType_SystemCodeIdAndDescriptionEqualsIgnoreCase(
			String cardSchemeId, String mcc, int merchantTypeId, String description);

	List<MCCList> findByCardSchemeTypeMapping_CardSchemeIdEqualsIgnoreCaseAndMccEqualsIgnoreCaseAndMerchantType_SystemCodeIdAndDescriptionEqualsIgnoreCaseAndMccIdNot(
			String cardSchemeId, String mcc, int merchantTypeId, String description, int mccId);

	List<MCCList> findByMccEqualsIgnoreCaseAndCardSchemeTypeMapping_CardSchemeIdEqualsIgnoreCase(String mcc,String cardSchemeId);
}
