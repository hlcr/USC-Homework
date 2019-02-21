
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class generate_report {
    static void get_size_category(HashMap<String,Integer> scm, int size, int num){
        if((size-1) / 1024 < 1){
            scm.put("1kb",scm.get("1kb") + num);
        }
        else if((size-1) / 1024 < 10){
            scm.put("10kb",scm.get("10kb") + num);
        }else if((size-1) / 1024 < 100){
            scm.put("100kb",scm.get("100kb") + num);
        }else if((size-1) / 1024 / 1024 < 1){
            scm.put("1mb",scm.get("1mb") + num);
        }else{
            scm.put(">1mb",scm.get(">1mb") + num);
        }
    }

    private static void readFile1(File fin) throws IOException {
        FileInputStream fis = new FileInputStream(fin);

        //Construct BufferedReader from InputStreamReader
        BufferedReader br = new BufferedReader(new InputStreamReader(fis));
        ArrayList<HashMap> result_list = new ArrayList<HashMap>();
        String line = null;;
        line = br.readLine();
        String[] item_list = line.split(",");
        for (int i=0; i < item_list.length; i++){
            result_list.add(new HashMap());
        }

        HashMap result_map;
        while ((line = br.readLine()) != null) {
            item_list = line.split(",");

            for(int i=0; i < item_list.length; i++){
                result_map = result_list.get(i);
                if(result_map.containsKey(item_list[i])){
                    int v = (Integer)result_map.get(item_list[i]) + 1;
                    result_map.put(item_list[i],v);

                }else{
                    result_map.put(item_list[i],1);
                }
            }

        }

        br.close();

        List<HashMap> result_list1 = result_list.subList(1,result_list.size());
        for(HashMap result:result_list1){
            System.out.print("\n" + result + "\n");
        }

//        HashMap<String, Integer> size_map = new HashMap<String, Integer>();
//        size_map.put("1kb",0);
//        size_map.put("10kb",0);
//        size_map.put("100kb",0);
//        size_map.put("1mb",0);
//        size_map.put(">1mb",0);
//
//
//        for (Object key : result_list1.get(0).keySet()) {
//            int size = (Integer)result_list1.get(0).get(key);
//            get_size_category(size_map,Integer.valueOf((String)key),size);
//        }
//
//        System.out.print(size_map);

    }
        public static void main(String[] arg) throws IOException {

        readFile1(new File("D:/url_DailyMail1.csv"));


    }

}
