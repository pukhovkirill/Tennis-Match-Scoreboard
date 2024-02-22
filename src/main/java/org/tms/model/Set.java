package org.tms.model;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
public class Set {
    private int firstPlayerSet = 0;

    private int secondPlayerSet = 0;

    private boolean isTieBreak = false;
}
