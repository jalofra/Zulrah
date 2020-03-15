package elfZulrah;

import java.util.Arrays;

import net.runelite.api.coords.WorldPoint;
import simple.hooks.scripts.task.Task;
import simple.hooks.simplebot.ChatMessage;
import simple.hooks.wrappers.SimpleGroundItem;
import simple.hooks.wrappers.SimpleItem;
import simple.hooks.wrappers.SimpleNpc;
import simple.hooks.wrappers.SimpleObject;
import simple.robot.api.ClientContext;

/**
 * @author Elf
 *
 */

class PrepFight extends Task {

	public static boolean ready = false;
	public static boolean replenished = false;
	private int bankFail = 0;
	WorldPoint bTile = new WorldPoint(3089, 3495, 0);

	public PrepFight(ClientContext ctx) {
		super(ctx);
	}

	@Override
	public boolean condition() {
		return !Utils.PLAYER_DIED && Fight.isDead && Utils.COLLECTED_ITEMS
				|| !Utils.PLAYER_DIED && !Fight.started && Utils.COLLECTED_ITEMS;
	}

	@Override
	public void run() {
		SimpleNpc banker = ctx.npcs.populate().filter(Utils.BANKER).filter(bTile).next();
		SimpleItem teleTab = ctx.inventory.populate().filter(Utils.TELEPORT_TAB).next();
		SimpleItem desertAmmy = ctx.inventory.populate().filter(Utils.DESERT_AMMY).next();
		SimpleItem food = ctx.inventory.populate().filter(Utils.FOOD).next();
		SimpleObject portal = ctx.objects.populate().filter(Utils.PORTAL).next();
		SimpleObject zulTeleport = ctx.objects.populate().filter(Utils.ZUL_TELEPORT).next();
		SimpleObject boat = ctx.objects.populate().filter(Utils.SACRIFICE_BOAT).next();
		SimpleObject restoreBox = ctx.objects.populate().filter(Utils.RESTORE_BOX).next();
		SimpleObject desertStatue = ctx.objects.populate().filter(Utils.DESERT_STATUE).next();

		if (Utils.USE_DESERT && !replenished) {
			if (desertRestore(ctx, desertStatue)) {
				teleTab.click(1);
			}
		} else {
			if (!ctx.inventory.populate().filter(Utils.FOOD_IDS).isEmpty() && ctx.combat.healthPercent() < 99) {
				food.click(1);
			}
		}
		if (banker != null) {
			if (!ready) {
				if (replenished) {
					if (ctx.bank.bankOpen()) {
						if (ctx.bank.depositInventory()) {
							restockInventory(ctx, Utils.OG_INVENTORY);
						}
					} else {
						if (banker.validateInteractable()) {
							banker.click("Bank");
							ctx.updateStatus("Banking");
						}
					}
				} else {
					restoreBox(ctx, restoreBox);
				}
			} else {
				if (ctx.bank.bankOpen())
					ctx.bank.closeBank();
			}
		}
		if (ready && !ctx.bank.bankOpen()) {

			if (portal != null) {
				if (ctx.players.getLocal().distanceTo(portal) < 6) {
					portal.click("Teleport-previous");
					ctx.updateStatus("To Zulrah");
				} else {
					ctx.pathing
							.step(new WorldPoint(portal.getLocation().getX() - 1, portal.getLocation().getY() - 4, 0));
					ctx.sleepCondition(() -> ctx.players.getLocal().distanceTo(portal) < 6);
				}
			}
		}

		if (boat != null) {
			if (boat.validateInteractable() && !ctx.dialogue.dialogueOpen()) {
				boat.click("Board");
				ctx.sleepCondition(() -> ctx.dialogue.dialogueOpen());
			}
			if (ctx.dialogue.dialogueOpen()) {
				ctx.dialogue.clickDialogueOption(1);
				Fight.isDead = false;
				ctx.sleep(1500);

			}
		}
		if (ctx.dialogue.dialogueOpen()) {
			if (ctx.dialogue.canContinue()) {
				ctx.dialogue.clickContinue();
				replenished = false;
			}
		}
		if (zulTeleport != null) {
			if (checkLoot(ctx, Zulrah.DROP_TABLE)) {
				ready = false;
				System.out.println("Teleport after looting");
				if (Utils.USE_DESERT) {
					desertAmmy.click("Nardah");
					ctx.updateStatus("Healing");
				} else {
					teleTab.click(1);
				}
				ctx.sleep(2500);
			}
		}

	}

	/**
	 * @param ctx
	 * @param ogInventory
	 */
	private boolean restockInventory(ClientContext ctx, int[] ogInventory) {
		System.out.println("ogInventory: " + ogInventory.length);
		if (ctx.inventory.populate().filter(Zulrah.DROP_TABLE).isEmpty()
				&& ctx.inventory.populate().filter(Zulrah.SUPPLY_DROPS).isEmpty()) {
			for (int i = 0; i < ogInventory.length; i++) {
				if (ogInventory[i] == Utils.FOOD) {
					ctx.bank.withdraw(ogInventory[i], Utils.FOOD_AMOUNT);
					i += (Utils.FOOD_AMOUNT - 1);
				} else {
					ctx.bank.withdraw(ogInventory[i], 1);
				}
			}
			if (checkInventory()) {
				bankFail = 0;
				ready = true;
			} else {
				System.out.println("Error while Banking.");
			}
		} else {
			ctx.bank.depositInventory();
		}
		return ready;
	}

	/**
	 * @return
	 */
	private boolean checkInventory() {
		Utils.CURR_INVENTORY = null;
		ctx.inventory.populate();
		int invSize = ctx.inventory.populate().size();
		Utils.CURR_INVENTORY = new int[invSize];
		for (int i = 0; i < invSize; i++) {
			SimpleItem a = ctx.inventory.next();
			Utils.CURR_INVENTORY[i] = a.getId();
		}
		if (Arrays.equals(Utils.OG_INVENTORY, Utils.CURR_INVENTORY)) {
			System.out.println("Same Invises");
			return true;
		} else {
			if (bankFail < 3) {
				System.out.println("Not same");
				bankFail++;
				ctx.bank.depositInventory();
				restockInventory(ctx, Utils.OG_INVENTORY);
				return false;
			} else {
				ctx.updateStatus("Original inventory supplies missing. Stopping.");
				ctx.stopScript();
				return false;
			}
		}

	}

	/*private void handleRecoil(ClientContext ctx, SimpleItem recoil) {
		if (ctx.bank.bankOpen()) {
			ctx.bank.closeBank();
		} else {
			recoil.click(1);
		}
	
	}*/

	public static boolean checkLoot(ClientContext ctx, String[] lootIDs) {
		boolean looted = false;
		if (!ctx.groundItems.populate().filter(lootIDs).isEmpty()) {
			Utils.swapGear(ctx, Utils.MAIN_GEAR);
			for (SimpleGroundItem item : ctx.groundItems) {
				if (item != null) {
					if (ctx.inventory.inventoryFull()) {
						if (!ctx.inventory.populate().filter(Utils.FOOD_IDS).isEmpty()) {
							SimpleItem food = ctx.inventory.filter(Utils.FOOD).next();
							food.click(1);
						}
					} else {
						if (item.getName().contains("Tanzanite fang"))
							Utils.tanzFang += 1;
						if (item.getName().contains("Serpentine"))
							Utils.serpVisage += 1;
						if (item.getName().contains("Magic fang"))
							Utils.magicFang += 1;

						item.click(1);
						ctx.sleep(900);
					}
				}
			}
		} else {
			looted = true;
		}
		return looted;
	}

	public static boolean restoreBox(ClientContext ctx, SimpleObject restoreBox) {
		if (restoreBox != null) {
			if (restoreBox.validateInteractable()) {
				restoreBox.click(1);
				ctx.sleep(2000);
				if (ctx.players.getLocal().distanceTo(restoreBox) <= 2
						|| ctx.players.getLocal().getGraphic() == Utils.RESTORE_BOX_GFX) {
					ctx.sleep(1000);
					replenished = true;
				}
				return true;
			}
		}
		return false;
	}

	public static boolean desertRestore(ClientContext ctx, SimpleObject statue) {
		if (statue != null) {
			if (statue.validateInteractable()) {
				statue.click("Pray-at");
				ctx.sleepCondition(() -> ctx.players.getLocal().distanceTo(statue) < 2);
				if (ctx.players.getLocal().getAnimation() == Utils.DESERT_STATUE_ANIM) {
					replenished = true;
				}
				return replenished;
			}
		}
		return replenished;
	}

	@Override
	public String status() {
		return "Looting";
	}

	public void onChatMessage(ChatMessage arg0) {
	}

}
