package elfZulrah;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import net.runelite.api.coords.WorldPoint;
import simple.hooks.filters.SimplePrayers.Prayers;
import simple.hooks.filters.SimpleSkills.Skills;
import simple.hooks.scripts.task.Task;
import simple.hooks.wrappers.SimpleItem;
import simple.hooks.wrappers.SimpleNpc;
import simple.hooks.wrappers.SimpleWidget;
import simple.robot.api.ClientContext;

class Fight extends Task {

	public static List<Integer> currRotation = new ArrayList<Integer>();
	public static int currPhase = 0;
	public static int nextPhase = 0;
	public static int killCount = 0;

	public static boolean started = false;
	public static boolean doingJad = false;
	public static boolean phaseOver = false;
	public static boolean needToReset = false;
	public static boolean attack = false;
	public static boolean isDead = false;

	public static String currStyle = "None";
	public static String rotName = "None";
	public static String nextStyle = "None";

	public static WorldPoint playerSpawn = null;

	public static SimpleItem food = null;
	public static SimpleItem antiVenom = null;
	public static SimpleItem teleport = null;
	public static SimpleItem prayPot = null;
	public static SimpleItem magePot = null;
	public static SimpleItem rangePot = null;

	public Fight(ClientContext ctx) {
		super(ctx);
	}

	@Override
	public boolean condition() {
		return ctx.npcs.populate().filter("Zulrah").nearest().next() != null && !isDead;
	}

	@Override
	public void run() {
		SimpleNpc zulrah = ctx.npcs.populate().filter("Zulrah").next();
		if (zulrah != null) {
			started = true;
			food = ctx.inventory.populate().filter(Utils.FOOD_IDS).next();
			antiVenom = ctx.inventory.populate().filter(Utils.ANTIVENOMS).next();
			teleport = ctx.inventory.populate().filter(Utils.TELEPORT_TAB).next();
			prayPot = ctx.inventory.populate().filter(Utils.PRAYER_RESTORES).next();
			rangePot = ctx.inventory.populate().filter(Utils.RANGE_POTS).next();
			magePot = ctx.inventory.populate().filter(Utils.MAGE_POTS).next();

			handleSwitches(); // (ctx); TODO
			if (rotName.contains("Alpha")) {
				Alpha.initiate(zulrah, ctx);
			} else if (rotName.contains("Bravo")) {
				Bravo.initiate(zulrah, ctx);
			} else if (rotName.contains("Charlie")) {
				Charlie.initiate(zulrah, ctx);
			} else if (rotName.contains("Delta")) {
				Delta.initiate(zulrah, ctx);
			}
			if (!checkCondition(isVenomed(), antiVenom, Utils.ANTIVENOMS)
					|| !checkCondition(ctx.combat.healthPercent() < 75, food, Utils.FOOD_IDS)
					|| !checkCondition(ctx.prayers.points() < 10, prayPot, Utils.PRAYER_RESTORES)) {// Utils.PRAYER_RESTORES
				abortFight(teleport); // , ctx);
			}
			if (attack && currPhase != 1) {
				boolean result = IntStream.of(Utils.RANGE_SWITCH).anyMatch(x -> x == 12926);
				if (result && ctx.combat.getSpecialAttackPercentage() >= 50 && currStyle.contains("Magic")
						&& !ctx.combat.specialAttack() && ctx.combat.healthPercent() < 85) {
					ctx.widgets.getWidget(160, 35).click(1);
				}
				attackZulrah(zulrah);
			}

			if (zulrah.getAnimation() == Zulrah.SPAWN && currPhase == 0) {
				playerSpawn = ctx.players.getLocal().getLocation();
				currRotation.add(zulrah.getId());
				currPhase += 1;
				currStyle = Zulrah.getAttackStyle(currRotation, currPhase);
				System.out.println("Started.");
			}
			checkCondition(ctx.skills.level(Skills.MAGIC) <= ctx.skills.realLevel(Skills.MAGIC) + 1, magePot,
					Utils.MAGE_POTS);
			checkCondition(ctx.skills.level(Skills.RANGED) <= ctx.skills.realLevel(Skills.RANGED) + 7, rangePot,
					Utils.RANGE_POTS);

			if (zulrah.getAnimation() == Zulrah.DEATH && !isDead || zulrah.isDead() || zulrah.getHealth() == 0) {
				ctx.prayers.quickPrayers(true);
				ctx.prayers.quickPrayers(false);
				Utils.swapGear(ctx, Utils.MAIN_GEAR);
				synchronized (this) {
					killCount++;
				}
				resetRotation();
				started = false;
				isDead = true;
			}

			if (currPhase > 0) {
				if (currRotation.size() < getRotation(currRotation).size()) {
					nextPhase = getRotation(currRotation).get(currRotation.size());
				} else {
					nextPhase = 1;
				}
			}
		} else {
			started = false;
		}

	}

	private boolean checkCondition(boolean condition, SimpleItem item, int[] ids) {
		if (condition) {
			if (!ctx.inventory.populate().filter(ids).isEmpty()) {
				item.click(1);
				return true;
			} else {
				return false;
			}
		}
		return true;
	}

	boolean isVenomed() { 
		SimpleWidget healthOrb = ctx.widgets.getWidget(160, 6);
		return healthOrb.getSpriteId() == 1102;
	}

	private void abortFight(SimpleItem teleport) { // TODO, ClientContext ctx) {
		if (!ctx.inventory.populate().filter(teleport.getId()).isEmpty() && started) {
			Utils.swapGear(ctx, Utils.MAIN_GEAR);
			teleport.click(1);
			resetRotation();
			ctx.sleep(3000);
			started = false;
			PrepFight.ready = false;
		}

	}

	public void handleSwitches() {
		if (currStyle == "Ranged") {
			if (!doingJad) {
				if (!ctx.prayers.prayerActive(Prayers.PROTECT_FROM_MISSILES)) {
					ctx.prayers.prayer(Prayers.PROTECT_FROM_MISSILES, true);
				}
				if (!ctx.prayers.prayerActive(Utils.magicPrayer())) {
					ctx.prayers.prayer(Utils.magicPrayer(), true);
				}
			}
			Utils.swapGear(ctx, Utils.MAIN_GEAR);

		}
		if (currStyle == "Melee") {
			if (ctx.prayers.prayerActive(Prayers.PROTECT_FROM_MISSILES)) {
				ctx.prayers.prayer(Prayers.PROTECT_FROM_MISSILES, false);
			}
			if (ctx.prayers.prayerActive(Prayers.PROTECT_FROM_MAGIC)) {
				ctx.prayers.prayer(Prayers.PROTECT_FROM_MAGIC, false);
			}
			if (!ctx.prayers.prayerActive(Utils.magicPrayer())) {
				ctx.prayers.prayer(Utils.magicPrayer(), true);
			}
			Utils.swapGear(ctx, Utils.MAIN_GEAR);

		}
		if (currStyle == "Magic") {
			if (!ctx.prayers.prayerActive(Prayers.PROTECT_FROM_MAGIC)) {
				ctx.prayers.prayer(Prayers.PROTECT_FROM_MAGIC, true);
			}
			if (!ctx.prayers.prayerActive(Utils.rangePrayer())) {
				ctx.prayers.prayer(Utils.rangePrayer(), true);
			}

			Utils.swapGear(ctx, Utils.RANGE_SWITCH);
		}
	}

	public static boolean attackZulrah(SimpleNpc zulrah) {
		if (!phaseOver && !zulrah.isDead()) {
			if (zulrah.visibleOnScreen()) {
				zulrah.click("Attack");
			}
			attack = false;
			return true;
		}

		return false;
	}

	public static List<Integer> getRotation(List<Integer> currRot) {
		List<Integer> zulRot = new ArrayList<Integer>();
		if (currRot.size() > 0 && currRot.size() <= 4) {
			for (int j = currRot.size() - 1; j < currRot.size(); j++) {
				if (Zulrah.A_ROT.get(j).equals(currRot.get(j))) {
					zulRot.clear();
					zulRot.addAll(Zulrah.A_ROT);
					rotName = "Alpha";
				} else if (Zulrah.B_ROT.get(j).equals(currRot.get(j))) {
					zulRot.clear();
					zulRot.addAll(Zulrah.B_ROT);
					rotName = "Bravo";
				} else if (Zulrah.C_ROT.get(j).equals(currRot.get(j))) {
					zulRot.clear();
					zulRot.addAll(Zulrah.C_ROT);
					rotName = "Charlie";
				} else if (Zulrah.D_ROT.get(j).equals(currRot.get(j))) {
					zulRot.clear();
					zulRot.addAll(Zulrah.D_ROT);
					rotName = "Delta";
				}
			}
		}
		if (currRot.size() > 0 && currRot.size() > 4) {
			int j = 1;
			int i = 4;
			if (Zulrah.A_ROT.get(j).equals(currRot.get(j)) && Zulrah.A_ROT.get(i).equals(currRot.get(i))) {
				zulRot.clear();
				zulRot.addAll(Zulrah.A_ROT);
				rotName = "Alpha";
			} else if (Zulrah.B_ROT.get(j).equals(currRot.get(j)) && Zulrah.B_ROT.get(i).equals(currRot.get(i))) {
				zulRot.clear();
				zulRot.addAll(Zulrah.B_ROT);
				rotName = "Bravo";
			} else if (Zulrah.C_ROT.get(j).equals(currRot.get(j)) && Zulrah.C_ROT.get(i).equals(currRot.get(i))) {
				zulRot.clear();
				zulRot.addAll(Zulrah.C_ROT);
				rotName = "Charlie";
			} else if (Zulrah.D_ROT.get(j).equals(currRot.get(j)) && Zulrah.D_ROT.get(i).equals(currRot.get(i))) {
				zulRot.clear();
				zulRot.addAll(Zulrah.D_ROT);
				rotName = "Delta";
			}

		}
		return zulRot;
	}

	public static void handleJad(ClientContext ctx, boolean rangeFirst) {
		doingJad = true;
		if (rangeFirst) {
			if (!ctx.prayers.prayerActive(Prayers.PROTECT_FROM_MISSILES) && phaseOver) {
				ctx.prayers.prayer(Prayers.PROTECT_FROM_MISSILES);// Avoids first Ranged hit
			}
		} else {
			if (!ctx.prayers.prayerActive(Prayers.PROTECT_FROM_MAGIC) && phaseOver) {
				ctx.prayers.prayer(Prayers.PROTECT_FROM_MAGIC);// Avoids first Mage hit
			}
		}
		if (ctx.projectiles.projectileActive(Zulrah.RANGE_ATTACK)) {
			ctx.prayers.prayer(Prayers.PROTECT_FROM_MAGIC);
		} else if (ctx.projectiles.projectileActive(Zulrah.MAGIC_ATTACK)) {
			ctx.prayers.prayer(Prayers.PROTECT_FROM_MISSILES);
		}
	}

	public static void resetRotation() {
		System.out.println("Resetting Zulrah rotation");
		currPhase = 0;
		currRotation.clear();
		rotName = "None ";
		currStyle = "None ";
		nextStyle = "None";
		phaseOver = false;
		doingJad = false;
		started = false;
		needToReset = false;
	}

	public static boolean moveToPoint(ClientContext ctx, WorldPoint safeSpot) {
		if (!ctx.players.getLocal().getLocation().equals(safeSpot) && !ctx.pathing.inMotion()) {
			ctx.pathing.clickSceneTile(safeSpot, true, true);
			if (currPhase == 1) {
				ctx.sleep(400);
			} else {
				ctx.sleep(100);
			}
			return true;
		}
		return false;
	}

	public static void resetVariables() {
		currPhase = 0;
		nextPhase = 0;
		killCount = 0;
		started = false;
		doingJad = false;
		phaseOver = false;
		needToReset = false;
		attack = false;
		isDead = false;
	}

	@Override
	public String status() {
		return "Fighting";
	}

}
