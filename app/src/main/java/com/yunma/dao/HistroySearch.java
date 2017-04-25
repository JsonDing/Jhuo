package com.yunma.dao;

import org.greenrobot.greendao.annotation.*;

/**
 * Created on 2017-04-18
 *
 * @author Json.
 */
@Entity
public class HistroySearch {
    @Id
    private Long Id;
    @Property(nameInDb = "searchNumber")
    private String searchNumber; // 搜索货号
    @Property(nameInDb = "searchTime")
    private long searchTime; // 首次搜索时间
    @Property(nameInDb = "lastUpdatasTime")
    private long lastUpdatasTime; //最后一次搜索时间
    @Property(nameInDb = "searchTimes")
    private int searchTimes; //搜索次数
    @Generated(hash = 518012300)
    public HistroySearch(Long Id, String searchNumber, long searchTime,
            long lastUpdatasTime, int searchTimes) {
        this.Id = Id;
        this.searchNumber = searchNumber;
        this.searchTime = searchTime;
        this.lastUpdatasTime = lastUpdatasTime;
        this.searchTimes = searchTimes;
    }
    @Generated(hash = 38093316)
    public HistroySearch() {
    }
    public Long getId() {
        return this.Id;
    }
    public void setId(Long Id) {
        this.Id = Id;
    }
    public String getSearchNumber() {
        return this.searchNumber;
    }
    public void setSearchNumber(String searchNumber) {
        this.searchNumber = searchNumber;
    }
    public long getSearchTime() {
        return this.searchTime;
    }
    public void setSearchTime(long searchTime) {
        this.searchTime = searchTime;
    }
    public long getLastUpdatasTime() {
        return this.lastUpdatasTime;
    }
    public void setLastUpdatasTime(long lastUpdatasTime) {
        this.lastUpdatasTime = lastUpdatasTime;
    }
    public int getSearchTimes() {
        return this.searchTimes;
    }
    public void setSearchTimes(int searchTimes) {
        this.searchTimes = searchTimes;
    }

    
}
