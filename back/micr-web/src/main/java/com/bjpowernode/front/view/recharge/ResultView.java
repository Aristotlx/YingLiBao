package com.bjpowernode.front.view.recharge;

import com.bjpowernode.api.model.RechargeRecord;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.math.BigDecimal;

public class ResultView {
    private Integer id;
    private String result = "未知";
    private String rechargeDate = "-";
    private BigDecimal rechargeMoney;

    public ResultView(RechargeRecord record) {
        this.id = record.getId();
        this.rechargeMoney = record.getRechargeMoney();
        if (record.getRechargeTime() != null) {
            this.rechargeDate = DateFormatUtils.format(record.getRechargeTime(), "yyyy-MM-dd");
        }
        switch (record.getRechargeStatus()) {
            case 0:
                this.result = "充值中";
                break;
            case 1:
                this.result = "成功";
                break;
            case 2:
                this.result = "失败";
        }
    }

    public Integer getId() {
        return id;
    }

    public String getReult() {
        return result;
    }

    public String getRechargeDate() {
        return rechargeDate;
    }

    public BigDecimal getRechargeMoney() {
        return rechargeMoney;
    }
}
