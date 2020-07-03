package com.iromanchenko.exam.sender.beans;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Value;

import java.util.List;

@Value
public class Mail {
    Email from;
    String subject;
    List<Personalization> personalizations;
    List<Content> content;
    List<Attachment> attachments;

    @Value
    public static class Email {
        @JsonInclude(JsonInclude.Include.NON_NULL)
        String name;
        String email;

    }

    @Value
    public static class Personalization {
        List<Email> to;
    }

    @Value
    public static class Content {
        String type;
        String value;
    }

    @Value
    public static class Attachment {
        String content; // base64 encoded file content
        String fileName; // file name
    }

}
