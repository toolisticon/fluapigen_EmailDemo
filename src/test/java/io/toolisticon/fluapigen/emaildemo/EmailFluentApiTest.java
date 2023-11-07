package io.toolisticon.fluapigen.emaildemo;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

import java.io.File;

public class EmailFluentApiTest {


    @Test
    public void testFluentApi() {

        EmailFluentApi.EmailBB emailBB = EmailFluentApiStarter.cc("tobias1@holisticon.de")
                .and().to("tobias2@holisticon.de")
                .and().bcc("tobias3@holisticon.de")
                .and().to("tobias4@holisticon.de")
                .withSubject("Test")
                .withBody("LOREM IPSUM")
                .addAttachment().withCustomName("itWorks.png").fromFile(new File("abc.png"))
                .sendEmail();

        MatcherAssert.assertThat(emailBB, Matchers.notNullValue());


    }
}
