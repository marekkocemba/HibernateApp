package com.sda.hib.chapter4.many.to.many;

import com.sda.hib.HibernateUtils;
import com.sda.hib.chapter3.one.to.one.model.Husband;
import com.sda.hib.chapter3.one.to.one.model.Wife;
import com.sda.hib.chapter4.many.to.many.model.Course;
import com.sda.hib.chapter4.many.to.many.model.Student;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.Arrays;
import java.util.List;

public class HibernateChapter4ManyToMany {

    public static void main(String... args) {


        HibernateChapter4ManyToMany hibernateChapter4ManyToMany = new HibernateChapter4ManyToMany();
        hibernateChapter4ManyToMany.deleteObjects(); //sprzątamy po poprzedniej instancji
        hibernateChapter4ManyToMany.createAndSaveObjects(); //przygotowywujemy obiekty typu Husband  i Wife wraz z relacją pomiędzy nimi.
        hibernateChapter4ManyToMany.studentToCoursesRelation(); // bawimy się relacją, idziemy od Husbant do Wife
        hibernateChapter4ManyToMany.courseToStudentsRelation(); // bawimy się relacją, idziemy od Wife do Husbant

    }

    private void createAndSaveObjects() {
        try (SessionFactory factory = HibernateUtils.buildSessionFactory()) {
            Session session = factory.getCurrentSession();
            session.getTransaction().begin();

            Student student1 = new Student("Michał", "Lewicki", 108166);
            Student student2 = new Student("Andrzej", "Lewicki", 108167);
            Student student3 = new Student("Krzysztof", "Kubiak", 108168);
            Student student4 = new Student("Julia", "Gunther", 108169);
            Student student5 = new Student("Katarzyna", "Morison", 108170);

            Course course1 = new Course("Algebra liniowa");
            Course course2 = new Course("Analiza matematyczna");

            // zauważcie że nie musicie ustawiać relacji obustronnie czyli
//            student1.setCourseList(Arrays.asList(course1));
//            course1.setStudentList(Arrays.asList(student1));

            // wystarczy ustawić listę po jednej stronie

            student1.setCourseList(Arrays.asList(course1,course2));
            student2.setCourseList(Arrays.asList(course1,course2));
            student3.setCourseList(Arrays.asList(course1));
            student4.setCourseList(Arrays.asList(course1));
            student5.setCourseList(Arrays.asList(course1));

            session.save(student1);
            session.save(student2);
            session.save(student3);
            session.save(student4);
            session.save(student5);

            session.save(course1);
            session.save(course2);

            session.getTransaction().commit();
            session.close();
        }
    }

    private void studentToCoursesRelation(){
        try (SessionFactory factory = HibernateUtils.buildSessionFactory()) {
            // sprawdzamy na jakie kursy zapisani są wybrani studenci
            Session session = factory.getCurrentSession();
            session.getTransaction().begin();

            String hql = "Select s from Student s where s.surname = :surname";

            Query<Student> query = session.createQuery(hql);
            query.setParameter("surname", "Lewicki");
            List<Student> students = query.getResultList();
            Student student1 = students.get(0);//ogólnie to jest słabe, bo w normalnej aplikacji nie mamy gwarancji na to że coś będzie w liście pod tym indexem, tutaj w ten sposób wyciągamy bo aplikacja jest mała i wiemy co wrzuciliśmy do bazy
            Student student2 = students.get(1);
            student1.getCourseList().forEach(course ->{
                //debuguj wyswietl nazwe kursu za pomocą println, cokolwiek
            });

            //Adnotacja @ManyToMany ma defaultowo FetchType.LAZY, kursy dla studenta 1 są doczytywane w lini 80

            session.close();

           // student2.getCourseList().forEach(course ->{
                //wywali błąd bo FetchType.LAZY, jesteśmy po za sesją, a podczas sesji nie żadaliśmy aby były doczytywane kursy dla studenta 2
          //  });
        }
    }

    private void courseToStudentsRelation(){
// sprawdzamy ilu studentów jest zapisanych na dany kurs, pozwala to dziekanatom określać który kurs nalezy uruchamiać
        try (SessionFactory factory = HibernateUtils.buildSessionFactory()) {
            Session session = factory.getCurrentSession();
            session.getTransaction().begin();

            String hql = "Select c from Course c";

            Query<Course> query = session.createQuery(hql);
            List<Course> courses = query.getResultList();
            courses.forEach(course -> {
                if(course.getStudentList().size() < 5){
                   // jeśli zapisanych jest mniej niż pięciu studentów to kasuj kurs, zgłoś do dziekana, cokolwiek
                } //jeszcze raz przypominam @ManyTo@Many, defaultowo mamy wiec leniwe doczytywanie i doczytywanie studentów odbywa sięwłaśnie tutaj, próbując zrobić to dopiero za seesion.close() wyleci wyjątek
            });
            session.close();
        }
    }

    private void deleteObjects() {
        try (SessionFactory factory = HibernateUtils.buildSessionFactory()) {
            Session session = factory.getCurrentSession();
            session.getTransaction().begin();

            String hql = "Select s from Student s";

            Query<Student> query = session.createQuery(hql);
            List<Student> students = query.getResultList();

            students.forEach(student -> session.delete(student));

            String hql2 = "Select c from Course c";

            Query<Course> query2 = session.createQuery(hql2);
            List<Course> courses = query2.getResultList();

            courses.forEach(course -> session.delete(course));

            session.getTransaction().commit();
            session.close();
        }
    }
}

