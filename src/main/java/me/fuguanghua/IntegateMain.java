package me.fuguanghua;

import me.fuguanghua.configuration.Conf;
import me.fuguanghua.executor.Doc2Pdf;
import me.fuguanghua.watchservice.WatchDir;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * <p>
 *     程序入口，主线程
 *     调度文档创建监控线程和转换线程池
 * </p>
 * @author fuguanghua
 * @date {2022.02.14}
 * @version 1.0
 */
public class IntegateMain {
    private static final Logger LOGGER = LoggerFactory.getLogger(IntegateMain.class);

    public static void main(String[] args) {
        ExecutorService executor_watchdir = Executors.newSingleThreadExecutor();
        ExecutorService executor_doc2pdf = Executors.newFixedThreadPool(Conf.workerdoc2pdfxx);

        Path dir = Paths.get(new File(System.getProperty("user.dir")).getParentFile()+ "/workdir/doc");
        try {
            executor_watchdir.submit(new WatchDir(dir, false));
            for (int i=0; i < Conf.workerdoc2pdfxx; ++i) {
                executor_doc2pdf.submit(new Doc2Pdf());
            }
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
    }
}
