package com.app.risk.model;

import java.util.Observable;

/**
 * This model class would hold the contents that are to be written to the logger
 *
 * @author Akhila Chilukuri
 * @version 1.0.0
 */
public class Log extends Observable {
    String message;

    /**
     * This method sets the message that has to be displayed in the logger
     *
     * @param msg: message that has to displayed in the logger
     */
    public void setMessage(String msg) {
        message = msg;
        setChanged();
        notifyObservers(this);
    }

    /**
     * This method gets the message that has to be displayed in the logger
     *
     * @return message that would be displayed in logger of type string
     */
    public String getMessage() {
        return message;
    }

    /**
     * This method is called when the object is printed
     *
     * @return message that object contains at that time
     */
    public String toString() {
        return message;
    }
}
