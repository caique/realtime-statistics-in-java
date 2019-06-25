package com.caique.component;

import com.caique.component.testutils.StatisticsResponseWrapper;
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

import java.math.BigDecimal;

import static java.math.BigDecimal.ROUND_HALF_UP;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.OK;

@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
public class StatisticsEndpointsComponentTest {

    public static final String DEFAULT_AMOUNT = "12.33433";

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    public void getStatistics() throws Exception {
        registerTransaction();

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get("/statistics")
                .contentType(MediaType.APPLICATION_JSON);

        MockHttpServletResponse response = mockMvc.perform(request).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(OK.value());

        StatisticsResponseWrapper statistics = new StatisticsResponseWrapper(response);

        BigDecimal expectedDefaultStatistics = new BigDecimal(DEFAULT_AMOUNT).setScale(2, ROUND_HALF_UP);

        assertThat(statistics.getSum()).isEqualTo(expectedDefaultStatistics);
        assertThat(statistics.getAvg()).isEqualTo(expectedDefaultStatistics);
        assertThat(statistics.getMax()).isEqualTo(expectedDefaultStatistics);
        assertThat(statistics.getMin()).isEqualTo(expectedDefaultStatistics);
        assertThat(statistics.getCount()).isEqualTo(1L);
    }

    private void registerTransaction() throws Exception {
        String newTransactionRequestBody = new TransactionRequestTestBuilder()
                .withAmount(DEFAULT_AMOUNT)
                .withTimestampAgedIn(0)
                .build();

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post("/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(newTransactionRequestBody);

        mockMvc.perform(request).andReturn();
    }
}
