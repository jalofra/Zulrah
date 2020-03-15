package elfZulrah;

import java.util.Timer;
import java.util.TimerTask;

import simple.hooks.filters.SimpleSkills.Skills;
import simple.hooks.wrappers.SimpleNpc;
import simple.robot.api.ClientContext;

/**
 * @author Elf
 *
 */
public class ZulrahCheck extends Fight implements Runnable {

	public static Timer timer = new Timer();

	public ZulrahCheck(ClientContext ctx) {
		super(ctx);
	}

	@Override
	public void run() {
		try {
			timer.scheduleAtFixedRate(zulrahCheckTask, 0, 100);
		} catch (IllegalStateException e) {
			e.printStackTrace();
			System.out.println("Task cancelled.");
		}
	}

	TimerTask zulrahCheckTask = new TimerTask() {

		@Override
		public void run() {
			if (started) {
				SimpleNpc zulrah = ctx.npcs.populate().filter("Zulrah").nearest().next();
				if (zulrah != null) {
					if (zulrah.getAnimation() == Zulrah.EMERGE && phaseOver) {
						if (currPhase == 1 && zulrah.getNpc().getId() != Zulrah.GREEN) {
							currPhase++;
							currRotation.add(Zulrah.GREEN);
						}
						System.out.println("Zulrah ID: " + zulrah.getId());
						currRotation.add(zulrah.getNpc().getId());
						System.out.println("Adding to currRotation");
						currStyle = Zulrah.getAttackStyle(currRotation, currPhase);
						if (currPhase < getRotation(currRotation).size()) {
							nextStyle = Zulrah.getAttackStyle(getRotation(currRotation), currPhase + 1);
						} else {
							needToReset = true;
						}
						System.out.println("currRotation: " + currRotation);
						System.out.println("PredictedRot: " + getRotation(currRotation));
						phaseOver = false;
					}
					if (zulrah.getAnimation() == Zulrah.HIDE && !phaseOver) {
						if (needToReset) {
							resetRotation();
						}
						currPhase += 1;
						System.out.println("Adding phase | Phase Number: " + currPhase);
						phaseOver = true;
					}
				}
				if (ctx.combat.health() == 0) {
					Utils.COLLECTED_ITEMS = false;
					Utils.PLAYER_DIED = true;
					System.out.println("We DIED");
					resetRotation();
				}
			}
		}
	};

	public long getXP() {
		return ctx.skills.experience(Skills.HITPOINTS);
	}

}
