package elfZulrah;

import java.util.Timer;
import java.util.TimerTask;

import simple.hooks.filters.SimpleSkills.Skills;
import simple.robot.api.ClientContext;

/**
 * @author Elf
 *
 */
public class AttackCheck extends Fight implements Runnable {

	public static Timer timer = new Timer();
	long startingXp = ctx.skills.experience(Skills.HITPOINTS);

	public AttackCheck(ClientContext ctx) {
		super(ctx);
	}

	@Override
	public void run() {
		timer.scheduleAtFixedRate(attackTask, 0, 1750);//og 3000 reduce for faster checks
	}

	TimerTask attackTask = new TimerTask() {

		@Override
		public void run() {
			if (started) // we have to stop it call it. started HAS to eventually be false. on zul death
				if (shouldAttack(ctx)) {
					attack = true;
				} else {
					attack = false;
				}
			//ToDo face zulrah 
		}
	};

	public boolean shouldAttack(ClientContext ctx) {
		boolean should = false;
		boolean noAnim = false;
		for (int i = 0; i < Utils.PLAYER_ANIMS.length; i++) {
			if (ctx.players.getLocal().getAnimation() != Utils.PLAYER_ANIMS[i]) {
				noAnim = true;
			}
		}
		if (getXP() == startingXp && noAnim) {
			should = true;
		} else {
			startingXp = getXP();
		}
		return should;
	}

	public long getXP() {
		return ctx.skills.experience(Skills.HITPOINTS);
	}

}
