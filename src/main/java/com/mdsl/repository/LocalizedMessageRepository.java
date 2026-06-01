package com.mdsl.repository;

import com.mdsl.model.entity.LocalizedMsgs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LocalizedMessageRepository extends JpaRepository<LocalizedMsgs, Integer>
{
    Optional<LocalizedMsgs> findByLanguageCodeAndMessageKey(String languageCode, String messageKey);
}