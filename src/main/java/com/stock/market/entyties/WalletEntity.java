package com.stock.market.entyties;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table
@Data
public class WalletEntity {
    private long walletId;

}
