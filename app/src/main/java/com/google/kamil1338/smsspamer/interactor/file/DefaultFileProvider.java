package com.google.kamil1338.smsspamer.interactor.file;

import android.content.res.AssetManager;

import com.google.kamil1338.smsspamer.utils.App;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Kamil on 2016-07-06.
 */
public class DefaultFileProvider {

    /**
     * <p>
     *     Kopiowanie przykładowych plików z assetów na kartę sd.
     *     @param assetFileName Nazwa pliku w assetach do skopiowania.
     *     @param directory Folder, do którego zostanie skopiowany plik z assetów.
     *     @return Zwraca informację o powodzeniu operacji.
     * </p>
     * */
    public static boolean provideDefaultFile(String assetFileName, File directory) {
        AssetManager assetManager = App.getContext().getAssets();

        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = assetManager.open(assetFileName);
            File outputFile = new File(directory, assetFileName);
            outputStream = new FileOutputStream(outputFile);

            byte[] buffer = new byte[1024];
            int readBytes;
            while ((readBytes = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, readBytes);
            }
            inputStream.close();
            outputStream.flush();
            outputStream.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
