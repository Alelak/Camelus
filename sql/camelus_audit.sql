-- MySQL Script generated by MySQL Workbench
-- 07/07/15 11:27:04
-- Model: New Model    Version: 1.0
-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema camelus_audit
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema camelus_audit
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `camelus_audits` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci ;
USE `camelus_audit` ;

-- -----------------------------------------------------
-- Table `camelus_audit`.`audits`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `camelus_audit`.`audits` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `action_user` VARCHAR(45) NOT NULL,
  `action_type` CHAR(1) NOT NULL,
   `description` VARCHAR(255) NULL,
  `created_at` TIMESTAMP  NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
