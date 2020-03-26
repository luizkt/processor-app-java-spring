package br.com.fiap.utils;

public class NameFormatter {
    public static String capitalizeName(String str){
        String words[]=str.split("\\s");
        String capitalizeName="";
        for(String w:words){
            String first=w.substring(0,1);
            String afterfirst=w.substring(1);
            capitalizeName+=first.toUpperCase()+afterfirst+" ";
        }
        return capitalizeName.trim();
    }
}