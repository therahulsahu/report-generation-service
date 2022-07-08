package com.reportgenerationservice.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.reportgenerationservice.excelConfig.ProductExcelExporter;
import com.reportgenerationservice.model.Product;
import com.reportgenerationservice.model.ProductResponse;
import com.reportgenerationservice.model.UserBean;
import com.reportgenerationservice.service.ProductService;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("api/productlist/v1")
public class ProductController {

	@Autowired
	ProductService productService;

	@GetMapping("/getlist")
	public List<Product> getlist() {
		System.out.println("Get list called");
		return productService.getProductList();
	}

	@PostMapping("/createproduct")
	public Map<String, String> createProduct(@RequestBody List<Product> productRequest) {
		System.out.println(">>>>>productRequest--->>>>" + productRequest);
		HashMap<String, String> map = new HashMap<>();
		boolean res = productService.createProducts(productRequest);
		if (res) {
			map.put("response", "Successfully product created");
		} else {
			map.put("response", "Products not created");
		}
		return map;
	}

	@PostMapping("/deleteproduct")
	public ProductResponse deleteproduct(@RequestBody List<Product> productRequest) {
		System.out.println("Start 2>>>>>In newlistController::deleteproduct()>>>>>>>>>>>>");
		System.out.println("Before Size of product List" + productService.getProductsCount());
		ProductResponse response = new ProductResponse();

		boolean res = productService.deleteProducts(productRequest);

		if (res) {
			response.setStatusCode("200");
			response.setStatusMessage("Successfully Deleted");
		} else {
			response.setStatusCode("401");
			response.setStatusMessage("Not Deleted");
		}
		return response;
	}

	@PostMapping("/login")
	public UserBean login(@RequestBody UserBean userBean) {
		System.out.println("Start 2 In HelloWorldController::login()>>>>>>>>>>>>");

		if ((userBean.getUserName().equals("abc") && userBean.getUserPassword().equals("abc"))) {
			System.out.println("User is login succesful ");
			userBean.setErrorCode("200");
			// navigate to welcome screen where it shows product page with links or tabs
			return userBean;
		} else {
			System.out.println("User is login faild due to invalid credientials ");
			userBean.setErrorCode("400");
			// show same login page
			return userBean;
		}
	}

	@GetMapping("/excel")
	public void exportToExcelAndDownload(HttpServletResponse response) throws IOException {
		response.setContentType("application/octet-stream");
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String currentDateTime = dateFormatter.format(new Date());

		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=products_" + currentDateTime + ".xlsx";
		response.setHeader(headerKey, headerValue);

		List<Product> listProducts = productService.getProductList();

		ProductExcelExporter excelExporter = new ProductExcelExporter(listProducts);

		excelExporter.export(response);
	}

	@GetMapping("/csv")
	public void exportToCSV(HttpServletResponse response) throws IOException {
		response.setContentType("text/csv");
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String currentDateTime = dateFormatter.format(new Date());

		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=products_" + currentDateTime + ".csv";
		response.setHeader(headerKey, headerValue);

		List<Product> listProducts = productService.getProductList();

		ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
		String[] csvHeader = { "Product Id", "Product Name", "Product Price", "Product Quantity", "Product Description",
				"Product Type" };
		String[] nameMapping = { "productId", "productName", "productPrice", "productDesc", "productQuantity",
				"productType" };

		csvWriter.writeHeader(csvHeader);

		for (Product products : listProducts) {
			csvWriter.write(products, nameMapping);
		}

		csvWriter.close();

	}

	// single product
//	@PostMapping("/pro/{id}")
//	public product getlist(@pathVariable("id")int id) {
//		return productList.getListById(id);
//	}
//	
//	
//	@PostMapping("/pro/{proid}")
//	public List deleteproduct(@pathvariable("proid") int proId)
//	{
//		this.productList.deleteproduct(proId);
//	}

}
