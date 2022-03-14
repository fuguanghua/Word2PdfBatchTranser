package me.fuguanghua.watchservice;

import me.fuguanghua.cache.LinkedBlockingQueueObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.*;
import static java.nio.file.StandardWatchEventKinds.*;
import java.nio.file.attribute.*;
import java.io.*;
import java.util.*;

/**
 * <p>
 *     word文档创建事件监控线程实现
 * </p>
 * @author fuguanghua
 * @date {2022.02.14}
 * @version 1.0
 */
public class WatchDir implements Runnable{
    private static final Logger LOGGER = LoggerFactory.getLogger(WatchDir.class);
    private final WatchService watcher;
    private final Map<WatchKey,Path> keys;
    private final boolean recursive;
    private boolean trace = false;

    @SuppressWarnings("unchecked")
    static <T> WatchEvent<T> cast(WatchEvent<?> event) {
        return (WatchEvent<T>)event;
    }

    /**
     * Register the given directory with the WatchService
     */
    private void register(Path dir) throws IOException {
        WatchKey key = dir.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
        if (trace) {
            Path prev = keys.get(key);
            if (prev == null) {
                System.out.format("register: %s\n", dir);
            } else {
                if (!dir.equals(prev)) {
                    System.out.format("update: %s -> %s\n", prev, dir);
                }
            }
        }
        keys.put(key, dir);
    }

    /**
     * Register the given directory, and all its sub-directories, with the
     * WatchService.
     */
    private void registerAll(final Path start) throws IOException {
        // register directory and sub-directories
        Files.walkFileTree(start, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
                    throws IOException
            {
                register(dir);
                return FileVisitResult.CONTINUE;
            }
        });
    }

    /**
     * Creates a WatchService and registers the given directory
     */
    public WatchDir(Path dir, boolean recursive) throws IOException {
        this.watcher = FileSystems.getDefault().newWatchService();
        this.keys = new HashMap<WatchKey,Path>();
        this.recursive = recursive;

        if (recursive) {
            System.out.format("Scanning %s ...\n", dir);
            registerAll(dir);
            System.out.println("Done.");
        } else {
            register(dir);
        }

        // enable trace after initial registration
        this.trace = true;
    }

    /**
     * Process all events for keys queued to the watcher
     */
    void processEvents() {
        LOGGER.info("进入到创建文档事件监控");
        for (;;) {
            // wait for key to be signalled
            WatchKey key;
            try {
                key = watcher.take();
            } catch (InterruptedException x) {
                return;
            }

            Path dir = keys.get(key);
            if (dir == null) {
                LOGGER.error("WatchKey not recognized!!");
                continue;
            }

            for (WatchEvent<?> event: key.pollEvents()) {
                WatchEvent.Kind kind = event.kind();

                // TBD - provide example of how OVERFLOW event is handled
                if (kind == OVERFLOW) {
                    continue;
                }

                // Context for directory entry event is the file name of entry
                WatchEvent<Path> ev = cast(event);
                Path name = ev.context();
                Path child = dir.resolve(name);

                // print out event
                // System.out.format("%s: %s\n", event.kind().name(), child);
                // LOGGER.info(String.valueOf(new StringBuilder().append(event.kind().name()).append(":").append(child)));

                // if directory is created, and watching recursively, then
                // register it and its sub-directories
                if (recursive && (kind == ENTRY_CREATE)) {
                    LOGGER.error("SubDirectories is not allowed");
                    /*try {
                        if (Files.isDirectory(child, NOFOLLOW_LINKS)) {
                            registerAll(child);
                        }
                    } catch (IOException x) {
                        // ignore to keep sample readbale
                    }*/
                }
                if (kind == ENTRY_CREATE) {
                    try {
                        LOGGER.info("put path to blockingqueue " + child);
                        LinkedBlockingQueueObject.doc_num.put(child.toString());
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    // ConvertDoc2Pdf convertDoc2Pdf = new ConvertDoc2Pdf();
                    // convertDoc2Pdf.ConvertToPDF(child, System.getProperty("user.dir") + "/configuration/pdf");
                }

            }

            // reset key and remove from set if directory no longer accessible
            boolean valid = key.reset();
            if (!valid) {
                keys.remove(key);
                // all directories are inaccessible
                if (keys.isEmpty()) {
                    break;
                }
            }
        }
    }

    public void run() {
        LOGGER.info("当前doc处理线程号：" + Thread.currentThread().getId());
        this.processEvents();
        // String folder = System.getProperty("user.dir") + "/configuration/doc";
        // Path dir = Paths.get(folder);
        // boolean recursive = false;
        // try {
            // new WatchDir(dir, recursive).processEvents();
        //    this.processEvents();
        // } catch (IOException e) {
        //    LOGGER.error(e.getMessage());
        // }
    }
}
