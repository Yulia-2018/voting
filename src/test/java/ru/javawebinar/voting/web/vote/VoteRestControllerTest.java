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
import static ru.javawebinar.voting.util.ValidationUtil.TIME;
import static ru.javawebinar.voting.util.VotesUtil.getResultsVoting;

class VoteRestControllerTest extends AbstractControllerTest {

    private static final String REST_URL = VoteRestController.REST_URL + '/';

    @Autowired
    private VoteService service;

    @Test
    void testCreate() throws Exception {
        Vote created = getCreated();
        ResultActions resultActions = mockMvc.perform(post(REST_URL + "?restaurantId=" + RESTAURANT1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(created))
                .with(userHttpBasic(ADMIN)))
                .andDo(print());
        if (LocalTime.now().compareTo(TIME) <= 0) {
            resultActions.andExpect(status().isCreated());

            Vote returned = readFromJson(resultActions, Vote.class);
            created.setId(returned.getId());

            assertMatch(returned, created);
            assertMatch(service.getAll(created.getDate()), VOTE_FOR_CURRENT_DATE, created);
        } else {
            getAndCheckContentsString(resultActions).andExpect(status().isUnprocessableEntity());
        }
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void testCreateDuplicateUserDate() throws Exception {
        Vote created = getCreated();
        ResultActions resultActions = mockMvc.perform(post(REST_URL + "?restaurantId=" + RESTAURANT1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(created))
                .with(userHttpBasic(USER)))
                .andDo(print());
        if (LocalTime.now().compareTo(TIME) <= 0) {
            resultActions.andExpect(status().isConflict());
        } else {
            getAndCheckContentsString(resultActions).andExpect(status().isUnprocessableEntity());
        }
    }

    @Test
    void testUpdate() throws Exception {
        Vote updated = getUpdated();
        ResultActions resultActions = mockMvc.perform(put(REST_URL + VOTE_ID_FOR_CURRENT_DATE + "?restaurantId=" + RESTAURANT1_ID).contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated))
                .with(userHttpBasic(USER)))
                .andDo(print());
        if (LocalTime.now().compareTo(TIME) <= 0) {
            resultActions.andExpect(status().isNoContent());

            Vote actual = service.get(VOTE_ID_FOR_CURRENT_DATE, USER_ID);
            assertMatch(actual, updated);
        } else {
            getAndCheckContentsString(resultActions).andExpect(status().isUnprocessableEntity());
        }
    }

    @Test
    void testUpdateNotFound() throws Exception {
        Vote updated = getUpdated();
        ResultActions resultActions = mockMvc.perform(put(REST_URL + VOTE_ID_FOR_CURRENT_DATE + "?restaurantId=" + RESTAURANT1_ID).contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated))
                .with(userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
        if (LocalTime.now().compareTo(TIME) <= 0) {
            resultActions.andExpect(detailMessage("Not found entity with id=" + VOTE_ID_FOR_CURRENT_DATE));
        } else {
            getAndCheckContentsString(resultActions);
        }
    }

    @Test
    void testUpdateDate() throws Exception {
        Vote updated = new Vote(VOTE1_USER);
        LocalDate newDate = LocalDate.now();
        updated.setDate(newDate);
        ResultActions resultActions = mockMvc.perform(put(REST_URL + VOTE1_ID + "?restaurantId=" + RESTAURANT1_ID).contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated))
                .with(userHttpBasic(USER)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
        if (LocalTime.now().compareTo(TIME) <= 0) {
            resultActions
                    .andExpect(detailMessage("Date " + VOTE1_USER.getDate() + " is invalid"));
        } else {
            getAndCheckContentsString(resultActions);
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

    private ResultActions getAndCheckContentsString(ResultActions resultActions) throws Exception {
        return resultActions
                .andExpect(content().string(containsString("Time")))
                .andExpect(content().string(containsString("is invalid")));
    }
}