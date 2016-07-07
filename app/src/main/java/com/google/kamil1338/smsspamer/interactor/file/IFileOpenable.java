package com.google.kamil1338.smsspamer.interactor.file;

import java.util.List;
import java.util.ListIterator;

import com.google.kamil1338.smsspamer.ISmsService;

/**
 * <p>
 *     Pobranie danych z pliku.
 *
 *     Created by pierudzki on 2016-06-01.
 * </p>
 */
public interface IFileOpenable {

    /**
     * @return Zwraca listę z danymi. Lista może być pusta.
     * */
    List<String> getReadedData();

    /**
     * <p>
     *     @return Zwraca iterator z pobranej listy. Jeśli trzeba to iterator jest
     *     przesunięty na pozycje jeszcze nie odwiedzone.
     * </p>
     * */
    ListIterator<String> getReadedDataIterator();

    /**
     * <p>
     *     @return Zwraca ilość elementów, które zostały odczytane.
     * </p>
     * */
    int getReadedDataSize();

    /**
     * <p>
     *     Podanie listenera i błuskawiczna odpowiedz z onformacją dla widoku.
     * </p>
     * */
    void getResponse(ISmsService.ISmsServiceListener listener);
}
