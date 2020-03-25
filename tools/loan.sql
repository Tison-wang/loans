/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80018
 Source Host           : localhost:3306
 Source Schema         : loan

 Target Server Type    : MySQL
 Target Server Version : 80018
 File Encoding         : 65001

 Date: 22/12/2019 15:09:08
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_aliyun_sms_template
-- ----------------------------
DROP TABLE IF EXISTS `t_aliyun_sms_template`;
CREATE TABLE `t_aliyun_sms_template`
(
    `order_state` int(11)                                                       NOT NULL,
    `code`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '阿里云短信模板'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_contract
-- ----------------------------
DROP TABLE IF EXISTS `t_contract`;
CREATE TABLE `t_contract`
(
    `id`          int(10) UNSIGNED                                              NOT NULL AUTO_INCREMENT,
    `fk_owner_id` int(11) UNSIGNED                                              NOT NULL,
    `type`        int(11) UNSIGNED                                              NOT NULL,
    `type_name`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL,
    `content`     text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci         NULL,
    `create_time` timestamp(0)                                                  NULL     DEFAULT NULL,
    `modify_time` timestamp(0)                                                  NULL     DEFAULT NULL,
    `status`      int(11)                                                       NOT NULL DEFAULT 1,
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `uindex_fkownerid_type` (`fk_owner_id`, `type`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_faq
-- ----------------------------
DROP TABLE IF EXISTS `t_faq`;
CREATE TABLE `t_faq`
(
    `id`          int(11) UNSIGNED                                              NOT NULL AUTO_INCREMENT,
    `fk_user_id`  int(11) UNSIGNED                                              NOT NULL,
    `name`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `content`     text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci         NOT NULL,
    `state`       int(11)                                                       NOT NULL DEFAULT 1 COMMENT '状态 -1:删除 0:停用 1：启用',
    `create_time` timestamp(0)                                                  NULL     DEFAULT NULL,
    `modify_time` timestamp(0)                                                  NULL     DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_loan_info
-- ----------------------------
DROP TABLE IF EXISTS `t_loan_info`;
CREATE TABLE `t_loan_info`
(
    `id`                      int(11) UNSIGNED                                              NOT NULL AUTO_INCREMENT,
    `fk_user_id`              int(11) UNSIGNED                                              NOT NULL,
    `fk_owner_id`             int(11) UNSIGNED                                              NOT NULL,
    `no`                      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `amount`                  decimal(12, 2) UNSIGNED                                       NOT NULL,
    `loan_term`               int(11)                                                       NOT NULL,
    `use_for`                 varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL,
    `repay_amount_of_month`   decimal(12, 2)                                                NOT NULL,
    `payment_pic`             varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL,
    `comment`                 varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL,
    `current_withdraw_amount` decimal(12, 2)                                                NULL     DEFAULT 0.00 COMMENT '当前提现金额',
    `total_withdraw_amount`   decimal(10, 0)                                                NULL     DEFAULT 0 COMMENT '累计提现金额',
    `state`                   int(11)                                                       NOT NULL DEFAULT 0 COMMENT '状态 默认为0：提交贷款申请',
    `create_time`             timestamp(0)                                                  NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0),
    `modify_time`             timestamp(0)                                                  NULL     DEFAULT NULL,
    `fk_modifier`             int(11)                                                       NULL     DEFAULT NULL,
    `state_desc`              varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL COMMENT '状态描述',
    `transfer_voucher`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL COMMENT '转账凭证',
    `contract`                text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci         NULL COMMENT '合同',
    `service_charge_rate`     double(10, 8) UNSIGNED                                        NULL     DEFAULT NULL COMMENT '服务费率',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_repayment
-- ----------------------------
DROP TABLE IF EXISTS `t_repayment`;
CREATE TABLE `t_repayment`
(
    `id`             int(10) UNSIGNED                                              NOT NULL AUTO_INCREMENT,
    `fk_user_id`     int(11)                                                       NOT NULL,
    `fk_loan_id`     int(11)                                                       NOT NULL,
    `loan_no`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `amount`         decimal(12, 2)                                                NOT NULL,
    `current_period` int(11) UNSIGNED                                              NOT NULL,
    `total_period`   int(11) UNSIGNED                                              NOT NULL,
    `state`          int(11)                                                       NOT NULL DEFAULT 0,
    `create_time`    timestamp(0)                                                  NULL     DEFAULT NULL,
    `modify_time`    timestamp(0)                                                  NULL     DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_sys_config
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_config`;
CREATE TABLE `t_sys_config`
(
    `id`                  int(11) UNSIGNED                                              NOT NULL AUTO_INCREMENT,
    `fk_owner_id`         int(11) UNSIGNED                                              NOT NULL,
    `domain`              varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL,
    `site_name`           varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL COMMENT '站点名称',
    `site_title`          varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL,
    `site_keyword`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL,
    `site_desc`           varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL,
    `close_status`        tinyint(2) UNSIGNED                                           NULL     DEFAULT 0,
    `closed_tips`         varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL,
    `min_loan`            decimal(12, 2) UNSIGNED                                       NULL     DEFAULT NULL,
    `max_loan`            decimal(12, 2) UNSIGNED                                       NULL     DEFAULT NULL,
    `default_loan`        decimal(12, 2) UNSIGNED                                       NULL     DEFAULT NULL,
    `allow_loan_months`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL,
    `default_loan_month`  int(11) UNSIGNED                                              NULL     DEFAULT NULL,
    `service_charge_rate` double(10, 8) UNSIGNED                                        NULL     DEFAULT NULL,
    `repayment_date`      int(11) UNSIGNED                                              NULL     DEFAULT NULL,
    `statistical_code`    text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci         NULL COMMENT '统计/客服代码',
    `record_info`         varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL COMMENT '备案信息',
    `sms_sign`            varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL COMMENT '短信签名',
    `index_template`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL COMMENT '首页模板',
    `total_sms_count`     int(11)                                                       NOT NULL DEFAULT 10000,
    `cost_sms_count`      int(11)                                                       NOT NULL DEFAULT 0,
    `status`              int(11)                                                       NOT NULL DEFAULT 1,
    `bottom_content`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `t_sys_config_fk_owner_id_uindex` (`fk_owner_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user`
(
    `id`              int(11)                                                       NOT NULL AUTO_INCREMENT,
    `fk_owner_id`     int(11) UNSIGNED                                              NULL     DEFAULT NULL,
    `user_name`       varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `password`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `type`            int(11) UNSIGNED                                              NOT NULL DEFAULT 0 COMMENT '用户类型：0：普通用户（默认） 1：管理员 2：超级管理员',
    `state`           int(11)                                                       NOT NULL DEFAULT 1 COMMENT '状态 -1:删除 0：停用 1：启用',
    `create_time`     timestamp(0)                                                  NULL     DEFAULT NULL,
    `last_login_time` timestamp(0)                                                  NULL     DEFAULT NULL,
    `amount`          decimal(12, 2)                                                NULL     DEFAULT 0.00 COMMENT '可提现金额',
    `freeze_amount`   decimal(12, 2)                                                NULL     DEFAULT 0.00 COMMENT '冻结金额',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_user_detail
-- ----------------------------
DROP TABLE IF EXISTS `t_user_detail`;
CREATE TABLE `t_user_detail`
(
    `id`                    int(11) UNSIGNED                                              NOT NULL AUTO_INCREMENT,
    `fk_user_id`            int(11) UNSIGNED                                              NOT NULL,
    `name`                  varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL,
    `id_card_number`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL,
    `id_card_front_image`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL,
    `id_card_reverse_image` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL,
    `id_card_hold_image`    varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL,
    `current_address`       varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL,
    `bank_name`             varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL,
    `bank_card_number`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL,
    `create_time`           timestamp(0)                                                  NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0),
    `monthly_income`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL COMMENT '月收入',
    `link_name1`            varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL,
    `mobile1`               varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL,
    `relation1`             varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL,
    `link_name2`            varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL,
    `mobile2`               varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL,
    `relation2`             varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `t_user_detail_fk_user_id_uindex` (`fk_user_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_website_config
-- ----------------------------
DROP TABLE IF EXISTS `t_website_config`;
CREATE TABLE `t_website_config`
(
    `id`                  int(11) UNSIGNED                                              NOT NULL AUTO_INCREMENT,
    `sms_sign_name`       varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '短信签名',
    `sms_access_key_id`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
    `sms_access_secret`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
    `open_code_status`    tinyint(2) UNSIGNED                                           NULL DEFAULT 1,
    `site_count_limit`    int(11)                                                       NULL DEFAULT NULL COMMENT '站点短信接口每天访问次数限制',
    `code_sms_url`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '验证码短信接口地地址',
    `code_sms_user_id`    varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '验证码短信用户id',
    `code_sms_account`    varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '验证码短信账号',
    `code_sms_password`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '验证码短信账号的密码',
    `notify_sms_url`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
    `notify_sms_user_id`  varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
    `notify_sms_account`  varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
    `notify_sms_password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_withdraw
-- ----------------------------
DROP TABLE IF EXISTS `t_withdraw`;
CREATE TABLE `t_withdraw`
(
    `id`          int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
    `fk_user_id`  int(11)          NOT NULL,
    `amount`      decimal(12, 2)   NOT NULL,
    `state`       int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '0：未处理 1：已处理',
    `create_time` timestamp(0)     NULL     DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
