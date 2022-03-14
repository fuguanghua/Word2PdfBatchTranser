package me.fuguanghua.configuration;

import me.fuguanghua.logger.LogFileName;
import me.fuguanghua.logger.LoggerUtils;
import org.slf4j.Logger;

import java.io.*;
// import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

/**
 * <p>
 *     静态配置内，程序启用时加载配置
 *     linkBlockQueueCapcity--核心数据结构容量
 *     workerdoc2pdfxx--word2pdf线程池初始化容量
 *     workerdoc2pdfxm--word2pdf线程池最大容量
 * </p>
 * @author fuguanghua
 * @date {2022.02.14}
 * @version 1.0
 */
public final class Conf {
    private final static Logger confLogger = LoggerUtils.Logger(LogFileName.WORD2PDF);

    public final static int linkBlockQueueCapcity;
    public final static int workerdoc2pdfxx;
    public final static int workerdoc2pdfxm;

    private static BufferedInputStream inputStream;
    private static ResourceBundle bundle;

    static {

        String proFilePath = new File(System.getProperty("user.dir")).getParentFile()+ "/configuration/application.properties";
        // Locale en_US = new Locale("en", "US");
        try {
            inputStream = new BufferedInputStream(new FileInputStream(proFilePath));
            bundle = new PropertyResourceBundle(inputStream);
        } catch (FileNotFoundException e) {
            confLogger.error(e.getMessage());
        } catch (IOException e) {
            confLogger.error(e.getMessage());
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                confLogger.error(e.getMessage());
            }
        }

        linkBlockQueueCapcity = Integer.valueOf(bundle.getString("LinkBlockQueue.Capcity"));
        workerdoc2pdfxx = Integer.valueOf(bundle.getString("Workers.Doc2Pdf.Min"));
        workerdoc2pdfxm = Integer.valueOf(bundle.getString("Workers.Doc2Pdf.Max"));
    }
}
