package ru.javawebinar.voting.web.dish;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.voting.DishTestData;
import ru.javawebinar.voting.model.Dish;
import ru.javawebinar.voting.service.DishService;
import ru.javawebinar.voting.web.AbstractControllerTest;
import ru.javawebinar.voting.web.json.JsonUtil;

import java.time.LocalDate;

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
        Dish created = DishTestData.getCreated();
        ResultActions action = mockMvc.perform(post(REST_URL + "?restaurantId=" + RESTAURANT1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(created))
                .with(userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isCreated());

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
                .andExpect(status().isForbidden())
                .andExpect(detailMessage("Access is denied"));
    }

    @Test
    void testCreateInvalid() throws Exception {
        Dish created = new Dish(null, "   ", 0, null);
        mockMvc.perform(post(REST_URL + "?restaurantId=" + RESTAURANT1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(created))
                .with(userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void testCreateDuplicateRestaurantDateName() throws Exception {
        Dish created = new Dish(null, "Картошечка", 550, LocalDate.of(2019, 1, 1));
        mockMvc.perform(post(REST_URL + "?restaurantId=" + RESTAURANT1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(created))
                .with(userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isConflict());
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
                .andExpect(status().isForbidden())
                .andExpect(detailMessage("Access is denied"));
    }

    @Test
    void testUpdateInvalid() throws Exception {
        Dish updated = new Dish(DISH1_ID, "Б", 150000, LocalDate.of(2019, 1, 1));

        mockMvc.perform(put(REST_URL + DISH1_ID + "?restaurantId=" + RESTAURANT1_ID).contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated))
                .with(userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void testUpdateDuplicateRestaurantDateName() throws Exception {
        Dish updated = new Dish(DISH1_ID, "Шашлычок", 100, LocalDate.of(2019, 1, 1));
        mockMvc.perform(put(REST_URL + DISH1_ID + "?restaurantId=" + RESTAURANT1_ID).contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated))
                .with(userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    void testUpdateNotFound() throws Exception {
        Dish updated = DishTestData.getUpdated();

        mockMvc.perform(put(REST_URL + DISH1_ID + "?restaurantId=" + RESTAURANT2_ID).contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated))
                .with(userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(detailMessage("Not found entity with id=" + DISH1_ID));
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