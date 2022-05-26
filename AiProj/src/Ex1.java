import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Ex1 {
    public static void main(String[] args) {
        readFile();
    }
    public static void readFile()  {
        try {
            // create the output file
            File myObj = new File("output.txt");
            if (!myObj.createNewFile()) {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        try {
            //define writer in the output.txt file
            FileWriter myWriter = new FileWriter("output.txt");
            //read from the file
            File myObj = new File("../data/input.txt");
            Scanner myReader = new Scanner(myObj);
            String data1 = myReader.nextLine();
            //create empty net
            net bNet;
            while (myReader.hasNextLine()) {
                //every run create net from the xml file
                bNet=xmlRead.makeNet("../data/"+data1);
                String data = myReader.nextLine();

                    if ('P' == data.charAt(0)) {
                        try {
                            // go to variable elimination
                            myWriter.write(varElimination.variableElimination(bNet, data));
                        } catch (Exception e) {
                            myWriter.write("fail");
                        }
                    }
                else{
                    // go to bayesBall
                    myWriter.write(bayesBall.bayesBallAns(bNet,data));
                }
                if (myReader.hasNextLine()) {
                    myWriter.write("\n");
                }
            }
            myWriter.close();
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
