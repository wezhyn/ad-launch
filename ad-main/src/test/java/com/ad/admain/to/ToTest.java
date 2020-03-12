package com.ad.admain.to;

import com.wezhyn.project.StringEnum;
import com.wezhyn.project.account.SexEnum;
import com.wezhyn.project.database.JpaHibernateEnumConverter;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @author wezhyn
 * @since 12.12.2019
 */
@Entity(name="ad_test")
@Setter
@Getter
@ToString
@TypeDef(name="stringStrategy", typeClass=JpaHibernateEnumConverter.class,
        defaultForType=StringEnum.class)
public class ToTest {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;

    private SexEnum sexEnum;

    @ColumnDefault("'TEST1'")
    @Type(type="stringStrategy")
    private ToEnum toEnum;

}
