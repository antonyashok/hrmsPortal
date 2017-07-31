CREATE 
   OR REPLACE
VIEW `empl_prfl` AS
    SELECT DISTINCT
        `e`.`empl_id` AS `empl_id`,
        `e`.`emp_rl_id` AS `emp_role_id`,
        `e`.`designation_id` AS `designation_id`,
        `er`.`emp_rl_nm` AS `empl_role_nm`,
        `e`.`file_no` AS `file_no`,
        `e`.`mgr_empl_id` AS `mgr_empl_id`,
        `e`.`first_nm` AS `first_name`,
        `e`.`last_nm` AS `last_name`,
        `e`.`empl_strt_dt` AS `joining_dt`,
        `e`.`employee_profile_image_id` AS `employee_profile_image_id`,
        CONCAT(`e`.`first_nm`, ' ',`e`.`last_nm`) AS `full_name`,
        (SELECT 
                `cm`.`cntct_info`
            FROM
                (`employee_comm_info` `ecm`
                JOIN `comm_info` `cm`)
            WHERE
                ((`cm`.`comm_info_id` = `ecm`.`comm_info_id`)
                    AND (`ecm`.`empl_id` = `e`.`empl_id`)
                    AND (`cm`.`actv_flg` = 'Y')
                    AND (`cm`.`cntct_typ` = 'email')
                    AND (`ecm`.`actv_flg` = 'Y'))) AS `empl_email_id`,
        `e`.`ofc_id` AS `ofc_id`,
        `ofc1`.`ofc_nm` AS `ofc_name`,
        (SELECT 
                `tm1`.`zone_name`
            FROM
                (`ofc_timezone` `otm1`
                JOIN `timezone` `tm1`)
            WHERE
                ((`otm1`.`timezone_id` = `tm1`.`timezone_id`)
                    AND (`otm1`.`ofc_id` = `ofc1`.`ofc_id`))) AS `tm_zone`,
        `e`.`hourly_flg` AS `hourly_flg`,
        `e`.`empl_typ` AS `empl_type`,
        (CASE
            WHEN (`e`.`hourly_flg` = 'Y') THEN 'RCTR'
            ELSE 'NON-RCTR'
        END) AS `empl_category`,
        `addr`.`st_prv_id` AS `st_prv_id`,
        `st`.`st_prv_nm` AS `st_prv_nm`,
        `addr`.`cntry_id` AS `country_id`,
        `cntry`.`cntry_nm` AS `cntry_nm`,
        `t1`.`empl_id` AS `rep_mgr_id`,
        `t1`.`actv_flg` AS `rep_actv_flg`,
        `t1`.`emp_rl_id` AS `rep_mgr_rolel_id`,
        `er1`.`emp_rl_nm` AS `rep_mgr_role_name`,
        `t1`.`ofc_id` AS `rep_mgr_ofc_id`,
        `ofc2`.`ofc_nm` AS `rep_mgr_ofc_name`,
        CONCAT(`t1`.`first_nm`, ' ', `t1`.`last_nm`) AS `rep_mgr_full_name`,
        (SELECT 
                `cm1`.`cntct_info`
            FROM
                (`employee_comm_info` `ecm1`
                JOIN `comm_info` `cm1`)
            WHERE
                ((`cm1`.`comm_info_id` = `ecm1`.`comm_info_id`)
                    AND (`ecm1`.`empl_id` = `t1`.`empl_id`)
                    AND (`cm1`.`actv_flg` = 'Y')
                    AND (`cm1`.`cntct_typ` = 'email')
                    AND (`ecm1`.`actv_flg` = 'Y'))) AS `rep_mgr_email_id`,
        `t2`.`empl_id` AS `sales_mgr_empl_id`,
        `t2`.`actv_flg` AS `sales_mgr_empl_actv_flg`,
        `t2`.`emp_rl_id` AS `sales_mgr_role_id`,
        `er2`.`emp_rl_nm` AS `sales_mgr_role_name`,
        `t2`.`ofc_id` AS `sales_mgr_ofc_id`,
        `ofc3`.`ofc_nm` AS `sales_mgr_ofc_name`,
        `t2`.`mgr_empl_id` AS `sales_mgr_rep_empl_id`,
        CONCAT(`t2`.`first_nm`, `t2`.`last_nm`) AS `sales_mgr_full_name`,
        `jtr`.`job_title_id` AS `job_title_id`,
        `jtr`.`job_title_rl_id` AS `job_title_rl_id`,
        `jt`.`job_title` AS `job_title`,
        `tz`.`zone_name` AS `sales_mgr_tz`,
        (SELECT 
                `cm2`.`cntct_info`
            FROM
                (`employee_comm_info` `ecm2`
                JOIN `comm_info` `cm2`)
            WHERE
                ((`cm2`.`comm_info_id` = `ecm2`.`comm_info_id`)
                    AND (`ecm2`.`empl_id` = `t2`.`empl_id`)
                    AND (`cm2`.`actv_flg` = 'Y')
                    AND (`cm2`.`cntct_typ` = 'email')
                    AND (`ecm2`.`actv_flg` = 'Y'))) AS `sales_mgr_email_id`
    FROM
        ((((((((((((((((`employee` `e`
        LEFT JOIN `employee` `t1` ON ((`e`.`mgr_empl_id` = `t1`.`empl_id`)))
        JOIN `office` `ofc1` ON ((`ofc1`.`ofc_id` = `e`.`ofc_id`)))
        LEFT JOIN `office` `ofc2` ON ((`ofc2`.`ofc_id` = `t1`.`ofc_id`)))
        JOIN `employee_role` `er` ON ((`er`.`emp_rl_id` = `e`.`emp_rl_id`)))
        LEFT JOIN `job_title_role` `jtr` ON ((`jtr`.`emp_rl_id` = `e`.`emp_rl_id`)))
        LEFT JOIN `job_title` `jt` ON ((`jt`.`job_title_id` = `jtr`.`job_title_id`)))
        LEFT JOIN `employee_role` `er1` ON ((`er1`.`emp_rl_id` = `t1`.`emp_rl_id`)))
        LEFT JOIN `employee` `t2` ON ((`t1`.`mgr_empl_id` = `t2`.`empl_id`)))
        LEFT JOIN `employee_role` `er2` ON ((`er2`.`emp_rl_id` = `t2`.`emp_rl_id`)))
        LEFT JOIN `office` `ofc3` ON ((`ofc3`.`ofc_id` = `t2`.`ofc_id`)))
        JOIN `office_address` `ofc_addr` ON ((`ofc_addr`.`ofc_id` = `e`.`ofc_id`)))
        JOIN `address` `addr` ON ((`addr`.`addr_id` = `ofc_addr`.`addr_id`)))
        LEFT JOIN `state_province` `st` ON ((`st`.`st_prv_id` = `addr`.`st_prv_id`)))
        LEFT JOIN `country` `cntry` ON ((`cntry`.`cntry_id` = `addr`.`cntry_id`)))
        LEFT JOIN `ofc_timezone` `ot1` ON ((`ofc3`.`ofc_id` = `ot1`.`ofc_id`)))
        LEFT JOIN `timezone` `tz` ON ((`tz`.`timezone_id` = `ot1`.`timezone_id`)))
    WHERE
        ((`e`.`actv_flg` = 'Y')
            AND (`ofc_addr`.`actv_flg` = 'Y')
            AND (`addr`.`actv_flg` = 'Y'))
