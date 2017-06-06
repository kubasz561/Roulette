package game_logic;

/**
 * Created by Kuba on 2017-06-06.
 */
public class Bet {
    private int betRound;
    private int betValue;
    private RollResult betColor;

    public int getBetRound() {
        return betRound;
    }
    public void setBetRound(int betRound) {
        this.betRound = betRound;
    }
    public int getBetValue() {
        return betValue;
    }
    public void setBetValue(int betValue) {
        this.betValue = betValue;
    }
    public RollResult getBetColor() {
        return betColor;
    }
    public void setBetColor(RollResult betColor) {
        this.betColor = betColor;
    }

}
