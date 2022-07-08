package com.reportgenerationervice.repository;

import org.springframework.data.couchbase.repository.CouchbaseRepository;

import com.reportgenerationservice.model.Product;

public interface ProductRepository extends CouchbaseRepository<Product, String> {

}
