use invoice;


DROP TABLE IF EXISTS `purchase_order`;
DROP TABLE IF EXISTS `purchase_order_contractor`;
DROP TABLE IF EXISTS `purchase_order_notes`;

CREATE TABLE IF NOT EXISTS `purchase_order` (
  `po_id` CHAR(36) NOT NULL,
  `po_number` VARCHAR(20) NOT NULL,
  `customer_id` BIGINT(15) NOT NULL,
  `start_date` DATE NOT NULL,
  `end_date` DATE NOT NULL,
  `actv_flg` ENUM('Y', 'N') NOT NULL DEFAULT 'Y',
  `description` VARCHAR(300) NOT NULL,
  `amt_alert` ENUM('Y', 'N') NOT NULL DEFAULT 'N',
  `exp_alert` ENUM('Y', 'N') NOT NULL DEFAULT 'N',
  `rev_amt` DECIMAL(10,2) NOT NULL,
  `exp_amt` DECIMAL(10,2) NULL DEFAULT NULL,
  `notes` VARCHAR(300) NOT NULL,
  `parent_po_id` CHAR(36) NULL DEFAULT NULL,
  `status` ENUM('Active', 'Inactive') NOT NULL DEFAULT 'Active',
  `bal_rev_amt` DECIMAL(10,2) NOT NULL,
  `bal_exp_amt` DECIMAL(10,2) NOT NULL,
  `unbilled_rev_amt` DECIMAL(10,2) NULL DEFAULT NULL,
  `unbilled_exp_amt` DECIMAL(10,2) NULL DEFAULT NULL,
  `unbilled_po_ref` CHAR(36) NULL DEFAULT NULL,
  `rollover_type` ENUM('Auto', 'Manual') NULL DEFAULT NULL,
  `created_by` BIGINT(15) NULL DEFAULT NULL,
  `created_on` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_by` BIGINT(15) NULL DEFAULT NULL,
  `updated_on` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`po_id`),
  UNIQUE INDEX `po_number_UNIQUE` (`po_number` ASC))
ENGINE = InnoDB;


CREATE TABLE IF NOT EXISTS `purchase_order_replenishments` (
  `po_repl_id` CHAR(36) NOT NULL,
  `po_id` CHAR(36) NOT NULL,
  `rev_amt` DECIMAL(10,2) NOT NULL,
  `exp_amt` DECIMAL(10,2) NULL,
  `reversal_flg` ENUM('Y', 'N') NOT NULL DEFAULT 'Y',
  `notes` VARCHAR(200) NOT NULL,
  `reversal_notes` VARCHAR(200) NULL DEFAULT NULL,
  `created_by` BIGINT(15) NULL DEFAULT NULL,
  `created_on` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_by` BIGINT(15) NULL DEFAULT NULL,
  `updated_on` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`po_repl_id`),
  INDEX `fk_purchase_order_replenishments_idx` (`po_id` ASC),
  CONSTRAINT `fk_purchase_order_replenishments`
    FOREIGN KEY (`po_id`)
    REFERENCES `purchase_order` (`po_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


CREATE TABLE IF NOT EXISTS `purchase_order_contractor` (
  `po_cntctr_id` CHAR(36) NOT NULL,
  `po_id` CHAR(36) NOT NULL,
  `empl_id` BIGINT(15) NOT NULL,
  `created_by` BIGINT(15) NULL DEFAULT NULL,
  `created_on` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_by` BIGINT(15) NULL DEFAULT NULL,
  `updated_on` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`po_cntctr_id`),
  INDEX `fk_purchase_order_contractor_po_idx` (`po_id` ASC),
  CONSTRAINT `fk_purchase_order_contractor_po`
    FOREIGN KEY (`po_id`)
    REFERENCES `purchase_order` (`po_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


CREATE 
  OR REPLACE
VIEW `contractor_tab_view` AS
    SELECT 
        ROUND((RAND() * 10000), 0) AS `unique_id`,
        `common`.`employee`.`empl_id` AS `employee_id`,
        CONCAT(`common`.`employee`.`first_nm`,
                `common`.`employee`.`last_nm`) AS `employee_name`,
        `common`.`employee`.`file_no` AS `file_no`,
        `icd`.`inv_setup_id` AS `inv_setup_id`,
        `poc`.`po_id` AS `po_id`,
        `po`.`po_number` AS `po_number`,
        `po`.`start_date` AS `po_start_date`
    FROM
        (((`invoice`.`invoicesetup_contractor_details` `icd`
        JOIN `common`.`employee` ON (((`common`.`employee`.`empl_id` = `icd`.`empl_id`)
            AND (`icd`.`remove_flg` = 'N'))))
        LEFT JOIN `invoice`.`purchase_order_contractor` `poc` ON ((`common`.`employee`.`empl_id` = `poc`.`empl_id`)))
        LEFT JOIN `invoice`.`purchase_order` `po` ON ((`poc`.`po_id` = `po`.`po_id`)));







