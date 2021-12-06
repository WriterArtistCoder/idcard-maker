package qr;

import java.awt.image.BufferedImage;

// Java code to generate QR code

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import crpyto.Encryptor;
import crpyto.Keys;

public class Generator {
	/**
	 * Creates a QR code from an arbitrary string and returns it.
	 * 
	 * @param data    Arbitrary string
	 * @param charset Charset (e.g. UTF-8)
	 * @param hashMap Error correction level HashMap
	 * @param height  Image height
	 * @param width   Image width
	 * @throws WriterException
	 * @throws IOException
	 * @returns QR code as a BufferedImage
	 */
	public static BufferedImage createQRRaw(String data, String charset, Map hashMap, int height, int width)
			throws WriterException, IOException {
		BitMatrix matrix = new MultiFormatWriter().encode(new String(data.getBytes(charset), charset),
				BarcodeFormat.QR_CODE, width, height);

		return MatrixToImageWriter.toBufferedImage(matrix);
	}

	/**
	 * Creates a QR code from an arbitrary string and saves it as a file.
	 * 
	 * @param data    Arbitrary string
	 * @param path    Relative filepath to save it at
	 * @param charset Charset (e.g. UTF-8)
	 * @param hashMap Error correction level HashMap
	 * @param height  Image height
	 * @param width   Image width
	 * @throws WriterException
	 * @throws IOException
	 */
	public static void createQRRaw(String data, String path, String charset, Map hashMap, int height, int width)
			throws WriterException, IOException {
		BitMatrix matrix = new MultiFormatWriter().encode(new String(data.getBytes(charset), charset),
				BarcodeFormat.QR_CODE, width, height);

		MatrixToImageWriter.writeToPath(matrix, path.substring(path.lastIndexOf('.') + 1), Paths.get(path));
		System.out.println("\nQR Code generated at " + path);
	}

	/**
	 * Creates the front and back QR codes from a private key and returns them as an array of BufferedImages.
	 * @param pkey
	 * @return [front, back]
	 * @throws NoSuchAlgorithmException
	 * @throws WriterException
	 * @throws IOException
	 */
	public static BufferedImage[] createQR(String pkey) throws NoSuchAlgorithmException, WriterException, IOException {
		// The data that the QR code will contain
		Keys keys = new Keys();
		byte[][] exBoth = Encryptor.encrypt(keys.getOkey(), pkey);

		String e64Front = Base64.getUrlEncoder().encodeToString(exBoth[0]);
		String e64Back = Base64.getUrlEncoder().encodeToString(exBoth[1]);

		System.out.println("\nENCRYPTED BASE-64");
		System.out.println("Front QR data:    " + e64Front);
		System.out.println("Back QR data:     " + e64Back);

		Map<EncodeHintType, ErrorCorrectionLevel> hashMap = new HashMap<EncodeHintType, ErrorCorrectionLevel>();

		hashMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);

		return new BufferedImage[] {
				Generator.createQRRaw(Keys.QR_VERSION + ":" + e64Front, "UTF-8", hashMap, 200, 200),
				Generator.createQRRaw(Keys.QR_VERSION + ":" + e64Back, "UTF-8", hashMap, 200, 200) };
	}
}
