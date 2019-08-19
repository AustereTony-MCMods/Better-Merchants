package austeretony.better_merchants.common;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import austeretony.better_merchants.common.main.BetterMerchantsMain;

public class MerchantsLoader {

    public static void loadPersistentData(IPersistentData persistentData) {
        Path path = Paths.get(persistentData.getPath());
        if (Files.exists(path)) {
            try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(persistentData.getPath()))) {    
                persistentData.read(bis);
            } catch (IOException exception) {
                BetterMerchantsMain.LOGGER.error("Persistent data loading failed! Path: {}", persistentData.getPath());
                exception.printStackTrace();
            }
        }
    }

    public static void savePersistentData(IPersistentData persistentData) {
        Path path = Paths.get(persistentData.getPath());
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path.getParent());
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(persistentData.getPath()))) {   
            persistentData.write(bos);
        } catch (IOException exception) {
            BetterMerchantsMain.LOGGER.error("Persistent data saving failed! Path: {}", persistentData.getPath());
            exception.printStackTrace();
        }
    }
}