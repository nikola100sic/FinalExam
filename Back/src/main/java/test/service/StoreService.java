package test.service;

import java.util.List;

import org.springframework.data.domain.Page;

import test.model.Store;

public interface StoreService {

	Store findOne(Long id);

	List<Store> findAll();

	Page<Store> findAll(Integer pageNo);

}
