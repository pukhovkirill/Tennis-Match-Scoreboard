package org.tms.repository;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.tms.entity.Match;
import org.tms.utility.HibernateUtility;

import java.util.List;
import java.util.Optional;

public class MatchRepository implements ModelRepository<Match>{
    @Override
    public Optional<Match> find(Long id) {
        Session session = HibernateUtility.getSessionFactory().openSession();
        Match match = null;
        try{
            session.beginTransaction();

            match = session.find(Match.class, id);

            session.getTransaction().commit();
        }catch (RuntimeException ex){
            session.getTransaction().rollback();
            ex.printStackTrace();
        }finally {
            session.close();
        }

        return Optional.ofNullable(match);
    }

    public List<Match> findByWinner(String name) {
        Session session = HibernateUtility.getSessionFactory().openSession();
        List<Match> matches = null;
        try{
            final String hql = "select m from match m join player p on m.winner.id = p.id where p.name = :name";
            Query<Match> query = session.createQuery(hql, Match.class);
            query.setParameter("name", name);
            matches = query.list();
        }catch(RuntimeException ex){
            ex.printStackTrace();
        }finally {
            session.close();
        }

        return matches;
    }

    public List<Match> findByPlayer(String name){
        Session session = HibernateUtility.getSessionFactory().openSession();
        List<Match> matches = null;
        try{
            session.beginTransaction();
            final String hql =
                    "select m from match m " +
                    "join player p on m.firstPlayer.id = p.id " +
                    "join player p1 on m.secondPlayer.id = p1.id " +
                    "where p.name = :name1 or p1.name = :name2";
            Query<Match> query = session.createQuery(hql, Match.class);
            query.setParameter("name1", name);
            query.setParameter("name2", name);

            matches = query.list();

            session.getTransaction().commit();
        }catch (RuntimeException ex){
            ex.printStackTrace();
        }finally {
            session.close();
        }

        return matches;
    }

    @Override
    public List<Match> findAll() {
        Session session = HibernateUtility.getSessionFactory().openSession();
        List<Match> matches = null;
        try{
            final String sql = "select m from match m";
            Query<Match> query = session.createQuery(sql, Match.class);
            matches = query.list();
        }catch (RuntimeException ex){
            ex.printStackTrace();
        }finally {
            session.close();
        }

        return matches;
    }

    @Override
    public void save(Match entity) {
        Session session = HibernateUtility.getSessionFactory().openSession();
        try{
            session.beginTransaction();

            session.persist(entity);

            session.getTransaction().commit();
        }catch (RuntimeException ex){
            ex.printStackTrace();
        }finally {
            session.close();
        }
    }

    @Override
    public void update(Match entity) {
        Session session = HibernateUtility.getSessionFactory().openSession();
        try{
            session.beginTransaction();

            var match = session.getReference(Match.class, entity.getId());
            match.setFirstPlayer(entity.getFirstPlayer());
            match.setSecondPlayer(entity.getSecondPlayer());
            match.setWinner(entity.getWinner());

            session.getTransaction().commit();
        }catch(RuntimeException ex){
            ex.printStackTrace();
        }finally {
            session.close();
        }
    }

    @Override
    public void delete(Long id) {
        Session session = HibernateUtility.getSessionFactory().openSession();
        try{
            session.beginTransaction();

            var match = session.find(Match.class, id);
            if(match != null)
                session.remove(match);

            session.getTransaction().commit();
        }catch (RuntimeException ex){
            ex.printStackTrace();
        }finally {
            session.close();
        }
    }
}
