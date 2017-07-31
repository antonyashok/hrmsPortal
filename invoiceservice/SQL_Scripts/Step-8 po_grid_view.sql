CREATE OR REPLACE VIEW `invoice`.`purchase_order_details_view` AS
    SELECT 
        `po`.`po_id` AS `po_customer_id`,
        `po`.`po_number` AS `po_number`,
        `po`.`po_id` AS `po_id`,
        `po`.`parent_po_id` AS `parent_po_id`,
        `po`.`start_date` AS `start_date`,
        `po`.`end_date` AS `end_date`,
        (`po`.`bal_rev_amt` - `po`.`bal_exp_amt`) AS `po_balance`,
        `po`.`updated_on` AS `last_updated_on`,
        `po`.`status` AS `status`,
        `po`.`actv_flg` AS `po_actv_flg`,
        `ci`.`customer_id` AS `customer_id`,
        `ci`.`customer_name` AS `customer_name`,
        `ci`.`active_flg` AS `customer_actv_flg`
    FROM
        (`invoice`.`purchase_order` `po`
        JOIN `engagement`.`customer_profile` `ci` ON (((`po`.`customer_id` = `ci`.`customer_id`)
            AND (`ci`.`active_flg` = 'Y'))))
