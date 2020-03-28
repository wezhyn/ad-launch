package com.ad.screen.server.cache;

import lombok.Getter;

import java.util.Objects;

/**
 * 根据 entryId 标识在 hashmap 中的唯一性
 * version 只用来进行版本控制
 *
 * @author wezhyn
 * @since 03.28.2020
 */
@Getter
public final class TaskIdentify {

    /**
     * 标识 entryId 版本
     */
    private final int version;
    /**
     * 标识在25个序列帧的哪一个位置
     */
    private Integer entryId;

    public TaskIdentify(Integer entryId, int version) {
        this.entryId=entryId;
        this.version=version;
    }

    @Override
    public int hashCode() {
        return Objects.hash(entryId);
    }

    @Override
    public boolean equals(Object o) {
        if (this==o) {
            return true;
        }
        if (o==null || getClass()!=o.getClass()) {
            return false;
        }
        TaskIdentify that=(TaskIdentify) o;
        return Objects.equals(entryId, that.entryId);
    }
}
