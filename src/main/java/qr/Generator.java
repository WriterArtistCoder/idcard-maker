package qr;

// Java code to generate QR code

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.NotFoundException;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import crpyto.Encryptor;

public class Generator {
	private static String[] keys;

	static {
		ArrayList<String> alKeys = new ArrayList<>();
		try {
			File file = new File("src/main/resources/keys.txt");
			Scanner reader = new Scanner(file);
			while (reader.hasNextLine()) {
				alKeys.add(reader.nextLine());
			}
			
			reader.close();
	        keys = new String[alKeys.size()];
	        keys = alKeys.toArray(keys);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	// Function to create the QR code
	public static void createQR(String data, String path, String charset, Map hashMap, int height, int width)
			throws WriterException, IOException {
		BitMatrix matrix = new MultiFormatWriter().encode(new String(data.getBytes(charset), charset),
				BarcodeFormat.QR_CODE, width, height);

		MatrixToImageWriter.writeToFile(matrix, path.substring(path.lastIndexOf('.') + 1), new File(path));
	}

	// Driver code
	public static void main(String[] args)
		throws WriterException, IOException,
			NotFoundException, NoSuchAlgorithmException
	{
		// TODO improve docs, print keys
		// The data that the QR code will contain
		byte[][] exBoth = Encryptor.encrypt(keys[0], keys[1]);
		
		String e64Front = Base64.getUrlEncoder().encodeToString(exBoth[0]);
		String e64Back = Base64.getUrlEncoder().encodeToString(exBoth[1]);

		System.out.println("\nENCRYPTED BASE-64");
		System.out.println("Front QR data:    " + e64Front);
		System.out.println("Back QR data:     " + e64Back);

		// The path where the image will get saved
		String path = "src/main/resources/demo.png";

		// Encoding charset
		String charset = "UTF-8";

		Map<EncodeHintType, ErrorCorrectionLevel> hashMap
			= new HashMap<EncodeHintType,
						ErrorCorrectionLevel>();

		hashMap.put(EncodeHintType.ERROR_CORRECTION,
					ErrorCorrectionLevel.L);

		// Create the QR code and save
		// in the specified folder
		// as a jpg file
		createQR(e64Front, path, charset, hashMap, 200, 200);
		System.out.println("QR Code Generated!!! ");
	}
}
