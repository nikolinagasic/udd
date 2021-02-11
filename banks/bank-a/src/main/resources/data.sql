INSERT INTO `banka`.`clients` (`address`, `name`, `surname`) VALUES ('Petra Drapsina 32, Novi Sad', 'Mile', 'Milic');
INSERT INTO `banka`.`clients` (`address`, `name`, `surname`) VALUES ('Puskinova 8, Novi Sad', 'Ana', 'Antic');

INSERT INTO `banka`.`accounts` (`account_number`, `balance`, `client_id`) VALUES ('535123456789666666', '5000', 1);
INSERT INTO `banka`.`accounts` (`account_number`, `balance`) VALUES ('535123456789333333', '5000');

INSERT INTO `banka`.`cards` (`cardholder_name`, `expire_date`, `pan`, `security_code`, `account_id`) VALUES ('MILE MILIC', '12/21', '7273e3b3536175e6f9f7ccdd86ad14908b2c5041236279dcebcfbbb978f1b3689185d4d88b41fd1765b6177a8c8cd105c62ac7e0596e6f8cfc69c0c4b0ad14fb', '3c9909afec25354d551dae21590bb26e38d53f2173b8d3dc3eee4c047e7ab1c1eb8b85103e3be7ba613b31bb5c9c36214dc9f14a42fd7a2fdb84856bca5c44c2', 1);
--5125353412341111
INSERT INTO `banka`.`merchants` (`account_id`,`password`, `company_name`, `address`) VALUES (2, '$2a$10$perBHa7ac8GAUllOFTH46elUbG6ZLjs6VJFOu2mqm.knQLDIM8Asm', 'Vulkan DOO', 'Puskinova 8, Novi Sad');

INSERT INTO `banka`.`payments` (`amount`, `merchant_order_id`, `merchant_timestamp`, `merchant_id`) VALUES ('1000', '2', '2019-04-28 12:45:15.000000', '1');