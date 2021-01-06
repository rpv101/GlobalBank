package com.globalbank.retail.entity;

import java.util.Date;

import org.springframework.data.annotation.Id;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;


@Data
public class Transaction {

  @Id
  public String id;

  public String toCustomerId;
  public String fromCustomerId;
  @JsonFormat(pattern="yyyy-mm-dd hh:mm:ss")
  public Date date;
  public String transactionType;
  public String amount; 

}