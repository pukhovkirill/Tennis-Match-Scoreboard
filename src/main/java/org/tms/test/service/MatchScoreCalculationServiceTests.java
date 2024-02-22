package org.tms.test.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.tms.entity.Match;
import org.tms.entity.Player;
import org.tms.model.GameScore;
import org.tms.model.MatchScore;
import org.tms.model.Set;
import org.tms.service.MatchScoreCalculationService;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MatchScoreCalculationServiceTests {
    private MatchScoreCalculationService service;
    private MatchScore matchScore;

    @BeforeEach
    void matchScoreInit(){

        var uuid = UUID.randomUUID();

        var newMatch = new Match();

        var player1 = new Player();
        player1.setId(1L);
        player1.setName("player1");

        var player2 = new Player();
        player2.setId(2L);
        player2.setName("player2");

        newMatch.setFirstPlayer(player1);
        newMatch.setSecondPlayer(player2);
        newMatch.setWinner(null);

        var gameScore = new GameScore(new Set());

        matchScore = new MatchScore(uuid, newMatch, gameScore);

        service = new MatchScoreCalculationService(matchScore);
    }

    @Test
    void gameWinnersTest(){

        // test score 40:30 by points ---------------

        service.awardPoint(1L);
        service.awardPoint(2L);

        service.awardPoint(1L);
        service.awardPoint(2L);

        service.awardPoint(1L);
        //now score is 40:30

        service.awardPoint(1L);
        //it is assumed that the players' score will be 0:0
        assertEquals(0, matchScore.getFirstPlayerScore());
        assertEquals(0, matchScore.getSecondPlayerScore());

        //it is assumed that the games' score will be 1:0
        assertEquals(1, matchScore.getGameScore().getFirstPlayerGame());
        assertEquals(0, matchScore.getGameScore().getSecondPlayerGame());

        // test score 30:40 by points ---------------

        service.awardPoint(1L);
        service.awardPoint(2L);

        service.awardPoint(1L);
        service.awardPoint(2L);

        service.awardPoint(2L);
        //now score is 30:40

        service.awardPoint(2L);
        //it is assumed that the players' score will be 0:0
        assertEquals(0, matchScore.getFirstPlayerScore());
        assertEquals(0, matchScore.getSecondPlayerScore());

        //it is assumed that the games' score will be 1:1
        assertEquals(1, matchScore.getGameScore().getFirstPlayerGame());
        assertEquals(1, matchScore.getGameScore().getSecondPlayerGame());
    }

    @Test
    void deuceTest(){

        // test score 40:40 by points and winner first player ---------------

        service.awardPoint(1L);
        service.awardPoint(2L);

        service.awardPoint(1L);
        service.awardPoint(2L);

        service.awardPoint(1L);
        service.awardPoint(2L);
        //now score is 40:40

        service.awardPoint(1L);
        service.awardPoint(1L);
        //it is assumed that the players' score will be 0:0
        assertEquals(0, matchScore.getFirstPlayerScore());
        assertEquals(0, matchScore.getSecondPlayerScore());

        //it is assumed that the games' score will be 1:0
        assertEquals(1, matchScore.getGameScore().getFirstPlayerGame());
        assertEquals(0, matchScore.getGameScore().getSecondPlayerGame());

        // test score 40:40 by points and winner second player ---------------

        service.awardPoint(1L);
        service.awardPoint(2L);

        service.awardPoint(1L);
        service.awardPoint(2L);

        service.awardPoint(1L);
        service.awardPoint(2L);
        //now score is 40:40

        service.awardPoint(2L);
        service.awardPoint(2L);
        //it is assumed that the players' score will be 0:0
        assertEquals(0, matchScore.getFirstPlayerScore());
        assertEquals(0, matchScore.getSecondPlayerScore());

        //it is assumed that the games' score will be 1:1
        assertEquals(1, matchScore.getGameScore().getFirstPlayerGame());
        assertEquals(1, matchScore.getGameScore().getSecondPlayerGame());
    }

    @Test
    void setWinnerFirstPlayerTest(){

        // test score 5:4 by games ---------------

        //5 game for player1
        for(int i = 0; i < 20; i++){
            service.awardPoint(1L);
        }

        //4 game for player2
        for(int i = 0; i < 16; i++){
            service.awardPoint(2L);
        }
        //now score by games is 5:4

        service.awardPoint(1L);
        service.awardPoint(1L);
        service.awardPoint(1L);
        service.awardPoint(1L);

        //it is assumed that the players' score will be 0:0
        assertEquals(0, matchScore.getFirstPlayerScore());
        assertEquals(0, matchScore.getSecondPlayerScore());

        //it is assumed that the games' score will be 0:0
        assertEquals(0, matchScore.getGameScore().getFirstPlayerGame());
        assertEquals(0, matchScore.getGameScore().getSecondPlayerGame());

        //it is assumed that the sets' score will be 1:0
        assertEquals(1, matchScore.getGameScore().getSetScore().getFirstPlayerSet());
        assertEquals(0, matchScore.getGameScore().getSetScore().getSecondPlayerSet());
    }

    @Test
    void setWinnerSecondPlayerTest(){

        // test score 5:6 by games ---------------

        //5 game for player1
        for(int i = 0; i < 20; i++){
            service.awardPoint(1L);
        }

        //6 game for player2
        for(int i = 0; i < 24; i++){
            service.awardPoint(2L);
        }
        //now score by games is 5:6

        service.awardPoint(2L);
        service.awardPoint(2L);
        service.awardPoint(2L);
        service.awardPoint(2L);

        //it is assumed that the players' score will be 0:0
        assertEquals(0, matchScore.getFirstPlayerScore());
        assertEquals(0, matchScore.getSecondPlayerScore());

        //it is assumed that the games' score will be 0:0
        assertEquals(0, matchScore.getGameScore().getFirstPlayerGame());
        assertEquals(0, matchScore.getGameScore().getSecondPlayerGame());

        //it is assumed that the sets' score will be 0:1
        assertEquals(0, matchScore.getGameScore().getSetScore().getFirstPlayerSet());
        assertEquals(1, matchScore.getGameScore().getSetScore().getSecondPlayerSet());
    }

    @Test
    void TieBreakTest(){

        // test score 8:6 by games ---------------

        setTieBreak();
        // play tiebreak
        service.awardPoint(1L);
        assertTrue(matchScore.getGameScore().getSetScore().isTieBreak());

        service.awardPoint(1L);
        // it is assumed that the players' score will be 0:0
        assertEquals(0, matchScore.getFirstPlayerScore());
        assertEquals(0, matchScore.getSecondPlayerScore());

        // it is assumed that the games' score will be 0:0
        assertEquals(0, matchScore.getGameScore().getFirstPlayerGame());
        assertEquals(0, matchScore.getGameScore().getSecondPlayerGame());

        // it is assumed that the sets' score will be 1:0
        assertEquals(1, matchScore.getGameScore().getSetScore().getFirstPlayerSet());
        assertEquals(0, matchScore.getGameScore().getSetScore().getSecondPlayerSet());

        // test score 7:9 by games ---------------

        setTieBreak();
        // play tiebreak
        service.awardPoint(1L);
        assertTrue(matchScore.getGameScore().getSetScore().isTieBreak());

        service.awardPoint(2L);
        service.awardPoint(2L);
        service.awardPoint(2L);
        // it is assumed that the players' score will be 0:0
        assertEquals(0, matchScore.getFirstPlayerScore());
        assertEquals(0, matchScore.getSecondPlayerScore());

        // it is assumed that the games' score will be 0:0
        assertEquals(0, matchScore.getGameScore().getFirstPlayerGame());
        assertEquals(0, matchScore.getGameScore().getSecondPlayerGame());

        // it is assumed that the sets' score will be 1:0
        assertEquals(1, matchScore.getGameScore().getSetScore().getFirstPlayerSet());
        assertEquals(1, matchScore.getGameScore().getSetScore().getSecondPlayerSet());
    }

    void setTieBreak(){

        // 5 game for player1
        for(int i = 0; i < 20; i++){
            service.awardPoint(1L);
        }

        // 5 game for player2
        for(int i = 0; i < 20; i++){
            service.awardPoint(2L);
        }
        // now score by games is 5:5

        // 6th game for player1
        service.awardPoint(1L);
        service.awardPoint(2L);

        service.awardPoint(1L);
        service.awardPoint(2L);

        service.awardPoint(1L);
        service.awardPoint(2L);

        service.awardPoint(1L);
        service.awardPoint(1L);

        // 6th game for player 2
        service.awardPoint(1L);
        service.awardPoint(2L);

        service.awardPoint(1L);
        service.awardPoint(2L);

        service.awardPoint(1L);
        service.awardPoint(2L);

        service.awardPoint(2L);
        service.awardPoint(2L);
    }

    @Test
    void matchWinnerFirstPlayerTest(){

        // test score 2:0 by sets ---------------

        //6 game for player1
        for(int i = 0; i < 24; i++){
            service.awardPoint(1L);
        }

        //6 game for player1
        for(int i = 0; i < 24; i++){
            service.awardPoint(1L);
        }

        // it is assumed that the players' score will be 0:0
        assertEquals(0, matchScore.getFirstPlayerScore());
        assertEquals(0, matchScore.getSecondPlayerScore());

        // it is assumed that the games' score will be 0:0
        assertEquals(0, matchScore.getGameScore().getFirstPlayerGame());
        assertEquals(0, matchScore.getGameScore().getSecondPlayerGame());

        assertEquals(1L, matchScore.getMatch().getWinner().getId());

        // test score 2:1 by sets ---------------

        //6 game for player1
        for(int i = 0; i < 24; i++){
            service.awardPoint(1L);
        }

        //6 game for player2
        for(int i = 0; i < 24; i++){
            service.awardPoint(2L);
        }

        //6 game for player1
        for(int i = 0; i < 24; i++){
            service.awardPoint(1L);
        }

        // it is assumed that the players' score will be 0:0
        assertEquals(0, matchScore.getFirstPlayerScore());
        assertEquals(0, matchScore.getSecondPlayerScore());

        // it is assumed that the games' score will be 0:0
        assertEquals(0, matchScore.getGameScore().getFirstPlayerGame());
        assertEquals(0, matchScore.getGameScore().getSecondPlayerGame());

        assertEquals(1L, matchScore.getMatch().getWinner().getId());
    }

    @Test
    void matchWinnerSecondPlayerTest(){

        // test score 0:2 by sets ---------------

        //6 game for player2
        for(int i = 0; i < 24; i++){
            service.awardPoint(2L);
        }

        //6 game for player2
        for(int i = 0; i < 24; i++){
            service.awardPoint(2L);
        }

        // it is assumed that the players' score will be 0:0
        assertEquals(0, matchScore.getFirstPlayerScore());
        assertEquals(0, matchScore.getSecondPlayerScore());

        // it is assumed that the games' score will be 0:0
        assertEquals(0, matchScore.getGameScore().getFirstPlayerGame());
        assertEquals(0, matchScore.getGameScore().getSecondPlayerGame());

        assertEquals(2L, matchScore.getMatch().getWinner().getId());

        // test score 1:2 by sets ---------------

        //6 game for player2
        for(int i = 0; i < 24; i++){
            service.awardPoint(2L);
        }

        //6 game for player1
        for(int i = 0; i < 24; i++){
            service.awardPoint(1L);
        }

        //6 game for player2
        for(int i = 0; i < 24; i++){
            service.awardPoint(2L);
        }

        // it is assumed that the players' score will be 0:0
        assertEquals(0, matchScore.getFirstPlayerScore());
        assertEquals(0, matchScore.getSecondPlayerScore());

        // it is assumed that the games' score will be 0:0
        assertEquals(0, matchScore.getGameScore().getFirstPlayerGame());
        assertEquals(0, matchScore.getGameScore().getSecondPlayerGame());

        assertEquals(2L, matchScore.getMatch().getWinner().getId());
    }
}
