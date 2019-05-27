package dk.tangsolutions.bankingapp.models;

public class AutoPayment {
    private Double amount;
    private String to;
    private String from;
    private String when;


    public AutoPayment() {
    }

    public AutoPayment(Double amount, String to, String from, String when) {
        this.amount = amount;
        this.to = to;
        this.from = from;
        this.when = when;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getWhen() {
        return when;
    }

    public void setWhen(String when) {
        this.when = when;
    }
}
