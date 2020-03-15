package elfZulrah;

import simple.hooks.scripts.task.Task;
import simple.robot.api.ClientContext;
import simple.robot.managers.framework.LoginManager;

/**
 * @author Elf
 *
 */

public class PlayerDeath extends Task {

	public PlayerDeath(ClientContext ctx) {
		super(ctx);
	}

	@Override
	public boolean condition() {
		return Utils.PLAYER_DIED;
	}

	@Override
	public void run() {
		/*
		SimpleNpc priestess = ctx.npcs.populate().filter(Utils.PRIESTESS).nearest().next();
		SimpleObject portal = ctx.objects.populate().filter(Utils.PORTAL).next();
		
		List<String> menuOptions;
		Utils.swapGear(ctx, Utils.MAIN_GEAR);
		if (priestess != null) {
			menuOptions = Arrays.asList(priestess.getActions());
			if (menuOptions.contains("Collect")) {
				if (priestess.validateInteractable()) {
					priestess.click("Collect");
					ctx.sleepCondition(() -> ctx.players.getLocal().distanceTo(priestess) < 2);
					if (ctx.widgets.getWidget(602, 6).visibleOnScreen()) {
						ctx.widgets.getWidget(602, 6).click(1);
						Utils.swapGear(ctx, Utils.MAIN_GEAR);
						menuOptions.clear();
					}
				}
			} else {
				System.out.println("We collected all our shit yay.");
				Utils.COLLECTED_ITEMS = true;
				Utils.PLAYER_DIED = false;
			}
		} else {
			if (portal != null) {
				if (ctx.players.getLocal().distanceTo(portal) < 6) {
					portal.click("Teleport-previous");
				} else {
					ctx.pathing
							.step(new WorldPoint(portal.getLocation().getX() - 1, portal.getLocation().getY() - 4, 0));
					ctx.sleepCondition(() -> ctx.players.getLocal().distanceTo(portal) < 6);
				}
			} else {
				ctx.magic.castSpellOnce("Zenyte Home Teleport");
				ctx.sleep(1300);
				ctx.sleepCondition(() -> ctx.players.getLocal().getAnimation() == -1);
		
			}
		}
		*/
		LoginManager.sendLogout();
		ctx.stopScript();
		return;
	}

	@Override
	public String status() {
		return "Died, Logging out.";
	}

}
