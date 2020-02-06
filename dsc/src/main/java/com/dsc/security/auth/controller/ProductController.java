package com.dsc.security.auth.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dsc.security.auth.facadeimpl.ProductFacadeImpl;
import com.dsc.security.auth.model.Product;
import com.dsc.security.auth.request.RegisterCompanyRequest;
import com.dsc.security.auth.response.ErrorResponse;

@RestController
@RequestMapping("/dsc/products")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ProductController {

	@Autowired
	private ProductFacadeImpl productFacade;

	@Secured({ "COMPANY_ADMIN", "COMPANY_USER" })
	@PostMapping("/addproduct")
	public ResponseEntity<Object> setRegisterCompany(@RequestBody RegisterCompanyRequest regComReq,
			HttpServletRequest request, HttpServletResponse response) {
		Product product = regComReq.getProduct();

		try {

			if (regComReq.getProduct() == null || regComReq.getTransactionType().isEmpty()
					|| regComReq.getTransactionType() == null) {
				ErrorResponse error = new ErrorResponse();
				error.setStatusCode("422");
				error.setMessage("Invalid Request");
				return new ResponseEntity<>(error, HttpStatus.UNPROCESSABLE_ENTITY);
			}
			if (!regComReq.getTransactionType().equalsIgnoreCase("save") ) {
				ErrorResponse error = new ErrorResponse();
				error.setStatusCode("422");
				error.setMessage("Transactiontype mismatch");
				return new ResponseEntity<>(error, HttpStatus.UNPROCESSABLE_ENTITY);
			}
			if ((product.getProductName().isEmpty() || product.getProductName() == null)
					|| (product.getProductRef().isEmpty() || product.getProductRef() == null)
					|| (product.getProductBrand().isEmpty() || product.getProductBrand() == null)
					|| (product.getProductModel().isEmpty() || product.getProductModel() == null)
					|| (product.getCompanyRef().isEmpty() || product.getCompanyRef() == null)
					|| (product.getUpcNum() == null)) {
				ErrorResponse error = new ErrorResponse();
				error.setStatusCode("422");
				error.setMessage("Invalid Request fields ");
				return new ResponseEntity<>(error, HttpStatus.UNPROCESSABLE_ENTITY);
			}

			return productFacade.setProduct(regComReq);
		} catch (Exception e) {
			ErrorResponse error = new ErrorResponse();
			error.setStatusCode("409");
			error.setMessage("Exception caught Product controller!");
			error.setStausMessage(e.getMessage());
			return new ResponseEntity<>(error, HttpStatus.CONFLICT);

		}
	}

}
