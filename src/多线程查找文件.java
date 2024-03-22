import java.io.*;
import java.nio.file.Files;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class 多线程查找文件 {

    private static final ExecutorService executorService = Executors.newFixedThreadPool(8);
    private static final List<String> list = new Vector<>();
    private static String fileName;
    private static final AtomicInteger fileNum = new AtomicInteger(0);
    private static int exit = 0;

    public static void main(String[] args) throws InterruptedException, IOException {
        Scanner sc = new Scanner(System.in);
        System.out.println("请输入盘符（如果为空则查全部盘符）：");
        String roots = sc.nextLine();
        System.out.println("请输入要查找的文件名：");
        fileName = sc.next();
        if (fileName == null || fileName.isEmpty()) {
            System.out.println("文件名不能为空，程序退出。");
            System.exit(0);
        }
        sc.close(); // 关闭Scanner对象

        File[] files = File.listRoots();
        for (File file : files) {
            if (roots.isEmpty() || file.getAbsolutePath().startsWith(roots)) {
                executorService.submit(() -> extracted(file));
            }
        }

        while (true) {
            Thread.sleep(100);
            int file = fileNum.get();
            if (Objects.equals(file,exit)) {
                break;
            }
            System.out.println("已处理文件数：" + file);
            exit = file;
        }

        executorService.shutdown();
        System.out.println("处理完成！");
        System.out.println("正在关闭线程池...");
        if (!executorService.awaitTermination(2, TimeUnit.SECONDS)) {
            executorService.shutdownNow(); // 强制关闭ExecutorService
        }

        list.forEach(System.out::println);
        System.out.println("查询完成，共找到：" + list.size() + " 个文件");
        File file = new File(System.getProperty("user.dir") + File.separator + "查找文件.txt");
        if(!file.createNewFile()){
            System.err.println("文件生成失败！");
        }
        BufferedWriter bw = new BufferedWriter(new FileWriter(file));
        for (String s : list) {
            bw.write(s);
            bw.newLine();
        }
        bw.close();
    }

    private static void extracted(File file) {
        File[] files = file.listFiles();
        if (files == null) return;
        for (File file1 : files) {
            fileNum.incrementAndGet();
            if (file1.getName().contains(fileName)) {
                add(file1.getAbsolutePath());
            }
            if (file1.isDirectory()) {
                executorService.submit(() -> extracted(file1));
            }
        }
    }

    private static synchronized void add(String filePath) {
        list.add(filePath);
    }
}