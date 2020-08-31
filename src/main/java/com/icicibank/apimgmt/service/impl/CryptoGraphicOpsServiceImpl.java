package com.icicibank.apimgmt.service.impl;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;

import com.icicibank.apimgmt.service.CyptographicOpsService;

@Service
public final class CryptoGraphicOpsServiceImpl implements CyptographicOpsService {

	@Override
	public String doEncryption(String input) {
		byte[] ivBytes=null;
		 String password="THIS_IS_TEST";
		 
		    long time =System.currentTimeMillis();
	        String currentTime=String.valueOf(time);
	        System.out.println(currentTime.substring(currentTime.length()-8));
		 
		 String salt=currentTime.substring(currentTime.length()-8);
                     
		byte[] saltBytes = salt.getBytes();
		// Derive the key
		SecretKeyFactory factory=null;
		try {
			factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), saltBytes, 65556, 128);
		SecretKey secretKey=null;
		try {
			secretKey = factory.generateSecret(spec);
		} catch (InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		SecretKeySpec secret = new SecretKeySpec(secretKey.getEncoded(), "AES");
		// encrypting the word
		Cipher cipher=null;
		try {
			cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			cipher.init(Cipher.ENCRYPT_MODE, secret);
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		AlgorithmParameters params = cipher.getParameters();
		try {
			ivBytes = params.getParameterSpec(IvParameterSpec.class).getIV();
		} catch (InvalidParameterSpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		byte[] encryptedTextBytes=null;
		try {
			encryptedTextBytes = cipher.doFinal(input.getBytes("UTF-8"));
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		List<Byte> saltBytesList = Arrays.asList(ArrayUtils.toObject(saltBytes));
		List<Byte> ivBytesList = Arrays.asList(ArrayUtils.toObject(ivBytes));
		List<Byte> encryptedTextBytesList = Arrays.asList(ArrayUtils.toObject(encryptedTextBytes));
		List<Byte> al = new ArrayList<Byte>(1);
		al.addAll(saltBytesList);
		al.addAll(ivBytesList);
		al.addAll(encryptedTextBytesList);
		Byte[] buffer = al.toArray(new Byte[al.size()]);
		return Base64Utils.encodeToString(ArrayUtils.toPrimitive(buffer));

	}

	@Override
	public String doDecyption(String input) {
		 String password="THIS_IS_TEST";
			Cipher cipher=null;;
			try {
				cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchPaddingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// strip off the salt and iv
			ByteBuffer buffer = ByteBuffer.wrap(Base64Utils.decodeFromString(input));
			byte[] saltBytes = new byte[8];
			buffer.get(saltBytes, 0, saltBytes.length);
			byte[] ivBytes1 = new byte[cipher.getBlockSize()];
			buffer.get(ivBytes1, 0, ivBytes1.length);
			byte[] encryptedTextBytes = new byte[buffer.capacity() - saltBytes.length - ivBytes1.length];

			buffer.get(encryptedTextBytes);
			// Deriving the key
			SecretKeyFactory factory=null;
			try {
				factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), saltBytes, 65556, 128);
			SecretKey secretKey=null;
			try {
				secretKey = factory.generateSecret(spec);
			} catch (InvalidKeySpecException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			SecretKeySpec secret = new SecretKeySpec(secretKey.getEncoded(), "AES");
			try {
				cipher.init(Cipher.DECRYPT_MODE, secret, new IvParameterSpec(ivBytes1));
			} catch (InvalidKeyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvalidAlgorithmParameterException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			byte[] decryptedTextBytes = null;
			try {
				decryptedTextBytes = cipher.doFinal(encryptedTextBytes);
			} catch (IllegalBlockSizeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (BadPaddingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return new String(decryptedTextBytes);
		
	}

}
