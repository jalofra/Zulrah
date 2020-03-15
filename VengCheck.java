package elfZulrah;

import java.util.Timer;
import java.util.TimerTask;

import simple.hooks.simplebot.Game;
import simple.robot.api.ClientContext;

/**
 * @author Elf
 *
 */
public class VengCheck extends Fight implements Runnable {

	public static Timer vengTimer = new Timer();

	public VengCheck(ClientContext ctx) {
		super(ctx);
	}

	@Override
	public void run() {
		vengTimer.scheduleAtFixedRate(attackTask, 0, 32000);
	}

	TimerTask attackTask = new TimerTask() {

		@Override
		public void run() {
			System.out.println("Checking Veng");
			boolean hasPouch = !ctx.inventory.populate().filter(Utils.RUNE_POUCH).isEmpty();
			if (started && !doingJad && hasPouch) {
				castVengeance();

			}
		}

	};

	void castVengeance() {
		if (ctx.game.tab(Game.Tab.MAGIC)) {
			if (ctx.widgets.getWidget(218, 137).getSprite() == 564) {
				ctx.widgets.getWidget(218, 137).click(1);
			}
		}
	}

}
