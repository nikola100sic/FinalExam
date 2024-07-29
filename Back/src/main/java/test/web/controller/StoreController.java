package test.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import test.model.Store;
import test.service.StoreService;
import test.support.StoreToStoreDTO;
import test.web.dto.StoreDTO;

@RestController
@RequestMapping(value = "/api/stores", produces = MediaType.APPLICATION_JSON_VALUE)
public class StoreController {

	@Autowired
	private StoreService storeService;

	@Autowired
	private StoreToStoreDTO toStoreDTO;

	@GetMapping
	@PreAuthorize("permitAll()")
	public ResponseEntity<List<StoreDTO>> getAll(@RequestParam(value = "pageNo", defaultValue = "0") int pageNo) {

		List<Store> stores = storeService.findAll();

		return new ResponseEntity<>(toStoreDTO.convert(stores), HttpStatus.OK);
	}

}
