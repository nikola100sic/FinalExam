INSERT INTO user (id, e_mail, username, password, name, surname, role)
              VALUES (1,'miroslav@maildrop.cc','miroslav','$2y$12$NH2KM2BJaBl.ik90Z1YqAOjoPgSd0ns/bF.7WedMxZ54OhWQNNnh6','Miroslav','Simic','ADMIN');
INSERT INTO user (id, e_mail, username, password, name, surname, role)
              VALUES (2,'tamara@maildrop.cc','tamara','$2y$12$DRhCpltZygkA7EZ2WeWIbewWBjLE0KYiUO.tHDUaJNMpsHxXEw9Ky','Tamara','Milosavljevic','USER');
INSERT INTO user (id, e_mail, username, password, name, surname, role)
              VALUES (3,'petar@maildrop.cc','petar','$2y$12$i6/mU4w0HhG8RQRXHjNCa.tG2OwGSVXb0GYUnf8MZUdeadE4voHbC','Petar','Jovic','USER');


 INSERT INTO `finalexamnikola`.`store` (`id`, `name`) VALUES ('1', 'Lidl');
INSERT INTO `finalexamnikola`.`store` (`id`, `name`) VALUES ('2', 'Maxi');

INSERT INTO `finalexamnikola`.`product` (`id`, `available`, `name`, `price`, `store_id`) VALUES ('1', '10', 'Milk', '2', '1');
INSERT INTO `finalexamnikola`.`product` (`id`, `available`, `name`, `price`, `store_id`) VALUES ('2', '3', 'Ketchup', '1.5', '2');
INSERT INTO `finalexamnikola`.`product` (`id`, `available`, `name`, `price`, `store_id`) VALUES ('3', '22', 'Chocolate', '2.3', '1');


INSERT INTO `finalexamnikola`.`orders` (`id`, `date`, `quantity`, `total_price`, `product_id`) VALUES ('1', '2021-03-21 09:28', '2', '4', '1');
INSERT INTO `finalexamnikola`.`orders` (`id`, `date`, `quantity`, `total_price`, `product_id`) VALUES ('2', '2023-11-11 16:54', '1', '1.5', '2');
INSERT INTO `finalexamnikola`.`orders` (`id`, `date`, `quantity`, `total_price`, `product_id`) VALUES ('3', '2022-10-18 11:42', '3', '6.9', '3');
