package ru.javawebinar.voting.web.dish;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.voting.model.Dish;
import ru.javawebinar.voting.service.DishService;
import ru.javawebinar.voting.to.DishTo;
import ru.javawebinar.voting.util.DishUtil;
import ru.javawebinar.voting.web.AbstractControllerTest;
import ru.javawebinar.voting.web.json.JsonUtil;

import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javawebinar.voting.DishTestData.*;
import static ru.javawebinar.voting.RestaurantTestData.RESTAURANT1_ID;
import static ru.javawebinar.voting.RestaurantTestData.RESTAURANT2_ID;
import static ru.javawebinar.voting.TestUtil.*;
import static ru.javawebinar.voting.UserTestData.ADMIN;
import static ru.javawebinar.voting.UserTestData.USER;

class DishRestControllerTest extends AbstractControllerTest {

    private static final String REST_URL = DishRestController.REST_URL + '/';

    @Autowired
    private DishService service;

    @Test
    void testCreate() throws Exception {
        DishTo createdTo = getCreatedTo();
        ResultActions action = mockMvc.perform(post(REST_URL + "?restaurantId=" + RESTAURANT1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(createdTo))
                .with(userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isCreated());

        Dish returned = readFromJson(action, Dish.class);

        Dish created = DishUtil.createNewFromTo(createdTo);
        created.setId(returned.getId());

        assertMatch(returned, created);
        assertMatch(service.getAll(RESTAURANT1_ID, created.getDate()), DISH_FOR_CURRENT_DATE, created);
    }

    @Test
    void testCreateForbidden() throws Exception {
        DishTo createdTo = getCreatedTo();
        mockMvc.perform(post(REST_URL + "?restaurantId=" + RESTAURANT1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(createdTo))
                .with(userHttpBasic(USER)))
                .andDo(print())
                .andExpect(status().isForbidden())
                .andExpect(detailMessage("Access is denied"));
    }

    @Test
    void testCreateInvalid() throws Exception {
        DishTo createdTo = new DishTo(null, "   ", 0);
        mockMvc.perform(post(REST_URL + "?restaurantId=" + RESTAURANT1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(createdTo))
                .with(userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void testCreateDuplicateRestaurantDateName() throws Exception {
        DishTo createdTo = new DishTo(null, "Минтай", 380);
        mockMvc.perform(post(REST_URL + "?restaurantId=" + RESTAURANT1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(createdTo))
                .with(userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    void testUpdate() throws Exception {
        DishTo updatedTo = getUpdatedTo();

        mockMvc.perform(put(REST_URL + DISH_ID_FOR_CURRENT_DATE + "?restaurantId=" + RESTAURANT1_ID).contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updatedTo))
                .with(userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isNoContent());

        assertMatch(service.get(DISH_ID_FOR_CURRENT_DATE, RESTAURANT1_ID), DishUtil.updateFromTo(new Dish(DISH_FOR_CURRENT_DATE), updatedTo));
    }

    @Test
    void testUpdateInvalidDate() throws Exception {
        DishTo updatedTo = new DishTo(DISH1_ID, "Обновленное блюдо", 150);

        mockMvc.perform(put(REST_URL + DISH1_ID + "?restaurantId=" + RESTAURANT1_ID).contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updatedTo))
                .with(userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(detailMessage("Date " + DISH1_1.getDate() +" is invalid"));
    }

    @Test
    void testUpdateForbidden() throws Exception {
        DishTo updatedTo = getUpdatedTo();

        mockMvc.perform(put(REST_URL + DISH_ID_FOR_CURRENT_DATE + "?restaurantId=" + RESTAURANT1_ID).contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updatedTo))
                .with(userHttpBasic(USER)))
                .andDo(print())
                .andExpect(status().isForbidden())
                .andExpect(detailMessage("Access is denied"));
    }

    @Test
    void testUpdateInvalid() throws Exception {
        DishTo updatedTo = new DishTo(DISH_ID_FOR_CURRENT_DATE, "   ", 0);

        mockMvc.perform(put(REST_URL + DISH_ID_FOR_CURRENT_DATE + "?restaurantId=" + RESTAURANT1_ID).contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updatedTo))
                .with(userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void testUpdateNotFound() throws Exception {
        DishTo updatedTo = getUpdatedTo();

        mockMvc.perform(put(REST_URL + DISH_ID_FOR_CURRENT_DATE + "?restaurantId=" + RESTAURANT2_ID).contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updatedTo))
                .with(userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(detailMessage("Not found entity with id=" + DISH_ID_FOR_CURRENT_DATE));
    }

    @Test
    void testDelete() throws Exception {
        mockMvc.perform(delete(REST_URL + DISH_ID_FOR_CURRENT_DATE + "?restaurantId=" + RESTAURANT1_ID)
                .with(userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertMatch(service.getAll(RESTAURANT1_ID, DISH_FOR_CURRENT_DATE.getDate()), Collections.emptyList());
    }

    @Test
    void testDeleteInvalidDate() throws Exception {
        mockMvc.perform(delete(REST_URL + DISH2_ID + "?restaurantId=" + RESTAURANT2_ID)
                .with(userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(detailMessage("Date " + DISH2_1.getDate() +" is invalid"));
    }

    @Test
    void testDeleteForbidden() throws Exception {
        mockMvc.perform(delete(REST_URL + DISH2_ID + "?restaurantId=" + RESTAURANT2_ID)
                .with(userHttpBasic(USER)))
                .andDo(print())
                .andExpect(status().isForbidden())
                .andExpect(detailMessage("Access is denied"));
    }

    @Test
    void testDeleteNotFound() throws Exception {
        mockMvc.perform(delete(REST_URL + DISH2_ID + "?restaurantId=" + RESTAURANT1_ID)
                .with(userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(detailMessage("Not found entity with id=" + DISH2_ID));
    }

    @Test
    void testGet() throws Exception {
        mockMvc.perform(get(REST_URL + DISH1_ID + "?restaurantId=" + RESTAURANT1_ID)
                .with(userHttpBasic(USER)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertMatch(readFromJsonMvcResult(result, Dish.class), DISH1_1));
    }

    @Test
    void testGetUnauth() throws Exception {
        mockMvc.perform(get(REST_URL + DISH1_ID + "?restaurantId=" + RESTAURANT1_ID))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testGetNotFound() throws Exception {
        mockMvc.perform(get(REST_URL + DISH2_ID + "?restaurantId=" + RESTAURANT1_ID)
                .with(userHttpBasic(USER)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(detailMessage("Not found entity with id=" + DISH2_ID));
    }

    @Test
    void testGetAll() throws Exception {
        mockMvc.perform(get(REST_URL + "?restaurantId=" + RESTAURANT1_ID)
                .param("date", "2019-01-01")
                .with(userHttpBasic(USER)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJson(DISH1_1, DISH1_2));
    }
}