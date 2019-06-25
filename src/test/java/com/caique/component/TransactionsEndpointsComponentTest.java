package com.caique.component;

import com.caique.component.testutils.TransactionRequestTestBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
public class TransactionsEndpointsComponentTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    public void rejectRequestsWithoutBody() throws Exception {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post("/transactions")
                .contentType(MediaType.APPLICATION_JSON);

        MockHttpServletResponse response = mockMvc.perform(request).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(BAD_REQUEST.value());
    }

    @Test
    public void rejectRequestBodyWithoutAmountAndTimestamp() throws Exception {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post("/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TransactionRequestTestBuilder.buildBadRequest());

        MockHttpServletResponse response = mockMvc.perform(request).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(BAD_REQUEST.value());
    }

    @Test
    public void rejectRequestWhenFieldsAreNotParseable() throws Exception {
        String unprocessableRequestBody = new TransactionRequestTestBuilder()
                .withAmount("nonnumericvalue")
                .withTimestamp("nondatetimevalue")
                .build();

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post("/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(unprocessableRequestBody);

        MockHttpServletResponse response = mockMvc.perform(request).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(UNPROCESSABLE_ENTITY.value());
    }

    @Test
    public void rejectTransactionOlderThan60SecondsLimit() throws Exception {
        String oldTransactionRequestBody = new TransactionRequestTestBuilder()
                .withAmount("12.33433")
                .withTimestampAgedIn(61)
                .build();

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post("/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(oldTransactionRequestBody);

        MockHttpServletResponse response = mockMvc.perform(request).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(NO_CONTENT.value());
    }

    @Test
    public void acceptTransactionWithin60SecondsLimit() throws Exception {
        String newTransactionRequestBody = new TransactionRequestTestBuilder()
                .withAmount("12.33433")
                .withTimestampAgedIn(0)
                .build();

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post("/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(newTransactionRequestBody);

        MockHttpServletResponse response = mockMvc.perform(request).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(CREATED.value());
    }

    @Test
    public void deleteAllTransactions() throws Exception {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .delete("/transactions")
                .contentType(MediaType.APPLICATION_JSON);

        MockHttpServletResponse response = mockMvc.perform(request).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(NO_CONTENT.value());
    }

}
