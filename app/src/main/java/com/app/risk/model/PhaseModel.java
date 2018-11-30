package com.app.risk.model;

import com.app.risk.view.PlayScreenActivity;

import java.util.ArrayList;
import java.util.Observable;

/**
 * This model class would hold the contents that are to be written to the logger
 *
 * @author Akhila Chilukuri
 * @version 1.0.0
 */
public class PhaseModel extends Observable {
    /**
     * Stores list of messages for phases
     */
    private ArrayList<String> message=new ArrayList<String>();

    /**
     * This method sets the message that has to be displayed in the logger
     *
     * @param msg: message that has to displayed in the logger
     */
    public void setAction(String msg) {
        message.add(msg);
        setChanged();
        notifyObservers(this);
    }

    /**
     * This method gets the message that has to be displayed in the logger
     *
     * @return message that would be displayed in logger of type string
     */
    public ArrayList<String> getActions() {
        return message;
    }

    /**
     * This method clears the logs that is displayed in the logger
     *
     *
     */
    public void clear() {
        message.clear();
        setChanged();
        notifyObservers(this);
    }
}
