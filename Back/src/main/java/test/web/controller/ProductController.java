package test.web.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import test.model.Product;
import test.service.ProductService;
import test.support.ProductDTOToProduct;
import test.support.ProductToProductDTO;
import test.web.dto.ProductDTO;

@RestController
@RequestMapping(value = "/api/products", produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
public class ProductController {

	@Autowired
	private ProductService productService;

	@Autowired
	private ProductDTOToProduct toProduct;

	@Autowired
	private ProductToProductDTO toProductDTO;

	@GetMapping
	@PreAuthorize("permitAll()")
	public ResponseEntity<List<ProductDTO>> getAll(@RequestParam(required = false) Long storeId,
			@RequestParam(required = false) String name, @RequestParam(required = false) Double priceFrom,
			@RequestParam(required = false) Double priceTo,
			@RequestParam(value = "pageNo", defaultValue = "0") int pageNo) {

		Page<Product> page;

		page = productService.find(storeId, name, priceFrom, priceTo, pageNo);

		HttpHeaders headers = new HttpHeaders();
		headers.add("Total-Pages", Integer.toString(page.getTotalPages()));

		return new ResponseEntity<>(toProductDTO.convert(page.getContent()), headers, HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasAnyAuthority('ADMIN')")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		Product product = productService.findOne(id);
		if (product.getOrders().size() > 0) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

		}
		Product deletedProduct = productService.delete(id);

		if (deletedProduct != null) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/{id}")
	@PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
	public ResponseEntity<ProductDTO> getOne(@PathVariable Long id) {
		Product product = productService.findOne(id);

		if (product != null) {
			return new ResponseEntity<>(toProductDTO.convert(product), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
	@PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ProductDTO> update(@PathVariable Long id, @Valid @RequestBody ProductDTO productDTO) {

		if (!id.equals(productDTO.getId())) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		Product product = toProduct.convert(productDTO);
		Product savedProduct = productService.update(product);

		return new ResponseEntity<>(toProductDTO.convert(savedProduct), HttpStatus.OK);

	}

	@PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ProductDTO> create(@Valid @RequestBody ProductDTO productDTO) {
		Product product = toProduct.convert(productDTO);

		if (product.getStore() == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		Product savedProduct = productService.save(product);

		return new ResponseEntity<>(toProductDTO.convert(savedProduct), HttpStatus.CREATED);
	}

	@ExceptionHandler(value = DataIntegrityViolationException.class)
	public ResponseEntity<Void> handle() {
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

}
