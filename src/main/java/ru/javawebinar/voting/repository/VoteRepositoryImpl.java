package ru.javawebinar.voting.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.javawebinar.voting.model.Vote;

import java.time.LocalDate;
import java.util.List;

@Repository
public class VoteRepositoryImpl implements VoteRepository {

    @Autowired
    private CrudVoteRepository crudVoteRepository;

    @Autowired
    private CrudUserRepository crudUserRepository;

    @Autowired
    private CrudRestaurantRepository crudRestaurantRepository;

    @Override
    public Vote save(Vote vote, int userId, int restaurantId) {
        vote.setUser(crudUserRepository.getOne(userId));
        vote.setRestaurant(crudRestaurantRepository.getOne(restaurantId));
        return crudVoteRepository.save(vote);
    }

    @Override
    public Vote get(int id, int userId) {
        return crudVoteRepository.get(id, userId);
    }

    @Override
    public List<Vote> getAll(LocalDate date) {
        return crudVoteRepository.getAll(date);
    }
}
