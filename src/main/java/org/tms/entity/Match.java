package org.tms.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity(name = "match")
@Table(name = "match")
@Getter
@Setter
@NoArgsConstructor
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "first_player_id", referencedColumnName = "id", nullable = false)
    private Player firstPlayer;

    @ManyToOne
    @JoinColumn(name = "second_player_id", referencedColumnName = "id", nullable = false)
    private Player secondPlayer;

    @ManyToOne
    @JoinColumn(name = "winner_id", referencedColumnName = "id", nullable = false)
    private Player winner;
}
