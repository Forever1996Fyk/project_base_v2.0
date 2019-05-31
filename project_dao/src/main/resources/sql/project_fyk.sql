DROP TABLE tb_permission;
DROP TABLE tb_role;
DROP TABLE tb_role_permission;
DROP TABLE tb_user;
DROP TABLE tb_user_role;
DROP TABLE sys_action_log;

CREATE TABLE `tb_permission` (
  `id` varchar(32) NOT NULL COMMENT '权限标识',
  `name` varchar(200) DEFAULT NULL COMMENT '权限名称',
  `pid` varchar(32) DEFAULT NULL COMMENT '上级权限id',
  `sort` int(4) DEFAULT NULL COMMENT '排名',
  `level` int(4) DEFAULT NULL COMMENT '等级',
  `remark` varchar(200) DEFAULT NULL COMMENT '备注',
  `status` int(4) DEFAULT '1' COMMENT '状态:0  已禁用 1 正在使用',
  `create_user_id` varchar(32) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_user_id` varchar(32) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='权限表';

CREATE TABLE `tb_role` (
  `id` varchar(32) NOT NULL COMMENT '角色标识',
  `role_name` varchar(200) DEFAULT NULL COMMENT '角色名称',
  `remark` varchar(200) DEFAULT NULL COMMENT '备注',
  `status` int(4) DEFAULT '1' COMMENT '状态:0  已禁用 1 正在使用',
  `create_user_id` varchar(32) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_user_id` varchar(32) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色表';

CREATE TABLE `tb_role_permission` (
  `id` varchar(32) NOT NULL COMMENT '主键标识',
  `role_id` varchar(200) DEFAULT NULL COMMENT '角色标识',
  `permission_id` varchar(32) DEFAULT NULL COMMENT '权限标识',
  `create_user_id` varchar(32) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_user_id` varchar(32) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色权限表';

CREATE TABLE `tb_user` (
  `id` varchar(32) NOT NULL COMMENT '用户标识',
  `account` varchar(200) DEFAULT NULL COMMENT '用户账号',
  `user_name` varchar(200) DEFAULT NULL COMMENT '用户名称',
  `password` varchar(200) DEFAULT NULL COMMENT '密码',
  `nick_name` varchar(200) DEFAULT NULL COMMENT '用户昵称',
  `user_icon` varchar(255) DEFAULT NULL COMMENT '头像',
  `last_login_time` datetime DEFAULT NULL COMMENT '最后登录时间',
  `age` int(11) DEFAULT NULL COMMENT '年龄',
  `sex` int(2) DEFAULT NULL COMMENT '性别 1男  0女',
  `marry_flag` int(2) DEFAULT NULL COMMENT '婚否',
  `education` int(2) DEFAULT NULL COMMENT '学历',
  `phone` varchar(200) DEFAULT NULL COMMENT '手机号',
  `email` varchar(200) DEFAULT NULL COMMENT '邮箱',
  `prov` varchar(32) DEFAULT NULL COMMENT '省级',
  `city` varchar(32) DEFAULT NULL COMMENT '地市级',
  `dist` varchar(32) DEFAULT NULL COMMENT '区县',
  `address` varchar(200) DEFAULT NULL COMMENT '地址',
  `idcard` varchar(200) DEFAULT NULL COMMENT '身份证号',
  `remark` varchar(200) DEFAULT NULL COMMENT '备注',
  `status` int(4) DEFAULT '1' COMMENT '状态:0  已禁用 1 正在使用',
  `create_user_id` varchar(32) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_user_id` varchar(32) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_marry` (`marry_flag`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

CREATE TABLE `tb_user_role` (
  `id` varchar(32) NOT NULL COMMENT '主键标识',
  `user_id` varchar(200) DEFAULT NULL COMMENT '用户标识',
  `role_id` varchar(32) DEFAULT NULL COMMENT '角色标识',
  `create_user_id` varchar(32) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_user_id` varchar(32) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户角色表';

CREATE TABLE `tb_attachment` (
  `id` varchar(32)  NOT NULL COMMENT '附件标识',
  `owner_id` varchar(32) DEFAULT NULL COMMENT '所属标识',
  `owner_entity` varchar(32) DEFAULT NULL COMMENT '所属表',
  `attach_origin_title` varchar(255)  DEFAULT NULL COMMENT '文件原名',
  `attach_utily` varchar(255)  DEFAULT NULL COMMENT '附件属性，例身份证证明',
  `attach_type` int(11) DEFAULT NULL COMMENT '附件类型0图片1附件',
  `attach_name` varchar(20)  DEFAULT NULL COMMENT '文件名',
  `attach_size` bigint(20) DEFAULT NULL COMMENT '文件大小',
  `attach_postfix` varchar(32)  DEFAULT NULL COMMENT '文件后缀',
  `attachment` varchar(255)  DEFAULT NULL COMMENT '附件',
  `remark` varchar(255)  DEFAULT NULL COMMENT '备注',
  `status` int(4) DEFAULT '1' COMMENT '状态:0  已禁用 1 正在使用',
  `create_user_id` varchar(32)  DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_user_id` varchar(32)  DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='文件表';

CREATE TABLE `tb_dict` (
  `id` varchar(36) NOT NULL COMMENT '字典标识',
  `dic_code` varchar(36)  DEFAULT NULL COMMENT '字典编码',
  `dic_name` varchar(255)  DEFAULT NULL COMMENT '字典名称',
  `remark` varchar(255)  DEFAULT NULL,
  `status` int(4) DEFAULT '1' COMMENT '状态:0  已禁用 1 正在使用',
  `create_user_id` varchar(32)  DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_user_id` varchar(32)  DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='数据字典';


CREATE TABLE `tb_dict_item` (
  `id` varchar(32)  NOT NULL COMMENT '字典项标识',
  `dic_id` varchar(32)  DEFAULT NULL COMMENT '字典标识',
  `item_code` varchar(2)  DEFAULT NULL COMMENT '字典项编码',
  `item_name` varchar(255)  DEFAULT NULL COMMENT '字典项名称',
  `item_sort` int(11) DEFAULT NULL COMMENT '排序',
  `remark` varchar(255)  DEFAULT NULL,
  `status` int(4) DEFAULT '1' COMMENT '状态:0  已禁用 1 正在使用',
  `create_user_id` varchar(32)  DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_user_id` varchar(32)  DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='数据字典项'

CREATE TABLE `tb_action_log` (
  `id` varchar(32) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(255) DEFAULT NULL COMMENT '日志名称',
  `type` varchar(255) DEFAULT NULL COMMENT '日志类型',
  `ipaddr` varchar(255) DEFAULT NULL COMMENT '操作IP地址',
  `clazz` varchar(255) DEFAULT NULL COMMENT '产生日志的类',
  `method` varchar(255) DEFAULT NULL COMMENT '产生日志的方法',
  `model` varchar(255) DEFAULT NULL COMMENT '产生日志的表',
  `record_id` bigint(20) DEFAULT NULL COMMENT '产生日志的数据id',
  `message` text COMMENT '日志消息',
	`create_user_id` varchar(32) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_user_id` varchar(32) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户日志';

