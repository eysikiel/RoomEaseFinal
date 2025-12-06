import java.util.LinkedList;

public class BillingAndPayments {
    private LinkedList<Bill> billList;
    private LinkedList<Payment> paymentRecords;
    private double lateFeeAmount;

    public BillingAndPayments(LinkedList<Bill> billList, LinkedList<Payment> paymentRecords, double lateFeeAmount) {
        this.billList = billList;
        this.paymentRecords = paymentRecords;
        this.lateFeeAmount = lateFeeAmount;
    }

    public LinkedList<Bill> getBillList() {
        return billList;
    }

    public LinkedList<Payment> getPaymentRecords() {
        return paymentRecords;
    }

    public double getLateFeeAmount() {
        return lateFeeAmount;
    }

    public void setBillList(LinkedList<Bill> billList) {
        this.billList = billList;
    }

    public void setPaymentRecords(LinkedList<Payment> paymentRecords) {
        this.paymentRecords = paymentRecords;
    }

    public void setLateFeeAmount(double lateFeeAmount) {
        this.lateFeeAmount = lateFeeAmount;
    }

    public void generateMonthlyBills() {
        System.out.println("Generating monthly bills...");
        for (Tenant tenant : TenantManagement.getAllTenants()) {
            double baseRent = generateBaseRent(tenant.getTenantID());
            double electricity = computeSubmeteredElectricity(
                    tenant.getTenantID(),
                    tenant.getLastElectricReading(),
                    tenant.getCurrentElectricReading()
            );

            Bill bill = new Bill(
                    tenant.getTenantID(),
                    baseRent,
                    electricity
            );

            billList.add(bill);

            System.out.println("Bill created for " + tenant.getTenantID());
        }
        System.out.println("Monthly bill generation completed.");
    }

    public double generateBaseRent(String tenantID) {
        Tenant tenant = TenantManagement.findTenant(tenantID);
        if (tenant == null) return 0;
        return tenant.getMonthlyRent();
    }

    public double computeSubmeteredElectricity(String tenantID, double previousReading, double currentReading) {
        double ratePerKwh = 12.50;
        double consumption = currentReading - previousReading;
        if (consumption < 0) consumption = 0;
        return consumption * ratePerKwh;
    }

    public void addOptionalFee(String tenantID, OptionalFee fee) {
        Bill bill = findLatestUnpaidBill(tenantID);
        if (bill == null) {
            System.out.println("No unpaid bill found for " + tenantID);
            return;
        }
        bill.addOptionalFee(fee);
        System.out.println("Optional fee added for " + tenantID + ": " + fee.getDescription());
    }

    public void finalizeBills() {
        for (Bill bill : billList) {
            if (!bill.isFinalized()) {
                bill.finalizeBill();
            }
        }
        System.out.println("All bills have been finalized.");
    }

    public void viewUnpaidBills() {
        System.out.println("----- UNPAID BILLS -----");
        for (Bill bill : billList) {
            if (!bill.isPaid()) {
                System.out.println(bill);
            }
        }
    }

    public void recordPayment(String tenantID, Payment payment) {
        Bill bill = findLatestUnpaidBill(tenantID);

        if (bill == null) {
            System.out.println("No unpaid bill found for tenant " + tenantID);
            return;
        }

        double balance = bill.getTotalAmount() - bill.getAmountPaid();

        if (payment.getAmount() >= balance) {
            bill.markAsPaid();
        }

        bill.addPayment(payment);
        paymentRecords.add(payment);

        System.out.println("Payment recorded for " + tenantID
                + ". Amount: " + payment.getAmount());
    }

    public void addLateFee(String tenantID, double amount) {
        Bill bill = findLatestUnpaidBill(tenantID);
        if (bill == null) {
            System.out.println("No unpaid bill found for " + tenantID);
            return;
        }
        bill.addLateFee(amount);
        System.out.println("Late fee added to " + tenantID);
    }

    public void generateMonthlyIncomeReport(String month) {
        double totalIncome = 0;

        System.out.println("----- INCOME REPORT for " + month + " -----");

        for (Payment payment : paymentRecords) {
            if (payment.getMonth().equalsIgnoreCase(month)) {
                totalIncome += payment.getAmount();
            }
        }

        System.out.println("Total Income: PHP " + totalIncome);
    }

    public void backToAdminMenu() {
        System.out.println("Returning to Admin Menu...");
    }

    private Bill findLatestUnpaidBill(String tenantID) {
        for (int i = billList.size() - 1; i >= 0; i--) {
            Bill bill = billList.get(i);
            if (bill.getTenantID().equals(tenantID) && !bill.isPaid()) {
                return bill;
            }
        }
        return null;
    }
}
