package Model.Billing;

import Enums.BillStatus;
import Enums.TypesOfBill;
import Model.Contract.Contract;

public class Bill {

    private String billID;
    private String tenantID;
    private Contract contractID;
    private TypesOfBill type;
    private double amount;
    private BillStatus status;

    public Bill(String billID, String tenantID, Contract contractID, TypesOfBill type, double amount,
            BillStatus status) {
        this.billID = billID;
        this.tenantID = tenantID;
        this.contractID = contractID;
        this.type = type;
        this.amount = amount;
        this.status = status;
    }

    public BillStatus getStatus() {
        return status;
    }

    public double getAmount() {
        return amount;
    }

    public TypesOfBill getType() {
        return type;
    }

    public Contract getContractID() {
        return contractID;
    }

    public String getTenantID() {
        return tenantID;
    }

    public String getBillID() {
        return billID;
    }

    public void setBillID(String billID) {
        this.billID = billID;
    }

    public void setTenantID(String tenantID) {
        this.tenantID = tenantID;
    }

    public void setContractID(Contract contractID) {
        this.contractID = contractID;
    }

    public void setType(TypesOfBill type) {
        this.type = type;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setStatus(BillStatus status) {
        this.status = status;
    }

    public double computeTotalBillFromEnum() {
        double total = 0;

        for (TypesOfBill billType : TypesOfBill.values()) {
            switch (billType) {
                case MONTHLY_PAYMENT:
                    total += contractID.getMonthlyRent(); 
                    break;
                case WATER:
                    total += 150;
                    break;
                case ELECTRICITY:
                    total += 450;
                    break;
                case WIFI:
                    total += 200;
                    break;
                case PARKING_FEE:
                    total += 300;
                    break;
                case LATE_FEE:
                    total += 100;
                    break;
                case SECURITY_DEPOSIT:
                    total += contractID.getDeposit();
                    break;
                case ADVANCE_PAYMENT:
                    total += contractID.getMonthlyRent();
                    break;
            }
        }

        return total;
    }

    public void displayBill() {
        System.out.println("===== BILL DETAILS =====");
        System.out.println("Bill ID       : " + this.billID);
        System.out.println("Tenant ID     : " + this.tenantID);
        System.out.println("Contract ID   : " + this.contractID);
        System.out.println("Bill Type     : " + this.type);
        System.out.println("Amount        : ₱" + this.amount);
        System.out.println("Status        : " + this.status);
        System.out.println("========================");
    }

}
