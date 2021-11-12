-- phpMyAdmin SQL Dump
-- version 4.8.2
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Nov 14, 2018 at 02:33 PM
-- Server version: 10.1.34-MariaDB
-- PHP Version: 7.2.8

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `book_information`
--

-- --------------------------------------------------------

--
-- Table structure for table `author_book_table`
--

CREATE TABLE `author_book_table` (
  `author_book_id` int(11) NOT NULL,
  `author_id` int(11) DEFAULT NULL,
  `book_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `author_book_table`
--

INSERT INTO `author_book_table` (`author_book_id`, `author_id`, `book_id`) VALUES
(12, 1, 1),
(14, 2, 1),
(17, 3, 1),
(20, 1, 2),
(21, 2, 2),
(22, 3, 2);

-- --------------------------------------------------------

--
-- Table structure for table `author_table`
--

CREATE TABLE `author_table` (
  `author_id` int(11) NOT NULL,
  `author_name` varchar(45) DEFAULT NULL,
  `author_gender` varchar(45) DEFAULT NULL,
  `author_birthdate` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `author_table`
--

INSERT INTO `author_table` (`author_id`, `author_name`, `author_gender`, `author_birthdate`) VALUES
(1, 'Abrasaldo, Adrian Manuel Oraye', 'Male', '2018-11-14'),
(2, 'Abrasaldo, PJ', 'Male', '2018-11-14'),
(3, 'Abrasaldo, Nico', 'Male', '2018-11-14');

-- --------------------------------------------------------

--
-- Table structure for table `book_table`
--

CREATE TABLE `book_table` (
  `book_id` int(11) NOT NULL,
  `book_isbn` longtext,
  `book_title` longtext,
  `book_description` longtext,
  `book_date` date DEFAULT NULL,
  `book_major` varchar(45) DEFAULT NULL,
  `book_genres` varchar(45) DEFAULT NULL,
  `book_color` varchar(45) DEFAULT NULL,
  `book_noOfPages` int(11) DEFAULT NULL,
  `book_shelf` varchar(45) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `book_table`
--

INSERT INTO `book_table` (`book_id`, `book_isbn`, `book_title`, `book_description`, `book_date`, `book_major`, `book_genres`, `book_color`, `book_noOfPages`, `book_shelf`) VALUES
(2, '567-231-568-789', 'Book 2: Harr Potter and the Chamber of Secrets', 'Book 2 Of Harry Potter', '2018-11-14', 'N/A', 'N/A', 'N/A', 0, 'N/A'),
(3, '89789-234234-123123', 'Book 3: Harry Potter adn the Prisoner of Askaban', 'Description', '2018-11-14', 'Novel', 'Comedy', 'N/A', 250, 'Circulation');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `author_book_table`
--
ALTER TABLE `author_book_table`
  ADD PRIMARY KEY (`author_book_id`);

--
-- Indexes for table `author_table`
--
ALTER TABLE `author_table`
  ADD PRIMARY KEY (`author_id`);

--
-- Indexes for table `book_table`
--
ALTER TABLE `book_table`
  ADD PRIMARY KEY (`book_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `author_book_table`
--
ALTER TABLE `author_book_table`
  MODIFY `author_book_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=23;

--
-- AUTO_INCREMENT for table `author_table`
--
ALTER TABLE `author_table`
  MODIFY `author_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `book_table`
--
ALTER TABLE `book_table`
  MODIFY `book_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
