package test.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import test.model.Store;
import test.repository.StoreRepository;
import test.service.StoreService;

@Service
public class JpaStoreService implements StoreService {

	@Autowired
	private StoreRepository storeRepository;

	@Override
	public Store findOne(Long id) {
		return storeRepository.findOneById(id);
	}

	@Override
	public List<Store> findAll() {
		return storeRepository.findAll();
	}

	@Override
	public Page<Store> findAll(Integer pageNo) {
		return storeRepository.findAll(PageRequest.of(pageNo, 10));
	}

}
