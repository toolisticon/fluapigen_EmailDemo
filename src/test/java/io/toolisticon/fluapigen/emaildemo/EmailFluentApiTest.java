package io.toolisticon.fluapigen.emaildemo;

import io.toolisticon.fluapigen.validation.api.ValidatorException;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

import java.io.File;
import java.util.stream.Collectors;

public class EmailFluentApiTest {


    @Test
    public void testFluentApi() {

        EmailFluentApi.EmailBB emailBB = EmailDataBuilder
                .withSender("tobias.stamann@holisticon.de")
                .withRecipients()
                    .cc("tobias1@holisticon.de")
                    .and().to("tobias2@holisticon.de")
                    .and().bcc("tobias3@holisticon.de")
                    .and().to("tobias4@holisticon.de")
                .withSubject("Test")
                .withBody("LOREM IPSUM")
                .addAttachment().withCustomName("itWorks.png").fromFile(new File("abc.png"))
                .build();

        MatcherAssert.assertThat(emailBB, Matchers.notNullValue());
        MatcherAssert.assertThat(emailBB.subject(), Matchers.is("Test"));
        MatcherAssert.assertThat(emailBB.body(), Matchers.is("LOREM IPSUM"));
        MatcherAssert.assertThat(emailBB.sender(), Matchers.is("tobias.stamann@holisticon.de"));
        MatcherAssert.assertThat(emailBB.getRecipientsByKind(EmailFluentApi.RecipientKind.TO).stream().map(EmailFluentApi.RecipientBB::emailAddress).collect(Collectors.toList()), Matchers.containsInAnyOrder("tobias2@holisticon.de", "tobias4@holisticon.de"));
        MatcherAssert.assertThat(emailBB.getRecipientsByKind(EmailFluentApi.RecipientKind.BCC).stream().map(EmailFluentApi.RecipientBB::emailAddress).collect(Collectors.toList()), Matchers.containsInAnyOrder("tobias3@holisticon.de"));
        MatcherAssert.assertThat(emailBB.getRecipientsByKind(EmailFluentApi.RecipientKind.CC).stream().map(EmailFluentApi.RecipientBB::emailAddress).collect(Collectors.toList()), Matchers.containsInAnyOrder("tobias1@holisticon.de"));
        MatcherAssert.assertThat("Expect one attachment" , emailBB.attachments().size() == 1);
        MatcherAssert.assertThat("Expect one attachment" , emailBB.attachments().get(0).attachmentName(), Matchers.is("itWorks.png") );

    }

    @Test(expected = ValidatorException.class)
    public void testFluentApi_nullValueForSender() {

        EmailDataBuilder
                .withSender(null);

    }

    @Test(expected = ValidatorException.class)
    public void testFluentApi_invalidSender() {

        EmailDataBuilder
                .withSender("null");

    }


}
