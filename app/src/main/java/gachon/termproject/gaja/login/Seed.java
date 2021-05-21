package gachon.termproject.gaja.login;

public class Seed {
    public static int MakeSeed(String s){
        int result = 0;
        for(int i=0; i<s.length(); i++){
            result += s.charAt(i);
            result = result << 1;
        }
        return result;
    }
}
