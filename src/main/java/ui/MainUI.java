package ui;

import java.awt.event.ActionListener;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.google.zxing.EncodeHintType;
import com.google.zxing.NotFoundException;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import crpyto.Encryptor;
import crpyto.Keys;
import qr.Generator;

public class MainUI {
	JFrame frame;
	String[] pkeysDrop;
	public static final int state1 = 1;
	public static final int state2 = 2;
	public int currentState = state1;
	public MainUI() {
		frame = new JFrame("ID Card Maker");
		frame.setSize(300, 300);
		frame.setVisible(true);

		Keys keys = new Keys();
		pkeysDrop = Stream.of(keys.getPkeys()).sorted().map(e -> e.substring(0, 3) + "…" + e.substring(e.length() - 3))
				.toArray(String[]::new);
	}

	// Driver code
	public static void main(String[] args) throws InterruptedException {
		MainUI mui = new MainUI();
		mui.startUI();
	}

	JPanel menu = new JPanel();
	private void startUI() throws InterruptedException {
		menu.setVisible(true);
		initMenu(menu);
		frame.add(menu);
		
		// Do the next thing, check if currentState is state2
	}

	/**
	 * Initialize the "menu" JPanel - add all the necessary components.
	 * @param panel The JPanel
	 */
	private void initMenu(JPanel panel) {
		// TODO line breaks not working in wider screen
		JLabel header = new JLabel("<html><h1 style='text-align: center;'>Generate a DSID<sup>*</sup></h1><br></html>");
		panel.add(header);

		JLabel footnote = new JLabel("<html><br><div><sup>*</sup>Dark Society ID</h1></html>");
		panel.add(footnote);

		JComboBox<String> dropdown = new JComboBox<String>(pkeysDrop);
		panel.add(dropdown);

		JButton submit = new JButton("Generate");
		submit.addActionListener((e) -> {
			if (currentState == state1) {
				frame.remove(menu);
				JPanel preview = new JPanel();
				preview.setVisible(true);
				initPreview(preview);
				
				frame.add(preview);
				currentState = state2;
			}
		});
		panel.add(submit);
	}

	/**
	 * Initialize the "preview" JPanel - add all the necessary components.
	 * @param panel The JPanel
	 */
	private void initPreview(JPanel panel) {
		
	}
}
