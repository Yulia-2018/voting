package ru.javawebinar.voting.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import ru.javawebinar.voting.DishTestData;
import ru.javawebinar.voting.model.Dish;
import ru.javawebinar.voting.service.DishService;
import ru.javawebinar.voting.web.json.JsonUtil;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javawebinar.voting.DishTestData.assertMatch;
import static ru.javawebinar.voting.DishTestData.contentJson;
import static ru.javawebinar.voting.DishTestData.*;
import static ru.javawebinar.voting.RestaurantTestData.*;
import static ru.javawebinar.voting.TestUtil.*;
import static ru.javawebinar.voting.UserTestData.ADMIN;
import static ru.javawebinar.voting.UserTestData.USER;

class DishRestControllerTest extends AbstractControllerTest {

    private static final String REST_URL = DishRestController.REST_URL + '/';

    @Autowired
    private DishService service;

    @Test
    void testCreate() throws Exception {
        Dish created = DishTestData.getCreated();
        ResultActions action = mockMvc.perform(post(REST_URL + "?restaurantId=" + RESTAURANT1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(created))
                .with(userHttpBasic(ADMIN)))
                .andDo(print());

        Dish returned = readFromJson(action, Dish.class);
        created.setId(returned.getId());

        assertMatch(returned, created);
        assertMatch(service.getAll(RESTAURANT1_ID, LocalDate.of(2019, 1, 1)), DISH1_1, created, DISH1_2);
    }

    @Test
    void testCreateForbidden() throws Exception {
        Dish created = DishTestData.getCreated();
        mockMvc.perform(post(REST_URL + "?restaurantId=" + RESTAURANT1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(created))
                .with(userHttpBasic(USER)))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    void testUpdate() throws Exception {
        Dish updated = DishTestData.getUpdated();

        mockMvc.perform(put(REST_URL + DISH1_ID + "?restaurantId=" + RESTAURANT1_ID).contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated))
                .with(userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isNoContent());

        assertMatch(service.get(DISH1_ID, RESTAURANT1_ID), updated);
    }

    @Test
    void testUpdateForbidden() throws Exception {
        Dish updated = DishTestData.getUpdated();

        mockMvc.perform(put(REST_URL + DISH1_ID + "?restaurantId=" + RESTAURANT1_ID).contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated))
                .with(userHttpBasic(USER)))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    void testDelete() throws Exception {
        mockMvc.perform(delete(REST_URL + DISH2_ID + "?restaurantId=" + RESTAURANT2_ID)
                .with(userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertMatch(service.getAll(RESTAURANT2_ID, LocalDate.of(2019, 1, 1)), DISH2_2);
    }

    @Test
    void testDeleteForbidden() throws Exception {
        mockMvc.perform(delete(REST_URL + DISH2_ID + "?restaurantId=" + RESTAURANT2_ID)
                .with(userHttpBasic(USER)))
                .andDo(print())
                .andExpect(status().isForbidden());
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