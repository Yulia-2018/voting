package ru.javawebinar.voting.util;

import ru.javawebinar.voting.model.Restaurant;
import ru.javawebinar.voting.model.Vote;
import ru.javawebinar.voting.to.ResultVote;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class VotesUtil {

    private static final Comparator<ResultVote> RESULT_VOTE_COMPARATOR = Comparator.comparing(ResultVote::getQuantity).thenComparing(ResultVote::getName);

    private VotesUtil() {
    }

    public static List<ResultVote> getFilteredResults(Collection<Vote> votes, LocalDate date) {
        Map<Restaurant, Long> quantityByRestaurant = votes.stream()
                .collect(
                        Collectors.groupingBy(Vote::getRestaurant, Collectors.counting())
                );
        List<ResultVote> results = new ArrayList<>();
        quantityByRestaurant.forEach((key, value) -> results.add(new ResultVote(key.getId(), key.getName(), date, value.intValue())));
        results.sort(RESULT_VOTE_COMPARATOR);
        return results;
    }
}
