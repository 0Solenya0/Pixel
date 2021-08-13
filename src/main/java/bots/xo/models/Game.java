package bots.xo.models;

import java.util.ArrayList;

public class Game {
    private int gameId;
    private int[][] board = new int[3][3];
    private int player1, player2;
    private ArrayList<Integer> messageIds = new ArrayList<>();

    public synchronized int getWinnerNumber() {
        for (int i = 0; i < 3; i++)
            if (board[i][0] == board[i][1] && board[i][1] == board[i][2] && board[i][0] != 0)
                return board[i][0];
        for (int j = 0; j < 3; j++)
            if (board[0][j] == board[1][j] && board[1][j] == board[2][j] && board[0][j] != 0)
                return board[0][j];
        if (board[0][0] == board[1][1] && board[1][1] == board[2][2] && board[0][0] != 0)
            return board[0][0];
        if (board[0][2] == board[1][1] && board[1][1] == board[2][0] && board[1][1] != 0)
            return board[1][1];
        return 0;
    }

    public int getWinnerId() {
        return getPlayerId(getWinnerNumber());
    }

    public synchronized boolean isFinished() {
        return getWinnerNumber() != 0 || getTurn() == 9;
    }

    public synchronized boolean isStarted() {
        return player1 != 0 && player2 != 0;
    }

    public int getTurn() {
        int res = 0;
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                if (board[i][j] != 0)
                    res++;
        return res;
    }

    public int getCurrentPlayerId() {
        return getPlayerId(getCurrentPlayerNumber());
    }

    public int getCurrentPlayerNumber() {
        return getTurn() % 2 + 1;
    }

    public int getPlayerId(int p) {
        return p == 1 ? player1 : player2;
    }

    public synchronized void setPlayer(int p, int id) {
        if (p == 1)
            player1 = id;
        else
            player2 = id;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public int getGameId() {
        return gameId;
    }

    public synchronized int getCell(int x, int y) {
        return board[x][y];
    }

    public synchronized boolean playTurn(int x, int y) {
        if (board[x][y] != 0)
            return false;
        board[x][y] = getCurrentPlayerNumber();
        return true;
    }

    public ArrayList<Integer> getMessageIds() {
        return messageIds;
    }
}
