create or replace view invoice.gis_grid_view as
select 	
    gis.global_inv_setup_id,
    gis.inv_setup_name,
    "" as clientName,
    Date(gis.created_on) as created_date,
    gis.inv_status
from
	invoice.global_invoice_setup gis
where gis.actv_flg = 'Y';


create or replace view invoice.gis_contractors_view as
SELECT 
	ctd.employeeid,   
    
    ctd.employeename , 
	ctd.filenumber, 
    ctd.ponumber,
	0 as clientId,
    '' as clientName
FROM   
	invoice.contractor_tab_details  ctd;








