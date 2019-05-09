package ru.javawebinar.voting.web.vote;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
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
import static ru.javawebinar.voting.RestaurantTestData.RESTAURANT1_ID;
import static ru.javawebinar.voting.TestUtil.*;
import static ru.javawebinar.voting.UserTestData.*;
import static ru.javawebinar.voting.VoteTestData.assertMatch;
import static ru.javawebinar.voting.VoteTestData.contentJson;
import static ru.javawebinar.voting.VoteTestData.*;
import static ru.javawebinar.voting.util.VotesUtil.getResultsVoting;

class VoteRestControllerTest extends AbstractControllerTest {

    private static final String REST_URL = VoteRestController.REST_URL + '/';

    @Autowired
    private VoteService service;

    @Test
    void testCreate() throws Exception {
        Vote created = getCreated();
        if (LocalTime.now().compareTo(LocalTime.of(11, 0)) <= 0) {
            ResultActions action = mockMvc.perform(post(REST_URL + "?restaurantId=" + RESTAURANT1_ID)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(JsonUtil.writeValue(created))
                    .with(userHttpBasic(ADMIN)))
                    .andDo(print())
                    .andExpect(status().isCreated());

            Vote returned = readFromJson(action, Vote.class);
            created.setId(returned.getId());

            //RestaurantTestData.assertMatch(returned.getRestaurant(), created.getRestaurant());
            assertMatch(returned, created);
            assertMatch(service.getAll(created.getDate()), VOTE_FOR_CURRENT_DATE, created);
        } else {
            mockMvc.perform(post(REST_URL + "?restaurantId=" + RESTAURANT1_ID)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(JsonUtil.writeValue(created))
                    .with(userHttpBasic(ADMIN)))
                    .andDo(print())
                    .andExpect(status().isUnprocessableEntity())
                    .andExpect(content().string(containsString("Date")))
                    .andExpect(content().string(containsString("or time")))
                    .andExpect(content().string(containsString("is invalid")));
        }
    }

    /*@Test
    void testCreateInvalid() throws Exception {
        Vote created = new Vote(null, null);
        mockMvc.perform(post(REST_URL + "?restaurantId=" + RESTAURANT1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(created))
                .with(userHttpBasic(USER)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }*/

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void testCreateDuplicateUserDate() throws Exception {
        Vote created = getCreated();
        if (LocalTime.now().compareTo(LocalTime.of(11, 0)) <= 0) {
            mockMvc.perform(post(REST_URL + "?restaurantId=" + RESTAURANT1_ID)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(JsonUtil.writeValue(created))
                    .with(userHttpBasic(USER)))
                    .andDo(print())
                    .andExpect(status().isConflict());
        } else {
            mockMvc.perform(post(REST_URL + "?restaurantId=" + RESTAURANT1_ID)
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
    void testUpdate() throws Exception {
        Vote updated = getUpdated();

        if (LocalTime.now().compareTo(LocalTime.of(11, 0)) <= 0) {
            mockMvc.perform(put(REST_URL + VOTE_ID_FOR_CURRENT_DATE + "?restaurantId=" + RESTAURANT1_ID).contentType(MediaType.APPLICATION_JSON)
                    .content(JsonUtil.writeValue(updated))
                    .with(userHttpBasic(USER)))
                    .andDo(print())
                    .andExpect(status().isNoContent());

            Vote actual = service.get(VOTE_ID_FOR_CURRENT_DATE, USER_ID);
            //RestaurantTestData.assertMatch(actual.getRestaurant(), updated.getRestaurant());
            assertMatch(actual, updated);
        } else {
            mockMvc.perform(put(REST_URL + VOTE_ID_FOR_CURRENT_DATE + "?restaurantId=" + RESTAURANT1_ID).contentType(MediaType.APPLICATION_JSON)
                    .content(JsonUtil.writeValue(updated))
                    .with(userHttpBasic(USER)))
                    .andDo(print())
                    .andExpect(status().isUnprocessableEntity())
                    .andExpect(content().string(containsString("Date")))
                    .andExpect(content().string(containsString("or time")))
                    .andExpect(content().string(containsString("is invalid")));
        }
    }

    /*@Test
    void testUpdateInvalid() throws Exception {
        Vote updated = new Vote(VOTE_ID_FOR_CURRENT_DATE, null);

        mockMvc.perform(put(REST_URL + VOTE_ID_FOR_CURRENT_DATE + "?restaurantId=" + RESTAURANT1_ID).contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated))
                .with(userHttpBasic(USER)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }*/

    @Test
    void testUpdateNotFound() throws Exception {
        Vote updated = getUpdated();
        mockMvc.perform(put(REST_URL + VOTE_ID_FOR_CURRENT_DATE + "?restaurantId=" + RESTAURANT1_ID).contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated))
                .with(userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(detailMessage("Not found entity with id=" + VOTE_ID_FOR_CURRENT_DATE));
    }

    @Test
    void testUpdateDate() throws Exception {
        Vote updated = new Vote(VOTE1_USER);
        LocalDate newDate = LocalDate.of(2019, 5, 3);
        updated.setDate(newDate);
        mockMvc.perform(put(REST_URL + VOTE1_ID + "?restaurantId=" + RESTAURANT1_ID).contentType(MediaType.APPLICATION_JSON)
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
                .andExpect(contentJson(getResultsVoting(service.getAll(date), date)));
    }
}