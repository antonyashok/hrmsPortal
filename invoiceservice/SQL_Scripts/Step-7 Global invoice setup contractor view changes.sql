use invoice;

CREATE OR REPLACE    
VIEW `gis_contractors_view` AS
    SELECT 
	 ctd. contractor_tab_id as globalInvoiceSetupId,
        `ctd`.`employeeId` AS `employeeid`,
        `ctd`.`employeeName` AS `employeename`,
        `ctd`.`fileNumber` AS `filenumber`,
        `ctd`.`poNumber` AS `ponumber`,
        0 AS `clientId`,
        '' AS `clientName`
    FROM
        `contractor_tab_details` `ctd`;

use common;

ALTER TABLE `common`.`employee` 
DROP FOREIGN KEY `em_mgr_fk`;

create or replace view invoice.gis_grid_view as
select 	
    gis.global_inv_setup_id,
    gis.inv_setup_name,
    "" as client_name,
    Date(gis.created_on) as created_date,
    gis.inv_status
from
	invoice.global_invoice_setup gis
where gis.actv_flg = 'Y';

