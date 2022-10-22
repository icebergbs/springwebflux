package com.bingshan.user.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class Account implements Serializable {


    private static final long serialVersionUID = -1354095961912360712L;
    private String accountId;

    private String accountName;

    @JsonProperty("PHoNe")
    private String PHoNe;


    private Account account;
}
