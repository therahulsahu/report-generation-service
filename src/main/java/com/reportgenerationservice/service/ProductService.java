package com.reportgenerationservice.service;

import java.util.List;

import com.reportgenerationservice.model.Product;

public interface ProductService {
	List<Product> getProductList();
	boolean createProducts(List<Product> productList);
	boolean deleteProducts(List<Product> productList);
	long getProductsCount();
}
