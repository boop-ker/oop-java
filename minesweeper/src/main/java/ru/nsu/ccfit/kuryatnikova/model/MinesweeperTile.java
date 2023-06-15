package ru.nsu.ccfit.kuryatnikova.model;

import java.util.ArrayList;
import java.util.Collection;

public class MinesweeperTile {
    public enum Status {
        CLOSE, FLAGGED, OPEN
    }

    public Status status = Status.CLOSE;
    private boolean isMined = false;
    private int minedNeighboursNumber;
    private final Collection<MinesweeperTile> neighbours = new ArrayList<>();

    public MinesweeperTile() {}

    public boolean mined() {
        return isMined;
    }

    public void setMined(boolean mined) {
        isMined = mined;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setOpen() {
        status = Status.OPEN;
    }

    public int getMinedNeighboursNumber() {
        return minedNeighboursNumber;
    }

    public void setMinedNeighboursNumber(int minedNeighboursNumber) {
        this.minedNeighboursNumber = minedNeighboursNumber;
    }

    public Collection<MinesweeperTile> getNeighbours() {
        return neighbours;
    }
}
