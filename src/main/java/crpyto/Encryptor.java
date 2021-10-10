package crpyto;

import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Encryptor {
	public static void main(String[] args) throws NoSuchAlgorithmException {
		byte[][] e = encrypt(0xce7, 0x9f426b);

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
	 * the back.
	 * 
	 * @param okey Organization key
	 * @param pkey Personal key (length must be double org. key)
	 * @return [Front hash, back hash]
	 * @throws NoSuchAlgorithmException
	 */
	public static byte[][] encrypt(int okey, int pkey) throws NoSuchAlgorithmException {
		// Divide into two halves
		String okeys = Integer.toHexString(okey);
		String pkeys = Integer.toHexString(pkey);
		int fInt = Integer.parseInt(okeys + pkeys.substring(0, pkeys.length() / 2), 16);
		int bInt = Integer.parseInt(okeys + pkeys.substring(pkeys.length() / 2), 16);

		System.out.println(fInt + " " + bInt);

		// Hash
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		byte[] fByte = digest.digest(intToBytes(fInt));
		byte[] bByte = digest.digest(intToBytes(bInt));

		return new byte[][] { fByte, bByte };
	}

	/**
	 * Converts an int to a byte array.
	 * 
	 * @param i Input int
	 * @return Byte array
	 */
	private static byte[] intToBytes(final int i) {
		ByteBuffer bb = ByteBuffer.allocate(4);
		bb.putInt(i);
		return bb.array();
	}
}
