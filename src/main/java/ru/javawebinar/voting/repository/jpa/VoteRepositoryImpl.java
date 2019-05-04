package ru.javawebinar.voting.repository.jpa;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.voting.model.Restaurant;
import ru.javawebinar.voting.model.User;
import ru.javawebinar.voting.model.Vote;
import ru.javawebinar.voting.repository.VoteRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class VoteRepositoryImpl implements VoteRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public Vote save(Vote vote, int userId, int restaurantId) {
        User refUser = em.getReference(User.class, userId);
        vote.setUser(refUser);
        Restaurant refRestaurant = em.getReference(Restaurant.class, restaurantId);
        vote.setRestaurant(refRestaurant);
        if (vote.isNew()) {
            em.persist(vote);
            return vote;
        } else {
            return get(vote.getId(), userId) != null ? em.merge(vote) : null;
        }
    }

    @Override
    public Vote get(int id, int userId) {
        final Vote vote = em.find(Vote.class, id);
        return vote != null && vote.getUser().getId() == userId ? vote : null;
    }

    @Override
    public List<Vote> getAll(LocalDate date) {
        return em.createNamedQuery(Vote.ALL_SORTED, Vote.class)
                .setParameter("date", date)
                .getResultList();
    }
}
