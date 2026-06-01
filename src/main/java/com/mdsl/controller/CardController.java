package com.mdsl.controller;

import javax.transaction.Transactional;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;

@Api("Card Controller")
@CrossOrigin(origins = "*")
@RestController
@Transactional(rollbackOn = Exception.class)
@RequestMapping("/v1/config/cards")
public class CardController {

//	private CardService cardService;

//	@Autowired
//	public CardController(CardService cardService) {
//		super();
//		this.cardService = cardService;
//	}
//
//	@GetMapping("/{id}")
//	public ResponseEntity<CardResponseDto> getCardById(@PathVariable("id") int cardId) {
//		return ResponseEntity.ok(cardService.getCardById(cardId));
//	}
	
	
//
//	@GetMapping("/customer/{id}")
//	public ResponseEntity<List<CardResponseDto>> getCardByCustomerId(@PathVariable("id") int customerId) {
//		return ResponseEntity.ok(cardService.getCardByCustomerId(customerId));
//	}
//
//	@PostMapping
//	public ResponseEntity<CardResponseDto> updateCard(@Valid @RequestBody CardRequestDto card,
//			BindingResult bindingResult) {
//		Validations.validate(bindingResult);
//		return ResponseEntity.ok(cardService.updateCard(card));
//	}
//
//	@PutMapping("/activation")
//	public ResponseEntity<CardStatusChangeResponseDto> cardActivation(HttpServletRequest request,
//			@Valid @RequestBody CardStatusChangeRequestDto cardStatusChange, BindingResult bindingResult) {
//		Validations.validate(bindingResult);
//		setupInstIdAndBranchId(request, cardStatusChange);
//		return ResponseEntity.ok(cardService.updateCardActivation(cardStatusChange));
//	}
//
//	@PutMapping("/terminate")
//	public ResponseEntity<CardStatusChangeResponseDto> cardTermination(HttpServletRequest request,
//			@Valid @RequestBody CardStatusChangeRequestDto cardStatusChange, BindingResult bindingResult) {
//		Validations.validate(bindingResult);
//		setupInstIdAndBranchId(request, cardStatusChange);
//		return ResponseEntity.ok(cardService.updateCardTermination(cardStatusChange));
//	}
//
//	@PutMapping("/status-reset")
//	public ResponseEntity<CardStatusChangeResponseDto> cardStatusReset(HttpServletRequest request,
//			@Valid @RequestBody CardStatusChangeRequestDto cardStatusChange, BindingResult bindingResult) {
//		Validations.validate(bindingResult);
//		setupInstIdAndBranchId(request, cardStatusChange);
//		return ResponseEntity.ok(cardService.updateCardStatusReset(cardStatusChange));
//	}
//
//	@PutMapping("/block")
//	public ResponseEntity<CardStatusChangeResponseDto> cardBlock(HttpServletRequest request,
//			@Valid @RequestBody CardStatusChangeRequestDto cardStatusChange, BindingResult bindingResult) {
//		Validations.validate(bindingResult);
//		if (Objects.isNull(cardStatusChange.getCardStatusCode())) {
//			throw new BusinessException(ResponseCode.CFG_INVALID_CARD_STATUS, HttpStatus.NOT_FOUND);
//		}
//		setupInstIdAndBranchId(request, cardStatusChange);
//		return ResponseEntity.ok(cardService.updateCardBlock(cardStatusChange));
//	}
//
//	private void setupInstIdAndBranchId(HttpServletRequest request, CardStatusChangeRequestDto cardStatusChange) {
//		cardStatusChange.setInstId(Integer.parseInt(request.getHeader("instId")));
//		cardStatusChange.setBranchId(Integer.parseInt(request.getHeader("branchId")));
//	}
//
//	@GetMapping("/limit/{id}")
//	public ResponseEntity<LimitRelationResponseDto> getCardlimitById(@PathVariable("id") int cardId,
//			HttpServletRequest request) {
//		return ResponseEntity.ok(cardService.getCardlimitById(cardId, Integer.parseInt(request.getHeader("instId"))));
//	}
//
//	@GetMapping("/update-embossing-name/{id}/{embossingName}")
//	public void updateEmbossingName(
//			@Min(value = 1, message = ResponseCode.CFG_INVALID_CARD) @Max(value = 999, message = ResponseCode.CFG_INVALID_CARD) @PathVariable("id") int cardId,
//			@PathVariable("embossingName") String embossingName, HttpServletRequest request) {
//		cardService.updateEmbossingName(cardId, embossingName, Integer.parseInt(request.getHeader("instId")),
//				Integer.parseInt(request.getHeader("branchId")));
//	}
//
//	@PutMapping("/update-card-contact")
//	public void updateContactInfo(HttpServletRequest request,
//			@Valid @RequestBody CardContactInfoRequestDto cardContactInfo, BindingResult bindingResult) {
//		Validations.validate(bindingResult);
//		cardService.updateContactInfo(cardContactInfo, Integer.parseInt(request.getHeader("branchId")),
//				request.getRemoteAddr());
//	}
//
//	@GetMapping(path = {"/card-replacement/{cardId}/{deliveryBranchId}", "/card-replacement/{cardId}"})
//	public void CardReplacement(
//			@Min(value = 1, message = ResponseCode.CFG_INVALID_CARD) 
//			@Max(value = 999, message = ResponseCode.CFG_INVALID_CARD) 
//			@PathVariable("cardId") int cardId,
//			@PathVariable(name = "deliveryBranchId", required = false) Integer deliveryBranchId, HttpServletRequest request) {
//		cardService.cardReplacement(cardId, deliveryBranchId, Integer.parseInt(request.getHeader("instId")),
//				Integer.parseInt(request.getHeader("branchId")));
//	}

}
