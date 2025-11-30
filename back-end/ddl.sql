-- 伴侣宠物之家系统 数据库建表脚本

-- 删除数据库（如果存在）
DROP DATABASE IF EXISTS pet_home;

-- 创建数据库
CREATE DATABASE pet_home DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

-- 使用数据库
USE pet_home;

-- 删除用户表（如果存在）
DROP TABLE IF EXISTS user;

-- 创建用户表
CREATE TABLE user (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    username VARCHAR(50) NOT NULL COMMENT '用户名',
    password VARCHAR(50) NOT NULL COMMENT '密码',
    role VARCHAR(20) NOT NULL COMMENT '角色：管理员、普通用户',
    nickname VARCHAR(50) NOT NULL COMMENT '昵称',
    avatar VARCHAR(500) DEFAULT NULL COMMENT '头像URL',
    phone VARCHAR(20) DEFAULT NULL COMMENT '手机号',
    email VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
    status VARCHAR(20) NOT NULL DEFAULT '正常' COMMENT '状态：正常、禁用',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 插入示例数据
INSERT INTO user (username, password, role, nickname, avatar, phone, email, status, create_time) VALUES
('admin', '123456', '管理员', '系统管理员', 'https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?w=400', '13800138000', 'admin@pet.com', '正常', '2024-01-01 10:00:00'),
('user001', '123456', '普通用户', '宠物爱好者小王', 'https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=400', '13800138001', 'user001@pet.com', '正常', '2024-01-02 10:00:00'),
('user002', '123456', '普通用户', '猫咪达人小李', 'https://images.unsplash.com/photo-1438761681033-6461ffad8d80?w=400', '13800138002', 'user002@pet.com', '正常', '2024-01-03 10:00:00'),
('user003', '123456', '普通用户', '狗狗主人小张', 'https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=400', '13800138003', 'user003@pet.com', '禁用', '2024-01-04 10:00:00'),
('user004', '123456', '普通用户', '萌宠小管家小赵', 'https://images.unsplash.com/photo-1544005313-94ddf0286df2?w=400', '13800138004', 'user004@pet.com', '正常', '2024-01-05 10:00:00'),
('user005', '123456', '普通用户', '宠物医生小刘', 'https://images.unsplash.com/photo-1506794778202-cad84cf45f1d?w=400', '13800138005', 'user005@pet.com', '正常', '2024-01-06 10:00:00'),
('user006', '123456', '普通用户', '养猫大师小周', 'https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=400', '13800138006', 'user006@pet.com', '正常', '2024-01-07 10:00:00'),
('user007', '123456', '普通用户', '宠物训练师小孙', 'https://images.unsplash.com/photo-1539571696357-5a69c17a67c6?w=400', '13800138007', 'user007@pet.com', '正常', '2024-01-08 10:00:00'),
('user008', '123456', '普通用户', '爱狗人士小钱', 'https://images.unsplash.com/photo-1504593811423-6dd665756598?w=400', '13800138008', 'user008@pet.com', '正常', '2024-01-09 10:00:00'),
('user009', '123456', '普通用户', '猫奴小吴', 'https://images.unsplash.com/photo-1529626455594-4ff0802cfb7e?w=400', '13800138009', 'user009@pet.com', '正常', '2024-01-10 10:00:00'),
('user010', '123456', '普通用户', '铲屎官小郑', 'https://images.unsplash.com/photo-1568602471122-7832951cc4c5?w=400', '13800138010', 'user010@pet.com', '正常', '2024-01-11 10:00:00'),
('user011', '123456', '普通用户', '宠物摄影师小冯', 'https://images.unsplash.com/photo-1463453091185-61582044d556?w=400', '13800138011', 'user011@pet.com', '正常', '2024-01-12 10:00:00'),
('user012', '123456', '普通用户', '养宠达人小陈', 'https://images.unsplash.com/photo-1552058544-f2b08422138a?w=400', '13800138012', 'user012@pet.com', '正常', '2024-01-13 10:00:00'),
('user013', '123456', '普通用户', '宠物美容师小许', 'https://images.unsplash.com/photo-1531746020798-e6953c6e8e04?w=400', '13800138013', 'user013@pet.com', '正常', '2024-01-14 10:00:00'),
('user014', '123456', '普通用户', '资深猫奴小马', 'https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=400', '13800138014', 'user014@pet.com', '正常', '2024-01-15 10:00:00'),
('user015', '123456', '普通用户', '狗狗教练小丁', 'https://images.unsplash.com/photo-1492562080023-ab3db95bfbce?w=400', '13800138015', 'user015@pet.com', '正常', '2024-01-16 10:00:00');

-- 删除系统配置表（如果存在）
DROP TABLE IF EXISTS system_config;

-- 创建系统配置表
CREATE TABLE system_config (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    system_name VARCHAR(100) NOT NULL COMMENT '系统名称',
    carousel_images VARCHAR(2000) DEFAULT NULL COMMENT '轮播图URL列表，JSON格式',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统配置表';

-- 插入默认配置
INSERT INTO system_config (system_name, carousel_images, create_time) VALUES
('伴侣宠物之家系统', '["https://images.unsplash.com/photo-1601758228041-f3b2795255f1?w=1200","https://images.unsplash.com/photo-1543466835-00a7907e9de1?w=1200","https://images.unsplash.com/photo-1574158622682-e40e69881006?w=1200"]', '2024-01-01 09:00:00');

-- 删除商品分类表（如果存在）
DROP TABLE IF EXISTS category;

-- 创建商品分类表
CREATE TABLE category (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    name VARCHAR(50) NOT NULL COMMENT '分类名称',
    description VARCHAR(500) DEFAULT NULL COMMENT '分类描述',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品分类表';

-- 插入示例数据（至少5条）
INSERT INTO category (name, description, create_time) VALUES
('宠物食品', '各类宠物的主粮、零食、营养品等', '2024-01-01 10:00:00'),
('宠物用品', '宠物日常使用的各类用品，如玩具、窝垫等', '2024-01-01 11:00:00'),
('宠物医疗', '宠物医疗用品、保健品、药品等', '2024-01-01 12:00:00'),
('宠物清洁', '宠物洗护、清洁、除臭等用品', '2024-01-01 13:00:00'),
('宠物服饰', '宠物衣服、配饰、装扮用品等', '2024-01-01 14:00:00'),
('宠物出行', '宠物外出用品，如背包、牵引绳等', '2024-01-01 15:00:00'),
('宠物训练', '宠物训练用品和辅助工具', '2024-01-01 16:00:00');

-- 删除商品表（如果存在）
DROP TABLE IF EXISTS product;

-- 创建商品表
CREATE TABLE product (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    images VARCHAR(2000) DEFAULT NULL COMMENT '商品图片URL，多个用逗号分隔，最多5个',
    name VARCHAR(200) NOT NULL COMMENT '商品名称',
    category_id BIGINT NOT NULL COMMENT '商品分类ID',
    description VARCHAR(1000) DEFAULT NULL COMMENT '商品描述',
    brand VARCHAR(100) DEFAULT NULL COMMENT '品牌',
    pet_type VARCHAR(20) NOT NULL COMMENT '适用宠物类型：狗、猫',
    price DECIMAL(10, 2) NOT NULL COMMENT '价格',
    status VARCHAR(20) NOT NULL DEFAULT '上架' COMMENT '状态：上架、下架',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品表';

-- 插入示例数据
INSERT INTO product (images, name, category_id, description, brand, pet_type, price, status, create_time) VALUES
('https://images.unsplash.com/photo-1589924691995-400dc9ecc119?w=600,https://images.unsplash.com/photo-1583511655857-d19b40a7a54e?w=600,https://images.unsplash.com/photo-1558929996-da64ba858215?w=600', '皇家狗粮中型犬成犬粮10kg', 1, '皇家中型犬成犬粮专为1-7岁的中型犬（11-25kg）研制，采用科学配方，富含优质动物蛋白和L-肉碱，帮助维持理想体重和肌肉质量。添加EPA/DHA和多种维生素，促进皮肤健康和毛发亮泽。独特的颗粒形状设计，鼓励犬只充分咀嚼，有助于口腔健康。不含人工色素、香料和防腐剂，安全可靠，是千万宠物家庭的信赖之选。', '皇家', '狗', 368.00, '上架', '2024-01-10 09:00:00'),
('https://images.unsplash.com/photo-1568152950566-c1bf43f4ab28?w=600,https://images.unsplash.com/photo-1535591273668-578e31182c4f?w=600', '渴望猫咪天然粮海洋鱼味5kg', 1, '渴望海洋鱼味猫粮采用生物学适当配方，含85%优质动物原料，15%蔬菜水果，0谷物添加。精选新鲜深海鱼（包括三文鱼、鲱鱼、比目鱼）作为主要蛋白质来源，富含Omega-3和Omega-6脂肪酸，促进皮肤健康和毛发光泽。添加天然益生菌和益生元，支持消化系统健康。冷冻干燥技术保留营养成分，WDJ推荐品牌，适合全阶段猫咪食用，让您的猫咪享受自然美味的同时获得全面营养。', '渴望', '猫', 299.00, '上架', '2024-01-11 10:00:00'),
('https://images.unsplash.com/photo-1616012734987-0a8a3a4d1b98?w=600,https://images.unsplash.com/photo-1597075095886-e0caad0b2c70?w=600,https://images.unsplash.com/photo-1589941013453-ec89f33b5e95?w=600', '小佩宠物自动饮水器猫狗通用2L大容量', 2, '小佩智能宠物饮水器采用循环过滤系统，通过三重过滤（PP棉+活性炭+离子交换树脂）净化水质，去除杂质、异味和重金属，为宠物提供新鲜健康的饮用水。流动活水设计模拟自然泉水，吸引宠物主动饮水，有效预防泌尿系统疾病。超静音水泵（<40分贝），24小时运行不扰民。2L大容量适合多宠家庭和外出2-3天使用。透明水箱可视化设计，方便观察水量。可拆卸结构，清洗方便。省电节能，功率仅2W。', '小佩', '狗', 128.00, '上架', '2024-01-12 11:00:00'),
('https://images.unsplash.com/photo-1561948955-570b270e7c36?w=600', '爱丽思猫砂盆全封闭式防外溅加大号', 2, '爱丽思全封闭猫砂盆采用人性化设计，顶部进出方式有效防止猫砂外溅，保持家居清洁。加大内部空间（53x40x43cm），适合大型猫咪使用，让猫咪有足够空间转身和掩埋。双层过滤踏板设计，猫咪走出时自动清理脚底猫砂，减少猫砂浪费。全封闭结构有效控制异味扩散，配合活性炭除臭盒使用效果更佳。顶盖可完全打开，方便日常清理。优质PP材质，安全无毒，耐用易清洗。多色可选，简约设计融入各种家居风格。', '爱丽思', '猫', 159.00, '上架', '2024-01-13 12:00:00'),
('https://images.unsplash.com/photo-1587300003388-59208cc962cb?w=600,https://images.unsplash.com/photo-1548199973-03cce0bbc87b?w=600', '谷登狗狗营养膏幼犬成犬补钙增强免疫120g', 3, '谷登宠物营养膏专为狗狗研发，富含50多种营养成分，包括复合维生素、矿物质、氨基酸、牛磺酸等。特别添加钙、磷、维生素D3，促进骨骼和牙齿发育，预防佝偻病和软骨症。添加益生菌和益生元，调理肠胃，改善消化吸收。增强免疫力配方，帮助幼犬健康成长，助力成犬保持活力。适口性极佳，狗狗爱吃。适用于幼犬、怀孕哺乳期母犬、体弱犬、术后恢复期犬只。每天2-3次，每次3-5cm，可直接喂食或拌入食物中。', '谷登', '狗', 68.00, '下架', '2024-01-14 13:00:00'),
('https://images.unsplash.com/photo-1556229010-5c3f1c64d4b2?w=600,https://images.unsplash.com/photo-1629043598308-e810aa47dc57?w=600,https://images.unsplash.com/photo-1594149929610-5ac11bfdb02f?w=600', '雪貂宠物沐浴露猫咪专用除菌去异味500ml', 4, '雪貂猫咪专用沐浴露采用温和低敏配方，PH值接近猫咪皮肤，不刺激眼睛和皮肤。天然植物提取成分（芦荟精华、洋甘菊、茶树油），深层清洁毛发和皮肤，有效去除油脂、污垢和异味。添加抑菌因子，预防皮肤病和寄生虫。含丝蛋白和维生素E，滋养毛发，修复受损毛鳞片，洗后毛发柔顺亮泽，蓬松飘逸。清新花果香味，留香持久不刺鼻。易冲洗不残留，适合长毛猫和短毛猫使用。500ml大容量，一瓶可用3-4个月。', '雪貂', '猫', 49.00, '上架', '2024-01-15 14:00:00'),
('https://images.unsplash.com/photo-1583512603806-077998240c7a?w=600', 'Touchdog狗狗衣服冬季保暖加厚四脚衣', 5, 'Touchdog冬季保暖四脚衣采用双层夹棉设计，外层防风防水面料，内层柔软抓绒，温暖舒适。四脚设计全方位保暖，保护狗狗四肢不受寒。背部加宽加厚，重点保护胸腹部等敏感部位。松紧袖口设计，防风保暖的同时不勒腿。魔术贴+按扣双重固定，穿脱方便不易脱落。背部预留牵引绳孔，外出散步更便捷。精致车工，走线细密，耐穿耐洗不变形。多种尺码可选，适合泰迪、比熊、雪纳瑞、柯基等中小型犬。多色可选，时尚保暖两不误。', 'Touchdog', '狗', 89.00, '上架', '2024-01-16 15:00:00'),
('https://images.unsplash.com/photo-1580489944761-15a19d654956?w=600,https://images.unsplash.com/photo-1569890795351-58e3f6cdc742?w=600', 'PETKIT宠物外出包猫咪背包太空舱透气便携', 6, 'PETKIT太空舱宠物背包融合时尚与实用，半球形透明舱设计，视野开阔，缓解宠物焦虑。优质PC材质舱体，坚固耐用，透光通风。环绕透气孔+透气网布，多方位通风系统，即使长时间外出也保持空气流通。人体工学肩带，加宽加厚，分散压力，背负舒适。内置安全扣，防止宠物中途逃脱。底部防滑耐磨，放置稳固。内置可拆卸软垫，可机洗，保持清洁卫生。多个收纳袋设计，可放置零食、水瓶、玩具等。承重约8kg，适合猫咪和小型犬。时尚外观，出街回头率超高。', 'PETKIT', '猫', 138.00, '上架', '2024-01-17 16:00:00'),
('https://images.unsplash.com/photo-1585664811087-47f65abbad64?w=600,https://images.unsplash.com/photo-1628009368231-7bb7cfcb0def?w=600', '比瑞吉狗粮泰迪贵宾专用小型犬成犬粮3kg', 1, '比瑞吉泰迪贵宾专用粮针对小型犬特点研发，小颗粒设计易于咀嚼和消化。精选鸡肉、鸭肉作为优质蛋白来源，蛋白质含量≥25%，满足小型犬高代谢需求。添加深海鱼油和卵磷脂，美毛亮毛，改善泪痕问题。富含膳食纤维，促进肠道蠕动，便便成型不臭。添加软骨素和葡萄糖胺，保护关节健康。含丝兰提取物，减少粪便异味。无谷物配方，降低过敏风险。适合10个月以上泰迪、比熊、博美等小型犬食用。', '比瑞吉', '狗', 158.00, '上架', '2024-01-18 09:00:00'),
('https://images.unsplash.com/photo-1573865526739-10c1d3a1f0cc?w=600,https://images.unsplash.com/photo-1611003228941-98852ba62227?w=600', '网易严选冻干猫粮双拼鸡肉鱼肉全阶段1.8kg', 1, '网易严选冻干双拼猫粮采用创新工艺，将冻干生骨肉与烘焙粮完美结合。80%动物原料（鸡肉+鱼肉），高蛋白低碳水，符合猫咪天然饮食习惯。冷冻干燥技术最大程度保留肉质营养和风味，适口性极佳，挑食猫也爱吃。添加12种维生素和矿物质，营养全面均衡。含牛磺酸、DHA，促进视力和大脑发育。益生元+益生菌组合，呵护肠胃健康。无谷配方，低敏配方，适合肠胃敏感猫咪。全阶段适用，幼猫成猫老年猫都可以吃。', '网易严选', '猫', 228.00, '上架', '2024-01-18 10:00:00'),
('https://images.unsplash.com/photo-1623387641168-d9803ddd3f35?w=600,https://images.unsplash.com/photo-1615751072497-5f5169febe17?w=600,https://images.unsplash.com/photo-1511512578047-dfb367046420?w=600', '福来恩猫狗体外驱虫药跳蚤蜱虫滴剂3支装', 3, '福来恩是法国进口的体外驱虫药，有效成分为非泼罗尼。一次使用可持续杀灭跳蚤、蜱虫、虱子等体外寄生虫长达30天。24小时内杀灭100%跳蚤，48小时内杀灭蜱虫。防水配方，洗澡游泳不影响药效。滴在颈部皮肤即可，操作简单，宠物无感。安全性高，怀孕哺乳期也可使用。单独包装，每月一支，使用方便。适合8周龄以上、体重≥1kg的猫犬使用。预防优于治疗，定期驱虫让宠物远离寄生虫困扰，保护全家健康。', '福来恩', '狗', 198.00, '上架', '2024-01-19 11:00:00'),
('https://images.unsplash.com/photo-1598134493776-854e8af0ab3e?w=600,https://images.unsplash.com/photo-1582435249284-0c3b58b0f32b?w=600', '宠物益生菌猫狗通用调理肠胃腹泻呕吐30包', 3, '宠物专用益生菌含10种活性益生菌菌株（120亿CFU/包），包括乳酸菌、双歧杆菌、枯草芽孢杆菌等，调节肠道菌群平衡。添加益生元（果寡糖、低聚木糖），促进有益菌增殖。含消化酶（蛋白酶、淀粉酶、纤维素酶），帮助分解食物，提高营养吸收率。有效改善腹泻、便秘、呕吐、食欲不振、便臭等肠胃问题。适用于换粮期、打疫苗后、服用抗生素后、应激反应等情况。粉末状，适口性好，拌入食物或直接喂食均可。独立小包装，每天1-2包，携带方便。适合全年龄段猫狗使用。', '宠儿香', '狗', 78.00, '上架', '2024-01-19 14:00:00'),
('https://images.unsplash.com/photo-1608483875649-e8e1df4e2c0f?w=600', '宠物指甲剪猫狗专用指甲刀磨甲器套装', 2, '宠物专用指甲护理套装包含指甲剪+指甲锉+止血粉。指甲剪采用优质不锈钢刀头，锋利耐用，斜角设计精准修剪不劈裂。人体工学防滑手柄，省力舒适，新手也能轻松操作。安全护罩设计，防止剪到血线。赠送指甲锉，打磨指甲边缘，避免刮伤人和家具。配备止血粉，万一剪到血线可快速止血。适用于猫、狗、兔、龙猫等小型宠物。定期修剪指甲可防止指甲过长刺入肉垫，预防感染和行走不适，也能保护家具不被抓花。', '洛克优品', '狗', 35.00, '上架', '2024-01-20 09:00:00'),
('https://images.unsplash.com/photo-1565033920687-f0e4abc56a94?w=600,https://images.unsplash.com/photo-1598384894785-fd58c7e2fa30?w=600', '猫咪玩具套装逗猫棒羽毛铃铛球逗猫猫10件套', 2, '猫咪玩具豪华10件套，满足猫咪多样化玩耍需求。包含逗猫棒2支（羽毛款+铃铛款）、弹簧鼠、毛绒球、响纸球、轨道球、猫薄荷玩具等。多种材质和玩法，激发猫咪狩猎天性，增加运动量，预防肥胖。逗猫棒可伸缩设计，长度可调，增加互动乐趣，增进主人与猫咪感情。轨道球可单猫自嗨，主人不在家也不无聊。猫薄荷玩具让猫咪兴奋放松，缓解压力。所有玩具采用安全无毒材质，可放心让猫咪玩耍。丰富猫咪生活，让猫咪快乐健康成长。', '多格漫', '猫', 45.00, '上架', '2024-01-20 10:00:00'),
('https://images.unsplash.com/photo-1605981993417-102c7e5fc1f6?w=600,https://images.unsplash.com/photo-1532684723385-1103b0ebb6cf?w=600', '狗狗零食鸡肉干训练奖励磨牙棒500g', 1, '纯鸡胸肉制作的狗狗零食，无添加防腐剂、色素、香精。选用新鲜鸡胸肉，经过低温烘干处理，最大程度保留肉质营养和天然风味。高蛋白低脂肪，健康营养。条状设计，大小适中，既可作为训练奖励，也能作为磨牙零食，清洁牙齿，预防牙结石。适口性极佳，挑食狗也爱吃。独立包装，保持新鲜，携带方便，外出训练必备。适合3个月以上所有犬种食用。每天喂食量不超过正餐的10%，避免影响主食摄入。让训练变得更有趣，让狗狗更听话。', '宠幸', '狗', 58.00, '上架', '2024-01-20 15:00:00'),
('https://images.unsplash.com/photo-1526336024174-e58f5cdd8e13?w=600', '猫砂膨润土除臭无尘猫沙10kg', 2, '优质膨润土猫砂，采用纳米级膨润土原料，吸水性强，瞬间结团。3秒快速结团，紧实不散，易于清理，减少浪费。99%除尘工艺，粉尘极少，保护猫咪和主人呼吸健康。添加活性炭和天然植物除臭因子，强效锁臭，保持室内空气清新。颗粒大小适中（2-4mm），脚感舒适，猫咪喜欢。包裹性好，带出少，保持家居清洁。10kg大容量，单猫可用1-2个月。性价比高，是养猫家庭的经济实惠之选。建议每天清理结团，每周全部更换一次，保持猫砂盆清洁。', '喵洁客', '猫', 45.00, '上架', '2024-01-21 09:00:00');

-- 删除购物车表（如果存在）
DROP TABLE IF EXISTS cart;

-- 创建购物车表
CREATE TABLE cart (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    product_id BIGINT NOT NULL COMMENT '商品ID',
    quantity INT NOT NULL DEFAULT 1 COMMENT '数量',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='购物车表';

-- 插入示例数据（至少5条）
INSERT INTO cart (user_id, product_id, quantity, create_time) VALUES
(2, 1, 2, '2024-01-20 10:00:00'),
(2, 3, 1, '2024-01-20 11:00:00'),
(2, 6, 3, '2024-01-20 12:00:00'),
(3, 2, 1, '2024-01-21 10:00:00'),
(3, 4, 2, '2024-01-21 11:00:00'),
(4, 1, 1, '2024-01-22 10:00:00'),
(4, 7, 2, '2024-01-22 11:00:00');

-- 删除订单表（如果存在）
DROP TABLE IF EXISTS orders;

-- 创建订单表
CREATE TABLE orders (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    order_no VARCHAR(50) NOT NULL COMMENT '订单编号',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    total_amount DECIMAL(10, 2) NOT NULL COMMENT '订单总金额',
    receiver_name VARCHAR(50) NOT NULL COMMENT '收货人姓名',
    receiver_phone VARCHAR(20) NOT NULL COMMENT '收货人电话',
    receiver_address VARCHAR(500) NOT NULL COMMENT '收货地址',
    status VARCHAR(20) NOT NULL DEFAULT '待发货' COMMENT '订单状态：待发货、已发货、已收货',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表';

-- 插入示例数据（至少5条）
INSERT INTO orders (order_no, user_id, total_amount, receiver_name, receiver_phone, receiver_address, status, create_time) VALUES
('ORD202401250001', 2, 883.00, '张三', '13800138001', '北京市朝阳区建国路88号', '已收货', '2024-01-25 10:00:00'),
('ORD202401250002', 2, 299.00, '张三', '13800138001', '北京市朝阳区建国路88号', '已发货', '2024-01-25 11:30:00'),
('ORD202401250003', 3, 318.00, '李四', '13800138002', '上海市浦东新区世纪大道100号', '待发货', '2024-01-25 14:20:00'),
('ORD202401260001', 4, 368.00, '王五', '13800138003', '广州市天河区天河路123号', '已发货', '2024-01-26 09:15:00'),
('ORD202401260002', 5, 425.00, '赵六', '13800138004', '深圳市南山区科技园路666号', '待发货', '2024-01-26 15:45:00'),
('ORD202401270001', 2, 217.00, '张三', '13800138001', '北京市朝阳区建国路88号', '已收货', '2024-01-27 10:30:00'),
('ORD202401270002', 3, 138.00, '李四', '13800138002', '上海市浦东新区世纪大道100号', '待发货', '2024-01-27 16:00:00');

-- 删除订单详情表（如果存在）
DROP TABLE IF EXISTS order_detail;

-- 创建订单详情表
CREATE TABLE order_detail (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    order_id BIGINT NOT NULL COMMENT '订单ID',
    product_id BIGINT NOT NULL COMMENT '商品ID',
    product_name VARCHAR(200) NOT NULL COMMENT '商品名称',
    product_image VARCHAR(500) DEFAULT NULL COMMENT '商品图片URL',
    price DECIMAL(10, 2) NOT NULL COMMENT '商品单价',
    quantity INT NOT NULL DEFAULT 1 COMMENT '购买数量',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单详情表';

-- 插入示例数据
INSERT INTO order_detail (order_id, product_id, product_name, product_image, price, quantity, create_time) VALUES
(1, 1, '皇家狗粮中型犬成犬粮10kg', 'https://images.unsplash.com/photo-1589924691995-400dc9ecc119?w=600', 368.00, 2, '2024-01-25 10:00:00'),
(1, 6, '雪貂宠物沐浴露猫咪专用除菌去异味500ml', 'https://images.unsplash.com/photo-1556229010-5c3f1c64d4b2?w=600', 49.00, 3, '2024-01-25 10:00:00'),
(2, 2, '渴望猫咪天然粮海洋鱼味5kg', 'https://images.unsplash.com/photo-1568152950566-c1bf43f4ab28?w=600', 299.00, 1, '2024-01-25 11:30:00'),
(3, 4, '爱丽思猫砂盆全封闭式防外溅加大号', 'https://images.unsplash.com/photo-1561948955-570b270e7c36?w=600', 159.00, 2, '2024-01-25 14:20:00'),
(4, 1, '皇家狗粮中型犬成犬粮10kg', 'https://images.unsplash.com/photo-1589924691995-400dc9ecc119?w=600', 368.00, 1, '2024-01-26 09:15:00'),
(5, 3, '小佩宠物自动饮水器猫狗通用2L大容量', 'https://images.unsplash.com/photo-1616012734987-0a8a3a4d1b98?w=600', 128.00, 1, '2024-01-26 15:45:00'),
(5, 2, '渴望猫咪天然粮海洋鱼味5kg', 'https://images.unsplash.com/photo-1568152950566-c1bf43f4ab28?w=600', 299.00, 1, '2024-01-26 15:45:00'),
(6, 7, 'Touchdog狗狗衣服冬季保暖加厚四脚衣', 'https://images.unsplash.com/photo-1583512603806-077998240c7a?w=600', 89.00, 1, '2024-01-27 10:30:00'),
(6, 3, '小佩宠物自动饮水器猫狗通用2L大容量', 'https://images.unsplash.com/photo-1616012734987-0a8a3a4d1b98?w=600', 128.00, 1, '2024-01-27 10:30:00'),
(7, 8, 'PETKIT宠物外出包猫咪背包太空舱透气便携', 'https://images.unsplash.com/photo-1580489944761-15a19d654956?w=600', 138.00, 1, '2024-01-27 16:00:00');

-- 删除评价表（如果存在）
DROP TABLE IF EXISTS review;

-- 创建评价表
CREATE TABLE review (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    order_id BIGINT NOT NULL COMMENT '订单ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    product_id BIGINT NOT NULL COMMENT '商品ID',
    content VARCHAR(1000) NOT NULL COMMENT '评价内容',
    rating INT NOT NULL COMMENT '评分：1-5星',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='评价表';

-- 插入示例数据（至少5条）
INSERT INTO review (order_id, user_id, product_id, content, rating, create_time) VALUES
(1, 2, 1, '狗粮质量很好，我家狗狗很喜欢吃，营养也很丰富，会继续购买！', 5, '2024-01-26 10:00:00'),
(1, 2, 6, '沐浴露味道很清新，洗完毛发很柔顺，去污能力强，物有所值！', 4, '2024-01-26 10:05:00'),
(6, 2, 7, '衣服质量不错，做工精细，保暖性好，尺码也合适，狗狗穿上很可爱！', 5, '2024-01-28 11:00:00'),
(6, 2, 3, '饮水器设计很人性化，过滤效果好，声音也很小，猫咪很喜欢喝水了！', 5, '2024-01-28 11:05:00'),
(4, 4, 1, '狗粮还可以，但是感觉颗粒有点大，不太适合小型犬，但质量是不错的。', 3, '2024-01-27 09:00:00');

-- 删除宠物保险表（如果存在）
DROP TABLE IF EXISTS insurance;

-- 创建宠物保险表
CREATE TABLE insurance (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    name VARCHAR(200) NOT NULL COMMENT '保险名称',
    detail VARCHAR(2000) DEFAULT NULL COMMENT '保险详情',
    price DECIMAL(10, 2) NOT NULL COMMENT '价格/年',
    coverage VARCHAR(1000) DEFAULT NULL COMMENT '保险范围',
    claim_notice VARCHAR(2000) DEFAULT NULL COMMENT '理赔须知',
    claim_limit VARCHAR(200) DEFAULT NULL COMMENT '理赔限额',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='宠物保险表';

-- 插入示例数据（至少5条）
INSERT INTO insurance (name, detail, price, coverage, claim_notice, claim_limit, create_time) VALUES
('宠物健康综合保险', '为您的宠物提供全面的健康保障，涵盖意外伤害、疾病治疗、住院费用等多项保障内容。投保简单，理赔快捷，让您和爱宠无忧。', 699.00, '意外伤害医疗、疾病医疗、住院津贴、手术费用、药品费用', '需提供正规医院就诊证明和发票；意外事故需在48小时内报案；疾病治疗需等待期30天；理赔材料需完整真实；单次理赔金额超过5000元需提供详细医疗记录', '年度累计赔付限额50000元，单次赔付不超过20000元', '2024-01-10 09:00:00'),
('宠物意外伤害保险', '专门针对宠物意外伤害设计的保险产品，保障范围包括跌落、碰撞、咬伤、烧伤等各类意外事故造成的医疗费用。', 399.00, '意外伤害医疗、意外骨折治疗、意外烧烫伤、误食中毒治疗', '意外事故需在24小时内报案；需提供事故现场照片或证明；医疗费用凭正规发票报销；等待期7天', '年度累计赔付限额30000元，单次赔付不超过10000元', '2024-01-11 10:00:00'),
('宠物疾病医疗保险', '针对宠物常见疾病提供医疗保障，涵盖呼吸道疾病、消化系统疾病、皮肤病、传染病等多种疾病的治疗费用。', 899.00, '疾病门诊、疾病住院、疾病手术、传染病治疗、慢性病治疗', '等待期60天；需在指定医院就诊；提供完整病历和检查报告；预防性治疗不在保障范围；既往病史不予赔付', '年度累计赔付限额80000元，单次赔付不超过30000元', '2024-01-12 11:00:00'),
('宠物责任险', '为宠物造成的第三方人身伤害或财产损失提供赔偿保障，让宠物主人免除后顾之忧，安心养宠。', 299.00, '第三方人身伤害、第三方财产损失、法律诉讼费用、紧急医疗费用', '事故发生后立即报案；配合保险公司调查；提供事故证明材料；不包含故意行为造成的损失', '年度累计赔付限额100000元，单次赔付不超过50000元', '2024-01-13 12:00:00'),
('宠物手术费用保险', '专门保障宠物手术费用的保险产品，涵盖各类外科手术、骨科手术、绝育手术等医疗费用。', 599.00, '外科手术、骨科手术、绝育手术、肿瘤切除手术、麻醉费用、术后护理', '需在指定医院进行手术；提供手术通知单和费用清单；等待期90天；美容性手术不在保障范围', '年度累计赔付限额60000元，单次手术赔付不超过25000元', '2024-01-14 13:00:00'),
('宠物全面保障计划', '集健康、意外、责任于一体的全方位保险方案，为您的爱宠提供最全面的保护，是宠物主人的最佳选择。', 1299.00, '意外伤害、疾病医疗、手术费用、住院津贴、第三方责任、宠物走失找寻', '全面保障需提供宠物健康证明；等待期30天；理赔时需提供完整材料；特殊疾病需额外审核；走失需在24小时内报案', '年度累计赔付限额150000元，单次赔付不超过50000元', '2024-01-15 14:00:00'),
('宠物门诊保险', '针对宠物日常门诊就医费用提供保障，包括疫苗接种、体检、常规治疗等费用，降低养宠成本。', 499.00, '门诊治疗、疫苗接种、健康体检、药品费用、化验检查', '需在保险公司指定医院就诊；提供门诊发票和病历；单次门诊费用超过500元需提前报备；保健品不在保障范围', '年度累计赔付限额15000元，单次门诊赔付不超过2000元', '2024-01-16 15:00:00');

-- 删除保险订单表（如果存在）
DROP TABLE IF EXISTS insurance_order;

-- 创建保险订单表
CREATE TABLE insurance_order (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    order_no VARCHAR(50) NOT NULL COMMENT '订单编号',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    insurance_id BIGINT NOT NULL COMMENT '保险ID',
    insurance_name VARCHAR(200) NOT NULL COMMENT '保险名称',
    price DECIMAL(10, 2) NOT NULL COMMENT '保险价格',
    pet_name VARCHAR(50) NOT NULL COMMENT '宠物名称',
    pet_type VARCHAR(20) NOT NULL COMMENT '宠物类型',
    pet_age INT NOT NULL COMMENT '宠物年龄',
    pet_breed VARCHAR(50) DEFAULT NULL COMMENT '宠物品种',
    payee_name VARCHAR(50) NOT NULL COMMENT '收款人姓名',
    payee_phone VARCHAR(20) NOT NULL COMMENT '收款人电话',
    payee_account VARCHAR(100) NOT NULL COMMENT '收款账号',
    start_date DATE NOT NULL COMMENT '保险生效日期',
    end_date DATE NOT NULL COMMENT '保险到期日期',
    status VARCHAR(20) NOT NULL DEFAULT '生效中' COMMENT '订单状态：生效中、已过期',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='保险订单表';

-- 插入示例数据（至少5条）
INSERT INTO insurance_order (order_no, user_id, insurance_id, insurance_name, price, pet_name, pet_type, pet_age, pet_breed, payee_name, payee_phone, payee_account, start_date, end_date, status, create_time) VALUES
('INS202401280001', 2, 1, '宠物健康综合保险', 699.00, '小白', '狗', 3, '金毛', '张三', '13800138001', '6222021234567890123', '2024-01-28', '2025-01-27', '生效中', '2024-01-28 10:00:00'),
('INS202401280002', 2, 2, '宠物意外伤害保险', 399.00, '咪咪', '猫', 2, '英短', '张三', '13800138001', '6222021234567890123', '2024-01-28', '2025-01-27', '生效中', '2024-01-28 11:00:00'),
('INS202401290001', 3, 3, '宠物疾病医疗保险', 899.00, '旺财', '狗', 5, '泰迪', '李四', '13800138002', '6222028765432109876', '2024-01-29', '2025-01-28', '生效中', '2024-01-29 09:30:00'),
('INS202401290002', 4, 4, '宠物责任险', 299.00, '大黄', '狗', 4, '中华田园犬', '王五', '13800138003', '6222025555666677778', '2024-01-29', '2025-01-28', '生效中', '2024-01-29 14:20:00'),
('INS202401300001', 5, 5, '宠物手术费用保险', 599.00, '花花', '猫', 1, '美短', '赵六', '13800138004', '6222029999888877776', '2024-01-30', '2025-01-29', '生效中', '2024-01-30 08:45:00'),
('INS202312150001', 2, 6, '宠物全面保障计划', 1299.00, '豆豆', '狗', 6, '柯基', '张三', '13800138001', '6222021234567890123', '2023-12-15', '2024-12-14', '已过期', '2023-12-15 10:00:00');

-- 删除理赔申请表（如果存在）
DROP TABLE IF EXISTS insurance_claim;

-- 创建理赔申请表
CREATE TABLE insurance_claim (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    claim_no VARCHAR(50) NOT NULL COMMENT '理赔编号',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    order_id BIGINT NOT NULL COMMENT '保险订单ID',
    insurance_name VARCHAR(200) NOT NULL COMMENT '保险名称',
    pet_name VARCHAR(50) NOT NULL COMMENT '宠物名称',
    reason VARCHAR(500) NOT NULL COMMENT '申请原因',
    description VARCHAR(2000) NOT NULL COMMENT '情况描述',
    evidence_images VARCHAR(2000) DEFAULT NULL COMMENT '佐证图片URL，多个用逗号分隔，最多5个',
    claim_amount DECIMAL(10, 2) DEFAULT NULL COMMENT '理赔金额',
    status VARCHAR(20) NOT NULL DEFAULT '待审核' COMMENT '状态：待审核、审核通过、审核拒绝、已打款',
    reject_reason VARCHAR(500) DEFAULT NULL COMMENT '拒绝原因',
    admin_remark VARCHAR(500) DEFAULT NULL COMMENT '管理员备注',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='理赔申请表';

-- 插入示例数据（至少5条）
INSERT INTO insurance_claim (claim_no, user_id, order_id, insurance_name, pet_name, reason, description, evidence_images, claim_amount, status, reject_reason, admin_remark, create_time) VALUES
('CLM202402010001', 2, 1, '宠物健康综合保险', '小白', '宠物意外受伤需要治疗', '我的狗狗小白在公园玩耍时不慎摔伤，导致前腿骨折，已在宠物医院进行了手术治疗，花费医疗费用8500元。现申请理赔，附上医院诊断证明、手术记录和费用发票。', 'https://images.unsplash.com/photo-1559523161-0fc0d8b38a7a?w=600,https://images.unsplash.com/photo-1576086213369-97a306d36557?w=600,https://images.unsplash.com/photo-1614941059122-c7f8b828e5ad?w=600', 8500.00, '已打款', NULL, '审核通过，理赔金额8500元已打款至您的账户', '2024-02-01 10:00:00'),
('CLM202402020001', 3, 3, '宠物疾病医疗保险', '旺财', '宠物生病住院治疗', '我的狗狗旺财患上了肠胃炎，在医院住院治疗5天，产生医疗费用3200元。现申请理赔，附上住院病历、检查报告和费用清单。', 'https://images.unsplash.com/photo-1535930891776-0c2dfb7fda1a?w=600,https://images.unsplash.com/photo-1617791160536-598cf32026fb?w=600', 3200.00, '审核通过', NULL, '审核通过，待打款', '2024-02-02 09:30:00'),
('CLM202402030001', 2, 2, '宠物意外伤害保险', '咪咪', '宠物意外烫伤', '我的猫咪咪咪不小心被热水烫伤，已在医院进行治疗，花费1500元。现申请理赔。', 'https://images.unsplash.com/photo-1548681528-6a5c45b66b42?w=600', NULL, '待审核', NULL, NULL, '2024-02-03 14:20:00'),
('CLM202402040001', 4, 4, '宠物责任险', '大黄', '宠物咬伤他人', '我的狗狗大黄不小心咬伤了邻居，对方产生医疗费用2000元。现申请理赔，附上事故证明和医疗发票。', 'https://images.unsplash.com/photo-1598387181032-a3103a2db5b3?w=600,https://images.unsplash.com/photo-1603147627908-79013d8e3be1?w=600', NULL, '审核拒绝', '经核实，事故发生时您的宠物未佩戴牵引绳，不符合理赔条件', '不符合理赔条件', '2024-02-04 11:00:00'),
('CLM202402050001', 5, 5, '宠物手术费用保险', '花花', '宠物绝育手术', '我的猫咪花花进行了绝育手术，产生手术费用1200元。现申请理赔，附上手术记录和费用清单。', 'https://images.unsplash.com/photo-1576201836106-db1758fd1c97?w=600', 1200.00, '审核通过', NULL, '审核通过，待打款', '2024-02-05 16:30:00');

-- 删除宠物医院表（如果存在）
DROP TABLE IF EXISTS hospital;

-- 创建宠物医院表
CREATE TABLE hospital (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    images VARCHAR(2000) DEFAULT NULL COMMENT '医院图片URL，多个用逗号分隔，最多5个',
    name VARCHAR(200) NOT NULL COMMENT '医院名称',
    introduction VARCHAR(2000) DEFAULT NULL COMMENT '医院介绍',
    services VARCHAR(500) DEFAULT NULL COMMENT '服务项目，多个标签用逗号分隔',
    address VARCHAR(500) NOT NULL COMMENT '医院地址',
    phone VARCHAR(50) NOT NULL COMMENT '联系电话',
    business_hours VARCHAR(200) NOT NULL COMMENT '营业时间',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='宠物医院表';

-- 插入示例数据
INSERT INTO hospital (images, name, introduction, services, address, phone, business_hours, create_time) VALUES
('https://images.unsplash.com/photo-1628009368231-7bb7cfcb0def?w=600,https://images.unsplash.com/photo-1583511655857-d19b40a7a54e?w=600,https://images.unsplash.com/photo-1625316708582-7c38734be31d?w=600', '爱宠动物医院', '爱宠动物医院是一家集宠物医疗、美容、寄养为一体的综合性宠物医院。医院拥有先进的医疗设备和专业的医疗团队，致力于为每一位宠物提供优质的医疗服务。我们秉承"用心呵护每一个生命"的服务理念，为宠物健康保驾护航。配备有数字X光机、彩色B超、血液生化分析仪、心电监护仪等先进设备，可进行各类疾病的精准诊断和治疗。', '疾病诊疗,疫苗接种,体检,手术,美容,寄养', '北京市朝阳区建国路88号宠物大厦3层', '010-12345678', '周一至周日 08:00-20:00', '2024-01-10 09:00:00'),
('https://images.unsplash.com/photo-1548681528-6a5c45b66b42?w=600,https://images.unsplash.com/photo-1598387181032-a3103a2db5b3?w=600,https://images.unsplash.com/photo-1603147627908-79013d8e3be1?w=600,https://images.unsplash.com/photo-1576201836106-db1758fd1c97?w=600', '康宁宠物医疗中心', '康宁宠物医疗中心是上海知名的宠物医疗机构，拥有20年的行业经验。医院配备了国际领先的宠物医疗设备，包括DR数字影像系统、全自动生化分析仪、血球计数仪、尿液分析仪等，能够为宠物提供全方位的医疗服务。我们的专家团队均有海外留学背景，医疗技术精湛，在骨科、眼科、皮肤科等专科领域有深入研究。医院环境整洁舒适，候诊区设有独立的犬猫候诊室，手术室达到洁净手术室标准。', '内科,外科,骨科,眼科,牙科,影像诊断,实验室检查', '上海市浦东新区世纪大道100号', '021-87654321', '周一至周日 24小时营业', '2024-01-11 10:00:00'),
('https://images.unsplash.com/photo-1597633425046-08f5110420b5?w=600,https://images.unsplash.com/photo-1623387641168-d9803ddd3f35?w=600,https://images.unsplash.com/photo-1581888227599-779811939961?w=600', '瑞派宠物医院', '瑞派宠物医院是中国领先的宠物医疗连锁品牌，致力于为宠物提供高品质的医疗服务。医院环境舒适，设施齐全，拥有专业的医疗团队和完善的医疗设备。我们提供从预防保健到疾病治疗的全流程服务，让您的爱宠得到最好的照顾。医院提供24小时急诊服务，配备专业的急诊医生和设备，随时为您的爱宠提供紧急救治。还开设中兽医科室，采用针灸、中药等传统疗法，为宠物提供更多治疗选择。', '疾病诊疗,预防保健,手术,住院,急诊,中兽医', '广州市天河区天河路123号', '020-55556666', '周一至周日 08:30-21:00', '2024-01-12 11:00:00'),
('https://images.unsplash.com/photo-1559523161-0fc0d8b38a7a?w=600,https://images.unsplash.com/photo-1576086213369-97a306d36557?w=600', '宠颐生动物医院', '宠颐生动物医院专注于宠物健康管理和疾病治疗，拥有多名执业兽医师和专业护理团队。医院设有独立的诊疗室、手术室、住院部，为宠物提供安全、舒适的医疗环境。我们注重医疗质量和服务品质，赢得了众多宠物主人的信赖。医院特别注重术后护理，住院部配备24小时监护系统，专业护理人员全天候照看住院宠物，确保宠物得到最好的康复护理。', '内科,外科,绝育手术,疫苗,驱虫,洗牙', '深圳市南山区科技园路666号', '0755-33334444', '周一至周日 09:00-19:00', '2024-01-13 12:00:00'),
('https://images.unsplash.com/photo-1606214174585-fe31582dc6ee?w=600,https://images.unsplash.com/photo-1587300003388-59208cc962cb?w=600,https://images.unsplash.com/photo-1543466835-00a7907e9de1?w=600,https://images.unsplash.com/photo-1601758228041-f3b2795255f1?w=600', '芭比堂动物医院', '芭比堂动物医院是一家专注于犬猫医疗的专科医院，拥有先进的诊疗设备和资深的医疗团队。医院提供全面的宠物医疗服务，包括内科、外科、皮肤科、眼科等多个科室。我们坚持"专业、专注、专心"的服务理念，为您的爱宠提供最优质的医疗保障。皮肤科配备皮肤镜、真菌培养设备等专业仪器，可精准诊断各类皮肤病。眼科配备眼压计、眼底镜等设备，为眼科疾病提供专业诊疗。', '内科,外科,皮肤科,眼科,耳科,化验检查,住院观察', '成都市武侯区人民南路四段888号', '028-88889999', '周一至周日 08:00-22:00', '2024-01-14 13:00:00'),
('https://images.unsplash.com/photo-1614941059122-c7f8b828e5ad?w=600,https://images.unsplash.com/photo-1535930891776-0c2dfb7fda1a?w=600', '安安宠物医院', '安安宠物医院是一家温馨的社区型宠物医院，为周边居民的爱宠提供便捷的医疗服务。医院虽小但设备齐全，医生经验丰富，对每一位宠物都像对待家人一样细心呵护。我们提供免费的健康咨询，让您的宠物健康成长。医院坚持亲民价格，让更多普通家庭的宠物也能享受到优质的医疗服务。定期举办宠物健康知识讲座，帮助宠物主人学习科学养宠知识。', '疾病诊疗,疫苗接种,体检,美容护理,健康咨询', '杭州市西湖区文二路200号', '0571-77778888', '周一至周六 09:00-18:00', '2024-01-15 14:00:00'),
('https://images.unsplash.com/photo-1617791160536-598cf32026fb?w=600,https://images.unsplash.com/photo-1628009368231-7bb7cfcb0def?w=600,https://images.unsplash.com/photo-1583511655857-d19b40a7a54e?w=600', '宠福鑫动物医院', '宠福鑫动物医院是一家专业的宠物医疗机构，拥有现代化的医疗设施和温馨的就诊环境。医院提供全科诊疗、专科手术、住院护理等多项服务。我们的医疗团队具有丰富的临床经验，能够快速准确地诊断和治疗各类宠物疾病，让您的爱宠尽快康复。医院特设康复理疗中心，引进宠物水疗设备、激光治疗仪等康复设备，为术后恢复、关节疾病、肌肉损伤等提供专业的康复治疗服务。', '全科诊疗,专科手术,住院护理,影像检查,实验室检测,康复理疗', '武汉市江汉区解放大道500号', '027-66665555', '周一至周日 24小时营业', '2024-01-16 15:00:00');

-- 删除医院预约表（如果存在）
DROP TABLE IF EXISTS appointment;

-- 创建医院预约表
CREATE TABLE appointment (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    hospital_id BIGINT NOT NULL COMMENT '医院ID',
    hospital_name VARCHAR(200) NOT NULL COMMENT '医院名称',
    pet_name VARCHAR(50) NOT NULL COMMENT '宠物名称',
    pet_type VARCHAR(20) NOT NULL COMMENT '宠物类型',
    condition_description VARCHAR(1000) NOT NULL COMMENT '病情描述',
    images VARCHAR(2000) DEFAULT NULL COMMENT '病情图片URL，多个用逗号分隔，最多5个',
    appointment_date DATE NOT NULL COMMENT '预约日期',
    appointment_time VARCHAR(20) NOT NULL COMMENT '预约时间',
    contact_phone VARCHAR(20) NOT NULL COMMENT '联系电话',
    status VARCHAR(20) NOT NULL DEFAULT '待赴约' COMMENT '状态：待赴约、已到店、已完成',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='医院预约表';

-- 插入示例数据
INSERT INTO appointment (user_id, hospital_id, hospital_name, pet_name, pet_type, condition_description, images, appointment_date, appointment_time, contact_phone, status, create_time) VALUES
(2, 1, '爱宠动物医院', '小白', '狗', '我家狗狗最近食欲不振，精神萎靡，偶尔会呕吐，希望医生帮忙检查一下。', 'https://images.unsplash.com/photo-1597633425046-08f5110420b5?w=600,https://images.unsplash.com/photo-1623387641168-d9803ddd3f35?w=600', '2024-02-10', '10:00', '13800138001', '已完成', '2024-02-08 10:00:00'),
(2, 2, '康宁宠物医疗中心', '咪咪', '猫', '猫咪右前腿好像受伤了，走路一瘸一拐的，不让碰，请医生帮忙看看。', 'https://images.unsplash.com/photo-1581888227599-779811939961?w=600,https://images.unsplash.com/photo-1601758228041-f3b2795255f1?w=600', '2024-02-12', '14:00', '13800138001', '已到店', '2024-02-09 14:30:00'),
(3, 3, '瑞派宠物医院', '旺财', '狗', '狗狗皮肤出现红疹，一直抓挠，有些地方已经抓破了，需要治疗。', 'https://images.unsplash.com/photo-1587300003388-59208cc962cb?w=600', '2024-02-13', '09:30', '13800138002', '待赴约', '2024-02-10 09:00:00'),
(4, 4, '宠颐生动物医院', '大黄', '狗', '狗狗需要做绝育手术，希望预约手术时间。', NULL, '2024-02-15', '10:30', '13800138003', '待赴约', '2024-02-10 16:00:00'),
(5, 5, '芭比堂动物医院', '花花', '猫', '猫咪眼睛经常流泪，有分泌物，看起来不舒服，想带来检查一下。', 'https://images.unsplash.com/photo-1543466835-00a7907e9de1?w=600,https://images.unsplash.com/photo-1574158622682-e40e69881006?w=600', '2024-02-14', '15:00', '13800138004', '待赴约', '2024-02-11 10:30:00'),
(2, 6, '安安宠物医院', '豆豆', '狗', '狗狗需要体检和疫苗接种，顺便做个全面检查。', NULL, '2024-02-16', '11:00', '13800138001', '已到店', '2024-02-11 14:00:00'),
(3, 1, '爱宠动物医院', '球球', '猫', '猫咪误食了异物，现在不吃不喝，非常着急，请尽快安排。', 'https://images.unsplash.com/photo-1514888286974-6c03e2ca1dba?w=600', '2024-02-09', '16:00', '13800138002', '已完成', '2024-02-08 18:00:00');

-- 删除寻找配种表（如果存在）
DROP TABLE IF EXISTS breeding_post;

-- 创建寻找配种表
CREATE TABLE breeding_post (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    title VARCHAR(200) NOT NULL COMMENT '标题',
    description VARCHAR(2000) DEFAULT NULL COMMENT '描述',
    pet_type VARCHAR(20) NOT NULL COMMENT '宠物类型：狗、猫',
    pet_gender VARCHAR(10) NOT NULL COMMENT '宠物性别：公、母',
    pet_name VARCHAR(50) NOT NULL COMMENT '宠物名称',
    breed VARCHAR(100) NOT NULL COMMENT '品种',
    vaccine_status VARCHAR(500) DEFAULT NULL COMMENT '疫苗情况',
    photos VARCHAR(2000) DEFAULT NULL COMMENT '照片URL，多个用逗号分隔，最多5个',
    breeding_requirement VARCHAR(1000) DEFAULT NULL COMMENT '配种要求',
    location VARCHAR(200) DEFAULT NULL COMMENT '地理位置',
    owner_info VARCHAR(500) DEFAULT NULL COMMENT '主人信息',
    status VARCHAR(20) NOT NULL DEFAULT '寻找中' COMMENT '状态：寻找中、已结束',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='寻找配种表';

-- 删除寻找寄养表（如果存在）
DROP TABLE IF EXISTS foster_post;

-- 创建寻找寄养表
CREATE TABLE foster_post (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    title VARCHAR(200) NOT NULL COMMENT '标题',
    description VARCHAR(2000) DEFAULT NULL COMMENT '描述',
    pet_type VARCHAR(20) NOT NULL COMMENT '宠物类型：狗、猫',
    pet_gender VARCHAR(10) NOT NULL COMMENT '宠物性别：公、母',
    pet_name VARCHAR(50) NOT NULL COMMENT '宠物名称',
    breed VARCHAR(100) NOT NULL COMMENT '品种',
    vaccine_status VARCHAR(500) DEFAULT NULL COMMENT '疫苗情况',
    photos VARCHAR(2000) DEFAULT NULL COMMENT '照片URL，多个用逗号分隔，最多5个',
    foster_requirement VARCHAR(1000) DEFAULT NULL COMMENT '寄养要求',
    location VARCHAR(200) DEFAULT NULL COMMENT '地理位置',
    owner_info VARCHAR(500) DEFAULT NULL COMMENT '主人信息',
    status VARCHAR(20) NOT NULL DEFAULT '寻找中' COMMENT '状态：寻找中、已结束',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='寻找寄养表';

-- 插入寄养表示例数据（至少5条）
INSERT INTO foster_post (user_id, title, description, pet_type, pet_gender, pet_name, breed, vaccine_status, photos, foster_requirement, location, owner_info, status, create_time) VALUES
(2, '金毛寻找寄养家庭', '我家金毛小白，2岁，性格温顺乖巧，不吵不闹，因工作原因需要出差一个月，希望找一个有爱心有经验的家庭临时寄养。狗狗健康状况良好，所有疫苗齐全，有良好的生活习惯，会定时定点排便。', '狗', '母', '小白', '金毛', '已接种狂犬疫苗、犬瘟热疫苗、细小病毒疫苗，疫苗记录齐全，定期驱虫', 'https://images.unsplash.com/photo-1601758228041-f3b2795255f1?w=600,https://images.unsplash.com/photo-1587300003388-59208cc962cb?w=600,https://images.unsplash.com/photo-1543466835-00a7907e9de1?w=600', '希望寄养家庭有养狗经验，家中环境干净整洁，有足够的活动空间。能够每天遛狗两次，提供优质狗粮和充足的水。有独立的休息区域，能够定期和主人反馈狗狗的情况。寄养费用可协商。', '北京市朝阳区', '主人：张女士，联系方式：13800138001，微信：zhangsan2024，养犬经验5年', '寻找中', '2024-02-15 10:00:00'),
(3, '英短蓝猫寄养', '纯种英短蓝猫球球，公猫，1岁半，毛色纯正，圆脸大眼，性格亲人温顺。因为要回老家过年一个月，需要找靠谱的家庭寄养。猫咪很乖，不会乱抓家具，会使用猫砂盆，吃猫粮不挑食。', '猫', '公', '球球', '英国短毛猫', '已接种猫三联疫苗，狂犬疫苗，定期驱虫，有疫苗本', 'https://images.unsplash.com/photo-1574158622682-e40e69881006?w=600,https://images.unsplash.com/photo-1548802673-380ab8ebc7b7?w=600,https://images.unsplash.com/photo-1514888286974-6c03e2ca1dba?w=600,https://images.unsplash.com/photo-1576201836106-db1758fd1c97?w=600', '希望寄养家庭有养猫经验，家中无其他宠物或宠物性格温和。能够提供安静的环境，保证猫咪安全，不要让猫咪外出。每天铲屎两次，提供优质猫粮和干净的水。能够每天拍照发给主人看猫咪情况。寄养费用面议。', '上海市浦东新区', '主人：李先生，电话：13800138002，微信：liqiuqiu123，有5年养猫经验', '寻找中', '2024-02-16 14:30:00'),
(4, '泰迪犬短期寄养', '棕色泰迪妞妞，母犬，3岁，体重5kg，健康活泼，性格开朗。因家中装修需要寄养两周时间。狗狗很听话，不乱叫，和人类小孩相处很好。', '狗', '母', '妞妞', '泰迪', '疫苗接种完整，每年定期体检，健康证明齐全', 'https://images.unsplash.com/photo-1597633425046-08f5110420b5?w=600,https://images.unsplash.com/photo-1623387641168-d9803ddd3f35?w=600', '希望寄养家庭有耐心，能够每天遛狗1-2次。提供小型犬专用狗粮，保持环境卫生。不要让狗狗单独在家超过4小时。如有问题及时联系主人。按天计费，费用合理。', '广州市天河区', '主人：王女士，电话：13800138003，可随时联系', '寻找中', '2024-02-17 09:20:00'),
(5, '美短虎斑猫求寄养', '美短虎斑母猫花花，2岁，花纹标准，身体健康，性格活泼但不闹腾。主人要出国学习3个月，需要找长期可靠的寄养家庭。猫咪适应能力强，和其他猫也能相处。', '猫', '母', '花花', '美国短毛猫', '疫苗齐全，定期体检，健康状况良好，有完整的医疗记录', 'https://images.unsplash.com/photo-1581888227599-779811939961?w=600,https://images.unsplash.com/photo-1601758228041-f3b2795255f1?w=600,https://images.unsplash.com/photo-1587300003388-59208cc962cb?w=600', '希望寄养家庭有养猫经验，家中环境安全。每天清理猫砂，提供优质猫粮和零食。有猫爬架等娱乐设施更佳。定期拍视频发给主人。长期寄养可按月付费，价格好商量。', '深圳市南山区', '主人：赵先生，微信：zhaoliu666，QQ：123456789', '寻找中', '2024-02-18 16:45:00'),
(2, '柯基犬寻找临时寄养', '纯种柯基豆豆，公犬，2岁半，短腿大耳，性格活泼友善。因家中有事需要回老家一周，希望找个临时寄养的地方。狗狗非常乖巧，不会随地大小便。', '狗', '公', '豆豆', '柯基', '所有疫苗已接种，有健康证明和疫苗本', 'https://images.unsplash.com/photo-1559523161-0fc0d8b38a7a?w=600,https://images.unsplash.com/photo-1576086213369-97a306d36557?w=600', '希望寄养家庭住在北京，交通方便。每天遛狗2次，提供狗粮和零食（主人提供）。能够保证狗狗安全，不要让狗狗和陌生狗接触。价格面议，可以当面详谈。', '北京市海淀区', '主人：张先生，电话：13800138001，随时可联系', '寻找中', '2024-02-19 11:00:00'),
(3, '波斯猫寄养求助', '纯白波斯猫雪儿，母猫，3岁，性格温顺安静。主人因工作调动需要寄养2个月。猫咪很安静，喜欢睡觉，不会乱叫。需要每天梳毛。', '猫', '母', '雪儿', '波斯猫', '疫苗完整，健康体检合格，有医疗记录本', 'https://images.unsplash.com/photo-1614941059122-c7f8b828e5ad?w=600,https://images.unsplash.com/photo-1535930891776-0c2dfb7fda1a?w=600', '希望寄养家庭有养长毛猫的经验，能够每天梳毛。保持环境干净，提供优质猫粮。定期清理猫砂，保持猫咪整洁。能够定期拍照反馈。寄养费用可按月结算。', '成都市武侯区', '主人：刘女士，微信：liuxue789，电话：13900139000', '已结束', '2024-02-10 10:30:00'),
(4, '布偶猫长期寄养', '纯种布偶猫王子，公猫，2岁，蓝双色，性格温柔粘人。因主人要出国工作一年，需要找长期寄养家庭。猫咪很听话，喜欢和人互动。', '猫', '公', '王子', '布偶猫', '疫苗齐全，定期驱虫和体检，健康状况优秀', 'https://images.unsplash.com/photo-1617791160536-598cf32026fb?w=600,https://images.unsplash.com/photo-1628009368231-7bb7cfcb0def?w=600,https://images.unsplash.com/photo-1583511655857-d19b40a7a54e?w=600', '希望寄养家庭非常有爱心和耐心，有养布偶猫的经验。能够提供宽敞舒适的环境，每天陪伴猫咪玩耍。提供进口猫粮，保持猫咪毛发光泽。定期视频通话让主人看到猫咪。长期寄养费用优厚，可月付或季付。', '杭州市西湖区', '主人：周先生，电话：13900139000，微信：zhouwangzi', '寻找中', '2024-02-20 15:20:00');

-- 删除宠物领养表（如果存在）
DROP TABLE IF EXISTS adoption_post;

-- 创建宠物领养表
CREATE TABLE adoption_post (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    title VARCHAR(200) NOT NULL COMMENT '标题',
    description VARCHAR(2000) DEFAULT NULL COMMENT '描述',
    pet_type VARCHAR(20) NOT NULL COMMENT '宠物类型：狗、猫',
    pet_gender VARCHAR(10) NOT NULL COMMENT '宠物性别：公、母',
    pet_name VARCHAR(50) NOT NULL COMMENT '宠物名称',
    breed VARCHAR(100) NOT NULL COMMENT '品种',
    vaccine_status VARCHAR(500) DEFAULT NULL COMMENT '疫苗情况',
    photos VARCHAR(2000) DEFAULT NULL COMMENT '照片URL，多个用逗号分隔，最多5个',
    adoption_requirement VARCHAR(1000) DEFAULT NULL COMMENT '领养要求',
    location VARCHAR(200) DEFAULT NULL COMMENT '地理位置',
    owner_info VARCHAR(500) DEFAULT NULL COMMENT '主人信息',
    status VARCHAR(20) NOT NULL DEFAULT '寻找中' COMMENT '状态：寻找中、已结束',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='宠物领养表';

-- 插入领养表示例数据（至少5条）
INSERT INTO adoption_post (user_id, title, description, pet_type, pet_gender, pet_name, breed, vaccine_status, photos, adoption_requirement, location, owner_info, status, create_time) VALUES
(2, '可爱金毛幼犬寻找温暖的家', '我家金毛生了一窝小狗狗，现在2个月大，健康活泼，非常可爱。因为家里养不了这么多，希望为它们找到负责任的主人。小狗已经做了第一针疫苗，会定期驱虫，毛色金黄，品相很好。', '狗', '公', '小金', '金毛', '已接种第一针疫苗，定期驱虫', 'https://images.unsplash.com/photo-1601758228041-f3b2795255f1?w=600,https://images.unsplash.com/photo-1587300003388-59208cc962cb?w=600,https://images.unsplash.com/photo-1543466835-00a7907e9de1?w=600', '希望领养人有稳定的住所和收入，有养狗经验优先。能够科学喂养，定期打疫苗和体检。承诺不弃养、不虐待。有独立的居住空间，家人同意养狗。可以签订领养协议。', '北京市朝阳区', '主人：张女士，联系方式：13800138001，微信：zhangsan2024，可随时上门看狗', '寻找中', '2024-02-15 10:00:00'),
(3, '温顺英短蓝猫找新家', '因为工作调动要去国外，无法带走家里的猫咪球球。球球是纯种英短蓝猫，1岁半，公猫，已绝育，毛色纯正，圆脸大眼，性格特别温顺亲人。希望找一个有爱心的家庭收养。', '猫', '公', '球球', '英国短毛猫', '已接种猫三联疫苗、狂犬疫苗，已绝育，定期驱虫', 'https://images.unsplash.com/photo-1574158622682-e40e69881006?w=600,https://images.unsplash.com/photo-1548802673-380ab8ebc7b7?w=600,https://images.unsplash.com/photo-1514888286974-6c03e2ca1dba?w=600,https://images.unsplash.com/photo-1576201836106-db1758fd1c97?w=600', '希望领养人真心喜欢猫，家中环境安全，能够提供优质猫粮和定期体检。不散养，家中有纱窗保护。能够定期发猫咪照片给我。愿意签订领养协议，承诺善待猫咪一生。', '上海市浦东新区', '主人：李先生，电话：13800138002，微信：liqiuqiu123，可视频看猫', '寻找中', '2024-02-16 14:30:00'),
(4, '泰迪犬妞妞求领养', '因为家里小孩过敏，不得不为妞妞找新家。妞妞是棕色泰迪，母犬，3岁，体重5kg，性格温顺乖巧，不乱叫，很听话。会很多技能，坐下、握手、转圈都会。', '狗', '母', '妞妞', '泰迪', '疫苗接种完整，每年定期体检，健康证明齐全', 'https://images.unsplash.com/photo-1597633425046-08f5110420b5?w=600,https://images.unsplash.com/photo-1623387641168-d9803ddd3f35?w=600', '希望领养人有耐心和爱心，能够善待妞妞。有养狗经验，家中环境适合养小型犬。承诺不弃养，能够定期带妞妞体检打疫苗。希望能定期给我发妞妞的照片。', '广州市天河区', '主人：王女士，电话：13800138003，可随时联系看狗', '寻找中', '2024-02-17 09:20:00'),
(5, '美短虎斑猫花花寻找新主人', '花花是美短虎斑母猫，2岁，花纹标准漂亮，身体健康，性格活泼。因为家里老人身体原因无法继续养猫，希望为花花找到一个好人家。花花很聪明，会用猫砂，不乱抓家具。', '猫', '母', '花花', '美国短毛猫', '疫苗齐全，定期体检，健康状况良好，有完整的医疗记录', 'https://images.unsplash.com/photo-1581888227599-779811939961?w=600,https://images.unsplash.com/photo-1601758228041-f3b2795255f1?w=600,https://images.unsplash.com/photo-1587300003388-59208cc962cb?w=600', '希望领养人有养猫经验，家中环境安全。每天清理猫砂，提供优质猫粮。不散养，做好窗户防护。能够给花花足够的关爱和陪伴。愿意签订领养协议。', '深圳市南山区', '主人：赵先生，微信：zhaoliu666，QQ：123456789', '寻找中', '2024-02-18 16:45:00'),
(2, '柯基犬豆豆找新家', '豆豆是纯种柯基公犬，2岁半，短腿大耳，非常可爱。因为要搬到不允许养狗的小区，不得不为豆豆找新家。豆豆性格活泼友善，和小孩相处很好，会很多技能。', '狗', '公', '豆豆', '柯基', '所有疫苗已接种，有健康证明和疫苗本', 'https://images.unsplash.com/photo-1559523161-0fc0d8b38a7a?w=600,https://images.unsplash.com/photo-1576086213369-97a306d36557?w=600', '希望领养人住房宽敞，有足够的活动空间。能够每天遛狗，提供优质狗粮。有养中型犬的经验。能够善待豆豆，不弃养不虐待。可以定期回访。', '北京市海淀区', '主人：张先生，电话：13800138001，周末可上门看狗', '寻找中', '2024-02-19 11:00:00'),
(3, '哈士奇雪儿求收养', '雪儿是纯种哈士奇母犬，3岁，蓝眼三火，品相很好。因为主人怀孕，家人不同意继续养狗，希望为雪儿找个好归宿。雪儿虽然是哈士奇但很听话，不拆家。', '狗', '母', '雪儿', '哈士奇', '疫苗完整，健康体检合格，有医疗记录本', 'https://images.unsplash.com/photo-1614941059122-c7f8b828e5ad?w=600,https://images.unsplash.com/photo-1535930891776-0c2dfb7fda1a?w=600', '希望领养人有养大型犬的经验，住房面积大，有独立的活动空间。能够每天保证足够的运动量。经济条件稳定，能够承担养狗费用。承诺善待雪儿一生。', '成都市武侯区', '主人：刘女士，微信：liuxue789，电话：13900139000', '已结束', '2024-02-10 10:30:00'),
(4, '布偶猫王子寻找爱心家庭', '王子是纯种布偶猫，公猫，2岁，蓝双色，性格温柔粘人。因为要出国定居，无法带走王子，希望为它找到真心爱猫的家庭。王子非常亲人，会用猫砂，不乱抓。', '猫', '公', '王子', '布偶猫', '疫苗齐全，定期驱虫和体检，健康状况优秀', 'https://images.unsplash.com/photo-1617791160536-598cf32026fb?w=600,https://images.unsplash.com/photo-1628009368231-7bb7cfcb0def?w=600,https://images.unsplash.com/photo-1583511655857-d19b40a7a54e?w=600', '希望领养人非常有爱心和耐心，有养布偶猫的经验。能够提供宽敞舒适的环境，每天陪伴猫咪玩耍。提供进口猫粮，定期体检美容。家中做好防护，不散养。愿意签订领养协议并定期回访。', '杭州市西湖区', '主人：周先生，电话：13900139000，微信：zhouwangzi', '寻找中', '2024-02-20 15:20:00');

-- 插入配种表示例数据（至少5条）
INSERT INTO breeding_post (user_id, title, description, pet_type, pet_gender, pet_name, breed, vaccine_status, photos, breeding_requirement, location, owner_info, status, create_time) VALUES
(2, '寻找金毛公犬配种', '我家金毛母犬小白，2岁，性格温顺，品相优良，希望找一只优秀的金毛公犬配种。狗狗健康状况良好，所有疫苗齐全，已做绝育前体检。希望找到同样品相好、健康的金毛公犬。', '狗', '母', '小白', '金毛', '已接种狂犬疫苗、犬瘟热疫苗、细小病毒疫苗，疫苗记录齐全', 'https://images.unsplash.com/photo-1601758228041-f3b2795255f1?w=600,https://images.unsplash.com/photo-1587300003388-59208cc962cb?w=600,https://images.unsplash.com/photo-1543466835-00a7907e9de1?w=600', '希望公犬年龄在2-4岁之间，品相优良，性格温顺，有血统证书优先。健康状况良好，疫苗齐全。', '北京市朝阳区', '主人：张女士，联系方式：13800138001，养犬经验5年，家庭环境优越', '寻找中', '2024-02-15 10:00:00'),
(3, '英短蓝猫寻找配偶', '纯种英短蓝猫，公猫，1岁半，毛色纯正，圆脸大眼，性格亲人，寻找品相好的母猫配种。可上门或约定地点。', '猫', '公', '球球', '英国短毛猫', '已接种猫三联疫苗，狂犬疫苗，定期驱虫', 'https://images.unsplash.com/photo-1574158622682-e40e69881006?w=600,https://images.unsplash.com/photo-1548802673-380ab8ebc7b7?w=600,https://images.unsplash.com/photo-1514888286974-6c03e2ca1dba?w=600,https://images.unsplash.com/photo-1576201836106-db1758fd1c97?w=600', '希望母猫品种纯正，毛色标准，性格温顺，1-3岁之间。健康检查合格，疫苗齐全。可协商配种费用或小猫分配方案。', '上海市浦东新区', '主人：李先生，微信：liqiuqiu123，有多年养猫经验，家中环境整洁', '寻找中', '2024-02-16 14:30:00'),
(4, '泰迪母犬配种', '棕色泰迪犬，母犬，3岁，体重5kg，健康活泼，希望找同品种公犬配种。', '狗', '母', '妞妞', '泰迪', '疫苗接种完整，每年定期体检', 'https://images.unsplash.com/photo-1597633425046-08f5110420b5?w=600,https://images.unsplash.com/photo-1623387641168-d9803ddd3f35?w=600', '公犬要求：体型适中（4-6kg），毛色纯正，性格温顺，健康状况良好。配种费用可商议。', '广州市天河区', '主人：王女士，电话：13800138003', '寻找中', '2024-02-17 09:20:00'),
(5, '美短虎斑猫寻找配偶', '美短虎斑母猫，2岁，花纹标准，身体健康，性格活泼，寻找优质公猫配种。', '猫', '母', '花花', '美国短毛猫', '疫苗齐全，定期体检，健康状况良好', 'https://images.unsplash.com/photo-1581888227599-779811939961?w=600,https://images.unsplash.com/photo-1601758228041-f3b2795255f1?w=600,https://images.unsplash.com/photo-1587300003388-59208cc962cb?w=600', '希望公猫品种纯正，花纹清晰，健康状况良好。可提供小猫作为配种费用。', '深圳市南山区', '主人：赵先生，QQ：123456789，经验丰富的猫主', '寻找中', '2024-02-18 16:45:00'),
(2, '柯基公犬寻找母犬', '纯种柯基公犬，2岁半，短腿大耳，性格活泼，希望找同品种母犬配种。', '狗', '公', '豆豆', '柯基', '所有疫苗已接种，有健康证明', 'https://images.unsplash.com/photo-1559523161-0fc0d8b38a7a?w=600,https://images.unsplash.com/photo-1576086213369-97a306d36557?w=600', '母犬要求2-4岁，品相标准，健康状况良好。配种方式灵活，可协商。', '北京市海淀区', '主人：张先生，电话：13800138001，家庭饲养', '寻找中', '2024-02-19 11:00:00'),
(3, '哈士奇母犬配种', '纯种哈士奇母犬，3岁，蓝眼三火，性格温顺，寻找优秀公犬配种。', '狗', '母', '雪儿', '哈士奇', '疫苗完整，健康体检合格', 'https://images.unsplash.com/photo-1614941059122-c7f8b828e5ad?w=600,https://images.unsplash.com/photo-1535930891776-0c2dfb7fda1a?w=600', '希望公犬品相优良，眼睛颜色纯正，性格稳定。有血统证书更佳。', '成都市武侯区', '主人：刘女士，微信：liuxue789', '已结束', '2024-02-10 10:30:00'),
(4, '布偶猫寻找配偶', '纯种布偶猫，公猫，2岁，蓝双色，性格温柔，寻找同品种母猫配种。', '猫', '公', '王子', '布偶猫', '疫苗齐全，定期驱虫和体检', 'https://images.unsplash.com/photo-1617791160536-598cf32026fb?w=600,https://images.unsplash.com/photo-1628009368231-7bb7cfcb0def?w=600,https://images.unsplash.com/photo-1583511655857-d19b40a7a54e?w=600', '母猫要求品相标准，毛色纯正，性格温顺。可提供优质的配种服务。', '杭州市西湖区', '主人：周先生，电话：13900139000', '寻找中', '2024-02-20 15:20:00');

-- 删除论坛帖子表（如果存在）
DROP TABLE IF EXISTS forum_post;

-- 创建论坛帖子表
CREATE TABLE forum_post (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    title VARCHAR(200) NOT NULL COMMENT '标题',
    description VARCHAR(500) DEFAULT NULL COMMENT '描述',
    content TEXT NOT NULL COMMENT '正文',
    images VARCHAR(2000) DEFAULT NULL COMMENT '图片URL，多个用逗号分隔，最多5个',
    keywords VARCHAR(200) DEFAULT NULL COMMENT '关键词',
    user_id BIGINT NOT NULL COMMENT '发布者ID',
    username VARCHAR(50) NOT NULL COMMENT '发布者用户名',
    like_count INT NOT NULL DEFAULT 0 COMMENT '点赞数',
    comment_count INT NOT NULL DEFAULT 0 COMMENT '评论数',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='论坛帖子表';

-- 插入示例数据（至少5条）
INSERT INTO forum_post (title, description, content, images, keywords, user_id, username, like_count, comment_count, create_time) VALUES
('分享我家金毛的日常训练心得', '通过科学的训练方法，让金毛变得更加听话懂事', '大家好！我家金毛小白现在2岁了，从小狗到现在的乖巧懂事，我总结了一些训练心得分享给大家。\n\n首先是基础口令训练：坐下、趴下、握手这些基本动作一定要在幼犬时期就开始训练。每次训练时间不要太长，10-15分钟为宜，用零食作为奖励效果最好。\n\n其次是社会化训练：要经常带狗狗出门，接触不同的人和环境，这样狗狗长大后不会胆小或者过度兴奋。\n\n最后是耐心和坚持：训练狗狗不是一蹴而就的，需要主人有足够的耐心，坚持每天训练，才能看到效果。\n\n希望我的经验能帮到大家！', 'https://images.unsplash.com/photo-1601758228041-f3b2795255f1?w=600,https://images.unsplash.com/photo-1587300003388-59208cc962cb?w=600,https://images.unsplash.com/photo-1543466835-00a7907e9de1?w=600', '金毛,训练,经验分享', 2, '宠物爱好者小王', 25, 8, '2024-02-20 10:00:00'),
('猫咪挑食怎么办？实用方法分享', '针对猫咪挑食问题的解决方案和预防措施', '最近很多猫友问我猫咪挑食的问题，我家球球之前也经历过这个阶段，现在分享一下我的解决方法：\n\n1. 定时定量喂食：每天固定时间喂食，每次放下食物30分钟后就收走，不管吃没吃完。这样可以培养猫咪的饥饿感。\n\n2. 不要频繁换粮：有些主人看到猫咪不吃就换粮，这样反而会让猫咪越来越挑食。选定一款优质猫粮后要坚持喂食。\n\n3. 减少零食：零食给太多会影响正餐食欲，建议零食只在训练或者特殊情况下给予。\n\n4. 增加运动量：多陪猫咪玩耍，增加运动量可以提高食欲。\n\n5. 适当添加罐头或冻干：如果猫咪实在不吃，可以在猫粮中混入少量罐头或冻干，慢慢减少比例。\n\n希望对大家有帮助！', 'https://images.unsplash.com/photo-1574158622682-e40e69881006?w=600,https://images.unsplash.com/photo-1548802673-380ab8ebc7b7?w=600', '猫咪,挑食,喂养经验', 3, '猫咪达人小李', 42, 15, '2024-02-21 14:30:00'),
('新手养狗必备物品清单', '给准备养狗的新手朋友们整理的物品清单', '作为一个养狗5年的老铲屎官，今天给大家整理一份新手养狗必备物品清单：\n\n【饮食类】\n- 狗粮：选择适合狗狗年龄和体型的优质狗粮\n- 食盆水盆：不锈钢材质最好，易清洗\n- 自动饮水器：保证狗狗随时有干净的水喝\n\n【日用品】\n- 狗窝/狗垫：给狗狗一个舒适的休息空间\n- 牵引绳：外出必备，保证安全\n- 项圈/胸背带：根据狗狗体型选择\n- 玩具：磨牙玩具、互动玩具等\n\n【清洁用品】\n- 宠物沐浴露：温和配方\n- 梳子：根据毛发类型选择\n- 指甲剪：定期修剪指甲\n- 宠物湿巾：日常清洁\n\n【医疗保健】\n- 体外驱虫药：预防寄生虫\n- 体内驱虫药：定期驱虫\n- 宠物急救箱：碘伏、纱布等\n\n希望能帮到准备养狗的朋友们！', 'https://images.unsplash.com/photo-1514888286974-6c03e2ca1dba?w=600,https://images.unsplash.com/photo-1576201836106-db1758fd1c97?w=600', '新手养狗,物品清单,经验分享', 2, '宠物爱好者小王', 38, 12, '2024-02-22 09:00:00'),
('猫咪呕吐的常见原因和应对方法', '详细分析猫咪呕吐的各种情况和处理方式', '猫咪呕吐是比较常见的问题，但很多新手铲屎官会很紧张。今天我来科普一下猫咪呕吐的常见原因：\n\n【正常情况的呕吐】\n1. 吐毛球：猫咪舔毛后会吐出毛球，这是正常现象。可以喂化毛膏或猫草帮助排出。\n2. 吃太快：有些猫咪进食太快会呕吐未消化的食物，这种情况可以少食多餐。\n\n【需要注意的呕吐】\n1. 肠胃炎：如果呕吐物带血或呈黄绿色，需要立即就医。\n2. 食物过敏：更换猫粮后频繁呕吐，可能是食物过敏。\n3. 误食异物：如果猫咪精神萎靡且持续呕吐，可能是误食异物。\n\n【应对方法】\n- 轻微呕吐：禁食4-6小时，观察情况\n- 频繁呕吐：立即就医\n- 预防措施：定期驱虫、选择优质猫粮、避免突然换粮\n\n希望大家的猫咪都健健康康的！', 'https://images.unsplash.com/photo-1614941059122-c7f8b828e5ad?w=600', '猫咪健康,呕吐,医疗知识', 5, '宠物医生小刘', 56, 20, '2024-02-23 16:00:00'),
('分享我家泰迪的美容造型', '专业美容师给泰迪做的几种造型分享', '大家好！我家泰迪妞妞最近去做了美容，美容师给推荐了几种造型，效果都很棒，分享给大家：\n\n【泰迪装】\n最经典的造型，头部修成圆形，身体毛发留长，四肢修剪整齐。适合毛量丰富的泰迪。\n\n【运动装】\n整体毛发剪短，清爽利落，适合夏天。打理方便，不容易打结。\n\n【羊羔装】\n身体毛发留长，头部、四肢修剪短，看起来像小羊一样可爱。\n\n【日式造型】\n耳朵留长，身体修剪成圆润的形状，非常萌。\n\n美容小贴士：\n- 建议1-2个月做一次美容\n- 日常要经常梳毛，防止打结\n- 洗澡后一定要吹干，避免皮肤病\n- 定期修剪指甲和脚底毛\n\n大家也可以分享自己家宠物的造型哦！', 'https://images.unsplash.com/photo-1535930891776-0c2dfb7fda1a?w=600,https://images.unsplash.com/photo-1617791160536-598cf32026fb?w=600', '泰迪,美容,造型分享', 4, '萌宠小管家小赵', 31, 10, '2024-02-24 11:30:00'),
('宠物保险真的有必要买吗？', '分析宠物保险的利弊和适用情况', '最近考虑给家里的狗狗买保险，研究了一番后跟大家分享一下：\n\n【宠物保险的优势】\n1. 降低医疗费用：宠物看病很贵，保险可以报销大部分费用\n2. 意外保障：涵盖意外伤害、骨折等突发情况\n3. 疾病保障：慢性病、传染病等治疗费用\n4. 责任险：宠物伤人可以赔付\n\n【需要注意的点】\n1. 等待期：一般有30-90天等待期\n2. 免赔额：单次理赔可能有免赔额\n3. 年龄限制：老年宠物可能无法投保\n4. 既往病史：之前的疾病不予理赔\n\n【个人建议】\n- 品种狗、易生病的宠物建议购买\n- 年轻健康的宠物可以考虑\n- 注意看清保险条款，选择正规公司\n- 日常预防比治疗更重要\n\n大家有什么看法欢迎讨论！', 'https://images.unsplash.com/photo-1628009368231-7bb7cfcb0def?w=600', '宠物保险,保障,讨论', 3, '猫咪达人小李', 45, 18, '2024-02-25 15:00:00'),
('夏天如何给宠物降温？', '分享夏季宠物防暑降温的实用方法', '随着天气越来越热，宠物也容易中暑，今天分享一些降温方法：\n\n【物理降温】\n1. 冰垫、凉席：给宠物准备凉爽的休息地\n2. 冰块玩具：在玩具里放冰块，让宠物玩\n3. 湿毛巾：用湿毛巾擦拭宠物身体\n4. 风扇、空调：保持室内凉爽\n\n【饮食调节】\n1. 充足饮水：多处放水盆，保证随时能喝到水\n2. 适量水果：西瓜、苹果等含水量高的水果\n3. 湿粮：适当增加湿粮比例\n\n【注意事项】\n1. 避免在最热的时段遛狗（11:00-16:00）\n2. 不要把宠物留在车里\n3. 剃毛要适度，不要剃光\n4. 注意观察中暑症状：喘气急促、流口水、体温升高\n\n【中暑急救】\n如果发现中暑，立即转移到阴凉处，用冷水降温，及时就医！\n\n希望所有毛孩子都能安全度夏！', 'https://images.unsplash.com/photo-1583511655857-d19b40a7a54e?w=600,https://images.unsplash.com/photo-1625316708582-7c38734be31d?w=600', '夏季护理,降温,健康', 2, '宠物爱好者小王', 52, 14, '2024-02-26 10:30:00'),
('多猫家庭如何避免猫咪打架？', '多猫家庭和谐相处的秘诀', '我家现在养了三只猫，从最初的打架到现在和平相处，总结了一些经验：\n\n【新猫进家的正确方式】\n1. 隔离期：新猫单独隔离1-2周，让它们闻到对方气味\n2. 交换气味：用毛巾擦拭不同的猫，交换给对方闻\n3. 视觉接触：用笼子或门缝让它们互相看到\n4. 短暂接触：在监督下短暂接触，有攻击倾向立即分开\n5. 逐步融合：慢慢增加接触时间\n\n【资源分配】\n1. 猫砂盆：数量 = 猫数量 + 1\n2. 食盆水盆：每只猫单独的食盆\n3. 猫爬架：提供足够的垂直空间\n4. 藏身处：每只猫都要有自己的藏身之所\n\n【日常管理】\n1. 公平对待：不要偏心某只猫\n2. 分散注意力：用玩具、零食转移注意力\n3. 绝育：未绝育的猫更容易打架\n\n【打架应对】\n轻微打闹：不用干预，这是在建立等级\n激烈打架：用水枪、声音制止，隔离冷静\n\n希望大家的多猫家庭都能和谐相处！', 'https://images.unsplash.com/photo-1548681528-6a5c45b66b42?w=600,https://images.unsplash.com/photo-1598387181032-a3103a2db5b3?w=600,https://images.unsplash.com/photo-1603147627908-79013d8e3be1?w=600', '多猫家庭,相处,经验分享', 3, '猫咪达人小李', 40, 16, '2024-02-27 13:00:00');

-- 删除论坛帖子点赞表（如果存在）
DROP TABLE IF EXISTS forum_post_like;

-- 创建论坛帖子点赞表
CREATE TABLE forum_post_like (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    post_id BIGINT NOT NULL COMMENT '帖子ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='论坛帖子点赞表';

-- 插入示例数据（至少5条）
INSERT INTO forum_post_like (post_id, user_id, create_time) VALUES
(1, 3, '2024-02-20 11:00:00'),
(1, 4, '2024-02-20 12:00:00'),
(1, 5, '2024-02-20 13:00:00'),
(2, 2, '2024-02-21 15:00:00'),
(2, 4, '2024-02-21 16:00:00'),
(2, 5, '2024-02-21 17:00:00'),
(3, 3, '2024-02-22 10:00:00'),
(3, 4, '2024-02-22 11:00:00'),
(3, 5, '2024-02-22 12:00:00'),
(4, 2, '2024-02-23 17:00:00'),
(4, 3, '2024-02-23 18:00:00'),
(4, 4, '2024-02-23 19:00:00'),
(5, 2, '2024-02-24 12:00:00'),
(5, 3, '2024-02-24 13:00:00'),
(5, 5, '2024-02-24 14:00:00');

-- 删除论坛帖子评论表（如果存在）
DROP TABLE IF EXISTS forum_post_comment;

-- 创建论坛帖子评论表
CREATE TABLE forum_post_comment (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    post_id BIGINT NOT NULL COMMENT '帖子ID',
    content VARCHAR(1000) NOT NULL COMMENT '评论内容',
    user_id BIGINT NOT NULL COMMENT '评论者ID',
    username VARCHAR(50) NOT NULL COMMENT '评论者用户名',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='论坛帖子评论表';

-- 插入示例数据（至少5条）
INSERT INTO forum_post_comment (post_id, content, user_id, username, create_time) VALUES
(1, '非常实用的训练方法！我也要试试这样训练我家狗狗。', 3, '猫咪达人小李', '2024-02-20 11:30:00'),
(1, '请问训练的时候用什么零食比较好？', 4, '萌宠小管家小赵', '2024-02-20 14:00:00'),
(1, '我家金毛也是这样训练的，确实很有效果！', 5, '宠物医生小刘', '2024-02-20 15:30:00'),
(2, '我家猫咪也挑食，试试你的方法！', 2, '宠物爱好者小王', '2024-02-21 15:30:00'),
(2, '定时定量真的很重要，我家猫咪就是这样改过来的。', 4, '萌宠小管家小赵', '2024-02-21 16:30:00'),
(2, '感谢分享！非常有帮助。', 5, '宠物医生小刘', '2024-02-21 17:30:00'),
(3, '清单很全面，已收藏！', 3, '猫咪达人小李', '2024-02-22 10:30:00'),
(3, '请问有推荐的狗粮品牌吗？', 4, '萌宠小管家小赵', '2024-02-22 11:30:00'),
(3, '新手养狗必看！', 5, '宠物医生小刘', '2024-02-22 13:00:00'),
(4, '专业！学到了很多知识。', 2, '宠物爱好者小王', '2024-02-23 17:30:00'),
(4, '我家猫咪前几天也吐了，还好不严重。', 3, '猫咪达人小李', '2024-02-23 18:30:00'),
(4, '宠物医生的科普就是专业，点赞！', 4, '萌宠小管家小赵', '2024-02-23 19:30:00'),
(5, '泰迪装最可爱了！', 2, '宠物爱好者小王', '2024-02-24 12:30:00'),
(5, '我家泰迪也是这个造型，很萌。', 3, '猫咪达人小李', '2024-02-24 13:30:00'),
(5, '请问美容大概多少钱？', 5, '宠物医生小刘', '2024-02-24 14:30:00');

