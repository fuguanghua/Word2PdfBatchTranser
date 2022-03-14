package me.fuguanghua.worker;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.ComThread;
import com.jacob.com.Dispatch;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Path;

/**
 * <p>
 *     word转换pdf方法类
 *     清空pdf metadata方法实现
 * </p>
 * @author fuguanghua
 * @date {2022.02.14}
 * @version 1.0
 */
public final class ConvertDoc2Pdf {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConvertDoc2Pdf.class);

    public void ConvertToPDF(String source, String target) {
        long start = System.currentTimeMillis();
        ActiveXComponent app = null;
        Dispatch doc = null;
        try {
            File targetFile = new File(target);
            if (targetFile.exists()) {
                targetFile.delete();
            }

            ComThread.InitSTA();
            app = new ActiveXComponent("Word.Application");
            app.setProperty("Visible", false);
            Dispatch docs = app.getProperty("Documents").toDispatch();

            LOGGER.info("打开文档" + source);
            doc = Dispatch.call(docs, "Open", source, false, true).toDispatch();

            LOGGER.info("转换文档到PDF " + target);
            Dispatch.call(doc, "SaveAs", target, 17); // wordSaveAsPDF为特定值17

            long end = System.currentTimeMillis();
            LOGGER.info("转换完成用时：" + (end - start) + "ms.");
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        } finally {
            if (doc != null) {
                Dispatch.call(doc, "Close", false);
            }

            if (app != null) {
                app.invoke("Quit", 0); // 不保存待定的更改
            }

            File sourceFile = new File(source);
            if (sourceFile.exists()) {
                sourceFile.delete();
            }

            ComThread.Release();

            File pdf = new File(target);
            PDDocument document = null;
            try {
                document = PDDocument.load(pdf);
                document.setDocumentInformation(new PDDocumentInformation());
                document.save(pdf);
            } catch (IOException e) {
                LOGGER.error(e.getMessage());
            } finally {
                try {
                    document.close();
                } catch (IOException e) {
                    LOGGER.error(e.getMessage());
                }
            }
        }
    }
}
