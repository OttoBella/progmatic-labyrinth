package com.progmatic.labyrinthproject;

import com.progmatic.labyrinthproject.enums.CellType;
import com.progmatic.labyrinthproject.enums.Direction;
import com.progmatic.labyrinthproject.exceptions.CellException;
import com.progmatic.labyrinthproject.exceptions.InvalidMoveException;
import com.progmatic.labyrinthproject.interfaces.Labyrinth;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @author pappgergely
 */
public class LabyrinthImpl implements Labyrinth {
    int width = -1;
    int height = -1;
    CellType[][] cellTypes = new CellType[0][0];
    Coordinate playerCor = new Coordinate(0, 0);

    public LabyrinthImpl(int width, int height) {
        this.width = width;
        this.height = height;

    }

    @Override
    public void loadLabyrinthFile(String fileName) {
        try {
            Scanner sc = new Scanner(new File(fileName));
            int width = Integer.parseInt(sc.nextLine());
            int height = Integer.parseInt(sc.nextLine());
            new LabyrinthImpl(width, height);
            for (int hh = 0; hh < height; hh++) {
                String line = sc.nextLine();
                for (int ww = 0; ww < width; ww++) {
                    switch (line.charAt(ww)) {
                        case 'W':
                            cellTypes[hh][ww] = CellType.WALL;
                            break;
                        case 'E':
                            cellTypes[hh][ww] = CellType.EMPTY;
                            break;
                        case 'S':
                            cellTypes[hh][ww] = CellType.START;
                            break;
                        default:
                            cellTypes[hh][ww] = CellType.END;
                            break;
                    }
                }
            }
        } catch (FileNotFoundException | NumberFormatException ex) {
            System.out.println(ex.toString());
        }
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public CellType getCellType(Coordinate c) throws CellException {
        int row = c.getRow();
        int col = c.getCol();
        if (row + 1 > width || col + 1 > height || row < 0 || col < 0) {
            throw new CellException(row, col, "Invalid coodinate");
        }

        CellType cellType = cellTypes[row][col];
        if (cellType == null) {
            throw new CellException(row, col, "Invalid coordinate.");
        }
        return cellType;
    }

    @Override
    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
        cellTypes = new CellType[width][height];
    }

    @Override
    public void setCellType(Coordinate c, CellType type) throws CellException {
        int row = c.getRow();
        int col = c.getCol();


        if (col < 0 || row < 0) {
            throw new CellException(row, col, "mass");
        }
        cellTypes[row][col] = type;
    }

    @Override
    public Coordinate getPlayerPosition() {
        return playerCor;
    }

    @Override
    public boolean hasPlayerFinished() {
        int row = getPlayerPosition().getRow();
        int col = getPlayerPosition().getCol();
        return cellTypes[row][col] == CellType.END;
    }

    @Override
    public List<Direction> possibleMoves() {
        List<Direction> directionsList = new ArrayList<>();
        int playerRow = getPlayerPosition().getRow();
        int playerCol = getPlayerPosition().getCol();
        CellType type;

        if (playerRow < width) {
            type = cellTypes[playerRow + 1][playerCol];
            if (type != CellType.WALL) {
                directionsList.add(Direction.EAST);
            }
        }
        if (playerCol < height) {
            type = cellTypes[playerRow][playerCol + 1];
            if (type != CellType.WALL) {
                directionsList.add(Direction.NORTH);
            }
        }
        if (playerRow > 0) {
            type = cellTypes[playerRow - 1][playerCol];
            if (type != CellType.WALL) {
                directionsList.add(Direction.WEST);
            }
        }
        if (playerCol > 0) {
            type = cellTypes[playerRow][playerCol - 1];
            if (type != CellType.WALL) {
                directionsList.add(Direction.SOUTH);
            }
        }
        return directionsList;
    }

    @Override
    public void movePlayer(Direction direction) throws InvalidMoveException {
        int playerRow = getPlayerPosition().getRow();
        int playerCol = getPlayerPosition().getCol();
        List<Direction> directionList = possibleMoves();
        if (directionList.contains(direction)) {
            if (direction == Direction.EAST) {
                int newIdx = playerRow + 1;
                if (cellTypes[newIdx][playerCol] == CellType.WALL || cellTypes[playerRow][newIdx] == null) {
                    throw new InvalidMoveException();
                }
                playerCor = new Coordinate(playerCol, newIdx);
            } else if (direction == Direction.NORTH) {
                int newIdx = playerCol + 1;
                if (cellTypes[playerRow][newIdx] == CellType.WALL || cellTypes[playerRow][newIdx] == null) {
                    throw new InvalidMoveException();
                }
                playerCor = new Coordinate(newIdx, playerRow);
            } else if (direction == Direction.WEST) {
                int newIdx = playerRow - 1;
                if (cellTypes[newIdx][playerCol] == CellType.WALL || cellTypes[playerRow][newIdx] == null) {
                    throw new InvalidMoveException();
                }
                playerCor = new Coordinate(playerCol, newIdx);
            } else if (direction == Direction.SOUTH) {
                int newIdx = playerCol - 1;
                if (cellTypes[playerRow][newIdx] == CellType.WALL || cellTypes[playerRow][newIdx] == null) {
                    throw new InvalidMoveException();
                }
                playerCor = new Coordinate(newIdx, playerRow);
            }
        }
    }
}
