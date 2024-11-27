-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: Nov 18, 2024 at 03:11 AM
-- Server version: 10.4.28-MariaDB
-- PHP Version: 8.2.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
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
-- Table structure for table `accesslevel`
--

CREATE TABLE `accesslevel` (
  `userID` int(255) NOT NULL,
  `username` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `fullname` varchar(255) NOT NULL,
  `role` varchar(255) NOT NULL,
  `logStatus` int(1) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Dumping data for table `accesslevel`
--

INSERT INTO `accesslevel` (`userID`, `username`, `password`, `fullname`, `role`, `logStatus`) VALUES
(1, 'admin', 'admin', 'ADMIN USER', 'Admin', 0),
(6, 'cashier', 'cashier', 'SAMPLE CASHIER', 'Cashier', 0);

-- --------------------------------------------------------

--
-- Table structure for table `accountingaccounts`
--

CREATE TABLE `accountingaccounts` (
  `accountID` int(255) NOT NULL,
  `accountName` varchar(255) NOT NULL,
  `accountType` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Dumping data for table `accountingaccounts`
--

INSERT INTO `accountingaccounts` (`accountID`, `accountName`, `accountType`) VALUES
(1, 'Sample', 'Credit');

-- --------------------------------------------------------

--
-- Table structure for table `basicinformation`
--

CREATE TABLE `basicinformation` (
  `PhotoID` longblob NOT NULL,
  `EmployeeID` varchar(50) NOT NULL,
  `Lastname` varchar(255) NOT NULL,
  `Firstname` varchar(255) NOT NULL,
  `Middlename` varchar(255) NOT NULL,
  `Address` varchar(255) DEFAULT NULL,
  `Course` varchar(255) DEFAULT NULL,
  `Birthday` varchar(255) DEFAULT NULL,
  `Gender` varchar(50) DEFAULT NULL,
  `CivilStatus` varchar(50) DEFAULT NULL,
  `ContactNumber` varchar(255) DEFAULT NULL,
  `Status` varchar(255) NOT NULL DEFAULT 'Active',
  `ResignationDate` varchar(255) NOT NULL DEFAULT 'N/A'
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `credit_sales`
--

CREATE TABLE `credit_sales` (
  `salesID` int(255) NOT NULL,
  `salesDate` date NOT NULL,
  `timeGenerated` timestamp NOT NULL DEFAULT current_timestamp(),
  `customerID` int(255) NOT NULL,
  `Quantity` int(255) NOT NULL,
  `menuItem` varchar(255) NOT NULL,
  `menuID` int(255) NOT NULL,
  `salesCashier` varchar(255) NOT NULL,
  `recieptNumber` varchar(255) NOT NULL,
  `salesAmount` double NOT NULL,
  `discount` double DEFAULT NULL,
  `status` varchar(255) NOT NULL DEFAULT 'Unpaid'
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Dumping data for table `credit_sales`
--

INSERT INTO `credit_sales` (`salesID`, `salesDate`, `timeGenerated`, `customerID`, `Quantity`, `menuItem`, `menuID`, `salesCashier`, `recieptNumber`, `salesAmount`, `discount`, `status`) VALUES
(1, '2024-03-10', '2024-03-09 16:03:13', 3, 3, 'Dermovate Ointment', 1, 'ADMIN USER', '20242100252', 1117.2, 22.8, 'Unpaid'),
(2, '2024-03-10', '2024-03-09 16:15:34', 3, 1, 'Dermovate Ointment', 1, 'ADMIN USER', '202421001532', 380, 0, 'Unpaid');

-- --------------------------------------------------------

--
-- Table structure for table `deductions`
--

CREATE TABLE `deductions` (
  `deductID` int(255) NOT NULL,
  `EmployeeID` varchar(255) NOT NULL,
  `payrollDate` varchar(255) NOT NULL,
  `deductionName` varchar(255) NOT NULL,
  `deductionAmount` double NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `department`
--

CREATE TABLE `department` (
  `deptID` int(8) NOT NULL,
  `DepartmentHead` varchar(255) NOT NULL,
  `DepartmentName` varchar(255) NOT NULL,
  `Specification` varchar(255) NOT NULL,
  `Location` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `emergencycontacts`
--

CREATE TABLE `emergencycontacts` (
  `eID` int(8) NOT NULL,
  `EmployeeID` varchar(255) NOT NULL,
  `ContactPerson` varchar(255) DEFAULT NULL,
  `Relation` varchar(255) DEFAULT NULL,
  `ContactNumber` varchar(255) DEFAULT NULL,
  `ContactAddress` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `employmentinformations`
--

CREATE TABLE `employmentinformations` (
  `eiID` int(8) NOT NULL,
  `EmployeeID` varchar(255) NOT NULL,
  `DateOfHire` varchar(255) NOT NULL,
  `Position` varchar(255) NOT NULL,
  `Department` varchar(255) DEFAULT NULL,
  `Salary` varchar(255) NOT NULL,
  `payrollMode` varchar(255) DEFAULT NULL,
  `Role` varchar(255) DEFAULT NULL,
  `PreviousEmployer` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `expenses`
--

CREATE TABLE `expenses` (
  `expenseID` int(255) NOT NULL,
  `merchant` varchar(255) NOT NULL,
  `orNumber` varchar(255) DEFAULT NULL,
  `expenseType` varchar(255) NOT NULL,
  `particular` varchar(255) NOT NULL,
  `purchaseDate` varchar(255) NOT NULL,
  `amount` double NOT NULL,
  `timeStamp` datetime NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `User` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `governmentid`
--

CREATE TABLE `governmentid` (
  `giID` int(8) NOT NULL,
  `EmployeeID` varchar(255) NOT NULL,
  `SSS` varchar(255) NOT NULL,
  `PhilHealth` varchar(255) NOT NULL,
  `HDMF` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `menus`
--

CREATE TABLE `menus` (
  `menuID` int(8) UNSIGNED NOT NULL,
  `menuCode` varchar(255) NOT NULL,
  `menuDesc` varchar(255) DEFAULT NULL,
  `origPrice` double DEFAULT 0,
  `menuPrice` double DEFAULT 0,
  `expiration` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Dumping data for table `menus`
--

INSERT INTO `menus` (`menuID`, `menuCode`, `menuDesc`, `origPrice`, `menuPrice`, `expiration`) VALUES
(1, '4800333160848', 'Dermovate Ointment', 350, 380, NULL),
(2, '242143955', 'Safe Guard 150ml', 20, 25, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `merchants`
--

CREATE TABLE `merchants` (
  `merchantID` int(255) NOT NULL,
  `merchantName` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `merchantNumber` varchar(255) DEFAULT NULL,
  `contactPerson` varchar(255) DEFAULT NULL,
  `merchantAddress` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Dumping data for table `merchants`
--

INSERT INTO `merchants` (`merchantID`, `merchantName`, `merchantNumber`, `contactPerson`, `merchantAddress`) VALUES
(2, 'kcc', '09095648686', 'christian suarez', 'koronadal city'),
(3, 'Gaisano Mall', '09638215464', 'ronnie fernadez', 'Koronadal City');

-- --------------------------------------------------------

--
-- Table structure for table `payrollsummary`
--

CREATE TABLE `payrollsummary` (
  `psID` int(255) NOT NULL,
  `payrollDate` varchar(255) NOT NULL,
  `empID` varchar(255) NOT NULL,
  `gross` double NOT NULL,
  `deductions` double DEFAULT NULL,
  `net` double NOT NULL,
  `MachineOperator` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `printer`
--

CREATE TABLE `printer` (
  `id` int(8) NOT NULL,
  `location` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Dumping data for table `printer`
--

INSERT INTO `printer` (`id`, `location`) VALUES
(1, 'Win32 Printer : JposPrinter');

-- --------------------------------------------------------

--
-- Table structure for table `programuser`
--

CREATE TABLE `programuser` (
  `UserID` int(8) NOT NULL,
  `Username` varchar(255) NOT NULL,
  `Password` varchar(255) NOT NULL,
  `FullName` varchar(255) NOT NULL,
  `Status` varchar(255) DEFAULT NULL,
  `UserType` varchar(255) NOT NULL,
  `EmailAddress` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Dumping data for table `programuser`
--

INSERT INTO `programuser` (`UserID`, `Username`, `Password`, `FullName`, `Status`, `UserType`, `EmailAddress`) VALUES
(3, 'admin', 'admin', 'Sebando, Michael Paul O.', 'Active', 'Admin', 'admin');

-- --------------------------------------------------------

--
-- Table structure for table `pulled_out_stck`
--

CREATE TABLE `pulled_out_stck` (
  `id` int(8) NOT NULL,
  `prod_id` int(8) NOT NULL,
  `quantity` int(8) NOT NULL,
  `notes` text NOT NULL,
  `added` datetime NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Dumping data for table `pulled_out_stck`
--

INSERT INTO `pulled_out_stck` (`id`, `prod_id`, `quantity`, `notes`, `added`) VALUES
(1, 1, 5, 'Transfer to Polomolok Branch', '2024-03-11 14:44:44');

-- --------------------------------------------------------

--
-- Table structure for table `registered_customer`
--

CREATE TABLE `registered_customer` (
  `id` int(11) NOT NULL,
  `full_name` varchar(255) NOT NULL,
  `contact_number` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Dumping data for table `registered_customer`
--

INSERT INTO `registered_customer` (`id`, `full_name`, `contact_number`) VALUES
(3, 'Sample Customer', '09123456789');

-- --------------------------------------------------------

--
-- Table structure for table `reg_keys`
--

CREATE TABLE `reg_keys` (
  `reg_key_id` int(255) NOT NULL,
  `reg_key` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Dumping data for table `reg_keys`
--

INSERT INTO `reg_keys` (`reg_key_id`, `reg_key`) VALUES
(1, 'PGD67-JN23K-JGVWW-KTHP4-GXR9G'),
(2, 'B9GN2-DXXQC-9DHKT-GGWCR-4X6XK'),
(3, '6PMNJ-Q33T3-VJQFJ-23D3H-6XVTX'),
(4, 'MT7YN-TMV9C-7DDX9-64W77-B7R4D');

-- --------------------------------------------------------

--
-- Table structure for table `sales`
--

CREATE TABLE `sales` (
  `salesID` int(255) NOT NULL,
  `salesDate` date NOT NULL,
  `timeGenerated` timestamp NOT NULL DEFAULT current_timestamp(),
  `customerID` int(255) NOT NULL,
  `Quantity` int(255) NOT NULL,
  `menuItem` varchar(255) NOT NULL,
  `menuID` int(255) NOT NULL,
  `salesCashier` varchar(255) NOT NULL,
  `recieptNumber` varchar(255) NOT NULL,
  `salesAmount` double NOT NULL,
  `discount` double DEFAULT NULL,
  `cash` double DEFAULT NULL,
  `customerChange` double DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Dumping data for table `sales`
--

INSERT INTO `sales` (`salesID`, `salesDate`, `timeGenerated`, `customerID`, `Quantity`, `menuItem`, `menuID`, `salesCashier`, `recieptNumber`, `salesAmount`, `discount`, `cash`, `customerChange`) VALUES
(5, '2024-03-09', '2024-03-09 14:39:55', 1, 1, 'Dermovate Ointment', 1, 'ADMIN USER', '202429223952', 380, 0, 400, 20),
(6, '2024-03-09', '2024-03-09 15:59:55', 2, 1, 'Dermovate Ointment', 1, 'ADMIN USER', '202429235953', 380, 0, 500, 120),
(7, '2024-03-10', '2024-03-09 16:07:20', 1, 1, 'Dermovate Ointment', 1, 'ADMIN USER', '20242100712', 372.4, 7.6000000000000005, 500, 2),
(8, '2024-03-10', '2024-03-09 16:09:44', 2, 4, 'Dermovate Ointment', 1, 'ADMIN USER', '20242100925', 1444, 76, 1500, 2),
(9, '2024-03-10', '2024-03-09 16:14:58', 3, 2, 'Dermovate Ointment', 1, 'ADMIN USER', '202421001454', 737.2, 22.8, 800, 62.8),
(10, '2024-03-10', '2024-03-09 16:22:36', 4, 17, 'Dermovate Ointment', 1, 'ADMIN USER', '202421002210', 6460, 0, 6500, 40),
(11, '2024-03-11', '2024-03-11 06:39:15', 1, 2, 'Dermovate Ointment', 1, 'ADMIN USER', '2024211143911', 760, 0, 1000, 240),
(12, '2024-03-11', '2024-03-11 06:41:39', 2, 4, 'Safe Guard 150ml', 2, 'ADMIN USER', '2024211144137', 100, 0, 100, 0),
(13, '2024-03-11', '2024-03-11 06:49:10', 3, 1, 'Dermovate Ointment', 1, 'ADMIN USER', '202421114498', 380, 0, 500, 120);

-- --------------------------------------------------------

--
-- Table structure for table `stck_begin_end`
--

CREATE TABLE `stck_begin_end` (
  `id` int(8) NOT NULL,
  `prod_id` int(8) NOT NULL,
  `begin` double NOT NULL,
  `end` double NOT NULL,
  `added` datetime NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Dumping data for table `stck_begin_end`
--

INSERT INTO `stck_begin_end` (`id`, `prod_id`, `begin`, `end`, `added`) VALUES
(3, 1, 47, 42, '2024-03-09 22:37:51'),
(4, 2, 11, 6, '2024-03-11 14:40:58');

-- --------------------------------------------------------

--
-- Table structure for table `stocks_addition`
--

CREATE TABLE `stocks_addition` (
  `sa_id` int(8) NOT NULL,
  `product_id` int(8) NOT NULL,
  `date_encoded` varchar(255) NOT NULL,
  `quantity` double NOT NULL,
  `added_by` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `stocks_expiration`
--

CREATE TABLE `stocks_expiration` (
  `id` int(255) NOT NULL,
  `product_id` int(255) NOT NULL,
  `quantity` int(255) NOT NULL,
  `original_price` varchar(255) NOT NULL,
  `expiration_date` varchar(255) NOT NULL,
  `lot_no` varchar(255) NOT NULL,
  `status` int(11) NOT NULL DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Dumping data for table `stocks_expiration`
--

INSERT INTO `stocks_expiration` (`id`, `product_id`, `quantity`, `original_price`, `expiration_date`, `lot_no`, `status`) VALUES
(5, 1, 10, '350.00', '2024-12-01', 'HX9X', 1),
(6, 1, 10, '360.00', '2024-12-20', 'RX10X', 1),
(7, 1, 50, '360.00', '2025-01-01', 'HX11X', 1),
(8, 2, 10, '20.00', '2025-01-01', 'LOT245', 1),
(9, 2, 5, '20.00', '2025-01-01', 'LOT245', 1),
(10, 1, 5, '350.00', '2024-03-24', 'LOT1234', 0);

-- --------------------------------------------------------

--
-- Table structure for table `stocks_inventory`
--

CREATE TABLE `stocks_inventory` (
  `si_id` int(255) NOT NULL,
  `prod_id` int(255) NOT NULL,
  `unit` varchar(255) NOT NULL,
  `available_stock` int(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Dumping data for table `stocks_inventory`
--

INSERT INTO `stocks_inventory` (`si_id`, `prod_id`, `unit`, `available_stock`) VALUES
(2, 1, 'Box', 42),
(3, 2, 'Box', 11);

-- --------------------------------------------------------

--
-- Table structure for table `store`
--

CREATE TABLE `store` (
  `storeID` int(11) NOT NULL,
  `storename` varchar(255) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `contactNumber` varchar(255) DEFAULT NULL,
  `TIN` varchar(255) DEFAULT NULL,
  `owner` varchar(255) DEFAULT NULL,
  `payrollType` varchar(255) DEFAULT NULL,
  `payrollDates` varchar(255) DEFAULT NULL,
  `logo` longblob DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Dumping data for table `store`
--

INSERT INTO `store` (`storeID`, `storename`, `address`, `email`, `contactNumber`, `TIN`, `owner`, `payrollType`, `payrollDates`, `logo`) VALUES
(6, 'POLARIS RX PHARMACY', 'TUPI, SOUTH COTABATO', 'polaris@gmail.com', '09123456789', '000-000-000-000', 'Poalris Owaner', NULL, NULL, 0x89504e470d0a1a0a0000000d49484452000000320000003208060000001e3f88b1000000017352474200aece1ce90000000473424954080808087c08648800000a10494441546881ed9a797055e515c07fdf7defe585904db6442524800a9284526ba92c2a2ac511c485160d8eb6028ab5d6b638b65aadcaf40f9762c76a475bb14ab12a200c5895a51604651547ab90c880b22410cda664cf5beefd4effb86fbbefdd974449fd8b332173b9f7dcf39ddf39e75b6fe0949c9253d29da8be36386cf5ada51ef42582ba1045015adb0f0450d48be65df1585b6b7ebcacaa2fdbed1390616be64d36b4ba45c174941a0c800802c47e49e43272a1a11191754ac9df6bae5fb6e3647df8c62067bcb120cb1f32e78ba83b9452a3620f7a0090884ea2ae683980e2699d93f9fce73397747e6b20256be65728e18fa08a4e06205957448e217277ed4d2fbdfaff05797576468927f75985bab92f0162cf6d1880276afdc17bb96e55a8cf41ce5cf393813e9df1ba524c74752ae68f209646b4d837a2f7a26a864229e5804d0088979bc876e5936b3fbf6179539f8194ac5e301a656d50a812bb6d490110ad9190890e5a8cc91dca9881c514650f4a54a3aae928555f56536b3563f8bdae0089d9d2c261243cb36edeaa4f4e1a64d0bfe6e5e4986a27a2cadc0040b0bac2e45a19cc1b7339d79d3b85a2dc21ddda1cbfec766a551bca50a40426a9dc04f698ba636ad3fcd7dbbab3e9ed09243ba4168ba2cc6e21a984c21add1962fea8692c1c3f9b3c7fff9ecc01d012ea007f241b69fa96dd8e008cf748bfc5c0cfbab3d96d464a56cfbd1c8c8dc90008e890c9e88c42164dfc29138796f60a0060e367ef3177d39f30b2328806c3ea0c0182f27b511e038c885bd1800968654d6f9cbf7ac3d706396bfd9d7eb3bde36314a3120100ac409873fd85acba6651da2c1c6f6de0586b0335ad0db4063b281d341c80c777af6477eba728af0701acd62ed65ef30786e60e66c3a7bb59b17f0b555f55a3323ce031509e48f9897cd210683f8f5f6e08bab597b6b4ccf68e8502a3e2238f9d6a313567aadcb410af7eb28595fbb7b0b37e3f780d3b5a0ab4256059289f17e5f722916c5c51fc7d2616950170dbf95773dbf95753d352cffa83bb5859b585ca966a54a60f65a83183fdd90b1be151377f5d3352b2f4e64ce9cf114415460100440b565b80d5d31f6042523955351ee1c17797b2b3e90046b4442265913c38c432db15e2c90b6fa7a2ecb274f1e4fe4d4b78eee0bfc16b20c8174d1dc648ee5ad595ac67b8bd6cf5a70251852212ef9022588130777d67962bc4acd71e6277db6778b232501e65672fa1c66d3b122d13fb5fd062fad917a48558b16f13cbab3623cae657c2e903fb59d7bbe9ba9796a62271488cac87c8d53e6e1937c3a17aacb58159af3d44bbd742798cf8bc90988d94490fc4b4283dad98dc347dece217eea4aaad0695e143192af2ae805001fc23593f2523f96b6fce5730c59ea022ce88a08326b3cfba28a55fcc7df351dabd61f0aa04c7c53503d1cc8a08dad454945ee20a0130b9a8dc1ebd14311f2281bd98a553327b04c9e9628a88f825a15111410261167cf74a87eecaaab7a96c3b0e1e4fa4217138ed06102bb76098e9674f00ec125a7f7097c37645f96548d08adb25662b33bf3d3fa51e534a4b448f5324ace344102de47833199a34632ff9681d86cf136bc8ad84e22516b9119dc93d06f76d5a4265e3616aba9a9834640cd3cf9910b35d5e3892a27e8338a69b512a3aafd8656ec014606ba22f291951c2e8782a2351b42c26168e71e8b5063ba8faf22844fb450f1948ce9691e96563fd7f39ae5bf06465b0bdb692964087a38da2dcc1a0c569cbbe1e9dec770a88162971346adfa474508943afb2e10878558a838ebe1503c0155079141876df523e4565fd21471b938acaed95b423b020a20b7b0401c974ac48639148d5241d802403b8463505b6bab9deb58d645b2292d2d9dd87df9425b5a47054d61f4650a814dde8a5b8da4aed2f716787e515a4ba92602b7aad849e41b448402539158b6a82940d198e8876cc15dd01c41fa5d14db21f8795145d8d0492355d3abb34a69405b0a3b6d2a1573a643812b687471dd6e870c250995042d12cc4cb224db9852cca0b473adad8577f38a95423ba5a1a7b04d19aaa94c9cc501c6b71d66f5e6636130b4b31db039ca972b97cc838acaed0d70710414c8b4943cbc9cb744eb6db6b3e8e4c88497d0f95722696525a0a5d852862bd420414d4743451d97098b2212362ba0bbe37938ac0a5cc193b95632df5ac7f6e37f8bddd9410aebb411d0c73c3d81f3afca869aee344b013a37f463ca8115ba2f4073d6644609b901839fb6595e161f9becd0edd19e74c60ced8a900ac3bb00b7cde34198846355aaa91e708123629cf2fe68671d31cb61fd9f222f854dc56acbf88b41b81fff408523777c551443e4c74445b1a095beca8de97ac1e9357f66d8aac7a7b0110853535b9968f97ae5be4b0d5126867dd819de0f5100f6ae43d2defb3706b738f2000da92e7a291d0a6454ec0c35397fc82adf39e728538d65c4f65d3117b91d70b000474d822d7f4f2c68d8b293ecd39bf3df2f6324e4897bd5972d8021159eae6832b488374bd28225fd92442c5b997c64ac84db655ef059f27de28dd009816564790c90347f1c68d8b197bfa590e5bdb8f7cc4d37bd6a27c1e070022889613edbed6656e3e785c3d7bf360386be6980108930578bf763febf7ef20180e51d0ff34f232b31dea8fbdfb329f767e11cb880e9ae84018312dc4d488a9d12113e90a7365f1787efd83d93c7ac51d14e40c70d86909b433eb9ff7d26204634745f1495410d14f857ffbce463797d31e3e64bf307b7066481f52902302625948d83e842b1b50c29cf2a9cc1835919ae67a66aeb807233b131074d862d28051dc73d14d00ecabfb8c61f905e46566332cbf30a58ca25273a28e8a571e646fcb510c9fd70110f969ebf0064770f756d793c76e8f8306fced47bf3244fe9cbc3c1753a32d0b422608a87e3e30ec3dbaee08f1f235bf67c6e849dd9976c8b6231f3167f943b4a820cab1418bb48b205affbcf3be4d7f4d67a3e793c667ae7d5384d8fed67daf11efe0b9210fd5bf59db2b8096403b0f6f5ec6d3efad41657923c148b0178759d9f1bbb72abab3d5e3496393782b0660ee522265519f9301a280a285bc8cec74a6625273a28e973edcc833bbd7d24c272a3b231d005acb9eaef057f37ab2d9ab43ecacbf5c75861f639b4246b801447d00c10a9a14fb07525e3082f28291c41481bd5f1c626fdd21aa5b1b204381d713d9fda5024446ac035d4a2671dfe62ffb040420e7f19983bc3eb51698ec00b0ffe35caa5b82681d5b9ac454057bc04f1891dc000044b3b3cbd057f50602d20dbf2e127aeb6067e082b12ffbfde1d381f3dc0012fcb1874fa562df43541480f4192066463fd9657e7e230fec69efad7fdfe8d35bfe133366093c89a8a10e80580692060437d8c4bd481ce0b8d6fa8ee0035b5effba3e7df3afbacfceccca6bd70b45b80724e72401da44cb6301abe509167df0ed7d0c4d94ecc5d38680e75685cc07353c6db9b9031c1551cf074c63098bde6a38193ffaf40f06b2174f2bc3521783948a500cf427615010e8d022d506526562bd13ba7f6b657a6ba7e4949c92be94ff01cc879338fe0646a80000000049454e44ae426082);

-- --------------------------------------------------------

--
-- Table structure for table `systemstatus`
--

CREATE TABLE `systemstatus` (
  `reg_key` varchar(255) NOT NULL,
  `company_name` varchar(255) NOT NULL,
  `pc_ip` varchar(255) DEFAULT NULL,
  `date_installed` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Dumping data for table `systemstatus`
--

INSERT INTO `systemstatus` (`reg_key`, `company_name`, `pc_ip`, `date_installed`) VALUES
('MT7YN-TMV9C-7DDX9-64W77-B7R4D', 'Polaris Rx Pharmacy', NULL, '2023-05-15');

-- --------------------------------------------------------

--
-- Table structure for table `voidsales`
--

CREATE TABLE `voidsales` (
  `salesID` int(255) NOT NULL,
  `salesDate` date NOT NULL,
  `timeGenerated` timestamp NOT NULL DEFAULT current_timestamp(),
  `customerID` int(255) NOT NULL,
  `Quantity` int(255) NOT NULL,
  `menuItem` varchar(255) NOT NULL,
  `salesCashier` varchar(255) NOT NULL,
  `recieptNumber` varchar(255) NOT NULL,
  `salesAmount` double NOT NULL,
  `discount` double DEFAULT NULL,
  `cash` double NOT NULL,
  `customerChange` double NOT NULL,
  `voidBy` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `workeddayscalculation`
--

CREATE TABLE `workeddayscalculation` (
  `payrollID` int(255) NOT NULL,
  `payrollDate` varchar(255) NOT NULL,
  `empID` varchar(255) NOT NULL,
  `WorkingType` varchar(255) NOT NULL,
  `WorkingHours` double NOT NULL,
  `Amount` double NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `accesslevel`
--
ALTER TABLE `accesslevel`
  ADD PRIMARY KEY (`userID`);

--
-- Indexes for table `accountingaccounts`
--
ALTER TABLE `accountingaccounts`
  ADD PRIMARY KEY (`accountID`);

--
-- Indexes for table `basicinformation`
--
ALTER TABLE `basicinformation`
  ADD PRIMARY KEY (`EmployeeID`);

--
-- Indexes for table `credit_sales`
--
ALTER TABLE `credit_sales`
  ADD PRIMARY KEY (`salesID`);

--
-- Indexes for table `deductions`
--
ALTER TABLE `deductions`
  ADD PRIMARY KEY (`deductID`);

--
-- Indexes for table `department`
--
ALTER TABLE `department`
  ADD PRIMARY KEY (`deptID`);

--
-- Indexes for table `emergencycontacts`
--
ALTER TABLE `emergencycontacts`
  ADD PRIMARY KEY (`eID`),
  ADD UNIQUE KEY `EmployeeID` (`EmployeeID`);

--
-- Indexes for table `employmentinformations`
--
ALTER TABLE `employmentinformations`
  ADD PRIMARY KEY (`eiID`),
  ADD UNIQUE KEY `EmployeeID` (`EmployeeID`);

--
-- Indexes for table `expenses`
--
ALTER TABLE `expenses`
  ADD PRIMARY KEY (`expenseID`);

--
-- Indexes for table `governmentid`
--
ALTER TABLE `governmentid`
  ADD PRIMARY KEY (`giID`),
  ADD UNIQUE KEY `EmployeeID` (`EmployeeID`);

--
-- Indexes for table `menus`
--
ALTER TABLE `menus`
  ADD PRIMARY KEY (`menuID`),
  ADD UNIQUE KEY `menuCode` (`menuCode`);

--
-- Indexes for table `merchants`
--
ALTER TABLE `merchants`
  ADD PRIMARY KEY (`merchantID`);

--
-- Indexes for table `payrollsummary`
--
ALTER TABLE `payrollsummary`
  ADD PRIMARY KEY (`psID`),
  ADD UNIQUE KEY `psID` (`psID`);

--
-- Indexes for table `programuser`
--
ALTER TABLE `programuser`
  ADD PRIMARY KEY (`UserID`);

--
-- Indexes for table `pulled_out_stck`
--
ALTER TABLE `pulled_out_stck`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `registered_customer`
--
ALTER TABLE `registered_customer`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `reg_keys`
--
ALTER TABLE `reg_keys`
  ADD PRIMARY KEY (`reg_key_id`);

--
-- Indexes for table `sales`
--
ALTER TABLE `sales`
  ADD PRIMARY KEY (`salesID`);

--
-- Indexes for table `stck_begin_end`
--
ALTER TABLE `stck_begin_end`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `stocks_addition`
--
ALTER TABLE `stocks_addition`
  ADD PRIMARY KEY (`sa_id`);

--
-- Indexes for table `stocks_expiration`
--
ALTER TABLE `stocks_expiration`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `stocks_inventory`
--
ALTER TABLE `stocks_inventory`
  ADD PRIMARY KEY (`si_id`);

--
-- Indexes for table `store`
--
ALTER TABLE `store`
  ADD PRIMARY KEY (`storeID`);

--
-- Indexes for table `systemstatus`
--
ALTER TABLE `systemstatus`
  ADD PRIMARY KEY (`reg_key`);

--
-- Indexes for table `voidsales`
--
ALTER TABLE `voidsales`
  ADD PRIMARY KEY (`salesID`);

--
-- Indexes for table `workeddayscalculation`
--
ALTER TABLE `workeddayscalculation`
  ADD PRIMARY KEY (`payrollID`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `accesslevel`
--
ALTER TABLE `accesslevel`
  MODIFY `userID` int(255) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `accountingaccounts`
--
ALTER TABLE `accountingaccounts`
  MODIFY `accountID` int(255) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `credit_sales`
--
ALTER TABLE `credit_sales`
  MODIFY `salesID` int(255) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `deductions`
--
ALTER TABLE `deductions`
  MODIFY `deductID` int(255) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `department`
--
ALTER TABLE `department`
  MODIFY `deptID` int(8) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `emergencycontacts`
--
ALTER TABLE `emergencycontacts`
  MODIFY `eID` int(8) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `employmentinformations`
--
ALTER TABLE `employmentinformations`
  MODIFY `eiID` int(8) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `expenses`
--
ALTER TABLE `expenses`
  MODIFY `expenseID` int(255) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `governmentid`
--
ALTER TABLE `governmentid`
  MODIFY `giID` int(8) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `menus`
--
ALTER TABLE `menus`
  MODIFY `menuID` int(8) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `merchants`
--
ALTER TABLE `merchants`
  MODIFY `merchantID` int(255) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `payrollsummary`
--
ALTER TABLE `payrollsummary`
  MODIFY `psID` int(255) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `programuser`
--
ALTER TABLE `programuser`
  MODIFY `UserID` int(8) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `pulled_out_stck`
--
ALTER TABLE `pulled_out_stck`
  MODIFY `id` int(8) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `registered_customer`
--
ALTER TABLE `registered_customer`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `reg_keys`
--
ALTER TABLE `reg_keys`
  MODIFY `reg_key_id` int(255) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `sales`
--
ALTER TABLE `sales`
  MODIFY `salesID` int(255) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;

--
-- AUTO_INCREMENT for table `stck_begin_end`
--
ALTER TABLE `stck_begin_end`
  MODIFY `id` int(8) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `stocks_addition`
--
ALTER TABLE `stocks_addition`
  MODIFY `sa_id` int(8) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `stocks_expiration`
--
ALTER TABLE `stocks_expiration`
  MODIFY `id` int(255) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT for table `stocks_inventory`
--
ALTER TABLE `stocks_inventory`
  MODIFY `si_id` int(255) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `store`
--
ALTER TABLE `store`
  MODIFY `storeID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `voidsales`
--
ALTER TABLE `voidsales`
  MODIFY `salesID` int(255) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `workeddayscalculation`
--
ALTER TABLE `workeddayscalculation`
  MODIFY `payrollID` int(255) NOT NULL AUTO_INCREMENT;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
