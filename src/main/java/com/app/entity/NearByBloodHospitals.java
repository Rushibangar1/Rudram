package com.app.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "nearby_hospitals")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NearByBloodHospitals {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String address;

    @Column(name = "state_id")
    private Long stateId;

    @Column(name = "city_id")
    private Long cityId;


}
