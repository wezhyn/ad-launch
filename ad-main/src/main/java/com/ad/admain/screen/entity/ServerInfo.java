package com.ad.admain.screen.entity;

import com.wezhyn.project.IBaseTo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

/**
 * @ClassName ServerInfo
 * @Description TODO
 * @Param
 * @Author ZLB
 * @Date 2020/2/23 2:01
 * @Version 1.0
 */
@Entity
@Table(name = "screen_server_info")
@Data
@AllArgsConstructor
@NoArgsConstructor
@DynamicInsert
@DynamicUpdate
@Builder
public class ServerInfo implements IBaseTo<Integer> {

    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    int id;

    @Column(name = "ip")
    String ip;

    @Column(name = "port")
    int port;

}
