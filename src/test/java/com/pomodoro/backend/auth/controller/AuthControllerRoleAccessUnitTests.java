package com.pomodoro.backend.auth.controller;

import com.pomodoro.backend.auth.service.GoogleAuthService;
import com.pomodoro.backend.auth.service.UserService;
import com.pomodoro.backend.sync.model.*;
import com.pomodoro.backend.sync.service.SyncService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.node.ObjectNode;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc // Needed to apply configuration to Mvc
// without configuration, just plain context
public class AuthControllerRoleAccessUnitTests {
    private UserController userController;

    @Mock
    UserService userService;

    @MockitoBean
    SyncService syncService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
//        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void userRoleHasNoAccessToUsersList() throws Exception {
        // ACT
        mockMvc.perform(get("/api/admin/users")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void adminRoleHasAccessToUsersList() throws Exception {
        // ACT
        mockMvc.perform(get("/api/admin/users")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void adminRoleHasAccessToSync() throws Exception {
        // ARRANGE - Valid OutboxRequest
        OutboxEntry outboxEntry = validEntry();
        List<OutboxEntry> changes = new ArrayList<>();
        changes.add(outboxEntry);
        OutboxRequest outboxRequest = new OutboxRequest("deviceId", changes);

        // ACT and ASSERT
        mockMvc.perform(post("/api/sync/push")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(outboxRequest))
                )
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "guest")
    void noRoleHasNoAccessToSync() throws Exception {
        // ARRANGE - Valid OutboxRequest
        OutboxEntry outboxEntry = validEntry();
        List<OutboxEntry> changes = new ArrayList<>();
        changes.add(outboxEntry);
        OutboxRequest outboxRequest = new OutboxRequest("deviceId", changes);

        // ACT and ASSERT
        mockMvc.perform(post("/api/sync/push")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(outboxRequest))
                )
                .andExpect(status().isForbidden());
    }

    private OutboxEntry validEntry() {
        ObjectNode data = objectMapper.createObjectNode();
        data.put("id", "6f1c1c1e-5b0e-4a57-9a64-2f6f3a1d8b10");
        data.put("duration_minutes", 25);

        return new OutboxEntry(
                UUID.randomUUID().toString(),                 // mutationId
                "sessions",                                   // table
                "6f1c1c1e-5b0e-4a57-9a64-2f6f3a1d8b10",       // id
                OutboxOp.values()[0],                         // op (see note)
                data,                                         // data
                null                                          // baseSeq: row never synced
        );
    }
}
