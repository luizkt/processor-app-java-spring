package br.com.fiap.entity;

import org.agileware.test.PropertiesTester;
import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles({"test"})
public class ResponseBodyTest {

        @Test
        public void standardGettersSettersTest() throws Exception {
            PropertiesTester tester = new PropertiesTester();

            tester.testAll(ResponseBody.class);
        }
}
