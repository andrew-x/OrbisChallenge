import com.orbischallenge.engine.gameboard.GameObject;
import com.orbischallenge.engine.gameboard.Gameboard;

import java.util.*;
import java.awt.Point;
import java.util.ArrayList;

public class PlayerAI extends ClientAI {

    private ArrayList<GameObjects> leftFile;
    private ArrayList<GameObjects> rightFile;
    private ArrayList<GameObject> upFile;
    private ArrayList<GameObject> downFile;

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

    private void populateFiles(Gameboard gameboard){
        int startX = 0;
        int startY = 0;
        for (int x = startX; x < gameboard.getWidth(); i++){
        }

    }
}
