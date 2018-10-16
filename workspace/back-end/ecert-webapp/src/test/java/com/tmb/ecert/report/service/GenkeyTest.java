package com.tmb.ecert.report.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.tmb.ecert.Application;
import com.tmb.ecert.requestorform.service.RequestGenKeyService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class})
public class GenkeyTest {

	@Autowired
	private RequestGenKeyService requestGenKeyService; 
	
	@Test
	public void Genkey() {
		for(int i=0; i < 5; i++) {
			System.out.println(requestGenKeyService.getNextKey());			
		}
	}
}
