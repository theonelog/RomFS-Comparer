package com.log;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;


public class Main {
    public static void main(String[] args) {
      FileCompare manager = new FileCompare();
      manager.compareVersions("D:\\Console Games\\RomFS\\Splatoon 3 7.2.0 FS", "D:\\Console Games\\RomFS\\Splatoon 3 9.0.0 FS");
    }
    
}

class FileCompare{

    private void getFiles(String path, ArrayList<String> nameArrayList){
        File folder = new File(path);
        File[] files = folder.listFiles();
        for(File file : files){
            if(file.isFile()){
                nameArrayList.add(file.getName());
            }
            else if (file.isDirectory()){
                getFiles(file.getAbsolutePath(), nameArrayList);
            }
        } 
    }
    public void compareVersions(String oldVerDir, String newVerDir){
        ArrayList<String> oldVerNames = new ArrayList<>();
        getFiles(oldVerDir, oldVerNames);
        ArrayList<String> newVerNames = new ArrayList<>();
        getFiles(newVerDir, newVerNames);
        for(int i = 0; i < oldVerNames.size(); i++){
            for(int j = 0; j < newVerNames.size(); j++){
                if(oldVerNames.get(i).equals(newVerNames.get(j))){
                    newVerNames.remove(j);
                    j--;
                }
            }
        }
        writeToFile(newVerNames);
    }
    private void writeToFile(ArrayList<String> fNames){
        try {
            File exists = new File("list.txt");
            if(exists.createNewFile()){
                FileWriter updater = new FileWriter("list.txt");
                for(String fName : fNames){
                    updater.write(fName);
                    updater.write("\n");
                }
                updater.close();
            }
            else{
                new FileWriter("list.txt", false).close();
                FileWriter updater = new FileWriter("list.txt");
                for(String fName : fNames){
                    updater.write(fName);
                    updater.write("\n");
                }
                updater.close();
            }
            System.out.println("Successfully Updated Difference List!");
        } catch (IOException e) {
            System.err.println("Something bad happened");
            e.printStackTrace();
        }
    }
}