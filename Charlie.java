package elfZulrah;

import java.util.ArrayList;
import net.runelite.api.coords.WorldPoint;
import simple.hooks.wrappers.SimpleNpc;
import simple.robot.api.ClientContext;

/**
 * @author Elf
 *
 */
public class Charlie extends Fight {

	private static boolean dodgedOnce = false;
	private static boolean switchTile = false;

	public Charlie(ClientContext ctx) {
		super(ctx);
	}

	public static void initiate(SimpleNpc zulrah, ClientContext ctx) {
		ArrayList<WorldPoint> _aPoints = Utils.createPointArray(playerSpawn, rotName);
		WorldPoint safeSpot;

		switch (currPhase) {
		case 2:
			dodgedOnce = false;
			safeSpot = _aPoints.get(1);
			break;
		case 3:
			if (!dodgedOnce) {
				safeSpot = _aPoints.get(2);
			} else {
				safeSpot = _aPoints.get(7);
			}
			moveToPoint(ctx, safeSpot);
			if (zulrah.getAnimation() == Zulrah.MELEE_ATTACK2) {
				switchTile = true;
				moveToPoint(ctx, safeSpot);
			}
			if (zulrah.getAnimation() == -1 && switchTile) {
				dodgedOnce = true;
				switchTile = false;
			}
			break;
		case 4:
			safeSpot = _aPoints.get(2);
			moveToPoint(ctx, safeSpot);
			break;
		case 5:
		case 6:
			safeSpot = _aPoints.get(3);
			moveToPoint(ctx, safeSpot);
			break;
		case 7:
		case 8:
			safeSpot = _aPoints.get(4);
			moveToPoint(ctx, safeSpot);
			break;
		case 9:
			safeSpot = _aPoints.get(5);
			moveToPoint(ctx, safeSpot);
			break;
		case 10:
			safeSpot = _aPoints.get(5);
			moveToPoint(ctx, safeSpot);
			handleJad(ctx, false);
			break;
		case 11:
			doingJad = false;
			safeSpot = _aPoints.get(6);
			moveToPoint(ctx, safeSpot);
			break;
		default:
			System.out.println("Oops.. something went wrong");
			break;
		}
	}

}
