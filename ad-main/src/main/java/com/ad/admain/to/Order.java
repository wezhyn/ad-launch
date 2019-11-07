package com.ad.admain.to;

import com.ad.admain.utils.CustomJsonDateDeserializer;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Table(name = "ad_order")
@Accessors(chain = true)
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@DynamicUpdate
public class Order{

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Value> valueList;

    @JsonDeserialize(using = CustomJsonDateDeserializer.class)
    private Date startTime;
    @JsonDeserialize(using = CustomJsonDateDeserializer.class)
    private Date endTime;
    @JsonDeserialize(using = CustomJsonDateDeserializer.class)
    private Date startDate;
    @JsonDeserialize(using = CustomJsonDateDeserializer.class)
    private Date endDate;

    private Long price;

    private double latitude;

    private double longitude;

    private int scope;

    private int rate;

    private int uid;


}
