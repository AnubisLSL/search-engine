package com.anubis.li.searchengine.core.exception;

public class SearchException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public SearchException(String message){
        super(message);
    }

    public SearchException(Throwable cause)
    {
        super(cause);
    }

    public SearchException(String message, Throwable cause)
    {
        super(message,cause);
    }
}
