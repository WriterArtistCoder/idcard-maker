package ui;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.stream.Stream;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

import com.google.zxing.WriterException;
import crpyto.Keys;
import qr.Generator;

public class MainUI {
	private JFrame frame;
	private String[] pkeys;
	private String[] pkeysDrop;

	private static final byte MENU = 0;
	private static final byte PREVIEW = 1;
	private int scene = MENU;

	public MainUI() {
		frame = new JFrame("ID Card Maker");
		frame.setSize(300, 300);
		frame.setVisible(true);

		Keys keys = new Keys();

		// To do: make sure no two keys can be equal (e.g. 359...eee and 359...eee)
		pkeys = keys.getPkeys();
		pkeysDrop = Stream.of(keys.getPkeys()).map(e -> e.substring(0, 3) + "…" + e.substring(e.length() - 3))
				.toArray(String[]::new);
	}

	// Driver code
	public static void main(String[] args)
			throws InterruptedException, NoSuchAlgorithmException, WriterException, IOException {
		MainUI mui = new MainUI();
		mui.startUI();
	}

	private JPanel menu = new JPanel();
	private String s0_chosen_pkey;

	private JPanel preview = new JPanel();

	private void startUI() throws InterruptedException, NoSuchAlgorithmException, WriterException, IOException {
		// TODO launch html page instead of using swing
		
		menu.setVisible(true);
		initMenu(menu);
		frame.add(menu);
		frame.pack();
	}

	/**
	 * Initialize the "menu" JPanel - add all the necessary components.
	 * 
	 * @param panel The JPanel
	 */
	private void initMenu(JPanel panel) {
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		Border padding = BorderFactory.createEmptyBorder(10, 10, 10, 10);
		panel.setBorder(padding);

		JLabel header = new JLabel("<html><h1>Generate a DSID<sup>*</sup></h1> <br>"
				+ "<h3><sup>*</sup>Dark Society ID</h3> </html>");
		panel.add(header);

		JComboBox<String> dropdown = new JComboBox<String>(pkeysDrop);
		panel.add(dropdown);

		JButton submit = new JButton("Generate");
		submit.addActionListener((e) -> {
			// Find chosen key
			s0_chosen_pkey = pkeys[dropdown.getSelectedIndex()];
			scene = PREVIEW;

			try {
				menu.setVisible(false);

				preview.setVisible(true);
				initPreview(preview);
				frame.add(preview);
				frame.pack();
			} catch (NoSuchAlgorithmException | WriterException | IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
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
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		Border padding = BorderFactory.createEmptyBorder(10, 10, 10, 10);
		panel.setBorder(padding);

		BufferedImage[] qrCodes = Generator.createQR(s0_chosen_pkey, "src/main/resources/front.png", "src/main/resources/back.png");
		Path src = Paths.get("src/main/resources/id-card.svg"); // Template image
		Path temp = Paths.get("src/main/resources/temp-id-card.svg"); // Generated image
		Charset charset = StandardCharsets.UTF_8;

		String content = new String(Files.readAllBytes(src), charset);
		String absFront = FileSystems.getDefault().getPath("src/main/resources/front.png").normalize().toAbsolutePath().toString();
		String absBack = FileSystems.getDefault().getPath("src/main/resources/back.png").normalize().toAbsolutePath().toString();
		content = content.replaceAll("$FN", "");
		content = content.replaceAll("$LN", "");
		content = content.replaceAll("$JOB", "");
		content = content.replaceAll("$DOB", "");
		content = content.replaceAll("$EYES", "");
		content = content.replaceAll("$HAIR", "");
		content = content.replaceAll("$HEIGHT", "");
		content = content.replaceAll("$PHOTO", "");
		content = content.replaceAll("$LICENSE", "");
		content = content.replaceAll("$EXPIRES", "");
		content = content.replaceAll("$PROFILE", "");
		content = content.replaceAll("$BACK", absBack);
		content = content.replaceAll("$FRONT", absFront);
		
		Files.write(temp, content.getBytes(charset));
		
		JLabel header = new JLabel("<html><h1>Result</h1><br>");
		preview.add(header);

		// TODO do not save to file
		JLabel headerFront = new JLabel("<html><h2>Front QR code</h2></html>");
		preview.add(headerFront);
		
		JLabel qrFront = new JLabel(new ImageIcon(qrCodes[0]));
		qrFront.setSize(new Dimension(200, 200));
		preview.add(qrFront);

		JLabel headerBack = new JLabel("<html><h2>Back QR code</h2></html>");
		preview.add(headerBack);
		
		JLabel qrBack = new JLabel(new ImageIcon(qrCodes[1]));
		qrFront.setSize(new Dimension(200, 200));
		preview.add(qrBack);
	}
}
