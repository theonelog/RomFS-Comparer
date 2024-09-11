package com.log;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        FileCompare manager = new FileCompare();
        Scanner ms = new Scanner(System.in);
        System.out.print("Do you have an  existing files .txt file?(yes or no): ");
        String yon = ms.nextLine();
        if(yon.toLowerCase().equals("yes")){
            System.out.print("What is the path of said file?: ");
            manager.updateListPath(ms.nextLine());
            System.out.println("What is the directory of the new game version RomFS?(w/o quotations): ");
            manager.compareVersions(ms.nextLine());
        }
        else{
            System.out.println("What is the directory of the older game version?: ");
            String oldVer = ms.nextLine();
            System.out.println("What is the directory of the new game version?: ");
            manager.compareVersions(oldVer, ms.nextLine());
            //an option to just hardcode it for personal use | manager.compareVersions("D:\\Console Games\\RomFS\\Splatoon 3 8.0.0 FS", "D:\\Console Games\\RomFS\\Splatoon 3 9.0.0 FS");
        }
        ms.close();
    }
    
}

class FileCompare{
    private String name;
    private String version;
    private String listPath;

    public FileCompare(){
        Scanner s = new Scanner(System.in);
        System.out.print("What is the name of the game you are comparing file versions of?: ");
        name = s.nextLine();
        System.out.print("What is the current version of that game?: ");
        version = s.nextLine();
        s.close();
    }

    public void updateListPath(String s){
        listPath = s;
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
        ArrayList<String> oldVerNames = new ArrayList<>();
        getFiles(newVerDir, newVerNames);
        writeToFile(newVerNames, false);
        getFiles(oldVerDir, oldVerNames);
        for(int i = 0; i < oldVerNames.size(); i++){
            for(int j = 0; j < newVerNames.size(); j++){
                if(oldVerNames.get(i).equals(newVerNames.get(j))){
                    newVerNames.remove(j);
                    j--;
                }
            }
        }
        writeToFile(newVerNames, true);
    }

    public void compareVersions(String newVerDir){
        ArrayList<String> newVerNames = new ArrayList<>();
        ArrayList<String> oldVerNames = new ArrayList();
        getFiles(newVerDir, newVerNames);
        writeToFile(newVerNames, false);
        try{ 
            BufferedReader reader = new BufferedReader(new FileReader(listPath));
            String line = reader.readLine();
            while (line != null){
                oldVerNames.add(line);
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e){
            e.printStackTrace();
        }
        for(int i = 0; i < oldVerNames.size(); i++){
            for(int j = 0; j < newVerNames.size(); j++){
                if(oldVerNames.get(i).equals(newVerNames.get(j))){
                    newVerNames.remove(j);
                    j--;
                }
            }
        }
        writeToFile(newVerNames, true);
    }
    
    private void writeToFile(ArrayList<String> fNames, Boolean isDiff){
        if(isDiff){
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
        else{
            try{
                File exists = new File(listPath);
                if(exists.createNewFile()){
                    FileWriter updater = new FileWriter(listPath);
                    for(String fName : fNames){
                        updater.write(fName + "\n");
                    }
                    updater.close();
                }
                else{
                    new FileWriter(name + " Existing Files.txt", false).close();
                    FileWriter updater = new FileWriter(name + " Existing Files.txt");
                    for(String fName : fNames){
                        updater.write(fName + "\n");
                    }
                    updater.close();
                }
            } catch (IOException e){
                System.err.println("Something really broke huh?");
                e.printStackTrace();
            }
        }
    }
}