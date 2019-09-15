package com.sda.hib.chapter3.one.to.one;

import com.sda.hib.HibernateUtils;
import com.sda.hib.chapter3.one.to.one.model.Husband;
import com.sda.hib.chapter3.one.to.one.model.Wife;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.List;

public class HibernateChapter3OneToOne {

    public static void main(String... args) {


        HibernateChapter3OneToOne hibernateChapter3OneToOne = new HibernateChapter3OneToOne();
        hibernateChapter3OneToOne.deleteObjects(); //sprzątamy po poprzedniej instancji
        hibernateChapter3OneToOne.createAndSaveObjects(); //przygotowywujemy obiekty typu Husbant  i Wife wraz z relacją pomiędzy nimi.
        hibernateChapter3OneToOne.husbantToWifeRelation(); // bawimy się relacją, idziemy od Husbant do Wife
        hibernateChapter3OneToOne.wifeToHusbandRelation(); // bawimy się relacją, idziemy od Wife do Husbant

    }

    private void createAndSaveObjects() {
        try (SessionFactory factory = HibernateUtils.buildSessionFactory()) {
            Session session = factory.getCurrentSession();
            session.getTransaction().begin();

            Husband husband = new Husband("George");
            Wife wife = new Wife("Mary");
            husband.setWife(wife);

            session.save(husband);
            session.save(wife);

            session.getTransaction().commit();
            session.close();
        }
    }

    private void husbantToWifeRelation() {
        try (SessionFactory factory = HibernateUtils.buildSessionFactory()) {
            Session session = factory.getCurrentSession();
            session.getTransaction().begin();

            String hql = "Select h from Husband h ";


            Query<Husband> query = session.createQuery(hql);
            List<Husband> husbands = query.getResultList();

            Husband husband = husbands.get(0); //ogólnie to jest słabe, bo w normalnej aplikacji nie mamy gwarancji na to że coś będzie w liście pod tym indexem, tutaj w ten sposób wyciągamy bo aplikacja jest mała i wiemy co wrzuciliśmy do bazy

            // UWAGA UWAGA :
            System.out.println(husband.getWife().getName()); // oczywiście wyświetli się ale wife została już wcześniej pobrana (tj w 50 linii)

            // dlaczego ?
            // bo mamy tutaj relację @OneToOne z husbant do wife, defaultowo jest ona w typie EAGER (pobiera wife nie ważne czy ją potrzebujemy czy nie )
            // @OneToOne jest równoważne z  @OneToOne(fetch = FetchType.EAGER), // mozesz dla ćwiczenia ustawić w klasie Husband @ManyToOne(fetch = FetchType.LAZY) i zobaczyć co sięstanie

            session.close();
            // akurat w tym przykladzie jest tylko jedna para husbant-wife, ale gdybybyło ich więcej to poza sesją też można było by wyciągać obiekty wife z husband
            // wszystko dzięki defaultowej wartośći FetchType.EAGER w adnotacji @OneToOne
        }
    }

    private void wifeToHusbandRelation() {
        try (SessionFactory factory = HibernateUtils.buildSessionFactory()) {
            Session session = factory.getCurrentSession();
            session.getTransaction().begin();

            String hql = "Select w from Wife w";


            Query<Wife> query = session.createQuery(hql);
            List<Wife> wifes = query.getResultList();

            Wife wife = wifes.get(0);//ogólnie to jest słabe, bo w normalnej aplikacji nie mamy gwarancji na to że coś będzie w liście pod tym indexem, tutaj w ten sposób wyciągamy bo aplikacja jest mała i wiemy co wrzuciliśmy do bazy
            // wszystko dzieje się tak samo co metodzie wyżej, tylko idziemy od drugiej strony
            System.out.println(wife.getHusband().getName());

            session.close();

        }
    }

    private void deleteObjects() {
        try (SessionFactory factory = HibernateUtils.buildSessionFactory()) {
            Session session = factory.getCurrentSession();
            session.getTransaction().begin();

            String hql = "Select h from Husband h";

            Query<Husband> query = session.createQuery(hql);
            List<Husband> husbands = query.getResultList();

            husbands.forEach(husband -> session.delete(husband));

            String hql2 = "Select w from Wife w";

            Query<Wife> query2 = session.createQuery(hql2);
            List<Wife> wifes = query2.getResultList();

            wifes.forEach(wife -> session.delete(wife));

            session.getTransaction().commit();
            session.close();

            //alternatywnie możecie w klasie Husband w adnotacji @OneToOne dopisać kaskadowość czyli @OneToOne(cascade = CascadeType.REMOVE)
            // wtedy nie będziecie musili jawnie kasować obiekty typu Wife bo będą one leciały wraz z kasowaniem przypisanych do nich obiektów Husband
            // to samo można zrobić w adnotacji @OneToMany (klasa Mom, chapter 2), wtedy kasując obiekt typu Mom, lecą wszystkie dzieci.
            //oraz w @ManyToMany (klasa Student, Course, chapter 4)
        }
    }
}

