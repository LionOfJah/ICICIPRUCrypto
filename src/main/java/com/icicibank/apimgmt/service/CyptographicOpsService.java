package com.icicibank.apimgmt.service;

import org.jvnet.hk2.annotations.Service;

@Service
public interface CyptographicOpsService {

	public String doEncryption(String input);
	
	public String doDecyption(String input);
}
