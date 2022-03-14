package me.fuguanghua.worker;

import me.fuguanghua.cache.LinkedBlockingQueueObject;
import me.fuguanghua.logger.LogFileName;
import me.fuguanghua.logger.LoggerUtils;
import org.slf4j.Logger;

import java.io.File;

/*
 * @deprecated 废弃
 * @author fuguanghua
 * @date {2022.02.14}
 * @version 1.0
 */
public class FilesList2LBQ implements Runnable {
    private final static Logger filesList2LBQLogger = LoggerUtils.Logger(LogFileName.WORD2PDF);

    private void getFilesList(String filepath) {
        File folder = new File(filepath);
        File[] listOfFiles = folder.listFiles();

        assert listOfFiles != null;
        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                try {
                    LinkedBlockingQueueObject.doc_num.put(listOfFiles[i].getPath());
                } catch (InterruptedException e) {
                    filesList2LBQLogger.error("当前线程发生中断，自动复位！");
                    Thread.currentThread().interrupt();
                }
            } else if (listOfFiles[i].isDirectory()) {
                filesList2LBQLogger.error("转换目录不应该包含子目录！");
            }
        }
    }

    @Override
    public void run() {
    }
}
