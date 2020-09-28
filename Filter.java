/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stocks;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author tyler
 */
public class Filter {

    public String n;
    public String fb;
    public ArrayList<String> values = new ArrayList<>();
    char preface = 'u';

    public Filter() {
        
    }
    
    public Filter(String f) throws FileNotFoundException {
        Scanner sc = new Scanner(f);
        this.fb = sc.next();
        this.n = sc.next();
        while (sc.hasNext()) {
            this.values.add(sc.next());
        }
        preface = n.contains("Price") ? 'u' : 'o';
    }

    public void init(String input) {
        String[] ff = parseInput(input);
        n = ff[0];
        fb = ff[1];
        preface = n.contains("Price") ? 'u' : 'o';
    }

    public String pickGood() {
        ArrayList<String> goodVals = new ArrayList<>();
        for (String s : this.values) {
            if (s.startsWith(this.preface + "")) {
                goodVals.add(s);
            }
        }
        int rand = (int) (Math.random() * goodVals.size());
        return goodVals.get(rand);
    }
    
    
    public String[] parseInput(String in) {
        String copy = in;
        String copy2 = in;
        int name = in.indexOf("header=[", in.indexOf("header=[") + 1);
        if (name < 0) {
            return new String[]{"0", "0"};
        }
        in = in.substring(name);
        String[] yee = in.split("header=");
        String[] boii = yee[1].split("body");
        int filter = copy.indexOf("data-filter=");
        if (filter < 0) {
            return new String[]{"0", "0"};
        }
        copy = copy.substring(filter);
        String[] eee = copy.split("\"");
        String[] done = {boii[0], eee[1]};
        String regex = "<option value";
        int len = regex.length();
        int index = copy2.indexOf(regex);
        //System.out.println(copy2);
        while (index >= 0) {
            copy2 = copy2.substring(index + len);
            String toAdd = copy2.substring(2, copy2.indexOf(">") - 1);
            if (!toAdd.contains("range") && !toAdd.isEmpty()) {
                values.add(toAdd);
            }
            index = copy2.indexOf(regex);
        }
        return done;
    }

    @Override
    public String toString() {
        String out = this.fb + " " + this.n;
        for (String t : this.values) {
            out += t + " ";
        }
        return out;
    }

}
