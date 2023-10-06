// Aaron Pelto
// CST-183
// Fall 2023


/*
Program Details

Input consists of a file of raw loan data (named loandata.txt) in the following format:
        {name} {principal} {term} {annualRate} {creditRating} {fee-optional}

        Example data could be:
        SMITH 20000 5 4.5 750
        JOHNSON 18000 4 4.2 560 2.0


        The principal amount is the loan request in dollars. The term is the duration of the loan in years.
        Note that some loan applicants with a low credit score (under 580) will be required to pay an up-front fee.
        In the example above, it implies that the customer would need to pay 2.0% of the $18,000 requested principal for
        the added risk to the bank for taking on the risky loan.
        For any loan applications without this field, simply list the fee as $0.00.


A typical credit score ranges from 300 to 850. A bank will rate a loan applicant based on this number. From
the raw credit score in the data file, enter the credit rating description (i.e. Fair, Good, etc.) in your output
report.

Credit Score Range Credit Rating
300-579 Very Poor
580-669 Fair
670-739 Good
740-799 Very Good
800-850 Exceptional


Your output should appear as an organized, formal financial report summarizing each order in detail. You
are free to write your output directly to the Java file console or you may choose to write the report to an
external file. This will assist in maintaining the text formatting.


An example format could be:
Customer Principal Rate Years Payment Payoff Fee Credit Rating
xxxxxxxxx $xxxxx.xx x.x% xx $xxxx.xx $xxxxx.xx $xxx.xx xxxxxxxxx
xxxxxxxxx $xxxxx.xx x.x% xx $xxxx.xx $xxxxx.xx $xxx.xx xxxxxxxxx
… and so on ...
TOTALS $xxxxx.xx $xxxx.xx $xxxxx.xx $xxx.xx

 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {
    static final public String headerNumber = "#";
    static final public String headerCustomer = "Customer";
    static final public String headerPrincipal = "Principal";
    static final public String headerRate = "Rate";
    static final public String headerYears = "Years";
    static final public String headerPayment = "Payment";
    static final public String headerPayoff = "Payoff";
    static final public String headerFee = "Fee";
    static final public String headerCreditRating = "Credit Rating";

    // Header for the Total Row
    static final public String headerTotal = "Total";

    // Variable for Months in Year
    static final public int MONTHS_IN_YEAR = 12;

    public static void main(String[] args) {

        // Data from the loandata.txt file
        String loanName;
        double loanPrincipal;
        int loanTerm;
        double annualRate;
        int creditRating;
        double loanFee;

        // Variable for the monthly payment
        double monthlyPayment;

        // Variable to calculate the fee column
        // Principal * fee (%)
        double feeAmount;

        // Variable to count the number of people in the loandata.txt file
        int count = 0;

        // Variables for the total row
        // Totals Needed are Principal,Payment, Payoff, Fee
        double totalPrincipal = 0;
        double totalPayOff = 0;
        double totalFee = 0;
        double totalPayments = 0;


        // This prints out the header with spacing for the loandata.txt import
        System.out.printf("%-15s %15s %15s %15s %15s %15s %15s %15s %15s %n", headerNumber, headerCustomer, headerPrincipal, headerRate, headerYears, headerPayment, headerPayoff, headerFee, headerCreditRating);


        try {
            File myObj = new File("loandata.txt");
            Scanner dataReader = new Scanner(myObj);

            // Reading Loop
            while (dataReader.hasNext()) {
                loanName = dataReader.next();
                loanPrincipal = dataReader.nextInt();
                loanTerm = dataReader.nextInt();
                annualRate = dataReader.nextDouble();
                creditRating = dataReader.nextInt();

                // If no loandata is found, put 0.0
                // Loan amount will be zero
                // Principal * 0.0 = 0.0
                if (dataReader.hasNextDouble()) {
                    loanFee = dataReader.nextDouble();
                } else {
                    loanFee = 0.0;
                }

                // Loan fee comes out as .2 but is intended to be 2% so we need to divide by 100
                double loanFeePercentage = loanFee / 100.0;

                // When the file loops through each person in the loandata.txt file, add +1
                count += 1;

                // These are the variables required to calculate the Payments
                feeAmount = loanPrincipal * loanFeePercentage;
                double presentValue = loanPrincipal - feeAmount;
                double ratePerPeriod = annualRate / MONTHS_IN_YEAR / 100.0;
                double annualRatePeriod = annualRate / MONTHS_IN_YEAR;
                int loanPayments = loanTerm * MONTHS_IN_YEAR;

                // Monthly payment is provided by the homework .pdf
                monthlyPayment = (ratePerPeriod * presentValue) / (1 - Math.pow(1 + ratePerPeriod, -loanPayments));

                // When the file loops through each person in the loandata.txt file, add the values to the Totals
                totalPrincipal += loanPrincipal;
                totalPayments += monthlyPayment;
                totalFee += feeAmount;


                // This uses monthlyPayments * loanPayments to calulate the amount of payoff
                double loanPaymentAmount = monthlyPayment * loanPayments;
                totalPayOff += loanPaymentAmount;

                // Declare the credit rating text for fun
                String creditRatingText = null;

                // Credit Rating Calculation
                if (creditRating >= 300 && creditRating <= 579) {
                    creditRatingText = "Very Poor";
                } else if (creditRating >= 580 && creditRating <= 669) {
                    creditRatingText = "Fair";
                } else if (creditRating >= 670 && creditRating <= 739) {
                    creditRatingText = "Good";
                } else if (creditRating >= 740 && creditRating <= 799) {
                    creditRatingText = "Very Good";
                } else if (creditRating >= 800 && creditRating <= 850) {
                    creditRatingText = "Exceptional";
                }

                // Printing Text for each row
                System.out.printf("%-15s %15s %15s %15s %15s %15s %15s %15s %15s %n", count, loanName, "$" + String.format("%.2f", loanPrincipal), String.format("%.2f", annualRatePeriod) + "%", loanTerm, "$" + String.format("%.2f", monthlyPayment), "$" + String.format("%.2f", loanPaymentAmount), "$" + String.format("%.2f", feeAmount), creditRatingText);


            }
            dataReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
        }

        // Using blanks as a spacer because I didn't want to use a string format
        String blank = "";
        // Print Totals Row
        // Put 14 for payoff because it was off center
        System.out.printf("%-15s %15s  %15s %15s %15s %15s %14s %15s %15s %n", headerTotal, blank, "$" + String.format("%.2f", totalPrincipal), blank, blank, "$" + String.format("%.2f", totalPayments), "$" + String.format("%.2f", totalPayOff), "$" + totalFee, blank);

    }
}
