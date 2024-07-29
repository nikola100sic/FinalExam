package test.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import test.model.Product;
import test.repository.ProductRepository;
import test.service.ProductService;

@Service
public class JpaProductService implements ProductService {

	@Autowired
	private ProductRepository productRepository;

	@Override
	public Product findOne(Long id) {
		return productRepository.findOneById(id);
	}

	@Override
	public List<Product> findAll() {
		return productRepository.findAll();
	}

	@Override
	public Page<Product> findAll(Integer pageNo) {
		return productRepository.findAll(PageRequest.of(pageNo, 10));
	}

	@Override
	public Product save(Product product) {
		return productRepository.save(product);
	}

	@Override
	public Product update(Product product) {
		return productRepository.save(product);
	}

	@Override
	public Product delete(Long id) {
		Product product = findOne(id);
		if (product != null) {
			product.getStore().getProducts().remove(product);
			productRepository.delete(product);
			return product;
		}
		return null;
	}

	@Override
	public Page<Product> find(Long shopId, String name, Double priceFrom, Double priceTo, int page) {
		if (priceFrom == null) {
			priceFrom = 0.0;
		}
		if (priceTo == null) {
			priceTo = Double.MAX_VALUE;
		}
		if (name == null) {
			name = "";
		}

		return productRepository.search(shopId, name, priceFrom, priceTo, PageRequest.of(page, 10));
	}

}
