package ui;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.stream.Stream;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.google.zxing.WriterException;
import crpyto.Keys;
import qr.Generator;

public class MainUI {
	private JFrame frame;
	private String[] pkeysDrop;

	private static final byte MENU = 0;
	private static final byte PREVIEW = 1;
	private int scene = MENU;

	public MainUI() {
		frame = new JFrame("ID Card Maker");
		frame.setSize(300, 300);
		frame.setVisible(true);

		Keys keys = new Keys();
		pkeysDrop = Stream.of(keys.getPkeys()).sorted().map(e -> e.substring(0, 3) + "…" + e.substring(e.length() - 3))
				.toArray(String[]::new);
	}

	// Driver code
	public static void main(String[] args) throws InterruptedException, NoSuchAlgorithmException, WriterException, IOException {
		MainUI mui = new MainUI();
		mui.startUI();
	}

	private JPanel menu = new JPanel();
	private String s0_chosen_pkey;
	
	private JPanel preview = new JPanel();

	private void startUI() throws InterruptedException, NoSuchAlgorithmException, WriterException, IOException {
		while (true) {
			switch (scene) {
			
			case MENU:
				menu.setVisible(true);
				initMenu(menu);
				frame.add(menu);
				break;
				
			case PREVIEW:
				preview.setVisible(true);
				initPreview(preview);
				frame.add(preview);
				break;
				
			}
		}
	}

	/**
	 * Initialize the "menu" JPanel - add all the necessary components.
	 * 
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
			s0_chosen_pkey = (String) dropdown.getSelectedItem();
			
			frame.remove(menu);
			JPanel preview = new JPanel();
			preview.setVisible(true);
			try {
				initPreview(preview);
			} catch (NoSuchAlgorithmException | WriterException | IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			frame.add(preview);
			scene = PREVIEW;
		});
		panel.add(submit);
	}

	/**
	 * Initialize the "preview" JPanel - add all the necessary components.
	 * 
	 * @param panel The JPanel
	 * @throws IOException 
	 * @throws WriterException 
	 * @throws NoSuchAlgorithmException 
	 */
	private void initPreview(JPanel panel) throws NoSuchAlgorithmException, WriterException, IOException {
		BufferedImage[] qrCodes = Generator.createQR(s0_chosen_pkey);
		JLabel qrFront = new JLabel(new ImageIcon(qrCodes[0]));
		JLabel qrBack = new JLabel(new ImageIcon(qrCodes[1]));
		
		preview.add(qrFront);
		preview.add(qrBack);
	}
}
