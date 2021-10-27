package ui;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.google.zxing.EncodeHintType;
import com.google.zxing.NotFoundException;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import crpyto.Encryptor;
import crpyto.Keys;
import qr.Generator;

public class MainUI {
	// Driver code
	public static void main(String[] args)
			throws WriterException, IOException, NotFoundException, NoSuchAlgorithmException {
		Keys keys = new Keys();
		String[] pkeysDrop = Stream.of(keys.getPkeys()).sorted().map(e -> e.substring(0, 3) + "…" + e.substring(e.length() - 3)).toArray(String[]::new);

		// create a new frame to store text field and button
		JFrame frame = new JFrame("ID Card Maker");
		frame.setSize(300, 300);

		JPanel pMenu = new JPanel();
		pMenu.setVisible(true);
		frame.add(pMenu);

		JComboBox<String> dropdown = new JComboBox<String>(pkeysDrop);
		dropdown.setVisible(true);
		pMenu.add(dropdown);
		
		JButton submit = new JButton();
		submit.setVisible(true);
		pMenu.add(submit);

		frame.setVisible(true);
	}
}
