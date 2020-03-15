package elfZulrah;

import java.awt.Cursor;
import java.awt.Image;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JToggleButton;
import javax.swing.border.BevelBorder;

/**
 *
 * @author Elf
 */
public class ZulrahGUI extends JFrame {
	public ElfZulrah ctx;
	Image helmImg = null;
	Image capeImg = null;
	Image neckImg = null;
	Image ammoImg = null;
	Image wepImg = null;
	Image bodyImg = null;
	Image shieldImg = null;
	Image legsImg = null;
	Image glovesImg = null;
	Image bootsImg = null;
	Image ringImg = null;
	Image selectImg = null;
	Image range1Img = null;
	Image range2Img = null;
	Image range3Img = null;
	Image range4Img = null;

	private static final long serialVersionUID = 8699458653601470371L;

	/**
	 * Creates new form ZulrahGUI
	 * 
	 * @throws IOException
	 */
	public ZulrahGUI(ElfZulrah main) throws IOException {
		this.ctx = main;
		initComponents();
	}

	public static int[] mainGearIds = new int[11];
	public static int[] rangeGearIds = new int[4];

	public void resetImages() {
		helmImg = null;
		capeImg = null;
		neckImg = null;
		ammoImg = null;
		wepImg = null;
		bodyImg = null;
		shieldImg = null;
		legsImg = null;
		glovesImg = null;
		bootsImg = null;
		ringImg = null;
		selectImg = null;
		range1Img = null;
		range2Img = null;
		range3Img = null;
		range4Img = null;
	}

	public void initComponents() throws MalformedURLException {
		ArrayList<URL> urlList = new ArrayList<URL>();

		try {
			for (int i = 0; i < mainGearIds.length; i++) {
				URL urlString = new URL("https://everythingrs.com/img/gameitem/" + mainGearIds[i] + ".png");

				urlList.add(urlString);
			}
			helmImg = ImageIO.read(urlList.get(0));
			capeImg = ImageIO.read(urlList.get(1));
			neckImg = ImageIO.read(urlList.get(2));
			wepImg = ImageIO.read(urlList.get(3));
			bodyImg = ImageIO.read(urlList.get(4));
			shieldImg = ImageIO.read(urlList.get(5));
			legsImg = ImageIO.read(urlList.get(6));
			glovesImg = ImageIO.read(urlList.get(7));
			bootsImg = ImageIO.read(urlList.get(8));
			ringImg = ImageIO.read(urlList.get(9));
			ammoImg = ImageIO.read(urlList.get(10));

		} catch (IOException ee) {
			ee.printStackTrace();
		}

		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		auguryToggle = new JToggleButton();
		rigourToggle = new JToggleButton();
		vengToggle = new JToggleButton();
		desertToggle = new JToggleButton();
		wornHelm = new JLabel();
		wornCape = new JLabel();
		wornNeck = new JLabel();
		wornWep = new JLabel();
		wornBody = new JLabel();
		wornShield = new JLabel();
		wornLegs = new JLabel();
		wornGloves = new JLabel();
		wornBoots = new JLabel();
		wornAmmo = new JLabel();
		wornRing1 = new JLabel();
		switch1 = new JLabel();
		switch2 = new JLabel();
		switch3 = new JLabel();
		switch4 = new JLabel();
		startButton = new JButton();
		loadInvButton = new JButton();
		loadWornButton = new JButton();
		backgroundLabel = new JLabel();

		//setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setMinimumSize(new java.awt.Dimension(415, 540));
		getContentPane().setLayout(null);

		auguryToggle.setToolTipText(
				"<html>\n Uses the Augury prayer if toggled\n<br>if not then uses Mystic Might\n</html>");
		auguryToggle.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		auguryToggle.setBorderPainted(false);
		auguryToggle.setContentAreaFilled(false);
		auguryToggle.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
		auguryToggle.setIcon(new ImageIcon(ZulrahGUI.class.getResource("/elfZulrah/redSquare.png"))); //
		auguryToggle.setSelectedIcon(new ImageIcon(ZulrahGUI.class.getResource("/elfZulrah/greenSquare.png"))); //
		getContentPane().add(auguryToggle);
		auguryToggle.setBounds(30, 120, 50, 50);

		rigourToggle
				.setToolTipText("<html>\n Uses the Rigour prayer if toggled\n<br>if not then uses Eagle Eye\n</html>");
		rigourToggle.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		rigourToggle.setBorderPainted(false);
		rigourToggle.setContentAreaFilled(false);
		rigourToggle.setCursor(new Cursor(Cursor.HAND_CURSOR));
		rigourToggle.setIcon(new ImageIcon(ZulrahGUI.class.getResource("/elfZulrah/redSquare.png"))); //
		rigourToggle.setSelectedIcon(new ImageIcon(ZulrahGUI.class.getResource("/elfZulrah/greenSquare.png"))); //
		rigourToggle.addActionListener(e -> {

		});
		getContentPane().add(rigourToggle);
		rigourToggle.setBounds(30, 180, 50, 50);

		vengToggle.setToolTipText(
				"<html>\nif using vengeance make sure\n<br>to have your runepouch in your\n<br>inventory with plenty of veng\n<br>runes stored inside of it\n</html>");
		vengToggle.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		vengToggle.setBorderPainted(false);
		vengToggle.setContentAreaFilled(false);
		vengToggle.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
		vengToggle.setIcon(new ImageIcon(ZulrahGUI.class.getResource("/elfZulrah/redSquare.png")));
		vengToggle.setSelectedIcon(new ImageIcon(ZulrahGUI.class.getResource("/elfZulrah/greenSquare.png")));
		getContentPane().add(vengToggle);
		vengToggle.setBounds(30, 240, 50, 50);

		desertToggle.setFont(new java.awt.Font("Arial", 0, 10));
		desertToggle.setToolTipText(
				"<html>\nif you have the desert amulet 4\n<br>your player will replenish their\n<br>prayer and health at Nardah \n</html>");
		desertToggle.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		desertToggle.setBorderPainted(false);
		desertToggle.setContentAreaFilled(false);
		desertToggle.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
		desertToggle.setIcon(new ImageIcon(ZulrahGUI.class.getResource("/elfZulrah/redSquare.png"))); //
		desertToggle.setSelectedIcon(new ImageIcon(ZulrahGUI.class.getResource("/elfZulrah/greenSquare.png"))); //
		getContentPane().add(desertToggle);
		desertToggle.setBounds(30, 300, 50, 50);

		wornHelm.setBackground(new java.awt.Color(102, 102, 102));
		getContentPane().add(wornHelm);
		wornHelm.setBounds(170, 160, 32, 32);

		wornCape.setBackground(new java.awt.Color(102, 102, 102));
		getContentPane().add(wornCape);
		wornCape.setBounds(130, 200, 32, 32);

		wornNeck.setBackground(new java.awt.Color(102, 102, 102));
		getContentPane().add(wornNeck);
		wornNeck.setBounds(170, 200, 32, 32);

		wornWep.setBackground(new java.awt.Color(102, 102, 102));
		getContentPane().add(wornWep);
		wornWep.setBounds(120, 240, 32, 32);

		wornBody.setBackground(new java.awt.Color(102, 102, 102));
		getContentPane().add(wornBody);
		wornBody.setBounds(170, 240, 32, 32);

		wornShield.setBackground(new java.awt.Color(102, 102, 102));
		getContentPane().add(wornShield);
		wornShield.setBounds(220, 240, 32, 32);

		wornLegs.setBackground(new java.awt.Color(102, 102, 102));
		getContentPane().add(wornLegs);
		wornLegs.setBounds(170, 280, 32, 32);

		wornGloves.setBackground(new java.awt.Color(102, 102, 102));
		getContentPane().add(wornGloves);
		wornGloves.setBounds(120, 320, 32, 32);

		wornBoots.setBackground(new java.awt.Color(102, 102, 102));
		getContentPane().add(wornBoots);
		wornBoots.setBounds(170, 320, 32, 32);

		wornAmmo.setBackground(new java.awt.Color(102, 102, 102));
		getContentPane().add(wornAmmo);
		wornAmmo.setBounds(210, 200, 32, 32);

		wornRing1.setBackground(new java.awt.Color(102, 102, 102));
		getContentPane().add(wornRing1);
		wornRing1.setBounds(220, 320, 32, 32);

		switch1.setBackground(new java.awt.Color(102, 102, 102));
		getContentPane().add(switch1);
		switch1.setBounds(300, 140, 32, 32);

		switch2.setBackground(new java.awt.Color(102, 102, 102));
		getContentPane().add(switch2);
		switch2.setBounds(340, 140, 32, 32);

		switch3.setBackground(new java.awt.Color(102, 102, 102));
		getContentPane().add(switch3);
		switch3.setBounds(300, 180, 32, 32);

		switch4.setBackground(new java.awt.Color(102, 102, 102));
		getContentPane().add(switch4);
		switch4.setBounds(340, 180, 32, 32);

		startButton.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
		startButton.setForeground(new java.awt.Color(255, 255, 255));
		startButton.setText("Start");
		startButton.setBorderPainted(false);
		startButton.setContentAreaFilled(false);

		startButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
		getContentPane().add(startButton);
		startButton.setBounds(300, 300, 72, 60);

		loadInvButton.setForeground(new java.awt.Color(0, 0, 0));
		loadInvButton.setText("Load ");
		loadInvButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
		getContentPane().add(loadInvButton);
		loadInvButton.setBounds(300, 230, 73, 25);
		loadInvButton.addActionListener(e -> {
			urlList.clear();
			HttpURLConnection[] hC = new HttpURLConnection[4];
			try {
				for (int i = 0; i < rangeGearIds.length; i++) {
					URL urlString = new URL("https://www.osrsbestinslot.com/img/items/" + rangeGearIds[i] + ".png");
					urlList.add(urlString);
					hC[i] = (HttpURLConnection) urlList.get(i).openConnection();
					hC[i].addRequestProperty("User-Agent", "");
				}
				range1Img = ImageIO.read(hC[0].getInputStream());
				range2Img = ImageIO.read(hC[1].getInputStream());
				range3Img = ImageIO.read(hC[2].getInputStream());
				range4Img = ImageIO.read(hC[3].getInputStream());

			} catch (IOException ee) {
				ee.printStackTrace();
			}

			switch1.setIcon(new ImageIcon(range1Img));
			switch2.setIcon(new ImageIcon(range2Img));
			switch3.setIcon(new ImageIcon(range3Img));
			switch4.setIcon(new ImageIcon(range4Img));

		});
		loadWornButton.setForeground(new java.awt.Color(0, 0, 0));
		loadWornButton.setText("Load ");
		loadWornButton.setBorderPainted(false);
		loadWornButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
		getContentPane().add(loadWornButton);
		loadWornButton.setBounds(150, 370, 73, 25);
		loadWornButton.addActionListener(e -> {
			urlList.clear();
			try {
				for (int i = 0; i < mainGearIds.length; i++) {
					URL urlString = new URL("https://everythingrs.com/img/gameitem/" + mainGearIds[i] + ".png");
					urlList.add(urlString);
				}
				helmImg = ImageIO.read(urlList.get(0));
				capeImg = ImageIO.read(urlList.get(1));
				neckImg = ImageIO.read(urlList.get(2));
				wepImg = ImageIO.read(urlList.get(3));
				bodyImg = ImageIO.read(urlList.get(4));
				shieldImg = ImageIO.read(urlList.get(5));
				legsImg = ImageIO.read(urlList.get(7));
				glovesImg = ImageIO.read(urlList.get(9));
				bootsImg = ImageIO.read(urlList.get(10));
				ringImg = ImageIO.read(urlList.get(6));
				ammoImg = ImageIO.read(urlList.get(8));

			} catch (IOException ee) {
				ee.printStackTrace();
			}

			wornHelm.setIcon(new ImageIcon(helmImg));
			wornCape.setIcon(new ImageIcon(capeImg));
			wornNeck.setIcon(new ImageIcon(neckImg));
			wornWep.setIcon(new ImageIcon(wepImg));
			wornBody.setIcon(new ImageIcon(bodyImg));
			wornShield.setIcon(new ImageIcon(shieldImg));
			wornLegs.setIcon(new ImageIcon(legsImg));
			wornGloves.setIcon(new ImageIcon(glovesImg));
			wornBoots.setIcon(new ImageIcon(bootsImg));
			wornAmmo.setIcon(new ImageIcon(ammoImg));
			wornRing1.setIcon(new ImageIcon(ringImg));

		});
		backgroundLabel.setIcon(new ImageIcon(ZulrahGUI.class.getResource("/elfZulrah/ZulrahGui.png"))); // NOI18N
		backgroundLabel.setText("");
		getContentPane().add(backgroundLabel);
		backgroundLabel.setBounds(0, 0, 400, 500);

		pack();
	}

	public static JToggleButton desertToggle;
	public static JToggleButton rigourToggle;
	public static JToggleButton auguryToggle;
	public static JToggleButton vengToggle;

	public JButton loadInvButton;
	public JButton loadWornButton;
	public JButton startButton;
	private JLabel backgroundLabel;
	private JLabel switch1;
	private JLabel switch2;
	private JLabel switch4;
	private JLabel switch3;
	private JLabel wornAmmo;
	private JLabel wornBody;
	private JLabel wornBoots;
	private JLabel wornCape;
	private JLabel wornGloves;
	private JLabel wornHelm;
	private JLabel wornLegs;
	private JLabel wornNeck;
	private JLabel wornRing1;
	private JLabel wornShield;
	private JLabel wornWep;

	public boolean startClicked;

}
