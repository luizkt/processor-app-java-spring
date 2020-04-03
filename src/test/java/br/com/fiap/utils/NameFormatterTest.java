package br.com.fiap.utils;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
@ActiveProfiles({"test"})
public class NameFormatterTest {

    @Test
    public void givenSomeFullName_whenNormalizingIt_shouldReturnNormalizedStringName() {
        String name = "john doe bar";

        String normalizedName = NameFormatter.capitalizeName(name);

        assertEquals("John Doe Bar", normalizedName);
    }
}
