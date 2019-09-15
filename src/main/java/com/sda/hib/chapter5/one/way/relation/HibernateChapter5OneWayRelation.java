package com.sda.hib.chapter5.one.way.relation;

import com.sda.hib.HibernateUtils;
import com.sda.hib.chapter4.many.to.many.model.Course;
import com.sda.hib.chapter4.many.to.many.model.Student;
import com.sda.hib.chapter5.one.way.relation.model.Boyfriend;
import com.sda.hib.chapter5.one.way.relation.model.Girlfriend;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.Arrays;
import java.util.List;

public class HibernateChapter5OneWayRelation {

    public static void main(String... args) {

        //Do tej pory tworzyliśmy dwustronne relacje za pomocą hibernate, prawda jest taka że czasami wystarczy nam relacja jednostronna
        // Przykład poniżej: Rafał utrzymuje że chodzi z Emilią (przepraszam jeśli kogoś uraziłem)
        // problem jest taki że Emilia nic o tym nie wie.

        HibernateChapter5OneWayRelation hibernateChapter5OneWayRelation = new HibernateChapter5OneWayRelation();
        hibernateChapter5OneWayRelation.deleteObjects();
        hibernateChapter5OneWayRelation.createAndSaveObjects();
        hibernateChapter5OneWayRelation.relationFromBoyFriend();
        hibernateChapter5OneWayRelation.relationFromGirlFriend();
    }

    private void createAndSaveObjects() {
        try (SessionFactory factory = HibernateUtils.buildSessionFactory()) {
            Session session = factory.getCurrentSession();
            session.getTransaction().begin();

            Boyfriend boyfriend = new Boyfriend("Rafał");
            Girlfriend girlfriend = new Girlfriend("Emilia");

            boyfriend.setGirlfriend(girlfriend);

            session.save(boyfriend);
            session.save(girlfriend);

            session.getTransaction().commit();
            session.close();
        }
    }

    private void relationFromBoyFriend() {
        try (SessionFactory factory = HibernateUtils.buildSessionFactory()) {
            Session session = factory.getCurrentSession();
            session.getTransaction().begin();

            String hql = "Select b from Boyfriend b";

            Query<Boyfriend> query = session.createQuery(hql);
            Boyfriend boyfriend = query.getSingleResult(); // to też słabe, normalnie nie wolno nam zakładać w takim wypadku że będzie tylko jeden wpis znaleziony,
            // moglibyśmy zakładać tak gdybysmy szukali po unikalnej kolumnie i mimo to w przypadku gdy nic nie znajdzie to rzuci wyjątkiem
            // ale aplikacja jest prosta i uzywa po jednym obiekcie w typie Boyfriend i Girlfiend a na początku działania aplikacji usuwane jest wszytko, więc sobie pozwoliłem

            System.out.println(boyfriend.getName() + " + " +  boyfriend.getGirlfriend().getName()); //Tutaj Rafał chwali się "swoją" dziewczyna

            session.close();
        }
    }

    private void relationFromGirlFriend() {
        try (SessionFactory factory = HibernateUtils.buildSessionFactory()) {
            Session session = factory.getCurrentSession();
            session.getTransaction().begin();

            String hql = "Select g from Girlfriend g";

            Query<Girlfriend> query = session.createQuery(hql);
            Girlfriend girlfriend = query.getSingleResult(); // to też słabe,

            // girlfriend.getBoyfriend() nie ma :-), dla obiekt Girlfriend nie ma w ogóle świadomości o istnieniu Boyfriend-a.

            session.close();
        }
    }

    private void deleteObjects() {
        try (SessionFactory factory = HibernateUtils.buildSessionFactory()) {
            Session session = factory.getCurrentSession();
            session.getTransaction().begin();

            String hql = "Select s from Boyfriend s";

            Query<Boyfriend> query = session.createQuery(hql);
            List<Boyfriend> boyfriends = query.getResultList();

            boyfriends.forEach(boyfriend -> session.delete(boyfriend));

            String hql2 = "Select c from Girlfriend c";

            Query<Girlfriend> query2 = session.createQuery(hql2);
            List<Girlfriend> girlfriends = query2.getResultList();

            girlfriends.forEach(girlfriend -> session.delete(girlfriend));

            session.getTransaction().commit();
            session.close();
        }
    }
}

