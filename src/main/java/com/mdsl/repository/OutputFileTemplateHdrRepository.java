package com.mdsl.repository;

import com.mdsl.model.entity.OutputFileTemplateHdr;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public abstract interface OutputFileTemplateHdrRepository extends JpaRepository<OutputFileTemplateHdr, Integer>
{
  public abstract List<OutputFileTemplateHdr> findByInstitutionId(String institutionId);
  public abstract boolean existsByInstitutionIdAndOutputFileTypeAndOutputFileTypeAbbr(String institutionId, String outputFileType, String outputFileTypeAbbr);
  public abstract boolean existsByInstitutionIdAndOutputFileTypeAndOutputFileTypeAbbrAndOutputTemplateHdrIdNot(String institutionId, String outputFileType, String outputFileTypeAbbr, int outputTemplateHdrId);
}