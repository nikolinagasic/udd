INSERT INTO `bankb`.`clients` (`address`, `name`, `surname`) VALUES ('Petra Drapsina 32, Novi Sad', 'Marko', 'Markovic');
INSERT INTO `bankb`.`clients` (`address`, `name`, `surname`) VALUES ('Puskinova 8, Novi Sad', 'Ana', 'Antic');

INSERT INTO `bankb`.`accounts` (`account_number`, `balance`, `client_id`) VALUES ('535123456789666666', '5000', 1);
INSERT INTO `bankb`.`accounts` (`account_number`, `balance`) VALUES ('535123456789333333', '5000');

INSERT INTO `bankb`.`cards` (`cardholder_name`, `expire_date`, `pan`, `security_code`, `account_id`) VALUES ('MARKO MARKOVIC', '12/21', '3f9014488d3d9b8e7db3620d3c0d8b75ea7ed370635db59ded7706b6e05f17039f4bc605a5667451ae5ec029dba67c24396a553a746037c75790bf510a4f4b45', '3c9909afec25354d551dae21590bb26e38d53f2173b8d3dc3eee4c047e7ab1c1eb8b85103e3be7ba613b31bb5c9c36214dc9f14a42fd7a2fdb84856bca5c44c2', 1);
--4125353412341111
INSERT INTO `bankb`.`merchants` (`account_id`,`password`, `company_name`, `address`) VALUES (2, '$2a$10$perBHa7ac8GAUllOFTH46elUbG6ZLjs6VJFOu2mqm.knQLDIM8Asm', 'Vulkan DOO', 'Puskinova 8, Novi Sad');

INSERT INTO `bankb`.`payments` (`amount`, `merchant_order_id`, `merchant_timestamp`, `merchant_id`) VALUES ('1000', '2', '2019-04-28 12:45:15.000000', '1');