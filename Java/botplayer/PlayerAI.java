import java.util.*;
import java.awt.Point;
import java.util.ArrayList;

public class PlayerAI extends ClientAI {

    private ArrayList<GameObjects> leftFile;
    private ArrayList<GameObjects> rightFile;
    private ArrayList<GameObjects> upFile;
    private ArrayList<GameObjects> downFile;

	public PlayerAI() {
        leftFile = new ArrayList<>();
        rightFile = new ArrayList<>();
        upFile = new ArrayList<>();
        downFile = new ArrayList<>();
	}

	@Override
	public Move getMove(Gameboard gameboard, Opponent opponent, Player player) throws NoItemException, MapOutOfBoundsException {

		return Move.NONE;
	}

    private void populateFiles(Gameboard gameboard, Player player) {
        int startX = player.getX();
        int startY = player.getY();


        for (int x = startX; x < gameboard.getWidth(); x++) {

            ArrayList<GameObjects> rightObjects = gameboard.getGameObjectsAtTile
                    (x, startY);

            if (rightObjects.get(0) instanceof Wall) {
                break;
            } else if (rightObjects.isEmpty()) {
                rightFile.add(null);
            } else if (rightObjects.size() == 1) {
                rightFile.add(rightObjects.get(0));
            } else {
                for (int i = 0; i < rightObjects.size(); i++) {
                    if ((rightObjects.get(i) instanceof Bullet) && ((Bullet)
                            rightObjects.get(i)).getDirection() ==
                            Direction.LEFT) {
                        rightFile.add(rightObjects.get(i));
                        break;
                    } else if (rightObjects.get(i) instanceof Combatant) {
                        rightFile.add(rightObjects.get(i));
                        break;
                    } else if (isInTurretRange(gameboard, x, startY)) {
                        rightFile.add(new Laser());
                    } else if (((rightObjects.get(i) instanceof Bullet) && (
                            (Bullet)
                            rightObjects.get(i)).getDirection() ==
                            Direction.UP) || ((rightObjects.get(i) instanceof Bullet) && ((Bullet)
                            rightObjects.get(i)).getDirection() ==
                            Direction.DOWN)) {
                        rightFile.add(rightObjects.get(i));
                        break;
                    } else if ((rightObjects.get(i) instanceof Bullet) && ((Bullet)
                            rightObjects.get(i)).getDirection() ==
                            Direction.RIGHT) {
                        rightFile.add(rightObjects.get(i));
                        break;
                    }
                }
            }

        }

        for (int x = startX; x >= 0; x--) {

            ArrayList<GameObjects> leftObjects = gameboard.getGameObjectsAtTile
                    (x, startY);

            if (leftObjects.get(0) instanceof Wall) {
                break;
            } else if (leftObjects.isEmpty()) {
                leftFile.add(null);
            } else if (leftObjects.size() == 1) {
                leftFile.add(leftObjects.get(0));
            } else {
                for (int i = 0; i < leftObjects.size(); i++) {
                    if ((leftObjects.get(i) instanceof Bullet) && ((Bullet)
                            leftObjects.get(i)).getDirection() ==
                            Direction.RIGHT) {
                        leftFile.add(leftObjects.get(i));
                        break;
                    } else if (leftObjects.get(i) instanceof Combatant) {
                        leftFile.add(leftObjects.get(i));
                        break;
                    } else if (isInTurretRange(gameboard, x, startY)) {
                        leftFile.add(new Laser());
                    } else if (((leftObjects.get(i) instanceof Bullet) && (
                            (Bullet)
                                    leftObjects.get(i)).getDirection() ==
                            Direction.UP) || ((leftObjects.get(i) instanceof
                            Bullet) && ((Bullet)
                            leftObjects.get(i)).getDirection() ==
                            Direction.DOWN)) {
                        leftFile.add(leftObjects.get(i));
                        break;
                    } else if ((leftObjects.get(i) instanceof Bullet) && (
                            (Bullet)
                            leftObjects.get(i)).getDirection() ==
                            Direction.LEFT) {
                        leftFile.add(leftObjects.get(i));
                        break;
                    }
                }
            }
        }

        for (int y = startY; y >= 0; y--) {
            ArrayList<GameObjects> upObjects = gameboard.getGameObjectsAtTile
                    (startX, y);

            if (upObjects.get(0) instanceof Wall) {
                break;
            } else if (upObjects.isEmpty()) {
                upFile.add(null);
            } else if (upObjects.size() == 1) {
                upFile.add(upObjects.get(0));
            } else {
                for (int i = 0; i < upObjects.size(); i++) {
                    if ((upObjects.get(i) instanceof Bullet) && ((Bullet)
                            upObjects.get(i)).getDirection() ==
                            Direction.DOWN) {
                        upFile.add(upObjects.get(i));
                        break;
                    } else if (upObjects.get(i) instanceof Combatant) {
                        upFile.add(upObjects.get(i));
                        break;
                    } else if (isInTurretRange(gameboard, startX, y)) {
                        upFile.add(new Laser());
                    } else if (((upObjects.get(i)
                            instanceof Bullet) && (
                            (Bullet)
                                    upObjects.get(i)).getDirection() ==
                            Direction.LEFT) || ((upObjects.get(i) instanceof
                            Bullet) && ((Bullet)
                            upObjects.get(i)).getDirection() ==
                            Direction.RIGHT)) {
                        upFile.add(upObjects.get(i));
                        break;
                    } else if ((upObjects.get(i) instanceof Bullet) && ((Bullet)
                            upObjects.get(i)).getDirection() ==
                            Direction.UP) {
                        upFile.add(upObjects.get(i));
                        break;
                    }
                }
            }
        }

        for (int y = startY; y < gameboard.getHeight(); y++) {
            ArrayList<GameObjects> downObjects = gameboard.getGameObjectsAtTile
                    (startX, y);

            if (downObjects.get(0) instanceof Wall) {
                break;
            } else if (downObjects.isEmpty()) {
                downFile.add(null);
            } else if (downObjects.size() == 1) {
                downFile.add(downObjects.get(0));
            } else {
                for (int i = 0; i < downObjects.size(); i++) {
                    if ((downObjects.get(i) instanceof Bullet) && ((Bullet)
                            downObjects.get(i)).getDirection() ==
                            Direction.UP) {
                        downFile.add(downObjects.get(i));
                        break;
                    } else if (downObjects.get(i) instanceof Combatant) {
                        downFile.add(downObjects.get(i));
                        break;
                    } else if (isInTurretRange(gameboard, startX, y)) {
                        downFile.add(new Laser());
                    } else if (((downObjects.get(i) instanceof Bullet) && (
                            (Bullet) downObjects.get(i)).getDirection() ==
                            Direction.LEFT) || ((downObjects.get(i)
                            instanceof Bullet) && ((Bullet)
                            downObjects.get(i)).getDirection() ==
                            Direction.RIGHT)) {
                        downFile.add(downObjects.get(i));
                        break;
                    } else if ((downObjects.get(i) instanceof Bullet) && (
                            (Bullet) downObjects.get(i)).getDirection() ==
                            Direction.DOWN) {
                        downFile.add(downObjects.get(i));
                        break;
                    }
                }
            }
        }
    }

    private boolean isInTurretRange(Gameboard gameboard, int x, int y) {

        boolean isInRange = false;
        ArrayList<Turret> turrets = gameboard.getTurrets();
        int size = turrets.size();

        for (int i = 0; i < size; i++) {
            int turretX = turrets.get(i).getX();
            int turretY = turrets.get(i).getY();

            if (x == turretX || y == turretY) {
                if ((Math.abs(turretX - x) < 5) || (Math.abs(turretY - y) <
                        5)) {
                    if (turrets.get(i).isFiringNextTurn()) {
                        isInRange = true;
                    }
                }
            }
        }

        return isInRange;

    }

    private static class Laser extends GameObjects {

        public Laser() {
            super(0, 0);
        }

    }

}

