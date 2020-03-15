package elfZulrah;

import java.util.ArrayList;
import net.runelite.api.coords.WorldPoint;
import simple.hooks.wrappers.SimpleNpc;
import simple.robot.api.ClientContext;

/**
 * @author Elf
 *
 */
public class Delta extends Fight {

	public Delta(ClientContext ctx) {
		super(ctx);
	}

	public static void initiate(SimpleNpc zulrah, ClientContext ctx) {
		ArrayList<WorldPoint> _aPoints = Utils.createPointArray(playerSpawn, rotName);
		WorldPoint safeSpot;

		switch (currPhase) {
		case 2:
			safeSpot = _aPoints.get(0);
			moveToPoint(ctx, safeSpot);
			break;
		case 3:
		case 4:
			safeSpot = _aPoints.get(1);
			moveToPoint(ctx, safeSpot);
			break;
		case 5:
		case 6:
			safeSpot = _aPoints.get(2);
			moveToPoint(ctx, safeSpot);
			break;
		case 7:
		case 8:
			safeSpot = _aPoints.get(3);
			moveToPoint(ctx, safeSpot);
			break;
		case 9:
		case 10:
			safeSpot = _aPoints.get(4);
			moveToPoint(ctx, safeSpot);
			break;
		case 11:
			safeSpot = _aPoints.get(4);
			moveToPoint(ctx, safeSpot);
			handleJad(ctx, false);
			break;
		case 12:
			doingJad = false;
			safeSpot = _aPoints.get(5);
			moveToPoint(ctx, safeSpot);
			break;
		default:
			System.out.println("Oops.. something went wrong");
			break;
		}
	}

}
