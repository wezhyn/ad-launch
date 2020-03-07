package com.ad.admain.cache;

import com.ad.admain.controller.equipment.entity.Equipment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName EquipmentCache
 * @Description
 * @Author ZLB_KAM
 * @Date 2020/3/7 20:40
 * @Version V1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PooledIdAndEquipCache {
    Long pooledId;
    Equipment equipment;
}
