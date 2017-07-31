
use common;

insert into common.entity_lst(entity_id,entity_name) values('fef03902-7dc0-459d-9c6d-8b1f0660d6ea','GLOBAL_INVOICE_SETUP');

insert into common.attribute_lst(attrib_id, attrib_name, data_type) values ('d8c6d3c9-a8dc-437d-93cb-88690cbabe22', 'LINE_OF_BUSINESS', 'VARCHAR');
insert into common.attribute_lst(attrib_id, attrib_name, data_type) values ('e5ef3d97-a720-4b40-8aec-93a29e2b98d7', 'PAY_CURRENCY', 'VARCHAR');
insert into common.attribute_lst(attrib_id, attrib_name, data_type) values ('06c65d52-66d6-4193-8555-8ddaf7d9b1fb', 'TERMS', 'VARCHAR');
insert into common.attribute_lst(attrib_id, attrib_name, data_type) values ('7c031733-46d0-41f3-9952-84c66d6daced', 'DELIVERY', 'VARCHAR');
insert into common.attribute_lst(attrib_id, attrib_name, data_type) values ('f0136412-e016-4790-9d97-905dcad9de56', 'OPTIONS', 'VARCHAR');


insert into common.entity_attrib_map(entity_attrib_map_id, entity_id, attrib_id, attrib_val, actv_flg, seq_no) values ('fc5af4a0-c389-465a-a9da-408a316b7c41','fef03902-7dc0-459d-9c6d-8b1f0660d6ea','d8c6d3c9-a8dc-437d-93cb-88690cbabe22','Telecom','Y',1);
insert into common.entity_attrib_map(entity_attrib_map_id, entity_id, attrib_id, attrib_val, actv_flg, seq_no) values ('1ce86fd0-f0a9-48bc-bc2c-7b41cde4ba3e','fef03902-7dc0-459d-9c6d-8b1f0660d6ea','d8c6d3c9-a8dc-437d-93cb-88690cbabe22','Finance','Y',2);
insert into common.entity_attrib_map(entity_attrib_map_id, entity_id, attrib_id, attrib_val, actv_flg, seq_no) values ('7d7de91e-c69e-4570-bd57-9a6d57c0b9ae','fef03902-7dc0-459d-9c6d-8b1f0660d6ea','d8c6d3c9-a8dc-437d-93cb-88690cbabe22','Healthcare','Y',3);
insert into common.entity_attrib_map(entity_attrib_map_id, entity_id, attrib_id, attrib_val, actv_flg, seq_no) values ('a9bfe311-f2c5-417f-b0cf-1cc6b5657c0e','fef03902-7dc0-459d-9c6d-8b1f0660d6ea','d8c6d3c9-a8dc-437d-93cb-88690cbabe22','Banking','Y',4);

insert into common.entity_attrib_map(entity_attrib_map_id, entity_id, attrib_id, attrib_val, actv_flg, seq_no) values ('114e6dbd-7c65-4cd2-af7a-1a0cefa31d2f','fef03902-7dc0-459d-9c6d-8b1f0660d6ea','e5ef3d97-a720-4b40-8aec-93a29e2b98d7','USD','Y',1);
insert into common.entity_attrib_map(entity_attrib_map_id, entity_id, attrib_id, attrib_val, actv_flg, seq_no) values ('715c5b7e-868a-4343-acd6-194249f1ab07','fef03902-7dc0-459d-9c6d-8b1f0660d6ea','e5ef3d97-a720-4b40-8aec-93a29e2b98d7','CAD','Y',2);


insert into common.entity_attrib_map(entity_attrib_map_id, entity_id, attrib_id, attrib_val, actv_flg, seq_no) values ('ee074d0a-2042-49c1-9f82-7b5ee1a661b3','fef03902-7dc0-459d-9c6d-8b1f0660d6ea','06c65d52-66d6-4193-8555-8ddaf7d9b1fb','1.5% 10 Net 30','Y',1);
insert into common.entity_attrib_map(entity_attrib_map_id, entity_id, attrib_id, attrib_val, actv_flg, seq_no) values ('e706142d-4099-4986-9066-22f631b6b44e','fef03902-7dc0-459d-9c6d-8b1f0660d6ea','06c65d52-66d6-4193-8555-8ddaf7d9b1fb','Due on Receipt','Y',2);
insert into common.entity_attrib_map(entity_attrib_map_id, entity_id, attrib_id, attrib_val, actv_flg, seq_no) values ('33c18248-8c8b-4889-a59d-2d2132a9effd','fef03902-7dc0-459d-9c6d-8b1f0660d6ea','06c65d52-66d6-4193-8555-8ddaf7d9b1fb','Net 10','Y',3);
insert into common.entity_attrib_map(entity_attrib_map_id, entity_id, attrib_id, attrib_val, actv_flg, seq_no) values ('1c0c205f-d52d-4b4e-b48f-93b6ebbedee9','fef03902-7dc0-459d-9c6d-8b1f0660d6ea','06c65d52-66d6-4193-8555-8ddaf7d9b1fb','Net 120','Y',4);
insert into common.entity_attrib_map(entity_attrib_map_id, entity_id, attrib_id, attrib_val, actv_flg, seq_no) values ('c4daec18-45a6-466b-8e5d-5dbbe54b5b58','fef03902-7dc0-459d-9c6d-8b1f0660d6ea','06c65d52-66d6-4193-8555-8ddaf7d9b1fb','Net 14','Y',5);
insert into common.entity_attrib_map(entity_attrib_map_id, entity_id, attrib_id, attrib_val, actv_flg, seq_no) values ('4866e967-23d1-4cdd-ae64-bf5217db48b9','fef03902-7dc0-459d-9c6d-8b1f0660d6ea','06c65d52-66d6-4193-8555-8ddaf7d9b1fb','Net 15','Y',6);
insert into common.entity_attrib_map(entity_attrib_map_id, entity_id, attrib_id, attrib_val, actv_flg, seq_no) values ('84873d1b-900e-410c-9b38-b65765e3fc2d','fef03902-7dc0-459d-9c6d-8b1f0660d6ea','06c65d52-66d6-4193-8555-8ddaf7d9b1fb','Net 180','Y',7);
insert into common.entity_attrib_map(entity_attrib_map_id, entity_id, attrib_id, attrib_val, actv_flg, seq_no) values ('ba6297df-f40c-4870-9609-5b461fbe34c6','fef03902-7dc0-459d-9c6d-8b1f0660d6ea','06c65d52-66d6-4193-8555-8ddaf7d9b1fb','Net 20','Y',8);
insert into common.entity_attrib_map(entity_attrib_map_id, entity_id, attrib_id, attrib_val, actv_flg, seq_no) values ('2a07d5b8-a1a7-48e9-9ad6-432a079e1fa2','fef03902-7dc0-459d-9c6d-8b1f0660d6ea','06c65d52-66d6-4193-8555-8ddaf7d9b1fb','Net 25','Y',9);
insert into common.entity_attrib_map(entity_attrib_map_id, entity_id, attrib_id, attrib_val, actv_flg, seq_no) values ('8ecc91e2-857b-4c70-8f5a-c27e067f1f3f','fef03902-7dc0-459d-9c6d-8b1f0660d6ea','06c65d52-66d6-4193-8555-8ddaf7d9b1fb','Net 30','Y',10);
insert into common.entity_attrib_map(entity_attrib_map_id, entity_id, attrib_id, attrib_val, actv_flg, seq_no) values ('9ec1c1f9-ca7c-4dac-873e-f15b8040c9be','fef03902-7dc0-459d-9c6d-8b1f0660d6ea','06c65d52-66d6-4193-8555-8ddaf7d9b1fb','Net 35','Y',11);
insert into common.entity_attrib_map(entity_attrib_map_id, entity_id, attrib_id, attrib_val, actv_flg, seq_no) values ('57d2a348-ac72-409e-a4ab-877681225893','fef03902-7dc0-459d-9c6d-8b1f0660d6ea','06c65d52-66d6-4193-8555-8ddaf7d9b1fb','Net 37','Y',12);
insert into common.entity_attrib_map(entity_attrib_map_id, entity_id, attrib_id, attrib_val, actv_flg, seq_no) values ('2eb8666a-603a-4b30-9064-69997217faf8','fef03902-7dc0-459d-9c6d-8b1f0660d6ea','06c65d52-66d6-4193-8555-8ddaf7d9b1fb','Net 40','Y',13);
insert into common.entity_attrib_map(entity_attrib_map_id, entity_id, attrib_id, attrib_val, actv_flg, seq_no) values ('c17a5b4c-bf66-459a-998a-073d06a6ab46','fef03902-7dc0-459d-9c6d-8b1f0660d6ea','06c65d52-66d6-4193-8555-8ddaf7d9b1fb','Net 45','Y',14);
insert into common.entity_attrib_map(entity_attrib_map_id, entity_id, attrib_id, attrib_val, actv_flg, seq_no) values ('68fc074e-e488-4029-8d21-b1b3bd661c42','fef03902-7dc0-459d-9c6d-8b1f0660d6ea','06c65d52-66d6-4193-8555-8ddaf7d9b1fb','Net 48','Y',15);
insert into common.entity_attrib_map(entity_attrib_map_id, entity_id, attrib_id, attrib_val, actv_flg, seq_no) values ('d75f39d0-d9f7-471a-ae3b-7079ae256a28','fef03902-7dc0-459d-9c6d-8b1f0660d6ea','06c65d52-66d6-4193-8555-8ddaf7d9b1fb','Net 50','Y',16);
insert into common.entity_attrib_map(entity_attrib_map_id, entity_id, attrib_id, attrib_val, actv_flg, seq_no) values ('475a97d7-fabf-43bf-a86a-5af842a567ce','fef03902-7dc0-459d-9c6d-8b1f0660d6ea','06c65d52-66d6-4193-8555-8ddaf7d9b1fb','Net 55','Y',17);
insert into common.entity_attrib_map(entity_attrib_map_id, entity_id, attrib_id, attrib_val, actv_flg, seq_no) values ('02ee22a5-d067-48e0-b67f-32313bacb992','fef03902-7dc0-459d-9c6d-8b1f0660d6ea','06c65d52-66d6-4193-8555-8ddaf7d9b1fb','Net 60','Y',18);
insert into common.entity_attrib_map(entity_attrib_map_id, entity_id, attrib_id, attrib_val, actv_flg, seq_no) values ('8945173b-58bb-408b-9b47-3f3ba0507c6f','fef03902-7dc0-459d-9c6d-8b1f0660d6ea','06c65d52-66d6-4193-8555-8ddaf7d9b1fb','Net 65','Y',19);
insert into common.entity_attrib_map(entity_attrib_map_id, entity_id, attrib_id, attrib_val, actv_flg, seq_no) values ('857a8080-df36-4f35-846a-0797ce39e534','fef03902-7dc0-459d-9c6d-8b1f0660d6ea','06c65d52-66d6-4193-8555-8ddaf7d9b1fb','Net 67','Y',20);
insert into common.entity_attrib_map(entity_attrib_map_id, entity_id, attrib_id, attrib_val, actv_flg, seq_no) values ('297cac2c-54a2-48fd-b7ad-37d4004e2d84','fef03902-7dc0-459d-9c6d-8b1f0660d6ea','06c65d52-66d6-4193-8555-8ddaf7d9b1fb','Net 7','Y',21);
insert into common.entity_attrib_map(entity_attrib_map_id, entity_id, attrib_id, attrib_val, actv_flg, seq_no) values ('a42e65bb-7e74-4f9c-b1fe-fa571dc81e4e','fef03902-7dc0-459d-9c6d-8b1f0660d6ea','06c65d52-66d6-4193-8555-8ddaf7d9b1fb','Net 75','Y',22);
insert into common.entity_attrib_map(entity_attrib_map_id, entity_id, attrib_id, attrib_val, actv_flg, seq_no) values ('57749cc6-68fb-4eeb-b648-8f4fc54d5ed7','fef03902-7dc0-459d-9c6d-8b1f0660d6ea','06c65d52-66d6-4193-8555-8ddaf7d9b1fb','Net 90','Y',23);
insert into common.entity_attrib_map(entity_attrib_map_id, entity_id, attrib_id, attrib_val, actv_flg, seq_no) values ('92c6e09d-8760-4c5f-96cc-f5ed1668283e','fef03902-7dc0-459d-9c6d-8b1f0660d6ea','06c65d52-66d6-4193-8555-8ddaf7d9b1fb','Net 95','Y',24);

insert into common.entity_attrib_map(entity_attrib_map_id, entity_id, attrib_id, attrib_val, actv_flg, seq_no) values ('9fb5f4bc-067f-4857-9cd4-ada2400acb77','fef03902-7dc0-459d-9c6d-8b1f0660d6ea','7c031733-46d0-41f3-9952-84c66d6daced','Auto Delivery','Y',1);
insert into common.entity_attrib_map(entity_attrib_map_id, entity_id, attrib_id, attrib_val, actv_flg, seq_no) values ('973bb0e9-ff1c-4537-96fd-e3968ddb109b','fef03902-7dc0-459d-9c6d-8b1f0660d6ea','7c031733-46d0-41f3-9952-84c66d6daced','Email','Y',2);
insert into common.entity_attrib_map(entity_attrib_map_id, entity_id, attrib_id, attrib_val, actv_flg, seq_no) values ('7c9cc6c2-1457-4864-b387-1ae31f9d0a93','fef03902-7dc0-459d-9c6d-8b1f0660d6ea','7c031733-46d0-41f3-9952-84c66d6daced','USPS','Y',3);
insert into common.entity_attrib_map(entity_attrib_map_id, entity_id, attrib_id, attrib_val, actv_flg, seq_no) values ('37db66ba-3146-481a-bb5e-75357896a88a','fef03902-7dc0-459d-9c6d-8b1f0660d6ea','7c031733-46d0-41f3-9952-84c66d6daced','CSV','Y',4);
insert into common.entity_attrib_map(entity_attrib_map_id, entity_id, attrib_id, attrib_val, actv_flg, seq_no) values ('9c5b08d1-1789-4bfe-adfa-9747805f4b7f','fef03902-7dc0-459d-9c6d-8b1f0660d6ea','7c031733-46d0-41f3-9952-84c66d6daced','Email + CSV','Y',5);
insert into common.entity_attrib_map(entity_attrib_map_id, entity_id, attrib_id, attrib_val, actv_flg, seq_no) values ('a34e18c7-ab31-48b6-899e-bbb5ebec6038','fef03902-7dc0-459d-9c6d-8b1f0660d6ea','7c031733-46d0-41f3-9952-84c66d6daced','Email & Online','Y',6);


insert into common.entity_attrib_map(entity_attrib_map_id, entity_id, attrib_id, attrib_val, actv_flg, seq_no) values ('3694691a-736e-4b3b-94c7-77df3d40842d','fef03902-7dc0-459d-9c6d-8b1f0660d6ea','f0136412-e016-4790-9d97-905dcad9de56','Default to Billing Profile PO','Y',1);
insert into common.entity_attrib_map(entity_attrib_map_id, entity_id, attrib_id, attrib_val, actv_flg, seq_no) values ('d62c59b7-a4b7-4356-ab4b-d3a125697b85','fef03902-7dc0-459d-9c6d-8b1f0660d6ea','f0136412-e016-4790-9d97-905dcad9de56','Include Timesheet in invoice','Y',2);
insert into common.entity_attrib_map(entity_attrib_map_id, entity_id, attrib_id, attrib_val, actv_flg, seq_no) values ('d2ddbe35-94e4-4a3a-a106-fba50512cf6d','fef03902-7dc0-459d-9c6d-8b1f0660d6ea','f0136412-e016-4790-9d97-905dcad9de56','Exclude Contractor Name','Y',3);
insert into common.entity_attrib_map(entity_attrib_map_id, entity_id, attrib_id, attrib_val, actv_flg, seq_no) values ('a193773e-3ab6-4232-ba48-1609ab972729','fef03902-7dc0-459d-9c6d-8b1f0660d6ea','f0136412-e016-4790-9d97-905dcad9de56','Generate Seperate Invoice for Overtime','Y',4);
insert into common.entity_attrib_map(entity_attrib_map_id, entity_id, attrib_id, attrib_val, actv_flg, seq_no) values ('f803a31d-7af2-4b27-b44f-17a9f43c0aa3','fef03902-7dc0-459d-9c6d-8b1f0660d6ea','f0136412-e016-4790-9d97-905dcad9de56','Show Hiring Manager On Invoice','Y',5);
insert into common.entity_attrib_map(entity_attrib_map_id, entity_id, attrib_id, attrib_val, actv_flg, seq_no) values ('871c1dae-30ec-4bad-99b0-b75f57726cbb','fef03902-7dc0-459d-9c6d-8b1f0660d6ea','f0136412-e016-4790-9d97-905dcad9de56','Invoice all Subproject as single line item','Y',6);
insert into common.entity_attrib_map(entity_attrib_map_id, entity_id, attrib_id, attrib_val, actv_flg, seq_no) values ('7ef96e4d-8033-4184-8108-f94f961aa15f','fef03902-7dc0-459d-9c6d-8b1f0660d6ea','f0136412-e016-4790-9d97-905dcad9de56','Invoice all Subproject as Different line item','Y',7);
insert into common.entity_attrib_map(entity_attrib_map_id, entity_id, attrib_id, attrib_val, actv_flg, seq_no) values ('14584ed3-2bb2-4eed-afb8-ad83082cd4df','fef03902-7dc0-459d-9c6d-8b1f0660d6ea','f0136412-e016-4790-9d97-905dcad9de56','Invoice all Subproject as Different Invoice','Y',8);
insert into common.entity_attrib_map(entity_attrib_map_id, entity_id, attrib_id, attrib_val, actv_flg, seq_no) values ('37b999a0-4afd-496e-877a-a46b6c91445c','fef03902-7dc0-459d-9c6d-8b1f0660d6ea','f0136412-e016-4790-9d97-905dcad9de56','Include Expenses','Y',9);
insert into common.entity_attrib_map(entity_attrib_map_id, entity_id, attrib_id, attrib_val, actv_flg, seq_no) values ('9bd3e2eb-01fb-45d0-99d3-6b3f37ce6841','fef03902-7dc0-459d-9c6d-8b1f0660d6ea','f0136412-e016-4790-9d97-905dcad9de56','Include Expense Documentation','Y',10);


create or replace view common.gis_entity_attributes_view as 
select 
	eamap.entity_attrib_map_id,
	eamap.attrib_id,
    attrib.attrib_name,
    eamap.attrib_val
from
	common.entity_attrib_map eamap
inner join
	common.attribute_lst attrib
on eamap.attrib_id = attrib.attrib_id
inner join
	common.entity_lst entity
on eamap.entity_id = entity.entity_id and entity.entity_name = 'GLOBAL_INVOICE_SETUP'
where eamap.actv_flg = 'Y' 
order by attrib.attrib_name, eamap.seq_no;


 



