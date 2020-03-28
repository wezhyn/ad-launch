package com.ad.screen.server.entity;

import com.ad.screen.server.config.GlobalIdentify;
import com.ad.screen.server.mq.PrepareTaskMessage;
import com.wezhyn.project.IBaseTo;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

/**
 * 支付成功订单消费成功后，保存此任务信息，完成与订单模块的再次交互,用于判断支付订单是否成功消费
 * 用于服务器间的任务分配
 * 联合主键：userId,orderId
 * 基本信息：orderInfo
 * 附加信息：executed,wordIdentify(添加当条条例的服务器)
 *
 * @author wezhyn
 * @since 03.26.2020
 */
@Entity(name="equip_task")
@Getter
@Table(
        indexes={
                @Index(name="work_exe_order", columnList="work_identity,executed,oid"),
                @Index(name="equip_unique_id", columnList="oid,uid", unique=true)
        }
)
@DynamicInsert
@DynamicUpdate
public class EquipTask implements IBaseTo<Integer>, PrepareTaskMessage {


    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;
    private TaskKey taskKey;
    /**
     * 显示广告内容
     */
    private String screenView;
    private Boolean vertical;
    private Integer totalNum;
    private Integer deliverNum;
    private Integer rate;
    private Double longitude;
    private Double latitude;
    private Double scope;
    /**
     * 当前任务已经完成的次数
     */
    @Setter
    private Integer executedNum;
    @Column(name="work_identity")
    private String workIdentity=GlobalIdentify.IDENTIFY.getId();

    /**
     * 当前任务是否结束
     */
    @Column(name="executed", columnDefinition="bit(1) default b'0'")
    private Boolean executed;

    public EquipTask() {
    }

    @Override
    public Integer getAvailableAllocateNum() {
        return totalNum - executedNum;
    }

    @Override
    public Integer getId() {
        return id;
    }


    public static final class EquipTaskBuilder {
        private Integer id;
        private TaskKey taskKey;
        private String screenView;
        private Boolean vertical;
        private Integer totalNum;
        private Integer deliverNum;
        private Integer rate;
        private Double longitude;
        private Double latitude;
        private Double scope;
        private Integer executedNum;
        private String workIdentity;

        private EquipTaskBuilder() {
        }

        public static EquipTaskBuilder anEquipTask() {
            return new EquipTaskBuilder();
        }

        public EquipTaskBuilder id(Integer id) {
            this.id=id;
            return this;
        }

        public EquipTaskBuilder taskKey(TaskKey taskKey) {
            this.taskKey=taskKey;
            return this;
        }

        public EquipTaskBuilder screenView(String screenView) {
            this.screenView=screenView;
            return this;
        }

        public EquipTaskBuilder vertical(Boolean vertical) {
            this.vertical=vertical;
            return this;
        }

        public EquipTaskBuilder driverNumInc(int inc) {
            this.deliverNum+=inc;
            return this;
        }

        public EquipTaskBuilder totalNumInc(int inc) {
            this.totalNum+=inc;
            return this;
        }

        public EquipTaskBuilder totalNum(Integer totalNum) {
            this.totalNum=totalNum;
            return this;
        }

        public EquipTaskBuilder deliverNum(Integer deliverNum) {
            this.deliverNum=deliverNum;
            return this;
        }

        public EquipTaskBuilder rate(Integer rate) {
            this.rate=rate;
            return this;
        }

        public EquipTaskBuilder longitude(Double longitude) {
            this.longitude=longitude;
            return this;
        }

        public EquipTaskBuilder latitude(Double latitude) {
            this.latitude=latitude;
            return this;
        }

        public EquipTaskBuilder scope(Double scope) {
            this.scope=scope;
            return this;
        }

        public EquipTaskBuilder executedNum(int executedNum) {
            this.executedNum=executedNum;
            return this;
        }

        public EquipTaskBuilder workIdentity(String workIdentity) {
            this.workIdentity=workIdentity;
            return this;
        }

        public EquipTask build() {
            EquipTask equipTask=new EquipTask();
            equipTask.scope=this.scope;
            equipTask.id=this.id;
            equipTask.vertical=this.vertical;
            equipTask.totalNum=this.totalNum;
            equipTask.rate=this.rate;
            equipTask.longitude=this.longitude;
            equipTask.latitude=this.latitude;
            equipTask.deliverNum=this.deliverNum;
            equipTask.workIdentity=this.workIdentity;
            equipTask.executedNum=this.executedNum;
            equipTask.taskKey=this.taskKey;
            equipTask.screenView=this.screenView;
            return equipTask;
        }
    }
}
