package elfZulrah;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Elf
 *
 */
public class Zulrah {

	// NPC IDS
	public final static int GREEN = 2042;
	public final static int RED = 2043;
	public final static int BLUE = 2044;

	// ANIMATIONS
	public static final int IDLE = -1;
	public static final int EMERGE = 5073;
	public static final int HIDE = 5072;
	public static final int SPAWN = 5071;
	public static final int ATTACK = 5069;
	public static final int MELEE_ATTACK = 5806;
	public static final int MELEE_ATTACK2 = 5807;
	public static final int DEATH = 5804;

	// PROJECTILES
	public static final int MINI_SPAWN = 1047;
	public static final int RANGE_ATTACK = 1044;
	public static final int MAGIC_ATTACK = 1046;
	public static final int VENOM_CLOUD = 1045;

	// ROTATIONS
	public static int[] A_ROTATION = { GREEN, RED, BLUE, GREEN, RED, BLUE, GREEN, BLUE, GREEN, RED };
	public static int[] B_ROTATION = { GREEN, RED, BLUE, GREEN, BLUE, RED, GREEN, BLUE, GREEN, RED };
	public static int[] C_ROTATION = { GREEN, GREEN, RED, BLUE, GREEN, BLUE, GREEN, GREEN, BLUE, GREEN, BLUE };
	public static int[] D_ROTATION = { GREEN, BLUE, GREEN, BLUE, RED, GREEN, GREEN, BLUE, GREEN, BLUE, GREEN, BLUE };

	// LOOT ITEMS
	public static final String[] DROP_TABLE = { "Zulrah's scales", "Tanzanite fang", "Magic fang", "Serpentine visage",
			"Uncut onyx", "Tanzanite mutagen", "Magma mutagen", "Battlestaff", "Dragon med helm", "Dragon halberd",
			"Death rune", "Law rune", "Chaos rune", "Snapdragon", "Dwarf weed", "Toadflax", "Torstol", "Palm tree seed",
			"Papaya tree seed", "Magic seed", "Toadflax seed", "Snapdragon seed", "Dwarf weed seed", "Torstol seed",
			"Spirit seed", "Snakeskin", "Runite ore", "Pure essence", "Flax", "Yew logs", "Adamantite bar", "Coal",
			"Dragon bones", "Mahogany logs", "Zul-andra teleport", "Antidote++(4)", "Dragonstone bolt tips", "Grapes",
			"Coconut", "Coins", "Loop half of key", "Tooth half of key", "Runite bar", "Rune 2h sword",
			"Rune battleaxe", "Rune arrow", "Rune sq shield", "Dragon spear", "Shield left half", "Snape grass",
			"Mort myre fungus", "Dragon scale dust", "Red spiders' eggs", "Potato cactus", "White berries",
			"Crystal key", "Dragonstone", "Diamond", "Ruby", "Emerald", "Sapphire", "Limpwurt root", "Ruby bolts",
			"Diamond bolts", "Dragonstone bolts", "Grey chinchompa", "Red chinchompa", "Black chinchompa",
			"Pet snakeling", "Jar of swamp", "Clue scroll (elite)", "Brimstone key", "Crystal seed", "Crushed nest" };

	public static final int[] SUPPLY_DROPS = { 7061, 386, 398, 13442, 11937, 392, 12696, 12626, 12914, 10926, 21979,
			2435, 3025, 6686 };

	public static List<Integer> A_ROT = new ArrayList<Integer>(A_ROTATION.length);
	public static List<Integer> B_ROT = new ArrayList<Integer>(B_ROTATION.length);
	public static List<Integer> C_ROT = new ArrayList<Integer>(C_ROTATION.length);
	public static List<Integer> D_ROT = new ArrayList<Integer>(D_ROTATION.length);

	public static void addRotations() {
		for (int i : A_ROTATION)
			A_ROT.add(i);
		for (int i : B_ROTATION)
			B_ROT.add(i);
		for (int i : C_ROTATION)
			C_ROT.add(i);
		for (int i : D_ROTATION)
			D_ROT.add(i);
	}

	public static void clearRotations() {
		A_ROT.clear();
		B_ROT.clear();
		C_ROT.clear();
		D_ROT.clear();
	}

	public static String getAttackStyle(List<Integer> currRot, int x) {
		String style = " ";
		if (currRot.get(x - 1) == GREEN)
			style = "Ranged";
		if (currRot.get(x - 1) == BLUE)
			style = "Magic";
		if (currRot.get(x - 1) == RED)
			style = "Melee";
		return style;
	}

}
