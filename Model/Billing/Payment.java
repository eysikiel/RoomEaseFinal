package Model.Billing;

public class Payment {

    private String paymentID;
    private double amount;
    private String paymentMethod;

    public Payment() {
    }

    public Payment(String paymentID, double amount, String paymentMethod) {
        this.paymentID = paymentID;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
    }

    public String getPaymentID() {
        return paymentID;
    }

    public void setPaymentID(String paymentID) {
        this.paymentID = paymentID;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}
