package ru.javawebinar.voting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.voting.model.Vote;

import java.time.LocalDate;
import java.util.List;

@Transactional(readOnly = true)
public interface CrudVoteRepository extends JpaRepository<Vote, Integer> {

    @Override
    @Transactional
    Vote save(Vote vote);

    @Query("SELECT v FROM Vote v INNER JOIN FETCH v.restaurant WHERE v.id=:id AND v.user.id=:userId")
    Vote get(@Param("id") int id, @Param("userId") int userId);

    @Query("SELECT v FROM Vote v INNER JOIN FETCH v.restaurant WHERE v.date=:date")
    List<Vote> getAll(@Param("date") LocalDate date);
}
