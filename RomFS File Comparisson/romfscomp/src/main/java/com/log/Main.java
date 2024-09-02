package com.log;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) {
      FileCompare manager = new FileCompare();
      //manager.compareVersions("D:\\Console Games\\RomFS\\Splatoon 3 7.2.0 FS", "D:\\Console Games\\RomFS\\Splatoon 3 9.0.0 FS");
    }
    
}

class FileCompare{
    private String name;
    private String version;
    private File filterList;
    private boolean listExists;

    public FileCompare(){
        Scanner s = new Scanner(System.in);
        System.out.print("What is the name of the game you are comparing file versions of?: ");
        name = s.nextLine();
        System.out.print("What is the current version of that game?: ");
        version = s.nextLine();
        System.out.print("Do you have an already existing filter list .txt file?(yes or no): ");
        String yon = s.nextLine();
        if(yon.equals("yes")){
            System.out.print("what is the directory of the file?(without the quotation marks on the end): ");
            filterList = new File(s.nextLine());
            listExists = true;
        }
        s.close();
    }
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
        ArrayList<String> newVerNames = new ArrayList<>();
        getFiles(newVerDir, newVerNames);
        if(!listExists){
            ArrayList<String> oldVerNames = new ArrayList<>();
            getFiles(oldVerDir, oldVerNames);
            for(int i = 0; i < oldVerNames.size(); i++){
                for(int j = 0; j < newVerNames.size(); j++){
                    if(oldVerNames.get(i).equals(newVerNames.get(j))){
                        newVerNames.remove(j);
                        j--;
                    }
                }
            }
        }
        else{
            Scanner reader = new Scanner(filterList); //Need to make the Scanner read thru an already existing filter list that is stored in the main file of the program. Use this link
            //https://search.brave.com/search?q=how+to+parse+through+a+txt+file+in+java
            while (reader.hasNextLine()){

            }
        }
        writeToFile(newVerNames);
    }
    private void writeToFile(ArrayList<String> fNames){
        try {
            File exists = new File(name + " " + version + " RomFS differences.txt");
            if(exists.createNewFile()){
                FileWriter updater = new FileWriter(name + " " + version + " RomFS differences.txt");
                for(String fName : fNames){
                    updater.write(fName + "\n");
                }
                updater.close();
            }
            else{
                new FileWriter(name + " " + version + " RomFS differences.txt", false).close();
                FileWriter updater = new FileWriter(name + " " + version + " RomFS differences.txt");
                for(String fName : fNames){
                    updater.write(fName + "\n");
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