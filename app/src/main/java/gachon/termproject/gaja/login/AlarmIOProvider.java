package gachon.termproject.gaja.login;

import android.os.Environment;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AlarmIOProvider {

    FileIO io = new FileIO();
    File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/GAJA", "alarm.dat");
    File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/GAJA");

    public void WriteAlarm(String s){
        String input = s + "\n";
        if (!dir.exists()) {
            dir.mkdir();
        }
        io.FileWriter(file, input.getBytes());
    }

    public void AppendAlarm(String s){
        String input = s + "\n";
        if (!dir.exists()) {
            dir.mkdir();
        }
        io.FileAppender(file, input);
    }

    public String[] ReadAlarm(){
        if(file.exists()) {
            byte[] temp = io.FileReader(file);
            String temp2 = new String(temp);
            String[] result = temp2.split("\n");
            List<String> list = Arrays.asList(result);
            Collections.reverse(list);
            result = list.toArray(new String[list.size()]);
            return result;
        } else{
            String[] error = new String[]{"알림이 없어요!"};
            return error;
        }
    }
}
