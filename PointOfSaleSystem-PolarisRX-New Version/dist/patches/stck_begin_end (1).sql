-- phpMyAdmin SQL Dump
-- version 4.8.0.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Mar 11, 2024 at 07:55 AM
-- Server version: 10.1.32-MariaDB
-- PHP Version: 7.2.5

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `conveniencestore`
--

-- --------------------------------------------------------

--
-- Table structure for table `stck_begin_end`
--

CREATE TABLE `stck_begin_end` (
  `id` int(8) NOT NULL,
  `prod_id` int(8) NOT NULL,
  `begin` double NOT NULL,
  `end` double NOT NULL,
  `added` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `stck_begin_end`
--

INSERT INTO `stck_begin_end` (`id`, `prod_id`, `begin`, `end`, `added`) VALUES
(3, 1, 47, 42, '2024-03-09 22:37:51'),
(4, 2, 11, 6, '2024-03-11 14:40:58');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `stck_begin_end`
--
ALTER TABLE `stck_begin_end`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `stck_begin_end`
--
ALTER TABLE `stck_begin_end`
  MODIFY `id` int(8) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
