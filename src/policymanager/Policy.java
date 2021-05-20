// policy Class for Gadget Insurance Policy Prototype
// Author: Christopher Burrell
// Student Number: T7145969

package policymanager;

public class Policy 
{
    // Policy attributes
    private String customerName;   
    private String policyRef;    
    private int numberOfItems;
    private int mostExpensiveItem;
    private int excess;
    private String policyTerms;
    private int policyAmount;



    Policy()
    {
        customerName = "";
        policyRef = "";        
        numberOfItems = 0;
        mostExpensiveItem = 0;
        excess = 30;
        policyTerms = "Monthly";
        policyAmount = -1; // -1 is the defined value for rejected policies.  Valid policies always have another value assigned to them.  
    }
    
    Policy (String name,String ref,int numItems,int mostExp,int policyExcess,String frequency,int premiumAmount) 
    {
        customerName = name;
        policyRef = ref;        
        numberOfItems = numItems;
        mostExpensiveItem = mostExp;
        excess = policyExcess;
        policyTerms = frequency;
        policyAmount = premiumAmount;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getPolicyRef() {
        return policyRef;
    }

    public int getNumberOfItems() {
        return numberOfItems;
    }

    public int getMostExpensiveItem() {
        return mostExpensiveItem;
    }

    public int getExcess() {
        return excess;
    }

    public String getPolicyTerms() {
        return policyTerms;
    }

    public int getPolicyAmount() {
        return policyAmount;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void setPolicyRef(String policyRef) {
        this.policyRef = policyRef;
    }

    public void setNumberOfItems(int numberOfItems) {
        this.numberOfItems = numberOfItems;
    }

    public void setMostExpensiveItem(int mostExpensiveItem) {
        this.mostExpensiveItem = mostExpensiveItem;
    }

    public void setExcess(int excess) {
        this.excess = excess;
    }

    public void setPolicyTerms(String policyTerms) {
        this.policyTerms = policyTerms;
    }

    public void setPolicyAmount(int policyAmount) {
        this.policyAmount = policyAmount;
    }

}