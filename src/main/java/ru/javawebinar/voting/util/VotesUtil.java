package ru.javawebinar.voting.util;

import ru.javawebinar.voting.model.Restaurant;
import ru.javawebinar.voting.model.Vote;
import ru.javawebinar.voting.to.ResultsVoting;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static ru.javawebinar.voting.util.RestaurantUtil.asTo;

public class VotesUtil {

    private static final Comparator<ResultsVoting> RESULTS_VOTING_COMPARATOR = Comparator
            .comparing(ResultsVoting::getQuantity).reversed()
            .thenComparing((ResultsVoting resultsVoting) -> resultsVoting.getRestaurant().getName());

    private VotesUtil() {
    }

    public static List<ResultsVoting> getResultsVoting(Collection<Vote> votes, LocalDate date) {
        Map<Restaurant, Long> quantityByRestaurant = votes.stream()
                .collect(
                        Collectors.groupingBy(Vote::getRestaurant, Collectors.counting())
                );
        List<ResultsVoting> results = new ArrayList<>();
        quantityByRestaurant.forEach((key, value) -> results.add(new ResultsVoting(asTo(key), date, value.intValue())));
        results.sort(RESULTS_VOTING_COMPARATOR);
        return results;
    }
}
