package com.company;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
	// write your code here
        List<Long> filePTS = readPTS(new File("/home/zolotar/solo/recordedPTS.txt"));
        long pts = 0;
        for (int i = 0; i < filePTS.size(); i++) {
            long value = filePTS.get(i);
            long prev = (i > 0) ? filePTS.get(i - 1) : 0;
            System.out.println(String.format("#%3d pts: %12d diff %12d pts", i, value, (value - prev)/1000));
        }
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
}
