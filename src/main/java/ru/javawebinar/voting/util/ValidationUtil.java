package ru.javawebinar.voting.util;


import org.slf4j.Logger;
import ru.javawebinar.voting.HasId;
import ru.javawebinar.voting.util.exception.IllegalRequestDataException;
import ru.javawebinar.voting.util.exception.InvalidDateTimeException;
import ru.javawebinar.voting.util.exception.NotFoundException;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalTime;

public class ValidationUtil {

    public static final LocalTime TIME = LocalTime.of(11, 0);

    private ValidationUtil() {
    }

    public static <T> T checkNotFoundWithId(T object, int id) {
        return checkNotFound(object, "id=" + id);
    }

    public static void checkNotFoundWithId(boolean found, int id) {
        checkNotFound(found, "id=" + id);
    }

    public static <T> T checkNotFound(T object, String msg) {
        checkNotFound(object != null, msg);
        return object;
    }

    public static void checkNotFound(boolean found, String msg) {
        if (!found) {
            throw new NotFoundException("Not found entity with " + msg);
        }
    }

    public static void checkNew(HasId bean) {
        if (!bean.isNew()) {
            throw new IllegalRequestDataException(bean + " must be new (id=null)");
        }
    }

    public static void assureIdConsistent(HasId bean, int id) {
//      conservative when you reply, but accept liberally (http://stackoverflow.com/a/32728226/548473)
        if (bean.isNew()) {
            bean.setId(id);
        } else if (bean.getId() != id) {
            throw new IllegalRequestDataException(bean + " must be with id=" + id);
        }
    }

    public static void checkInvalidDate(LocalDate actual, LocalDate expected) {
        if (actual.compareTo(expected) != 0) {
            throw new InvalidDateTimeException("Date " + actual + " is invalid");
        }
    }

    public static void checkInvalidTime(LocalTime time) {
        if (time.compareTo(TIME) > 0) {
            throw new InvalidDateTimeException("Time " + time + " is invalid");
        }
    }

    //  http://stackoverflow.com/a/28565320/548473
    public static Throwable getRootCause(Throwable t) {
        Throwable result = t;
        Throwable cause;

        while (null != (cause = result.getCause()) && (result != cause)) {
            result = cause;
        }
        return result;
    }

    public static String getMessage(Throwable e) {
        return e.getLocalizedMessage() != null ? e.getLocalizedMessage() : e.getClass().getName();
    }

    public static Throwable logAndGetRootCause(Logger log, HttpServletRequest req, Exception e, boolean logException) {
        Throwable rootCause = ValidationUtil.getRootCause(e);
        if (logException) {
            log.error("Request " + req.getRequestURL(), rootCause);
        } else {
            log.warn("Request  {}: {}", req.getRequestURL(), rootCause.toString());
        }
        return rootCause;
    }
}