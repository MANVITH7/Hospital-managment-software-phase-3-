CREATE DATABASE IF NOT EXISTS HOSPITALS;
USE HOSPITALS;
CREATE TABLE IF NOT EXISTS Patients(
    patient_id Integer(60) PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100),
    dob VARCHAR(30) NOT NULL,
    password VARCHAR(40)
);

CREATE TABLE IF NOT EXISTS Uchiha(
    patient_id INT PRIMARY KEY,
    prevhealth VARCHAR(100),
    prev_presc VARCHAR(100),
    immun VARCHAR(100),
    weight VARCHAR(50) NOT NULL,
    height VARCHAR(50) NOT NULL,
    Blood_pressure VARCHAR(50),
    body_temperature VARCHAR(50)
);


CREATE TABLE IF NOT EXISTS Prescriptions(
    patient_id INT(10) ,
    diagnosis VARCHAR(50) ,
    prescription VARCHAR(50) 
);




