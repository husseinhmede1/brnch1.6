package com.mdsl.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.data.repository.query.FluentQuery.FetchableFluentQuery;
import org.springframework.stereotype.Repository;

import com.mdsl.model.entity.CardScheme;
import com.mdsl.model.entity.MCCList;
import com.mdsl.model.entity.MerchantType;
import com.mdsl.model.entity.SystemCode;

@Repository
public class MCCListRepositoryImpl implements MCCListSearchRepository {

	@Autowired
	EntityManager em;

	@Override
	public Page<MCCList> findByMccIgnoreCaseContainingOrDescriptionIgnoreCaseContainingOrCardSchemeTypeMapping_CardSchemeIdIgnoreCaseContainingOrCardSchemeTypeMapping_CardSchemeNameIgnoreCaseContainingOrMerchantType_CodeValueIgnoreCaseContaining(
			PageRequest pageRequest, String mcc, String description, String cardSchemeId, int merchantType) {
		// TODO Auto-generated method stub
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<MCCList> cq = cb.createQuery(MCCList.class);

		Root<MCCList> book = cq.from(MCCList.class);
		List<Predicate> predicates = new ArrayList<>();
		Join<MCCList, SystemCode> merchantType1 = book.join("merchantType");
		Join<MCCList, CardScheme> cardSchemeId1 = book.join("cardSchemeTypeMapping");

		if ((!(mcc.equals(""))) && (!(mcc.equals(null)))) {
			predicates.add(cb.like(book.get("mcc"), "%" + mcc + "%"));
		}
		if (merchantType != 0) {

//		    	cq.where(cb.equal(customer .get("id"), 1));

			predicates.add(cb.equal(merchantType1.get("systemCodeId"), merchantType));

//		        predicates.add(cb.like(book.get("title"), "%" + title + "%"));
			// predicates.add(cb.equal(book.get("merchantTypeMapping"),
			// "'"+merchantType+"'"));
		}
		if ((!(description.equals(""))) && (!(description.equals(null)))) {
			predicates.add(cb.like(book.get("description"), "%" + description + "%"));
//		    	predicates.add(cb.equal(book.get("merchantTypeMapping"), description));
		}
		if ((!(cardSchemeId.equals(""))) && (!(cardSchemeId.equals(null)))) {

//		    	cq.where(cb.equal(customer .get("id"), 1));

			predicates.add(cb.equal(cardSchemeId1.get("cardSchemeId"), cardSchemeId));
		}

		cq.where(predicates.toArray(new Predicate[0]));
		
		cq.orderBy(QueryUtils.toOrders(pageRequest.getSort(), book, cb));
		//cq.orderBy(s2.getProperty(),s2.getDirection());
		List<MCCList> result12 = em.createQuery(cq).getResultList();
		System.out.println(result12.size());
		// This query fetches the Books as per the Page Limit
		List<MCCList> result = em.createQuery(cq).setFirstResult((int) pageRequest.getOffset())
				.setMaxResults(pageRequest.getPageSize()).getResultList();

		Long count = 0L;
		CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
		book = countQuery.from(MCCList.class);

		countQuery.groupBy(book);
		countQuery.select(cb.count(book)).where(cb.and(predicates.toArray(new Predicate[predicates.size()])));

//		countQuery.select(cb.count(merchantType1)).where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
//		countQuery.select(cb.count(cardSchemeId1)).where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
		// Fetches the count of all Books as per given criteria

		Page<MCCList> result1 = new PageImpl<>(result, pageRequest, result12.size());

		return result1;
	}

}
