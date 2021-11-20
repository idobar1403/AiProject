import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class fileRead {
    public static void main(String[] args) {
        readFile();
    }
    public static void readFile()  {
        try {
            File myObj = new File("output.txt");
            if (myObj.createNewFile()) {
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        try {
            FileWriter myWriter = new FileWriter("output.txt");
            File myObj = new File("/Users/idobar/Bsc/Year 2/AIAlgoProject/AiProject/AiProj/src/input.txt");
            Scanner myReader = new Scanner(myObj);
            String data1 = myReader.nextLine();
            net bNet=new net();
            bNet= xmlRead.makeNet(data1);
            net rNet=new net();
            while (myReader.hasNextLine()) {
                rNet=xmlRead.makeNet(data1);
                String data = myReader.nextLine();
                if('P'==data.charAt(0)){
                    myWriter.write(varElimination.variableElimination(rNet,data));
                }
                else{
                    myWriter.write(bayesBall.bayesBallAns(rNet,data));
                }
                myWriter.write("\n");

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
