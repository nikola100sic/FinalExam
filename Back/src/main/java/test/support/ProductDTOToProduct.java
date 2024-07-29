package test.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import test.model.Product;
import test.service.ProductService;
import test.service.StoreService;
import test.web.dto.ProductDTO;

@Component
public class ProductDTOToProduct implements Converter<ProductDTO, Product> {

	@Autowired
	private ProductService productService;

	@Autowired
	private StoreService storeService;

	@Override
	public Product convert(ProductDTO dto) {
		Product product;
		if (dto.getId() == null) {
			product = new Product();
		} else {
			product = productService.findOne(dto.getId());
		}
		if (product != null) {
			product.setId(dto.getId());
			product.setPrice(dto.getPrice());
			product.setAvailable(dto.getAvailable());
			product.setName(dto.getName());
			product.setStore(storeService.findOne(dto.getStoreId()));
		}
		return product;
	}

}
