package io.toolisticon.fluapigen.emaildemo;

import io.toolisticon.fluapigen.api.FluentApi;
import io.toolisticon.fluapigen.api.FluentApiBackingBean;
import io.toolisticon.fluapigen.api.FluentApiBackingBeanField;
import io.toolisticon.fluapigen.api.FluentApiBackingBeanMapping;
import io.toolisticon.fluapigen.api.FluentApiCommand;
import io.toolisticon.fluapigen.api.FluentApiImplicitValue;
import io.toolisticon.fluapigen.api.FluentApiInlineBackingBeanMapping;
import io.toolisticon.fluapigen.api.FluentApiInterface;
import io.toolisticon.fluapigen.api.FluentApiParentBackingBeanMapping;
import io.toolisticon.fluapigen.api.FluentApiRoot;
import io.toolisticon.fluapigen.api.MappingAction;
import io.toolisticon.fluapigen.api.TargetBackingBean;
import io.toolisticon.fluapigen.validation.api.Matches;
import io.toolisticon.fluapigen.validation.api.NotEmpty;
import io.toolisticon.fluapigen.validation.api.NotNull;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

@FluentApi("EmailDataBuilder")
@NotNull
public class EmailFluentApi {

    // Don't blame me : it's by far not a correct matcher for email addresses ;)
    final static String EMAIL_PATTERN = ".+[@].+";

    // ---------------------------------------------------
    // -- Backing Beans
    // ---------------------------------------------------

    /**
     * The root backing bean.
     */
    @FluentApiBackingBean
    public interface EmailBB {

        String sender();

        List<RecipientBB> recipients();

        String subject();

        String body();

        List<AttachmentBB> attachments();

        // default methods can be added to ease processing of data
        default List<RecipientBB> getRecipientsByKind(RecipientKind kind) {
            return recipients().stream()
                    .filter(e -> e.recipientKind() == kind)
                    .collect(Collectors.toList());
        }
    }

    /**
     * Backing bean for storing recipients
     */
    @FluentApiBackingBean
    public interface RecipientBB {
        RecipientKind recipientKind();

        String emailAddress();
    }

    public enum RecipientKind {
        TO,
        CC,
        BCC;
    }

    /**
     * Backing Bean for attachments
     */
    @FluentApiBackingBean
    public interface AttachmentBB {
        @FluentApiBackingBeanField()
        File attachment();

        String attachmentName();
    }

    // ---------------------------------------------------
    // -- Commands
    // ---------------------------------------------------

    @FluentApiCommand
    public static class SendEmailCommand {
        public static EmailBB build(EmailBB emailBB) {
            // just return the data object
            return emailBB;
        }
    }

    // ---------------------------------------------------
    // -- Fluent Api
    // ---------------------------------------------------

    @FluentApiRoot
    @FluentApiInterface(EmailBB.class)
    public interface EmailStartInterface {

        AddRecipientTraverse withSender(
                @FluentApiBackingBeanMapping(value = "sender")
                @Matches(EMAIL_PATTERN)
                String sender);

    }

    @FluentApiInterface(EmailBB.class)
    public interface AddRecipientTraverse{
        AddRecipient withRecipients();
    }


    @FluentApiInterface(EmailBB.class)
    public interface AddRecipient{

        @FluentApiInlineBackingBeanMapping("recipients")
        @FluentApiImplicitValue(value = "TO", id = "recipientKind", target = TargetBackingBean.INLINE)
        AddRecipientsOrSetSubject to(
                @FluentApiBackingBeanMapping(value = "emailAddress", target = TargetBackingBean.INLINE)
                @Matches(EMAIL_PATTERN)
                String emailAddress
        );

        @FluentApiInlineBackingBeanMapping("recipients")
        @FluentApiImplicitValue(value = "CC", id = "recipientKind", target = TargetBackingBean.INLINE)
        AddRecipientsOrSetSubject cc(
                @FluentApiBackingBeanMapping(value = "emailAddress", target = TargetBackingBean.INLINE)
                @Matches(EMAIL_PATTERN)
                String emailAddress
        );

        @FluentApiInlineBackingBeanMapping("recipients")
        @FluentApiImplicitValue(value = "BCC", id = "recipientKind", target = TargetBackingBean.INLINE)
        AddRecipientsOrSetSubject bcc(
                @FluentApiBackingBeanMapping(value = "emailAddress", target = TargetBackingBean.INLINE)
                @Matches(EMAIL_PATTERN)
                String emailAddress
        );
    }

    @FluentApiInterface(EmailBB.class)
    public interface AddRecipientsOrSetSubject {
        AddRecipient and();

        AddBodyInterface withSubject(
                @FluentApiBackingBeanMapping(value = "subject")
                String subject
        );

    }

    @FluentApiInterface(EmailBB.class)
    public interface AddBodyInterface {

        AddAttachmentOrCloseCommandInterface withBody(
                @FluentApiBackingBeanMapping(value = "body")
                String body
        );

    }

    @FluentApiInterface(EmailBB.class)
    public interface AddAttachmentOrCloseCommandInterface {

        AddAttachmentInterface addAttachment();


        @FluentApiCommand(SendEmailCommand.class)
        EmailBB build();

    }

    @FluentApiInterface(AttachmentBB.class)
    public interface AddAttachmentFileInterface {

        @FluentApiParentBackingBeanMapping(value = "attachments", action = MappingAction.ADD)
        AddAttachmentOrCloseCommandInterface fromFile(
                @FluentApiBackingBeanMapping(value = "attachment")
                File file
        );
    }

    @FluentApiInterface(AttachmentBB.class)
    public interface AddAttachmentInterface extends AddAttachmentFileInterface {

        AddAttachmentFileInterface withCustomName(
                @FluentApiBackingBeanMapping(value = "attachmentName")
                @NotEmpty String attachmentName
        );

    }


}
