package test.service;

import java.util.List;

import org.springframework.data.domain.Page;

import test.model.Product;

public interface ProductService {

	Product findOne(Long id);

	List<Product> findAll();

	Page<Product> findAll(Integer pageNo);

	Product save(Product product);

	Product update(Product product);

	Product delete(Long id);

	Page<Product> find(Long shopId, String name, Double priceFrom, Double priceTo, int page);

}
