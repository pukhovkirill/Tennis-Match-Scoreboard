package org.tms.service;

import org.tms.entity.Player;
import org.tms.model.GameScore;
import org.tms.model.MatchScore;
import org.tms.model.Set;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MatchScoreCalculationService {
    private final int NUMBER_OF_SETS_TO_WIN_MATCH = 2;
    private final Map<Integer, Integer> servNumberPointMap = new HashMap<>();
    private final MatchScore matchScore;
    private final GameScore gameScore;
    private final Set setScore;

    public MatchScoreCalculationService(MatchScore matchScore){
        this.matchScore = matchScore;
        this.gameScore = matchScore.getGameScore();
        this.setScore = gameScore.getSetScore();
        fillMap();
    }

    public void awardPoint(Long id){

        if(matchScore.getMatch().getWinner() != null)
            return;

        if(setScore.isTieBreak())
            tieBreakAwardPoint(id);
        else
            commonGameAwardPoint(id);
    }

    private void commonGameAwardPoint(Long id){
        var player = matchScore.getPlayerNumber(id);

        if(player == 1){
            int prevPoint = matchScore.getFirstPlayerScore();
            int newPoint = prevPoint + getPointByNthServ(incrementPlayerAce(player));
            matchScore.setFirstPlayerScore(newPoint);
        }else{
            int prevPoint = matchScore.getSecondPlayerScore();
            int newPoint = prevPoint + getPointByNthServ(incrementPlayerAce(player));
            matchScore.setSecondPlayerScore(newPoint);
        }

        updateGameState();
    }

    private void tieBreakAwardPoint(Long id){
        var player = matchScore.getPlayerNumber(id);

        if(player == 1){
            int prevGame = gameScore.getFirstPlayerGame();
            gameScore.setFirstPlayerGame(prevGame+1);
        }else{
            int prevGame = gameScore.getSecondPlayerGame();
            gameScore.setSecondPlayerGame(prevGame+1);
        }

        var firstPlayerGame = gameScore.getFirstPlayerGame();
        var secondPlayerGame = gameScore.getSecondPlayerGame();

        if(Math.abs(firstPlayerGame - secondPlayerGame) == 2){
            setScore.setTieBreak(false);
            updateGameState();
        }
    }

    private void updateGameState(){
        var gameWinner = gameWinner();
        if(gameWinner.isPresent()){
            int winner = gameWinner.get();

            if(winner == 1){
                int prevGame = gameScore.getFirstPlayerGame();
                gameScore.setFirstPlayerGame(prevGame+1);
            }else{
                int prevGame = gameScore.getSecondPlayerGame();
                gameScore.setSecondPlayerGame(prevGame+1);
            }

            matchScore.setFirstPlayerScore(0);
            matchScore.setSecondPlayerScore(0);

            matchScore.setFirstPlayerAce(0);
            matchScore.setSecondPlayerAce(0);
        }

        var setWinner = setWinner();
        if(setWinner.isPresent()){
            int winner = setWinner.get();

            if(winner == 1){
                int prevSet = setScore.getFirstPlayerSet();
                setScore.setFirstPlayerSet(prevSet+1);
            }else{
                int prevSet = setScore.getSecondPlayerSet();
                setScore.setSecondPlayerSet(prevSet+1);
            }

            gameScore.setFirstPlayerGame(0);
            gameScore.setSecondPlayerGame(0);
        }

        var matchWinner = matchWinner();
        if(matchWinner.isPresent()){
            int winner = matchWinner.get();

            Player winningPlayer;
            if(winner == 1){
                winningPlayer = matchScore.getMatch().getFirstPlayer();
            }else{
                winningPlayer = matchScore.getMatch().getSecondPlayer();
            }

            matchScore.getMatch().setWinner(winningPlayer);
        }
    }

    private Optional<Integer> matchWinner(){
        var firstPlayerSet = setScore.getFirstPlayerSet();
        var secondPlayerSet = setScore.getSecondPlayerSet();

        Integer player = null;

        if(firstPlayerSet == NUMBER_OF_SETS_TO_WIN_MATCH)
            player = 1;

        if(secondPlayerSet == NUMBER_OF_SETS_TO_WIN_MATCH)
            player = 2;

        return Optional.ofNullable(player);
    }

    private Optional<Integer> setWinner(){
        var firstPlayerGame = gameScore.getFirstPlayerGame();
        var secondPlayerGame = gameScore.getSecondPlayerGame();

        Integer player = null;

        if(isTieBreak()) {
            setScore.setTieBreak(true);
            return Optional.empty();
        }

        if(Math.abs(firstPlayerGame - secondPlayerGame) >= 2){
            if(firstPlayerGame >= 6)
                player = 1;

            if(secondPlayerGame >= 6)
                player = 2;

            if(firstPlayerGame >= 6 && secondPlayerGame >= 6)
                player = Math.max(firstPlayerGame, secondPlayerGame) == firstPlayerGame ? 1 : 2;
        }

        return Optional.ofNullable(player);
    }

    private boolean isTieBreak(){
        var firstPlayerGame = gameScore.getFirstPlayerGame();
        var secondPlayerGame = gameScore.getSecondPlayerGame();

        return firstPlayerGame == secondPlayerGame && firstPlayerGame == 6;
    }

    private Optional<Integer> gameWinner(){
        var firstPlayerScore = matchScore.getFirstPlayerScore();
        var secondPlayerScore = matchScore.getSecondPlayerScore();

        Integer player = null;

        if(isDeuce()) return Optional.empty();

        if(firstPlayerScore > 40)
            player = 1;

        if(secondPlayerScore > 40)
            player = 2;

        if(Math.abs(firstPlayerScore - secondPlayerScore) < 2)
            return Optional.empty();

        if(firstPlayerScore > 40 && secondPlayerScore > 40)
            player = Math.max(firstPlayerScore, secondPlayerScore) == firstPlayerScore ? 1 : 2;

        return Optional.ofNullable(player);
    }

    private boolean isDeuce(){
        var firstPlayerScore = matchScore.getFirstPlayerScore();
        var secondPlayerScore = matchScore.getSecondPlayerScore();

        return firstPlayerScore == secondPlayerScore && firstPlayerScore == 40;
    }

    private int getPointByNthServ(int ace){
        return ace > 4
                ? servNumberPointMap.get(4)
                : servNumberPointMap.get(ace);
    }

    private void fillMap(){
        if(servNumberPointMap.isEmpty()){
            servNumberPointMap.put(1, 15);
            servNumberPointMap.put(2, 15);
            servNumberPointMap.put(3, 10);
            servNumberPointMap.put(4, 1);
        }
    }

    private int incrementPlayerAce(int player){
        int ace;

        if(player == 1){
            ace = matchScore.getFirstPlayerAce();
            matchScore.setFirstPlayerAce(++ace);
        }else{
            ace = matchScore.getSecondPlayerAce();
            matchScore.setSecondPlayerAce(++ace);
        }

        return ace;
    }
}
