![Build Status](https://github.com/toolisticon/fluapigen_EmailDemo/workflows/default/badge.svg)

# FluApiGen - Email Demo

Demo project about building a immutable fluent api with [FluApiGen](https://github.com/toolisticon/FluApiGen).

Please check the following article on [medium](https://medium.com/@tobias.stamann/efficiently-creating-fluent-interfaces-in-java-d8a74351497b)

Creates a small fluent api for configuring and creating email data objects.

The example basically consists only of two files:
- the [api](/src/main/java/io/toolisticon/fluapigen/emaildemo/EmailFluentApi.java)
- a [unit test](/src/test/java/io/toolisticon/fluapigen/emaildemo/EmailFluentApiTest.java) demonstrating that if works ;)

It will generate a Builder Class _EmailDataBuilder_ which then can be used as starting pointb for fluent api:
```java
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
```