package com.progmatic.labyrinthproject.interfaces;

import com.progmatic.labyrinthproject.Coordinate;
import com.progmatic.labyrinthproject.LabyrinthImpl;
import com.progmatic.labyrinthproject.enums.Direction;

import java.util.Random;

public class RandomPlayer implements Player {
    Random random;
    @Override
    public Direction nextMove(Labyrinth l) {
        int rand = random.nextInt(4);
        Direction direction = Direction.NORTH;
        switch (rand){
            case 0: direction = Direction.EAST;
            break;
            case 1: direction = Direction.WEST;
            break;
            case 2: direction = Direction.SOUTH;
            break;
            case 3: direction = Direction.NORTH;
            break;
        }
        return direction;
    }
}
