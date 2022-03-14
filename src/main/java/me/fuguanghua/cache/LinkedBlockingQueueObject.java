package me.fuguanghua.cache;

import me.fuguanghua.configuration.Conf;

import java.nio.file.Path;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * <p>
 *     并发核心数据结构，用于文件created事件监控线程和word2pdf工作线程池之间的同步、word2pdf工作线程池内部线程之间的并发同步，
 *     线程安全的LinkedBlockingQueue实例，容量根据配置文件初始化
 *     文件created事件监控线程收到文件创建事件后把新建文件URL写入LinkedBlockingQueue
 *     word2pdf工作线程池线程竞争地从LinkedBlockingQueue读取写入的新建文件URL，没有可以读取的新建文件URL时，等待在LinkedBlockingQueue
 * </p>
 * @author fuguanghua
 * @date {2022.02.14}
 * @version 1.0
 */
public interface LinkedBlockingQueueObject {
    BlockingQueue<String> doc_num = new LinkedBlockingQueue<>(Conf.linkBlockQueueCapcity);
}
