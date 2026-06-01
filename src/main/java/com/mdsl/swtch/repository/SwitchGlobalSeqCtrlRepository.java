package com.mdsl.swtch.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mdsl.swtch.model.entity.SwitchGlobalSeqCtrl;

@Repository
public interface SwitchGlobalSeqCtrlRepository extends JpaRepository<SwitchGlobalSeqCtrl, String>{
	Optional<SwitchGlobalSeqCtrl> findBySeqName(String seqName);
}
