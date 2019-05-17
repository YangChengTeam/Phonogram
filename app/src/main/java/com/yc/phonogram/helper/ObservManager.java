package com.yc.phonogram.helper;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by wanglin  on 2019/5/16 15:22.
 */
public class ObservManager extends Observable {

    private static ObservManager instance;

    private ObservManager() {
    }

    public static ObservManager get() {
        synchronized (ObservManager.class) {
            if (instance == null) {
                synchronized (ObservManager.class) {
                    instance = new ObservManager();
                }
            }
        }
        return instance;

    }


    public void addMyObserver(Observer observer){
        addObserver(observer);
    }

    public void notifyMyObserver(Object object) {
        setChanged();
        notifyObservers(object);

    }

    public void deleteMyObserver(Observer observer) {
        deleteObserver(observer);
    }

    public void deleteMyObservers() {
        deleteObservers();
    }
}
