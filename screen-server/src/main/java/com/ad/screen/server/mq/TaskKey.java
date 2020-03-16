package com.ad.screen.server.mq;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

/**
 * @ClassName TaskKey
 * @Description FailTask复合主键
 * @Author ZLB_KAM
 * @Date 2020/3/16 13:43
 * @Version V1.0
 **/

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TaskKey implements Serializable {


    @Column(name = "oid")
    private Integer oid;


    @Column(name = "uid")
    private Integer uid;



    @Override
    public int hashCode() {
        return Objects.hash(oid, uid);
    }

    @Override
    public String toString() {
        return "TaskKey{" +
                "oid=" + oid +
                ", uid=" + uid +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaskKey taskKey = (TaskKey) o;
        return oid.equals(taskKey.oid) &&
                uid.equals(taskKey.uid);
    }
}
