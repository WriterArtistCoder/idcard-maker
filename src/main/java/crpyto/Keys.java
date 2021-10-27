package crpyto;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Keys {
	private String[] keys;
	
	/**
	 * Reads the file src/main/resources/keys.txt and initializes Keys.
	 */
	public Keys() {
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
	
	/**
	 * Gets the organization key as a hexadecimal string.
	 * @return The key as a String
	 */
	public String getOkey() {
		return keys[0];
	}
	
	/**
	 * Gets an array of personal keys as hexadecimal strings in order.
	 * @return The keys as a String[]
	 */
	public String[] getPkeys() {
		return Arrays.copyOfRange(keys, 0, keys.length);
	}
}
