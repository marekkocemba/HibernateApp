package com.sda.hib.chapter1.crud;

import com.sda.hib.HibernateUtils;
import com.sda.hib.chapter1.crud.enums.UserTypeEnum;
import com.sda.hib.chapter1.crud.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

public class HibernateChapter1Crud {

    public static void main(String... args) {

        HibernateChapter1Crud hibernateChapter1Crud = new HibernateChapter1Crud();
        User user = hibernateChapter1Crud.createUser(); // tworzymy obiekt klasy User
        Long id = hibernateChapter1Crud.saveUser(user); // zapisujemy do bazy
        hibernateChapter1Crud.updateUser(id, "new_password"); //update hasła,
        hibernateChapter1Crud.updateUserDirtyDetection(id, UserTypeEnum.SUPERVISOR); //update typu user-a, wykorzystanie mechanizmu wykrywania brudu,
        hibernateChapter1Crud.deleteUser(id); // usuwanie usera, sprzątamy po sobie
    }

    private User createUser() {

        User user = new User();
        user.setLogin("underthetree");
        user.setPassword("pass");
        user.setUserType(UserTypeEnum.ADMIN);
        user.setGarbage("this wont be saved unless transient adnotation will be removed");

        return user;
    }

    private Long saveUser(User user) {
        try (SessionFactory factory = HibernateUtils.buildSessionFactory()) {
            Session session = factory.getCurrentSession();
            session.getTransaction().begin();

            //Są trzy stany obiektu względem sesji NIE PODLĄCZONY, PODLĄCZONY, ODLACZONY

            // do tej linii user jest NIE PODLĄCZONY do sesji
            Long id = (Long) session.save(user);// mimo że jest to "tylko podłączenie obiektu do sesji", nasz obiekt w tym momencie ma przydzielone ID, metoda save zwraca go
            // od tej linii user jest PODLACZONY do sesji

            session.getTransaction().commit(); // w tej linijce następuje właściwy zapis

            // do tej linii user jest PODLĄCZONY do sesji
            session.close();
            // od tej linii user jest ODLACZONY od sesji
            return id; //zwracamy id zapisaneg w bazie obiektu, alternatywnie można zwrócić user.getId()
        }
    }

    private void updateUser(Long userId, String newPassword) {
        try (SessionFactory factory = HibernateUtils.buildSessionFactory()) {
            Session session = factory.getCurrentSession();
            session.getTransaction().begin();

            //Są trzy stany obiektu względem sesji NIE PODLĄCZONY, PODLĄCZONY, ODLACZONY

            String hql ="Select u from User u where u.id = :id";

            Query<User> query = session.createQuery(hql);
            query.setParameter("id", userId);
            User user = query.getSingleResult(); //wyrzuci wyjątek jeśli nie znajdzie wyniku lub będzie ich więcej niż jeden
            //ja akurat szukam po idku obiektu, który został założony wcześniej dlatego mam pewność że będe miał dokładnie jeden wynik
            //dlatego alternatywnie : List<User> results = query.getResultList(); sprawdzamy czy results.size != 0 i wyciągamy interesujący nas obiekt z listy
            // kolejna rzecz obiekt user jest podłączony do sesji ...
            session.evict(user); // a teraz został odłączony (w metodzie updateUserDirtyDetection nie zrobię odłączenia)
            user.setPassword(newPassword); // updateujemy hasło

            //session.merge(user); //zakomentowałem to aby pokazać że update na odłączonym obiekcie nic nie zmienia
            // jeśli odkomentujecie session.merge(user); to od tego momentu obiekt user będzie ponownie podłączony do sesji

            session.getTransaction().commit(); // w tej linijce następuje właściwy zapis

            session.close();
            // od tej linii user jest ponownie odłączony od sesji
        }
    }

    private void updateUserDirtyDetection(Long userId, UserTypeEnum newUserType) {
        try (SessionFactory factory = HibernateUtils.buildSessionFactory()) {
            Session session = factory.getCurrentSession();
            session.getTransaction().begin();

            //Są trzy stany obiektu względem sesji NIE PODLĄCZONY, PODLĄCZONY, ODLACZONY

            String hql ="Select u from User u where u.id = :id";

            Query<User> query = session.createQuery(hql);
            query.setParameter("id", userId);
            User user = query.getSingleResult(); // wyciągnięty obiekt user jest od razu podłączony do sesji
            user.setUserType(newUserType); // updateujemy typ usera

            //mimo że nie wywolalismy session.merge(user), typ usera zostanie zmieniony,
            // ponieważ zadziała mechanizm wykrywania zmian na wyciągniętym obiekcie podłączonym do sesji
            session.getTransaction().commit();
            session.close();
            // od tej linii user jest  odłączony od sesji, wszelkie zmiany będą już ignorowane
        }
    }

    private void deleteUser(Long userId) {
        try (SessionFactory factory = HibernateUtils.buildSessionFactory()) {
            Session session = factory.getCurrentSession();
            session.getTransaction().begin();

            //Są trzy stany obiektu względem sesji NIE PODLĄCZONY, PODLĄCZONY, ODLACZONY

            String hql ="Select u from User u where id = :id";

            Query<User> query = session.createQuery(hql);
            query.setParameter("id", userId);
            User user = query.getSingleResult(); // wyciągnięty obiekt user jest od razu podłączony do sesji

            session.delete(user); // session.delete rownierz podlacza do sesji, ale mamy obiekt juz podlaczony,
            //wiec tutaj wskazujemy tylko co chcemy zrobic z obiektem

            session.getTransaction().commit();
            session.close();
        }
    }
}

