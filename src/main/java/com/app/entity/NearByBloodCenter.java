package com.app.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "nearby_blood_centers")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class NearByBloodCenter {

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
