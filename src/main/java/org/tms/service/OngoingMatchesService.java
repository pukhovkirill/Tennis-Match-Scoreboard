package org.tms.service;

import org.tms.entity.Match;
import org.tms.entity.Player;
import org.tms.model.GameScore;
import org.tms.model.MatchScore;
import org.tms.model.Set;
import org.tms.repository.PlayerRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class OngoingMatchesService {
    private static final Map<UUID, MatchScore> storage = new HashMap<>();
    private final PlayerRepository playerRepository;

    public OngoingMatchesService(){
        this.playerRepository = new PlayerRepository();
    }

    public MatchScore createNewMatch(String playerName1, String playerName2){
        Player player1 = findOrPersist(playerName1);
        Player player2 = findOrPersist(playerName2);

        var newMatch = buildMatchScore(player1, player2);
        var uuid = newMatch.getUuid();

        storage.put(uuid, newMatch);

        return newMatch;
    }

    public Optional<MatchScore> getMatch(UUID uuid){
        return Optional.ofNullable(storage.get(uuid));
    }

    public void finishMatch(UUID uuid){
        storage.remove(uuid);
    }

    private MatchScore buildMatchScore(Player player1, Player player2){
        var uuid = UUID.randomUUID();

        var match = new Match();
        match.setFirstPlayer(player1);
        match.setSecondPlayer(player2);

        var gameScore = new GameScore(new Set());

        return new MatchScore(uuid, match, gameScore);
    }

    private Player findOrPersist(String playerName){
        Player player;

        var optionalPlayer = playerRepository.findByName(playerName);

        if(optionalPlayer.isPresent()){
            player = optionalPlayer.get();
        }else{
            player = new Player();
            player.setName(playerName);
            playerRepository.save(player);
        }

        return player;
    }
}
