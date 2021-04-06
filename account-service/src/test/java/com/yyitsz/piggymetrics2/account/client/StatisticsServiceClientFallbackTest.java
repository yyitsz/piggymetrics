package com.yyitsz.piggymetrics2.account.client;

import com.yyitsz.piggymetrics2.account.domain.Account;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

/**
 * @author cdov
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(properties = {
        "feign.hystrix.enabled=true"
})
public class StatisticsServiceClientFallbackTest {
    @Autowired
    private StatisticsServiceClient statisticsServiceClient;


    @Test
    @ExtendWith(OutputCaptureExtension.class)
    public void testUpdateStatisticsWithFailFallback(CapturedOutput outputCapture) {
        statisticsServiceClient.updateStatistics("test", new Account());

        assertThat(outputCapture.getOut(), containsString("Error during update statistics for account: test"));

    }

}

