package com.company;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
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
