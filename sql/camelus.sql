-- MySQL Script generated by MySQL Workbench
-- 06/30/15 22:54:50
-- Model: New Model    Version: 1.0
-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema camelus
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema camelus
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `camelus` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci ;
USE `camelus` ;

-- -----------------------------------------------------
-- Table `camelus`.`units`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `camelus`.`units` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `description` VARCHAR(45) NOT NULL,
  `deleted` TINYINT(1) NULL DEFAULT 0,
  `updated_at` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  `created_at` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `camelus`.`categories`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `camelus`.`categories` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `description` VARCHAR(45) NOT NULL,
  `deleted` TINYINT(1) NULL DEFAULT 0,
  `updated_at` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  `created_at` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `camelus`.`products`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `camelus`.`products` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `upc` VARCHAR(12) NOT NULL,
  `name` VARCHAR(45) NOT NULL,
  `quantity` INT NOT NULL,
  `cost_price` DECIMAL(10,2) NOT NULL,
  `selling_price` DECIMAL(10,2) NULL,
  `unit_id` INT NULL,
  `description` VARCHAR(45) NULL,
  `category_id` INT NULL,
  `img` LONGBLOB NULL,
  `deleted` TINYINT(1) NULL DEFAULT 0,
  `updated_at` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  `created_at` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  INDEX `FK_PRODUCTS_UNITS_ID_idx` (`unit_id` ASC),
  INDEX `FK_PRODUCTS_CATEGORIES_ID_idx` (`category_id` ASC),
  CONSTRAINT `FK_PRODUCTS_UNITS_ID`
    FOREIGN KEY (`unit_id`)
    REFERENCES `camelus`.`units` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_PRODUCTS_CATEGORIES_ID`
    FOREIGN KEY (`category_id`)
    REFERENCES `camelus`.`categories` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `camelus`.`commissions`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `camelus`.`commissions` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `type` TINYINT(1) NOT NULL,
  `rate` DECIMAL(10,2)  NOT NULL,
  `mcondition` DECIMAL(10,2)  NOT NULL,
  `deleted` TINYINT(1) NULL DEFAULT 0,
  `updated_at` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  `created_at` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `camelus`.`vendors`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `camelus`.`vendors` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `login` VARCHAR(45) NOT NULL,
  `password` VARCHAR(45) NOT NULL,
  `fname` VARCHAR(45) NOT NULL,
  `lname` VARCHAR(45) NOT NULL,
  `hire_date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `sin` VARCHAR(9) NOT NULL,
  `commission_id` INT NOT NULL,
  `deleted` TINYINT(1) NULL DEFAULT 0,
  `updated_at` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  `created_at` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  INDEX `FK_EMPLOYEES_COMMISSIONS_ID_idx` (`commission_id` ASC),
  CONSTRAINT `FK_VENDORS_COMMISSIONS_ID`
    FOREIGN KEY (`commission_id`)
    REFERENCES `camelus`.`commissions` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `camelus`.`clients`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `camelus`.`clients` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `associated_vendor` INT NOT NULL,
  `enterprise_name` VARCHAR(45) NOT NULL,
  `contact_name` VARCHAR(45) NOT NULL,
  `contact_tel` VARCHAR(45) NOT NULL,
  `contact_email` VARCHAR(45) NOT NULL,
  `address` VARCHAR(45) NOT NULL,
  `description` VARCHAR(255) NULL,
  `deleted` TINYINT(1) NULL DEFAULT 0,
  `updated_at` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  `created_at` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  INDEX `FK_CLIENTS_VENDORS_ID_idx` (`associated_vendor` ASC),
  CONSTRAINT `FK_CLIENTS_VENDORS_ID`
    FOREIGN KEY (`associated_vendor`)
    REFERENCES `camelus`.`vendors` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `camelus`.`orders`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `camelus`.`orders` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `vendor_id` INT NOT NULL,
  `client_id` BIGINT NOT NULL,
  `comment` VARCHAR(255) NULL,
  `status` TINYINT(1) NULL DEFAULT 0,
  `ordered_at` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  INDEX `FK_ORDERS_CLIENTS_ID_idx` (`client_id` ASC),
  INDEX `FK_ORDERS_VENDORS_ID_idx` (`vendor_id` ASC),
  CONSTRAINT `FK_ORDERS_CLIENTS_ID`
    FOREIGN KEY (`client_id`)
    REFERENCES `camelus`.`clients` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_ORDERS_VENDORS_ID`
    FOREIGN KEY (`vendor_id`)
    REFERENCES `camelus`.`vendors` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `camelus`.`order_lines`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `camelus`.`order_lines` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `product_id` BIGINT NOT NULL,
  `order_id` BIGINT NOT NULL,
  `quantity` INT NOT NULL,
  `modified_price` DECIMAL(10,2) NULL,
  PRIMARY KEY (`id`),
  INDEX `FK_ORDERLINES_PRODUCTS_ID_idx` (`product_id` ASC),
  INDEX `FK_ORDERLINES_ORDERS_ID_idx` (`order_id` ASC),
  CONSTRAINT `FK_ORDERLINES_PRODUCTS_ID`
    FOREIGN KEY (`product_id`)
    REFERENCES `camelus`.`products` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_ORDERLINES_ORDERS_ID`
    FOREIGN KEY (`order_id`)
    REFERENCES `camelus`.`orders` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `camelus`.`admins`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `camelus`.`admins` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `login` VARCHAR(45) NOT NULL,
  `password` VARCHAR(45) NOT NULL,
  `fname` VARCHAR(45) NOT NULL,
  `lname` VARCHAR(45) NOT NULL,
  `sin` VARCHAR(9) NOT NULL,
  `hire_date` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  `super_admin` TINYINT(1) NULL DEFAULT 0,
  `deleted` TINYINT(1) NULL DEFAULT 0,
  `updated_at` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  `created_at` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

-- DEFAULT CATEGORGIES AND UNITES

INSERT INTO categories (description) VALUES ("pas spécifié");
INSERT INTO units (description) VALUES ("pas spécifié");