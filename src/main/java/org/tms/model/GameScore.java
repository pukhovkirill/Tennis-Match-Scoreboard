package org.tms.model;

import lombok.Getter;
import lombok.Setter;
import lombok.RequiredArgsConstructor;

@Getter
@Setter
@RequiredArgsConstructor
public class GameScore {

    private final Set setScore;

    private int firstPlayerGame = 0;

    private int secondPlayerGame = 0;
}
