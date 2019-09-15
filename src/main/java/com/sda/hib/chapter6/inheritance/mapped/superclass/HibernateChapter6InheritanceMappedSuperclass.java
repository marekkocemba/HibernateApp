package com.sda.hib.chapter6.inheritance.mapped.superclass;

import com.sda.hib.HibernateUtils;
import com.sda.hib.chapter5.one.way.relation.model.Boyfriend;
import com.sda.hib.chapter5.one.way.relation.model.Girlfriend;
import com.sda.hib.chapter6.inheritance.mapped.superclass.model.Animal;
import com.sda.hib.chapter6.inheritance.mapped.superclass.model.Cat;
import com.sda.hib.chapter6.inheritance.mapped.superclass.model.Dog;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.List;

public class HibernateChapter6InheritanceMappedSuperclass {

    public static void main(String... args) {

        HibernateChapter6InheritanceMappedSuperclass hibernateChapter6InheritanceMappedSuperclass = new HibernateChapter6InheritanceMappedSuperclass();
        hibernateChapter6InheritanceMappedSuperclass.deleteObjects();
        hibernateChapter6InheritanceMappedSuperclass.createAndSaveObjects();
        hibernateChapter6InheritanceMappedSuperclass.getObjects();
    }

    private void createAndSaveObjects() {
        try (SessionFactory factory = HibernateUtils.buildSessionFactory()) {
            Session session = factory.getCurrentSession();
            session.getTransaction().begin();

            Animal animal =  new Animal();
            animal.setName("ami");
           // session.save(animal); wyrzuci wyjątek Animal jako @MappedSuperclass nie jest encją

            Dog dog = new Dog();
            dog.setName("Reks");
            dog.setBone(true);

            session.save(dog);

            Cat cat = new Cat();
            cat.setName("Tofi");
            cat.setMilk(true);

            session.save(cat);

            session.getTransaction().commit();
            session.close();
        }
    }



    private void deleteObjects() {
        try (SessionFactory factory = HibernateUtils.buildSessionFactory()) {
            Session session = factory.getCurrentSession();
            session.getTransaction().begin();

            String hql = "Select d from Dog d";

            Query<Dog> query = session.createQuery(hql);
            List<Dog> dogs = query.getResultList();

            dogs.forEach(dog -> session.delete(dog));

            String hql2 = "Select c from Cat c";

            Query<Cat> query2 = session.createQuery(hql2);
            List<Cat> cats = query2.getResultList();

            cats.forEach(cat -> session.delete(cat));

            session.getTransaction().commit();
            session.close();
        }
    }

    private void getObjects() {
        try (SessionFactory factory = HibernateUtils.buildSessionFactory()) {
            Session session = factory.getCurrentSession();
            session.getTransaction().begin();

           // String hql = "Select a from Animal a";
           // Query<Animal> query = session.createQuery(hql);
           // List<Animal> animals = query.getResultList(); nie ma polimorfizmu, trzeba każdy podtyp wyciągać osobno

            String hql2 = "Select d from Dog d";

            Query<Dog> query2 = session.createQuery(hql2);
            List<Dog> dogs = query2.getResultList();

            String hql3 = "Select c from Cat c";

            Query<Cat> query3 = session.createQuery(hql3);
            List<Cat> cats = query3.getResultList();
        }
    }
}

