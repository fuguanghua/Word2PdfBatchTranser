package me.fuguanghua.executor;

import me.fuguanghua.cache.LinkedBlockingQueueObject;
import me.fuguanghua.worker.ConvertDoc2Pdf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Path;

/**
 * <p>
 *     word2pdf转换线程实现
 * </p>
 * @author fuguanghua
 * @date {2022.02.14}
 * @version 1.0
 */
public class Doc2Pdf implements Runnable{
    private static final Logger LOGGER = LoggerFactory.getLogger(Doc2Pdf.class);
    // private static final String folder = System.getProperty("user.dir") + "/configuration/pdf";

    @Override
    public void run() {
        LOGGER.info("当前处理线程：" + Thread.currentThread().getId());
        for(;;) {
            try {
                LOGGER.info("take path from blocking queue ");
                String source = LinkedBlockingQueueObject.doc_num.take();
                String folder = new File(System.getProperty("user.dir")).getParentFile() + "\\workdir\\pdf" + source.substring(source.lastIndexOf("\\"), source.lastIndexOf(".")) + ".pdf";
                new ConvertDoc2Pdf().ConvertToPDF(source, folder);
            } catch (InterruptedException e) {
                LOGGER.error("Doc2pdf工作线程发生中断");
                Thread.currentThread().interrupt();
                LOGGER.error("Doc2pdf中断线程已复位");
            }
        }
    }
}
