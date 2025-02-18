package com.cloud.emr.Affair.Contract.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity(name = "Contract")
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ContractEntity {
    @Id
    @Column(name = "contract_code", nullable = false, unique = true)
    private Long contractCode;

    @Column(name = "contract_name", nullable = false)
    private String contractName;

    @Column(name = "contract_relationship", nullable = false)
    private String contractRelationship;

    @Column(name = "contract_telephone")
    private String contractTelephone;

    @Column(name = "contract_discount")
    private Long contractDiscount;
}
