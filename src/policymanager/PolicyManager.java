// Gadget Insurance Policy Prototype
// Author: Christopher Burrell
// Student Number: T7145969

package policymanager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Scanner;
import java.io.*;

public class PolicyManager
{
    static Scanner keyboard = new Scanner(System.in);
    
    // Main method, calls main menu
    public static void main(String[] args) 
    {
        mainMenu();
    }
    
    // Main menu, runs until user enters 0
    public static void mainMenu()
    {
        String prompt = ("1. Enter new policy\n"
                       + "2. Display summary of policies\n"
                       + "3. Display summaries for specific month\n"
                       + "4. Find and display policy\n"
                       + "0. Exit\n");
        while (true) // runs until user enters 0
        {
            int menuChoice = catchStringException(prompt); // Gets menu choice and catches string exception
            switch (menuChoice)
            {
                case 0:
                    System.exit(0);
                case 1: 
                    newPolicy();
                    break;
                case 2: 
                    policySummary("");
                    break;
                case 3: 
                    policySummary(getMonth());
                    break;
                case 4: 
                    findPolicy();
                    break;
                default: 
                    System.out.println("\nInvalid input.\n");
                    break;
            } // end of switch
        } // end of while
    }
    
    // Method to obtain new policy details and output policy summary (option 1 from main menu)
    public static void newPolicy()
    {      
        //initialise variables
        int gadgetLimit = 0;
        int discount = 0;
        int premiumAmount = -1;
        int itemLimit = 5;
        int mostExpensive = 0;
        int expenseLimit = 100000;
        int excess = 30;
        String frequency = "Monthly";

        //get policy values from user
        String customerName = inputName();
        String policyReference = inputPolicyReference();
        int numberItems = inputNumberOfItems();
                
        if (!isRejected(numberItems,itemLimit,mostExpensive,expenseLimit))  // skips further input if policy has been rejected based on number of items or most expensive item
        {
            mostExpensive = inputMostExpensive();
        }
        
        if (!isRejected(numberItems,itemLimit,mostExpensive,expenseLimit)) // skips further input if policy has been rejected based on number of items or most expensive item
        {
            excess = inputExcess();
            frequency = inputFrequency();
            
            // Perform policy value calculations
            gadgetLimit  = calcGadgetLimit(mostExpensive);
            premiumAmount = calcPremium(gadgetLimit,numberItems,frequency);
            discount = calcDiscount(frequency,excess);
            premiumAmount -= (premiumAmount * discount / 100);
        }
        
        Policy myPolicy = new Policy(customerName,policyReference,numberItems,mostExpensive,excess,frequency,premiumAmount);        
        
        // Output and log policy
        
        outputPolicy(getDate(), myPolicy.getCustomerName(), myPolicy.getPolicyRef(), myPolicy.getPolicyTerms(), myPolicy.getNumberOfItems(), 
                        gadgetLimit, myPolicy.getPolicyAmount(), myPolicy.getExcess(), isRejected(numberItems,itemLimit,mostExpensive,expenseLimit));
        
        logPolicy(myPolicy.getCustomerName(), myPolicy.getPolicyRef(), myPolicy.getPolicyTerms(), myPolicy.getNumberOfItems(), 
                        myPolicy.getMostExpensiveItem(), myPolicy.getPolicyAmount(), myPolicy.getExcess());
    }
    
    // Method to obtain customer name
    public static String inputName()
    {
        keyboard.nextLine();
        System.out.println("\nPlease enter customer name: ");
        String name = (keyboard.nextLine().trim());
        while(!isNameValid(name))
        {
            System.out.println("\nCustomer name must be between 1 and 20 characters.  Please enter customer name: ");
            name = (keyboard.nextLine().trim());
        }
        return name;
    }
    
    // returns true if customer name is between 1 and 20 characters
    public static boolean isNameValid(String custName)
    {
        return (custName.length() <= 20 && custName.length() >= 1);
    }
    
    // Method to obtain policy reference
    public static String inputPolicyReference()
    {
        System.out.println("\nPlease enter the policy reference: ");
        String reference = keyboard.nextLine().toUpperCase().trim();
        while(!isRefValid(reference)) 
        {
            System.out.println("\nInvalid policy number.  Please enter a six digit reference.  "
                    + "This must be two letters followed by three numbers followed by a letter.");
            reference = keyboard.nextLine().toUpperCase().trim();
        }
        return reference;
    }
    
    // returns true if policy reference follows format AB123C
    public static boolean isRefValid(String policyRef)
    {
        return policyRef.length() == 6 &&
               Character.isLetter(policyRef.charAt(0)) &
               Character.isLetter(policyRef.charAt(1)) &&
               Character.isDigit(policyRef.charAt(2)) &&
               Character.isDigit(policyRef.charAt(3)) &&
               Character.isDigit(policyRef.charAt(4)) &&
               Character.isLetter(policyRef.charAt(5));
    }
        
    // Method to cacth string exceptions on  positive integer input
    public static int catchStringException(String inputRequest)
    {
        System.out.println(inputRequest);
        int integer = -1;
        try
        {
            integer = keyboard.nextInt();
            return integer;
        }
        catch (Exception e)
        {
            keyboard.nextLine();  //moves scanner to next line ready for next input
        }
        return integer;
    }
    
    // Method to obtain number of items to be insured
    public static int inputNumberOfItems()
    {
        String prompt = "\nPlease enter the number of items: ";
        int numItems = catchStringException(prompt);
        while (!isNumItemsValid(numItems))
        {
            System.out.println("\nInvalid input. Number of items must be a number of 1 or more.");
            numItems = catchStringException(prompt);
        }
        return numItems;
    }
    
    // returns true value if policy is rejected on either criteria, number of items or most expensive item
    public static boolean isRejected(int numberOfItems, int rejectionLimit, int mostExpensiveItem, int valueLimit)
    {
        return numberOfItems > rejectionLimit || mostExpensiveItem > valueLimit;   
    }
    
    // returns true if number of items is greater than 0
    public static boolean isNumItemsValid(int numberOfItems)
    {
        return numberOfItems > 0;
    }
   
    // Method to obtain value of most expensive item
    public static int inputMostExpensive()
    {
        double value = -1;
        System.out.println("\nPlease enter the value of the most expensive item: ");
        while (!isMostExpensiveValid(value))
        {
            try
            {
                value = keyboard.nextDouble();
            }
            catch (Exception e)
            {
                keyboard.nextLine();  //moves scanner to next line to read next input
            }
            if (!isMostExpensiveValid(value))
            {
                System.out.println("\nInvalid input.  Most expensive item must be a number greater than 0. Please enter most expensive item: ");
            }
        }
        return (int)(value * 100); // converts to int and stores value in pence 
    }
    
    
    // returns true if most expensive item is over zero
    public static boolean isMostExpensiveValid(double mostExpensiveDouble)
    {
        return mostExpensiveDouble > 0;
    }

    
    // Method to obtain policy excess
    public static int inputExcess()
    {
        String prompt = ("\nPlease select excess:\n"
                         + "£30 (no discount)\n"
                         + "£40 (5% discount)\n"
                         + "£50 (10% discount)\n"
                         + "£60 (15% discount)\n"
                         + "£70 (20% discount)\n");
        int excessChoice = catchStringException(prompt);
        while (!isExcessValid(excessChoice))
        {
            System.out.println("\nInvalid input.  Please enter a number from the choices below.");
            excessChoice = catchStringException(prompt);
        }
        return excessChoice;
    }

    // returns true if excess is 30, 40, 50, 60 or 70 (logic is: true if between 30 and 70 (inclusive) and divides by 10 with no remainder)
    public static boolean isExcessValid(int excessChosen)
    {
        return excessChosen >= 30 && excessChosen <= 70 && excessChosen % 10 == 0;
    }
    
    // Method to obtain and validate payment frequency
    public static String inputFrequency()
    {
        String [] terms = {"Monthly", "Annual"};
        String prompt = ("\nPlease select payment method:\n"
                         + "1. Monthly (no discount)\n"
                         + "2. Annual (10% discount)\n");
        int menuChoice = catchStringException(prompt);
        while (!isTermsValid(menuChoice))
        {
            System.out.println("\nInvalid input.  Please enter a number from the menu below.\n");
            menuChoice = catchStringException(prompt);
        }
        return terms[menuChoice-1];
    }
    
    // returns true terms menu choice is 1 or 2
    public static boolean isTermsValid(int termsChosen)
    {
        return termsChosen >= 1 && termsChosen <= 2;
    }    
    
    // Method to return the gadget value limit, in pounds, based on the most expensive item
    public static int calcGadgetLimit(int mostExpensive)
    {
        if (mostExpensive <= 50000)
        {
            return 500;
        }
        else if (mostExpensive <= 80000)
        {
            return 800;
        }
        else
        {
            return 1000;
        }
    }
    
    // Method to calculate premium, based on number of items and gadget value limit
    public static int calcPremium(int gadgLim, int numItems, String terms)
    {
        int [][] premiums = 
        {
            {599,1099,1099,1599,1599},    // £ 500 most valuable item, monthly premiums for 1, 2, 3, 4 and 5 items respectively
            {715,1335,1335,1960,1960},    // £ 800 most valuable item, monthly premiums for 1, 2, 3, 4 and 5 items respectively
            {830,1555,1555,2282,2282}     // £1000 most valuable item, monthly premiums for 1, 2, 3, 4 and 5 items respectively
        };
        int premAmount;
        switch (gadgLim)
        {
            case 500: // sets premiumAmount for gadget limit = £500
                premAmount = premiums[0][numItems - 1];
                break;
            case 800: // sets premiumAmount for gadget limit = £800
                premAmount = premiums[1][numItems - 1];
                break;
            default: // sets premiumAmount for gadget limit = £1000
                premAmount = premiums[2][numItems - 1];
                break;
        }
        if (terms.equals("Annual"))
        {
            premAmount *= 12;
        }
        return premAmount;
    }
    
    // Method to calculate both discounts and add them together
    public static int calcDiscount(String terms, int polExcess)
    {
        int discount = 0;
        if (terms.equals("Annual"))  // checks if payment frequency is annual.  If so adds 10% to discount
        {
            discount += 10;
        }
        return discount + ((polExcess - 30) / 2);  // adds 5% discount for every £10 excess, after the first £30
    }
    
    // Method to return todays date in specific format
    public static String getDate()
    {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        return sdf.format(cal.getTime());
    }
    
    // Method to convert the number of items integer to a word if there are less than six items
    public static String numItemsString(int numberOfItems)
    {
        //list storing numbers as words.  If expanding always put each increasing number in order, without skipping any numbers (affects logic in this method)
        String [] numberAsWord = {"Zero","One","Two","Three","Four","Five"};  
        if (numberOfItems > numberAsWord.length - 1)  //checks if number of items is greater than the final number in the numberAsWord array
        {
            return Integer.toString(numberOfItems);
        }
        else  // converts numberOfItems into a word from the numberAsWord list
        {
            return numberAsWord[numberOfItems];
        }
    }

    // Method to display policy summary
    public static void outputPolicy(String date, String custNam, String polRef, String polTerms, int numberItems, int maxGadget, int premiumAmount, 
                                    int excessAmount, boolean rejected)
    {
        System.out.println("\n+==========================================+");
        System.out.println("|                                          |");
        System.out.printf("|  Client: %-32s|\n", custNam);
        System.out.println("|                                          |");
        System.out.printf("|    Date: %-19s Ref: %-7s|\n", date, polRef);
        System.out.printf("|   Terms: %-17s Items: %-7s|\n", polTerms, numItemsString(numberItems));
        System.out.printf("|  Excess: £%s.00                          |\n", excessAmount);
        System.out.println("|                                          |");
        rejectionOrPolicyAmount(polTerms, premiumAmount, maxGadget, rejected, numberItems);   //method to output either rejection message or accepted policy values
        System.out.println("|                                          |");
        System.out.println("+==========================================+");
        System.out.println("\n");
    }
    

    // Determines whether policy amount and gadget limit is displayed or, instead, a rejection message.  Called from outputPolicy method
    public static void rejectionOrPolicyAmount(String terms, int premiumAm, int gadgMax, boolean isRejected, int itemNum)
    {
        if (isRejected)
        {
            System.out.println("|         Policy has been rejected         |");
            if (itemNum > 5) // if true, policy is rejected based on number of items
            {
                System.out.println("|       due to numer of items (max 5)      |");
            }
            else // else rejected based on most expensive item
            {
                System.out.println("|  due to most expensive item (max £1000)  |");                
            }
        }
        else // if not rejected, prints premium terms and gadget limit
        {
            System.out.printf("| %7s                Limit per         |\n", terms);
            System.out.printf("| Premium: £%6.2f          Gadget: £%-6d|\n", (double)premiumAm/100, gadgMax);
        }
    }
    
    // Method to log policy data in policy.txt file
    public static void logPolicy(String custNam, String polRef, String polTerms, int numberItems, int expensiveItem, int premiumAmount, 
                                 int excessAmount)
    {
        PrintWriter policyLogger = null;
        FileWriter writer;
        File fileToAppend = new File("policy.txt");
        try
        {
            writer = new FileWriter(fileToAppend, true);
            policyLogger = new PrintWriter(writer);
        }
        catch (FileNotFoundException e)
        {
            System.out.println("Error logging policy");
        }
        catch (IOException ex)
        {
            System.out.println("Error logging policy");            
        }
        if (premiumAmount == -1)  // Sets polTerms to R if rejected as default terms up to this point are kept as monthly for rejection summary
        {
            polTerms = "R";
        }
        policyLogger.println("\n" + getDate() + "\t" + polRef + "\t" + numberItems + "\t" + expensiveItem/100 + "\t" + excessAmount + 
                             "\t" + premiumAmount + "\t" + polTerms.substring(0,1) + "\t" + custNam);
        policyLogger.close();
    }
    
    // Method to display full summary of policies
    public static void policySummary(String month)
    {
        //initialise variables
        int numItems;
        int totalNumItems = 0;
        int premium;
        int totalMonthlyPremium = 0;
        String annualOrMonthly;
        int acceptedCount = 0;
        int totalCount = 0;
        int monthTotals [] = new int [12];
        int monthInd;
        String monthOnLine;
        
        // get file choice from user
        String chosenFile = chooseFile();
        File fileToOpen = new File(chosenFile);
        

        //String data;     REMOVE?

        try
        {
            Scanner fileData = new Scanner(fileToOpen);
            while (fileData.hasNext()) // checks if there is more text in the file
            {
                monthOnLine = fileData.next().substring(3, 6);  // gets month on current line
                fileData.next();
                numItems = fileData.nextInt();
                fileData.next();
                fileData.next();
                premium = fileData.nextInt();
                annualOrMonthly = fileData.next();
                fileData.nextLine();
                if (month.equals("") || month.equals(monthOnLine))  // checks if annual output has been selected or if month on current line matches month user has input
                {
                    // increment count and total variables where current line is relevant to requested summary
                    monthInd = getMonthIndex(monthOnLine);
                    monthTotals[monthInd] ++;
                    if (!annualOrMonthly.equals("R"))
                    {
                        if (annualOrMonthly.equals("A"))
                        {
                            premium /=12;
                        }
                        totalMonthlyPremium += premium;
                        totalNumItems += numItems;
                        acceptedCount += 1;                           
                    }
                    totalCount++;
                }
            }
            summaryOfPolicies(totalNumItems,acceptedCount,totalMonthlyPremium,totalCount,month,monthTotals);
            System.out.println("\n");
            fileData.close();                    
        }                
        catch (FileNotFoundException e)
        {
            System.out.println("File doesn't exist");
        }
    }
    
    // Method outputs summary of policies (incorporates both annual and monthly summaries (options 2 and 3 from main menu))
    public static void summaryOfPolicies (int totalNum, int countAccepted, int totalMonthlyPrem, int countTotal, String theMonth, int [] monthlyTotals)
    {
        double averageNum = 0;
        double averagePrem = 0;
        
        if (countAccepted != 0)  // calculate averages if count is not zero
        {
            averageNum = totalNum / (double)countAccepted;
            averagePrem = totalMonthlyPrem / (double)countAccepted;
        }
        if (theMonth.equals("")) // output annual count if no month selected
        {
            System.out.println("\nTotal number of policies: " + countTotal);
        }
        else // else output monthly count for specified month
        {
            System.out.println("\nTotal number of policies: " + monthlyTotals[getMonthIndex(theMonth)]);
        }
        System.out.printf("Average number of items (Accepted policies): %.0f", averageNum); //rounded to nearest whole number
        System.out.printf("\nAverage monthly premium: £%.2f\n", averagePrem / 100);
        
        if (theMonth.equals("")) // output monthly breakdown where annual summary was chosen
        {
            System.out.println("Number of Policies per Month (inc. non-accepted):\n");
            System.out.println("Jan     Feb     Mar     Apr     May     Jun     Jul     Aug     Sep     Oct     Nov     Dec");
            for (int count = 0; count < 12; count++)
            {
                System.out.print(monthlyTotals[count]+ "\t");
            }
        }
    }
    
    //Method to convert three character string month to month index (eg Jan = 0, Feb = 1 etc)
    public static int getMonthIndex (String month)
    {
        int monthIndex = 0;
        String months [] = {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
        switch(month)
        {
            case("Jan"):
                monthIndex = 0;
                break;
            case("Feb"):
                monthIndex = 1;
                break;
            case("Mar"):
                monthIndex = 2;
                break;
            case("Apr"):
                monthIndex = 3;
                break;
            case("May"):
                monthIndex = 4;
                break;
            case("Jun"):
                monthIndex = 5;
                break;
            case("Jul"):
                monthIndex = 6;
                break;
            case("Aug"):
                monthIndex = 7;
                break;
            case("Sep"):
                monthIndex = 8;
                break;
            case("Oct"):
                monthIndex = 9;
                break;
            case("Nov"):
                monthIndex = 10;
                break;
            case("Dec"):
                monthIndex = 11;
                break;
        }
        return monthIndex;
    }

    // Method to obtain month from user and return the first three characters of that month as string (to compare to text file)
    public static String getMonth()
    {
        String [] months = {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
        String prompt = ("Please select month:\n"
                       + " 1. January\n"
                       + " 2. February\n"
                       + " 3. March\n"        
                       + " 4. April\n"
                       + " 5. May\n"
                       + " 6. June\n"                
                       + " 7. July\n"                
                       + " 8. August\n"                
                       + " 9. September\n"                
                       + "10. October\n"
                       + "11. November\n"
                       + "12. December\n");
        int menuChoice = catchStringException(prompt);        
        while (menuChoice < 1 || menuChoice > 12)
        {
            menuChoice = catchStringException(prompt);
        }
        return months[menuChoice - 1];
    }
    
    // Method to allow user to search for a policy
    public static void findPolicy()
    {
        // initialise variables
        
        int numItems;
        int premium;
        String date;
        String reference;
        int mostExp;
        int excess;
        String searchText;
        String name;
        int policyCount = 0;
        
        // get file choice from user
        String chosenFile = chooseFile();
        File fileToOpen = new File(chosenFile);
        
        try
        {
            Scanner fileData = new Scanner(fileToOpen);
            System.out.println("\nPlease enter search criteria (leave blank to display all records): \n");
            keyboard.nextLine();  //moves scanner to next line ready for next input
            searchText = keyboard.nextLine().toLowerCase();
            while (fileData.hasNext()) // checks if there is more text in the file
            {
                // read current line of text file into variables
                date = fileData.next();
                reference = fileData.next();
                numItems = fileData.nextInt();
                mostExp = fileData.nextInt();
                excess = fileData.nextInt();
                premium = fileData.nextInt();
                String terms = fileData.next();
                name = fileData.nextLine().trim();
                
                // if reference or name contain search criteria, output policy
                if (reference.toLowerCase().contains(searchText) || name.toLowerCase().contains(searchText))
                {
                    boolean rejected = false;
                    String policyTerms = "Monthly";
                    if (terms.equals("A"))
                    {
                        policyTerms = "Annual";
                    }
                    else if (terms.equals("R"))
                    {
                        rejected = true;
                    }
                    outputPolicy(date, name, reference, policyTerms, numItems, calcGadgetLimit(mostExp), premium, excess, rejected);
                    policyCount++;
                }
            }
            
            // after all matching policies are output, inform the user how many were found, including when none were found
            if (policyCount > 0)
            {
                System.out.println(policyCount + " policies found and displayed above\n");
            }
            else
            {
                System.out.println("\nNo policies found\n");
            }
            fileData.close();                    
        }                
        catch (FileNotFoundException e)
        {
            System.out.println("File doesn't exist");
        }
    }

    // get file choice from user
    public static String chooseFile()
    {
        String prompt = ("\nPlease select file:\n"
                         + "1. Archive\n"
                         + "2. Policy\n");
        String validFileNames [] = {"archive.txt","policy.txt"};
        int menuChoice = catchStringException(prompt);
        while (menuChoice < 1 || menuChoice > 2)
        {
            System.out.println("\nInvalid input. Please choose a number from the menu below.");
            menuChoice = catchStringException(prompt);
        }
        return validFileNames[menuChoice - 1];
    }
}