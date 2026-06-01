package com.mdsl.service;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

import com.mdsl.exceptionHandling.BusinessException;
import com.mdsl.model.dto.response.*;
import com.mdsl.model.mapper.FileMapper;
import com.mdsl.model.mapper.LayoutDetailsMapper;
import com.mdsl.model.mapper.LayoutMapper;
import com.mdsl.repository.*;
import com.mdsl.utils.enumerations.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import com.mdsl.model.entity.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.mdsl.model.dto.request.ChangeStatusRequestDto;
import com.mdsl.model.dto.request.LayoutRequestDto;
import com.mdsl.model.dto.request.PaginationRequestDto;
import com.mdsl.utils.ResponseCode;
import com.mdsl.utils.Validations;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LayoutService {
    private final LayoutRepository layoutRepository;
    private final LayoutDetailsRepository layoutDetailsRepository;
    private final FileRepository fileRepository;
    private final FileElementRepository fileElementRepository;
    private final FileTypeRepository fileTypeRepository;
    private final LayoutMapper layoutMapper;
    private final LayoutDetailsMapper layoutDetailsMapper;
    private final FileMapper fileMapper;
    private final JobTaskRepository jobTaskRepository;

    /*
     * Returns the list of all Layouts from table MD_CFG_LAYOUT and their
     * LayoutDetails from table MD_CFG_LAYOUT_DETAILS
     */
    public PageableLayoutResponseDto getAllLayouts(PaginationRequestDto paginationRequestDto, int instId) {
        List<LayoutResponseDto> listLayoutResponse = new ArrayList<LayoutResponseDto>();

        Pageable pageable = PageRequest.of(paginationRequestDto.getOffset(), paginationRequestDto.getPageSize(), paginationRequestDto.getAsc().trim().equalsIgnoreCase("true") ? Sort.by(paginationRequestDto.getSortBy()).ascending() : Sort.by(paginationRequestDto.getSortBy()).descending());

        Page<Layout> layouts = layoutRepository.findByInstId(instId, pageable);
        layouts.stream()
                .forEach((layout) -> {
                    List<LayoutDetails> listLayoutDetails = layoutDetailsRepository.findByLayoutId(layout.getLayoutId());
                    listLayoutResponse.add(convertLayoutToLayoutResponseDto(layout, listLayoutDetails));
                });
        Validations.isEmpty(listLayoutResponse);

        PageableLayoutResponseDto pageableLayoutResponseDto = new PageableLayoutResponseDto();
        pageableLayoutResponseDto.setPaginationResponseDto(getPaginationResponseDto(paginationRequestDto, layouts));
        pageableLayoutResponseDto.setListLayoutResponseDto(listLayoutResponse);
        return pageableLayoutResponseDto;
    }

    /*
     * Returns the list of all active layouts (status = 1) from table MD_CFG_LAYOUT
     * and their details from MD_CFG_LAYOUT_DETAILS
     */
    public List<LayoutResponseDto> getActiveLayouts(int instId) {
        List<LayoutResponseDto> listLayoutResponse = new ArrayList<LayoutResponseDto>();

        List<Layout> layouts = layoutRepository.findByInstIdAndStatus(instId, String.valueOf(StatusEnum.ENABLED.getValue()));
        layouts.forEach((layout) -> {
            List<LayoutDetails> listLayoutDetails = layoutDetailsRepository.findByLayoutId(layout.getLayoutId());
            listLayoutResponse.add(convertLayoutToLayoutResponseDto(layout, listLayoutDetails));
        });
        Validations.isEmpty(listLayoutResponse);
        return listLayoutResponse;
    }

    /*
     * Returns a specific layout from table MD_CFG_LAYOUT and its details from table
     * MD_CFG_LAYOUT_DETAILS
     */
    public LayoutResponseDto getLayoutById(int layoutId, int instId) {
        Layout layout = layoutRepository.findByInstIdAndLayoutId(instId, layoutId).orElseThrow(() -> new BusinessException(ResponseCode.CFG_INVALID_LAYOUT_ID, HttpStatus.NOT_FOUND));
        List<LayoutDetails> listLayoutDetails = layoutDetailsRepository.findByLayoutId(layoutId);
        return convertLayoutToLayoutResponseDto(layout, listLayoutDetails);
    }

    /*
     * Returns a list of layouts from table MD_CFG_LAYOUT and its details from table
     * MD_CFG_LAYOUT_DETAILS based on the sent file Id
     */
    public List<LayoutResponseDto> getLayoutsByFileId(int fileId, int instId) {
        File file = fileRepository.findById(fileId).orElseThrow(() -> new BusinessException(ResponseCode.CFG_INVALID_FILE, HttpStatus.NOT_FOUND));
        List<LayoutResponseDto> listLayoutResponse = new ArrayList<LayoutResponseDto>();

        List<Layout> layouts = layoutRepository.findByInstIdAndFileId(instId, file.getFileId());
        layouts.forEach((layout) -> {
            List<LayoutDetails> listLayoutDetails = layoutDetailsRepository.findByLayoutId(layout.getLayoutId());
            listLayoutResponse.add(convertLayoutToLayoutResponseDto(layout, listLayoutDetails));
        });
        Validations.isEmpty(listLayoutResponse);
        return listLayoutResponse;
    }

    /*
     * Returns a list of layouts from table MD_CFG_LAYOUT and its details from table
     * MD_CFG_LAYOUT_DETAILS based on the sent file type code
     */
    public List<LayoutResponseDto> getLayoutsByFileType(String fileTypeCode, int instId) {

        FileType fileType = fileTypeRepository.findByFileTypeCode(fileTypeCode.trim()).orElseThrow(() -> new BusinessException(ResponseCode.CFG_INVALID_FILE_TYPE, HttpStatus.BAD_REQUEST));

        List<Layout> layouts = layoutRepository.findByInstIdAndFileType(instId, fileType.getFileTypeCode());
        if (layouts.isEmpty()) {
            Validations.isEmpty(layouts);
            return List.of();
        }
        List<Integer> layoutIds = layouts.stream()
                .map(Layout::getLayoutId)
                .collect(Collectors.toList());

        List<LayoutDetails> allDetails =
                layoutDetailsRepository.findByLayoutIdIn(layoutIds);

        Map<Integer, List<LayoutDetails>> detailsByLayoutId =
                allDetails.stream()
                        .collect(Collectors.groupingBy(LayoutDetails::getLayoutId));

        List<LayoutResponseDto> listLayoutResponse = new ArrayList<>(layouts.size());

        for (Layout layout : layouts) {
            List<LayoutDetails> details =
                    detailsByLayoutId.getOrDefault(layout.getLayoutId(), List.of());

            listLayoutResponse.add(convertLayoutToLayoutResponseDto(layout, details));
        }
        Validations.isEmpty(listLayoutResponse);
        return listLayoutResponse;
    }

    /*
     * Saves or creates a layout If layout id is not available or is equal to 0, we
     * create If layout id is available we update The transactions are logged in
     * table MD_ADT_BKD_LOG
     */
    @Transactional
    public LayoutResponseDto saveLayout(LayoutRequestDto layoutRequestDto, String remoteAddress, int instId) {

        String action = "";
        String description = "";
        Layout saveLayout;
        List<Integer> elementsOrder = new ArrayList<Integer>();
        List<LayoutDetails> listLayoutDetails = new ArrayList<>();

		UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();

        //Validate that list of details is not null or empty
        if (Objects.isNull(layoutRequestDto.getListLayoutDetailsRequest()) || layoutRequestDto.getListLayoutDetailsRequest().isEmpty()) {
            throw new BusinessException(ResponseCode.CFG_LAYOUT_DETAILS_CANNOT_BE_EMPTY, HttpStatus.BAD_REQUEST);
        }

        //Validate that the layout description is not empty
        if (Objects.isNull(layoutRequestDto.getLayoutName()) || layoutRequestDto.getLayoutName().trim().equals("")) {
            throw new BusinessException(ResponseCode.CFG_INVALID_LAYOUT_NAME, HttpStatus.BAD_REQUEST);
        }

        //Validate file request
        File file = fileRepository.findById(layoutRequestDto.getFileId()).orElseThrow(() -> new BusinessException(ResponseCode.CFG_INVALID_FILE, HttpStatus.NOT_FOUND));

        if (Objects.isNull(layoutRequestDto.getLayoutId()) || layoutRequestDto.getLayoutId() == 0) { //create layout

            //Validate that layout name is unique by institution
            if (layoutRepository.existsByInstIdAndLayoutNameIgnoreCase(instId, layoutRequestDto.getLayoutName().trim())) {
                throw new BusinessException(ResponseCode.CFG_LAYOUT_NAME_ALREADY_EXISTS, HttpStatus.CONFLICT);
            }

            //In case of input files, separator cannot be null or empty
            if (file.getFileType().getFileTypeCode().equalsIgnoreCase(FileTypeEnum.INPUT.getValue()) && (Objects.isNull(layoutRequestDto.getLayoutSeparator()) || layoutRequestDto.getLayoutSeparator().trim().equalsIgnoreCase(""))) {
                throw new BusinessException(ResponseCode.CFG_INVALID_LAYOUT_SEPARATOR, HttpStatus.CONFLICT);
            }

            saveLayout = layoutMapper.toEntity(layoutRequestDto);
            saveLayout.setCreatedBy(userDetails.getId());
            saveLayout.setCreatedDate(new Timestamp(System.currentTimeMillis()));
            saveLayout.setInstId(instId);
            saveLayout = layoutRepository.save(saveLayout);

            action = "create";
            description = "Created Input Output Layout : " + layoutRequestDto.getLayoutName();

        } else {// update layout
            Layout layout = layoutRepository.findByInstIdAndLayoutId(instId, layoutRequestDto.getLayoutId()).orElseThrow(() -> new BusinessException(ResponseCode.CFG_INVALID_LAYOUT_ID, HttpStatus.NOT_FOUND));

            //Validate that layout name is unique by institution
            List<Integer> layoutIds = new ArrayList<Integer>();
            layoutIds.add(layout.getLayoutId());
            if (layoutRepository.existsByInstIdAndLayoutNameIgnoreCaseAndLayoutIdNotIn(instId, layoutRequestDto.getLayoutName().trim(), layoutIds)) {
                throw new BusinessException(ResponseCode.CFG_LAYOUT_NAME_ALREADY_EXISTS, HttpStatus.CONFLICT);
            }

            //In case of input files, separator cannot be null or empty
            if (file.getFileType().getFileTypeCode().equalsIgnoreCase(FileTypeEnum.INPUT.getValue()) && (Objects.isNull(layoutRequestDto.getLayoutSeparator()) || layoutRequestDto.getLayoutSeparator().trim().equalsIgnoreCase(""))) {
                throw new BusinessException(ResponseCode.CFG_INVALID_LAYOUT_SEPARATOR, HttpStatus.CONFLICT);
            }

            saveLayout = layoutMapper.toEntity(layoutRequestDto);
            saveLayout.setInstId(instId);
            saveLayout.setLayoutName(layoutRequestDto.getLayoutName());
            saveLayout.setCreatedBy(layout.getCreatedBy());
            saveLayout.setCreatedDate(layout.getCreatedDate());
            saveLayout.setUpdatedBy(userDetails.getId());
            saveLayout.setUpdatedDate(new Timestamp(System.currentTimeMillis()));
            saveLayout = layoutRepository.save(saveLayout);

            action = "update";
            description = "Updated Input Output Layout : " + layoutRequestDto.getLayoutName();
        }

        //Save layout Details

        //Validate details order
        layoutRequestDto.getListLayoutDetailsRequest().forEach(layoutDetails -> {
            elementsOrder.add(layoutDetails.getElementOrder());
        });
        Collections.sort(elementsOrder);

        if (!Validations.areAllUnique(elementsOrder)) {
            throw new BusinessException(ResponseCode.CFG_INVALID_ELEMENTS_ORDER, HttpStatus.BAD_REQUEST);
        }

        if (!Validations.allPositionsExist(elementsOrder)) {
            throw new BusinessException(ResponseCode.CFG_INVALID_ELEMENTS_ORDER, HttpStatus.BAD_REQUEST);
        }

        int layoutId = Objects.nonNull(saveLayout.getLayoutId()) ? saveLayout.getLayoutId() : 0;

        List<FileElement> fullMandatoryElements =
                fileElementRepository.findAllByFileIdAndIsMandatory(
                        layoutRequestDto.getFileId(),
                        String.valueOf(StatusEnum.ENABLED.getValue())
                );
        Set<String> mandatoryElementNames = fullMandatoryElements.stream()
                .map(FileElement::getElementName)
                .collect(Collectors.toSet());

        Set<String> satisfiedElements = new HashSet<>();

        layoutRequestDto.getListLayoutDetailsRequest().forEach(layoutDetailsRequest -> {

            LayoutDetails saveLayoutDetails;

            FileElement element = fileElementRepository
                    .findByElementIdAndFileId(
                            layoutDetailsRequest.getElementId(),
                            layoutRequestDto.getFileId()
                    )
                    .orElseThrow(() -> new BusinessException(
                            ResponseCode.CFG_INVALID_ELEMENT_ID,
                            HttpStatus.NOT_FOUND
                    ));

            String elementName = element.getElementName();

            if (file.getFileType().getFileTypeCode().equalsIgnoreCase(FileTypeEnum.INPUT.getValue())) {
                if ((Objects.nonNull(layoutDetailsRequest.getPaddingType()) && !layoutDetailsRequest.getPaddingType().trim().isEmpty()) ||
                        (Objects.nonNull(layoutDetailsRequest.getPaddingValue()) && !layoutDetailsRequest.getPaddingValue().trim().isEmpty()) ||
                        (Objects.nonNull(layoutDetailsRequest.getElementLength()) && layoutDetailsRequest.getElementLength() != 0)) {

                    throw new BusinessException(ResponseCode.VAL_NO_PADDING_ON_INPUT, HttpStatus.BAD_REQUEST);
                }
            } else {
                if (Objects.nonNull(layoutDetailsRequest.getPaddingType()) && !layoutDetailsRequest.getPaddingType().isEmpty()) {

                    if (Objects.isNull(layoutDetailsRequest.getPaddingValue()) || layoutDetailsRequest.getPaddingValue().isEmpty()) {
                        throw new BusinessException(ResponseCode.CFG_INVALID_ELEMENT_PADDING_VALUE, HttpStatus.BAD_REQUEST);
                    }

                    if (!elementName.contains("DATE") &&
                            !elementName.equalsIgnoreCase(FileLayoutElementEnum.FILLER.getCode()) &&
                            layoutDetailsRequest.getPaddingValue().length() > 1) {

                        throw new BusinessException(ResponseCode.CFG_INVALID_ELEMENT_PADDING_VALUE, HttpStatus.BAD_REQUEST);
                    }
                }
            }

            if (Objects.isNull(layoutDetailsRequest.getElementParentId()) ||
                    layoutDetailsRequest.getElementParentId() == 0) {
                layoutDetailsRequest.setElementParentId(null);
            }

            if (layoutDetailsRequest.getDetailsFlag().equalsIgnoreCase(ActionFlagEnum.ADD.getValue()) ||
                    layoutDetailsRequest.getDetailsFlag().equalsIgnoreCase(ActionFlagEnum.UPDATE.getValue())) {

                if (!element.getElementSection().contains(layoutDetailsRequest.getElementSection())) {
                    throw new BusinessException(ResponseCode.CFG_INVALID_ELEMENT_SECTION, HttpStatus.BAD_REQUEST);
                }

                if (mandatoryElementNames.contains(elementName)) {
                    satisfiedElements.add(elementName);
                }

                if (layoutDetailsRequest.getDetailsFlag().equalsIgnoreCase(ActionFlagEnum.ADD.getValue())) {

                    saveLayoutDetails = layoutDetailsMapper.toEntity(layoutDetailsRequest);
                    saveLayoutDetails.setLayoutId(layoutId);
                    saveLayoutDetails.setElemetSection(layoutDetailsRequest.getElementSection());
                    saveLayoutDetails.setCreatedBy(userDetails.getId());
                    saveLayoutDetails.setCreatedDate(new Timestamp(System.currentTimeMillis()));

                } else {
                    LayoutDetails existing = layoutDetailsRepository
                            .findByLayoutIdAndDetailsId(
                                    layoutRequestDto.getLayoutId(),
                                    layoutDetailsRequest.getDetailsId()
                            )
                            .orElseThrow(() -> new BusinessException(
                                    ResponseCode.CFG_INVALID_DETAILS_ID,
                                    HttpStatus.NOT_FOUND
                            ));

                    saveLayoutDetails = layoutDetailsMapper.toEntity(layoutDetailsRequest);
                    saveLayoutDetails.setLayoutId(existing.getLayoutId());
                    saveLayoutDetails.setElemetSection(existing.getElemetSection());
                    saveLayoutDetails.setDetailsId(existing.getDetailsId());
                    saveLayoutDetails.setCreatedBy(existing.getCreatedBy());
                    saveLayoutDetails.setCreatedDate(existing.getCreatedDate());
                    saveLayoutDetails.setUpdatedBy(userDetails.getId());
                    saveLayoutDetails.setUpdatedDate(new Timestamp(System.currentTimeMillis()));
                }

                saveLayoutDetails.setElementName(elementName);

                saveLayoutDetails = layoutDetailsRepository.save(saveLayoutDetails);

                if (Objects.nonNull(saveLayoutDetails)) {
                    listLayoutDetails.add(saveLayoutDetails);
                }

            } else if (layoutDetailsRequest.getDetailsFlag().equalsIgnoreCase(ActionFlagEnum.DELETE.getValue())) {

                layoutDetailsRepository
                        .findByLayoutIdAndDetailsId(
                                layoutRequestDto.getLayoutId(),
                                layoutDetailsRequest.getDetailsId()
                        )
                        .orElseThrow(() -> new BusinessException(
                                ResponseCode.CFG_INVALID_DETAILS_ID,
                                HttpStatus.NOT_FOUND
                        ));

                layoutDetailsRepository.deleteById(layoutDetailsRequest.getDetailsId());
            }
        });

        List<String> missingMandatory = mandatoryElementNames.stream()
                .filter(name -> !satisfiedElements.contains(name))
                .collect(Collectors.toList());

        if (!missingMandatory.isEmpty()) {
            throw new BusinessException(
                    ResponseCode.VAL_ALL_MANDATORY_ELEMENTS_SHOULD_BE_SENT,
                    HttpStatus.BAD_REQUEST
            );
        }
        return this.convertLayoutToLayoutResponseDto(saveLayout, listLayoutDetails);
    }

    /*
     * Enables/disables a layout by changing the status Logs the transaction in
     * table MD_ADT_BKD_LOG
     */
    @Transactional
    public void changeLayoutStatus(ChangeStatusRequestDto changeLayoutStatusRequestDto, String remoteAddress, int instId) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        Layout layout = layoutRepository.findByInstIdAndLayoutId(instId, changeLayoutStatusRequestDto.getId()).orElseThrow(() -> new BusinessException(ResponseCode.CFG_INVALID_LAYOUT_ID, HttpStatus.NOT_FOUND));
        if (changeLayoutStatusRequestDto.getStatus().trim().equalsIgnoreCase(layout.getStatus())) {
            throw new BusinessException(ResponseCode.CFG_STATUS_NOT_CHANGED, HttpStatus.CONFLICT);
        }
//
//        //Do not change layout status if linked to a job or to a channel
//        if (channelRepository.existsByInstIdAndLayout(instId, layout)) {
//            throw new BusinessException(ResponseCode.CFG_STATUS_NOT_CHANGED, HttpStatus.BAD_REQUEST);
//        }

        if (jobTaskRepository.existsByLayout(String.valueOf(layout.getLayoutId())) > 0) {
            throw new BusinessException(ResponseCode.CFG_STATUS_NOT_CHANGED, HttpStatus.BAD_REQUEST);
        }

        layoutRepository.updateLayoutStatus(changeLayoutStatusRequestDto.getId(), changeLayoutStatusRequestDto.getStatus().charAt(0), userDetails.getId());
    }

    /*
     * Deletes a layout from table MD_CFG_LAYOUT and its details from table
     * MD_CFG_LAYOUT_DETAILS Logs the transaction in table MD_ADT_BKD_LOG
     */
    @Transactional
    public void deleteLayout(int layoutId, String remoteAddress, int instId) {
		UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
        Layout layout = layoutRepository.findByInstIdAndLayoutId(instId, layoutId).orElseThrow(() -> new BusinessException(ResponseCode.CFG_INVALID_LAYOUT_ID, HttpStatus.NOT_FOUND));
//
//        //Do not delete layout if linked to a job or to a channel
//        if (channelRepository.existsByInstIdAndLayout(instId, layout)) {
//            throw new BusinessException(ResponseCode.CFG_LAYOUT_NO_DELETE, HttpStatus.BAD_REQUEST);
//        }

        if (jobTaskRepository.existsByLayout(String.valueOf(layout.getLayoutId())) > 0) {
            throw new BusinessException(ResponseCode.CFG_LAYOUT_NO_DELETE, HttpStatus.BAD_REQUEST);
        }

        //Delete layout details from MD_CFG_LAYOUT_DETAILS
        layoutDetailsRepository.deleteAllByLayoutId(layoutId);

        layoutRepository.deleteById(layoutId);
    }

    /*
     * Converts Layout object to LayoutResponseDto
     */
    private LayoutResponseDto convertLayoutToLayoutResponseDto(Layout layout, List<LayoutDetails> listLayoutDetails) {
        LayoutResponseDto layoutResponseDto = layoutMapper.toDto(layout);
        layoutResponseDto.setLayoutName(layout.getLayoutName().trim());

        File file = fileRepository.findById(layout.getFileId()).orElseThrow(() -> new BusinessException(ResponseCode.CFG_INVALID_FILE, HttpStatus.NOT_FOUND));
        FileResponseDto fileResponseDto = fileMapper.toDto(file);
        fileResponseDto.setFileTypeId(file.getFileType().getFileTypeId());
        fileResponseDto.setFileTypeCode(file.getFileType().getFileTypeCode());
        layoutResponseDto.setFileResponseDto(fileResponseDto);

        //Set file format description
        String fileFormatDesc = "";
        if (layout.getLayoutFormat().equalsIgnoreCase(LayoutFormatEnum.TXT.getValue())) {
            fileFormatDesc = "TEXT";
        } else if (layout.getLayoutFormat().equalsIgnoreCase(LayoutFormatEnum.CSV.getValue())) {
            fileFormatDesc = "CSV";
        } else if (layout.getLayoutFormat().equalsIgnoreCase(LayoutFormatEnum.EXCEL.getValue())) {
            fileFormatDesc = "EXCEL";
        } else if (layout.getLayoutFormat().equalsIgnoreCase(LayoutFormatEnum.XML.getValue())) {
            fileFormatDesc = "XML";
        }
        layoutResponseDto.setLayoutFormatDesc(fileFormatDesc);

        List<LayoutDetailsResponseDto> listLayoutDetailsResponse = new ArrayList<LayoutDetailsResponseDto>();
        if (Objects.nonNull(listLayoutDetails) && !listLayoutDetails.isEmpty()) {
            listLayoutDetails.forEach((layoutDetails) -> {
                LayoutDetailsResponseDto layoutDetailsResponseDto = layoutDetailsMapper.toDto(layoutDetails);

                if (Objects.nonNull(layoutDetails.getElementId())) {
                    FileElement element = fileElementRepository.findById(layoutDetails.getElementId()).orElseThrow(() -> new BusinessException(ResponseCode.CFG_INVALID_ELEMENT_ID, HttpStatus.NOT_FOUND));
                    layoutDetailsResponseDto.setElementId(element.getElementId());
                    layoutDetailsResponseDto.setElementName(element.getElementName());
                    layoutDetailsResponseDto.setElementSection(layoutDetails.getElemetSection());
                }

                if (Objects.nonNull(layoutDetails.getElementParentId()) && layoutDetails.getElementParentId() != 0) {
                    FileElement parentElement = fileElementRepository.findById(layoutDetails.getElementParentId()).orElseThrow(() -> new BusinessException(ResponseCode.CFG_INVALID_ELEMENT_ID, HttpStatus.NOT_FOUND));
                    layoutDetailsResponseDto.setElementParentId(parentElement.getElementId());
                    layoutDetailsResponseDto.setElementParentName(parentElement.getElementName().trim());
                }
                listLayoutDetailsResponse.add(layoutDetailsResponseDto);
            });
            layoutResponseDto.setListLayoutDetailsResponse(listLayoutDetailsResponse);
        }
        return layoutResponseDto;
    }

    public PaginationLayoutResponseDto getPaginationResponseDto(PaginationRequestDto paginationRequestDto, Page<?> pagesResult) {
        PaginationLayoutResponseDto paginationResponseDto = new PaginationLayoutResponseDto();
        paginationResponseDto.setAsc(paginationRequestDto.getAsc());
        paginationResponseDto.setPageNumber(paginationRequestDto.getOffset());
        paginationResponseDto.setPageSize(paginationRequestDto.getPageSize());
        paginationResponseDto.setSortBy(paginationRequestDto.getSortBy());

        long totalElements = pagesResult.getTotalElements();
        paginationResponseDto.setTotalNumberOfRecords((int) totalElements);
        return paginationResponseDto;
    }
}