package elfZulrah;

import java.util.ArrayList;
import net.runelite.api.coords.WorldPoint;
import simple.hooks.wrappers.SimpleNpc;
import simple.robot.api.ClientContext;

/**
 * @author Elf
 *
 */
public class Bravo extends Fight {

	public Bravo(ClientContext ctx) {
		super(ctx);
	}

	private static boolean dodgedOnce = false;
	private static boolean switchTile = false;

	public static void initiate(SimpleNpc zulrah, ClientContext ctx) {
		ArrayList<WorldPoint> _aPoints = Utils.createPointArray(playerSpawn, rotName);
		WorldPoint safeSpot;

		switch (currPhase) {
		case 5:
		case 6:
			dodgedOnce = false;
			safeSpot = _aPoints.get(2);
			moveToPoint(ctx, safeSpot);
			break;
		case 7:
			safeSpot = _aPoints.get(3);
			moveToPoint(ctx, safeSpot);
			break;
		case 8:
			safeSpot = _aPoints.get(4);
			moveToPoint(ctx, safeSpot);
			break;
		case 9:
			safeSpot = _aPoints.get(5);
			moveToPoint(ctx, safeSpot);
			handleJad(ctx, true);
		case 10:
			doingJad = false;
			if (!dodgedOnce) {
				safeSpot = _aPoints.get(6);
			} else {
				safeSpot = _aPoints.get(5);
			}
			if (zulrah.getAnimation() == Zulrah.MELEE_ATTACK2) {
				switchTile = true;
				moveToPoint(ctx, safeSpot);
			}
			if (zulrah.getAnimation() == -1 && switchTile) {
				dodgedOnce = true;
				switchTile = false;
			}
			break;
		default:
			System.out.println("Oops.. something went wrong");
			break;
		}
	}

}
