package com.pj.hrapp.service.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.pj.hrapp.dao.ValeProductRepository;
import com.pj.hrapp.model.Employee;
import com.pj.hrapp.model.Payslip;
import com.pj.hrapp.model.ValeProduct;
import com.pj.hrapp.service.ValeProductService;
import com.pj.hrapp.util.UrlUtil;

@Service
public class ValeProductServiceImpl implements ValeProductService {

	private static final String SEARCH_URL = "http://magic-db:8080/salesInvoice/search?";
	
	@Autowired private RestTemplate restTemplate;
	@Autowired private ValeProductRepository valeProductRepository;
	
	@Override
	public List<ValeProduct> findUnpaidValeProductsByEmployee(Employee employee) {
		Map<String, String> params = new HashMap<>();
		params.put("customerCode", employee.getMagicCustomerCode());
		params.put("paid", "false");
		
		String url = SEARCH_URL + UrlUtil.mapToQueryString(params);
		
		return Arrays.asList(restTemplate.getForObject(url, ValeProduct[].class));
	}

	@Transactional
	@Override
	public void addValeProductsToPayslip(List<ValeProduct> valeProducts, Payslip payslip) {
		for (ValeProduct valeProduct : valeProducts) {
			valeProduct.setPayslip(payslip);
			valeProductRepository.save(valeProduct);
		}
	}

}
