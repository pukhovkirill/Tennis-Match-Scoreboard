package org.tms.model;

import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import lombok.RequiredArgsConstructor;
import org.tms.entity.Match;

@Getter
@Setter
@RequiredArgsConstructor
public class MatchScore {
    private final UUID uuid;

    private final Match match;

    private final GameScore gameScore;

    private int firstPlayerScore = 0;

    private int secondPlayerScore = 0;

    private int firstPlayerAce = 0;

    private int secondPlayerAce = 0;

    public int getPlayerNumber(Long id){
        return match.getFirstPlayer().getId().equals(id) ? 1 : 2;
    }
}
