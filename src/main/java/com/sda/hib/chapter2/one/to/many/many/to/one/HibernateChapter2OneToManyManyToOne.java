package com.sda.hib.chapter2.one.to.many.many.to.one;

import com.sda.hib.HibernateUtils;
import com.sda.hib.chapter2.one.to.many.many.to.one.model.Child;
import com.sda.hib.chapter2.one.to.many.many.to.one.model.Mom;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.time.LocalDate;
import java.util.List;

public class HibernateChapter2OneToManyManyToOne {

    public static void main(String... args) {


        HibernateChapter2OneToManyManyToOne hibernateChapter2OneToManyManyToOne = new HibernateChapter2OneToManyManyToOne();
        hibernateChapter2OneToManyManyToOne.delete();//usuwamy wszystkio z tabel moms i children, sprzatamy po poprzedniej instancji
        hibernateChapter2OneToManyManyToOne.createAndSaveObjects(); //przygotowywujemy obiekty typu Mom i Child wraz z relacją pomiędzy nimi.
        hibernateChapter2OneToManyManyToOne.oneToManyRelation(); // bawimy się relacją, idziemy od strony mom to children czyli jeden do wielu
        hibernateChapter2OneToManyManyToOne.manyToOneRelation(); // bawimy się relacją, idziemy od strony child to mom czyli wiele do jednego

    }

    private void createAndSaveObjects() {
        Mom magdaMom = new Mom("Magda", 40); // tworzymy pierwszy obiekt klasy Mom,
        Child bartekChild = new Child(magdaMom, "Bartek", LocalDate.of(2000, 10, 15));//tworzymy obiekt Child będzie on w relacji wiele do jednego z obiektem klasy Mom
        Child milenaChild = new Child(magdaMom, "Milena", LocalDate.of(2003, 6, 1));//tworzymy obiekt Child będzie on w relacji wiele do jednego z obiektem klasy Mom
        Child sylwiaChild = new Child(magdaMom, "Sylwia", LocalDate.of(2006, 9, 10));//tworzymy obiekt Child będzie on w relacji wiele do jednego z obiektem klasy Mom
     //   magdaMom.setChildren(Arrays.asList(bartekChild, milenaChild, sylwiaChild));

        Mom dominikaMom = new Mom("Dominika", 34); // tworzymy drugi obiekt klasy Mom,
        Child synekChild = new Child(dominikaMom, "Synek", LocalDate.of(2010, 1, 1));//tworzymy obiekt Child będzie on w relacji wiele do jednego z obiektem klasy Mom
     //   dominikaMom.setChildren(Arrays.asList(synekChild));

        try (SessionFactory factory = HibernateUtils.buildSessionFactory()) {
            Session session = factory.getCurrentSession();
            session.getTransaction().begin();

            //zapisujemy wszystkie obiekty
            session.save(magdaMom);
            session.save(bartekChild);
            session.save(milenaChild);
            session.save(sylwiaChild);

            session.save(dominikaMom);
            session.save(synekChild);

            session.getTransaction().commit();
            session.close();
        }
    }

    private void oneToManyRelation() {
        try (SessionFactory factory = HibernateUtils.buildSessionFactory()) {
            Session session = factory.getCurrentSession();
            session.getTransaction().begin();

            String hql ="Select m from Mom m";

            Query<Mom> query = session.createQuery(hql);
            List<Mom> momList = query.getResultList(); // wyciągnięta lista obiektów mom jest od razu podłączony do sesji

            Mom firstMom= momList.get(0); // bierzemy pierwszą mamę i drugą , tak szczerze nie jest ważne która jest która
            Mom secondMom= momList.get(1);//ogólnie to jest słabe, bo w normalnej aplikacji nie mamy gwarancji na to że coś będzie w liście pod tym indexem, tutaj w ten sposób wyciągamy bo aplikacja jest mała i wiemy co wrzuciliśmy do bazy
            firstMom.getChildren().forEach(child -> System.out.println(child));// w tym momencie nastapi doczytanie dzieci z tabeli children dla pierwszej mamy
            //puki jesteśmy w sesji w żaden sposób nie interesujemy się dzieckiem dziecmi drugiej mamy

            //mimo że nie wywolalismy session.merge(user), typ usera zostanie zmieniony,
            // ponieważ zadziała mechanizm wykrywania zmian na wyciągniętym obiekcie podłączonym do sesji
            session.close();
            // od tej linii obiekty są odłączone od sesji
          //  secondMom.getChildren().forEach(child -> System.out.println(child));
            // jeśli odkomentujesz powyzszą linie, wyskoczy błąd, o co chodzi, tutaj nałożyły się dwie rzeczy

            // w przeciwieństwie do firstMom.getChildren() z linii 62-giej, tutaj jesteśmy po za sesją, obiekt secondMom jest w stanie odłączonym
            // firstMom, też jest zresztą odłączony, ale dla firstMom wewnątrz sesji w linii 62 giej zostały doczytane jej dzieci tak więc w tym przypadku wywołanie ponowne tutaj
            // firstMom.getChildren() nie sypnie wyjątkiem

            // leniwe ładowanie popatrz że klasa mom ma relacje z child przy użyciu adnotacji @OneToMany
            // adnotacja ta ma defaultowy parametr FetchType.LAZY, czyli doczytujemy dzieci tylko w sesji i to na nasze żądanie
            // @OneToMany jest równoważne z  @OneToMany(fetch = FetchType.LAZY), ustawienie @OneToMany(fetch = FetchType.EAGER) sprawi że dzieci będą wyciągane z bazy danych
            //dla kazdego obiektu typu Mom nawet jeśli nie będziemy ich nigdzie używać

            //PROBLEM
            // możemy robić selecta wg warunków  String hql ="Select m from Mom m WHERE m.age < 45"; String hql ="Select m from Mom m WHERE m.name like '%ominik%'";
            // ale jak możemy wybrać mame której dziecko ma np na imie Bartek ? Jedną z możliwości jest pójście od drugiej strony relacji
            // pokazane jest to w metodzie manyToOneRelation()
        }
    }

    private void manyToOneRelation() {
        try (SessionFactory factory = HibernateUtils.buildSessionFactory()) {
            Session session = factory.getCurrentSession();
            session.getTransaction().begin();

            String hql = "Select c from Child c WHERE name = 'Bartek' OR name = 'Synek'"; // zależy mi na wyciągnięciu 2 dzieci, każde pochodzące od innej mamy


            Query<Child> query = session.createQuery(hql);
            List<Child> children = query.getResultList();

            Child firstChild = children.get(0); // wyciągamy pierwsze dziecko z listy dzieci
            Child secondChild = children.get(1); // wyciągamy drugie dziecko z listy dzieci
//ogólnie to jest słabe, bo w normalnej aplikacji nie mamy gwarancji na to że coś będzie w liście pod tym indexem, tutaj w ten sposób wyciągamy bo aplikacja jest mała i wiemy co wrzuciliśmy do bazy

            // UWAGA UWAGA :
            System.out.println(firstChild.getMom().getName()); // oczywiście wyświetli się ale mama została już wcześniej pobrana (tj w 103 linii)

            // dlaczego ?
            // bo idziemy od drugiej strony i mamy tutaj relację @ManyToOne z dziecka do mamy, defaultowo jest ona w typie EAGER (pobiera mame nie ważne czy ją potrzebujemy czy nie )
            // @ManyToOne jest równoważne z  @ManyToOne(fetch = FetchType.EAGER), // mozesz dla ćwiczenia ustawić w klasie CHild @ManyToOne(fetch = FetchType.LAZY) i zobaczyć co sięstanie

            // Druga sprawa. Czy właśnie nie rozwiązaliśmy problemu który wyłożyłem metode wyżej ? Mamy dzieci oraz mamy wg oczekiwanych imion dzieci.
            session.close();

            System.out.println(secondChild.getMom().getName()); // oczywiście to zadziała, dzięki FetchType.EAGER, mama została zaczytana dla dziecka w 104 linii.
        }
    }

    private void delete() {
        try (SessionFactory factory = HibernateUtils.buildSessionFactory()) {
            Session session = factory.getCurrentSession();
            session.getTransaction().begin();

            String hql = "Select c from Child c";

            Query<Child> query = session.createQuery(hql);
            List<Child> children = query.getResultList();

            children.forEach(child -> session.delete(child)); //podłączamy kazdy obiekt typu child do sesji z przeznaczeniem do usuniecia

            String hql2 = "Select m from Mom m";

            Query<Mom> query2 = session.createQuery(hql2);
            List<Mom> moms = query2.getResultList();

            moms.forEach(mom -> session.delete(mom));//podłączamy kazdy obiekt typu mom do sesji z przeznaczeniem do usuniecia

            session.getTransaction().commit();
            session.close();
        }
    }
}

