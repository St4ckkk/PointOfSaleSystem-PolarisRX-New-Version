-- phpMyAdmin SQL Dump
-- version 4.8.0.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Mar 11, 2024 at 07:56 AM
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
-- Table structure for table `pulled_out_stck`
--

CREATE TABLE `pulled_out_stck` (
  `id` int(8) NOT NULL,
  `prod_id` int(8) NOT NULL,
  `quantity` int(8) NOT NULL,
  `notes` text NOT NULL,
  `added` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `pulled_out_stck`
--

INSERT INTO `pulled_out_stck` (`id`, `prod_id`, `quantity`, `notes`, `added`) VALUES
(1, 1, 5, 'Transfer to Polomolok Branch', '2024-03-11 14:44:44');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `pulled_out_stck`
--
ALTER TABLE `pulled_out_stck`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `pulled_out_stck`
--
ALTER TABLE `pulled_out_stck`
  MODIFY `id` int(8) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
