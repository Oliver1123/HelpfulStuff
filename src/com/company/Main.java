package com.company;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
//        ptsTiming();
//
//        try {
//            fetchVideos(new File("/home/zolotar/solo/bg_video_15"), new File("/home/zolotar/solo/bg_video_15/files.txt"));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        String ffmpegCommand = "ffmpeg -i %s -t 10 -vf scale=w=512:h=512 -c:v libx264 -an cropped/%s.mp4";
        try {
            writeFFmpegCommand("/home/zolotar/solo/bg_video_15/files.txt", "/home/zolotar/solo/bg_video_15/ffmpeg.txt", ffmpegCommand);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
