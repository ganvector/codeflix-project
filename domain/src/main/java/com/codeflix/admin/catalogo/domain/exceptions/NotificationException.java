package com.codeflix.admin.catalogo.domain.exceptions;

import com.codeflix.admin.catalogo.domain.validation.Error;
import com.codeflix.admin.catalogo.domain.validation.handlers.Notification;

import java.util.List;

public class NotificationException extends DomainException {
    public NotificationException(String aMessage, final Notification notification) {
        super(aMessage, notification.getErrors());
    }
}
