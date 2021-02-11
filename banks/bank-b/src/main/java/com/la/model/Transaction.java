package com.la.model;

import com.la.model.enums.Status;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // acqOrderId

    @Column
    private LocalDateTime timestamp; // acqTimestamp

    @Column
    private Long issuerOrderId; // Buyer bank transaction id (if different banks)

    @Column
    private LocalDateTime issuerTimestamp; // Buyer bank transaction timestamp (if different banks)

    @OneToOne
    @JoinColumn(name = "payment_id")
    private Payment payment; // Delegated from LA to Payment Concetrator to Bank

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @Column
    @Enumerated(EnumType.STRING)
    private Status status;

    public Transaction() {
    }

    public Transaction(LocalDateTime timestamp, Long issuerOrderId, LocalDateTime issuerTimestamp, Payment payment, Account account, Status status) {
        this.timestamp = timestamp;
        this.issuerOrderId = issuerOrderId;
        this.issuerTimestamp = issuerTimestamp;
        this.payment = payment;
        this.account = account;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Long getIssuerOrderId() {
        return issuerOrderId;
    }

    public void setIssuerOrderId(Long issuerOrderId) {
        this.issuerOrderId = issuerOrderId;
    }

    public LocalDateTime getIssuerTimestamp() {
        return issuerTimestamp;
    }

    public void setIssuerTimestamp(LocalDateTime issuerTimestamp) {
        this.issuerTimestamp = issuerTimestamp;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", timestamp=" + timestamp +
                ", issuerOrderId=" + issuerOrderId +
                ", issuerTimestamp=" + issuerTimestamp +
                ", payment=" + payment +
                ", account=" + account +
                ", status=" + status +
                '}';
    }
}
