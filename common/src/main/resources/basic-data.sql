INSERT INTO hipstour.category(created_at, updated_at, category_name, description) VALUES(current_timestamp(),current_timestamp(),"all","all category");

INSERT INTO hipstour.region_info(created_at, updated_at, area_code, region_name) VALUES(current_timestamp(), current_timestamp(), 1, "Seoul");

INSERT INTO hipstour.place
(created_at, updated_at, address1, latitude, longitude, place_name, tel_number, region_id)
VALUES(CURRENT_TIMESTAMP(6), CURRENT_TIMESTAMP(6), 'seoul', 10, 20, 'seoul playground', '02-0000-0000', 1);

INSERT INTO hipstour.place
(created_at, updated_at, address1, latitude, longitude, place_name, tel_number, region_id)
VALUES(CURRENT_TIMESTAMP(6), CURRENT_TIMESTAMP(6), 'seoul', 10, 20, 'seoul matjib', '02-0000-0000', 1);

INSERT INTO hipstour.place
(created_at, updated_at, address1, latitude, longitude, place_name, tel_number, region_id)
VALUES(CURRENT_TIMESTAMP(6), CURRENT_TIMESTAMP(6), 'seoul', 10, 20, 'seoul good cafe', '02-0000-0000', 1);

INSERT INTO hipstour.role_info(created_at, updated_at, description)VALUES(now(6), now(6), "root user");

INSERT INTO hipstour.task_info(created_at, updated_at, description)VALUES( now(6), now(6), "root");

INSERT INTO hipstour.role_task_info
(created_at, updated_at, role_id, task_id)
VALUES(now(6), now(6), 1, 1);