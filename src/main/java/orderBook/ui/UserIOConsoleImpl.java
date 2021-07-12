package orderBook.ui;

import java.util.Scanner;

public class UserIOConsoleImpl implements UserIO {
    //Print a given String to the console. The String value displayed should be passed in as a parameter.
    @Override
    public void print(String message) {
        System.out.println(message);
    }
    // Display a given message String to prompt the user to enter in a String, then read in the user response as a String and return that value.
    // The prompt message should be passed in as a parameter and the String value read in should be the return value of the method.
    @Override
    public String readString(String prompt) {
        System.out.println(prompt);
        Scanner myScanner = new Scanner(System.in);
        String userString = myScanner.nextLine();
        System.out.println(userString);

        return userString;
    }

//    Display a given message String to prompt the user to enter in an integer, then read in the user response and return that integer value.
//      The prompt message value should be passed in as a parameter and the value that is read in should be the return of the method.

    @Override
    public int readInt(String prompt) {
        System.out.println(prompt);
        Scanner myScanner = new Scanner(System.in);
        int userInteger = Integer.parseInt(myScanner.nextLine());
        System.out.println(userInteger);
        return userInteger;
    }
//     Display a prompt to the user to enter an integer between a specified min and max range, and read in an integer.
//     If the user's number does not fall within the range, keep prompting the user for new input until it does.
//     The prompt message and the min and max values should be passed in as parameters.
//     The value read in from the console should be the return of the method.

    @Override
    public int readInt(String prompt, int min, int max) {
        boolean validInput = false;
        int userInteger = 0;
        while (!validInput) {
            System.out.println(prompt + " min " + min + " max " + max);
            Scanner myScanner = new Scanner(System.in);
            userInteger = Integer.parseInt(myScanner.nextLine());
            if (max >= userInteger && userInteger >= min) {
                validInput = true;
            }
        }
        return userInteger;
    }
    //Display a given message String to prompt the user to enter in a double, then read in the user response and return
    // that double value. The prompt message value should be passed in as a parameter and the value that is read in should be the return of the method.


    @Override
    public double readDouble(String prompt) {
        System.out.println(prompt);
        Scanner myScanner = new Scanner(System.in);
        double userDouble = Double.parseDouble(myScanner.nextLine());

        return userDouble;
    }
    //Display a prompt to the user to enter a double between a specified min and max range, and read in a double.
    // If the user's number does not fall within the range, keep prompting the user for new input until it does.
    // The prompt message and min and max values should be passed in as parameters. The value read in from the console should be the return of the method.
    @Override
    public double readDouble(String prompt, double min, double max) {
        boolean validInput = false;
        double userDouble = 0;
        while (!validInput) {
            System.out.println(prompt + " min " + min + " max " + max);
            Scanner myScanner = new Scanner(System.in);
            userDouble = Double.parseDouble(myScanner.nextLine());
            if (max >= userDouble && userDouble >= min) {
                validInput = true;
            }
        }

        return userDouble;

    }
    //Display a given message String to prompt the user to enter a float and then read in the user response and return
    // that float value. The prompt message value should be passed in as a parameter and the value that is read in should
    // be the return of the method.


    @Override
    public float readFloat(String prompt) {
        System.out.println(prompt);
        Scanner myScanner = new Scanner(System.in);
        float userFloat = Float.parseFloat(myScanner.nextLine());

        return userFloat;
    }
    //Display a prompt to the user to enter a float between a specified min and max range, and read in a float.
    // If the user's number does not fall within the range, keep prompting the user for new input until it does.
    // The prompt message and min and max values should be passed in as parameters.
    // The value read in from the console should be the return of the method.
    @Override
    public float readFloat(String prompt, float min, float max) {
        boolean validInput = false;
        float userFloat = 0;
        while (!validInput) {
            System.out.println(prompt + " min " + min + " max " + max);
            Scanner myScanner = new Scanner(System.in);
            userFloat = Float.parseFloat(myScanner.nextLine());
            if (max >= userFloat && userFloat >= min) {
                validInput = true;
            }
        }
        return userFloat;

    }
    //Display a given message String to prompt the user to enter in a long,
    // then read in the user response and return that long value. The prompt message value should be passed
    // in as a parameter and the value that is read in should be the return of the method.


    @Override
    public long readLong(String prompt) {
        System.out.println(prompt);
        Scanner myScanner = new Scanner(System.in);
        long userLong = Long.parseLong(myScanner.nextLine());

        return userLong;

    }
    //Display a prompt to the user to enter a long between a specified min and max range, and read in a long.
    // If the user's number does not fall within the range, keep prompting the user for new input until it does.
    // The prompt message and min and max values should be passed in as parameters. The value read in from the console
    // should be the return of the method.
    @Override
    public long readLong(String prompt, long min, long max) {
        boolean validInput = false;
        long userLong = 0;
        while (!validInput) {
            System.out.println(prompt + " min " + min + " max " + max);
            Scanner myScanner = new Scanner(System.in);
            userLong = Long.parseLong(myScanner.nextLine());
            if (max >= userLong && userLong >= min) {
                validInput = true;
            }
        }
        return userLong;
    }
}
