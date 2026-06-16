package com.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Id;

@javax.persistence.Entity
@javax.persistence.Table(name = "hbn_product")
public class Product {

	@Id
	@Column(name = "product_id")
	private int productId;

	@Column(name = "product_name", length = 100, nullable = false, unique = true)
	private String productName;

	@Column(name = "selling_price", nullable = false)
	private BigDecimal sellingPrice;

	@Column(name = "base_category", length = 50, nullable = false)
	private String baseCategory;

	@Column(name = "product_description")
	private String productDescription;

	public Product() {
		super();
	}

	public Product(int productId, String productName, BigDecimal sellingPrice, String baseCategory,
			String productDescription) {
		super();
		this.productId = productId;
		this.productName = productName;
		this.sellingPrice = sellingPrice;
		this.baseCategory = baseCategory;
		this.productDescription = productDescription;
	}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public BigDecimal getSellingPrice() {
		return sellingPrice;
	}

	public void setSellingPrice(BigDecimal sellingPrice) {
		this.sellingPrice = sellingPrice;
	}

	public String getBaseCategory() {
		return baseCategory;
	}

	public void setBaseCategory(String baseCategory) {
		this.baseCategory = baseCategory;
	}

	public String getProductDescription() {
		return productDescription;
	}

	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}

	@Override
	public String toString() {
		return "Product [productId=" + productId + ", productName=" + productName + ", sellingPrice=" + sellingPrice
				+ ", baseCategory=" + baseCategory + ", productDescription=" + productDescription + "]";
	}

}
