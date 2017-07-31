use invoice;

CREATE TABLE IF NOT EXISTS `global_invoice_setup_contractor` (
  `global_invoice_setup_contractor_id` CHAR(36) NOT NULL,
  `global_invoice_setup_id` CHAR(36) NOT NULL,
  `contractor_id` BIGINT(15) NOT NULL, 
  `contractor_name` VARCHAR(128) NOT NULL,
  `file_number` VARCHAR(128) NOT NULL,
  `po_number` VARCHAR(128) NOT NULL,
  `client_id` VARCHAR(128) NOT NULL,
  `client_name` VARCHAR(128) NOT NULL  
   )
ENGINE = InnoDB;


CREATE 
   OR REPLACE
VIEW `gis_contractors_view` AS
    SELECT 
        `gis`.`global_invoice_setup_contractor_id`,
         `gis`.`global_invoice_setup_id`,
        `gis`.`contractor_id`,
        `gis`.`contractor_name`,
        `gis`.`file_number`,
        `gis`.`po_number`,
        `gis`.`client_id`,
        `gis`.`client_name`
    FROM
        `global_invoice_setup_contractor` `gis`;








