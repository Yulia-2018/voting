package ru.javawebinar.voting.web.restaurant;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.voting.model.Restaurant;
import ru.javawebinar.voting.service.RestaurantService;
import ru.javawebinar.voting.to.RestaurantTo;
import ru.javawebinar.voting.web.AbstractControllerTest;
import ru.javawebinar.voting.web.json.JsonUtil;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javawebinar.voting.RestaurantTestData.*;
import static ru.javawebinar.voting.TestUtil.*;
import static ru.javawebinar.voting.UserTestData.ADMIN;
import static ru.javawebinar.voting.UserTestData.USER;
import static ru.javawebinar.voting.util.RestaurantUtil.createNewFromTo;
import static ru.javawebinar.voting.util.RestaurantUtil.updateFromTo;
import static ru.javawebinar.voting.util.RestaurantsWithDishesUtil.getRestaurantsWithDishes;

class RestaurantRestControllerTest extends AbstractControllerTest {

    private static final String REST_URL = RestaurantRestController.REST_URL + '/';

    @Autowired
    private RestaurantService service;

    @Test
    void testCreate() throws Exception {
        RestaurantTo createdTo = getCreatedTo();
        ResultActions action = mockMvc.perform(post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(createdTo))
                .with(userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isCreated());

        Restaurant returned = readFromJson(action, Restaurant.class);

        Restaurant created = createNewFromTo(createdTo);
        created.setId(returned.getId());

        assertMatch(returned, created);
        assertMatch(service.getAll(), RESTAURANT2, created, RESTAURANT1);
    }

    @Test
    void testCreateForbidden() throws Exception {
        RestaurantTo createdTo = getCreatedTo();
        mockMvc.perform(post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(createdTo))
                .with(userHttpBasic(USER)))
                .andDo(print())
                .andExpect(status().isForbidden())
                .andExpect(detailMessage("Access is denied"));
    }

    @Test
    void testCreateInvalid() throws Exception {
        RestaurantTo createdTo = new RestaurantTo(null, "R");
        mockMvc.perform(post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(createdTo))
                .with(userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void testCreateDuplicateName() throws Exception {
        RestaurantTo createdTo = new RestaurantTo(null, "Местечко");
        mockMvc.perform(post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(createdTo))
                .with(userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    void testUpdate() throws Exception {
        RestaurantTo updatedTo = getUpdatedTo();

        mockMvc.perform(put(REST_URL + RESTAURANT1_ID).contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updatedTo))
                .with(userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isNoContent());

        assertMatch(service.get(RESTAURANT1_ID), updateFromTo(new Restaurant(RESTAURANT1), updatedTo));
    }

    @Test
    void testUpdateForbidden() throws Exception {
        RestaurantTo updatedTo = getUpdatedTo();

        mockMvc.perform(put(REST_URL + RESTAURANT1_ID).contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updatedTo))
                .with(userHttpBasic(USER)))
                .andDo(print())
                .andExpect(status().isForbidden())
                .andExpect(detailMessage("Access is denied"));
    }

    @Test
    void testUpdateInvalid() throws Exception {
        RestaurantTo updatedTo = new RestaurantTo(RESTAURANT1_ID, null);
        mockMvc.perform(put(REST_URL + RESTAURANT1_ID).contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updatedTo))
                .with(userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void testUpdateDuplicateName() throws Exception {
        RestaurantTo updatedTo = new RestaurantTo(RESTAURANT1_ID, "Местечко");

        mockMvc.perform(put(REST_URL + RESTAURANT1_ID).contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updatedTo))
                .with(userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    void testGet() throws Exception {
        mockMvc.perform(get(REST_URL + RESTAURANT1_ID)
                .with(userHttpBasic(USER)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertMatch(readFromJsonMvcResult(result, Restaurant.class), RESTAURANT1));
    }

    @Test
    void testGetNotFound() throws Exception {
        mockMvc.perform(get(REST_URL + 1)
                .with(userHttpBasic(USER)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(detailMessage("Not found entity with id=1"));
    }

    @Test
    void testGetUnauth() throws Exception {
        mockMvc.perform(get(REST_URL + RESTAURANT1_ID))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testGetAll() throws Exception {
        mockMvc.perform(get(REST_URL)
                .with(userHttpBasic(USER)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJson(RESTAURANT2, RESTAURANT1));
    }

    @Test
    void testGetAllWithDishes() throws Exception {
        LocalDate date = LocalDate.of(2019, 2, 1);
        mockMvc.perform(get(REST_URL + "withDishes").param("date", date.toString())
                .with(userHttpBasic(USER)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJson(getRestaurantsWithDishes(service.getAllWithDishes(date), date)));
    }
}