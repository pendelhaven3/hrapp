package com.pj.hrapp.service;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.pj.hrapp.dao.IntegrationTest;
import com.pj.hrapp.model.Employee;
import com.pj.hrapp.model.ValeProduct;

public class ValeProductServiceIntegrationTest extends IntegrationTest {

	@Autowired private ValeProductService valeProductService;
	
	@Test
	@Ignore
	public void test() {
		Employee employee = new Employee();
		employee.setMagicCustomerCode("PJ");
		
		List<ValeProduct> valeProducts = valeProductService.findUnpaidValeProductsByEmployee(employee);
		System.out.println(valeProducts.size());
	}
	
}
