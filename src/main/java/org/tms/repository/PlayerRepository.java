package org.tms.repository;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.tms.entity.Player;
import org.tms.utility.HibernateUtility;

import java.util.List;
import java.util.Optional;

public class PlayerRepository implements ModelRepository<Player>{
    @Override
    public Optional<Player> find(Long id) {
        Session session = HibernateUtility.getSessionFactory().openSession();
        Player player = null;
        try{
            session.beginTransaction();

            player = session.find(Player.class, id);

            session.getTransaction().commit();
        }catch(RuntimeException ex){
            session.getTransaction().rollback();
            ex.printStackTrace();
        }finally{
            session.close();
        }

        return Optional.ofNullable(player);
    }

    public Optional<Player> findByName(String name) {
        Session session = HibernateUtility.getSessionFactory().openSession();

        final String sql = "select p from player p where p.name = :name";
        Query<Player> query = session.createQuery(sql, Player.class);

        Player player = null;
        try{
            query.setParameter("name", name);
            player = query.getSingleResult();
        }catch(RuntimeException ex){
            ex.printStackTrace();
        }finally {
            session.close();
        }

        return Optional.ofNullable(player);
    }

    @Override
    public List<Player> findAll() {
        Session session = HibernateUtility.getSessionFactory().openSession();

        final String sql = "select p from player p";
        Query<Player> query = session.createQuery(sql, Player.class);

        List<Player> players = null;
        try{
            players = query.list();
        }catch(RuntimeException ex){
            ex.printStackTrace();
        }finally {
            session.close();
        }

        return players;
    }

    @Override
    public void save(Player entity) {
        Session session = HibernateUtility.getSessionFactory().openSession();
        try{
            session.beginTransaction();

            session.persist(entity);

            session.getTransaction().commit();
        }catch(RuntimeException ex){
            ex.printStackTrace();
        }finally {
            session.close();
        }
    }

    @Override
    public void update(Player entity) {
        Session session = HibernateUtility.getSessionFactory().openSession();
        try{
            session.beginTransaction();

            var player = session.getReference(Player.class, entity.getId());
            player.setName(entity.getName());

            session.getTransaction().commit();
        }catch (RuntimeException ex){
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

            var player = session.find(Player.class, id);
            if(player != null)
                session.remove(player);

            session.getTransaction().commit();
        }catch (RuntimeException ex){
            ex.printStackTrace();
        }finally {
            session.close();
        }
    }
}