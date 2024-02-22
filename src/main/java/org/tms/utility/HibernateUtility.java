package org.tms.utility;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.tms.entity.Match;
import org.tms.entity.Player;
import org.tms.repository.MatchRepository;
import org.tms.repository.PlayerRepository;

public class HibernateUtility {
    private static SessionFactory factory;

    static {
        final var matchesRepository = new MatchRepository();
        final var playerRepository = new PlayerRepository();

        Player player1 = new Player();
        player1.setName("К. АЛЬКАРАС");
        playerRepository.save(player1);

        Player player2 = new Player();
        player2.setName("Ф. КОБОЛЛИ");
        playerRepository.save(player2);

        Match match1 = new Match();
        match1.setFirstPlayer(player1);
        match1.setSecondPlayer(player2);
        match1.setWinner(player1);
        matchesRepository.save(match1);

        Player player3 = new Player();
        player3.setName("Д. МЕДВЕДЕВ");
        playerRepository.save(player3);

        Player player4 = new Player();
        player4.setName("Т. УАЙЛД");
        playerRepository.save(player4);

        Match match2 = new Match();
        match2.setFirstPlayer(player3);
        match2.setSecondPlayer(player4);
        match2.setWinner(player4);
        matchesRepository.save(match2);

        Player player5 = new Player();
        player5.setName("Н. ДЖОКОВИЧ");
        playerRepository.save(player5);

        Player player6 = new Player();
        player6.setName("М. ФУЧОВИЧ");
        playerRepository.save(player6);

        Match match3 = new Match();
        match3.setFirstPlayer(player5);
        match3.setSecondPlayer(player6);
        match3.setWinner(player5);
        matchesRepository.save(match3);

        Player player7 = new Player();
        player7.setName("К. ХАЧАНОВ");
        playerRepository.save(player7);

        Player player8 = new Player();
        player8.setName("К. РУУД");
        playerRepository.save(player8);

        Match match4 = new Match();
        match4.setFirstPlayer(player7);
        match4.setSecondPlayer(player8);
        match4.setWinner(player8);
        matchesRepository.save(match4);

        Player player9 = new Player();
        player9.setName("К. ХАЧАНОВ");
        playerRepository.save(player9);

        Player player10 = new Player();
        player10.setName("К. РУУД");
        playerRepository.save(player10);

        Match match5 = new Match();
        match5.setFirstPlayer(player9);
        match5.setSecondPlayer(player10);
        match5.setWinner(player9);
        matchesRepository.save(match5);
    }


    private static SessionFactory buildSessionFactory(){
        try{
            var configuration = new Configuration();
            configuration.configure("hibernate.cfg.xml");
            configuration.addAnnotatedClass(Match.class);
            configuration.addAnnotatedClass(Player.class);
            return configuration.buildSessionFactory();
        }catch(Throwable ex){
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory(){
        if(factory == null)
            factory = buildSessionFactory();

        return factory;
    }
}
