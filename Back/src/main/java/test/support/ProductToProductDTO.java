package test.support;

import java.util.ArrayList;
import java.util.List;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import test.model.Product;
import test.web.dto.ProductDTO;

@Component
public class ProductToProductDTO implements Converter<Product, ProductDTO> {

	@Override
	public ProductDTO convert(Product product) {
		ProductDTO dto = new ProductDTO();
		dto.setId(product.getId());
		dto.setPrice(product.getPrice());
		dto.setAvailable(product.getAvailable());
		dto.setName(product.getName());
		dto.setStoreId(product.getStore().getId());
		dto.setStoreName(product.getStore().getName());
		return dto;
	}

	public List<ProductDTO> convert(List<Product> products) {
		List<ProductDTO> productsDTO = new ArrayList<>();

		for (Product product : products) {
			productsDTO.add(convert(product));
		}

		return productsDTO;
	}
}
