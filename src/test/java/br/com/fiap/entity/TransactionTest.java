package br.com.fiap.entity;

import org.agileware.test.PropertiesTester;
import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles({"test"})
public class TransactionTest {

    @Test
    public void standartGettersSettersTest() throws Exception {
        PropertiesTester tester = new PropertiesTester();

        tester.testAll(Transaction.class);
    }
}
