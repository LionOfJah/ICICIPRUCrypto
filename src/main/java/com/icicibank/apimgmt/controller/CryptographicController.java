package com.icicibank.apimgmt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.icicibank.apimgmt.model.ResponseModel;
import com.icicibank.apimgmt.service.CyptographicOpsService;

@RestController
@RequestMapping("/api/v0")
public class CryptographicController {

	@Autowired
	ResponseModel responseModel;

	@Autowired
	CyptographicOpsService cryptoService;

	@PostMapping(value = "/encryption")
	public ResponseEntity<ResponseModel> doEncryption(@RequestBody String input) {

		String response = cryptoService.doEncryption(input);

		responseModel.setResponse(response);
		return ResponseEntity.ok().body(responseModel);
	}

	@PostMapping(value = "/decryption")
	public ResponseEntity<ResponseModel> doDecryption(@RequestBody String input) {

		String response = cryptoService.doDecyption(input);

		responseModel.setResponse(response);
		return ResponseEntity.ok().body(responseModel);
	}

}
