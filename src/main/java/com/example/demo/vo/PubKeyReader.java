package com.example.demo.vo;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.Cipher;

public class PubKeyReader {

	public static void main(String[] args) throws Exception {
		String toencrypt = "idontwantanyonetoseethis";
		String pubKeyContent = new String(Files.readAllBytes(Paths.get("d:/demo.pub")));
		pubKeyContent = pubKeyContent.replaceAll("\\n", "").replace("-----BEGIN PUBLIC KEY-----", "")
				.replace("-----END PUBLIC KEY-----", "");
		KeyFactory kf = KeyFactory.getInstance("RSA");
		X509EncodedKeySpec keySpecX509 = new X509EncodedKeySpec(Base64.getDecoder().decode(pubKeyContent));
		RSAPublicKey pubKey = (RSAPublicKey) kf.generatePublic(keySpecX509);
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		cipher.init(Cipher.ENCRYPT_MODE, pubKey);
		byte[] encryptedBytes = cipher.doFinal(toencrypt.getBytes());
		System.out.println(Base64.getEncoder().encodeToString(encryptedBytes));

	}
}