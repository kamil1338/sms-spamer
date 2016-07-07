package com.google.kamil1338.smsspamer.view.fragment;

/**
 * Created by pierudzki on 2016-06-02.
 */
public interface ISynchronizable {
    /**
     * <p>
     *     Ustawienie obiektu synchronizującego.
     *
     *     @param syncObject Obiekt synchronizujący.
     * </p>
     * */
    void setSyncObject(final Object syncObject);

    /**
     * @return Zwraca informację o tym czy widok został utworzony.
     * */
    boolean isViewCreated();
}
