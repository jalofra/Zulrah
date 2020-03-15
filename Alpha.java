package elfZulrah;

import java.util.ArrayList;

import net.runelite.api.coords.WorldPoint;
import simple.hooks.wrappers.SimpleNpc;
import simple.robot.api.ClientContext;

/**
 * @author Elf
 */
public class Alpha extends Fight {

    public Alpha(ClientContext ctx) {
        super(ctx);
    }

    private static boolean dodgedOnce = false;
    private static boolean switchTile = false;

    public static void initiate(SimpleNpc zulrah, ClientContext ctx) {
        ArrayList<WorldPoint> _aPoints = Utils.createPointArray(playerSpawn, rotName);
        WorldPoint safeSpot;
        switch (currPhase) {
            case 1:
                doingJad = false;
                dodgedOnce = false;
                safeSpot = _aPoints.get(0);
                moveToPoint(ctx, safeSpot);
                if (ctx.players.getLocal().getLocation().distanceTo(safeSpot) < 5)
                    attackZulrah(zulrah);
                break;
            case 2:
                if (!dodgedOnce) {
                    safeSpot = _aPoints.get(1);
                } else {
                    safeSpot = _aPoints.get(0);
                }
                if (zulrah.getAnimation() == Zulrah.MELEE_ATTACK) {
                    switchTile = true;
                    moveToPoint(ctx, safeSpot);
                }
                if (zulrah.getAnimation() == -1 && switchTile) {
                    dodgedOnce = true;
                    switchTile = false;
                }
                break;
            case 3:
                dodgedOnce = false;
                safeSpot = _aPoints.get(1);
                moveToPoint(ctx, safeSpot);
                break;
            case 4:
            case 5:
            case 6:
            case 8:
                safeSpot = _aPoints.get(2);
                moveToPoint(ctx, safeSpot);
                break;
            case 7:
                safeSpot = _aPoints.get(3);
                moveToPoint(ctx, safeSpot);
                break;
            case 9:
                safeSpot = _aPoints.get(5);
                moveToPoint(ctx, safeSpot);
                handleJad(ctx, true);
                break;
            case 10:
                if (!dodgedOnce) {
                    safeSpot = _aPoints.get(6);
                } else {
                    safeSpot = _aPoints.get(5);
                }
                if (zulrah.getAnimation() == Zulrah.MELEE_ATTACK || zulrah.getAnimation() == Zulrah.MELEE_ATTACK2) {
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
