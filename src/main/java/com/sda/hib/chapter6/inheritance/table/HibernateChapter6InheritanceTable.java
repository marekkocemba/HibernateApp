package com.sda.hib.chapter6.inheritance.table;

import com.sda.hib.HibernateUtils;
import com.sda.hib.chapter6.inheritance.mapped.superclass.model.Animal;
import com.sda.hib.chapter6.inheritance.mapped.superclass.model.Cat;
import com.sda.hib.chapter6.inheritance.mapped.superclass.model.Dog;
import com.sda.hib.chapter6.inheritance.table.model.InternetShop;
import com.sda.hib.chapter6.inheritance.table.model.Shop;
import com.sda.hib.chapter6.inheritance.table.model.Supermarket;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.List;

public class HibernateChapter6InheritanceTable {

    public static void main(String... args) {
// sprawdz klase Shop, zachęcam do obserwowania jak tabele są ułożone przy kolejnej strategi (jednej z trzech)
        HibernateChapter6InheritanceTable hibernateChapter6InheritanceTable = new HibernateChapter6InheritanceTable();
        hibernateChapter6InheritanceTable.deleteObjects();
        hibernateChapter6InheritanceTable.createAndSaveObjects();
        hibernateChapter6InheritanceTable.getObjects();
    }

    private void createAndSaveObjects() {
        try (SessionFactory factory = HibernateUtils.buildSessionFactory()) {

            Session session = factory.getCurrentSession();
            session.getTransaction().begin();

            InternetShop internet = new InternetShop("Poznań ul Borowska 34 ... ","kupuj.pl");
            Supermarket supermarket = new Supermarket("Poznań ul Rataje 3 ... ", 100L);

            session.save(internet);
            session.save(supermarket);

            session.getTransaction().commit();
            session.close();
        }
    }

    private void getObjects() {
        try (SessionFactory factory = HibernateUtils.buildSessionFactory()) {
            Session session = factory.getCurrentSession();
            session.getTransaction().begin();

            String hql = "Select s from Shop s"; // mozemy Select s from InternetShop s lub Supermarket ale wykorzystajmy polimorfizm
            Query<Shop> query = session.createQuery(hql);
            List<Shop> shops = query.getResultList();

            shops.forEach(shop ->{
                if (shop instanceof  Supermarket){
                    Supermarket supermarket = (Supermarket) shop; // rzutowanie
                    System.out.println("typ: " + shop.getClass()+ " ilosc miejsc parkingowych: "+supermarket.getParkingPlaces());
                }else if (shop instanceof  InternetShop){
                    InternetShop internetShop = (InternetShop) shop; // rzutowanie
                    System.out.println("typ: " + shop.getClass()+ " strona: "+internetShop.getWww());
                }
            });

            // Podsumowując, która strategia najlepsza? SINGLE_TABLE, JOINED, TABLE_PER_CLASS ?
            // W przypadku TABLE_PER_CLASS, zapytania są pobierane rezem ze wszystkich dziedziczących tabel za pomocą polecenia UNION, w przypadku wielu tabel może być to powodem spowolnien w zapytaniu
            // W przypadku JOINED, zapytania są pobierane rezem ze wszystkich dziedziczących tabel za pomocą polecenia JOIN, w przypadku wielu tabel może być to powodem spowolnien w zapytaniu
            // W przypadku SINGLE_TABLE, wszystko pobierane jest jednym pytaniem, niestety będziemy miec wiele nieużywanych kolumn

        }
    }

    private void deleteObjects() {
        try (SessionFactory factory = HibernateUtils.buildSessionFactory()) {
            Session session = factory.getCurrentSession();
            session.getTransaction().begin();

            String hql = "Select s from Supermarket s";
            Query<Supermarket> query = session.createQuery(hql);
            List<Supermarket> supermarkets = query.getResultList();
            supermarkets.forEach(supermarket -> session.delete(supermarket));

            String hql2 = "Select s from InternetShop s";
            Query<InternetShop> query2 = session.createQuery(hql2);
            List<InternetShop> internetShops = query2.getResultList();
            internetShops.forEach(internetShop -> session.delete(internetShop));

            String hql3 = "Select s from Shop s";
            Query<Shop> query3 = session.createQuery(hql3);
            List<Shop> shops = query3.getResultList();
            shops.forEach(shop -> session.delete(shop));

            session.close();
        }
    }


}

