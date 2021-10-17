package crpyto;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Encryptor {
	public static void main(String[] args) throws NoSuchAlgorithmException {
		byte[][] e = encrypt("ce7", "9f426b");

		System.out.println("Front");
		for (byte b : e[0]) {
			System.out.print(String.format("%02x", b));
		}

		System.out.println("\n\nBack");
		for (byte b : e[1]) {
			System.out.print(String.format("%02x", b));
		}
	}

	/**
	 * Returns a 2D array of two byte arrays each containing hashes. The first
	 * element should be used as the raw data of the front QR code, the second as
	 * the back. The personal key should be twice the length of the organization
	 * key.
	 * 
	 * @param oKey Organization key as hex string
	 * @param pKey Personal key as hex string
	 * @return [Front hash, back hash]
	 * @throws NoSuchAlgorithmException
	 */
	public static byte[][] encrypt(String oKey, String pKey) throws NoSuchAlgorithmException {
		// Divide into two halves
		String sxFront = oKey + pKey.substring(0, pKey.length() / 2);
		String sxBack = oKey + pKey.substring(pKey.length() / 2);

		System.out.println("Organization key: " + oKey);
		System.out.println("Personal key:     " + pKey);
		System.out.println("\nUNENCRYPTED");
		System.out.println("Front QR data:    " + sxFront);
		System.out.println("Back QR data:     " + sxBack);

		// Hash
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		byte[] bFront = digest.digest(hexToBytes(sxFront));
		byte[] bBack = digest.digest(hexToBytes(sxBack));

		System.out.println("\nENCRYPTED");
		System.out.println("Front QR data:    " + bytesToHex(bFront));
		System.out.println("Back QR data:     " + bytesToHex(bBack));

		return new byte[][] { bFront, bBack };
	}

	/**
	 * Converts a hexadecimal string to a byte array.
	 * 
	 * @param hex Hex string
	 * @return Byte array
	 */
	private static byte[] hexToBytes(String hex) {
		int len = hex.length();
		byte[] bytes = new byte[len / 2];

		for (int i = 0; i < len; i += 2) {
			bytes[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4) + Character.digit(hex.charAt(i + 1), 16));
		}
		return bytes;
	}

	/**
	 * Converts a byte array to a hexadecimal string.
	 * 
	 * @param bytes Byte array 
	 * @return Hex string
	 */
	private static String bytesToHex(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		for (byte b : bytes) {
			sb.append(String.format("%02x", b));
		}

		return sb.toString();
	}
}
