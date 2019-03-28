package ru.javawebinar.voting.repository.jpa;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.voting.model.Dish;
import ru.javawebinar.voting.model.Restaurant;
import ru.javawebinar.voting.repository.DishRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class DishRepositoryImpl implements DishRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public Dish save(Dish dish, int restaurantId, int userId) {
        // Сделать проверку, что это может делать только админ
        Restaurant ref = em.getReference(Restaurant.class, restaurantId);
        dish.setRestaurant(ref);
        if (dish.isNew()) {
            em.persist(dish);
            return dish;
        } else {
            return get(dish.getId(), restaurantId) != null ? em.merge(dish) : null;
        }
    }

    @Override
    @Transactional
    public boolean delete(int id, int restaurantId, int userId) {
        // Сделать проверку, что это может делать только админ
        return em.createNamedQuery(Dish.DELETE)
                .setParameter("id", id)
                .setParameter("restaurantId", restaurantId)
                .executeUpdate() != 0;
    }

    @Override
    public Dish get(int id, int restaurantId) {
        final Dish dish = em.find(Dish.class, id);
        return dish != null && dish.getRestaurant().getId() == restaurantId ? dish : null;
    }

    @Override
    public List<Dish> getAll(int restaurantId, LocalDate date) {
        return em.createNamedQuery(Dish.ALL_SORTED, Dish.class)
                .setParameter("restaurantId", restaurantId)
                .setParameter("date", date)
                .getResultList();
    }
}
