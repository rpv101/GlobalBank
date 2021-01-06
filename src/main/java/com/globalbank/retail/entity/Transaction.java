package com.globalbank.retail.entity;

import java.util.Date;

import org.springframework.data.annotation.Id;

import lombok.Data;


@Data
public class Transaction {

  @Id
  public String id;

  public String toCustomerId;
  public String fromCustomerId;
  public Date date;
  public String transactionType;
  public String amount; 

}