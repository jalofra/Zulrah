package elfZulrah;

import java.util.ArrayList;

import net.runelite.api.coords.WorldPoint;
import simple.hooks.filters.SimplePrayers.Prayers;
import simple.hooks.wrappers.SimpleItem;
import simple.robot.api.ClientContext;

/**
 * @author Elf
 *
 */
class Utils {
	public static boolean PLAYER_DIED = false;
	public static boolean COLLECTED_ITEMS = true;

	// OBJECTS + NPCS
	public static final int PORTAL = 35000;// Teleport-previous
	public static final int PRIESTESS = 1617;
	public static final int SACRIFICE_BOAT = 10068; // Board
	public static final int ZUL_TELEPORT = 11701;
	public static final int VENOM_CLOUD = 11700;
	public static final int RESTORE_BOX = 35001;
	public static final int DESERT_STATUE = 10389;
	public static final int DESERT_AMMY = 13136;
	public static final int LUMB_FOUNTAIN = 879;

	// ITEMS + AMOUNTS
	public static final int RECOIL = 2550;
	public static final int FOOD_AMT = 0;
	public static final int RUNE_POUCH = 12791;
	public static final int TELEPORT_TAB = 22721;

	// ANIMS + GFX
	public static final int RESTORE_BOX_GFX = 1177;
	public static final int VENG_GFX = 726;
	public static final int VENG_ANIM = 8147;
	public static final int DESERT_STATUE_ANIM = 645;

	// ARRAYS
	public static final int[] FOOD_IDS = { 13441, 385, 7946, 391, 11936, 7060, 397 };
	public static final int[] PRAYER_RESTORES = { 143, 141, 139, 2434, 3030, 3028, 3026, 3024 };
	public static final int[] RANGE_POTS = { 173, 171, 169, 2444 };
	public static final int[] MAGE_POTS = { 13046, 3044, 3042, 3040 };
	public static final int[] ANTIVENOMS = { 12919, 12917, 12915, 12913, 12911, 12909, 12907, 12905 };
	public static final int[] BANKER = { 2117, 2117 };
	public static final int[] PLAYER_ANIMS = { 1167, 7552, 829, 5061 };

	// GUI STUFF
	public static int[] OG_INVENTORY;
	public static int[] CURR_INVENTORY;
	public static int[] RANGE_SWITCH = new int[4];
	public static int[] MAIN_GEAR;

	// GUI SETTINGS
	public static boolean USE_VENG = false;
	public static boolean USE_DESERT = false;
	public static boolean USE_SERP = false;
	public static boolean USE_RECOIL = false;
	public static boolean USE_AUGURY = false;
	public static boolean USE_RIGOUR = false;

	public static String pointRotName = " ";
	public static int magicFang = 0;
	public static int serpVisage = 0;
	public static int tanzFang = 0;
	public static int FOOD_AMOUNT = 0;
	public static int FOOD = 0;

	public static String nA1 = "";
	public static String nA2 = "";
	public static String wlc = "";

	public static Prayers magicPrayer() {
		if (USE_AUGURY) {
			return Prayers.AUGURY;
		} else {
			return Prayers.MYSTIC_MIGHT;
		}
	}

	public static Prayers rangePrayer() {
		if (USE_RIGOUR) {
			return Prayers.RIGOUR;
		} else {
			return Prayers.EAGLE_EYE;
		}
	}

	public static ArrayList<WorldPoint> createPointArray(WorldPoint playerSpawn, String rotName) {
		ArrayList<WorldPoint> rotationPoints = new ArrayList<WorldPoint>();

		WorldPoint sw1 = new WorldPoint(playerSpawn.getX() + 4, playerSpawn.getY() + 10, 0);
		WorldPoint sw2 = new WorldPoint(playerSpawn.getX() + 6, playerSpawn.getY() + 9, 0);
		WorldPoint w1 = new WorldPoint(playerSpawn.getX() + 5, playerSpawn.getY() + 4, 0);
		WorldPoint n1 = new WorldPoint(playerSpawn.getX() + 2, playerSpawn.getY() + 1, 0);
		WorldPoint e1 = new WorldPoint(playerSpawn.getX() - 5, playerSpawn.getY() + 4, 0);
		WorldPoint se1 = new WorldPoint(playerSpawn.getX() - 4, playerSpawn.getY() + 10, 0);
		WorldPoint se2 = new WorldPoint(playerSpawn.getX() - 6, playerSpawn.getY() + 8, 0);

		if (rotName.contains("Alpha")) {
			rotationPoints.clear();
			rotationPoints.add(sw1);// phase 1 + 2
			rotationPoints.add(sw2);// phase 2 dodge, phase 3
			rotationPoints.add(e1); // phase 4, phase 5, phase 6
			rotationPoints.add(w1); // phase 7
			rotationPoints.add(e1); // phase 8
			rotationPoints.add(se1);// phase 9, phase 10
			rotationPoints.add(se2);// phase 10 dodge
			pointRotName = "Alpha";

		} else if (rotName.contains("Bravo")) {
			rotationPoints.clear();
			rotationPoints.add(sw1);// phase 1 + 2
			rotationPoints.add(sw2);// phase 2 dodge, phase 3
			rotationPoints.add(e1); // phase 4, phase 5, phase 6
			rotationPoints.add(n1); // phase 7
			rotationPoints.add(e1); // phase 8
			rotationPoints.add(se1);// phase 9, phase 10
			rotationPoints.add(se2);// phase 10 dodge
			pointRotName = "Bravo";
		} else if (rotName.contains("Charlie")) {
			rotationPoints.clear();
			rotationPoints.add(sw1);// phase 1
			rotationPoints.add(sw2);// phase 2, phase 3 dodge walk around
			rotationPoints.add(se1); // phase 3, phase 4
			rotationPoints.add(w1); // phase 5, phase 6
			rotationPoints.add(e1); // phase 7, phase 8
			rotationPoints.add(w1);// phase 9, phase 10
			rotationPoints.add(sw1);// phase 11
			rotationPoints.add(se2); // added later, for phase 3 melee dodge
			pointRotName = "Charlie";
		} else if (rotName.contains("Delta")) {
			rotationPoints.clear();
			rotationPoints.add(sw1);// phase 1 + 2
			rotationPoints.add(e1); // phase 3, phase 4
			rotationPoints.add(w1); // phase 5 + 6
			rotationPoints.add(e1); // phase 7, phase 8
			rotationPoints.add(w1);// phase 9, phase 10, phase 11
			rotationPoints.add(sw1);// phase 12
			pointRotName = "Delta";
		}

		return rotationPoints;
	}

	public static boolean swapGear(ClientContext ctx, int[] gearIds) {
		if (!ctx.inventory.populate().filter(gearIds).isEmpty()) {
			for (SimpleItem item : ctx.inventory) {
				item.click(1);
			}
			ctx.sleep(700);
			return true;
		}
		return false;
	}

	public static String flN = "";
}
