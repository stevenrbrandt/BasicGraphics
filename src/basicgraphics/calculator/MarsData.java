/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package basicgraphics.calculator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.Scanner;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

/**
 * This class fetches live data about Mars from the internet.
 *
 * @author sbrandt
 */
public class MarsData {

    public double rightAscension;
    public double declination;
    public double sunDistance;
    public double earthDistance;

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("MarsData{");
        sb.append(String.format("rightAscension=%.6f,", rightAscension));
        sb.append(String.format("declination=%.6f,", declination));
        sb.append(String.format("sunDistance=%.6f,", sunDistance));
        sb.append(String.format("earthDistance=%.6f", earthDistance));
        sb.append("}");
        return sb.toString();
    }

    public MarsData() {

        try {
            Pattern rightAscensionP = Pattern.compile("Right.*Ascension:.*>\\s*([-\\d.]+)\\s*<");
            Pattern declinationP = Pattern.compile("Declination:.*>\\s*([-\\d.]+)\\s*<");
            Pattern sunDistanceP = Pattern.compile("Sun.*Distance:.*>\\s*([-\\d.]+)\\s*<");
            Pattern earthDistanceP = Pattern.compile("Earth.*Distance:.*>\\s*([-\\d.]+)\\s*<");
            Scanner sc = null;

            String info = System.getProperty("user.home", ".") + "/mars_info.txt";
            File finfo = new File(info);
            if (finfo.exists() && finfo.lastModified() + 10 * 60 * 1000 > System.currentTimeMillis()) {
                sc = new Scanner(finfo);
            } else {
                URL url = new URL("https://theskylive.com/mars-tracker");
                sc = new Scanner(url.openStream());
            }
            while (sc.hasNextLine()) {
                if (sc.findInLine(rightAscensionP) != null) {
                    MatchResult mr = sc.match();
                    rightAscension = Double.parseDouble(mr.group(1));
                }
                if (sc.findInLine(declinationP) != null) {
                    MatchResult mr = sc.match();
                    declination = Double.parseDouble(mr.group(1));
                }
                if (sc.findInLine(sunDistanceP) != null) {
                    MatchResult mr = sc.match();
                    sunDistance = Double.parseDouble(mr.group(1));
                }
                if (sc.findInLine(earthDistanceP) != null) {
                    MatchResult mr = sc.match();
                    earthDistance = Double.parseDouble(mr.group(1));
                }
                sc.nextLine();
            }
            PrintWriter pw = new PrintWriter(finfo);
            pw.printf("Right Ascension: >%f<%n",rightAscension);
            pw.printf("Declination: >%f<%n",declination);
            pw.printf("Earth Distance: >%f<%n",earthDistance);
            pw.printf("Sun Distance: >%f<%n",sunDistance);
            pw.close();
        } catch (IOException ioe) {
        }
    }
}
