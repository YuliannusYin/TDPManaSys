SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;
ALTER DATABASE `teacher_portrait` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `teacher_portrait`;

-- ============================================================
-- 教师数字画像系统 - 测试数据脚本
-- 生成日期: 2026-05-13
-- 包含 5 位教师的完整成果数据
-- 密码统一为 123456 (BCrypt)
-- ============================================================

-- ============================================================
-- 0. 清理已有测试数据（支持重复执行）
-- ============================================================
SELECT id INTO @del1 FROM `user` WHERE work_no = 'T101';
SELECT id INTO @del2 FROM `user` WHERE work_no = 'T102';
SELECT id INTO @del3 FROM `user` WHERE work_no = 'T103';
SELECT id INTO @del4 FROM `user` WHERE work_no = 'T104';
SELECT id INTO @del5 FROM `user` WHERE work_no = 'T105';
DELETE FROM paper WHERE user_id IN (@del1, @del2, @del3, @del4, @del5);
DELETE FROM patent WHERE user_id IN (@del1, @del2, @del3, @del4, @del5);
DELETE FROM competition WHERE user_id IN (@del1, @del2, @del3, @del4, @del5);
DELETE FROM software_copyright WHERE user_id IN (@del1, @del2, @del3, @del4, @del5);
DELETE FROM horizontal_project WHERE user_id IN (@del1, @del2, @del3, @del4, @del5);
DELETE FROM vertical_project WHERE user_id IN (@del1, @del2, @del3, @del4, @del5);
DELETE FROM `user` WHERE work_no IN ('T101', 'T102', 'T103', 'T104', 'T105');

-- ============================================================
-- 1. 用户数据
-- ============================================================
INSERT INTO `user` (work_no, name, college, role, password) VALUES
('T101', '张三', '计算机学院', 'TEACHER', '$2b$10$K4hSd.4a/6mFOKq9sLtPReHri7Gc4TCtvgnjG3y0LtxOJNSt4pijy'),
('T102', '李四', '计算机学院', 'TEACHER', '$2b$10$K4hSd.4a/6mFOKq9sLtPReHri7Gc4TCtvgnjG3y0LtxOJNSt4pijy'),
('T103', '王五', '数学学院', 'TEACHER', '$2b$10$K4hSd.4a/6mFOKq9sLtPReHri7Gc4TCtvgnjG3y0LtxOJNSt4pijy'),
('T104', '赵六', '数学学院', 'TEACHER', '$2b$10$K4hSd.4a/6mFOKq9sLtPReHri7Gc4TCtvgnjG3y0LtxOJNSt4pijy'),
('T105', '钱七', '物理学院', 'TEACHER', '$2b$10$K4hSd.4a/6mFOKq9sLtPReHri7Gc4TCtvgnjG3y0LtxOJNSt4pijy');

SET @u1 = (SELECT id FROM `user` WHERE work_no = 'T101');
SET @u2 = (SELECT id FROM `user` WHERE work_no = 'T102');
SET @u3 = (SELECT id FROM `user` WHERE work_no = 'T103');
SET @u4 = (SELECT id FROM `user` WHERE work_no = 'T104');
SET @u5 = (SELECT id FROM `user` WHERE work_no = 'T105');

-- ============================================================
-- 2. 纵向项目数据
-- ============================================================
-- 张三 T101: 17条 (国家级4, 省部级5, 市厅级4, 校级4; 主持12, 参与5)
INSERT INTO vertical_project (user_id, name, project_no, level, source_unit, start_date, planned_end_date, funding, role, status, remark) VALUES
(@u1, '基于深度学习的图像语义分割关键技术研究', 'VP-T101-001', '国家级', '国家自然科学基金委员会', '2020-01-01', '2023-12-31', 180.00, '主持', '已结题', NULL),
(@u1, '面向边缘计算的高效神经网络压缩方法', 'VP-T101-002', '国家级', '科技部重点研发计划', '2022-06-01', '2025-05-31', 200.00, '主持', '在研', NULL),
(@u1, '多模态数据融合的智能感知系统研究', 'VP-T101-003', '国家级', '国家自然科学基金委员会', '2021-01-01', '2024-12-31', 85.00, '参与', '在研', NULL),
(@u1, '大规模图数据的分布式计算框架', 'VP-T101-004', '国家级', '国家自然科学基金委员会', '2023-01-01', '2026-12-31', 120.00, '主持', '在研', '重点项目'),
(@u1, '面向复杂场景的视频行为分析关键技术', 'VP-T101-005', '省部级', '福建省科技厅', '2023-03-01', '2025-02-28', 50.00, '主持', '在研', NULL),
(@u1, '智慧城市交通流量的时空预测模型', 'VP-T101-006', '省部级', '福建省自然科学基金', '2022-09-01', '2025-08-31', 35.00, '主持', '在研', NULL),
(@u1, '基于区块链的数据安全共享平台研发', 'VP-T101-007', '省部级', '福建省科技厅', '2021-06-01', '2023-05-31', 60.00, '主持', '已结题', NULL),
(@u1, '面向智能制造的数字孪生关键技术', 'VP-T101-008', '省部级', '福建省教育厅', '2021-10-01', '2024-09-30', 25.00, '参与', '已结题', NULL),
(@u1, '大数据驱动的教育质量评价模型', 'VP-T101-009', '省部级', '福建省自然科学基金', '2020-03-01', '2023-02-28', 40.00, '主持', '已结题', NULL),
(@u1, '云计算环境下虚拟资源调度优化算法研究', 'VP-T101-010', '市厅级', '福建省教育厅', '2024-01-01', '2026-12-31', 15.00, '主持', '在研', NULL),
(@u1, '基于知识图谱的智能问答系统', 'VP-T101-011', '市厅级', '福州市科技局', '2022-04-01', '2024-03-31', 8.00, '主持', '已结题', NULL),
(@u1, '面向金融风控的异常行为检测模型', 'VP-T101-012', '市厅级', '福州市科技局', '2021-07-01', '2023-06-30', 10.00, '参与', '已结题', NULL),
(@u1, '智能推荐系统的冷启动问题研究', 'VP-T101-013', '市厅级', '福州市科技局', '2023-05-01', '2025-04-30', 12.00, '主持', '在研', NULL),
(@u1, '基于深度强化学习的机器人导航系统', 'VP-T101-014', '校级', '福建师范大学科研基金', '2020-09-01', '2022-08-31', 5.00, '主持', '已结题', NULL),
(@u1, '面向物联网的隐私保护算法研究', 'VP-T101-015', '校级', '福建师范大学科研基金', '2022-03-01', '2024-02-28', 8.00, '主持', '延期', NULL),
(@u1, '自然语言处理在智慧教育中的应用', 'VP-T101-016', '校级', '福建师范大学科研基金', '2021-11-01', '2023-10-31', 6.00, '参与', '已结题', NULL),
(@u1, '多智能体协作的任务分配算法', 'VP-T101-017', '校级', '福建师范大学科研基金', '2024-02-01', '2026-01-31', 7.00, '主持', '在研', NULL);

-- 李四 T102: 6条 (国家级2, 省部级2, 市厅级1, 校级1; 主持4, 参与2)
INSERT INTO vertical_project (user_id, name, project_no, level, source_unit, start_date, planned_end_date, funding, role, status, remark) VALUES
(@u2, '面向自动驾驶的多传感器融合定位算法', 'VP-T102-001', '国家级', '国家自然科学基金委员会', '2022-01-01', '2025-12-31', 95.00, '主持', '在研', NULL),
(@u2, '大规模分布式存储系统的容错机制', 'VP-T102-002', '国家级', '科技部重点研发计划', '2021-09-01', '2024-08-31', 150.00, '参与', '已结题', NULL),
(@u2, '面向工业物联网的实时数据流处理', 'VP-T102-003', '省部级', '福建省科技厅', '2022-05-01', '2024-04-30', 45.00, '主持', '延期', NULL),
(@u2, '基于图神经网络的社交网络异常检测', 'VP-T102-004', '省部级', '福建省自然科学基金', '2021-04-01', '2024-03-31', 30.00, '主持', '已结题', NULL),
(@u2, '面向智慧医疗的联邦学习框架研究', 'VP-T102-005', '市厅级', '福州市科技局', '2023-08-01', '2025-07-31', 10.00, '主持', '在研', NULL),
(@u2, '课程知识图谱自动构建方法研究', 'VP-T102-006', '校级', '福建师范大学科研基金', '2023-03-01', '2025-02-28', 5.00, '参与', '在研', NULL);

-- 王五 T103: 10条 (国家级3, 省部级3, 市厅级2, 校级2; 主持7, 参与3)
INSERT INTO vertical_project (user_id, name, project_no, level, source_unit, start_date, planned_end_date, funding, role, status, remark) VALUES
(@u3, '高维数据降维的非线性流形学习算法', 'VP-T103-001', '国家级', '国家自然科学基金委员会', '2020-01-01', '2023-12-31', 65.00, '主持', '已结题', NULL),
(@u3, '基于随机矩阵理论的无线通信信道建模', 'VP-T103-002', '国家级', '国家自然科学基金委员会', '2022-01-01', '2025-12-31', 78.00, '主持', '在研', NULL),
(@u3, '面向金融量化交易的深度强化学习方法', 'VP-T103-003', '国家级', '国家自然科学基金委员会', '2023-01-01', '2026-12-31', 50.00, '参与', '在研', NULL),
(@u3, '偏微分方程在图像修复中的应用研究', 'VP-T103-004', '省部级', '福建省科技厅', '2022-07-01', '2025-06-30', 45.00, '主持', '在研', NULL),
(@u3, '复杂网络的动力学分析与同步控制', 'VP-T103-005', '省部级', '福建省自然科学基金', '2021-05-01', '2024-04-30', 32.00, '主持', '已结题', NULL),
(@u3, '基于深度学习的偏微分方程数值解法', 'VP-T103-006', '省部级', '福建省自然科学基金', '2023-04-01', '2026-03-31', 38.00, '主持', '在研', NULL),
(@u3, '面向智慧教育的个性化学习路径推荐', 'VP-T103-007', '市厅级', '福州市教育局', '2023-06-01', '2025-05-31', 8.00, '主持', '在研', NULL),
(@u3, '统计学方法在教学质量评价中的应用', 'VP-T103-008', '市厅级', '福州市科技局', '2022-03-01', '2024-02-28', 6.00, '参与', '延期', NULL),
(@u3, '概率图模型的理论改进与算法应用', 'VP-T103-009', '校级', '福建师范大学科研基金', '2021-10-01', '2023-09-30', 4.00, '主持', '已结题', NULL),
(@u3, '最优传输理论在机器学习中的应用', 'VP-T103-010', '校级', '福建师范大学科研基金', '2023-09-01', '2025-08-31', 6.00, '主持', '在研', NULL);

-- 赵六 T104: 3条 (国家级1, 省部级1, 校级1; 主持1, 参与2)
INSERT INTO vertical_project (user_id, name, project_no, level, source_unit, start_date, planned_end_date, funding, role, status, remark) VALUES
(@u4, '拓扑数据分析在生物信息学中的应用', 'VP-T104-001', '国家级', '国家自然科学基金委员会', '2021-01-01', '2024-12-31', 55.00, '参与', '已结题', NULL),
(@u4, '基于变分自编码器的生成模型改进', 'VP-T104-002', '省部级', '福建省自然科学基金', '2022-11-01', '2025-10-31', 28.00, '主持', '在研', NULL),
(@u4, '代数拓扑在数据科学中的前沿探索', 'VP-T104-003', '校级', '福建师范大学科研基金', '2022-06-01', '2024-05-31', 5.00, '参与', '延期', NULL);

-- 钱七 T105: 8条 (国家级2, 省部级2, 市厅级2, 校级2; 主持6, 参与2)
INSERT INTO vertical_project (user_id, name, project_no, level, source_unit, start_date, planned_end_date, funding, role, status, remark) VALUES
(@u5, '基于超导量子比特的量子计算纠错研究', 'VP-T105-001', '国家级', '国家自然科学基金委员会', '2022-01-01', '2025-12-31', 92.00, '主持', '在研', NULL),
(@u5, '二维材料异质结的光电特性理论研究', 'VP-T105-002', '国家级', '国家自然科学基金委员会', '2020-01-01', '2022-12-31', 60.00, '主持', '已结题', NULL),
(@u5, '基于机器学习的凝聚态物性预测', 'VP-T105-003', '省部级', '福建省科技厅', '2023-05-01', '2026-04-30', 42.00, '主持', '在研', NULL),
(@u5, '量子点太阳能电池的界面工程研究', 'VP-T105-004', '省部级', '福建省自然科学基金', '2022-08-01', '2025-07-31', 35.00, '参与', '延期', NULL),
(@u5, '飞秒激光与物质相互作用的超快动力学', 'VP-T105-005', '市厅级', '福建省教育厅', '2023-02-01', '2025-01-31', 15.00, '主持', '在研', NULL),
(@u5, '基于深度学习的X射线衍射图谱分析', 'VP-T105-006', '市厅级', '福州市科技局', '2022-10-01', '2024-09-30', 9.00, '主持', '已结题', NULL),
(@u5, '新型钙钛矿材料的电子结构计算', 'VP-T105-007', '校级', '福建师范大学科研基金', '2021-04-01', '2023-03-31', 5.00, '主持', '已结题', NULL),
(@u5, '声子晶体中弹性波的传播特性研究', 'VP-T105-008', '校级', '福建师范大学科研基金', '2023-07-01', '2025-06-30', 7.00, '主持', '在研', NULL);

-- ============================================================
-- 3. 横向项目数据
-- ============================================================
-- 张三 T101: 8条
INSERT INTO horizontal_project (user_id, name, company_name, contract_amount, sign_date, end_date, role, status, remark) VALUES
(@u1, '智慧校园大数据分析平台开发', '厦门云智教育科技有限公司', 80.00, '2021-03-15', '2023-03-14', '主持', '已结题', NULL),
(@u1, '企业ERP管理系统定制开发', '福州鸿远信息技术有限公司', 45.00, '2022-06-01', '2024-05-31', '主持', '在研', NULL),
(@u1, '基于AI的工业质量检测系统', '泉州智能制造集团有限公司', 120.00, '2023-01-10', '2025-01-09', '主持', '在研', NULL),
(@u1, '在线教育直播平台技术升级', '福建博学在线教育科技有限公司', 55.00, '2020-09-01', '2021-12-31', '主持', '已结题', NULL),
(@u1, '智慧社区安防监控系统开发', '福州安联智能科技有限公司', 38.00, '2021-11-01', '2022-10-31', '主持', '已结题', NULL),
(@u1, '区块链供应链金融平台咨询', '厦门金链科技有限公司', 30.00, '2023-08-01', '2024-07-31', '主持', '在研', NULL),
(@u1, '大数据驱动的客户画像分析', '深圳数谷信息技术有限公司', 65.00, '2022-04-15', '2023-10-14', '主持', '已结题', NULL),
(@u1, '政务云平台安全防护体系设计', '福州数字政务科技有限公司', 95.00, '2024-02-01', '2025-12-31', '主持', '在研', NULL);

-- 李四 T102: 13条
INSERT INTO horizontal_project (user_id, name, company_name, contract_amount, sign_date, end_date, role, status, remark) VALUES
(@u2, '智慧交通信号灯控制系统开发', '厦门智行交通科技有限公司', 55.00, '2021-05-01', '2023-05-01', '主持', '已结题', NULL),
(@u2, '网络信息安全态势感知平台', '福州网盾信息安全技术有限公司', 70.00, '2022-03-15', '2024-03-14', '主持', '在研', NULL),
(@u2, '基于物联网的智能仓储管理', '泉州星联物联科技有限公司', 48.00, '2020-11-01', '2022-04-30', '主持', '已结题', NULL),
(@u2, '企业级微服务架构搭建咨询', '厦门亿联软件有限公司', 25.00, '2023-04-01', '2024-03-31', '主持', '在研', NULL),
(@u2, '面向电商的个性化推荐引擎', '杭州云智商城科技有限公司', 85.00, '2022-07-01', '2024-01-31', '主持', '已结题', NULL),
(@u2, '工业设备远程运维管理系统', '福州精工智能制造有限公司', 110.00, '2023-09-01', '2025-09-01', '主持', '在研', NULL),
(@u2, '智慧医疗影像辅助诊断系统', '福州康达医疗科技有限公司', 60.00, '2021-08-01', '2023-02-28', '主持', '已结题', NULL),
(@u2, '数字孪生校园管理平台', '福建高教信息化科技有限公司', 90.00, '2022-12-01', '2024-12-31', '主持', '在研', NULL),
(@u2, '游戏引擎性能优化技术服务', '厦门趣游互动科技有限公司', 35.00, '2020-06-01', '2021-06-30', '主持', '已结题', NULL),
(@u2, 'AI客服机器人系统开发', '福州智联云计算有限公司', 42.00, '2023-02-15', '2024-08-14', '主持', '在研', NULL),
(@u2, '自动化测试框架定制', '泉州数测科技有限公司', 18.00, '2021-02-01', '2022-02-01', '主持', '已结题', NULL),
(@u2, '智慧社区物业管理系统', '福州万嘉物业服务有限公司', 33.00, '2022-09-01', '2023-09-30', '主持', '已结题', NULL),
(@u2, '融媒体内容管理平台开发', '福建海峡传媒集团有限公司', 75.00, '2024-01-15', '2025-07-31', '主持', '在研', NULL);

-- 王五 T103: 3条
INSERT INTO horizontal_project (user_id, name, company_name, contract_amount, sign_date, end_date, role, status, remark) VALUES
(@u3, '金融风控模型的数学建模与优化', '厦门金汇金融服务有限公司', 40.00, '2022-05-01', '2023-10-31', '主持', '已结题', NULL),
(@u3, '大数据统计分析咨询服务', '福州数源科技有限公司', 15.00, '2023-06-01', '2024-05-31', '主持', '在研', NULL),
(@u3, '基于数学模型的物流路径优化', '泉州迅达物流集团有限公司', 55.00, '2021-09-01', '2022-12-31', '主持', '已结题', NULL);

-- 赵六 T104: 20条
INSERT INTO horizontal_project (user_id, name, company_name, contract_amount, sign_date, end_date, role, status, remark) VALUES
(@u4, '企业数据仓库架构设计与实施', '厦门数联科技有限公司', 68.00, '2021-03-01', '2022-09-30', '主持', '已结题', NULL),
(@u4, '高校教务管理系统二次开发', '福建高教软件有限公司', 35.00, '2020-10-01', '2021-12-31', '主持', '已结题', NULL),
(@u4, '智慧园区人脸识别门禁系统', '福州识通智能科技有限公司', 52.00, '2022-04-15', '2023-10-14', '主持', '已结题', NULL),
(@u4, '在线考试防作弊监考系统', '福建学测教育科技有限公司', 28.00, '2023-02-01', '2024-01-31', '主持', '在研', NULL),
(@u4, '数字档案管理系统开发', '福州兰台信息技术有限公司', 44.00, '2021-07-01', '2023-06-30', '主持', '已结题', NULL),
(@u4, '微信公众号营销平台定制', '厦门微聚网络科技有限公司', 12.00, '2022-11-01', '2023-05-31', '主持', '已结题', NULL),
(@u4, '智慧养老健康监测平台', '福州乐颐养老服务有限公司', 98.00, '2023-05-01', '2025-05-01', '主持', '在研', NULL),
(@u4, '企业OA办公自动化系统升级', '泉州创智信息技术有限公司', 22.00, '2020-08-01', '2021-07-31', '主持', '已结题', NULL),
(@u4, '大数据在零售业的应用咨询', '福建万商数据服务有限公司', 76.00, '2023-08-15', '2025-02-14', '主持', '在研', NULL),
(@u4, '智能停车场管理系统开发', '福州泊联停车科技有限公司', 38.00, '2021-12-01', '2023-05-31', '主持', '已结题', NULL),
(@u4, '教育信息化平台第三方测评', '福建评测信息技术有限公司', 16.00, '2022-02-01', '2022-08-31', '主持', '已结题', NULL),
(@u4, '农业物联网监测系统开发', '福建农信物联科技有限公司', 65.00, '2023-01-01', '2024-12-31', '主持', '在研', NULL),
(@u4, '移动APP用户行为分析', '厦门知数科技有限公司', 42.00, '2020-12-01', '2021-11-30', '主持', '已结题', NULL),
(@u4, '政务大数据可视化展示平台', '福州政务云科技有限公司', 88.00, '2023-07-01', '2025-01-31', '主持', '在研', NULL),
(@u4, '三维虚拟仿真实验教学系统', '福建华泽信息技术有限公司', 55.00, '2021-05-15', '2022-11-14', '主持', '已结题', NULL),
(@u4, '车载智能终端通信协议开发', '厦门车载智联科技有限公司', 32.00, '2022-08-01', '2023-07-31', '主持', '已结题', NULL),
(@u4, '图书管理系统数字化改造', '福州知了图书有限公司', 18.00, '2023-03-01', '2023-11-30', '主持', '在研', NULL),
(@u4, '基于数据挖掘的客户流失预警', '泉州融通金融服务有限公司', 105.00, '2024-01-01', '2025-12-31', '主持', '在研', NULL),
(@u4, '企业网络安全防护体系建设', '福建迅捷网络安全技术有限公司', 72.00, '2022-06-01', '2024-05-31', '主持', '在研', NULL),
(@u4, '小程序商城系统开发', '福州优选电商科技有限公司', 25.00, '2021-08-15', '2022-02-28', '主持', '已结题', NULL);

-- 钱七 T105: 6条
INSERT INTO horizontal_project (user_id, name, company_name, contract_amount, sign_date, end_date, role, status, remark) VALUES
(@u5, '半导体材料性能测试分析服务', '厦门芯科半导体科技有限公司', 48.00, '2021-04-01', '2022-09-30', '主持', '已结题', NULL),
(@u5, '光伏组件效率优化技术咨询', '泉州阳光新能源有限公司', 36.00, '2022-07-15', '2023-07-14', '主持', '已结题', NULL),
(@u5, '新型显示材料的发光特性研究', '福州晶彩光电科技有限公司', 85.00, '2023-03-01', '2025-03-31', '主持', '在研', NULL),
(@u5, '量子点荧光探针的合成与应用', '厦门荧光生物科技有限公司', 62.00, '2022-11-01', '2024-10-31', '主持', '在研', NULL),
(@u5, '锂电池正极材料的结构表征', '宁德时代新能源科技股份有限公司', 120.00, '2023-05-01', '2025-05-31', '主持', '在研', NULL),
(@u5, '纳米涂层材料的耐磨性能测试', '福州纳新材料科技有限公司', 28.00, '2020-10-01', '2021-09-30', '主持', '已结题', NULL);

-- ============================================================
-- 4. 专利数据 + 转让记录
-- ============================================================
-- 张三 T101: 4条 (发明1, 实用新型2, 外观设计1; 已授权2, 申请中2; 转让1条)
INSERT INTO patent (user_id, name, type, application_no, grant_no, application_date, grant_date, status, inventors, patentee, is_counted, remark) VALUES
(@u1, '一种基于深度卷积网络的实时目标检测方法', '发明专利', '202010123456.7', 'ZL202010123456.7', '2020-05-15', '2022-03-18', '已授权', '张三,李明,王华', '福建师范大学', 1, NULL);
SET @pat1 = LAST_INSERT_ID();
INSERT INTO patent_transfer (patent_id, transfer_date, transferee, amount) VALUES
(@pat1, '2023-06-20', '福建智能科技有限公司', 15.00);

INSERT INTO patent (user_id, name, type, application_no, grant_no, application_date, grant_date, status, inventors, patentee, is_counted, remark) VALUES
(@u1, '一种智能家居多终端协同控制装置', '实用新型', '202120567890.1', 'ZL202120567890.1', '2021-03-20', '2021-11-15', '已授权', '张三,陈凯', '福建师范大学', 1, NULL);
SET @pat2 = LAST_INSERT_ID();
INSERT INTO patent_transfer (patent_id, transfer_date, transferee, amount) VALUES
(@pat2, '2023-01-10', '福州智美家居科技有限公司', 8.00);

INSERT INTO patent (user_id, name, type, application_no, grant_no, application_date, grant_date, status, inventors, patentee, is_counted, remark) VALUES
(@u1, '一种边缘计算场景下的任务卸载调度方法', '发明专利', '202310234567.8', NULL, '2023-06-01', NULL, '申请中', '张三,赵磊,孙明', '福建师范大学', 0, NULL);

INSERT INTO patent (user_id, name, type, application_no, grant_no, application_date, grant_date, status, inventors, patentee, is_counted, remark) VALUES
(@u1, '智能互动教学一体机', '外观设计', '202230098765.4', 'ZL202230098765.4', '2022-08-10', '2023-01-25', '已授权', '张三,刘洋', '福建师范大学', 1, NULL);
SET @pat4 = LAST_INSERT_ID();
INSERT INTO patent_transfer (patent_id, transfer_date, transferee, amount) VALUES
(@pat4, '2024-02-15', '福建慧教智能设备有限公司', 5.00);

-- 李四 T102: 7条 (发明3, 实用新型3, 外观设计1; 已授权4, 申请中3; 转让1条)
INSERT INTO patent (user_id, name, type, application_no, grant_no, application_date, grant_date, status, inventors, patentee, is_counted, remark) VALUES
(@u2, '一种基于联邦学习的隐私保护数据挖掘方法', '发明专利', '202110345678.9', 'ZL202110345678.9', '2021-07-20', '2023-01-10', '已授权', '李四,周明,吴强', '福建师范大学', 1, NULL);

INSERT INTO patent (user_id, name, type, application_no, grant_no, application_date, grant_date, status, inventors, patentee, is_counted, remark) VALUES
(@u2, '一种面向自动驾驶的多传感器融合感知装置', '实用新型', '202220678901.2', 'ZL202220678901.2', '2022-04-15', '2022-10-20', '已授权', '李四,黄飞', '福建师范大学', 1, NULL);
SET @pat_l2 = LAST_INSERT_ID();
INSERT INTO patent_transfer (patent_id, transfer_date, transferee, amount) VALUES
(@pat_l2, '2024-01-05', '厦门智驾科技有限公司', 12.00);

INSERT INTO patent (user_id, name, type, application_no, grant_no, application_date, grant_date, status, inventors, patentee, is_counted, remark) VALUES
(@u2, '一种分布式存储系统的自适应负载均衡装置', '实用新型', '202320789012.3', NULL, '2023-05-10', NULL, '申请中', '李四,何平', '福建师范大学', 0, NULL);

INSERT INTO patent (user_id, name, type, application_no, grant_no, application_date, grant_date, status, inventors, patentee, is_counted, remark) VALUES
(@u2, '基于知识蒸馏的轻量化图像分类方法', '发明专利', '202310890123.4', NULL, '2023-09-01', NULL, '申请中', '李四,张华,郑凯', '福建师范大学', 0, NULL);

INSERT INTO patent (user_id, name, type, application_no, grant_no, application_date, grant_date, status, inventors, patentee, is_counted, remark) VALUES
(@u2, '一种工业物联网数据实时采集终端', '实用新型', '202121901234.5', 'ZL202121901234.5', '2021-11-05', '2022-06-15', '已授权', '李四,杨林', '福建师范大学', 1, NULL);

INSERT INTO patent (user_id, name, type, application_no, grant_no, application_date, grant_date, status, inventors, patentee, is_counted, remark) VALUES
(@u2, '基于区块链的供应链溯源方法及系统', '发明专利', '202210012345.6', 'ZL202210012345.6', '2022-02-28', '2023-09-15', '已授权', '李四,王雷,陈健', '福建师范大学', 1, NULL);

INSERT INTO patent (user_id, name, type, application_no, grant_no, application_date, grant_date, status, inventors, patentee, is_counted, remark) VALUES
(@u2, '智能仓储管理终端操作面板', '外观设计', '202230123456.7', NULL, '2023-02-20', NULL, '申请中', '李四,赵明', '福建师范大学', 0, NULL);

-- 王五 T103: 12条 (发明5, 实用新型5, 外观设计2; 已授权7, 申请中5; 转让2条)
INSERT INTO patent (user_id, name, type, application_no, grant_no, application_date, grant_date, status, inventors, patentee, is_counted, remark) VALUES
(@u3, '一种基于随机矩阵的高维数据分析方法', '发明专利', '202010456789.0', 'ZL202010456789.0', '2020-09-10', '2022-04-20', '已授权', '王五,陈敏,林杰', '福建师范大学', 1, NULL);
SET @pat_w1 = LAST_INSERT_ID();
INSERT INTO patent_transfer (patent_id, transfer_date, transferee, amount) VALUES
(@pat_w1, '2023-08-15', '厦门数析科技有限公司', 18.00);

INSERT INTO patent (user_id, name, type, application_no, grant_no, application_date, grant_date, status, inventors, patentee, is_counted, remark) VALUES
(@u3, '一种偏微分方程数值求解的计算装置', '实用新型', '202120789012.1', 'ZL202120789012.1', '2021-06-15', '2022-01-20', '已授权', '王五,张凯', '福建师范大学', 1, NULL);

INSERT INTO patent (user_id, name, type, application_no, grant_no, application_date, grant_date, status, inventors, patentee, is_counted, remark) VALUES
(@u3, '基于最优传输理论的样本生成方法', '发明专利', '202110567890.2', 'ZL202110567890.2', '2021-12-01', '2023-07-18', '已授权', '王五,李辉,陈新', '福建师范大学', 1, NULL);

INSERT INTO patent (user_id, name, type, application_no, grant_no, application_date, grant_date, status, inventors, patentee, is_counted, remark) VALUES
(@u3, '一种教学用概率统计实验演示装置', '实用新型', '202221012345.3', 'ZL202221012345.3', '2022-05-20', '2022-12-15', '已授权', '王五,刘芳', '福建师范大学', 1, NULL);

INSERT INTO patent (user_id, name, type, application_no, grant_no, application_date, grant_date, status, inventors, patentee, is_counted, remark) VALUES
(@u3, '基于流形学习的非线性降维算法加速器', '发明专利', '202310123456.4', NULL, '2023-03-10', NULL, '申请中', '王五,杨明,黄丽', '福建师范大学', 0, NULL);

INSERT INTO patent (user_id, name, type, application_no, grant_no, application_date, grant_date, status, inventors, patentee, is_counted, remark) VALUES
(@u3, '一种金融时间序列波动率预测装置', '实用新型', '202220987654.5', NULL, '2022-08-25', NULL, '申请中', '王五,马超', '福建师范大学', 0, NULL);

INSERT INTO patent (user_id, name, type, application_no, grant_no, application_date, grant_date, status, inventors, patentee, is_counted, remark) VALUES
(@u3, '基于深度强化学习的组合优化求解方法', '发明专利', '202210678901.6', 'ZL202210678901.6', '2022-03-15', '2023-11-10', '已授权', '王五,周强,何勇', '福建师范大学', 1, NULL);
SET @pat_w7 = LAST_INSERT_ID();
INSERT INTO patent_transfer (patent_id, transfer_date, transferee, amount) VALUES
(@pat_w7, '2024-03-01', '北京数智科技有限公司', 25.00);

INSERT INTO patent (user_id, name, type, application_no, grant_no, application_date, grant_date, status, inventors, patentee, is_counted, remark) VALUES
(@u3, '一种便于携带的数学建模工具箱', '实用新型', '202320234567.7', NULL, '2023-11-01', NULL, '申请中', '王五,吴杰', '福建师范大学', 0, NULL);

INSERT INTO patent (user_id, name, type, application_no, grant_no, application_date, grant_date, status, inventors, patentee, is_counted, remark) VALUES
(@u3, '基于注意力机制的时间序列预测方法', '发明专利', '202120345678.8', 'ZL202120345678.8', '2021-09-20', '2023-02-25', '已授权', '王五,郑林,钱茂', '福建师范大学', 1, NULL);

INSERT INTO patent (user_id, name, type, application_no, grant_no, application_date, grant_date, status, inventors, patentee, is_counted, remark) VALUES
(@u3, '一种数据可视化展示大屏', '外观设计', '202230345678.9', 'ZL202230345678.9', '2022-10-15', '2023-04-10', '已授权', '王五,徐磊', '福建师范大学', 1, NULL);

INSERT INTO patent (user_id, name, type, application_no, grant_no, application_date, grant_date, status, inventors, patentee, is_counted, remark) VALUES
(@u3, '一种基于图神经网络的社交推荐装置', '实用新型', '202320456789.0', NULL, '2023-06-20', NULL, '申请中', '王五,张悦', '福建师范大学', 0, NULL);

INSERT INTO patent (user_id, name, type, application_no, grant_no, application_date, grant_date, status, inventors, patentee, is_counted, remark) VALUES
(@u3, '统计分析软件图形用户界面', '外观设计', '202330456789.1', NULL, '2023-08-05', NULL, '申请中', '王五,林凡', '福建师范大学', 0, NULL);

-- 赵六 T104: 2条 (发明1, 实用新型1; 已授权2, 申请中0; 转让2条)
INSERT INTO patent (user_id, name, type, application_no, grant_no, application_date, grant_date, status, inventors, patentee, is_counted, remark) VALUES
(@u4, '一种基于拓扑数据分析的异常检测方法', '发明专利', '202010789012.2', 'ZL202010789012.2', '2020-11-20', '2022-08-15', '已授权', '赵六,江涛,刘颖', '福建师范大学', 1, NULL);
SET @pat_z1 = LAST_INSERT_ID();
INSERT INTO patent_transfer (patent_id, transfer_date, transferee, amount) VALUES
(@pat_z1, '2023-09-10', '深圳拓扑科技有限公司', 20.00);

INSERT INTO patent (user_id, name, type, application_no, grant_no, application_date, grant_date, status, inventors, patentee, is_counted, remark) VALUES
(@u4, '一种数据仓库ETL流程可视化编排装置', '实用新型', '202220890123.3', 'ZL202220890123.3', '2022-07-15', '2023-01-25', '已授权', '赵六,陈鹏', '福建师范大学', 1, NULL);
SET @pat_z2 = LAST_INSERT_ID();
INSERT INTO patent_transfer (patent_id, transfer_date, transferee, amount) VALUES
(@pat_z2, '2024-01-20', '福州数联科技有限公司', 6.00);

-- 钱七 T105: 9条 (发明3, 实用新型4, 外观设计2; 已授权6, 申请中3; 转让2条)
INSERT INTO patent (user_id, name, type, application_no, grant_no, application_date, grant_date, status, inventors, patentee, is_counted, remark) VALUES
(@u5, '一种基于钙钛矿材料的太阳能电池制备方法', '发明专利', '202010890123.4', 'ZL202010890123.4', '2020-06-10', '2022-02-28', '已授权', '钱七,宋飞,吴涛', '福建师范大学', 1, NULL);
SET @pat_q1 = LAST_INSERT_ID();
INSERT INTO patent_transfer (patent_id, transfer_date, transferee, amount) VALUES
(@pat_q1, '2023-05-18', '厦门新能源科技有限公司', 30.00);

INSERT INTO patent (user_id, name, type, application_no, grant_no, application_date, grant_date, status, inventors, patentee, is_counted, remark) VALUES
(@u5, '一种量子点荧光材料的合成装置', '实用新型', '202121890234.5', 'ZL202121890234.5', '2021-10-15', '2022-05-20', '已授权', '钱七,胡亮', '福建师范大学', 1, NULL);

INSERT INTO patent (user_id, name, type, application_no, grant_no, application_date, grant_date, status, inventors, patentee, is_counted, remark) VALUES
(@u5, '一种二维材料异质结的制备方法及应用', '发明专利', '202110901345.6', 'ZL202110901345.6', '2021-12-01', '2023-08-20', '已授权', '钱七,黄杰,郑强', '福建师范大学', 1, NULL);

INSERT INTO patent (user_id, name, type, application_no, grant_no, application_date, grant_date, status, inventors, patentee, is_counted, remark) VALUES
(@u5, '一种超导量子比特的测试夹具', '实用新型', '202222012456.7', 'ZL202222012456.7', '2022-04-20', '2022-11-15', '已授权', '钱七,叶华', '福建师范大学', 1, NULL);
SET @pat_q4 = LAST_INSERT_ID();
INSERT INTO patent_transfer (patent_id, transfer_date, transferee, amount) VALUES
(@pat_q4, '2023-12-10', '北京量子科技有限公司', 15.00);

INSERT INTO patent (user_id, name, type, application_no, grant_no, application_date, grant_date, status, inventors, patentee, is_counted, remark) VALUES
(@u5, '一种纳米涂层材料的耐磨测试装置', '实用新型', '202320123567.8', NULL, '2023-04-10', NULL, '申请中', '钱七,徐宏', '福建师范大学', 0, NULL);

INSERT INTO patent (user_id, name, type, application_no, grant_no, application_date, grant_date, status, inventors, patentee, is_counted, remark) VALUES
(@u5, '光子晶体光纤的色散特性调控方法', '发明专利', '202210234567.9', NULL, '2023-02-15', NULL, '申请中', '钱七,刘源,张昊', '福建师范大学', 0, NULL);

INSERT INTO patent (user_id, name, type, application_no, grant_no, application_date, grant_date, status, inventors, patentee, is_counted, remark) VALUES
(@u5, '实验室用超低温恒温控制系统', '实用新型', '202122345678.0', 'ZL202122345678.0', '2021-07-25', '2022-02-18', '已授权', '钱七,高翔', '福建师范大学', 1, NULL);

INSERT INTO patent (user_id, name, type, application_no, grant_no, application_date, grant_date, status, inventors, patentee, is_counted, remark) VALUES
(@u5, '手持式光谱分析仪', '外观设计', '202230567890.1', NULL, '2022-09-18', NULL, '申请中', '钱七,何铭', '福建师范大学', 0, NULL);

INSERT INTO patent (user_id, name, type, application_no, grant_no, application_date, grant_date, status, inventors, patentee, is_counted, remark) VALUES
(@u5, '太阳能电池板清洁机器人外观', '外观设计', '202330567890.2', 'ZL202330567890.2', '2023-05-10', '2023-11-05', '已授权', '钱七,李航', '福建师范大学', 1, NULL);

-- ============================================================
-- 5. 软著数据
-- ============================================================
-- 张三 T101: 5条
INSERT INTO software_copyright (user_id, name, registration_no, version, dev_completion_date, first_publish_date, registration_date, copyright_owners, remark) VALUES
(@u1, '深度学习图像识别教学平台V1.0', 'SC-T101-001', 'V1.0', '2022-01-15', '2022-02-20', '2022-03-10', '福建师范大学', NULL),
(@u1, '智慧校园数据分析管理系统V2.0', 'SC-T101-002', 'V2.0', '2021-06-01', '2021-07-15', '2021-08-05', '福建师范大学', NULL),
(@u1, '边缘计算任务调度仿真平台V1.0', 'SC-T101-003', 'V1.0', '2023-03-20', '2023-04-15', '2023-05-08', '福建师范大学', NULL),
(@u1, '区块链供应链管理系统V1.2', 'SC-T101-004', 'V1.2', '2022-08-10', '2022-09-05', '2022-10-18', '福建师范大学', NULL),
(@u1, '多模态数据融合分析平台V1.0', 'SC-T101-005', 'V1.0', '2024-01-05', '2024-02-01', '2024-02-28', '福建师范大学', NULL);

-- 李四 T102: 10条
INSERT INTO software_copyright (user_id, name, registration_no, version, dev_completion_date, first_publish_date, registration_date, copyright_owners, remark) VALUES
(@u2, '智能交通信号控制仿真系统V1.0', 'SC-T102-001', 'V1.0', '2021-04-10', '2021-05-20', '2021-06-15', '福建师范大学', NULL),
(@u2, '网络信息安全监测管理平台V2.1', 'SC-T102-002', 'V2.1', '2022-12-15', '2023-01-20', '2023-02-08', '福建师范大学', NULL),
(@u2, '物联网仓储智能管理系统V1.0', 'SC-T102-003', 'V1.0', '2021-08-20', '2021-09-15', '2021-10-10', '福建师范大学', NULL),
(@u2, '电商个性化推荐引擎系统V1.5', 'SC-T102-004', 'V1.5', '2022-05-10', '2022-06-18', '2022-07-05', '福建师范大学', NULL),
(@u2, '工业设备远程监控运维平台V1.0', 'SC-T102-005', 'V1.0', '2024-02-01', '2024-03-05', '2024-03-28', '福建师范大学', NULL),
(@u2, 'AI智能客服对话管理系统V2.0', 'SC-T102-006', 'V2.0', '2023-05-20', '2023-06-15', '2023-07-10', '福建师范大学', NULL),
(@u2, '自动化软件测试管理平台V1.3', 'SC-T102-007', 'V1.3', '2022-09-05', '2022-10-10', '2022-11-02', '福建师范大学', NULL),
(@u2, '数字孪生校园三维可视化系统V1.0', 'SC-T102-008', 'V1.0', '2023-08-15', '2023-09-10', '2023-10-05', '福建师范大学', NULL),
(@u2, '融媒体内容管理与分发平台V1.0', 'SC-T102-009', 'V1.0', '2024-04-01', '2024-05-10', '2024-06-12', '福建师范大学', NULL),
(@u2, '智慧物业社区服务管理平台V2.5', 'SC-T102-010', 'V2.5', '2022-11-01', '2022-12-05', '2023-01-15', '福建师范大学', NULL);

-- 王五 T103: 2条
INSERT INTO software_copyright (user_id, name, registration_no, version, dev_completion_date, first_publish_date, registration_date, copyright_owners, remark) VALUES
(@u3, '金融风控数学模型分析系统V1.0', 'SC-T103-001', 'V1.0', '2022-04-20', '2022-05-25', '2022-06-18', '福建师范大学', NULL),
(@u3, '物流路径优化算法仿真平台V1.2', 'SC-T103-002', 'V1.2', '2023-01-10', '2023-02-18', '2023-03-12', '福建师范大学', NULL);

-- 赵六 T104: 15条
INSERT INTO software_copyright (user_id, name, registration_no, version, dev_completion_date, first_publish_date, registration_date, copyright_owners, remark) VALUES
(@u4, '企业数据仓库ETL管理平台V1.0', 'SC-T104-001', 'V1.0', '2021-03-10', '2021-04-20', '2021-05-15', '福建师范大学', NULL),
(@u4, '高校教务综合管理系统V3.0', 'SC-T104-002', 'V3.0', '2021-08-05', '2021-09-10', '2021-10-02', '福建师范大学', NULL),
(@u4, '智慧园区人脸识别门禁系统V1.0', 'SC-T104-003', 'V1.0', '2022-02-20', '2022-03-15', '2022-04-08', '福建师范大学', NULL),
(@u4, '在线考试智能监考防作弊系统V1.5', 'SC-T104-004', 'V1.5', '2023-03-15', '2023-04-20', '2023-05-12', '福建师范大学', NULL),
(@u4, '数字档案信息化管理系统V2.0', 'SC-T104-005', 'V2.0', '2022-06-10', '2022-07-18', '2022-08-05', '福建师范大学', NULL),
(@u4, '微信公众号内容营销管理平台V1.0', 'SC-T104-006', 'V1.0', '2022-10-05', '2022-11-10', '2022-12-02', '福建师范大学', NULL),
(@u4, '智慧养老健康监测评估平台V1.0', 'SC-T104-007', 'V1.0', '2023-07-20', '2023-08-15', '2023-09-10', '福建师范大学', NULL),
(@u4, '企业协同办公自动化系统V5.0', 'SC-T104-008', 'V5.0', '2021-11-01', '2021-12-05', '2022-01-08', '福建师范大学', NULL),
(@u4, '零售业大数据分析决策平台V1.0', 'SC-T104-009', 'V1.0', '2024-01-10', '2024-02-15', '2024-03-05', '福建师范大学', NULL),
(@u4, '智能停车场无人值守管理系统V1.2', 'SC-T104-010', 'V1.2', '2022-04-15', '2022-05-20', '2022-06-10', '福建师范大学', NULL),
(@u4, '农业物联网环境监测管理平台V1.0', 'SC-T104-011', 'V1.0', '2024-02-20', '2024-03-25', '2024-04-18', '福建师范大学', NULL),
(@u4, '移动APP用户行为分析系统V2.0', 'SC-T104-012', 'V2.0', '2021-06-10', '2021-07-15', '2021-08-10', '福建师范大学', NULL),
(@u4, '政务大数据可视化分析展示平台V1.0', 'SC-T104-013', 'V1.0', '2023-10-05', '2023-11-10', '2023-12-08', '福建师范大学', NULL),
(@u4, '三维虚拟仿真实验教学系统V1.5', 'SC-T104-014', 'V1.5', '2022-07-20', '2022-08-25', '2022-09-18', '福建师范大学', NULL),
(@u4, '车载智能终端远程通信管理系统V1.0', 'SC-T104-015', 'V1.0', '2023-05-10', '2023-06-20', '2023-07-15', '福建师范大学', NULL);

-- 钱七 T105: 7条
INSERT INTO software_copyright (user_id, name, registration_no, version, dev_completion_date, first_publish_date, registration_date, copyright_owners, remark) VALUES
(@u5, '半导体材料性能测试数据分析系统V1.0', 'SC-T105-001', 'V1.0', '2022-03-10', '2022-04-20', '2022-05-15', '福建师范大学', NULL),
(@u5, '光伏发电效率智能监测平台V2.0', 'SC-T105-002', 'V2.0', '2023-01-15', '2023-02-20', '2023-03-10', '福建师范大学', NULL),
(@u5, '新型显示材料光谱分析系统V1.0', 'SC-T105-003', 'V1.0', '2023-08-01', '2023-09-10', '2023-10-05', '福建师范大学', NULL),
(@u5, '量子点荧光探针信号处理平台V1.2', 'SC-T105-004', 'V1.2', '2023-04-20', '2023-05-25', '2023-06-18', '福建师范大学', NULL),
(@u5, '锂电池电化学性能仿真系统V2.5', 'SC-T105-005', 'V2.5', '2024-01-05', '2024-02-15', '2024-03-08', '福建师范大学', NULL),
(@u5, '纳米涂层材料性能评估系统V1.5', 'SC-T105-006', 'V1.5', '2021-09-10', '2021-10-20', '2021-11-15', '福建师范大学', NULL),
(@u5, '量子计算纠错模拟实验平台V1.0', 'SC-T105-007', 'V1.0', '2023-11-01', '2023-12-05', '2024-01-12', '福建师范大学', NULL);

-- ============================================================
-- 6. 论文数据 + 收录关联
-- ============================================================
-- 张三 T101: 9条 (期刊论文6, 会议论文3; SCI/SSCI~2, EI/CSCD/CSSCI~3, 北大核心~2, 普通~2)
INSERT INTO paper (user_id, title, type, journal_name, volume, issue, pages, publish_date, authors, author_order, doi, remark) VALUES
(@u1, 'A Novel Deep Learning Framework for Real-time Semantic Segmentation in Autonomous Driving Scenarios', '期刊论文', 'IEEE Transactions on Image Processing', '32', NULL, '1458-1472', '2023-06-15', 'San Zhang, Ming Li, Hua Wang', 1, '10.1109/TIP.2023.3298765', NULL);
SET @pap1 = LAST_INSERT_ID();
INSERT INTO paper_index (paper_id, index_type) VALUES (@pap1, 'SCI'), (@pap1, 'EI');

INSERT INTO paper (user_id, title, type, journal_name, volume, issue, pages, publish_date, authors, author_order, doi, remark) VALUES
(@u1, 'Edge Computing-Based Lightweight Neural Network Compression for IoT Applications', '期刊论文', 'IEEE Internet of Things Journal', '10', '8', '7234-7248', '2023-04-10', 'San Zhang, Lei Zhao', 1, '10.1109/JIOT.2023.3287654', NULL);
SET @pap2 = LAST_INSERT_ID();
INSERT INTO paper_index (paper_id, index_type) VALUES (@pap2, 'SCI');

INSERT INTO paper (user_id, title, type, journal_name, volume, issue, pages, publish_date, authors, author_order, doi, remark) VALUES
(@u1, '融合多尺度特征的金字塔网络在遥感图像目标检测中的应用', '期刊论文', '计算机学报', '45', '3', '567-582', '2022-03-20', '张三,赵磊,孙明', 1, '10.3724/SP.J.1016.2022.00567', NULL);
SET @pap3 = LAST_INSERT_ID();
INSERT INTO paper_index (paper_id, index_type) VALUES (@pap3, 'EI'), (@pap3, 'CSCD');

INSERT INTO paper (user_id, title, type, journal_name, volume, issue, pages, publish_date, authors, author_order, doi, remark) VALUES
(@u1, '面向智慧教育的知识图谱自动构建与推理方法研究', '期刊论文', '软件学报', '34', '6', '2789-2806', '2023-06-15', '张三,刘洋,陈凯', 1, '10.13328/j.cnki.jos.006789', NULL);
SET @pap4 = LAST_INSERT_ID();
INSERT INTO paper_index (paper_id, index_type) VALUES (@pap4, 'EI'), (@pap4, 'CSCD');

INSERT INTO paper (user_id, title, type, journal_name, volume, issue, pages, publish_date, authors, author_order, doi, remark) VALUES
(@u1, '基于区块链的数据安全共享机制综述', '期刊论文', '计算机研究与发展', '59', '8', '1765-1782', '2022-08-20', '李辉,张三,陈新', 2, '10.7544/issn1000-1239.2022.20220305', NULL);
SET @pap5 = LAST_INSERT_ID();
INSERT INTO paper_index (paper_id, index_type) VALUES (@pap5, 'EI'), (@pap5, '北大核心');

INSERT INTO paper (user_id, title, type, journal_name, volume, issue, pages, publish_date, authors, author_order, doi, remark) VALUES
(@u1, '基于自监督对比学习的少样本图像分类方法', '期刊论文', '模式识别与人工智能', '36', '2', '178-190', '2023-02-18', '张三,陈凯', 1, '10.16451/j.cnki.issn1003-6059.202302005', NULL);
SET @pap6 = LAST_INSERT_ID();
INSERT INTO paper_index (paper_id, index_type) VALUES (@pap6, '北大核心');

INSERT INTO paper (user_id, title, type, journal_name, volume, issue, pages, publish_date, authors, author_order, doi, remark) VALUES
(@u1, 'An Efficient Distributed Training Framework for Large-scale Graph Neural Networks', '会议论文', 'Proceedings of the 42nd IEEE International Conference on Distributed Computing Systems (ICDCS 2022)', NULL, NULL, '856-866', '2022-07-12', 'San Zhang, Ming Li, Kai Chen', 1, '10.1109/ICDCS54860.2022.00090', NULL);
SET @pap7 = LAST_INSERT_ID();
INSERT INTO paper_index (paper_id, index_type) VALUES (@pap7, 'EI');

INSERT INTO paper (user_id, title, type, journal_name, volume, issue, pages, publish_date, authors, author_order, doi, remark) VALUES
(@u1, '面向联邦学习的自适应客户端选择与模型聚合策略', '会议论文', '第39届中国数据库学术会议(NDBC 2023)论文集', NULL, NULL, '345-354', '2023-10-18', '张三,刘洋', 1, NULL, NULL);
SET @pap8 = LAST_INSERT_ID();
INSERT INTO paper_index (paper_id, index_type) VALUES (@pap8, '北大核心');

INSERT INTO paper (user_id, title, type, journal_name, volume, issue, pages, publish_date, authors, author_order, doi, remark) VALUES
(@u1, '云计算环境下基于深度强化学习的资源调度策略研究', '期刊论文', '计算机科学与探索', '16', '4', '895-908', '2022-04-25', '张三,杨林', 1, '10.3778/j.issn.1673-9418.2107061', NULL);
SET @pap9 = LAST_INSERT_ID();
INSERT INTO paper_index (paper_id, index_type) VALUES (@pap9, '普通期刊');

-- 李四 T102: 12条 (期刊论文8, 会议论文4; SCI/SSCI~3, EI/CSCD/CSSCI~3, 北大核心~3, 普通~3)
INSERT INTO paper (user_id, title, type, journal_name, volume, issue, pages, publish_date, authors, author_order, doi, remark) VALUES
(@u2, 'Privacy-Preserving Federated Learning for Healthcare Data Mining: A Systematic Review', '期刊论文', 'IEEE Access', '11', NULL, '32541-32560', '2023-03-01', 'Si Li, Ming Zhou, Qiang Wu', 1, '10.1109/ACCESS.2023.3267890', NULL);
SET @pap10 = LAST_INSERT_ID();
INSERT INTO paper_index (paper_id, index_type) VALUES (@pap10, 'SCI');

INSERT INTO paper (user_id, title, type, journal_name, volume, issue, pages, publish_date, authors, author_order, doi, remark) VALUES
(@u2, 'Multi-Sensor Fusion Perception for Autonomous Driving in Complex Urban Environments', '期刊论文', 'IEEE Transactions on Intelligent Transportation Systems', '24', '5', '5234-5250', '2023-05-15', 'Si Li, Fei Huang', 1, '10.1109/TITS.2023.3276543', NULL);
SET @pap11 = LAST_INSERT_ID();
INSERT INTO paper_index (paper_id, index_type) VALUES (@pap11, 'SCI');

INSERT INTO paper (user_id, title, type, journal_name, volume, issue, pages, publish_date, authors, author_order, doi, remark) VALUES
(@u2, 'Digital Twin-Driven Intelligent Manufacturing: Architecture, Key Technologies and Applications', '期刊论文', 'Journal of Manufacturing Systems', '68', NULL, '112-128', '2023-07-20', 'Si Li, Hua Zhang, Kai Zheng', 1, '10.1016/j.jmsy.2023.3289012', NULL);
SET @pap12 = LAST_INSERT_ID();
INSERT INTO paper_index (paper_id, index_type) VALUES (@pap12, 'SSCI');

INSERT INTO paper (user_id, title, type, journal_name, volume, issue, pages, publish_date, authors, author_order, doi, remark) VALUES
(@u2, '面向工业物联网的实时数据流分析与异常检测方法', '期刊论文', '计算机学报', '46', '1', '123-140', '2023-01-20', '李四,何平,黄飞', 1, '10.3724/SP.J.1016.2023.00123', NULL);
SET @pap13 = LAST_INSERT_ID();
INSERT INTO paper_index (paper_id, index_type) VALUES (@pap13, 'EI'), (@pap13, '北大核心');

INSERT INTO paper (user_id, title, type, journal_name, volume, issue, pages, publish_date, authors, author_order, doi, remark) VALUES
(@u2, '基于图神经网络的社交网络异常用户行为检测', '期刊论文', '计算机研究与发展', '60', '3', '658-673', '2023-03-15', '李四,王雷,陈健', 1, '10.7544/issn1000-1239.2023.20220890', NULL);
SET @pap14 = LAST_INSERT_ID();
INSERT INTO paper_index (paper_id, index_type) VALUES (@pap14, 'EI'), (@pap14, 'CSCD');

INSERT INTO paper (user_id, title, type, journal_name, volume, issue, pages, publish_date, authors, author_order, doi, remark) VALUES
(@u2, '区块链技术在供应链溯源中的应用综述', '期刊论文', '软件学报', '33', '9', '3412-3430', '2022-09-15', '赵明,李四', 2, '10.13328/j.cnki.jos.006512', NULL);
SET @pap15 = LAST_INSERT_ID();
INSERT INTO paper_index (paper_id, index_type) VALUES (@pap15, 'EI'), (@pap15, '北大核心');

INSERT INTO paper (user_id, title, type, journal_name, volume, issue, pages, publish_date, authors, author_order, doi, remark) VALUES
(@u2, 'A Lightweight Object Detection Method for Edge Devices via Knowledge Distillation', '会议论文', 'Proceedings of the 2023 International Joint Conference on Neural Networks (IJCNN 2023)', NULL, NULL, '1-8', '2023-06-20', 'Si Li, Hua Zhang, Kai Zheng', 1, '10.1109/IJCNN54540.2023.10192102', NULL);
SET @pap16 = LAST_INSERT_ID();
INSERT INTO paper_index (paper_id, index_type) VALUES (@pap16, 'EI');

INSERT INTO paper (user_id, title, type, journal_name, volume, issue, pages, publish_date, authors, author_order, doi, remark) VALUES
(@u2, 'Adaptive Load Balancing for Distributed Storage Systems Using Deep Reinforcement Learning', '会议论文', 'Proceedings of the 43rd IEEE International Conference on Distributed Computing Systems (ICDCS 2023)', NULL, NULL, '678-688', '2023-07-10', 'Ping He, Si Li, Fei Huang', 2, '10.1109/ICDCS57890.2023.00078', NULL);
SET @pap17 = LAST_INSERT_ID();
INSERT INTO paper_index (paper_id, index_type) VALUES (@pap17, 'EI');

INSERT INTO paper (user_id, title, type, journal_name, volume, issue, pages, publish_date, authors, author_order, doi, remark) VALUES
(@u2, '基于知识蒸馏的轻量化工业缺陷检测模型', '期刊论文', '计算机应用', '42', '10', '3180-3190', '2022-10-15', '李四,何平', 1, '10.11772/j.issn.1001-9081.2022103180', NULL);
SET @pap18 = LAST_INSERT_ID();
INSERT INTO paper_index (paper_id, index_type) VALUES (@pap18, '北大核心');

INSERT INTO paper (user_id, title, type, journal_name, volume, issue, pages, publish_date, authors, author_order, doi, remark) VALUES
(@u2, '云原生微服务架构的性能优化实践', '期刊论文', '计算机技术与发展', '32', '5', '145-154', '2022-05-20', '李四,张华', 1, '10.3969/j.issn.1673-629X.2022.05.025', NULL);
SET @pap19 = LAST_INSERT_ID();
INSERT INTO paper_index (paper_id, index_type) VALUES (@pap19, '普通期刊');

INSERT INTO paper (user_id, title, type, journal_name, volume, issue, pages, publish_date, authors, author_order, doi, remark) VALUES
(@u2, '融媒体内容的智能化分发与个性化推荐策略', '会议论文', '2023年全国多媒体技术学术会议论文集', NULL, NULL, '234-241', '2023-11-08', '李四,杨林', 1, NULL, NULL);
SET @pap20 = LAST_INSERT_ID();
INSERT INTO paper_index (paper_id, index_type) VALUES (@pap20, '普通期刊');

INSERT INTO paper (user_id, title, type, journal_name, volume, issue, pages, publish_date, authors, author_order, doi, remark) VALUES
(@u2, '面向边缘侧的实时视频流分析加速技术研究', '期刊论文', '小型微型计算机系统', '43', '7', '1492-1502', '2022-07-15', '周明,李四', 2, '10.20009/j.cnki.21-1106/TP.2022.07.015', NULL);
SET @pap21 = LAST_INSERT_ID();
INSERT INTO paper_index (paper_id, index_type) VALUES (@pap21, '北大核心');

-- 王五 T103: 18条 (期刊论文13, 会议论文5; SCI/SSCI~4, EI/CSCD/CSSCI~5, 北大核心~5, 普通~4)
INSERT INTO paper (user_id, title, type, journal_name, volume, issue, pages, publish_date, authors, author_order, doi, remark) VALUES
(@u3, 'Nonlinear Manifold Learning via Locally Linear Embedding with Adaptive Neighborhood Selection', '期刊论文', 'Pattern Recognition', '138', NULL, '109345', '2023-03-15', 'Wu Wang, Min Chen, Jie Lin', 1, '10.1016/j.patcog.2023.109345', NULL);
SET @pap22 = LAST_INSERT_ID();
INSERT INTO paper_index (paper_id, index_type) VALUES (@pap22, 'SCI');

INSERT INTO paper (user_id, title, type, journal_name, volume, issue, pages, publish_date, authors, author_order, doi, remark) VALUES
(@u3, 'Random Matrix Theory for MIMO Channel Modeling in 5G Communication Systems', '期刊论文', 'IEEE Transactions on Communications', '71', '6', '3789-3805', '2023-06-10', 'Wu Wang, Kai Zhang', 1, '10.1109/TCOMM.2023.3287651', NULL);
SET @pap23 = LAST_INSERT_ID();
INSERT INTO paper_index (paper_id, index_type) VALUES (@pap23, 'SCI');

INSERT INTO paper (user_id, title, type, journal_name, volume, issue, pages, publish_date, authors, author_order, doi, remark) VALUES
(@u3, 'Optimal Transport Theory for Domain Adaptation in Machine Learning', '期刊论文', 'Journal of Machine Learning Research', '24', NULL, '1-35', '2023-01-05', 'Wu Wang, Qiang Zhou, Yong He', 1, '10.5555/3456789', NULL);
SET @pap24 = LAST_INSERT_ID();
INSERT INTO paper_index (paper_id, index_type) VALUES (@pap24, 'SCI');

INSERT INTO paper (user_id, title, type, journal_name, volume, issue, pages, publish_date, authors, author_order, doi, remark) VALUES
(@u3, 'Deep Reinforcement Learning for Combinatorial Optimization: A Comprehensive Survey', '期刊论文', 'Operations Research Perspectives', '10', NULL, '100267', '2023-04-20', 'Wu Wang, Hui Li, Xin Chen', 1, '10.1016/j.orp.2023.100267', NULL);
SET @pap25 = LAST_INSERT_ID();
INSERT INTO paper_index (paper_id, index_type) VALUES (@pap25, 'SSCI');

INSERT INTO paper (user_id, title, type, journal_name, volume, issue, pages, publish_date, authors, author_order, doi, remark) VALUES
(@u3, '基于注意力机制的时间序列预测方法及应用研究', '期刊论文', '计算机学报', '45', '8', '1789-1806', '2022-08-20', '王五,郑林,钱茂', 1, '10.3724/SP.J.1016.2022.01789', NULL);
SET @pap26 = LAST_INSERT_ID();
INSERT INTO paper_index (paper_id, index_type) VALUES (@pap26, 'EI'), (@pap26, 'CSCD');

INSERT INTO paper (user_id, title, type, journal_name, volume, issue, pages, publish_date, authors, author_order, doi, remark) VALUES
(@u3, '复杂网络的动力学分析与同步控制综述', '期刊论文', '软件学报', '34', '4', '1890-1912', '2023-04-15', '王五,周强,何勇', 1, '10.13328/j.cnki.jos.006890', NULL);
SET @pap27 = LAST_INSERT_ID();
INSERT INTO paper_index (paper_id, index_type) VALUES (@pap27, 'EI'), (@pap27, 'CSSCI');

INSERT INTO paper (user_id, title, type, journal_name, volume, issue, pages, publish_date, authors, author_order, doi, remark) VALUES
(@u3, '基于深度学习的偏微分方程数值求解方法进展', '期刊论文', '中国科学:数学', '53', '9', '1237-1260', '2023-09-10', '王五,徐磊,林凡', 1, '10.1360/SCM-2023-0089', NULL);
SET @pap28 = LAST_INSERT_ID();
INSERT INTO paper_index (paper_id, index_type) VALUES (@pap28, 'CSSCI'), (@pap28, 'CSCD');

INSERT INTO paper (user_id, title, type, journal_name, volume, issue, pages, publish_date, authors, author_order, doi, remark) VALUES
(@u3, '个性化学习路径推荐的研究现状与挑战', '期刊论文', '计算机研究与发展', '59', '5', '1123-1142', '2022-10-15', '张悦,王五', 2, '10.7544/issn1000-1239.2022.20220456', NULL);
SET @pap29 = LAST_INSERT_ID();
INSERT INTO paper_index (paper_id, index_type) VALUES (@pap29, 'EI'), (@pap29, '北大核心');

INSERT INTO paper (user_id, title, type, journal_name, volume, issue, pages, publish_date, authors, author_order, doi, remark) VALUES
(@u3, '概率图模型在金融风控中的应用研究', '期刊论文', '模式识别与人工智能', '35', '7', '658-672', '2022-07-18', '吴杰,王五,郑林', 2, '10.16451/j.cnki.issn1003-6059.202207005', NULL);
SET @pap30 = LAST_INSERT_ID();
INSERT INTO paper_index (paper_id, index_type) VALUES (@pap30, '北大核心');

INSERT INTO paper (user_id, title, type, journal_name, volume, issue, pages, publish_date, authors, author_order, doi, remark) VALUES
(@u3, '高维数据降维的非线性流形学习算法改进', '期刊论文', '计算机科学与探索', '16', '6', '1245-1260', '2022-06-25', '王五,陈敏,林杰', 1, '10.3778/j.issn.1673-9418.2202025', NULL);
SET @pap31 = LAST_INSERT_ID();
INSERT INTO paper_index (paper_id, index_type) VALUES (@pap31, '北大核心');

INSERT INTO paper (user_id, title, type, journal_name, volume, issue, pages, publish_date, authors, author_order, doi, remark) VALUES
(@u3, '基于深度强化学习的投资组合优化方法', '期刊论文', '计算机应用', '43', '3', '789-802', '2023-03-20', '王五,马超,刘芳', 1, '10.11772/j.issn.1001-9081.2023030789', NULL);
SET @pap32 = LAST_INSERT_ID();
INSERT INTO paper_index (paper_id, index_type) VALUES (@pap32, '北大核心');

INSERT INTO paper (user_id, title, type, journal_name, volume, issue, pages, publish_date, authors, author_order, doi, remark) VALUES
(@u3, 'Statistics-Based Teaching Quality Evaluation Model for Higher Education', '期刊论文', 'International Journal of Educational Technology', '9', '2', '89-104', '2022-12-15', 'Wu Wang, Yue Zhang', 1, '10.5430/ijet.v9n2p89', NULL);
SET @pap33 = LAST_INSERT_ID();
INSERT INTO paper_index (paper_id, index_type) VALUES (@pap33, '普通期刊');

INSERT INTO paper (user_id, title, type, journal_name, volume, issue, pages, publish_date, authors, author_order, doi, remark) VALUES
(@u3, '变分自编码器在高维数据降维中的应用与改进', '期刊论文', '计算机技术与发展', '33', '2', '78-88', '2023-02-20', '林凡,王五', 2, '10.3969/j.issn.1673-629X.2023.02.014', NULL);
SET @pap34 = LAST_INSERT_ID();
INSERT INTO paper_index (paper_id, index_type) VALUES (@pap34, '普通期刊');

INSERT INTO paper (user_id, title, type, journal_name, volume, issue, pages, publish_date, authors, author_order, doi, remark) VALUES
(@u3, '金融时间序列波动率预测的数学模型研究', '期刊论文', '统计与决策', '39', '8', '65-72', '2023-04-15', '王五,马超', 1, '10.13546/j.cnki.tjyjc.2023.08.012', NULL);
SET @pap35 = LAST_INSERT_ID();
INSERT INTO paper_index (paper_id, index_type) VALUES (@pap35, '北大核心');

INSERT INTO paper (user_id, title, type, journal_name, volume, issue, pages, publish_date, authors, author_order, doi, remark) VALUES
(@u3, 'Sample-Efficient Deep Reinforcement Learning for Financial Portfolio Management', '会议论文', 'Proceedings of the 2023 AAAI Conference on Artificial Intelligence (AAAI 2023)', NULL, NULL, '7956-7964', '2023-02-12', 'Wu Wang, Hui Li, Xin Chen', 1, '10.1609/aaai.v37i7.26056', NULL);
SET @pap36 = LAST_INSERT_ID();
INSERT INTO paper_index (paper_id, index_type) VALUES (@pap36, 'SCI');

INSERT INTO paper (user_id, title, type, journal_name, volume, issue, pages, publish_date, authors, author_order, doi, remark) VALUES
(@u3, 'PDE-Constrained Deep Learning for Forward and Inverse Problems', '会议论文', 'Proceedings of NeurIPS 2023', NULL, NULL, '1-15', '2023-12-08', 'Wu Wang, Xu Lei, Fan Lin', 1, NULL, NULL);
SET @pap37 = LAST_INSERT_ID();
INSERT INTO paper_index (paper_id, index_type) VALUES (@pap37, 'SCI');

INSERT INTO paper (user_id, title, type, journal_name, volume, issue, pages, publish_date, authors, author_order, doi, remark) VALUES
(@u3, '智慧教育中个性化学习路径的多目标优化', '会议论文', '第16届中国计算机教育大会(CECC 2023)论文集', NULL, NULL, '412-420', '2023-07-20', '王五,徐磊', 1, NULL, NULL);
SET @pap38 = LAST_INSERT_ID();
INSERT INTO paper_index (paper_id, index_type) VALUES (@pap38, '普通期刊');

INSERT INTO paper (user_id, title, type, journal_name, volume, issue, pages, publish_date, authors, author_order, doi, remark) VALUES
(@u3, '基于图注意力网络的社交推荐算法研究', '会议论文', '2023年中国机器学习大会(CCML 2023)论文集', NULL, NULL, '278-286', '2023-08-15', '王五,吴杰', 1, NULL, NULL);
SET @pap39 = LAST_INSERT_ID();
INSERT INTO paper_index (paper_id, index_type) VALUES (@pap39, '普通期刊');

-- 赵六 T104: 4条 (期刊论文3, 会议论文1; SCI/SSCI~1, EI/CSCD/CSSCI~1, 北大核心~1, 普通~1)
INSERT INTO paper (user_id, title, type, journal_name, volume, issue, pages, publish_date, authors, author_order, doi, remark) VALUES
(@u4, 'Topological Data Analysis for High-Dimensional Biological Data: Persistent Homology and Applications', '期刊论文', 'Bioinformatics', '38', '15', '3754-3762', '2022-08-01', 'Tao Jiang, Liu Zhao, Ying Liu', 2, '10.1093/bioinformatics/btac455', NULL);
SET @pap40 = LAST_INSERT_ID();
INSERT INTO paper_index (paper_id, index_type) VALUES (@pap40, 'SCI');

INSERT INTO paper (user_id, title, type, journal_name, volume, issue, pages, publish_date, authors, author_order, doi, remark) VALUES
(@u4, '变分自编码器的改进及其在异常检测中的应用', '期刊论文', '计算机科学', '49', '11', '267-278', '2022-11-15', '赵六,陈鹏', 1, '10.11896/jsjkx.22080012', NULL);
SET @pap41 = LAST_INSERT_ID();
INSERT INTO paper_index (paper_id, index_type) VALUES (@pap41, '北大核心');

INSERT INTO paper (user_id, title, type, journal_name, volume, issue, pages, publish_date, authors, author_order, doi, remark) VALUES
(@u4, '基于数据挖掘的企业客户流失预警模型构建', '期刊论文', '计算机应用研究', '40', '2', '534-545', '2023-02-28', '赵六,江涛', 1, '10.19734/j.issn.1001-3695.2022.07.0356', NULL);
SET @pap42 = LAST_INSERT_ID();
INSERT INTO paper_index (paper_id, index_type) VALUES (@pap42, 'CSCD');

INSERT INTO paper (user_id, title, type, journal_name, volume, issue, pages, publish_date, authors, author_order, doi, remark) VALUES
(@u4, 'Application of Variational Autoencoders for Data Imputation in Educational Assessment', '会议论文', 'Proceedings of the 2023 International Conference on Educational Data Mining (EDM 2023)', NULL, NULL, '245-252', '2023-07-15', 'Liu Zhao, Peng Chen', 1, NULL, NULL);
SET @pap43 = LAST_INSERT_ID();
INSERT INTO paper_index (paper_id, index_type) VALUES (@pap43, '普通期刊');

-- 钱七 T105: 6条 (期刊论文4, 会议论文2; SCI/SSCI~1, EI/CSCD/CSSCI~2, 北大核心~2, 普通~1)
INSERT INTO paper (user_id, title, type, journal_name, volume, issue, pages, publish_date, authors, author_order, doi, remark) VALUES
(@u5, 'Machine Learning-Assisted Prediction of Condensed Matter Properties: A High-Throughput Screening Approach', '期刊论文', 'Physical Review B', '106', '20', '205134', '2022-11-15', 'Qi Qian, Jie Huang, Qiang Zheng', 1, '10.1103/PhysRevB.106.205134', NULL);
SET @pap44 = LAST_INSERT_ID();
INSERT INTO paper_index (paper_id, index_type) VALUES (@pap44, 'SCI');

INSERT INTO paper (user_id, title, type, journal_name, volume, issue, pages, publish_date, authors, author_order, doi, remark) VALUES
(@u5, 'Interface Engineering for High-Performance Perovskite Solar Cells via Machine Learning Optimization', '期刊论文', 'Advanced Energy Materials', '13', '12', '2203897', '2023-04-20', 'Qi Qian, Liang Hu', 1, '10.1002/aenm.202203897', NULL);
SET @pap45 = LAST_INSERT_ID();
INSERT INTO paper_index (paper_id, index_type) VALUES (@pap45, 'SCI');

INSERT INTO paper (user_id, title, type, journal_name, volume, issue, pages, publish_date, authors, author_order, doi, remark) VALUES
(@u5, '二维材料异质结光电特性的第一性原理研究', '期刊论文', '物理学报', '72', '5', '057301', '2023-03-15', '钱七,黄杰,郑强', 1, '10.7498/aps.72.20221890', NULL);
SET @pap46 = LAST_INSERT_ID();
INSERT INTO paper_index (paper_id, index_type) VALUES (@pap46, 'EI'), (@pap46, 'CSCD');

INSERT INTO paper (user_id, title, type, journal_name, volume, issue, pages, publish_date, authors, author_order, doi, remark) VALUES
(@u5, '钙钛矿太阳能电池稳定性提升策略研究进展', '期刊论文', '物理化学学报', '39', '2', '2205090', '2023-02-15', '宋飞,钱七,吴涛', 2, '10.3866/PKU.WHXB202205090', NULL);
SET @pap47 = LAST_INSERT_ID();
INSERT INTO paper_index (paper_id, index_type) VALUES (@pap47, 'EI'), (@pap47, '北大核心');

INSERT INTO paper (user_id, title, type, journal_name, volume, issue, pages, publish_date, authors, author_order, doi, remark) VALUES
(@u5, '基于深度学习的X射线衍射图谱相位鉴定方法', '期刊论文', '光谱学与光谱分析', '42', '8', '2534-2542', '2022-08-15', '叶华,钱七', 2, '10.3964/j.issn.1000-0593(2022)08-2534-09', NULL);
SET @pap48 = LAST_INSERT_ID();
INSERT INTO paper_index (paper_id, index_type) VALUES (@pap48, '北大核心');

INSERT INTO paper (user_id, title, type, journal_name, volume, issue, pages, publish_date, authors, author_order, doi, remark) VALUES
(@u5, 'Femtosecond Laser-Induced Ultrafast Dynamics in 2D Materials for Optoelectronic Applications', '会议论文', 'Proceedings of the 2023 Conference on Lasers and Electro-Optics (CLEO 2023)', NULL, NULL, '1-2', '2023-05-10', 'Qi Qian, Hong Xu', 1, '10.1364/CLEO_AT.2023.JTu2A.56', NULL);
SET @pap49 = LAST_INSERT_ID();
INSERT INTO paper_index (paper_id, index_type) VALUES (@pap49, 'EI');

-- ============================================================
-- 7. 竞赛指导数据
-- ============================================================
-- 张三 T101: 20条 (国家级~5, 省级~9, 校级~6; 一等奖多, 第一指导70%)
INSERT INTO competition (user_id, name, organizer, competition_date, student_team, award_level, award_grade, guide_rank, certificate_no, remark) VALUES
(@u1, '中国大学生计算机设计大赛', '教育部高等学校计算机类教学指导委员会', '2022-08-15', '陈明辉,林海燕,王俊杰', '国家级', '一等奖', 1, '2022GJ001', NULL),
(@u1, '全国大学生机器人大赛RoboMaster', '共青团中央、全国学联', '2023-05-20', '李浩然,张鹏飞,刘思远', '国家级', '一等奖', 1, '2023GJ002', NULL),
(@u1, '全国大学生算法设计与编程大赛', '中国计算机学会', '2022-11-10', '赵晓峰,孙志强', '国家级', '二等奖', 1, '2022GJ003', NULL),
(@u1, '中国"互联网+"大学生创新创业大赛', '教育部', '2023-04-15', '郑宇翔,吴佳音,黄子涵,林欣然', '国家级', '二等奖', 1, '2023GJ004', NULL),
(@u1, '全国大学生人工智能创新大赛', '中国人工智能学会', '2024-03-20', '高天宇,陈思颖', '国家级', '三等奖', 2, '2024GJ001', NULL),
(@u1, '福建省大学生程序设计竞赛', '福建省教育厅', '2021-05-15', '周伟明,吴嘉豪', '省级', '特等奖', 1, '2021SJ001', NULL),
(@u1, '福建省大学生电子设计竞赛', '福建省教育厅', '2022-07-10', '林志远,黄雪梅,何鑫', '省级', '一等奖', 1, '2022SJ002', NULL),
(@u1, '福建省大学生机器人大赛', '福建省教育厅', '2023-04-25', '谢宇航,苏雅婷', '省级', '一等奖', 1, '2023SJ003', NULL),
(@u1, '福建省"互联网+"大学生创新创业大赛', '福建省教育厅', '2022-06-18', '刘明哲,陈雨萱,张宇航', '省级', '一等奖', 1, '2022SJ004', NULL),
(@u1, '福建省大学生人工智能创意赛', '福建省人工智能学会', '2023-09-12', '郑慧琳,马浩然', '省级', '一等奖', 1, '2023SJ005', NULL),
(@u1, '全国大学生数学建模竞赛(福建赛区)', '福建省教育厅', '2021-09-10', '叶晓东,洪晓玲', '省级', '二等奖', 1, '2021SJ006', NULL),
(@u1, '福建省大学生大数据技能竞赛', '福建省大数据学会', '2022-11-28', '林诗涵,潘俊杰', '省级', '二等奖', 1, '2022SJ007', NULL),
(@u1, '华为ICT大赛福建省赛', '华为技术有限公司', '2023-10-05', '赖志鹏', '省级', '二等奖', 2, '2023SJ008', NULL),
(@u1, '蓝桥杯全国软件和信息技术大赛(福建赛区)', '工业和信息化部人才交流中心', '2023-12-15', '方晓峰,宋佳琪', '省级', '三等奖', 1, '2023SJ009', NULL),
(@u1, '福建师范大学程序设计竞赛', '福建师范大学', '2021-03-20', '邱志伟,卢晓莹', '校级', '一等奖', 1, '2021XJ001', NULL),
(@u1, '福建师范大学计算机应用能力大赛', '福建师范大学', '2022-04-10', '廖梓涵,蔡雅琴,戴永康', '校级', '一等奖', 1, '2022XJ002', NULL),
(@u1, '福建师范大学人工智能创新赛', '福建师范大学计算机学院', '2023-05-08', '严浩宇', '校级', '一等奖', 1, '2023XJ003', NULL),
(@u1, '福建师范大学网页设计与开发大赛', '福建师范大学', '2021-10-25', '洪宇轩,庄丽婷', '校级', '二等奖', 1, '2021XJ004', NULL),
(@u1, '福建师范大学大学生创新创业训练计划', '福建师范大学教务处', '2022-12-01', '柯明德,刘慧琳', '校级', '二等奖', 2, '2022XJ005', NULL),
(@u1, '福建师范大学网络安全知识竞赛', '福建师范大学网络中心', '2023-11-20', '覃浩宇', '校级', '三等奖', 1, '2023XJ006', NULL);

-- 李四 T102: 10条 (国家级~3, 省级~4, 校级~3; 一等奖多, 第一指导70%)
INSERT INTO competition (user_id, name, organizer, competition_date, student_team, award_level, award_grade, guide_rank, certificate_no, remark) VALUES
(@u2, '全国大学生信息安全竞赛', '教育部高等学校信息安全类教学指导委员会', '2023-07-20', '王俊凯,赵雪琴,周铭', '国家级', '一等奖', 1, '2023GJ005', NULL),
(@u2, '中国大学生服务外包创新创业大赛', '教育部、商务部', '2022-08-12', '钱晓东,孙丽娟', '国家级', '二等奖', 1, '2022GJ006', NULL),
(@u2, '中国研究生人工智能创新大赛', '中国人工智能学会', '2023-11-15', '郑凯文,沈晓涵,吕佳明', '国家级', '三等奖', 1, '2023GJ007', NULL),
(@u2, '福建省大学生网络安全挑战赛', '福建省教育厅', '2021-06-10', '冯建明,褚佳慧', '省级', '特等奖', 1, '2021SJ010', NULL),
(@u2, '福建省大学生软件测试大赛', '福建省软件行业协会', '2022-05-18', '蒋昊天,韩雨桐', '省级', '一等奖', 1, '2022SJ011', NULL),
(@u2, '福建省"互联网+"大学生创新创业大赛', '福建省教育厅', '2023-07-08', '许佳乐,尤丽华,何晓峰', '省级', '一等奖', 1, '2023SJ012', NULL),
(@u2, '全国大学生数学建模竞赛(福建赛区)', '福建省教育厅', '2021-09-10', '施元凯,孔维佳', '省级', '二等奖', 2, '2021SJ013', NULL),
(@u2, '福建师范大学大学生物联网设计大赛', '福建师范大学', '2021-04-22', '姜弘毅,钟悦', '校级', '一等奖', 1, '2021XJ007', NULL),
(@u2, '福建师范大学智慧校园APP创意大赛', '福建师范大学', '2022-11-15', '杜含笑,蓝建辉', '校级', '一等奖', 1, '2022XJ008', NULL),
(@u2, '福建师范大学信息安全知识竞赛', '福建师范大学网络中心', '2023-03-05', '金宏伟', '校级', '二等奖', 1, '2023XJ009', NULL);

-- 王五 T103: 5条 (国家级~1, 省级~2, 校级~2; 一等奖多, 第一指导70%)
INSERT INTO competition (user_id, name, organizer, competition_date, student_team, award_level, award_grade, guide_rank, certificate_no, remark) VALUES
(@u3, '全国大学生数学建模竞赛', '中国工业与应用数学学会', '2022-09-12', '徐志远,林中杰,汪小涵', '国家级', '一等奖', 1, '2022GJ008', NULL),
(@u3, '福建省大学生数学竞赛', '福建省数学学会', '2021-10-20', '邱瑞霖', '省级', '一等奖', 1, '2021SJ014', NULL),
(@u3, '福建省大学生统计建模大赛', '福建省统计学会', '2022-06-15', '吕鑫磊,何欣怡', '省级', '二等奖', 1, '2022SJ015', NULL),
(@u3, '福建师范大学大学生数学建模竞赛', '福建师范大学', '2022-04-08', '温伟豪,庄英杰,蓝雅芬', '校级', '一等奖', 1, '2022XJ010', NULL),
(@u3, '福建师范大学大学生数学竞赛', '福建师范大学数学学院', '2023-05-15', '项天宇', '校级', '二等奖', 1, '2023XJ011', NULL);

-- 赵六 T104: 8条 (国家级~2, 省级~4, 校级~2; 一等奖多, 第一指导70%)
INSERT INTO competition (user_id, name, organizer, competition_date, student_team, award_level, award_grade, guide_rank, certificate_no, remark) VALUES
(@u4, '全国大学生数学建模竞赛', '中国工业与应用数学学会', '2023-09-12', '邹启明,郭晓雯,彭建辉', '国家级', '二等奖', 1, '2023GJ009', NULL),
(@u4, '中国大学生计算机设计大赛', '教育部高等学校计算机类教学指导委员会', '2022-08-18', '唐宇轩,龚丽芳', '国家级', '优秀奖', 1, '2022GJ010', NULL),
(@u4, '福建省大学生大数据技能竞赛', '福建省大数据学会', '2021-07-20', '丁浩然,苏雨欣', '省级', '一等奖', 1, '2021SJ016', NULL),
(@u4, '福建省大学生程序设计竞赛', '福建省教育厅', '2022-05-10', '余志豪,洪玉婷', '省级', '二等奖', 1, '2022SJ017', NULL),
(@u4, '福建省"互联网+"大学生创新创业大赛', '福建省教育厅', '2023-06-08', '万明哲,柯嘉琳', '省级', '二等奖', 2, '2023SJ018', NULL),
(@u4, '福建省大学生计算机应用能力大赛', '福建省计算机学会', '2024-01-15', '饶宇航', '省级', '三等奖', 1, '2024SJ001', NULL),
(@u4, '福建师范大学程序设计竞赛', '福建师范大学', '2021-03-22', '刘子涵', '校级', '一等奖', 1, '2021XJ012', NULL),
(@u4, '福建师范大学大学生创新创业训练计划', '福建师范大学教务处', '2023-11-15', '曾景明,谢舒婷', '校级', '二等奖', 1, '2023XJ013', NULL);

-- 钱七 T105: 15条 (国家级~4, 省级~7, 校级~4; 一等奖多, 第一指导70%)
INSERT INTO competition (user_id, name, organizer, competition_date, student_team, award_level, award_grade, guide_rank, certificate_no, remark) VALUES
(@u5, '全国大学生物理实验竞赛', '教育部高等学校物理学类教学指导委员会', '2022-07-15', '石晓磊,阮思远', '国家级', '一等奖', 1, '2022GJ011', NULL),
(@u5, '全国大学生光电设计竞赛', '中国光学学会', '2023-08-10', '裴志强,翟海洋', '国家级', '一等奖', 1, '2023GJ012', NULL),
(@u5, '中国大学生物理学术竞赛(CUPT)', '中国物理学会', '2023-06-20', '岑浩宇,管嘉怡,费俊峰', '国家级', '二等奖', 1, '2023GJ013', NULL),
(@u5, '中国"互联网+"大学生创新创业大赛', '教育部', '2024-04-20', '霍思远,谈雨桐,牛瑞霖', '国家级', '三等奖', 2, '2024GJ002', NULL),
(@u5, '福建省大学生物理实验竞赛', '福建省物理学会', '2021-05-25', '咸天亮', '省级', '特等奖', 1, '2021SJ019', NULL),
(@u5, '福建省大学生光电设计竞赛', '福建省教育厅', '2022-06-18', '施俊杰,丛悦', '省级', '一等奖', 1, '2022SJ020', NULL),
(@u5, '福建省大学生科技竞赛', '福建省科技厅', '2023-09-22', '焦宇航,向晓莹', '省级', '一等奖', 1, '2023SJ021', NULL),
(@u5, '福建省大学生创新创业大赛', '福建省教育厅', '2022-05-12', '赖维佳,毕建明', '省级', '一等奖', 1, '2022SJ022', NULL),
(@u5, '福建省大学生物理学术竞赛', '福建省物理学会', '2023-04-08', '闵志豪,屈雅文', '省级', '二等奖', 1, '2023SJ023', NULL),
(@u5, '全国大学生节能减排社会实践与科技竞赛(福建赛区)', '福建省教育厅', '2024-01-10', '喻晓东,白一凡', '省级', '二等奖', 1, '2024SJ003', NULL),
(@u5, '福建省大学生电子设计竞赛', '福建省教育厅', '2023-07-15', '尚志强,戈雅婷', '省级', '三等奖', 2, '2023SJ024', NULL),
(@u5, '福建师范大学物理实验技能竞赛', '福建师范大学', '2021-04-15', '解宇航', '校级', '一等奖', 1, '2021XJ014', NULL),
(@u5, '福建师范大学挑战杯大学生课外学术科技作品竞赛', '福建师范大学团委', '2022-03-28', '储志鹏,席悦', '校级', '一等奖', 1, '2022XJ015', NULL),
(@u5, '福建师范大学大学生创新创业训练计划', '福建师范大学教务处', '2023-12-10', '仲天宇,伊嘉欣', '校级', '二等奖', 1, '2023XJ016', NULL),
(@u5, '福建师范大学物理知识科普竞赛', '福建师范大学物理学院', '2024-02-25', '卜浩然', '校级', '三等奖', 1, '2024XJ001', NULL);

-- ============================================================
-- 数据统计验证
-- ============================================================
SELECT '===== 测试数据统计 =====' AS '';
SELECT '用户总数' AS category, COUNT(*) AS count FROM `user`;
SELECT '纵向项目', COUNT(*) FROM vertical_project;
SELECT '横向项目', COUNT(*) FROM horizontal_project;
SELECT '专利', COUNT(*) FROM patent;
SELECT '专利转让', COUNT(*) FROM patent_transfer;
SELECT '软著', COUNT(*) FROM software_copyright;
SELECT '论文', COUNT(*) FROM paper;
SELECT '论文收录', COUNT(*) FROM paper_index;
SELECT '竞赛指导', COUNT(*) FROM competition;

SELECT '===== 各教师数据统计 =====' AS '';
SELECT u.name, u.work_no,
  (SELECT COUNT(*) FROM vertical_project vp WHERE vp.user_id = u.id) AS vertical_projects,
  (SELECT COUNT(*) FROM horizontal_project hp WHERE hp.user_id = u.id) AS horizontal_projects,
  (SELECT COUNT(*) FROM patent p WHERE p.user_id = u.id) AS patents,
  (SELECT COUNT(*) FROM software_copyright sc WHERE sc.user_id = u.id) AS software_copyrights,
  (SELECT COUNT(*) FROM paper pp WHERE pp.user_id = u.id) AS papers,
  (SELECT COUNT(*) FROM competition c WHERE c.user_id = u.id) AS competitions
FROM `user` u ORDER BY u.work_no;