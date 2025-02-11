package com.onlinebank.dto;

import java.time.LocalDateTime;

public class LoanDTO {
	
	private Long id;
	
	
    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	private Double amount;
    private String status;
    private String bankUserName;
    
    private String approvedBy; 
    
    private LocalDateTime approvalDate;
    
    
    public String getApprovedBy() {
 		return approvedBy;
 	}

 	public void setApprovedBy(String approvedBy) {
 		this.approvedBy = approvedBy;
 	}

 	public LocalDateTime getApprovalDate() {
 		return approvalDate;
 	}

 	public void setApprovalDate(LocalDateTime approvalDate) {
 		this.approvalDate = approvalDate;
 	}


    // Getters and Setters
    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBankUserName() {
        return bankUserName;
    }

    public void setBankUserName(String bankUserName) {
        this.bankUserName = bankUserName;
    }
}
