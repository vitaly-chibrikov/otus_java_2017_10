package ru.otus;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.h2.tools.Server;

import ru.otus.data.Account;
import ru.otus.data.UserDataSet;

class ConnectionHelper {

    public static void main(String[] args) throws SQLException {
        Server.createWebServer("-web","-webAllowOthers","-webPort","8082").start();

        DbService dbService = new DbService();
//        _jpql(dbService);
        _criteria(dbService);
    }

    /**
     * https://docs.oracle.com/javaee/6/tutorial/doc/bnbtl.html#bnbtn
     * http://blog.jbaysolutions.com/2014/10/16/jpa-2-tutorial-queries-on-the-model/
     *
     */
    private static void _jpql(DbService dbService) {
        EntityManager em = dbService.getEntityManager();



        // INSERT new record
        em.getTransaction().begin();
        em.persist(new Account("A"));
        em.persist(new Account("B"));
        em.persist(new Account("C"));
        em.getTransaction().commit();


        // SELECT
        // Use Java Object name - Account
        String jpql = "select acc from Account as acc";
        Query query = em.createQuery(jpql);
        List r = query.getResultList();
        System.out.println("select all: " + r);

        // SELECT with params
        query = em.createQuery("select acc from Account as acc where acc.name=:name");
        query.setParameter("name", "A");
        r = query.getResultList();
        System.out.println("select one: " + r);


        // SELECT typed
        TypedQuery<Account> query1 = em.createQuery("SELECT acc FROM Account AS acc WHERE acc.id=:id", Account.class);
        Account resultList = query1.getSingleResult();

    }

    private static void _criteria(DbService dbService) {
        final EntityManager em = dbService.getEntityManager();

        // INSERT new record
        em.getTransaction().begin();
        em.persist(new Account("A"));
        em.persist(new Account("B"));
        em.persist(new Account("C"));
        em.getTransaction().commit();



        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Account> query = builder.createQuery(Account.class);
        Root<Account> acc = query.from(Account.class);

        Predicate cond = builder.gt(acc.get("id"), 1);
        query.where(cond);
        TypedQuery<Account> q = em.createQuery(query);
        List<Account> resultList = q.getResultList();
        System.out.println(resultList);


    }




    private static void hibernate_(DbService dbService) {
        UserDataSet dataSet = new UserDataSet("deadbeaf", null, null);
        dbService.save(dataSet);
        System.out.println("result: " + dbService.read(1L));
    }
}
