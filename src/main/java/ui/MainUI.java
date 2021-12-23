package ui;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.stream.Stream;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
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

	public static final String QR_FRONT_PATH = "src/main/resources/temp/front.png";
	public static final String QR_BACK_PATH = "src/main/resources/temp/back.png";
	public static final String PROFILE_PATH = "src/main/resources/profile.jpg";
	public static final String ID_CARD_TEMP_PATH = "src/main/resources/temp/id-card.svg";

	public MainUI() {
		frame = new JFrame("ID Card Maker");
		frame.setSize(300, 300);
		frame.setVisible(true);

		Keys keys = new Keys();

		// TODO make sure no two keys can be equal (e.g. 359...eee and 359...eee)
		pkeys = keys.getPkeys();
		pkeysDrop = Stream.of(keys.getPkeys()).map(e -> e.substring(0, 3) + "…" + e.substring(e.length() - 3))
				.toArray(String[]::new);
		
		s0_card_details = new HashMap<>();

		Path base = Paths.get(ID_CARD_TEMP_PATH).getParent();
        Path front = Paths.get(QR_FRONT_PATH);
        Path back = Paths.get(QR_BACK_PATH);
        Path profile = Paths.get(PROFILE_PATH);
        
		// TODO let user choose images
        
        // Make versions of paths relative to the location of the ID card (ID_CARD_TEMP_PATH)
        String frontRel = base.relativize(front).toString().replaceAll("\\\\", "/");
        String backRel = base.relativize(back).toString().replaceAll("\\\\", "/");
        String profileRel = base.relativize(profile).toString().replaceAll("\\\\", "/");

		s0_card_details.put("FRONT", frontRel);
		s0_card_details.put("BACK", backRel);
		s0_card_details.put("PROFILE", profileRel);
		
        System.out.println(frontRel);
        System.out.println(backRel);
        System.out.println(profileRel);
	}

	// Driver code
	public static void main(String[] args)
			throws InterruptedException, NoSuchAlgorithmException, WriterException, IOException {
		MainUI mui = new MainUI();
		mui.startUI();
	}

	private JPanel menu = new JPanel();
	private String s0_chosen_pkey;
	private HashMap<String, String> s0_card_details;

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

		JLabel header = new JLabel(
				"<html><h1>Generate a DSID<sup>*</sup></h1> <br>" + "<h3><sup>*</sup>Dark Society ID</h3> </html>");
		panel.add(header);

		JComboBox<String> dropdown = new JComboBox<String>(pkeysDrop);
		panel.add(dropdown);


		JLabel label_FN = new JLabel("First"); panel.add(label_FN);
		JTextField input_FN = new JTextField(20); panel.add(input_FN);

		JLabel label_LN = new JLabel("Last"); panel.add(label_LN);
		JTextField input_LN = new JTextField(20); panel.add(input_LN);

		JLabel label_JOB = new JLabel("Role"); panel.add(label_JOB);
		JTextField input_JOB = new JTextField(20); panel.add(input_JOB);

		JLabel label_DOB = new JLabel("Date of birth"); panel.add(label_DOB);
		JTextField input_DOB = new JTextField(20); panel.add(input_DOB);

		JLabel label_EYES = new JLabel("Eye color"); panel.add(label_EYES);
		JTextField input_EYES = new JTextField(20); panel.add(input_EYES);

		JLabel label_HAIR = new JLabel("Hair color"); panel.add(label_HAIR);
		JTextField input_HAIR = new JTextField(20); panel.add(input_HAIR);

		JLabel label_HEIGHT = new JLabel("Height"); panel.add(label_HEIGHT);
		JTextField input_HEIGHT = new JTextField(20); panel.add(input_HEIGHT);

		JLabel label_PHOTO = new JLabel("Photo"); panel.add(label_PHOTO);
		JTextField input_PHOTO = new JTextField(20); panel.add(input_PHOTO);

		JLabel label_LICENSE = new JLabel("License"); panel.add(label_LICENSE);
		JTextField input_LICENSE = new JTextField(20); panel.add(input_LICENSE);

		JLabel label_EXPIRES = new JLabel("Expires"); panel.add(label_EXPIRES);
		JTextField input_EXPIRES = new JTextField(20); panel.add(input_EXPIRES);

		JButton submit = new JButton("Generate");
		submit.addActionListener((e) -> {
			// Find chosen key
			s0_chosen_pkey = pkeys[dropdown.getSelectedIndex()];
			scene = PREVIEW;
			
			s0_card_details.put("FN", input_FN.getText());
			s0_card_details.put("LN", input_LN.getText());
			s0_card_details.put("JOB", input_JOB.getText());
			s0_card_details.put("DOB", input_DOB.getText());
			s0_card_details.put("EYES", input_EYES.getText());
			s0_card_details.put("HAIR", input_HAIR.getText());
			s0_card_details.put("HEIGHT", input_HEIGHT.getText());
			s0_card_details.put("PHOTO", input_PHOTO.getText());
			s0_card_details.put("LICENSE", input_LICENSE.getText());
			s0_card_details.put("EXPIRES", input_EXPIRES.getText());

			try {
				menu.setVisible(false);

				preview.setVisible(true);
				initPreview(preview);
				frame.add(preview);
				frame.pack();
			} catch (NoSuchAlgorithmException | WriterException | IOException e1) {
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

		BufferedImage[] qrCodes = Generator.createQR(s0_chosen_pkey, QR_FRONT_PATH,
				QR_BACK_PATH);
		Path template = Paths.get("src/main/resources/id-card.svg"); // Template image filepath

		String content = new String(Files.readAllBytes(template), StandardCharsets.UTF_8);
		content = content.replaceAll("\\$FN", s0_card_details.get("FN"));
		content = content.replaceAll("\\$LN", s0_card_details.get("LN"));
		content = content.replaceAll("\\$JOB", s0_card_details.get("JOB"));
		content = content.replaceAll("\\$DOB", s0_card_details.get("DOB"));
		content = content.replaceAll("\\$EYES", s0_card_details.get("EYES"));
		content = content.replaceAll("\\$HAIR", s0_card_details.get("HAIR"));
		content = content.replaceAll("\\$HEIGHT", s0_card_details.get("HEIGHT"));
		content = content.replaceAll("\\$PHOTO", s0_card_details.get("PHOTO"));
		content = content.replaceAll("\\$LICENSE", s0_card_details.get("LICENSE"));
		content = content.replaceAll("\\$EXPIRES", s0_card_details.get("EXPIRES"));
		content = content.replaceAll("\\$FRONT", s0_card_details.get("FRONT"));
		content = content.replaceAll("\\$BACK", s0_card_details.get("BACK"));
		content = content.replaceAll("\\$PROFILE", s0_card_details.get("PROFILE")); // TODO let user do cropping

		try (FileWriter fw = new FileWriter(ID_CARD_TEMP_PATH)) {
			fw.write(content);
		} catch (IOException e) {
			System.err.println("ERROR WHILE CREATING FILE "+ID_CARD_TEMP_PATH.toString()+":");
			e.printStackTrace();
		}

		JLabel header = new JLabel("<html><h1>Result</h1><br>");
		preview.add(header);

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
