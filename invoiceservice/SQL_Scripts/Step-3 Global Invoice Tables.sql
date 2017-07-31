use invoice;

CREATE TABLE IF NOT EXISTS `global_invoice_setup` (
  `global_inv_setup_id` CHAR(36) NOT NULL,
  `inv_setup_name` VARCHAR(50) NOT NULL,
  `setup_descr` VARCHAR(300) NULL,
  `inv_type` VARCHAR(128) NOT NULL,
  `inv_freq` VARCHAR(20) NULL,
  `inv_freq_type` VARCHAR(20) NULL DEFAULT NULL,
  `inv_freq_date` DATE NULL DEFAULT NULL,
  `in_freq_day` VARCHAR(10) NULL DEFAULT NULL,
  `delivery_mode` VARCHAR(128) NOT NULL,
  `inv_template_id` BIGINT(15) NOT NULL,
  `inv_start_dt` DATE NULL,
  `inv_end_dt` DATE NULL,
  `notes_to_display` VARCHAR(2000) NULL,
  `payment_terms` VARCHAR(200) NULL,
  `lob_name` VARCHAR(128) NULL,
  `inv_terms` VARCHAR(128) NOT NULL,
  `currency` VARCHAR(128) NULL,
  `inv_notes` VARCHAR(300) NOT NULL,
  `actv_flg` ENUM('Y', 'N') NOT NULL DEFAULT 'Y',
  `inv_status` ENUM('Active', 'Inactive', 'Hold', 'Draft') NOT NULL,
  `created_by` BIGINT(15) NULL DEFAULT NULL,
  `created_on` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_by` BIGINT(15) NULL DEFAULT NULL,
  `updated_on` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`global_inv_setup_id`),
  INDEX `fk_global_invoice_setup_invoice_template_idx` (`inv_template_id` ASC),
  CONSTRAINT `fk_global_invoice_setup_invoice_template`
    FOREIGN KEY (`inv_template_id`)
    REFERENCES `invoice_template` (`inv_template_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


CREATE TABLE IF NOT EXISTS `global_invoice_setup_option` (
  `global_inv_setup_optn_id` CHAR(36) NOT NULL,
  `global_inv_setup_id` CHAR(36) NOT NULL,
  `option_lookup_id` CHAR(36) NOT NULL,
  `option_value` ENUM('Y', 'N') NOT NULL DEFAULT 'Y',
  PRIMARY KEY (`global_inv_setup_optn_id`),
  INDEX `fk_global_invoice_setup_option_gis_idx` (`global_inv_setup_id` ASC),
  CONSTRAINT `fk_global_invoice_setup_option_gis`
    FOREIGN KEY (`global_inv_setup_id`)
    REFERENCES `global_invoice_setup` (`global_inv_setup_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;



