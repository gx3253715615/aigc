package com.blockchain.aigc.enums;

/**
 * @author gaoxinyu
 * @date 2025/12/28 10:38
 **/
public enum LicenseTypeEnum {
    //个人 / 商用 / 独占
    NONE(0),
    PERSONAL(1),
    COMMERCIAL(2),
    EXCLUSIVE(3);

    public int num;

    LicenseTypeEnum(int num) {
    }

    // get
    public int getNum() {
        return num;
    }

    public static LicenseTypeEnum getLicenseType(int num) {
        for (LicenseTypeEnum licenseTypeEnum : LicenseTypeEnum.values()) {
            if (licenseTypeEnum.num == num) {
                return licenseTypeEnum;
            }
        }
        return null;
    }
}
