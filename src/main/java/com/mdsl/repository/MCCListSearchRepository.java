package com.mdsl.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.mdsl.model.entity.MCCList;

public interface MCCListSearchRepository {

	Page<MCCList> findByMccIgnoreCaseContainingOrDescriptionIgnoreCaseContainingOrCardSchemeTypeMapping_CardSchemeIdIgnoreCaseContainingOrCardSchemeTypeMapping_CardSchemeNameIgnoreCaseContainingOrMerchantType_CodeValueIgnoreCaseContaining(PageRequest pageRequest,
			String search2, String search3, String search4, int i);
}
