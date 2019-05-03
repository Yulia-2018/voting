package ru.javawebinar.voting.web.vote;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import ru.javawebinar.voting.RestaurantTestData;
import ru.javawebinar.voting.model.Vote;
import ru.javawebinar.voting.service.VoteService;
import ru.javawebinar.voting.web.AbstractControllerTest;
import ru.javawebinar.voting.web.json.JsonUtil;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.hamcrest.core.StringContains.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javawebinar.voting.RestaurantTestData.RESTAURANT1;
import static ru.javawebinar.voting.TestUtil.*;
import static ru.javawebinar.voting.UserTestData.*;
import static ru.javawebinar.voting.VoteTestData.assertMatch;
import static ru.javawebinar.voting.VoteTestData.contentJson;
import static ru.javawebinar.voting.VoteTestData.*;
import static ru.javawebinar.voting.util.VotesUtil.getFilteredResults;

class VoteRestControllerTest extends AbstractControllerTest {

    private static final String REST_URL = VoteRestController.REST_URL + '/';

    @Autowired
    private VoteService service;

    @Test
    void testCreate() throws Exception {
        Vote created = getCreated();
        if (LocalTime.now().compareTo(LocalTime.of(11, 0)) <= 0) {
            ResultActions action = mockMvc.perform(post(REST_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(JsonUtil.writeValue(created))
                    .with(userHttpBasic(USER)))
                    .andDo(print())
                    .andExpect(status().isCreated());

            Vote returned = readFromJson(action, Vote.class);
            created.setId(returned.getId());

            RestaurantTestData.assertMatch(returned.getRestaurant(), created.getRestaurant());
            assertMatch(returned, created);
            assertMatch(service.getAll(LocalDate.now()), created);
        } else {
            mockMvc.perform(post(REST_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(JsonUtil.writeValue(created))
                    .with(userHttpBasic(USER)))
                    .andDo(print())
                    .andExpect(status().isUnprocessableEntity())
                    .andExpect(content().string(containsString("Date")))
                    .andExpect(content().string(containsString("or time")))
                    .andExpect(content().string(containsString("is invalid")));
        }
    }

    @Test
    void testCreateInvalid() throws Exception {
        Vote created = new Vote(null, null, null);
        mockMvc.perform(post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(created))
                .with(userHttpBasic(USER)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void testUpdate() throws Exception {
        Vote created = createForCurrentDate(service);
        Vote updated = new Vote(created);
        updated.setRestaurant(RESTAURANT1);

        int id = updated.getId();
        if (LocalTime.now().compareTo(LocalTime.of(11, 0)) <= 0) {
            mockMvc.perform(put(REST_URL + id).contentType(MediaType.APPLICATION_JSON)
                    .content(JsonUtil.writeValue(updated))
                    .with(userHttpBasic(USER)))
                    .andDo(print())
                    .andExpect(status().isNoContent());

            Vote actual = service.get(id, USER_ID);
            RestaurantTestData.assertMatch(actual.getRestaurant(), updated.getRestaurant());
            assertMatch(actual, updated);
        } else {
            mockMvc.perform(put(REST_URL + id).contentType(MediaType.APPLICATION_JSON)
                    .content(JsonUtil.writeValue(updated))
                    .with(userHttpBasic(USER)))
                    .andDo(print())
                    .andExpect(status().isUnprocessableEntity())
                    .andExpect(content().string(containsString("Date")))
                    .andExpect(content().string(containsString("or time")))
                    .andExpect(content().string(containsString("is invalid")));
        }
    }

    @Test
    void testUpdateInvalid() throws Exception {
        Vote created = createForCurrentDate(service);
        Vote updated = new Vote(created);
        updated.setRestaurant(null);

        int id = updated.getId();
        mockMvc.perform(put(REST_URL + id).contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated))
                .with(userHttpBasic(USER)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void testUpdateNotFound() throws Exception {
        Vote updated = new Vote(1, LocalDate.now());
        updated.setRestaurant(RESTAURANT1);
        mockMvc.perform(put(REST_URL + 1).contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated))
                .with(userHttpBasic(USER)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(detailMessage("Not found entity with id=1"));
    }

    @Test
    void testUpdateDate() throws Exception {
        Vote updated = new Vote(VOTE1_USER);
        LocalDate newDate = LocalDate.of(2019, 5, 3);
        updated.setDate(newDate);
        mockMvc.perform(put(REST_URL + VOTE1_ID).contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated))
                .with(userHttpBasic(USER)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(detailMessage("The date of voting " + VOTE1_USER.getDate() + " cannot be changed to date " + newDate));
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
    void testGetUnauth() throws Exception {
        mockMvc.perform(get(REST_URL + VOTE1_ID))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testGetNotFound() throws Exception {
        mockMvc.perform(get(REST_URL + VOTE1_ID)
                .with(userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(detailMessage("Not found entity with id=" + VOTE1_ID));
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