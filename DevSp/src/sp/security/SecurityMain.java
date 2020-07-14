package sp.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class SecurityMain {

	public void makeHash() {
		String input =  "test string";
		String algorithm = "SHA-256";
		MessageDigest digest;
		try {
			digest = MessageDigest.getInstance(algorithm);
			byte[] result = digest.digest(input.getBytes());
			StringBuffer strBuf = new StringBuffer();
			for(int i=0;i<result.length;i++) {
				strBuf.append(Integer.toString((result[i] & 0xFF) + 0x100, 16).substring(1));
			}
			System.out.println("algorithm: "+algorithm+", input: "+input+", hash: "+strBuf.toString());
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * https://www.baeldung.com/java-base64-encode-and-decode 
	 */
	public void makeBase64() {
		String originalInput = "test input";
		String encodedString = Base64.getEncoder().encodeToString(originalInput.getBytes());

		byte[] decodedBytes = Base64.getDecoder().decode(encodedString);
		String decodedString = new String(decodedBytes);
		System.out.println("input: "+originalInput+", encoded: "+encodedString+", decoded: "+decodedString);
	}

	public static void main(String[] args) {
		SecurityMain m = new SecurityMain();
		m.makeHash();
		m.makeBase64();
	}
}
