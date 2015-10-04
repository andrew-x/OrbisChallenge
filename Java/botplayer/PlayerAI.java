import java.io.*;
import java.lang.Exception;
import java.util.ArrayList;
import java.util.Random;

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
        for (int i = 0; i < rightFile.size(); i++) {
            if (rightFile.get(i) != null){
                System.out.print(rightFile.get(i).toString() + " ");
            }

        }
        upVal = getValues(gameboard, player, upFile);
        System.out.println("up: " + upVal.approach + " " + upVal.avoid + " " +  upVal.attack+ " | size: " + upFile.size());
        downVal = getValues(gameboard, player, downFile);
        System.out.println("down: " + downVal.approach + " " + downVal.avoid + " " +  downVal.attack+ " | size: " + downFile.size());

        if (player.getDirection().equals(Direction.LEFT)) {
            curr_direction = 4;
            leftModifier = 0.25;
        } else if (player.getDirection().equals(Direction.RIGHT)) {
            curr_direction = 2;
            rightModifier = 0.25;
        } else if (player.getDirection().equals(Direction.UP)) {
            curr_direction = 1;
            upModifier = 0.25;
        } else if (player.getDirection().equals(Direction.DOWN)) {
            curr_direction = 3;
            downModifier = 0.25;
        }

        int centerX = gameboard.getWidth() / 2;
        int centerY = gameboard.getHeight() / 2;
        if (centerX > player.x){
            rightModifier += 0.20;
        } else if (centerX < player.x){
            leftModifier += 0.20;
        }
        if (centerY > player.y){
            downModifier += 0.20;
        } else if (centerY < player.y){
            upModifier += 0.20;
        }
        System.out.println("Right: " + rightModifier);
        System.out.println("left: " + leftVal.getAbsSum(leftModifier) + " right: " + rightVal.getAbsSum(rightModifier) + " up " +  upVal.getAbsSum(upModifier) + " down " + downVal.getAbsSum(downModifier));
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
        } else if (leftVal.getAbsSum(leftModifier) == rightVal.getAbsSum(rightModifier) && leftVal.getAbsSum(leftModifier) > upVal.getAbsSum(upModifier) && leftVal.getAbsSum(leftModifier) > downVal.getAbsSum(downModifier)){
            if (new Random().nextInt(2) == 0){
                val = leftVal;
                direction = 4;
            } else {
                val = rightVal;
                direction = 2;
            }
        } else if (leftVal.getAbsSum(leftModifier) > rightVal.getAbsSum(rightModifier) && leftVal.getAbsSum(leftModifier) == upVal.getAbsSum(upModifier) && leftVal.getAbsSum(leftModifier) > downVal.getAbsSum(downModifier)){
            if (new Random().nextInt(2) == 0){
                val = leftVal;
                direction = 4;
            } else {
                val = upVal;
                direction = 1;
            }
        } else if (leftVal.getAbsSum(leftModifier) > rightVal.getAbsSum(rightModifier) && leftVal.getAbsSum(leftModifier) > upVal.getAbsSum(upModifier) && leftVal.getAbsSum(leftModifier) == downVal.getAbsSum(downModifier)){
            if (new Random().nextInt(2) == 0){
                val = leftVal;
                direction = 4;
            } else {

            }
        } else if (rightVal.getAbsSum(rightModifier) > leftVal.getAbsSum(leftModifier) && rightVal.getAbsSum(rightModifier) == upVal.getAbsSum(upModifier) && rightVal.getAbsSum(rightModifier) > downVal.getAbsSum(downModifier)){
            if (new Random().nextInt(2) == 0){
                val = rightVal;
                direction = 2;
            } else {
                val = upVal;
                direction = 1;
            }
        } else if (rightVal.getAbsSum(rightModifier) > leftVal.getAbsSum(leftModifier) && rightVal.getAbsSum(rightModifier) > upVal.getAbsSum(upModifier) && rightVal.getAbsSum(rightModifier) == downVal.getAbsSum(downModifier)){
            if (new Random().nextInt(2) == 0){
                val = rightVal;
                direction = 2;
            } else {

            }
        } else if (upVal.getAbsSum(upModifier) > leftVal.getAbsSum(leftModifier) && upVal.getAbsSum(upModifier) > rightVal.getAbsSum(rightModifier) && upVal.getAbsSum(upModifier) == downVal.getAbsSum(downModifier)) {
            if (new Random().nextInt(2) == 0){
                val = upVal;
                direction = 1;
            } else {

            }
        } else {
            int random = new Random().nextInt(4);
            if (random == 0){
                val = leftVal;
                direction = 4;
            } else if (random == 1){
                val = rightVal;
                direction = 2;
            } else if (random == 2){
                val = upVal;
                direction = 1;
            } else {
                val = downVal;
                direction = 3;
            }
        }



        if (oppInRange(player, opponent) && player.getLaserCount() > 0){
            return Move.LASER;
        }

        if (val.getAction() == 2){
            switch (curr_direction){
                case 1:
                    switch (direction){
                        case 1:
                            return Move.FACE_DOWN;
                        case 2:
                            if (upFile.size() > 1){
                                return Move.FORWARD;
                            } else {
                                return Move.FACE_DOWN;
                            }
                        case 3:
                            if (upFile.size() > 1){
                                return Move.FORWARD;
                            } else {
                                if (leftFile.size() > rightFile.size()){
                                    return Move.FACE_LEFT;
                                } else if (leftFile.size() < rightFile.size()){
                                    return Move.FACE_RIGHT;
                                } else {
                                    if (leftFile.size() < 2 && rightFile.size() < 2){
                                        if (player.getShieldCount() > 0 && !player.isShieldActive()){
                                            return Move.SHIELD;
                                        }
                                        if (player.getTeleportCount() > 0 && gameboard.getTeleportLocations().size() > 0){
                                            int teleportLoc = new Random().nextInt((gameboard.getTeleportLocations().size() + 1));
                                            switch (teleportLoc){
                                                case 0:
                                                    return Move.TELEPORT_0;
                                                case 1:
                                                    return Move.TELEPORT_1;
                                                case 2:
                                                    return Move.TELEPORT_2;
                                                case 3:
                                                    return Move.TELEPORT_3;
                                                case 4:
                                                    return Move.TELEPORT_4;
                                                case 5:
                                                    return Move.TELEPORT_5;
                                                default:
                                                    return Move.FACE_DOWN;
                                            }
                                        }
                                        // Take it like a man.
                                        return Move.FACE_DOWN;
                                    } else {
                                        if (rightModifier > leftModifier){
                                            return Move.FACE_RIGHT;
                                        } else {
                                            return Move.FACE_LEFT;
                                        }
                                    }
                                }
                            }
                        case 4:
                            if (upFile.size() > 1){
                                return Move.FORWARD;
                            } else {
                                return Move.FACE_DOWN;
                            }
                    }
                    break;
                case 2:
                    switch (direction){
                        case 1:
                            if (rightFile.size() > 1){
                                return Move.FORWARD;
                            } else {
                                return Move.FACE_LEFT;
                            }
                        case 2:
                            return Move.FACE_LEFT;
                        case 3:
                            if (rightFile.size() > 1){
                                return Move.FORWARD;
                            } else {
                                return Move.FACE_LEFT;
                            }
                        case 4:
                            if (rightFile.size() > 1){
                                return Move.FORWARD;
                            } else {
                                if (upFile.size() > downFile.size()){
                                    return Move.FACE_UP;
                                } else if (upFile.size() < downFile.size()){
                                    return Move.FACE_DOWN;
                                } else {
                                    if (upFile.size() < 2 && downFile.size() < 2){
                                        if (player.getShieldCount() > 0 && !player.isShieldActive()){
                                            return Move.SHIELD;
                                        }
                                        if (player.getTeleportCount() > 0 && gameboard.getTeleportLocations().size() > 0){
                                            int teleportLoc = new Random().nextInt((gameboard.getTeleportLocations().size() + 1));
                                            switch (teleportLoc){
                                                case 0:
                                                    return Move.TELEPORT_0;
                                                case 1:
                                                    return Move.TELEPORT_1;
                                                case 2:
                                                    return Move.TELEPORT_2;
                                                case 3:
                                                    return Move.TELEPORT_3;
                                                case 4:
                                                    return Move.TELEPORT_4;
                                                case 5:
                                                    return Move.TELEPORT_5;
                                                default:
                                                    return Move.FACE_LEFT;
                                            }
                                        }
                                        // Take it like a man.
                                        return Move.FACE_LEFT;
                                    } else {
                                        if (upModifier > downModifier){
                                            return Move.FACE_UP;
                                        } else {
                                            return Move.FACE_DOWN;
                                        }
                                    }
                                }
                            }
                    }
                    break;
                case 3:
                    switch (direction){
                        case 1:
                            if (downFile.size() > 1){
                                return Move.FORWARD;
                            } else {
                                if (leftFile.size() > rightFile.size()){
                                    return Move.FACE_LEFT;
                                } else if (leftFile.size() < rightFile.size()) {
                                    return Move.FACE_RIGHT;
                                } else {
                                    if (leftFile.size() < 2 && rightFile.size() < 2){
                                        if (player.getShieldCount() > 0 && !player.isShieldActive()){
                                            return Move.SHIELD;
                                        }
                                        if (player.getTeleportCount() > 0 && gameboard.getTeleportLocations().size() > 0){
                                            int teleportLoc = new Random().nextInt((gameboard.getTeleportLocations().size() + 1));
                                            switch (teleportLoc){
                                                case 0:
                                                    return Move.TELEPORT_0;
                                                case 1:
                                                    return Move.TELEPORT_1;
                                                case 2:
                                                    return Move.TELEPORT_2;
                                                case 3:
                                                    return Move.TELEPORT_3;
                                                case 4:
                                                    return Move.TELEPORT_4;
                                                case 5:
                                                    return Move.TELEPORT_5;
                                                default:
                                                    return Move.FACE_UP;
                                            }
                                        }
                                        // Take it like a man.
                                        return Move.FACE_UP;
                                    } else {
                                        if (rightModifier > leftModifier){
                                            return Move.FACE_RIGHT;
                                        } else {
                                            return Move.FACE_LEFT;
                                        }
                                    }
                                }
                            }
                        case 2:
                            if (downFile.size() > 1){
                                return Move.FORWARD;
                            } else {
                                return Move.FACE_UP;
                            }
                        case 3:
                            return Move.FACE_UP;
                        case 4:
                            if (downFile.size() > 1){
                                return Move.FORWARD;
                            } else {
                                return Move.FACE_UP;
                            }
                    }
                    break;
                case 4:
                    switch (direction){
                        case 1:
                            if (leftFile.size() > 1){
                                return Move.FORWARD;
                            } else {
                                return Move.FACE_RIGHT;
                            }
                        case 2:
                            if (leftFile.size() > 1){
                                return Move.FORWARD;
                            } else {
                                if (upFile.size() > downFile.size()){
                                    return Move.FACE_UP;
                                } else if (upFile.size() < downFile.size()){
                                    return Move.FACE_DOWN;
                                } else {
                                    if (upFile.size() < 2 && downFile.size() < 2){
                                        if (player.getShieldCount() > 0 && !player.isShieldActive()){
                                            return Move.SHIELD;
                                        }
                                        if (player.getTeleportCount() > 0 && gameboard.getTeleportLocations().size() > 0){
                                            int teleportLoc = new Random().nextInt((gameboard.getTeleportLocations().size() + 1));
                                            switch (teleportLoc){
                                                case 0:
                                                    return Move.TELEPORT_0;
                                                case 1:
                                                    return Move.TELEPORT_1;
                                                case 2:
                                                    return Move.TELEPORT_2;
                                                case 3:
                                                    return Move.TELEPORT_3;
                                                case 4:
                                                    return Move.TELEPORT_4;
                                                case 5:
                                                    return Move.TELEPORT_5;
                                                default:
                                                    return Move.FACE_RIGHT;
                                            }
                                        }
                                        // Take it like a man.
                                        return Move.FACE_RIGHT;
                                    } else {
                                        if (upModifier > downModifier){
                                            return Move.FACE_UP;
                                        } else {
                                            return Move.FACE_DOWN;
                                        }
                                    }
                                }
                            }

                        case 3:
                            if (leftFile.size() > 1){
                                return Move.FORWARD;
                            } else {
                                return Move.FACE_RIGHT;
                            }
                        case 4:
                            return Move.FACE_RIGHT;
                    }
                    break;
            }
        } else if (curr_direction == direction) {
            switch (val.getAction()) {
                case 1:
                    return Move.SHOOT;
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

    private boolean oppInRange(Player player, Opponent opp){
        if (player.x == opp.x && Math.abs(player.y - opp.y) < 5){
            return true;
        } else if (player.y == opp.y && Math.abs(player.x - opp.x) < 5){
            return true;
        } else {
            return false;
        }
    }

    private void populateFiles(Gameboard gameboard, Player player) {
        int startX = player.getX();
        int startY = player.getY();

        for (int x = startX; x < gameboard.getWidth(); x++) {

            ArrayList<GameObjects> rightObjects = gameboard.getGameObjectsAtTile
                    (x, startY);

            if (rightObjects.isEmpty()) {
                if (isInTurretRange(gameboard, x, startY)){
                    rightFile.add(new Laser());
                } else {
                    rightFile.add(null);
                }
            } else if (rightObjects.get(0) instanceof Wall) {
                break;
            } else if (rightObjects.size() == 1) {
                if (isInTurretRange(gameboard, x, startY)){
                    rightFile.add(new Laser());
                } else {
                    rightFile.add(rightObjects.get(0));
                }

            } else {
                for (int i = 0; i < rightObjects.size(); i++) {
                    if (rightObjects.get(i) instanceof Player){
                        rightFile.add(rightObjects.get(i));
                    } else if ((rightObjects.get(i) instanceof Bullet) && ((Bullet)
                            rightObjects.get(i)).getDirection() ==
                            Direction.LEFT) {
                        rightFile.add(rightObjects.get(i));
                        break;
                    } else if (rightObjects.get(i) instanceof Opponent) {
                        rightFile.add(rightObjects.get(i));
                        break;
                    } else if (isInTurretRange(gameboard, x, startY)) {
                        rightFile.add(new Laser());
                        break;
                    } else if (((rightObjects.get(i) instanceof Bullet) && (
                            (Bullet)
                                    rightObjects.get(i)).getDirection() ==
                            Direction.UP) || ((rightObjects.get(i) instanceof Bullet) && ((Bullet)
                            rightObjects.get(i)).getDirection() ==
                            Direction.DOWN)) {
                        rightFile.add(rightObjects.get(i));
                        break;
                    } else if (rightObjects.get(0) instanceof PowerUp){
                        rightFile.add(new PowerUp(0, 0, PowerUpType.LASER));
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
                if (isInTurretRange(gameboard, x, startY)){
                    leftFile.add(new Laser());
                } else {
                    leftFile.add(null);
                }

            } else if (leftObjects.get(0) instanceof Wall) {
                break;
            } else if (leftObjects.size() == 1) {
                if (isInTurretRange(gameboard, x, startY)){
                    leftFile.add(new Laser());
                } else {
                    leftFile.add(leftObjects.get(0));
                }

            } else {
                for (int i = 0; i < leftObjects.size(); i++) {
                    if (leftObjects.get(i) instanceof Player){
                        rightFile.add(leftObjects.get(i));
                    } else if ((leftObjects.get(i) instanceof Bullet) && ((Bullet)
                            leftObjects.get(i)).getDirection() ==
                            Direction.RIGHT) {
                        leftFile.add(leftObjects.get(i));
                        break;
                    } else if (leftObjects.get(i) instanceof Opponent) {
                        leftFile.add(leftObjects.get(i));
                        break;
                    } else if (isInTurretRange(gameboard, x, startY)) {
                        leftFile.add(new Laser());
                        break;
                    } else if (((leftObjects.get(i) instanceof Bullet) && (
                            (Bullet)
                                    leftObjects.get(i)).getDirection() ==
                            Direction.UP) || ((leftObjects.get(i) instanceof
                            Bullet) && ((Bullet)
                            leftObjects.get(i)).getDirection() ==
                            Direction.DOWN)) {
                        leftFile.add(leftObjects.get(i));
                        break;
                    } else if (leftObjects.get(0) instanceof PowerUp){
                        rightFile.add(new PowerUp(0, 0, PowerUpType.LASER));
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
                if (isInTurretRange(gameboard, startX, y)){
                    upFile.add(new Laser());
                } else {
                    upFile.add(null);
                }

            } else if (upObjects.get(0) instanceof Wall) {
                break;
            } else if (upObjects.size() == 1) {
                if (isInTurretRange(gameboard, startX, y)){
                    upFile.add(new Laser());
                } else {
                    upFile.add(upObjects.get(0));
                }

            } else {
                for (int i = 0; i < upObjects.size(); i++) {
                    if (upObjects.get(i) instanceof Player){
                        rightFile.add(upObjects.get(i));
                    } else if ((upObjects.get(i) instanceof Bullet) && ((Bullet)
                            upObjects.get(i)).getDirection() ==
                            Direction.DOWN) {
                        upFile.add(upObjects.get(i));
                        break;
                    } else if (upObjects.get(i) instanceof Opponent) {
                        upFile.add(upObjects.get(i));
                        break;
                    } else if (isInTurretRange(gameboard, startX, y)) {
                        upFile.add(new Laser());
                        break;
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
                    } else if (upObjects.get(0) instanceof PowerUp){
                        rightFile.add(new PowerUp(0, 0, PowerUpType.LASER));
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
                if (isInTurretRange(gameboard, startX, y)){
                    downFile.add(new Laser());
                } else {
                    downFile.add(null);
                }

            } else if (downObjects.get(0) instanceof Wall) {
                break;
            } else if (downObjects.size() == 1) {
                if (isInTurretRange(gameboard, startX, y)){
                    downFile.add(new Laser());
                } else {
                    downFile.add(downObjects.get(0));
                }
            } else {
                for (int i = 0; i < downObjects.size(); i++) {
                    if (downObjects.get(i) instanceof Player){
                        rightFile.add(downObjects.get(i));
                    } else if ((downObjects.get(i) instanceof Bullet) && ((Bullet)
                            downObjects.get(i)).getDirection() ==
                            Direction.UP) {
                        downFile.add(downObjects.get(i));
                        break;
                    } else if (downObjects.get(i) instanceof Opponent) {
                        downFile.add(downObjects.get(i));
                        break;
                    } else if (isInTurretRange(gameboard, startX, y)) {
                        downFile.add(new Laser());
                        break;
                    } else if (((downObjects.get(i) instanceof Bullet) && (
                            (Bullet) downObjects.get(i)).getDirection() ==
                            Direction.LEFT) || ((downObjects.get(i)
                            instanceof Bullet) && ((Bullet)
                            downObjects.get(i)).getDirection() ==
                            Direction.RIGHT)) {
                        downFile.add(downObjects.get(i));
                        break;
                    } else if (downObjects.get(i) instanceof PowerUp){
                        rightFile.add(new PowerUp(0, 0, PowerUpType.LASER));
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
                    System.out.println("Wall");
                    approach += memory.Walls.approach / ((i+1)*2);
                    attack += memory.Walls.attack / ((i+1)*2);
                    avoid += memory.Walls.avoid / ((i+1)*2);
                } else if (go instanceof Bullet) {
                    if (((Bullet) go).getDirection().equals(Direction.opposite(player.getDirection()))) {
                        System.out.println("BulletApr");
                        approach += memory.BulletAppr.approach / ((i+1)*2);
                        attack += memory.BulletAppr.attack / ((i+1)*2);
                        avoid += memory.BulletAppr.avoid / ((i+1)*2);
                    } else if (((Bullet) go).getDirection().equals(player.getDirection())) {
                        System.out.println("BulletAway");
                        approach += memory.BulletAway.approach / ((i+1)*2);
                        attack += memory.BulletAway.attack / ((i+1)*2);
                        avoid += memory.BulletAway.avoid / ((i+1)*2);
                    } else {
                        System.out.println("BulletNeut");
                        approach += memory.BulletNeut.approach / ((i+1)*2);
                        attack += memory.BulletNeut.attack / ((i+1)*2);
                        avoid += memory.BulletNeut.avoid / ((i+1)*2);
                    }
                } else if (go instanceof Laser) {
                    System.out.println("Laser");
                    approach += memory.Laser.approach / ((i+1)*2);
                    attack += memory.Laser.attack / ((i+1)*2);
                    avoid += memory.Laser.avoid / ((i+1)*2);
                } else if (go instanceof Turret) {
                    if (i < 5) {
                        System.out.println("Turrets In");
                        approach += memory.TurretsIn.approach / ((i+1)*2);
                        attack += memory.TurretsIn.attack / ((i+1)*2);
                        avoid += memory.TurretsIn.avoid / ((i+1)*2);
                    } else {
                        System.out.println("Turrets Out");
                        approach += memory.TurretsOut.approach / ((i+1)*2);
                        attack += memory.TurretsOut.attack / ((i+1)*2);
                        avoid += memory.TurretsOut.avoid / ((i+1)*2);
                    }
                } else if (go instanceof PowerUp) {
                    System.out.println("Powerup");
                    approach += memory.Powerups.approach / ((i+1)*2);
                    attack += memory.Powerups.attack / ((i+1)*2);
                    avoid += memory.Powerups.avoid / ((i+1)*2);
                } else if (go instanceof Opponent) {
                    if (((Opponent) go).getDirection().equals(Direction.opposite(player.getDirection()))) {
                        System.out.println("OppApr");
                        approach += memory.OppAppr.approach / ((i+1)*2);
                        attack += memory.OppAppr.attack / ((i+1)*2);
                        avoid += memory.OppAppr.avoid / ((i+1)*2);
                    } else if (((Opponent) go).getDirection().equals(player.getDirection())) {
                        System.out.println("OppAway");
                        approach += memory.OppAway.approach / ((i+1)*2);
                        attack += memory.OppAway.attack / ((i+1)*2);
                        avoid += memory.OppAway.avoid / ((i+1)*2);
                    } else {
                        System.out.println("OppNeut");
                        approach += memory.OppNeut.approach / ((i+1)*2);
                        attack += memory.OppNeut.attack / ((i+1)*2);
                        avoid += memory.OppNeut.avoid / ((i+1)*2);
                    }

                    if (((Opponent)go).isShieldActive()){
                        avoid += 0.5 / ((i+1)*2);
                    }
                }
            } else {
                approach += memory.space.approach;
                attack += memory.space.attack;
                avoid += memory.space.avoid;
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
        public Values space;

        public Memory() {
            Walls = new Values(0, 0, 0);
            BulletAppr = new Values(0, 5, 0);
            BulletAway = new Values(3, 2, 0);
            BulletNeut = new Values(2, 3, 0);
            Laser = new Values(0, 3, 0);
            TurretsIn = new Values(0, 5, 2);
            TurretsOut = new Values(0, 2, 5);
            Powerups = new Values(5, 0, 0);
            OppAppr = new Values(1, 5, 4);
            OppAway = new Values(3, 2, 5);
            OppNeut = new Values(3, 1, 5);
            space = new Values(0.15, 0, 0);
        }
    }
}
