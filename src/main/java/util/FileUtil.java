package util;

import domain.Music;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {
   private static List<Music> list=new ArrayList<Music>() ;

    public static List<Music> FileLIst(File path){



        if(path.exists()){
            File[] files = path.listFiles();
            if(files!=null){
                for(int i=0;i<files.length;i++){
                    FileLIst(files[i]);
                }
            }else {

                String str = path.toString();

                String uri = path.toURI().toString();
                String substring = str.substring(str.indexOf(".")+1, str.length());
                if(substring.equals("mp3")){
                    String singer = str.substring(str.indexOf("-")+1, str.length()-4);
                    String name = str.substring(str.lastIndexOf("\\")+1,str.length());
                    Music music=new Music();
                    music.setName(name);
                    music.setSinger(singer);
                    music.setPath(uri);
                    list.add(music);

                }
            }

        }

        return list;

    }

    public static void main(String[] args) {

        List<Music> list = new FileUtil().FileLIst(new File("G:/CloudMusic"));
        System.out.println(list);
    }
}
