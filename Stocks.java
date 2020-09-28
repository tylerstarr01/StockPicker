/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stocks;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author tyler
 */
public class Stocks {
    
    public static String removeSigns(String input) {
      //  input = input.substring(1);
        boolean on = false;
        String out = "";
        for (int i = 0; i < input.length(); i++) {
            char curr = input.charAt(i);
            if (curr == '<') {
                on = true;
            } else if (curr == '>') {
                on = false;
                continue;
            } else if (curr == ';' || curr == ',') {
                continue;
            } else if (input.substring(i).startsWith("&nbsp")) {
                input = " " + input.substring(i);
            }
            if (!on) {
                out += curr;
            }
        }
        return out;
    }
    
    
    public static ArrayList<String> outputToReadable(ArrayList<String> s) {
        ArrayList<String> tickers = new ArrayList<>();
        for (String str : s) {
            String ss = str.substring(str.indexOf("c&t=") + 4);
            String newSS = ss.substring(0, ss.indexOf("'"));
            int len = "><br>&nbsp;<b>".length();
            String name = ss.substring(len);
            String[] n = name.split("]");
            tickers.add(newSS + " " + removeSigns(n[0]));
        }
        return tickers;
    }

    public static boolean getListSize(ArrayList<Filter> filters) throws IOException {
        String addOns = "https://finviz.com/screener.ashx?v=111&f=";
        for (int i = 0; i < filters.size(); i++) {
            Filter t = filters.get(i);
            addOns += t.fb + "_" + t.pickGood() + ",";
        }
        addOns += "&ft=2";
        URL url = new URL(addOns);
        HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
        urlc.addRequestProperty("User-Agent", "Mozilla/4.76");
        BufferedReader br = new BufferedReader(new InputStreamReader(urlc.getInputStream()));
        String line;
        int amount = 0;

        for (int i = 0; i < 300; i++) {
            br.readLine();
        }
        System.out.println(addOns);
        ArrayList<String> stonks = new ArrayList<>();
        while ((line = br.readLine()) != null) {
            //System.out.println(line);
            if (line.startsWith("<td height=")) {
                amount++;
                stonks.add(line);
            }
            if (amount >= 20) {
                return false;
            }
        }
        if (amount == 0) {
            return false;
        }
        ArrayList<String> tickers = outputToReadable(stonks);
        for (String s : tickers) {
            System.out.println(s);
            
        }
        return true;
    }

    public static int pickIndex(int size) {
        return (int) (Math.random() * size);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {

       //URL url = new URL("https://finviz.com/screener.ashx?v=111&ft=2");
        //String u = "http://www.mapquestapi.com/directions/v2/dragroute?key=aOgFRJqvFEZXgSlrhJEwlAWBFi1sBb9E&json={\"mapState\":{\"center\":{\"lat\":40.47683477496205,\"lng\":-76.46776167187544},\"width\":400,\"height\":300,\"scale\":1733371},\"locations\":[{\"linkId\":26982167,\"latLng\":{\"lng\":-77.793263,\"lat\":40.777703},\"dragPoint\":false},{\"latLng\":{\"lng\":-76.85228315623507,\"lat\":40.296919938790374},\"dragPoint\":true},{\"linkId\":37654138,\"latLng\":{\"lng\":-75.16562,\"lat\":39.951061},\"dragPoint\":false}]}";
        // URL url = new URL(u);
       // HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
        //urlc.addRequestProperty("User-Agent", "Mozilla/4.76");
        //BufferedReader br = new BufferedReader(new InputStreamReader(urlc.getInputStream()));
        //InputStream is = url.openStream();
        //String line;
        ArrayList<Filter> filters = new ArrayList<>();

        File fff = new File("temp.txt");
        Scanner sc = new Scanner(fff);
        while (sc.hasNext()) {
            Filter nf = new Filter(sc.nextLine());
            filters.add(nf);
        }
        ArrayList<Filter> temp = new ArrayList<>();
        temp.add(filters.get(pickIndex(filters.size())));
        while (!getListSize(temp)) {
            temp.add(filters.get(pickIndex(filters.size())));
        }

    }

}

/* Create 
 while ((line = br.readLine()) != null) {
 if (line.contains("<td width=")) {
 for (String l = br.readLine(); !l.equals("</td>") || l == null; l = br.readLine()) {
 curr += l;
 }
 }
 if (curr.contains("value=\"Reset (1)")) {
 break;
 }
 Filter filt = new Filter();
 filt.init(curr);
 f.add(filt);
 //System.out.println(curr);
 curr = "";
 }
 try (FileWriter fw = new FileWriter("temp.txt")) {
 for (Filter fil : f) {
 if (fil.toString().startsWith("0") || fil.toString().contains("Ticker")) {
 continue;
 }
 fw.write(fil.toString());
 fw.write("\n");
 }
                   
 }
        
 */
