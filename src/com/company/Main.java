package com.company;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
//        ptsTiming();
//
//        try {
//            fetchVideos(new File("/home/zolotar/solo/bg_video_15"), new File("/home/zolotar/solo/bg_video_15/files.txt"));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        String ffmpegCommand = "ffmpeg -i %s -t 10 -vf scale=w=512:h=512 -c:v libx264 -an cropped/%s.mp4";
//        try {
//            writeFFmpegCommand("/home/zolotar/solo/bg_video_15/files.txt", "/home/zolotar/solo/bg_video_15/ffmpeg.txt", ffmpegCommand);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        try {
//            Map<String, String> translationRus = generateStringsMap("/home/zolotar/solo/translation/strings-rus.xml");
//            for(Map.Entry<String, String> pair : translationRus.entrySet()) {
//                System.out.printf("key %40s value %20s\n", pair.getKey(), pair.getValue());
//            }
//            System.out.println("size: " + translationRus.size());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
       File sourceDir = new File("/home/zolotar/solo/translation");
        for (File file : sourceDir.listFiles()) {
            refactorTranslationFile(file.getAbsolutePath(),
                    "/home/zolotar/solo/translation/refactored/" + file.getName(),
                    "/home/zolotar/solo/translation/refactored/strings.xml");
        }
    }

    private static void refactorTranslationFile(String source, String dest, String pattern){

        try {
            Map<String, String> translationMap = generateStringsMap(source);
//            for(Map.Entry<String, String> pair : translationRus.entrySet()) {
//                System.out.printf("key %40s value %20s\n", pair.getKey(), pair.getValue());
//            }
//            System.out.println("size: " + translationRus.size());
            BufferedReader reader = new BufferedReader(new FileReader(pattern));
            BufferedWriter writer = new BufferedWriter(new FileWriter(dest));
            String line;
            String key;
            String value;
            String keyStart = "name=\"";
            String keyEnd = "\">";
            String valueStart = "\">";
            String valueEnd = "</string>";
            while ((line = reader.readLine()) != null) {
                try {
                    key = line.substring(line.indexOf(keyStart) + keyStart.length(), line.indexOf(keyEnd));
                    value = line.substring(line.indexOf(valueStart) + valueStart.length(), line.indexOf(valueEnd));
                    if (translationMap.containsKey(key)) {
                        line = line.replace(value, translationMap.get(key));
                    }
                } catch (Exception e) {
                    System.out.println("error with line: " + line);
                } finally {
                    writer.write(line + "\n");
                }
            }
            reader.close();
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Map<String, String> generateStringsMap(String fileName) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        Map<String, String> translationMap = new HashMap<>();
        String line;
        String key;
        String value;
        String keyStart = "name=\"";
        String keyEnd = "\">";
        String valueStart = "\">";
        String valueEnd = "</string>";
        while ((line = reader.readLine()) != null) {
            try {
                key = line.substring(line.indexOf(keyStart) + keyStart.length(), line.indexOf(keyEnd));
                value = line.substring(line.indexOf(valueStart) + valueStart.length(), line.indexOf(valueEnd));
                translationMap.put(key, value);
            } catch (Exception e) {
                System.out.println("error with line: " + line);
            }
        }
        reader.close();
        return translationMap;
    }

    private static void writeFFmpegCommand(String fileList, String outFIleList, String ffmpegCommand) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(fileList));
        BufferedWriter writer = new BufferedWriter(new FileWriter(outFIleList));
        String line;
        while ((line = reader.readLine()) != null) {
            writer.write(String.format(ffmpegCommand, line, getFileNameWithoutExt(line)));
            writer.write("\n");
        }
        writer.flush();
        writer.close();
        reader.close();
    }
    private static String getFileNameWithoutExt(String fileName) {
        System.out.println("FILE: " + fileName);
        return fileName.substring(0, fileName.indexOf("."));
    }

    private static void fetchVideos(File directory, File outputFile) throws IOException {
        if (directory.isDirectory()) {
            BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
            File[] files = directory.listFiles();
            for (File file : files) {
//                writer.write(getFileNameWithoutExt(file.getName()));
                writer.write(file.getName());
                writer.write("\n");
            }
            writer.flush();
            writer.close();
        }
    }

    private static void ptsTiming() {
        // write your code here
        List<Long> recordedPTS = readPTS(new File("/home/zolotar/solo/recordedPTS.txt"));
//        for (int i = 0; i < recordedPTS.size(); i++) {
//            long value = recordedPTS.get(i);
//            long prev = (i > 0) ? recordedPTS.get(i - 1) : 0;
//            System.out.println(String.format("rPTS #%3d pts: %12d diff %12d pts", i, value - recordedPTS.get(0), (value - prev)/1000));
//        }

        List<Long> playedPTS = readPTS(new File("/home/zolotar/solo/playedPTS.txt"));
//        for (int i = 0; i < playedPTS.size(); i++) {
//            long value = playedPTS.get(i);
//            long prev = (i > 0) ? playedPTS.get(i - 1) : 0;
//            System.out.println(String.format("pPTS#%3d pts: %12d diff %12d pts", i, value, (value - prev)/1000));
//        }
        int n = recordedPTS.size() > playedPTS.size() ? playedPTS.size() : recordedPTS.size();
        System.out.println("recordedPTS.size: " + recordedPTS.size() + " playedPTS.size: " + playedPTS.size());
        List<Float> ffmpegPTS = readFFmpegPTS(new File("/home/zolotar/solo/ffmpeg_logs.txt"));
        long rValue, pValue, rPrevValue, pPrevValue, rDiff, pDiff;
        for (int i = 0; i < n; i++) {
            rValue = recordedPTS.get(i);
            pValue = playedPTS.get(i);

            rPrevValue = i > 0 ? recordedPTS.get(i - 1) : 0;
            pPrevValue = i > 0 ? playedPTS.get(i - 1) : 0;

            System.out.println(String.format("rPTS: %8d pPTS: %8d ffmpeg: %8d", rValue - recordedPTS.get(0), pValue, (int)(ffmpegPTS.get(i) * 1000*1000)));

        }
    }

    public static long nStoMS(long nsec) {
        return nsec / 1000;
    }

    private static List<Long> readPTS(File file) {
        ArrayList<Long> resultPTS = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                resultPTS.add(Long.parseLong(line.trim()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return resultPTS;
    }

    public static List<Float> readFFmpegPTS(File file) {
        String startSTR = "pts_time:";
        String endSTR = "pos:";
        ArrayList<Float> resultPTS = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("line: " + line);

                line = line.substring(line.indexOf(startSTR) + startSTR.length(), line.indexOf(endSTR));
                resultPTS.add(Float.parseFloat(line.trim()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return resultPTS;
    }
}
