package ru.javawebinar.voting.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import ru.javawebinar.voting.RestaurantTestData;
import ru.javawebinar.voting.model.Vote;
import ru.javawebinar.voting.service.VoteService;
import ru.javawebinar.voting.web.json.JsonUtil;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javawebinar.voting.RestaurantTestData.RESTAURANT1;
import static ru.javawebinar.voting.TestUtil.*;
import static ru.javawebinar.voting.UserTestData.USER;
import static ru.javawebinar.voting.UserTestData.USER_ID;
import static ru.javawebinar.voting.VoteTestData.*;
import static ru.javawebinar.voting.util.VotesUtil.getFilteredResults;

class VoteRestControllerTest extends AbstractControllerTest {

    private static final String REST_URL = VoteRestController.REST_URL + '/';

    @Autowired
    private VoteService service;

    // Работает только до 11 часов
    @Test
    void testCreate() throws Exception {
        if (LocalTime.now().compareTo(LocalTime.of(11, 0)) <= 0) {
            Vote created = getCreated();
            ResultActions action = mockMvc.perform(post(REST_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(JsonUtil.writeValue(created))
                    .with(userHttpBasic(USER)))
                    .andDo(print());

            Vote returned = readFromJson(action, Vote.class);
            created.setId(returned.getId());

            RestaurantTestData.assertMatch(returned.getRestaurant(), created.getRestaurant());
            assertMatch(returned, created);
            assertMatch(service.getAll(LocalDate.now()), created);
        }
    }

    // Работает только до 11 часов
    @Test
    void testUpdate() throws Exception {
        if (LocalTime.now().compareTo(LocalTime.of(11, 0)) <= 0) {
            Vote created = createForCurrentDate(service);
            Vote updated = new Vote(created);
            updated.setRestaurant(RESTAURANT1);

            int id = updated.getId();
            mockMvc.perform(put(REST_URL + id).contentType(MediaType.APPLICATION_JSON)
                    .content(JsonUtil.writeValue(updated))
                    .with(userHttpBasic(USER)))
                    .andDo(print())
                    .andExpect(status().isNoContent());

            Vote actual = service.get(id, USER_ID);
            RestaurantTestData.assertMatch(actual.getRestaurant(), updated.getRestaurant());
            assertMatch(actual, updated);
        }
    }

    @Test
    void testGet() throws Exception {
        mockMvc.perform(get(REST_URL + VOTE1_ID)
                .with(userHttpBasic(USER)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertMatch(readFromJsonMvcResult(result, Vote.class), VOTE1_USER));
    }

    @Test
    void testGetResult() throws Exception {
        LocalDate date = LocalDate.of(2019, 1, 1);
        mockMvc.perform(get(REST_URL).param("date", "2019-01-01")
                .with(userHttpBasic(USER)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJson(getFilteredResults(service.getAll(date), date)));
    }
}