package me.fuguanghua.transfer;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.ComThread;
import com.jacob.com.Dispatch;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public class TransDoc2Pdf {
    private static final Logger LOGGER = LoggerFactory.getLogger(TransDoc2Pdf.class);
    /**
     * 测试本地Office转换word到pdf
     */
    public static void main(String[] args) {
        String source = "D:\\nginx\\Word2PdfBatchTranser\\configuration\\doc\\000533_永赢货币市场基金2021年第四季度报告.doc";
        String target = "D:\\nginx\\Word2PdfBatchTranser\\configuration\\pdf\\000533_永赢货币市场基金2021年第四季度报告.pdf";

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

            System.out.println("打开文档" + source);
            doc = Dispatch.call(docs, "Open", source, false, true).toDispatch();
            System.out.println("转换文档到PDF " + target);
            Dispatch.call(doc, "SaveAs", target, 17); // wordSaveAsPDF为特定值17

            long end = System.currentTimeMillis();
            System.out.println("转换完成用时：" + (end - start) + "ms.");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (doc != null) {
                Dispatch.call(doc, "Close", false);
            }

            if (app != null) {
                app.invoke("Quit", 0); // 不保存待定的更改
            }

            ComThread.Release();

            File pdf = new File(target);
            PDDocument document = null;
            try {
                document = PDDocument.load(pdf);
                // PDDocumentInformation information = document.getDocumentInformation();
                // document.getDocumentCatalog().setMetadata(null);
                // PDDocumentInformation information = document.getDocumentInformation();
                // information.setAuthor(null);
                // System.out.println(information.getAuthor());
                document.setDocumentInformation(new PDDocumentInformation());
                document.save(pdf);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    document.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
