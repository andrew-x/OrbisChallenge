import java.io.*;
import java.lang.Exception;
import java.util.ArrayList;

public class PlayerAI extends ClientAI {
    private static String filepath;

    private ArrayList<GameObjects> leftFile;
    private ArrayList<GameObjects> rightFile;
    private ArrayList<GameObjects> upFile;
    private ArrayList<GameObjects> downFile;

    private Values val;
    private Values rightVal;
    private Values leftVal;
    private Values upVal;
    private Values downVal;

    private double upModifier;
    private double downModifier;
    private double leftModifier;
    private double rightModifier;

    private int direction = 1;
    private int curr_direction = 1;

    private Memory memory;

    public PlayerAI() {
        memory = new Memory();
        filepath = PlayerAI.class.getProtectionDomain().getCodeSource().getLocation().getPath() + "/data.dat";
        //load();

        leftFile = new ArrayList<>();
        rightFile = new ArrayList<>();
        upFile = new ArrayList<>();
        downFile = new ArrayList<>();

        val = new Values();
        rightVal = new Values();
        leftVal = new Values();
        upVal = new Values();
        downVal = new Values();
    }

    @Override
    public Move getMove(Gameboard gameboard, Opponent opponent, Player player) throws NoItemException, MapOutOfBoundsException {
        reset();
        populateFiles(gameboard, player);
        leftVal = getValues(gameboard, player, leftFile);
        System.out.println("left: " + leftVal.approach + " " + leftVal.avoid + " " + leftVal.attack + " | size: " + leftFile.size());
        rightVal = getValues(gameboard, player, rightFile);
        System.out.println("right: " + rightVal.approach + " " + rightVal.avoid + " " +  rightVal.attack+ " | size: " + rightFile.size());
        upVal = getValues(gameboard, player, upFile);
        System.out.println("up: " + upVal.approach + " " + upVal.avoid + " " +  upVal.attack+ " | size: " + upFile.size());
        downVal = getValues(gameboard, player, downFile);
        System.out.println("down: " + downVal.approach + " " + downVal.avoid + " " +  downVal.attack+ " | size: " + downFile.size());

        if (player.getDirection().equals(Direction.LEFT)) {
            curr_direction = 4;
            leftModifier = 0.5;
        } else if (player.getDirection().equals(Direction.RIGHT)) {
            curr_direction = 2;
            rightModifier = 0.5;
        } else if (player.getDirection().equals(Direction.UP)) {
            curr_direction = 1;
            upModifier = 0.5;
        } else if (player.getDirection().equals(Direction.DOWN)) {
            curr_direction = 3;
            downModifier = 0.5;
        }

        if (leftVal.getAbsSum(leftModifier) > rightVal.getAbsSum(rightModifier) && leftVal.getAbsSum(leftModifier) > upVal.getAbsSum(upModifier) && leftVal.getAbsSum(leftModifier) > downVal.getAbsSum(downModifier)) {
            val = leftVal;
            direction = 4;
        } else if (rightVal.getAbsSum(rightModifier) > leftVal.getAbsSum(leftModifier) && rightVal.getAbsSum(rightModifier) > upVal.getAbsSum(upModifier) && rightVal.getAbsSum(rightModifier) > downVal.getAbsSum(downModifier)) {
            val = rightVal;
            direction = 2;
        } else if (upVal.getAbsSum(upModifier) > leftVal.getAbsSum(leftModifier) && upVal.getAbsSum(upModifier) > rightVal.getAbsSum(rightModifier) && upVal.getAbsSum(upModifier) > downVal.getAbsSum(downModifier)) {
            val = upVal;
            direction = 1;
        } else if (downVal.getAbsSum(downModifier) > leftVal.getAbsSum(leftModifier) && downVal.getAbsSum(downModifier) > rightVal.getAbsSum(rightModifier) && downVal.getAbsSum(downModifier) > upVal.getAbsSum(upModifier)) {
            val = downVal;
            direction = 3;
        }

        if (curr_direction == direction) {
            switch (val.getAction()) {
                case 1:
                    return Move.SHOOT;
                case 2:
                    switch (curr_direction) {
                        case 1:
                            return Move.FACE_DOWN;
                        case 2:
                            return Move.FACE_LEFT;
                        case 3:
                            return Move.FACE_UP;
                        case 4:
                            return Move.FACE_RIGHT;
                    }
                    break;
                case 3:
                    return Move.FORWARD;
            }
        } else {
            switch (direction) {
                case 1:
                    return Move.FACE_UP;
                case 2:
                    return Move.FACE_RIGHT;
                case 3:
                    return Move.FACE_DOWN;
                case 4:
                    return Move.FACE_LEFT;
            }
        }
        return Move.NONE;
    }

    private void reset() {
        leftModifier = 0;
        rightModifier = 0;
        upModifier = 0;
        downModifier = 0;

        val = new Values();
        leftVal = new Values();
        rightVal = new Values();
        upVal = new Values();
        downVal = new Values();

        leftFile = new ArrayList<>();
        rightFile = new ArrayList<>();
        upFile = new ArrayList<>();
        downFile = new ArrayList<>();

        direction = 0;
        curr_direction = 0;
    }

    private void populateFiles(Gameboard gameboard, Player player) {
        int startX = player.getX();
        int startY = player.getY();


        for (int x = startX; x < gameboard.getWidth(); x++) {

            ArrayList<GameObjects> rightObjects = gameboard.getGameObjectsAtTile
                    (x, startY);
            if (rightObjects.isEmpty()) {
                rightFile.add(null);
            } else if (rightObjects.get(0) instanceof Wall) {
                break;
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

            if (leftObjects.isEmpty()) {
                leftFile.add(null);
            } else if (leftObjects.get(0) instanceof Wall) {
                break;
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

            if (upObjects.isEmpty()) {
                upFile.add(null);
            } else if (upObjects.get(0) instanceof Wall) {
                break;
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

            if (downObjects.isEmpty()) {
                downFile.add(null);
            } else if (downObjects.get(0) instanceof Wall) {
                break;
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

    private Values getValues(Gameboard gameboard, Player player, ArrayList<GameObjects> file) {
        Values val = new Values();
        double approach = 0;
        double attack = 0;
        double avoid = 0;
        for (int i = 0; i < file.size(); i++) {
            GameObjects go = file.get(i);
            if (go != null) {
                if (go instanceof Wall) {
                    approach += memory.Walls.approach;
                    attack += memory.Walls.attack;
                    avoid += memory.Walls.avoid;
                } else if (go instanceof Bullet) {
                    if (((Bullet) go).getDirection().equals(Direction.opposite(player.getDirection()))) {
                        approach += memory.BulletAppr.approach;
                        attack += memory.BulletAppr.attack;
                        avoid += memory.BulletAppr.avoid;
                    } else if (((Bullet) go).getDirection().equals(player.getDirection())) {
                        approach += memory.BulletAway.approach;
                        attack += memory.BulletAway.attack;
                        avoid += memory.BulletAway.avoid;
                    } else {
                        approach += memory.BulletNeut.approach;
                        attack += memory.BulletNeut.attack;
                        avoid += memory.BulletNeut.avoid;
                    }
                } else if (go instanceof Laser) {
                    approach += memory.Laser.approach;
                    attack += memory.Laser.attack;
                    avoid += memory.Laser.avoid;
                } else if (go instanceof Turret) {
                    if (Math.abs(((Turret) go).getX() - player.getX()) < 5 || Math.abs(((Turret) go).getY() - player.getY()) < 5) {
                        approach += memory.TurretsIn.approach;
                        attack += memory.TurretsIn.attack;
                        avoid += memory.TurretsIn.avoid;
                    } else {
                        approach += memory.TurretsOut.approach;
                        attack += memory.TurretsOut.attack;
                        avoid += memory.TurretsOut.avoid;
                    }
                } else if (go instanceof PowerUp) {
                    approach += memory.Powerups.approach;
                    attack += memory.Powerups.attack;
                    avoid += memory.Powerups.avoid;
                } else if (go instanceof Combatant) {
                    if (((Combatant) go).getDirection().equals(Direction.opposite(player.getDirection()))) {
                        approach += memory.OppAppr.approach;
                        attack += memory.OppAppr.attack;
                        avoid += memory.OppAppr.avoid;
                    } else if (((Combatant) go).getDirection().equals(player.getDirection())) {
                        approach += memory.OppAway.approach;
                        attack += memory.OppAway.attack;
                        avoid += memory.OppAway.avoid;
                    } else {
                        approach += memory.OppNeut.approach;
                        attack += memory.OppNeut.attack;
                        avoid += memory.OppNeut.avoid;
                    }

                    if (((Combatant)go).isShieldActive()){
                        avoid += 0.5;
                    }
                }
            }
        }
        val.approach = approach;
        val.attack = attack;
        val.avoid = avoid;
        return val;
    }

    private static class Laser extends GameObjects {

        public Laser() {
            super(0, 0);
        }

    }

    public static class Values implements Serializable {
        public double attack = 0;
        public double avoid = 0;
        public double approach = 0;

        public Values() {
            approach = 0;
            avoid = 0;
            attack = 0;
        }

        public Values(double approach, double avoid, double attack) {
            this.approach = approach;
            this.avoid = avoid;
            this.attack = attack;
        }

        public double getAbsSum() {
            return Math.abs(getSum());
        }

        public double getAbsSum(double modifier) {
            return Math.abs(getSum(modifier));
        }

        // This means something different now.
        public double getSum() {
            if (attack >= approach && attack >= avoid) {
                return attack;
            } else if (avoid > attack && avoid >= approach) {
                return avoid;
            } else {
                return approach;
            }
        }

        // This means something different now.
        public double getSum(double modifier) {
            if (attack >= approach && attack >= avoid) {
                return attack + modifier;
            } else if (avoid > attack && avoid >= approach) {
                return avoid + modifier;
            } else {
                return approach + modifier;
            }
        }

        public int getAction() {
            if (attack >= approach && attack >= avoid) {
                return 1;
            } else if (avoid > attack && avoid >= approach) {
                return 2;
            } else {
                return 3;
            }
        }
    }

    public static class Memory implements Serializable {
        public Values Walls;
        public Values BulletAppr;
        public Values BulletAway;
        public Values BulletNeut;
        public Values Laser;
        public Values TurretsIn;
        public Values TurretsOut;
        public Values Powerups;
        public Values OppAppr;
        public Values OppAway;
        public Values OppNeut;

        public Memory() {
            Walls = new Values(0, 1, 0);
            BulletAppr = new Values(0, 4, 0);
            BulletAway = new Values(3, 2, 0);
            BulletNeut = new Values(2, 3, 0);
            Laser = new Values(0, 5, 0);
            TurretsIn = new Values(1, 5, 3);
            TurretsOut = new Values(3, 2, 4);
            Powerups = new Values(5, 0, 0);
            OppAppr = new Values(1, 3, 5);
            OppAway = new Values(3, 2, 5);
            OppNeut = new Values(3, 1, 5);
        }
    }
}
