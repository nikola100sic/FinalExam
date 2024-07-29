package test.support;

import java.util.ArrayList;
import java.util.List;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import test.model.Store;
import test.web.dto.StoreDTO;

@Component
public class StoreToStoreDTO implements Converter<Store, StoreDTO> {

	@Override
	public StoreDTO convert(Store store) {
		StoreDTO dto = new StoreDTO();
		dto.setId(store.getId());
		dto.setName(store.getName());
		return dto;
	}

	public List<StoreDTO> convert(List<Store> stores) {
		List<StoreDTO> storesDTO = new ArrayList<>();

		for (Store store : stores) {
			storesDTO.add(convert(store));
		}

		return storesDTO;
	}

}
