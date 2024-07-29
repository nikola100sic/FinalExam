package test.web.dto;

import java.util.Objects;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.springframework.lang.NonNull;

public class ProductDTO {

	private Long id;
	@NonNull
	private String name;

	private Double price;

	@Min(value = 0)
	@Max(value = 500)
	private Integer available;

	private Long storeId;
	@Size(max = 30)
	private String storeName;

	public ProductDTO(Long id, String name, Double price, @Min(0) @Max(500) Integer available, Long storeId,
			@NotBlank @Size(max = 30) String storeName) {
		super();
		this.id = id;
		this.name = name;
		this.price = price;
		this.available = available;
		this.storeId = storeId;
		this.storeName = storeName;
	}

	public ProductDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Integer getAvailable() {
		return available;
	}

	public void setAvailable(Integer available) {
		this.available = available;
	}

	public Long getStoreId() {
		return storeId;
	}

	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProductDTO other = (ProductDTO) obj;
		return Objects.equals(id, other.id);
	}

}
