package ru.otus;

import java.util.function.Function;
import javax.persistence.EntityManager;

/**
 *
 */
public class JPQL {
    private final EntityManager em;

    public JPQL(EntityManager em) {
        this.em = em;
    }

    private <R> R runInSession(Function<EntityManager, R> function) {
        em.getTransaction().begin();
        R result = function.apply(em);
        em.getTransaction().commit();
        return result;
    }


}

