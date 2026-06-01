package com.mdsl.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.Validation;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mdsl.model.dto.request.ChangeStatusRequestDto;
import com.mdsl.model.dto.request.ContactRequestDto;
import com.mdsl.model.dto.response.AddressResponseDto;
import com.mdsl.model.dto.response.CityResponseDto;
import com.mdsl.model.dto.response.ContactResponseDto;
import com.mdsl.model.dto.response.CountryResponseDto;
import com.mdsl.service.CityService;
import com.mdsl.service.ContactService;
import com.mdsl.service.CountryService;
import com.mdsl.utils.Validations;

@CrossOrigin(origins = "*")
//@Transactional
@RestController
@RequestMapping("/contact")
@RequiredArgsConstructor
public class ContactController {

	private final ContactService contactService;

	private final CityService cityService;

	private final CountryService countryService;

	@GetMapping
	public ResponseEntity<List<ContactResponseDto>> getAllContacts() {
		return ResponseEntity.ok(contactService.getAllContacts());
	}

	@GetMapping("/{id}")
	public ResponseEntity<ContactRequestDto> getContactById(@PathVariable(value = "id") int contactId) {
		return ResponseEntity.ok(contactService.getContactById(contactId));
	}

	@PostMapping
	public ResponseEntity<ContactRequestDto> saveOrUpdateContact(@Valid @RequestBody ContactRequestDto contactRequestDto, BindingResult bindingResult) {
		Validations.validate(bindingResult);
		return ResponseEntity.ok(contactService.saveOrUpdateContact(contactRequestDto));
	}

	@GetMapping("/countries")
	public ResponseEntity<List<CountryResponseDto>> getAllCountries() {
		return ResponseEntity.ok(countryService.getAllCountries());
	}

	@GetMapping("/entities/{entityId}/{instId}")
	public ResponseEntity<List<ContactResponseDto>> getContactByEntityId(@PathVariable(value = "entityId") String entityId,@PathVariable(value = "instId") String instId) {
		return ResponseEntity.ok(contactService.getContactByEntitiesId(entityId,instId));
	}

	@GetMapping("/country/{id}")
	public ResponseEntity<List<CityResponseDto>> getCityByCountryId(@PathVariable("id") int countryId) {
		return ResponseEntity.ok(cityService.getCityByCountryId(countryId));

	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteContact(@PathVariable(value = "id") int id) {
		try {
			contactService.deleteContact(id);
			String message = "An item is deleted with id : " + id;
			return ResponseEntity.ok(message);
		} catch (DataIntegrityViolationException e) {
			System.out.println("reference exists");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("DataIntegrityViolationException");
		} catch (ConstraintViolationException e) {
			System.out.println("reference exists");
		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.out.println("reference exists");
		}
		return null;
	}

	@PostMapping("/status-change")
	public void changeContactStatus(@Valid @RequestBody ChangeStatusRequestDto changeStatusRequestDto,BindingResult bindingResult) {
		Validations.validate(bindingResult);
		int id = changeStatusRequestDto.getId();
		contactService.updateStatus(id);
	}

	@GetMapping("/active-contacts")
	public ResponseEntity<List<ContactResponseDto>> getActiveContacts() {
		return ResponseEntity.ok(contactService.getActiveContacts());
	}
}
