SET NAMES utf8mb4;
DELETE FROM teacher_portrait.paper_index;
DELETE FROM teacher_portrait.paper;
DELETE FROM teacher_portrait.patent_transfer;
DELETE FROM teacher_portrait.patent;
DELETE FROM teacher_portrait.software_copyright;
DELETE FROM teacher_portrait.horizontal_project;
DELETE FROM teacher_portrait.vertical_project;
DELETE FROM teacher_portrait.competition;

-- ===== T001 张教授 (userId=2) — 5维度全覆盖 =====
INSERT INTO teacher_portrait.vertical_project (user_id, name, project_no, level, source_unit, start_date, planned_end_date, funding, role, status) VALUES
(2,'深度学习研究','VP-2024-001','国家级','国家自然科学基金','2024-03-01','2026-12-31',80,'主持','在研'),
(2,'大数据平台建设','VP-2024-002','省部级','省科技厅','2024-06-01','2027-06-30',30,'主持','在研'),
(2,'智慧校园系统','VP-2023-001','市厅级','市教育局','2023-01-01','2024-12-31',10,'参与','已结题'),
(2,'校级教改项目','VP-2025-001','校级','本校','2025-01-01','2026-06-30',5,'主持','在研');

INSERT INTO teacher_portrait.patent (user_id, name, type, application_no, grant_no, application_date, grant_date, status, inventors, patentee, is_counted) VALUES
(2,'图像识别方法','发明专利','CN-A001','ZL-A001','2024-01-01','2025-06-01','已授权','张教授;李博士','XX大学',1),
(2,'传感器装置','实用新型','CN-U001','','2025-03-01',NULL,'申请中','张教授;王同学','XX大学',1);

INSERT INTO teacher_portrait.software_copyright (user_id, name, registration_no, version, dev_completion_date, first_publish_date, registration_date, copyright_owners) VALUES
(2,'教育管理系统','SR-2024-001','V2.0','2024-05-01','2024-06-01','2024-08-01','张教授'),
(2,'数据分析工具','SR-2025-001','V1.0','2025-01-01','2025-03-01','2025-04-15','张教授;李博士');

INSERT INTO teacher_portrait.paper (user_id, title, type, journal_name, publish_date, authors, author_order) VALUES
(2,'AI in Education','期刊论文','SCI Journal','2024-06-01','张教授;王博士',1);
SET @pid1 = LAST_INSERT_ID();
INSERT INTO teacher_portrait.paper_index (paper_id, index_type) VALUES (@pid1,'SCI');
INSERT INTO teacher_portrait.paper (user_id, title, type, journal_name, publish_date, authors, author_order) VALUES
(2,'Big Data Mining','期刊论文','EI Conference','2024-09-01','李博士;张教授',2);
SET @pid2 = LAST_INSERT_ID();
INSERT INTO teacher_portrait.paper_index (paper_id, index_type) VALUES (@pid2,'EI');
INSERT INTO teacher_portrait.paper (user_id, title, type, journal_name, publish_date, authors, author_order) VALUES
(2,'General Survey','期刊论文','General Journal','2023-03-01','张教授',1);
SET @pid3 = LAST_INSERT_ID();
INSERT INTO teacher_portrait.paper_index (paper_id, index_type) VALUES (@pid3,'普通期刊');

INSERT INTO teacher_portrait.competition (user_id, name, organizer, competition_date, student_team, award_level, award_grade, guide_rank) VALUES
(2,'全国大学生AI大赛','教育部','2024-07-01','王同学;李同学','国家级','一等奖',1),
(2,'省大学生创新赛','省教育厅','2025-04-01','赵同学','省级','二等奖',1);

-- ===== T002 李教授 (userId=3) — 偏专利+软件方向 =====
INSERT INTO teacher_portrait.vertical_project (user_id, name, project_no, level, source_unit, start_date, planned_end_date, funding, role, status) VALUES
(3,'物联网平台','VP-2024-003','省部级','省科技厅','2024-03-01','2026-12-31',40,'主持','在研'),
(3,'云计算研究','VP-2023-002','市厅级','市教育局','2023-06-01','2025-06-30',15,'主持','在研');

INSERT INTO teacher_portrait.patent (user_id, name, type, application_no, grant_no, application_date, grant_date, status, inventors, patentee, is_counted) VALUES
(3,'智能传感网络','发明专利','CN-B001','ZL-B001','2023-06-01','2024-12-01','已授权','李教授','XX大学',1),
(3,'无线通信模块','外观设计','CN-D001','','2025-01-01',NULL,'申请中','李教授','XX大学',1);

INSERT INTO teacher_portrait.software_copyright (user_id, name, registration_no, version, dev_completion_date, first_publish_date, registration_date, copyright_owners) VALUES
(3,'物联网管理系统','SR-2024-002','V1.0','2024-03-01','2024-05-01','2024-07-01','李教授'),
(3,'实验室管理平台','SR-2025-002','V2.0','2025-02-01','2025-04-01','2025-06-01','李教授'),
(3,'设备监控软件','SR-2023-001','V1.0','2023-08-01','2023-10-01','2023-12-01','李教授;张教授');

INSERT INTO teacher_portrait.paper (user_id, title, type, journal_name, publish_date, authors, author_order) VALUES
(3,'IoT Security','期刊论文','EI Journal','2024-08-01','李教授;王博士',1);
SET @pid4 = LAST_INSERT_ID();
INSERT INTO teacher_portrait.paper_index (paper_id, index_type) VALUES (@pid4,'EI');
INSERT INTO teacher_portrait.paper (user_id, title, type, journal_name, publish_date, authors, author_order) VALUES
(3,'Wireless Survey','期刊论文','普通期刊','2025-01-01','王博士;李教授',2);
SET @pid5 = LAST_INSERT_ID();
INSERT INTO teacher_portrait.paper_index (paper_id, index_type) VALUES (@pid5,'普通期刊');

INSERT INTO teacher_portrait.competition (user_id, name, organizer, competition_date, student_team, award_level, award_grade, guide_rank) VALUES
(3,'省创新挑战赛','省科技厅','2024-11-01','钱同学','省级','一等奖',1);
